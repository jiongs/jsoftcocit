package com.jsoft.cocimpl.orm.nutz.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.nutz.dao.Sqls;
import org.nutz.dao.entity.EntityField;
import org.nutz.dao.entity.Link;
import org.nutz.dao.sql.Sql;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.jsoft.cocimpl.orm.DMLSession;
import com.jsoft.cocimpl.orm.dialect.Dialect;
import com.jsoft.cocimpl.orm.metadata.ColumnMetadata;
import com.jsoft.cocimpl.orm.metadata.DatabaseMetadata;
import com.jsoft.cocimpl.orm.metadata.ForeignKeyMetadata;
import com.jsoft.cocimpl.orm.metadata.IndexMetadata;
import com.jsoft.cocimpl.orm.metadata.TableMetadata;
import com.jsoft.cocimpl.orm.nutz.EnColumnMappingImpl;
import com.jsoft.cocimpl.orm.nutz.EnMappingHolder;
import com.jsoft.cocimpl.orm.nutz.EnMappingImpl;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.field.IExtField;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.ConnCallback;
import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

public class DMLSessionImpl implements DMLSession {
	private ExtDaoImpl extDao;

	private Dialect dialect;

	private EnMappingHolder mappingHolder;

	DMLSessionImpl(ExtDaoImpl dao) {
		extDao = dao;
		dialect = extDao.getDialect();
		mappingHolder = extDao.getEntityHolder();
	}

	public int getTableCount() {
		return getTableMetadatas().size();
	}

	public int getColumnCount(EnMapping entity) {
		return getColumnMetadatas(entity).size();
	}

	public int getImportFKCount(EnMapping entity) {
		return getImportFKMetadatas(entity).size();
	}

	public int getExportFKCount(EnMapping entity) {
		return getExportFKMetadatas(entity).size();
	}

	private void execSqls(List<Sql> sqls, boolean igloreError) {
		if (sqls == null || sqls.size() == 0) {
			return;
		}
		LogUtil.debug("执行<%s>条SQL......", sqls.size());
		Sql sql = null;
		int len = sqls.size();
		for (int i = 0; i < len; i++) {
			sql = sqls.get(i);
			try {
				this.extDao.execute(sql);
			} catch (RuntimeException e) {
				if (!igloreError) {
					LogUtil.warn("执行<%s>SQL出错: %s", sqls.size(), ExceptionUtil.msg(e));
					throw e;
				} else {
					LogUtil.warn("执行SQL出错! %s", sql);
				}
			}
		}
		LogUtil.info("执行<%s>条SQL: 成功.", sqls.size());
	}

	public void clear() {
		LogUtil.debug("清空数据库......");

		final StringBuffer res = new StringBuffer();
		Trans.exec(new Atom() {
			public void run() {
				List<Sql> sqls = makeSqlToClearDB();
				res.append(sqls.size());
				execSqls(sqls, false);
			}
		});
		LogUtil.debug("清空数据库: 结束. [sqlSize: %s]", res);
	}

	public void createOrAlterTable(final EnMapping<?> entity, final boolean syncRefTable) {
		if (entity.isReadonly()) {
			return;
		}
		EnMappingImpl enImp = (EnMappingImpl) entity;

		String enInfo = ClassUtil.getDisplayName(entity.getType());
		LogUtil.debug("同步%s数据库表......[syncRefTable=%s]", enInfo, syncRefTable);

		try {
			Trans.exec(new Atom() {
				public void run() {
					createOrAlterTable_(entity, syncRefTable);
				}
			});

			LogUtil.debug("同步%s实体表: 结束.", enInfo);

			enImp.setSyncedTable(true);
			enImp.setSyncedRefTable(syncRefTable);
		} catch (Throwable e) {
			enImp.setSyncedTable(false);
			enImp.setSyncedRefTable(false);

			e.printStackTrace();

			LogUtil.error("同步%s实体表出错: [syncRefTable=%s] %s", ClassUtil.getDisplayName(entity.getType()), syncRefTable, ExceptionUtil.msg(e));
		}
	}

	private void createOrAlterTable_(EnMapping mapping, boolean alterFkTable) {
		EnMappingImpl entity = (EnMappingImpl) mapping;

		String enInfo = ClassUtil.getDisplayName(entity.getType());

		// 表已经存在: 检查外键、字段
		if (this.extDao.exists(entity.getTableName())) {
			LogUtil.debug("同步%s数据库表: 检查新增字段", enInfo);
			this.execSqls(makeSqlToCreateColumns(entity), false);

			LogUtil.debug("同步%s数据库表: 检查外键 [syncRefTable=%s]", enInfo, alterFkTable);
			this.execSqls(makeSqlToAlterFKs(entity, alterFkTable), true);

			LogUtil.debug("同步%s数据库表: 检查更新字段", enInfo);
			this.execSqls(makeSqlToAlterColumns(entity), true);

			LogUtil.debug("同步%s数据库表: 检查索引", enInfo);
			this.execSqls(makeSqlToAlterIndexs(entity), true);

			// TODO: 取消 删除字段 功能
			// this.execSqls(checkDropFields(entity));
		}
		// 实体表不存在: 创建表、主键、外键
		else {
			LogUtil.debug("同步%s数据库表: 创建数据库表 [syncRefTable=%s]", enInfo, alterFkTable);
			this.execSqls(makeSqlToCreateTableWithPkFkIndex(entity), false);

			LogUtil.debug("同步%s数据库表: 创建外键 [syncRefTable=%s]", enInfo, alterFkTable);
			this.execSqls(makeSqlToCreateFKs(entity, alterFkTable), false);
		}

		// 检查多对多的中间表
		List<Link> links = entity.getManyMany(null);
		LogUtil.debug("同步%s数据库表: 检查<%s>个多对多关联表... [syncRefTable=%s]", enInfo, links.size(), alterFkTable);
		for (Link link : links) {
			if (!extDao.exists(link.getRelation())) {
				LogUtil.debug("同步%s数据库表: 创建多对多关联表 [columnName=%s, syncRefTable=%s]", enInfo, ClassUtil.getDisplayName(link.getOwnField()), alterFkTable);
				this.execSqls(makeSqlToCreateTableAndRelation(entity, link, alterFkTable), false);
			} else
				LogUtil.debug("同步%s数据库表: <多对多关联表已经存在>", enInfo);
		}
		LogUtil.debug("同步%s数据库表: 检查<%s>个多对多关联表: 结束. [syncRefTable=%s]", enInfo, links.size(), alterFkTable);
	}

	public void dropTable(final EnMapping mapping) {
		if (mapping == null) {
			return;
		}
		String enInfo = ClassUtil.getDisplayName(mapping.getType());
		LogUtil.debug("删除%s数据库表......", enInfo);

		final EnMappingImpl entity = (EnMappingImpl) mapping;
		try {
			Trans.exec(new Atom() {
				public void run() {
					dropTables_(entity);
				}
			});

			LogUtil.debug("删除%s数据库表: 结束.", enInfo);
		} catch (Exception e) {
			LogUtil.warn("删除%s数据库表出错: %s", enInfo, e);
		}
	}

	private void dropTables_(EnMapping mapping) {
		EnMappingImpl entity = (EnMappingImpl) mapping;
		String enInfo = ClassUtil.getDisplayName(mapping.getType());

		// 删除实体多对多关联表
		List<Link> links = entity.getManyMany(null);
		LogUtil.debug("删除%s数据库表: 删除<%s>个多对多关联表...", enInfo, links.size());

		for (Link link : links) {
			if (extDao.exists(link.getRelation())) {
				this.execSqls(makeSqlToDropTableAndRelation(entity, link), false);
				LogUtil.debug("删除%s数据库表: 删除多对多关联表成功! [columnName=%s]", enInfo, ClassUtil.getDisplayName(link.getOwnField()));
			}
		}
		// 删除实体表
		if (extDao.exists(entity.getTableName())) {
			this.execSqls(makeSqlToDropTable(entity), false);
			LogUtil.debug("删除%s数据库表: 删除实体数据库表结束! ", enInfo);
		} else
			LogUtil.debug("删除%s数据库表: <实体数据库表不存在> ", enInfo);
	}

	private String toSqlType(EnColumnMappingImpl col) {
		String type = col.getSqlType();
		String sqlType;
		sqlType = toSqlType(type, col.getLength(), col.getPrecision(), col.getScale());
		if (LogUtil.isTraceEnabled()) {
			LogUtil.trace("SQL类型转换: %s %s [columnType=%s, length=%s, precision=%s, scale=%s]", col.getName(), sqlType, type, col.getLength(), col.getPrecision(), col.getScale());
		}
		return sqlType;
	}

	private String toSqlType(Field fld) {
		Column column = fld.getAnnotation(Column.class);
		int l = 0;
		int p = 0;
		int s = 0;
		if (column != null) {
			l = column.length();
			p = column.precision();
			s = column.scale();
		}
		Class type = fld.getType();
		String sqlType = this.toSqlType(type, l, p, s);
		if (LogUtil.isTraceEnabled()) {
			LogUtil.trace("SQL类型转换: %s [fieldName=%s,fieldType=%s, length=%s, precision=%s, scale=%s]", sqlType, fld.getName(), type, l, p, s);
		}
		return sqlType;
	}

	private String toSqlType(Class classOfFld, int l, int p, int s) {
		return toSqlType(classOfFld.getSimpleName(), l, p, s);
	}

	private String toSqlType(String srcType, int l, int p, int s) {
		String type = srcType.toLowerCase();
		if (type.equals("boolean"))
			return dialect.getTypeName(Types.BIT);
		if (type.equals("long")) {
			return dialect.getTypeName(Types.BIGINT, l, p, s);
		}
		if (type.equals("integer") || type.equals("int")) {
			return dialect.getTypeName(Types.INTEGER, l, p, s);
		}
		if (type.equals("short")) {
			return dialect.getTypeName(Types.SMALLINT);
		}
		if (type.equals("byte")) {
			return dialect.getTypeName(Types.TINYINT, l, p, s);
		}

		if (type.equals("double")) {
			return dialect.getTypeName(Types.DOUBLE, l, p, s);
		}
		if (type.equals("float")) {
			return dialect.getTypeName(Types.FLOAT, l, p, s);
		}

		if (type.equals("date")) {
			return dialect.getTypeName(Types.DATE);
		}

		if (type.equals("clob")) {
			return dialect.getTypeName(Types.CLOB);
		}
		if (type.equals("text")) {
			return dialect.getTypeName(Types.CLOB);
		}
		if (type.equals("bigdecimal")) {
			return dialect.getTypeName(Types.NUMERIC, l, p, s);
		}
		if (type.equals("biginteger")) {
			return dialect.getTypeName(Types.NUMERIC, l, p, s);
		}
		if (type.equals("character") || type.equals("char")) {
			return dialect.getTypeName(Types.CHAR, l, p, s);
		}
		if (type.equals("string")) {
			return dialect.getTypeName(Types.VARCHAR, l, p, s);
		}

		try {
			type = "String";

			Class extFieldType = ClassUtil.forName(IExtField.class.getPackage().getName() + "." + srcType);
			CocColumn extFieldAnn = (CocColumn) extFieldType.getAnnotation(CocColumn.class);
			if (extFieldAnn != null) {
				if (!StringUtil.isBlank(extFieldAnn.dbColumnDefinition()))
					type = extFieldAnn.dbColumnDefinition();
				if (extFieldAnn.length() > 0)
					l = extFieldAnn.length();
				p = extFieldAnn.precision();
				s = extFieldAnn.scale();
			}

			return toSqlType(type, l, p, s);
		} catch (Throwable e) {
			throw new CocException("非法字段类型! [%s] %s", type, e);
		}
	}

	private String makeSqlToCreateTable(EnMapping mapping) {
		EnMappingImpl entity = (EnMappingImpl) mapping;

		StringBuffer buf = new StringBuffer(dialect.getCreateTableString()).append(' ').append(entity.getTableName()).append(" (\n");

		Iterator<EnColumnMappingImpl> iter = entity.fields().iterator();
		while (iter.hasNext()) {
			EnColumnMappingImpl col = iter.next();
			buf.append("\t").append(col.getColumnName());
			buf.append(' ').append(toSqlType(col));

			String defaultValue = "";// col.getDefaultValue("");
			if (!StringUtil.isBlank(defaultValue)) {
				buf.append(" default ").append(defaultValue);
			}

			if (col.isNotNull() || col.isPk() || col.isId()) {
				buf.append(" not null");
			} else {
				buf.append(dialect.getNullColumnString());
			}

			if (col.hasCheckConstraint() && dialect.supportsColumnCheck()) {
				buf.append(" check (").append(col.getCheckConstraint()).append(")");
			}

			String columnComment = col.getComment();
			if (columnComment != null) {
				buf.append(dialect.getColumnComment(columnComment));
			}

			if (iter.hasNext()) {
				buf.append(",\n");
			}
		}

		if (dialect.supportsColumnCheck()) {
			List checkConstraints = (List) entity.get("checkConstraints");
			if (checkConstraints != null) {
				Iterator<String> chiter = checkConstraints.iterator();
				while (chiter.hasNext()) {
					buf.append(", check (").append(chiter.next()).append(')');
				}
			}
		}

		buf.append("\n)");

		if (entity.getString("comment") != null) {
			buf.append(dialect.getTableComment(entity.getString("comment")));
		}

		buf.append(dialect.getTableTypeString());

		return buf.toString();
	}

	private List<Sql> makeSqlToCreateTableWithPkFkIndex(EnMapping mapping) {
		String enInfo = ClassUtil.getDisplayName(mapping.getType());
		LogUtil.debug("同步%s数据库表: 创建生成SQL...", enInfo);

		EnMappingImpl entity = (EnMappingImpl) mapping;
		List<Sql> sqls = new ArrayList();
		StringBuffer logBuf = new StringBuffer();

		/*
		 * 创建实体表
		 */
		String sqlStr = this.makeSqlToCreateTable(entity);
		if (LogUtil.isInfoEnabled()) {
			logBuf.append(String.format("\n%s", sqlStr));
		}
		sqls.add(Sqls.create(sqlStr));

		/*
		 * 添加主键
		 */
		String[] primaryKey = null;
		if (entity.getIdField() != null) {
			primaryKey = new String[1];
			primaryKey[0] = entity.getIdField().getColumnName();
		} else {
			EntityField[] pks = entity.getPkFields();
			if (pks != null && pks.length > 0) {
				primaryKey = new String[pks.length];
				int count = 0;
				for (EntityField pk : pks) {
					primaryKey[count++] = pk.getColumnName();
				}
			}
		}
		sqlStr = dialect.sqlAddPk(entity.getTableName(), "PK_" + entity.getTableName(), primaryKey);
		if (!StringUtil.isBlank(sqlStr)) {
			if (LogUtil.isInfoEnabled()) {
				logBuf.append(String.format("\n%s", sqlStr));
			}
			sqls.add(Sqls.create(sqlStr));
		}

		/*
		 * 添加唯一性校验
		 */
		List<String[]> list = entity.getUniqueFields();
		for (String[] fields : list) {
			sqlStr = dialect.sqlAddUnique(entity.getTableName(), "UNIQUE_" + StringUtil.join(fields, "_", true), fields);
			if (!StringUtil.isBlank(sqlStr)) {
				if (LogUtil.isInfoEnabled()) {
					logBuf.append(String.format("\n%s", sqlStr));
				}
				sqls.add(Sqls.create(sqlStr));
			}
		}

		/*
		 * 添加索引
		 */
		list = entity.getIndexFields();
		for (String[] fields : list) {
			sqlStr = dialect.sqlAddIndex(entity.getTableName(), "INDEX_" + StringUtil.join(fields, "_", true), fields);
			if (!StringUtil.isBlank(sqlStr)) {
				if (LogUtil.isInfoEnabled()) {
					logBuf.append(String.format("\n%s", sqlStr));
				}
				sqls.add(Sqls.create(sqlStr));
			}
		}

		LogUtil.info("同步%s数据库表: 创建表生成SQL结束. %s", enInfo, logBuf);

		return sqls;
	}

	private List<Sql> makeSqlToDropTable(final EnMapping mapping) {

		return (List<Sql>) extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				String enInfo = ClassUtil.getDisplayName(mapping.getType());
				LogUtil.debug("删除%s数据库表: 生成SQL...", enInfo);

				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				List<String> sqlStrs = new ArrayList();
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableInfo = db.getTableMetadata(entity.getTableName());
				Iterator<ForeignKeyMetadata> fks = tableInfo.iteratorForeignKeyMetadatas();
				while (fks.hasNext()) {
					ForeignKeyMetadata fk = fks.next();
					String sqlStr = dialect.sqlDropFk(entity.getTableName(), fk.getName());
					if (sqlStrs.contains(sqlStr)) {
						continue;
					}
					sqlStrs.add(sqlStr);
					if (LogUtil.isInfoEnabled()) {
						logBuf.append(String.format("\nIMPORT FK: %s", sqlStr));
					}
					sqls.add(Sqls.create(sqlStr));
				}
				fks = tableInfo.iteratorForeignedKeyMetadatas();
				while (fks.hasNext()) {
					ForeignKeyMetadata fk = fks.next();
					String sqlStr = dialect.sqlDropFk(fk.getTableName(), fk.getName());
					if (sqlStrs.contains(sqlStr)) {
						continue;
					}
					sqlStrs.add(sqlStr);
					if (LogUtil.isInfoEnabled()) {
						logBuf.append(String.format("\nEXPORT FK: %s", sqlStr));
					}
					sqls.add(Sqls.create(sqlStr));
				}

				String sqlStr = dialect.sqlDropTable(entity.getTableName());
				if (LogUtil.isInfoEnabled()) {
					logBuf.append(String.format("\nTABLE: %s", sqlStr));
				}
				sqls.add(Sqls.create(sqlStr));

				LogUtil.info("删除%s数据库表: 生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}
		});
	}

	private List<Sql> makeSqlToDropTableAndRelation(final EnMapping mapping, final Link link) {

		return (List<Sql>) extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				String enInfo = ClassUtil.getDisplayName(mapping.getType());
				LogUtil.debug("删除%s多对多关联表: 生成SQL...[columnName=%s]", enInfo, ClassUtil.getDisplayName(link.getOwnField()));

				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableInfo = db.getTableMetadata(link.getRelation());
				Iterator<ForeignKeyMetadata> fks = tableInfo.iteratorForeignKeyMetadatas();
				while (fks.hasNext()) {
					ForeignKeyMetadata fk = fks.next();
					String sqlStr = dialect.sqlDropFk(link.getRelation(), fk.getName());
					if (LogUtil.isInfoEnabled())
						logBuf.append(String.format("\nFK: %s", sqlStr));

					sqls.add(Sqls.create(sqlStr));
				}

				String sqlStr = dialect.sqlDropTable(link.getRelation());
				if (LogUtil.isInfoEnabled())
					logBuf.append(String.format("\nTABLE: %s", sqlStr));

				sqls.add(Sqls.create(sqlStr));

				if (LogUtil.isInfoEnabled())
					LogUtil.info("删除%s多对多关联表: 生成SQL结束. [columnName=%s] %s", entity.getMirror().getType().getName(), link.getOwnField().getName(), logBuf);

				return sqls;
			}
		});
	}

	@SuppressWarnings("unused")
	private List<Sql> makeSqlToDropColumns(final EnMapping mapping) {
		return (List<Sql>) extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				String enInfo = ClassUtil.getDisplayName(mapping.getType());
				LogUtil.debug("删除%s多余字段: 生成SQL...", enInfo);

				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				EnMappingImpl CocitEntity = (EnMappingImpl) entity;
				String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableInfo = db.getTableMetadata(CocitEntity.getTableName());
				Iterator<ColumnMetadata> columns = tableInfo.iteratorColumnMetaDatas();
				while (columns.hasNext()) {
					ColumnMetadata columnInfo = columns.next();
					EnColumnMappingImpl field = CocitEntity.getFieldByColumn(columnInfo.getName());
					if (field == null) {// 删除多余的字段
						if (CocitEntity.isChild()) {
							continue;
						}
						if (CocitEntity.isAbstract()) {
							continue;
						}
						String sqlStr = dialect.sqlDropColumn(tableName, columnInfo.getName());
						if (LogUtil.isInfoEnabled()) {
							logBuf.append(String.format("\n%s", sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}
				if (logBuf.length() == 0)
					LogUtil.debug("删除%s多余字段: 生成SQL结束. %s", enInfo, "<删除0个多余字段>");
				else
					LogUtil.info("删除%s多余字段: 生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}

		});
	}

	@SuppressWarnings("unused")
	private List<Sql> makeSqlToDropTrashColumns(final EnMapping mapping) {
		return (List<Sql>) extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				String enInfo = ClassUtil.getDisplayName(mapping.getType());
				LogUtil.debug("删除%s垃圾字段: 生成SQL...", enInfo);
				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				EnMappingImpl CocitEntity = (EnMappingImpl) entity;
				String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata table = db.getTableMetadata(CocitEntity.getTableName());
				Iterator<ColumnMetadata> columns = table.iteratorColumnMetaDatas();
				while (columns.hasNext()) {
					ColumnMetadata column = columns.next();
					EnColumnMappingImpl field = CocitEntity.getFieldByColumn(column.getName());
					if (field == null) {// 删除多余的字段
						String sqlStr = dialect.sqlDropColumn(tableName, column.getName());
						if (LogUtil.isInfoEnabled()) {
							logBuf.append(String.format("\n%s", sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}

				if (logBuf.length() == 0)
					LogUtil.debug("删除%s垃圾字段: 生成SQL结束. %s", enInfo, "<删除0个垃圾字段>");
				else
					LogUtil.info("删除%s垃圾字段: 生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}

		});
	}

	private List<Sql> makeSqlToAlterColumns(final EnMapping mapping) {

		return (List<Sql>) extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				String enInfo = ClassUtil.getDisplayName(mapping.getType());
				LogUtil.debug("同步%s数据库表: 检查更新字段生成SQL...", enInfo);

				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				EnMappingImpl CocitEntity = (EnMappingImpl) entity;
				String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableInfo = db.getTableMetadata(CocitEntity.getTableName());
				Iterator<ColumnMetadata> columns = tableInfo.iteratorColumnMetaDatas();
				while (columns.hasNext()) {
					ColumnMetadata columnInfo = columns.next();
					EnColumnMappingImpl field = CocitEntity.getFieldByColumn(columnInfo.getName());
					if (field != null) {
						// 修改类型不匹配的字段
						String sqlType = toSqlType(field);
						if (sqlType.equals(columnInfo.getTypeName())) {
							continue;
						}
						String sqlTypeOld = dialect.getTypeName(columnInfo.getTypeCode(), columnInfo.getColumnSize(), columnInfo.getColumnSize(), columnInfo.getDecimalDigits());
						if (sqlType.equals(sqlTypeOld)) {
							continue;
						}
						String sqlStr = dialect.sqlAlterColumnType(tableName, columnInfo.getName(), sqlType);
						if (LogUtil.isInfoEnabled()) {
							logBuf.append(String.format("\n%s:  [sqlOldType=%s, sqlType=%s] %s", field.getName(), sqlTypeOld, sqlType, sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					} else {
						String nullable = columnInfo.getNullable();
						if (!"YES".equals(nullable.toUpperCase()) && StringUtil.isBlank(columnInfo.getColumnDef())) {
							String sqlTypeOld = dialect.getTypeName(columnInfo.getTypeCode(), columnInfo.getColumnSize(), columnInfo.getColumnSize(), columnInfo.getDecimalDigits());

							String sqlStr = dialect.sqlAlterColumnType(tableName, columnInfo.getName(), sqlTypeOld);
							sqlStr = sqlStr + " null";
							sqls.add(Sqls.create(sqlStr));
						}
					}
				}

				if (logBuf.length() == 0)
					LogUtil.debug("同步%s数据库表: 检查更新字段生成SQL结束. %s", enInfo, "<更新0个字段>");
				else
					LogUtil.info("同步%s数据库表: 检查更新字段生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}

		});
	}

	private List<Sql> makeSqlToAlterIndexs(final EnMapping mapping) {

		return (List<Sql>) extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				List<Sql> sqls = new ArrayList();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableMeta = db.getTableMetadata(mapping.getTableName());

				String sqlStr;

				List<String[]> list = mapping.getUniqueFields();
				for (String[] fields : list) {
					String indexName = "UNIQUE_" + StringUtil.join(fields, "_", true);

					if (tableMeta.getIndexMetadata(indexName) == null) {
						sqlStr = dialect.sqlAddUnique(mapping.getTableName(), indexName, fields);
						sqls.add(Sqls.create(sqlStr));
					}
				}
				list = mapping.getIndexFields();
				for (String[] fields : list) {
					String indexName = "INDEX_" + StringUtil.join(fields, "_", true);

					if (tableMeta.getIndexMetadata(indexName) == null) {
						sqlStr = dialect.sqlAddIndex(mapping.getTableName(), indexName, fields);
						sqls.add(Sqls.create(sqlStr));
					}
				}

				return sqls;
			}

		});
	}

	private List<Sql> makeSqlToCreateColumns(final EnMapping mapping) {
		return (List<Sql>) extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				final String enInfo = ClassUtil.getDisplayName(mapping.getType());
				LogUtil.debug("同步%s数据库表: 检查新增字段生成SQL...", enInfo);

				final EnMappingImpl entity = (EnMappingImpl) mapping;
				final List<Sql> sqls = new ArrayList();
				final StringBuffer logBuf = new StringBuffer();

				final EnMappingImpl CocitEntity = (EnMappingImpl) entity;
				final String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata tableInfo = db.getTableMetadata(CocitEntity.getTableName());
				// 添加不存在的字段
				Iterator<EnColumnMappingImpl> fields = CocitEntity.fields().iterator();

				while (fields.hasNext()) {
					EnColumnMappingImpl f = fields.next();
					String columnName = f.getColumnName();
					if (tableInfo.getColumnMetadata(columnName) == null) {
						String sqlStr = dialect.sqlAddColumn(tableName, columnName, toSqlType(f));
						if (LogUtil.isInfoEnabled()) {
							logBuf.append(String.format("\n%s:  %s", f.getName(), sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}

				if (logBuf.length() == 0)
					LogUtil.debug("同步%s数据库表: 检查新增字段生成SQL结束. %s", enInfo, "<新增0个字段>");
				else
					LogUtil.info("同步%s数据库表: 检查新增字段生成SQL结束. %s", enInfo, logBuf);

				return sqls;

			}

		});
	}

	private List<Sql> makeSqlToAlterFKs(final EnMapping mapping, final boolean syncRefTable) {
		return (List<Sql>) extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				if (mapping == null) {
					return new ArrayList();
				}
				String enInfo = ClassUtil.getDisplayName(mapping.getType());
				LogUtil.debug("同步%s数据库表: 检查外键生成SQL...[syncRefTable=%s]", enInfo, syncRefTable);

				EnMappingImpl entity = (EnMappingImpl) mapping;
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				EnMappingImpl CocitEntity = (EnMappingImpl) entity;
				String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata table = db.getTableMetadata(CocitEntity.getTableName());
				Iterator<ForeignKeyMetadata> fkInfos = table.iteratorForeignKeyMetadatas();

				// 删除多余外键
				while (fkInfos.hasNext()) {
					ForeignKeyMetadata fkInfo = fkInfos.next();
					if (CocitEntity.getLink(fkInfo.getColumnName()) == null) {
						if (CocitEntity.isChild()) {
							continue;
						}
						if (CocitEntity.isAbstract()) {
							continue;
						}
						String fkName = fkInfo.getName();
						String sqlStr = dialect.sqlDropFk(tableName, fkName);
						if (LogUtil.isInfoEnabled()) {
							logBuf.append(String.format("\n[%s.%s-->%s]:  %s", CocitEntity.getTableName(), fkInfo.getColumnName(), fkInfo.getRefTableName(), sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}

				// 添加缺少的外键
				Iterator<EnColumnMappingImpl> fields = CocitEntity.fields().iterator();
				while (fields.hasNext()) {
					EnColumnMappingImpl ef = fields.next();
					Link link = ef.getLink();
					if (link == null) {
						continue;
					}
					Class targetClass = link.getTargetClass();
					if (LogUtil.isDebugEnabled())
						LogUtil.debug("同步%s数据库表: 检查外键 获取外键%s关联实体%s", enInfo, ClassUtil.getDisplayName(link.getOwnField()), ClassUtil.getDisplayName(targetClass));
					EnMappingImpl refEntity = (EnMappingImpl) mappingHolder.getEnMapping(targetClass);
					if (refEntity == null) {
						if (syncRefTable) {
							LogUtil.debug("同步%s数据库表: 检查外键 加载外键%s关联实体%s...", enInfo, ClassUtil.getDisplayName(link.getOwnField()), ClassUtil.getDisplayName(targetClass));
							refEntity = extDao.loadEntity(targetClass, true);
							LogUtil.debug("同步%s数据库表: 检查外键 加载外键%s关联实体%s结束.", enInfo, ClassUtil.getDisplayName(link.getOwnField()), ClassUtil.getDisplayName(targetClass));
						} else if (LogUtil.isInfoEnabled()) {
							logBuf.append(String.format("\n%s:  外键关联实体%s未加载", ClassUtil.getDisplayName(link.getOwnField()), ClassUtil.getDisplayName(targetClass)));
							continue;
						}
					}
					if (refEntity == null) {
						continue;
					}
					String refTableName = refEntity.getTableName();
					if (table.getForeignKeyMetadata(ef.getColumnName(), refTableName) == null) {
						String fkName = ef.getFkName();
						String[] foreignKey = new String[1];
						foreignKey[0] = ef.getColumnName();
						String sqlStr = dialect.sqlAddFk(tableName, fkName, foreignKey, refTableName, null, true);
						if (LogUtil.isInfoEnabled()) {
							logBuf.append(String.format("\n%s:  %s", ClassUtil.getDisplayName(link.getOwnField()), sqlStr));
						}
						sqls.add(Sqls.create(sqlStr));
					}
				}

				if (logBuf.length() == 0)
					LogUtil.debug("同步%s数据库表: 检查外键 生成SQL结束. %s", enInfo, "<新增0个外键>");
				else
					LogUtil.info("同步%s数据库表: 检查外键 生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}

		});
	}

	private List<Sql> makeSqlToCreateFKs(EnMapping entity, boolean syncTargetTable) {
		String enInfo = ClassUtil.getDisplayName(entity.getType());
		LogUtil.debug("同步%s数据库表: 创建外键生成SQL...[syncRefTable=%s]", enInfo, syncTargetTable);

		List<Sql> sqls = new ArrayList();
		StringBuffer logBuf = new StringBuffer();

		EnMappingImpl CocitEntity = (EnMappingImpl) entity;
		String tableName = entity.getTableName();
		Iterator<EnColumnMappingImpl> fields = CocitEntity.fields().iterator();

		while (fields.hasNext()) {
			EnColumnMappingImpl fkColumnMapping = fields.next();
			Link link = fkColumnMapping.getLink();
			if (link == null) {
				continue;
			}
			String fkName = fkColumnMapping.getFkName();
			if (!StringUtil.isBlank(fkName)) {
				Class targetClass = link.getTargetClass();
				EnMappingImpl tergetEntity = mappingHolder.getEnMapping(targetClass);
				if (tergetEntity == null) {
					if (syncTargetTable) {
						LogUtil.debug("同步%s数据库表: 创建外键加载外键%s关联实体%s...", enInfo, ClassUtil.getDisplayName(link.getOwnField()), ClassUtil.getDisplayName(targetClass));
						tergetEntity = extDao.loadEntity(targetClass, true);
						LogUtil.debug("同步%s数据库表: 创建外键 加载外键%s关联实体%s结束.", enInfo, ClassUtil.getDisplayName(link.getOwnField()), ClassUtil.getDisplayName(targetClass));
					} else {
						if (LogUtil.isInfoEnabled()) {
							logBuf.append(String.format("\n%s: 外键关联实体%s未加载", ClassUtil.getDisplayName(link.getOwnField()), ClassUtil.getDisplayName(targetClass)));
						}
						continue;
					}
				}
				String targetTable = tergetEntity.getTableName();
				String[] fkColumnNames = new String[1];
				fkColumnNames[0] = fkColumnMapping.getColumnName();
				String[] targetPKColumnNames = new String[1];
				if (link.getTo() != null) {
					targetPKColumnNames[0] = link.getTo();
				} else if (link.get("to") != null) {
					targetPKColumnNames[0] = link.get("to").toString();
				} else {
					targetPKColumnNames[0] = tergetEntity.getIdField().getColumnName();
				}
				String sqlStr = dialect.sqlAddFk(tableName, fkName, fkColumnNames, targetTable, targetPKColumnNames, false);
				if (LogUtil.isInfoEnabled()) {
					logBuf.append(String.format("\n%s: %s", ClassUtil.getDisplayName(link.getOwnField()), sqlStr));
				}
				sqls.add(Sqls.create(sqlStr));
			}
		}

		if (logBuf.length() == 0)
			LogUtil.debug("同步%s数据库表: 创建外键生成SQL结束. %s", enInfo, "<创建0个外键>");
		else
			LogUtil.info("同步%s数据库表: 创建外键生成SQL结束. %s", enInfo, logBuf);

		return sqls;
	}

	@SuppressWarnings("unused")
	private List<Sql> makeSqlToDropFKs(final EnMapping mapping) {

		return (List<Sql>) extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				final String enInfo = ClassUtil.getDisplayName(mapping.getType());
				LogUtil.debug("删除%s实体外键: 生成SQL...", enInfo);

				final EnMappingImpl entity = (EnMappingImpl) mapping;
				final List<Sql> sqls = new ArrayList();
				final StringBuffer logBuf = new StringBuffer();

				final EnMappingImpl CocitEntity = (EnMappingImpl) entity;
				final String tableName = entity.getTableName();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				TableMetadata table = db.getTableMetadata(CocitEntity.getTableName());
				Iterator<ForeignKeyMetadata> fkInfos = table.iteratorForeignKeyMetadatas();
				// 删除外键
				while (fkInfos.hasNext()) {
					ForeignKeyMetadata fkInfo = fkInfos.next();
					String fkName = fkInfo.getName();
					String sqlStr = dialect.sqlDropFk(tableName, fkName);
					if (LogUtil.isInfoEnabled()) {
						logBuf.append("\n" + sqlStr);
					}
					sqls.add(Sqls.create(sqlStr));
				}

				if (logBuf.length() == 0)
					LogUtil.debug("删除%s实体外键: 生成SQL结束. %s", "<删除0个外键>");
				else
					LogUtil.info("删除%s实体外键: 生成SQL结束. %s", enInfo, logBuf);

				return sqls;
			}

		});
	}

	private List<Sql> makeSqlToCreateTableAndRelation(EnMapping mapping, Link link, boolean syncRefTable) {
		String enInfo = ClassUtil.getDisplayName(mapping.getType());
		LogUtil.debug("同步%s数据库表: 创建多对多关联表生成SQL...[link=%s, syncRefTable=%s]", enInfo, ClassUtil.getDisplayName(link.getOwnField()), syncRefTable);

		EnMappingImpl entity = (EnMappingImpl) mapping;
		if (!link.isManyMany()) {
			return null;
		}
		List<Sql> sqls = new ArrayList();
		StringBuffer logBuf = new StringBuffer();

		Class targetClass = link.getTargetClass();
		EnMappingImpl targetEntity = mappingHolder.getEnMapping(targetClass);
		if (targetEntity == null) {
			if (syncRefTable) {
				LogUtil.debug("同步%s数据库表: 创建多对多关联表加载%s关联实体%s...", enInfo, ClassUtil.getDisplayName(link.getOwnField()), ClassUtil.getDisplayName(targetClass));
				targetEntity = extDao.loadEntity(targetClass, true);
				LogUtil.debug("同步%s数据库表: 创建多对多关联表加载%s关联实体%s结束.", enInfo, ClassUtil.getDisplayName(link.getOwnField()), ClassUtil.getDisplayName(targetClass));
			} else {
				if (LogUtil.isInfoEnabled()) {
					logBuf.append(String.format("\n%s: 关联实体%s未加载", ClassUtil.getDisplayName(link.getOwnField()), ClassUtil.getDisplayName(targetClass)));
				}
			}
		}

		// 创建多对多实体的中间表
		if (targetEntity != null) {
			StringBuffer buf = new StringBuffer(dialect.getCreateTableString()).append(' ').append(link.getRelation()).append(" (\n");
			buf.append("\t").append(link.getFrom()).append(' ').append(toSqlType(link.getReferField())).append(" not null").append(",\n");
			buf.append("\t").append(link.getTo()).append(' ').append(toSqlType(link.getTargetField())).append(" not null").append("\n)");
			if (LogUtil.isInfoEnabled()) {
				logBuf.append(String.format("\n%s", buf));
			}
			sqls.add(Sqls.create(buf.toString()));

			// 创建实体中间表主对象外键
			String tableName = entity.getTableName();
			String[] foreignKey = new String[1];
			foreignKey[0] = link.getFrom();
			String constraintName = entity.getNaming().fkName(link.getRelation(), StringUtil.join("_", foreignKey), tableName);
			String sqlStr = dialect.sqlAddFk(link.getRelation(), constraintName, foreignKey, tableName, null, true);
			if (LogUtil.isInfoEnabled()) {
				logBuf.append(String.format("\n主表外键<%s.%s>: %s", enInfo, ClassUtil.getDisplayName(link.getOwnField()), sqlStr));
			}
			sqls.add(Sqls.create(sqlStr));

			// 创建实体中间表目标对象外键
			tableName = targetEntity.getTableName();
			foreignKey = new String[1];
			foreignKey[0] = link.getTo();
			constraintName = entity.getNaming().fkName(link.getRelation(), StringUtil.join("_", foreignKey), tableName);
			sqlStr = dialect.sqlAddFk(link.getRelation(), constraintName, foreignKey, tableName, null, true);
			if (LogUtil.isInfoEnabled()) {
				logBuf.append(String.format("\n从表外键<%s>: %s", ClassUtil.getDisplayName(link.getTargetClass()), sqlStr));
			}
			sqls.add(Sqls.create(sqlStr));
		}

		if (LogUtil.isInfoEnabled())
			LogUtil.info("同步%s数据库表: 创建多对多关联表生成SQL结束. [columnName=%s, syncRefTable=%s] %s", enInfo, ClassUtil.getDisplayName(link.getOwnField()), syncRefTable, logBuf);

		return sqls;
	}

	private List<Sql> makeSqlToClearDB() {
		return (List<Sql>) extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				LogUtil.debug("清空数据库: 生成SQL...");
				List<Sql> sqls = new ArrayList();
				StringBuffer logBuf = new StringBuffer();

				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				Map mp = db.getTableMetadatas(null);

				Iterator<TableMetadata> tables = mp.values().iterator();
				while (tables.hasNext()) {
					TableMetadata table = tables.next();
					String tablename = table.getName();

					// 删除外键
					Iterator<ForeignKeyMetadata> fks = table.iteratorForeignKeyMetadatas();
					while (fks.hasNext()) {
						ForeignKeyMetadata fk = fks.next();
						String fkName = fk.getName();
						String sqlStr = dialect.sqlDropFk(tablename, fkName);

						if (LogUtil.isInfoEnabled()) {
							logBuf.append("\n" + sqlStr);
						}

						sqls.add(Sqls.create(sqlStr));
					}

					// 删除索引
					Iterator<IndexMetadata> indexs = table.iteratorIndexMetadatas();
					while (indexs.hasNext()) {
						IndexMetadata idx = indexs.next();
						String idxName = idx.getName();
						String sqlStr = dialect.sqlDropIndex(tablename, idxName);

						if (LogUtil.isInfoEnabled()) {
							logBuf.append("\n" + sqlStr);
						}

						sqls.add(Sqls.create(sqlStr));
					}

					// 删除表
					String sqlStr = dialect.sqlDropTable(tablename);

					if (LogUtil.isInfoEnabled()) {
						logBuf.append("\n" + sqlStr);
					}

					sqls.add(Sqls.create(sqlStr));
				}

				LogUtil.info("清空数据库: 生成SQL清除<%s>张表. %s", (mp == null ? 0 : mp.size()), logBuf);

				return sqls;
			}

		});
	}

	public Map<String, TableMetadata> getTableMetadatas() {
		final Map<String, TableMetadata> tables = new HashMap();

		extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				tables.putAll(db.getTableMetadatas(null));

				return null;
			}

		});

		return tables;
	}

	public TableMetadata getTableMetadata(EnMapping entity) {
		return getTableMetadata(entity.getTableName());
	}

	public TableMetadata getTableMetadata(final String tableName) {
		final TableMetadata[] tables = new TableMetadata[1];

		extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				tables[0] = db.getTableMetadata(tableName);

				return null;
			}

		});

		return tables[0];
	}

	public Map<String, ForeignKeyMetadata> getImportFKMetadatas(EnMapping entity) {
		return this.getImportFKMetadatas(entity.getTableName());
	}

	public Map<String, ForeignKeyMetadata> getImportFKMetadatas(final String tableName) {
		final Map<String, ForeignKeyMetadata> map = new HashMap();

		extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				map.putAll(db.getTableMetadata(tableName).getForeignKeys());

				return null;
			}

		});

		return map;
	}

	public Map<String, ForeignKeyMetadata> getExportFKMetadatas(final EnMapping entity) {
		return this.getExportFKMetadatas(entity.getTableName());
	}

	public Map<String, ForeignKeyMetadata> getExportFKMetadatas(final String tableName) {
		final Map<String, ForeignKeyMetadata> map = new HashMap();

		extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				map.putAll(db.getTableMetadata(tableName).getForeignedKeys());

				return null;
			}

		});

		return map;
	}

	public Map<String, ColumnMetadata> getColumnMetadatas(final EnMapping entity) {
		return this.getColumnMetadatas(entity.getTableName());
	}

	public Map<String, ColumnMetadata> getColumnMetadatas(final String tableName) {
		final Map<String, ColumnMetadata> map = new HashMap();

		extDao.run(new ConnCallback() {

			public Object invoke(Connection conn) throws Exception {
				DatabaseMetadata db = new DatabaseMetadata(conn, dialect);
				map.putAll(db.getTableMetadata(tableName).getColumns());

				return null;
			}

		});

		return map;
	}
}
