package com.jsoft.cocimpl.orm.nutz;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.nutz.castor.Castors;
import org.nutz.dao.DatabaseMeta;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.EntityField;
import org.nutz.dao.entity.EntityMaker;
import org.nutz.dao.entity.EntityName;
import org.nutz.dao.entity.ErrorEntitySyntaxException;
import org.nutz.dao.entity.FieldType;
import org.nutz.dao.entity.Link;
import org.nutz.dao.entity.ValueAdapter;
import org.nutz.dao.entity.annotation.PK;
import org.nutz.dao.entity.next.FieldQuery;
import org.nutz.dao.sql.FieldAdapter;
import org.nutz.lang.Mirror;
import org.nutz.lang.segment.CharSegment;
import org.nutz.lang.segment.Segment;

import com.jsoft.cocimpl.orm.generator.INamingStrategy;
import com.jsoft.cocimpl.orm.generator.impl.TableEntityIdGenerator;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;
import com.jsoft.cocit.entityengine.field.IExtField;
import com.jsoft.cocit.orm.mapping.EnColumnMapping;
import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.SortUtil;
import com.jsoft.cocit.util.StringUtil;

/*
 * 注意:代码中的field表示JAVA类中的field；column表示数据表中的列
 * 
 *
 */
public class EnMappingMaker implements EntityMaker {

	private EnMappingHolder holder;

	private INamingStrategy namingStrategyForNormal;

	private INamingStrategy namingStrategyForEncoding;

	private Map<String, TableEntityIdGenerator> idGenerators;

	private static EnMappingMaker me;

	public static EnMappingMaker make() {
		if (me == null) {
			me = new EnMappingMaker();
		}

		return me;
	}

	private EnMappingMaker() {
		idGenerators = new HashMap();
	}

	/**
	 * 创建实体映射
	 */
	public Entity<?> make(DatabaseMeta db, Connection conn, Class<?> type) {
		return null;
	}

	private INamingStrategy getNamingStrategy(boolean encoding) {
		return encoding ? namingStrategyForEncoding : namingStrategyForNormal;

		// return namingStrategyForNormal;
	}

	/**
	 * 创建实体映射
	 * 
	 */
	public EnMapping<?> make(EnMapping<?> parentEntity, Class<?> classOfEntity) {
		EnMapping mapping = holder.getEnMapping(classOfEntity);
		if (mapping != null)
			return mapping;

		String displayNameOfEntity = ClassUtil.getDisplayName(classOfEntity);

		LogUtil.debug("EnMappingMaker.make......[classOfEntity: %s, parentEntity: %s]", displayNameOfEntity, parentEntity == null ? "<NULL>" : ClassUtil.getDisplayName(parentEntity.getType()));

		/*
		 * 表名来自哪个类？有继承关系的多个实体类共用一张数据表时，表名一般通过父类产生。
		 */
		Class tableNameClass = this.getTableNameClass(classOfEntity);
		LogUtil.debug("EnMappingMaker.make: 解析表名来自哪个类？tableNameClass= %s [classOfEntity: %s]", tableNameClass, displayNameOfEntity);
		if (tableNameClass == null) {
			return null;
		}

		/*
		 * 检查是否需要对数据表和列名进行编码
		 */
		boolean dbEncoding = false;
		CocEntity $CocEntity = (CocEntity) tableNameClass.getAnnotation(CocEntity.class);
		if ($CocEntity != null) {
			dbEncoding = $CocEntity.dbEncoding();
		}
		INamingStrategy namingStrategy = getNamingStrategy(dbEncoding);

		/*
		 * 创建实体映射
		 */
		EnMappingImpl<?> entityMapping = new EnMappingImpl<Object>((EnMappingImpl) parentEntity);
		holder.cacheEntity(classOfEntity, entityMapping);

		Mirror<?> entityClassMirror = Mirror.me(classOfEntity);
		entityMapping.setMirror(entityClassMirror);
		entityMapping.setNaming(namingStrategy);

		/*
		 * 计算数据表名
		 */
		EntityName tableName = makeTableName(namingStrategy, tableNameClass);
		entityMapping.setTableName(tableName);
		entityMapping.setViewName(tableName);
		LogUtil.trace("EnMappingMaker.make: 计算数据表名！tableName=%s [classOfEntity: %s]", tableName, classOfEntity);

		/*
		 * 
		 */
		List<FieldQuery> befores = new ArrayList<FieldQuery>(5);
		List<FieldQuery> afters = new ArrayList<FieldQuery>(5);

		/*
		 * 创建DType字段:用于具有继承关系的实体类公用一张数据表
		 */
		EnColumnMappingImpl dtype = this.makeDTypeColumnMapping(namingStrategy, entityClassMirror, entityMapping);
		if (dtype != null) {
			entityMapping.addField(dtype);
			entityMapping.setDtype(dtype);
		}
		LogUtil.trace("EnMappingMaker.make: 计算DType字段(用于具有继承关系的多个实体类共用一张数据表)！dtype=%s [classOfEntity: %s]", dtype, displayNameOfEntity);

		/*
		 * 计算复合主键字段
		 */
		HashMap<String, EntityField> pkFieldMap = new HashMap<String, EntityField>();
		PK $PK = classOfEntity.getAnnotation(PK.class);
		if (null != $PK) {
			for (String pkName : $PK.value()) {
				pkFieldMap.put(pkName, null);
			}
		}

		/*
		 * 解析实体类字段
		 */
		Field[] fields = entityClassMirror.getFields();
		LogUtil.debug("EnMappingMaker.make: 解析实体类字段... [classOfEntity: %s, fields.length: %s]", displayNameOfEntity, fields.length);
		for (Field field : fields) {
			LogUtil.debug("EnMappingMaker.make: 解析实体类字段(field=%s)... [classOfEntity: %s]", field.getName(), displayNameOfEntity);

			try {

				/*
				 * 创建外键字段Link
				 */
				Link link = this.makeLink(namingStrategy, entityClassMirror, entityMapping, field);
				if (link != null) {
					entityMapping.addLinks(link);
					if (!link.isOne()) {
						continue;
					}
				}

				/*
				 * 创建字段映射
				 */
				EntityField columnMapping = makeColumnMapping(namingStrategy, entityClassMirror, entityMapping, field, link);
				if (null != columnMapping) {
					if (pkFieldMap.containsKey(columnMapping.getName())) {
						pkFieldMap.put(columnMapping.getName(), columnMapping);
						if (!(columnMapping.isId() || columnMapping.isName()))
							columnMapping.setType(FieldType.PK);
					}

					if (null != columnMapping.getBeforeInsert()) {
						befores.add(columnMapping.getBeforeInsert());
					} else if (null != columnMapping.getAfterInsert()) {
						afters.add(columnMapping.getAfterInsert());
					}

					entityMapping.addField(columnMapping);
				}

			} catch (Exception e) {
				LogUtil.error("解析%s实体: 解析字段%s出错! %s", displayNameOfEntity, ClassUtil.getDisplayName(field), ExceptionUtil.msg(e));
			}

		}

		/*
		 * 设置主键字段
		 */
		if (pkFieldMap.size() > 0) {
			EntityField[] pks = new EntityField[pkFieldMap.size()];
			for (int i = 0; i < $PK.value().length; i++)
				pks[i] = pkFieldMap.get($PK.value()[i]);

			entityMapping.setPkFields(pks);
		}

		/*
		 * 解析唯一性校验和索引
		 */
		parseUniqueColumns(entityMapping, classOfEntity);
		parseIndexColumns(entityMapping, classOfEntity);

		LogUtil.debug("解析%s实体: 设置%s个before字段查询和%s个after字段查询", displayNameOfEntity, befores.size(), afters.size());
		entityMapping.setBefores(befores.toArray(new FieldQuery[befores.size()]));
		entityMapping.setAfters(afters.toArray(new FieldQuery[afters.size()]));
		EnBorning born = new EnBorning(entityMapping);
		LogUtil.debug("解析%s实体: 创建实体Borning[%s]", displayNameOfEntity, born);
		entityMapping.setBorning(born);

		if (LogUtil.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("解析" + displayNameOfEntity + "实体: 结束. ");
			sb.append("\n" + entityMapping.getTableName());
			sb.append(" [");
			Iterator<EntityField> columnMappings = entityMapping.fields().iterator();
			while (columnMappings.hasNext()) {
				EnColumnMappingImpl f = (EnColumnMappingImpl) columnMappings.next();
				sb.append("\n\t").append(f.getColumnName());
				if (f.getField() != null) {
					sb.append(" <" + f.getName() + " " + f.getField().getType().getSimpleName() + ">");
				} else {
					sb.append(" <" + f.getName() + " " + f.getSqlType() + ">");
				}
				String fk = f.getFkName();
				if (!StringUtil.isBlank(fk)) {
					sb.append("[");
					sb.append("fk: ").append(fk);
					sb.append("]");
				}
			}
			sb.append("\n]");
			LogUtil.info(sb);
		}

		return entityMapping;
	}

	private ErrorEntitySyntaxException error(Entity<?> entity, String fmt, Object... args) {
		return new ErrorEntitySyntaxException(String.format("[%s] : %s", null == entity ? "NULL" : entity.getType().getName(), String.format(fmt, args)));
	}

	private EnColumnMappingImpl makeDTypeColumnMapping(INamingStrategy namingStrategy, Mirror<?> mirror, EnMappingImpl entity) {

		EnColumnMappingImpl dtype = null;
		String name = null;
		String columnName = null;
		String sqlType = null;
		Segment columnValue = null;

		DiscriminatorColumn dtypeColumnAnnotation = mirror.getType().getAnnotation(DiscriminatorColumn.class);
		if (dtypeColumnAnnotation != null) {
			name = dtypeColumnAnnotation.name();
			if (StringUtil.isBlank(name)) {
				name = "DTYPE";
			}
			dtype = new EnColumnMappingImpl(holder, null, null);

			columnName = namingStrategy.propertyToColumnName(name);
			sqlType = dtypeColumnAnnotation.discriminatorType().name();
			String discriminatorValue = StringUtil.unqualify(this.makeEntityName(mirror.getType(), ""));
			columnValue = new CharSegment(String.valueOf(discriminatorValue.hashCode()));
		} else {
			EnColumnMappingImpl parentDTypeColumnMapping = null;
			if (entity.getParent() != null) {
				parentDTypeColumnMapping = entity.getParent().getDtype();
			}
			if (parentDTypeColumnMapping != null) {
				dtype = new EnColumnMappingImpl(holder, null, null);

				name = parentDTypeColumnMapping.getName();
				columnName = parentDTypeColumnMapping.getColumnName();
				sqlType = parentDTypeColumnMapping.getSqlType();
				String discriminatorValue = StringUtil.unqualify(this.makeEntityName(mirror.getType(), ""));
				columnValue = new CharSegment(String.valueOf(discriminatorValue.hashCode()));
			}
		}

		if (dtype != null) {
			dtype.setEntity(entity);
			dtype.setDefaultValue(columnValue);
			dtype.setNotNull(true);
			dtype.setName(name);
			dtype.setColumnName(columnName);
			dtype.setSqlType(sqlType);

			dtype.setValueGetter(makeDTypeValueGetter(dtype));
			dtype.setValueSetter(makeDTypeValueSetter(dtype));
			dtype.setFieldAdapter(new FieldAdapter() {
				public void set(PreparedStatement statement, Object obj, int[] is) throws SQLException {
					if (null == obj) {
						for (int i : is)
							statement.setNull(i, Types.INTEGER);
					} else {
						int v;
						if (obj instanceof Number)
							v = ((Number) obj).intValue();
						else
							v = Castors.me().castTo(obj.toString(), int.class);
						for (int i : is)
							statement.setInt(i, v);
					}
				}
			});
			dtype.setValueAdapter(new ValueAdapter() {
				public Object get(ResultSet rs, String colnm) throws SQLException {
					return rs.getInt(colnm);
				}
			});
		}

		LogUtil.trace("EnMappingMaker.makeDTypeColumnMapping: return = %s [name: %s, columnName: %s, sqlType: %s, columnValue: %s]", dtype, name, columnName, sqlType, columnValue);

		return dtype;
	}

	private FieldValueGetter makeDTypeValueGetter(final EnColumnMappingImpl dtype) {
		return new FieldValueGetter() {
			public Object get(Object obj) {
				return Long.parseLong(dtype.getDefaultValue(obj));
			}
		};
	}

	private FieldValueSetter makeDTypeValueSetter(final EnColumnMappingImpl dtype) {
		return new FieldValueSetter() {
			public void set(Object obj, Object value) {
			}
		};
	}

	private FieldValueSetter makeFieldValueSetter(final EnColumnMappingImpl culumnMapping) {
		final Mirror me = culumnMapping.getMirror();
		final Class type = me.getType();
		final String fieldName = culumnMapping.getName();

		if (IExtField.class.isAssignableFrom(type)) {
			return new FieldValueSetter() {
				public void set(Object obj, Object value) {

					ObjectUtil.setValue(obj, fieldName, me.born(value));

				}
			};
		}

		return null;
	}

	private FieldValueSetter makeManyToOneValueSetter(final EnColumnMappingImpl columnMapping, final Link link) {
		final String targetField = link.getTargetField().getName();
		final String propName = columnMapping.getName();

		return new FieldValueSetter() {
			public void set(Object obj, Object value) {
				boolean isNull = false;
				if (value == null) {
					isNull = true;
				}
				if (value instanceof String) {
					isNull = StringUtil.isBlank((String) value);
				}

				if (!isNull) {
					EnMappingImpl targetMapping = (EnMappingImpl) holder.getEnMapping(link.getTargetClass());
					Mirror targetAgentMirror = targetMapping.getLazyAgentMirror();

					Object fieldValue = targetAgentMirror.born();
					ObjectUtil.setValue(fieldValue, targetField, value);
					ObjectUtil.setValue(obj, propName, fieldValue);
				}
			}
		};
	}

	private FieldValueGetter makeFieldValueGetter(final EnColumnMappingImpl columnMapping) {
		final Mirror me = columnMapping.getMirror();
		if (IExtField.class.isAssignableFrom(me.getType())) {
			return new FieldValueGetter() {
				public Object get(Object obj) {
					IExtField fldvalue = ObjectUtil.getValue(obj, columnMapping.getName());

					if (fldvalue != null) {
						return fldvalue.toString();
					}

					return null;
				}
			};
		}

		return null;
	}

	private FieldValueGetter makeManyToOneValueGetter(final EnColumnMappingImpl columnMapping, final Link link) {
		return new FieldValueGetter() {
			public Object get(Object obj) {

				Object fieldValue = ObjectUtil.getValue(obj, columnMapping.getName());

				if (fieldValue != null) {
					return ObjectUtil.getValue(fieldValue, link.getTargetField().getName());
				}

				return null;
			}
		};
	}

	@SuppressWarnings("deprecation")
	private String makeColumnName(INamingStrategy namingStrategy, Mirror<?> entityMirror, Field field) {
		String columnName = "";
		String fieldName = field.getName();

		/*
		 * 计算数据表列名
		 */
		CocColumn cocColumnAnnotation = null;
		CocEntity cocEntityAnnotation = entityMirror.getType().getAnnotation(CocEntity.class);
		if (cocEntityAnnotation != null) {
			CocGroup[] cocGroups = cocEntityAnnotation.groups();
			for (CocGroup cocGroup : cocGroups) {
				for (CocColumn cc : cocGroup.fields()) {
					if (fieldName.equals(cc.field()) || fieldName.equals(cc.propName())) {
						cocColumnAnnotation = cc;
						break;
					}
				}
			}
		}
		if (cocColumnAnnotation != null) {
			columnName = cocColumnAnnotation.dbColumnName();
		}

		// 获取硬编码列名
		Column columnAnnotation = getColumnAnnotation(field);
		if (StringUtil.isBlank(columnName)) {
			// 将属性名转换为列名
			if (null == columnAnnotation || StringUtil.isBlank(columnAnnotation.name())) {
				String name = makeFieldName(field, fieldName);
				columnName = namingStrategy.propertyToColumnName(name);
			} else {// 将@Column(name="")转换为列名
				String name = columnAnnotation.name();
				columnName = namingStrategy.columnName(name);
			}
		}

		return columnName;
	}

	private EnColumnMappingImpl makeColumnMapping(INamingStrategy namingStrategy, Mirror<?> entityMirror, EnMappingImpl<?> entityMapping, Field field, Link link) throws NoSuchFieldException {
		if (field.getAnnotation(Transient.class) != null) {
			return null;
		}

		field.setAccessible(true);

		CocColumn $CocColumn = this.getCocColumnAnnotation(entityMirror, field);
		Column $Column = getColumnAnnotation(field);

		Mirror fkTargetMirror = null;
		Field fkTargetField = null;
		if (link != null) {
			fkTargetMirror = Mirror.me(link.getTargetClass());
			fkTargetField = this.getFkTargetField(fkTargetMirror, entityMirror, field);
		}

		/*
		 * 定义字段映射属性
		 */
		String columnName = "";
		boolean readonly = false;
		boolean notnull = false;
		String columnDefinition = "";
		int length = 0;
		int precision = 0;
		int scale = 0;
		CharSegment defaultValue = new CharSegment();

		/*
		 * 计算列名
		 */
		columnName = this.makeColumnName(namingStrategy, entityMirror, field);

		/*
		 * 
		 */
		if ($Column != null) {
			readonly = !$Column.insertable() && !$Column.updatable();
			notnull = !$Column.nullable();
		}
		if (!notnull && $CocColumn != null) {
			notnull = !$CocColumn.nullable();
			readonly = $CocColumn.readonly();
		}

		/*
		 * 计算列定义类型： columnDefinition
		 */
		if ($CocColumn != null) {
			columnDefinition = $CocColumn.dbColumnDefinition();
		}
		if (StringUtil.isBlank(columnDefinition) && $Column != null) {
			columnDefinition = $Column.columnDefinition();
		}
		if (StringUtil.isBlank(columnDefinition) && fkTargetField != null) {
			columnDefinition = fkTargetField.getType().getSimpleName().toLowerCase();
		}

		/*
		 * 计算文本字段长度
		 */
		if ($CocColumn != null) {
			length = $CocColumn.length();
		}
		if (length <= 0 && $Column != null) {
			length = $Column.length();
		}
		if (length <= 0 && fkTargetField != null) {
			CocColumn $CocColumnOfTarget = this.getCocColumnAnnotation(fkTargetMirror, fkTargetField);
			Column $ColumnOfTarget = getColumnAnnotation(fkTargetField);
			if ($CocColumnOfTarget != null) {
				length = $CocColumnOfTarget.length();
			}
			if (length <= 0 && $ColumnOfTarget != null) {
				length = $ColumnOfTarget.length();
			}
		}
		if (length <= 0 && field.getType().equals(String.class)) {
			length = Const.ANN_COLUMN_DEFAULT_LENGTH;
		}

		/*
		 * 计算数值字段整数位精度
		 */
		if ($CocColumn != null) {
			precision = $CocColumn.precision();
		}
		if (precision <= 0 && $Column != null) {
			precision = $Column.precision();
		}

		/*
		 * 计算数值字段小数位数
		 */
		if ($CocColumn != null) {
			scale = $CocColumn.scale();
		}
		if (scale <= 0 && $Column != null) {
			scale = $Column.scale();
		}

		/*
		 * 计算默认值
		 */
		if ($CocColumn != null) {
			defaultValue = new CharSegment($CocColumn.defalutValue());
		}

		// refFieldMapping.setValueAdapter(new AsIdEntity(link.getTargetClass()));

		/*
		 * @Version
		 */
		Version version = field.getAnnotation(Version.class);
		if (version != null) {
			notnull = true;
		}

		/*
		 * 创建字段映射
		 */
		EnColumnMappingImpl columnMapping = new EnColumnMappingImpl(holder, entityMapping, field);
		columnMapping.setColumnName(columnName);
		columnMapping.setLength(length);
		columnMapping.setPrecision(precision);
		columnMapping.setScale(scale);
		columnMapping.setColumnDefinition(columnDefinition);
		columnMapping.setNotNull(notnull);
		columnMapping.setReadonly(readonly);
		columnMapping.setDefaultValue(defaultValue);

		/*
		 * 字段值Getter/Setter
		 */
		FieldValueGetter getter = this.makeFieldValueGetter(columnMapping);
		if (getter != null) {
			columnMapping.setValueGetter(getter);
		} else if (link != null) {
			columnMapping.setValueGetter(makeManyToOneValueGetter(columnMapping, link));
		}
		FieldValueSetter setter = this.makeFieldValueSetter(columnMapping);
		if (setter != null) {
			columnMapping.setValueSetter(setter);
		} else if (link != null) {
			columnMapping.setValueSetter(makeManyToOneValueSetter(columnMapping, link));
		}

		/*
		 * 字段值适配器
		 */
		columnMapping.setFieldAdapter(FieldAdapter.create(columnMapping.getMirror(), columnMapping.isEnumInt()));
		if (link == null) {
			columnMapping.setValueAdapter(ValueAdapter.create(columnMapping.getMirror(), columnMapping.isEnumInt()));
		} else {
			columnMapping.setLink(link);

			link.set(EnColumnMapping.class.getSimpleName(), columnMapping);
			link.set("to", fkTargetField.getName());

			columnMapping.setValueAdapter(ValueAdapter.create(columnMapping.getMirror(), columnMapping.isEnumInt()));
		}

		/*
		 * @Id
		 */
		Id id = getIdAnnotation(field);
		if (null != id) {
			if (!columnMapping.getMirror().isIntLike()) {
				throw error(entityMapping, "@Id 字段[%s]必需是一个数字型！", field.getName());
			}

			columnMapping.setType(FieldType.ID);

			EnMappingImpl parentEntity = entityMapping.getParent();
			if (parentEntity != null && parentEntity.getIdGenerator() != null) {
				entityMapping.setIdGenerator(parentEntity.getIdGenerator());
			} else {

				TableGenerator generatorAnnotation = field.getAnnotation(TableGenerator.class);
				if (generatorAnnotation != null) {
					TableEntityIdGenerator idGenerator = idGenerators.get(generatorAnnotation.table());

					if (idGenerator == null) {
						idGenerator = new TableEntityIdGenerator(generatorAnnotation.table(), generatorAnnotation.pkColumnName(), generatorAnnotation.valueColumnName());
						idGenerators.put(generatorAnnotation.table(), idGenerator);
					}

					entityMapping.setIdGenerator(idGenerator);
				}
			}
		}

		this.parseGenerator(entityMapping, columnMapping, $CocColumn);

		LogUtil.trace("EnMappingMaker.makeColumnMapping: classOfEntity = %s {field: %s, columnName: %s, length: %s, precision: %s, scale: %s, columnDefinition: %s, notnull: %s, readonly: %s, getter: %s, setter: %s}", //
		        entityMirror.getType(), field, columnName, length, precision, scale, columnDefinition, notnull, readonly, getter, setter);

		return columnMapping;
	}

	private Link makeLink(INamingStrategy namingStrategy, Mirror<?> entityClassMirror, Entity<?> entityMapping, Field field) {
		try {

			/*
			 * 一对一
			 */
			OneToOne one = field.getAnnotation(OneToOne.class);
			if (null != one) {
				Class targetClass = one.targetEntity();
				if (targetClass == null || targetClass == void.class) {
					targetClass = field.getType();
				}

				if (StringUtil.isBlank(one.mappedBy())) {

					Field selfFkField = field;// entityMirror.getField(field.getName());
					Field fkTargetField = getFkTargetField(Mirror.me(targetClass), entityClassMirror, selfFkField);

					Link link = Link.getLinkForOne(entityClassMirror, field, targetClass, selfFkField, fkTargetField);
					link.set("fetch", one.fetch());

					LogUtil.trace("EnMappingMaker.makeLink: @OneToOne，%s, 本实体的外键字段关联到目标实体的主键！[classOfEntity: %s, selfFkField: %s, targetClass: %s, targetPkField: %s]",//
					        field, entityClassMirror.getType(), selfFkField, targetClass, fkTargetField);

					return link;

				} else {

					Mirror targetClassMirror = Mirror.me(targetClass);
					Field targetFkField = targetClassMirror.getField(one.mappedBy());
					Field selfPkField = getFkTargetField(entityClassMirror, targetClassMirror, targetFkField);
					String key = null;

					Link link = Link.getLinkForMany(entityClassMirror, field, targetClass, targetFkField, selfPkField, key);
					link.set("mappedBy", one.mappedBy());
					link.set("fetch", one.fetch());

					LogUtil.trace("EnMappingMaker.makeLink: @OneToOne(mappedBy=%s)，%s, 目标实体的外键关联到本实体的主键！[classOfEntity: %s, selfPkField: %s, targetClass: %s, targetFkField: %s]",//
					        one.mappedBy(), field, entityClassMirror.getType(), selfPkField, targetClass, targetFkField);

					return link;

				}
			}

			/*
			 * 多对一
			 */
			ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
			if (null != manyToOne) {

				Class targetClass = manyToOne.targetEntity();
				if (targetClass == null || targetClass == void.class) {
					targetClass = field.getType();
				}

				Field selfFkField = field;// entityMirror.getField(field.getName());
				Field targetPkField = getFkTargetField(Mirror.me(targetClass), entityClassMirror, selfFkField);

				Link link = Link.getLinkForOne(entityClassMirror, field, targetClass, selfFkField, targetPkField);
				link.set("fetch", manyToOne.fetch());
				if (targetClass.equals(entityClassMirror.getType())) {
					link.set("linkEntity", entityMapping);
				} else {
					EnMapping linkEntity = holder.getEnMapping(targetClass);
					if (linkEntity == null) {
						linkEntity = this.make(null, targetClass);
					}
					link.set("linkEntity", linkEntity);
				}

				LogUtil.trace("EnMappingMaker.makeLink: @ManyToOne,%s, 目标实体的外键关联到本实体的主键！[classOfEntity: %s, selfFkField: %s, targetClass: %s, targetPkField: %s]",//
				        field, entityClassMirror.getType(), selfFkField, targetClass, targetPkField);

				return link;
			}

			/*
			 * 一对多
			 */
			OneToMany many = field.getAnnotation(OneToMany.class);
			if (null != many) {

				Class targetClass = many.targetEntity();
				if (targetClass == null || targetClass == void.class) {
					targetClass = getGenericType(field);
				}

				if (StringUtil.isBlank(many.mappedBy())) {
					throw ExceptionUtil.throwEx("EnMappingMaker.makeLink: @OneToMany(mappedBy=\"???\"),%s,非法注解！[classOfEntity: %s]", field, entityClassMirror.getType());
				}

				Mirror targetClassMirror = Mirror.me(targetClass);
				Field targetFkField = targetClassMirror.getField(many.mappedBy());
				Field selfPkField = getFkTargetField(entityClassMirror, targetClassMirror, targetFkField);
				String key = null;

				Link link = Link.getLinkForMany(entityClassMirror, field, targetClass, targetFkField, selfPkField, key);
				link.set("mappedBy", many.mappedBy());
				link.set("fetch", many.fetch());

				LogUtil.trace("EnMappingMaker.makeLink: @OneToMany(mappedBy=%s),%s, 目标实体的外键关联到本实体的主键！[classOfEntity: %s, selfPkField: %s, targetClass: %s, targetFkField: %s]",//
				        many.mappedBy(), field, entityClassMirror.getType(), selfPkField, targetClass, targetFkField);

				return link;
			}

			/*
			 * 多对多
			 */
			ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
			if (manyToMany != null) {

				Class selfClass = entityClassMirror.getType();
				Class targetClass = manyToMany.targetEntity();
				if (targetClass == null || targetClass == void.class) {
					targetClass = getGenericType(field);
				}
				Mirror targetMirror = Mirror.me(targetClass);
				Field[] targetFields = targetMirror.getFields(ManyToMany.class);
				Field selfField = field;
				Field targetField = null;

				// 实体类——用于生成中间表“表名”
				Class mainClass = null;
				Field mainFkField = null;

				Class minorClass = null;
				Field minorFkField = null;

				String mappedBy = manyToMany.mappedBy();
				if (StringUtil.isBlank(mappedBy)) {// 该实体是多对多的主体

					mainClass = selfClass;
					minorClass = targetClass;
					mainFkField = field;

					for (Field f : targetFields) {
						ManyToMany m2m = f.getAnnotation(ManyToMany.class);
						if (field.getName().equals(m2m.mappedBy())) {// 主对象字段名与目标多对多字段映射名相同
							minorFkField = f;
							targetField = f;
							break;
						}
					}

				} else {// 目标实体才是多对多的主体

					mainClass = targetClass;
					minorClass = selfClass;
					minorFkField = field;
					for (Field f : targetFields) {
						if (mappedBy.equals(f.getName())) {// 目标实体（多对多的主体）字段为该字段的映射
							mainFkField = f;
							targetField = f;
							break;
						}
					}

				}

				if (mainFkField == null) {
					LogUtil.error("EnMappingMaker.makeLink: @ManyToMany,%s,注解非法！[selfClass: %s, targetClass: %s, mainClass: %s, mainFkField: %s, minorClass: %s, minorFkField: %s]",//
					        field, selfClass, targetClass, mainClass, mainFkField, minorClass, minorFkField);

					throw ExceptionUtil.throwEx("EnMappingMaker.makeLink: @ManyToMany,%s, 注解非法！[selfClass: %s, targetClass: %s, mainClass: %s, mainFkField: %s, minorClass: %s, minorFkField: %s]",//
					        field, selfClass, targetClass, mainClass, mainFkField, minorClass, minorFkField);
				}

				String mainEntityName = makeEntityName(mainClass, "");
				String minorEntityName = makeEntityName(minorClass, "");

				Class mainTableNameClass = getTableNameClass(mainClass);
				String mainTableName = StringUtil.unqualify(makeEntityName(mainTableNameClass, ""));
				Class minorTableNameClass = getTableNameClass(minorClass);
				String minorTableName = StringUtil.unqualify(makeEntityName(minorTableNameClass, ""));

				String mainFkFieldName = makeFieldName(mainFkField, "");
				String selfFieldName = makeFieldName(minorFkField, "");

				String selfEntityName = makeEntityName(selfClass, "");
				Class selfTableNameClass = getTableNameClass(selfClass);
				String selfTableName = StringUtil.unqualify(makeEntityName(selfTableNameClass, ""));

				Field selfPkField = getFkTargetField(entityClassMirror, targetMirror, targetField);
				String selfPkFieldName = makeFieldName(selfPkField, selfPkField.getName());

				String selfLogicalColumnName = "";
				Column columnAnnotation = getColumnAnnotation(selfPkField);
				if (null == columnAnnotation || StringUtil.isBlank(columnAnnotation.name())) {
					selfLogicalColumnName = namingStrategy.logicalColumnName(null, selfPkFieldName);
				} else {
					selfLogicalColumnName = namingStrategy.logicalColumnName(columnAnnotation.name(), selfPkFieldName);
				}
				String fkColumnNameToSelf = namingStrategy.foreignKeyColumnName(selfFieldName, selfEntityName, selfTableName, selfLogicalColumnName);

				// 计算指向 目标POJO 主键的字段名
				String targetFieldName = makeFieldName(selfField, "");
				String targetEntityName = makeEntityName(targetClass, "");

				Class targetTableNameClass = getTableNameClass(targetClass);
				String targetTableName = StringUtil.unqualify(makeEntityName(targetTableNameClass, ""));
				Field targetPkField = getFkTargetField(targetMirror, entityClassMirror, selfField);
				String targetPkFieldName = makeFieldName(targetPkField, targetPkField.getName());

				String toLogicalColumnName = "";
				columnAnnotation = getColumnAnnotation(targetPkField);
				if (null == columnAnnotation || StringUtil.isBlank(columnAnnotation.name())) {
					toLogicalColumnName = namingStrategy.logicalColumnName(null, targetPkFieldName);
				} else {
					toLogicalColumnName = namingStrategy.logicalColumnName(columnAnnotation.name(), targetPkFieldName);
				}
				String fkColumnNameToTarget = namingStrategy.foreignKeyColumnName(targetFieldName, targetEntityName, targetTableName, toLogicalColumnName);

				/*
				 * 计算中间表名称
				 */
				String relationTableName = namingStrategy.collectionTableName(mainEntityName, mainTableName, minorEntityName, minorTableName, mainFkFieldName);

				// 创建多对多Link对象
				Link link = Link.getLinkForManyMany(entityClassMirror, field, targetClass, selfPkField, targetPkField, null, relationTableName, fkColumnNameToSelf, fkColumnNameToTarget);
				link.set("fetch", manyToMany.fetch());
				if (!StringUtil.isBlank(mappedBy)) {
					link.set("mappedBy", mappedBy);
				}

				return link;
			}

		} catch (Exception e) {
			throw ExceptionUtil.throwEx("Fail to eval linked field '%s' of class[%s] for the reason '%s'", field.getName(), entityClassMirror.getType().getName(), ExceptionUtil.msg(e));
		}

		return null;
	}

	private void parseGenerator(EnMappingImpl mapping, EnColumnMappingImpl column, CocColumn $CocColumn) {
		if ($CocColumn == null) {
			return;
		}

		String gname = $CocColumn.generator();
		if (StringUtil.hasContent(gname)) {
			column.setCocGenerator(gname);
			mapping.addGeneratorColumn(column);
		}
	}

	private Class getGenericType(Field field) {
		Class[] types = Mirror.getGenericTypes(field);
		if (types.length > 0) {
			return types[0];
		} else {
			return field.getType();
		}
	}

	private String makeFieldName(Field field, String defaultFieldName) {

		if (field == null) {
			return defaultFieldName;
		}

		if (StringUtil.isBlank(defaultFieldName)) {
			return field.getName();
		}

		return defaultFieldName;

	}

	private void parseUniqueColumns(EnMappingImpl mapping, Class type) {
		CocEntity ann = (CocEntity) type.getAnnotation(CocEntity.class);

		if (ann == null) {
			return;
		}

		String uniqueFields = ann.uniqueFields();
		if (StringUtil.isBlank(uniqueFields)) {
			return;
		}

		String[] array = StringUtil.toArray(uniqueFields, ";；");
		for (String str : array) {
			String[] fields = StringUtil.toArray(str, " ,，");
			String[] columns = new String[fields.length];
			for (int i = 0; i < fields.length; i++) {
				String f = fields[i];
				String fieldName = f;
				EnColumnMappingImpl column = (EnColumnMappingImpl) mapping.propsMap.get(f);
				if (column == null) {
					column = (EnColumnMappingImpl) mapping.fieldsMap.get(f);
				}

				if (column != null) {
					fieldName = column.getColumnName();
				}
				columns[i] = fieldName;
			}

			SortUtil.sort(columns, null, false);

			mapping.getUniqueFields().add(columns);
		}
	}

	private void parseIndexColumns(EnMappingImpl mapping, Class type) {
		CocEntity ann = (CocEntity) type.getAnnotation(CocEntity.class);

		if (ann == null)
			return;

		String indexFields = ann.indexFields();
		if (StringUtil.isBlank(indexFields)) {
			return;
		}

		String[] array = StringUtil.toArray(indexFields, ";；");
		for (String str : array) {
			String[] fields = StringUtil.toArray(str, " ,，");
			String[] columns = new String[fields.length];
			for (int i = 0; i < fields.length; i++) {
				String f = fields[i];
				String fieldName = f;
				EnColumnMappingImpl column = (EnColumnMappingImpl) mapping.propsMap.get(f);
				if (column == null) {
					column = (EnColumnMappingImpl) mapping.fieldsMap.get(f);
				}

				if (column != null) {
					fieldName = column.getColumnName();
				}
				columns[i] = fieldName;
			}

			SortUtil.sort(columns, null, false);

			mapping.getIndexFields().add(columns);
		}
	}

	private Annotation getAnnotation(Field field, Class annClas) {
		Annotation ret = field.getAnnotation(annClas);
		if (ret == null) {
			String name = field.getName();
			String getter = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
			try {
				Method m = field.getDeclaringClass().getMethod(getter);
				ret = m.getAnnotation(annClas);
			} catch (SecurityException e) {
				return null;
			} catch (NoSuchMethodException e) {
				return null;
			}
		}
		return ret;
	}

	@SuppressWarnings("deprecation")
	private CocColumn getCocColumnAnnotation(Mirror<?> entityMirror, Field fld) {
		CocEntity cocEntityAnnotation = entityMirror.getType().getAnnotation(CocEntity.class);
		if (cocEntityAnnotation != null) {
			CocGroup[] cocGroups = cocEntityAnnotation.groups();
			for (CocGroup cocGroup : cocGroups) {
				for (CocColumn cc : cocGroup.fields()) {
					if (fld.getName().equals(cc.field()) || fld.getName().equals(cc.propName())) {
						return cc;
					}
				}
			}
		}

		return (CocColumn) fld.getAnnotation(CocColumn.class);
	}

	private Column getColumnAnnotation(Field fld) {
		return (Column) getAnnotation(fld, Column.class);
	}

	private Id getIdAnnotation(Field fld) {
		return (Id) getAnnotation(fld, Id.class);
	}

	/**
	 * 获取主键字段：可以是物理主键，也可以是逻辑主键。
	 */
	private Field getFkTargetField(Mirror targetClassMirror, Mirror selfClassMirror, Field selfFkField) throws NoSuchFieldException {
		Field ret = null;

		CocColumn $CocColumn = this.getCocColumnAnnotation(selfClassMirror, selfFkField);

		String fkTargetField = null;
		if ($CocColumn != null)
			fkTargetField = $CocColumn.fkTargetField();
		if (StringUtil.hasContent(fkTargetField)) {
			ret = targetClassMirror.getField(fkTargetField);
		} else {
			ret = targetClassMirror.getField(Id.class);
			if (ret == null) {
				Method m = this.getMethod(targetClassMirror, Id.class);
				String name = m.getName();
				if (name.startsWith("get")) {
					name = name.replace("get", "");
					name = name.substring(0, 1).toLowerCase() + name.substring(1);
					ret = targetClassMirror.getField(name);
				}
			}
		}

		return ret;
	}

	public <AT extends Annotation> Method getMethod(Mirror mirror, Class<AT> ann) {
		for (Method method : mirror.getMethods()) {
			if (method.isAnnotationPresent(ann))
				return method;
		}
		return null;
	}

	/**
	 * 计算表名从哪个类产生？用于继承关系的实体类。
	 * <p>
	 * 目前只支持继承类型为单表的情况。
	 * <p>
	 * 对于每个类一个表和使用连接表的情况有待实现。
	 */
	private Class<?> getTableNameClass(Class classOfEntity) {
		if (classOfEntity == null)
			return null;

		// 代理类
		if (ClassUtil.isAgent(classOfEntity)) {
			classOfEntity = classOfEntity.getSuperclass();
		}

		javax.persistence.Entity entityAnnotation = (javax.persistence.Entity) classOfEntity.getAnnotation(javax.persistence.Entity.class);

		if (entityAnnotation != null) {

			Class<?> superClass = classOfEntity.getSuperclass();

			Class superClassOfEntity = getTableNameClass(superClass);
			if (superClassOfEntity == null) {
				return classOfEntity;
			} else {
				return superClassOfEntity;
			}
		}

		return null;
	}

	// private boolean isNeedEncodingDBObject(Class<?> type) {
	// javax.persistence.Table table = type.getAnnotation(javax.persistence.Table.class);
	// if (table != null && !StringUtil.isEmpty(table.name())) {
	// return false;
	// }
	//
	// return true;
	// }

	private EntityName makeTableName(INamingStrategy namingStrategy, Class<?> classOfEntity) {
		javax.persistence.Table tableAnnotation = classOfEntity.getAnnotation(javax.persistence.Table.class);

		String tableName = "";
		String entityName = "";

		if (tableAnnotation != null && !StringUtil.isBlank(tableAnnotation.name())) {
			tableName = namingStrategy.tableName(tableAnnotation.name());
		} else {
			entityName = makeEntityName(classOfEntity, classOfEntity.getName());
			tableName = namingStrategy.tableNameFromClassName(StringUtil.unqualify(entityName));
		}

		return EntityName.create(tableName);
	}

	private String makeEntityName(Class classOfEntity, String defaultEntityName) {

		if (classOfEntity == null) {
			return defaultEntityName;
		}

		CocEntity cocEntityAnnotation = (CocEntity) classOfEntity.getAnnotation(CocEntity.class);

		if (cocEntityAnnotation != null && !StringUtil.isBlank(cocEntityAnnotation.key())) {

			if (StringUtil.isBlank(cocEntityAnnotation.tableName())) {
				return cocEntityAnnotation.key();
			} else {
				return cocEntityAnnotation.tableName();
			}

		}

		if (StringUtil.isBlank(defaultEntityName)) {
			return classOfEntity.getName();
		}

		return defaultEntityName;
	}

	public void setHolder(EnMappingHolder holder) {
		this.holder = holder;
	}

	public void setNamingStrategy(INamingStrategy namingStrategy) {
		this.namingStrategyForNormal = namingStrategy;
	}

	public void setNamingStrategyForEncoding(INamingStrategy namingStrategyForEncoding) {
		this.namingStrategyForEncoding = namingStrategyForEncoding;
	}

	public INamingStrategy getNamingStrategy() {
		return namingStrategyForNormal;
	}
}
