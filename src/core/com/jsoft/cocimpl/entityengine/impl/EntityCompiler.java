package com.jsoft.cocimpl.entityengine.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.persistence.Entity;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.ClassFile;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.compiler.batch.CompilationUnit;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;

import com.jsoft.cocimpl.mvc.servlet.StaticResourceFilter;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.entity.IDataEntity;
import com.jsoft.cocit.entity.coc.ICocEntity;
import com.jsoft.cocit.entity.coc.ICocField;
import com.jsoft.cocit.entity.coc.IExtCocEntity;
import com.jsoft.cocit.entityengine.EntityEngine;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.entityengine.service.CocGroupService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.DateUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

public class EntityCompiler {
	private static Pattern var = Pattern.compile("^[_a-zA-Z][_a-zA-Z0-9]*$", Pattern.CASE_INSENSITIVE);

	// 编译次数
	// private static int times = 0;

	private static final String TAB = "    ";

	private EntityEngine engine;

	EntityCompiler(EntityEngine engine) {
		this.engine = engine;
	}

	/**
	 * 检查能否可以自动生成实体模块源代码。
	 * <p>
	 * 空值——表示可以
	 * <p>
	 * 非空——表示不可以
	 * 
	 * @param system
	 *            实体模块
	 * @return 提示信息
	 *         <p>
	 *         空值——表示可以
	 *         <p>
	 *         非空——表示不可以
	 */
	private String checkSrc(ICocEntity system) {
		if (system.isDisabled())
			return system.getName() + "(" + system.getKey() + "," + system.getId() + ")实体模块被停用;";
		// if (!StringUtil.isEmpty(system.getEntityClassName()))
		// return system.getName() + "(" + system.getKey() + "," + system.getId() + ")是内置模块;";

		return "";
	}

	/**
	 * 检查能否可以自动生成字段源代码。
	 * <p>
	 * 空值——表示可以
	 * <p>
	 * 非空——表示不可以
	 * 
	 * @param field
	 * @return 提示信息
	 *         <p>
	 *         空值——表示可以
	 *         <p>
	 *         非空——表示不可以
	 */
	@SuppressWarnings("unused")
	private String checkSrc(ICocEntity system, ICocField field) {
		String prop = engine.getPropName(field);
		String name = field.getName() + "(" + prop + ")";
		if (field.isDisabled())
			return name + "字段被停用;";
		if (!StringUtil.isBlank(field.getFkTargetFieldKey()))
			return name + "是引用字段;";
		if (field.isTransient())
			return name + "是临时字段;";
		if (engine.isBuildin(system, prop))
			return name + "是内置字段;";
		// if (StringUtil.isEmpty(engine.getClassName(field)))
		// return name + "字段类型不存在;";

		return "";
	}

	/**
	 * 生成package语句源代码
	 * 
	 * @return 源代码片段
	 */
	private String makePackageCode(CocEntityService system) {
		return new StringBuffer().append("package ").append(system.getPackageName()).append(";").toString();
	}

	/**
	 * 生成import语句源代码
	 * 
	 * @return 源代码片段
	 */
	private String makeImportCode(ICocEntity system) {
		return new StringBuffer().append("\n")//
				.append("\nimport java.util.*;")//
				.append("\nimport java.math.*;")//
				.append("\nimport " + Entity.class.getPackage().getName() + ".*;")//
				.append("\nimport " + CocEntity.class.getPackage().getName() + ".*;")//
				.append("\nimport " + IDataEntity.class.getPackage().getName() + ".*;")//
				.toString();
	}

	private String makeProp(String prop, Object value, Object defaultValue) {
		if (value == null || value == defaultValue) {
			return "";
		}
		if (value instanceof String) {
			if (!StringUtil.isBlank((String) value)) {
				return ", " + prop + " = " + JsonUtil.toJson(((String) value).trim());
			} else
				return "";
		}

		return ", " + prop + " = " + value;
	}

	/**
	 * 生成类定义源代码
	 * 
	 * @param module
	 *            实体模块
	 */
	private String makeClassAnnotation(CocEntityService module) {
		StringBuffer src = new StringBuffer();

		String tableName = module.getTableName();

		src.append("\n");

		src.append("\n@Entity");
		if (!StringUtil.isBlank(tableName)) {
			src.append("\n@Table(name = \"" + tableName + "\")");
		}

		/*
		 * 实体模块注解
		 */
		src.append(String.format("\n@%s(name = \"%s\"", CocEntity.class.getSimpleName(), module.getName()));
		src.append(makeProp("key", module.getKey(), ""));
		src.append(makeProp("catalog", module.getCatalogKey(), ""));
		src.append(makeProp("uniqueFields", module.getUniqueFields(), ""));
		src.append(makeProp("indexFields", module.getIndexFields(), ""));
		src.append(makeProp("version", getComplireVersion(module), ""));
		src.append(" //");

		/*
		 * 操作按钮注解
		 */
		src.append("\n, actions = {");
		src.append("\n// begin action");
		List<CocActionService> actions = module.getActions(null);
		if (actions != null) {
			int count = 0;
			for (CocActionService action : actions) {
				if (action.isDisabled() || action.isRemoved())
					continue;

				if (count > 0) {
					src.append("\n\t\t, ");
				} else {
					src.append("\n\t\t");
				}

				src.append(String.format("@%s(name = \"%s\"", CocAction.class.getSimpleName(), action.getName()));
				src.append(makeProp("opCode", action.getOpCode(), 0));
				src.append(makeProp("key", action.getKey(), ""));
				src.append(makeProp("title", action.getTitle(), ""));
				src.append(") //");

				count++;
			}
		}
		src.append("\n} // end actions");

		/*
		 * 字段分组注解
		 */
		src.append("\n, groups = {");
		src.append("\n// begin group");
		List<CocGroupService> groups = module.getGroups();
		if (groups != null) {
			int count = 0;
			for (CocGroupService group : groups) {
				if (group.isDisabled() || group.isRemoved())
					continue;

				if (count > 0) {
					src.append("\n\t\t, ");
				} else {
					src.append("\n\t\t");
				}

				/*
				 * 字段组信息注解
				 */
				src.append(String.format("@%s(name = \"%s\", key = \"%s\", fields = {", CocGroup.class.getSimpleName(), group.getName(), group.getKey()));
				src.append("\n\t\t\t\t// begin column");

				/*
				 * 字段注解
				 */
				List<CocFieldService> fields = group.getFields();
				if (groups != null) {
					int count1 = 0;
					for (CocFieldService field : fields) {
						if (field.isDisabled() || field.isRemoved())
							continue;

						if (count1 > 0) {
							src.append("\n\t\t\t\t, ");
						} else {
							src.append("\n\t\t\t\t");
						}

						/*
						 * 字段组信息注解
						 */
						src.append(String.format("@%s(name = \"%s\"", CocColumn.class.getSimpleName(), field.getName()));
						src.append(makeProp("propName", field.getFieldName(), ""));
						src.append(makeProp("dbColumnName", field.getDbColumnName(), ""));
						src.append(makeProp("mode", field.getMode(), ""));
						src.append(makeProp("dicOptions", field.getDicOptions(), ""));
						src.append(makeProp("gridColumnSn", field.getGridColumnSn(), 0));
						src.append(makeProp("gridColumnWidth", field.getGridColumnWidth(), 0));
						src.append(makeProp("pattern", field.getPattern(), ""));
						src.append(makeProp("precision", field.getPrecision(), 0));
						src.append(makeProp("fkTargetField", field.getFkTargetFieldKey(), ""));
						src.append(makeProp("fkTargetEntity", field.getFkTargetEntityKey(), ""));
						src.append(makeProp("sn", field.getSn(), 0));
						src.append(makeProp("uiCascading", field.getUiCascading(), ""));
						src.append(makeProp("uiView", field.getUiView(), ""));
						src.append(makeProp("asGridColumn", field.isAsGridColumn(), false));
						src.append(makeProp("asFilterNode", field.isAsFilterNode(), false));
						src.append(makeProp("fkTargetAsGroup", field.isFkTargetAsGroup(), false));
						src.append(makeProp("fkTargetAsParent", field.isFkTargetAsParent(), false));
						src.append(makeProp("isTransient", field.isTransient(), false));
						src.append(makeProp("dbColumnDefinition", field.getDbColumnDefinition(), ""));
						src.append(makeProp("length", field.getLength(), 0));
						src.append(makeProp("scale", field.getScale(), 0));
						src.append(") //");

						count1++;
					}
				}

				src.append("\n\t\t}) // end group");

				count++;
			}
		}
		src.append("\n} // end groups");

		src.append("\n)");

		// TODO: 扩展类特殊属性

		return src.toString();
	}

	private String makeClassCode(CocEntityService module) {
		StringBuffer src = new StringBuffer();

		String className = module.getSimpleClassName();
		String extendClass = module.getExtendsClassName();

		src.append("\npublic class ").append(className).append(" extends ").append(extendClass).append(" {");

		// TODO: 扩展类特殊属性

		return src.toString();
	}

	private String getComplireVersion(CocEntityService module) {
		String version = DateUtil.formatDateTime(new Date());
		((IExtCocEntity) module.getEntityData()).setCompileVersion(version);

		return version;
	}

	/**
	 * 生成引出字段注解
	 * 
	 * @return 源代码片段
	 */
	@SuppressWarnings("unused")
	private String genSrcOfTargetAnnotation(ICocField fld) {
		StringBuffer src = new StringBuffer();

		String mappedBy = engine.getPropName(fld);

		src.append("\n");
		if (engine.isManyToMany(fld)) {
			src.append("\n").append(TAB).append("@ManyToMany(mappedBy = \"" + mappedBy + "\")");
		} else if (engine.isManyToOne(fld)) {
			src.append("\n").append(TAB).append("@OneToMany(mappedBy = \"" + mappedBy + "\")");
		}
		src.append("\n").append(TAB).append("@CocField(")//
				.append("name = \"").append(fld.getName().replace("\"", "\\\"")).append("\"")//
				.append(", id = ").append(fld.getId())//
				.append(", code = \"").append(fld.getKey().replace("\"", "\\\"")).append("\"");
		src.append(")");

		return src.toString();
	}

	/**
	 * 生成字段注解
	 * 
	 * @return 源代码片段
	 */
	private String makeFieldAnnotation(ICocField fld) {
		StringBuffer src = new StringBuffer();

		src.append("\n");

		if (engine.isManyToOne(fld)) {
			// src.append("\n").append(TAB).append("@ManyToOne");
			// } else if (engine.isNumber(fld)) {
			// int p = engine.getPrecision(fld);
			// Integer scale = fld.getScale();
			// if (scale == null) {
			// scale = 0;
			// }
			// if (p > 0 && p > scale) {
			// src.append("\n").append(TAB).append("@Column(precision = ").append(p).append(", scale = ").append(scale).append(")");
			// }
		} else if (engine.isManyToMany(fld)) {
			// src.append("\n").append(TAB).append("@ManyToMany");
		} else if (engine.isRichText(fld)) {
			src.append("\n").append(TAB).append("@Column(columnDefinition = \"text\")");
			// } else if (engine.isUpload(fld)) {
			// src.append("\n@Column(length = 256)");
		} else if (engine.isString(fld)) {
			if (fld.getLength() > 2000) {
				src.append("\n").append(TAB).append("@Column(columnDefinition = \"text\")");
				// } else {
				// src.append("\n").append(TAB).append("@Column(length = ").append(p).append(")");
			}
		}

		// src.append("\n").append(TAB).append("@" + CocColumn.class.getSimpleName() + "(").append("name = \"").append(fld.getName() == null ? "" : fld.getName().replace("\"", "\\\"")).append("\"");
		// if (!StringUtil.isEmpty(fld.getKey())) {
		// src.append(", fieldName = \"").append(fld.getKey()).append("\"");
		// }
		// if (!StringUtil.isEmpty(fld.getDicOptions())) {
		// src.append(", dicOptions = \"").append(fld.getDicOptions()).append("\"");
		// }
		// if (!StringUtil.isEmpty(fld.getMode())) {
		// src.append(", mode = \"").append(fld.getMode()).append("\"");
		// }
		// if (!StringUtil.isEmpty(fld.getPattern())) {
		// src.append(", pattern = \"").append(fld.getPattern()).append("\"");
		// }
		// if (!StringUtil.isEmpty(fld.getRefFieldKey())) {
		// src.append(", refColumn = \"").append(fld.getRefFieldKey()).append("\"");
		// }
		// if (!StringUtil.isEmpty(fld.getRefModuleKey())) {
		// src.append(", refEntity = \"").append(fld.getRefModuleKey()).append("\"");
		// }
		// if (!StringUtil.isEmpty(fld.getUiCascading())) {
		// src.append(", uiCascading = \"").append(fld.getUiCascading()).append("\"");
		// }
		// if (!StringUtil.isEmpty(fld.getUiWidget())) {
		// src.append(", uiWidget = \"").append(fld.getUiWidget()).append("\"");
		// }
		// if (fld.getGridSn() > 0) {
		// src.append(", gridSn = ").append(fld.getGridSn());
		// }
		// if (fld.getGridWidth() > 0) {
		// src.append(", gridWidth = ").append(fld.getGridWidth());
		// }
		// if (fld.getPrecision() != null && fld.getPrecision() > 0) {
		// src.append(", precision = ").append(fld.getPrecision());
		// }
		// if (fld.getScale() != null && fld.getScale() > 0) {
		// src.append(", scale = ").append(fld.getScale());
		// }
		// if (!StringUtil.isEmpty(fld.getDataCascading())) {
		// src.append(", dataCascading = \"").append(fld.getDataCascading()).append("\"");
		// }
		// if (fld.getSn() != null && fld.getSn() > 0) {
		// src.append(", sn = ").append(fld.getSn());
		// }
		// if (!fld.isGridShow()) {
		// src.append(", gridShow = ").append(fld.isGridShow());
		// }
		// if (fld.isMultipleValue()) {
		// src.append(", isMultipleValue = \"").append(fld.isMultipleValue()).append("\"");
		// }
		// if (!fld.isNotFilter()) {
		// src.append(", notFilter = ").append(fld.isNotFilter());
		// }
		// if (fld.isRefGroupBy()) {
		// src.append(", refGroupBy = ").append(fld.isRefGroupBy());
		// }
		// if (fld.isRefParent()) {
		// src.append(", refParent = ").append(fld.isRefParent());
		// }

		// src.append(")");

		return src.toString();
	}

	/**
	 * 生成属性定义源代码
	 * 
	 * @param propType
	 *            属性类型
	 * @param propName
	 *            属性名称
	 * @return 源代码片段
	 */
	private String makeFieldCode(String propType, String propName, String comment) {
		StringBuffer src = new StringBuffer();

		src.append("\n").append(TAB).append("private " + propType + " " + propName + ";");
		src.append(" // ").append(comment);

		return src.toString();
	}

	/**
	 * 生成属性Getter/Setter源代码
	 * 
	 * @param propType
	 *            属性类型
	 * @param propName
	 *            属性名称
	 * @return 源码片段
	 */
	private String makeFieldMethodCode(String propType, String propName, String comment) {
		StringBuffer src = new StringBuffer();

		String upperName = propName.substring(0, 1).toUpperCase() + propName.substring(1);

		src.append("\n");

		src.append("\n").append(TAB).append("public void set" + upperName + "(" + propType + " param) {");
		src.append("\n").append(TAB).append(TAB).append(propName + " = param;");
		src.append("\n").append(TAB).append("}");

		src.append("\n");

		src.append("\n").append(TAB).append("public " + propType + " get" + upperName + "() {");
		src.append("\n").append(TAB).append(TAB).append("return " + propName + ";");
		src.append("\n").append(TAB).append("}");

		return src.toString();
	}

	/**
	 * toString() method src code
	 * 
	 * @param propType
	 * @param propName
	 * @return
	 */
	@SuppressWarnings("unused")
	private String genSrcOfToString(String propType, String propName) {
		StringBuffer src = new StringBuffer();

		src.append("\n");

		src.append("\n").append(TAB).append("public String toString() {");
		if ("String".equals(propType))
			src.append("\n").append(TAB).append(TAB).append("return ").append(propName).append(";");
		else
			src.append("\n").append(TAB).append(TAB).append("return ").append(propName).append("==null?\"\":").append(propName).append(".toString();");
		src.append("\n").append(TAB).append("}");

		return src.toString();
	}

	private void appendSrcInfo(List<String> flds, StringBuffer msg, String name) {
		if (flds != null && flds.size() > 0) {
			msg.append(name).append("[");
			for (String str : flds) {
				msg.append(str).append(",");
			}
			msg.append("]");
		}
	}

	private String makeModuleJavaCode(StringBuffer src, CocEntityService module) {
		String info = checkSrc(module);
		if (!StringUtil.isBlank(info)) {
			return info;
		}

		StringBuffer msg = new StringBuffer();

		src.append(makePackageCode(module));
		src.append(makeImportCode(module));

		// 检查字段
		List<CocFieldService> checkedFields = new LinkedList();
		List<? extends CocFieldService> fields = module.getFieldsOfEnabled();
		if (fields != null && fields.size() > 0) {
			List<String> disFlds = new LinkedList();
			List<String> refFlds = new LinkedList();
			List<String> refDisSysFlds = new LinkedList();
			List<String> tmpFlds = new LinkedList();
			List<String> bdnFlds = new LinkedList();
			List<String> empTypeFlds = new LinkedList();
			List<String> otherFlds = new LinkedList();
			List<String> formalFields = new LinkedList();
			for (CocFieldService fld : fields) {
				String prop = fld.getFieldName();

				info = fld.getName() + "(" + fld.getFieldName() + "," + fld.getId() + ")";
				if (StringUtil.isBlank(prop) || !var.matcher(prop).find())
					otherFlds.add(info);
				else if (fld.isDisabled())
					disFlds.add(info);
				else if (prop.indexOf(".") > -1)
					refFlds.add(info);
				// else if (!StringUtil.isEmpty(fld.getRefModuleKey()))
				// refDisSysFlds.add(info);
				// else if (fld.isTransient())
				// tmpFlds.add(info);
				else if (engine.isBuildin(module, prop))
					bdnFlds.add(info);
				// else if (!var.matcher(engine.getClassName(fld)).find())
				// empTypeFlds.add(info);
				else {
					formalFields.add(info);
					checkedFields.add(fld);
				}
			}
			appendSrcInfo(formalFields, msg, "\n\t源码字段");
			appendSrcInfo(disFlds, msg, "\n\t停用字段");
			appendSrcInfo(refDisSysFlds, msg, "\n\t引用实体模块被停用");
			appendSrcInfo(refFlds, msg, "\n\t引用字段");
			appendSrcInfo(tmpFlds, msg, "\n\t临时字段");
			appendSrcInfo(bdnFlds, msg, "\n\t内置字段");
			appendSrcInfo(empTypeFlds, msg, "\n\t空类型字段");
			appendSrcInfo(otherFlds, msg, "\n\t其他非法字段");
		}

		src.append(makeClassAnnotation(module));
		src.append(makeClassCode(module));

		// 检查引出字段
		// List<IBizField> checkedExportFields = new LinkedList();
		// fields = this.getExportFields(system);
		// if (fields != null && fields.size() > 0) {
		// for (IBizField fld : fields) {
		// info = checkSrc(system, fld);
		// if (!Strings.isEmpty(info)) {
		// IBizSystem exportSys = fld.getSystem();
		// msg.append(exportSys.getName()).append("(" + exportSys.getKey() +
		// ")").append("::").append(info);
		// } else {
		// checkedExportFields.add(fld);
		// }
		// }
		// }

		// 生成属性声明语句
		for (ICocField fld : checkedFields) {
			src.append(makeFieldAnnotation(fld));
			src.append(makeFieldCode(engine.getClassName(fld), fld.getFieldName(), fld.getName()));
		}
		// for (IBizField fld : checkedExportFields) {
		// src.append(genSrcOfTargetAnnotation(fld));
		// src.append(genSrcOfPropDeclare(getTargetClassName(fld),
		// getTargetPropName(fld), fld.getName()));
		// }

		// 生成属性 GETTER/SETTER F语句
		for (ICocField fld : checkedFields) {
			src.append(makeFieldMethodCode(engine.getClassName(fld), engine.getPropName(fld), fld.getName()));
		}
		// for (IBizField exportField : checkedExportFields) {
		// String type = "List<" + getClassSimpleName(exportField.getSystem()) +
		// ">";
		// String prop = getTargetPropName(exportField);
		//
		// src.append(genSrcOfPropGetterSetter(type, prop,
		// exportField.getName()));
		// }
		// List<? extends IEntityField> gridFields = module.getFieldsForGrid(null);
		// if (gridFields.size() > 0) {
		// String propType = null;
		// String propName = null;
		// for (IEntityField fld : gridFields) {
		// if (engine.isString(fld)) {
		// propType = "String";
		// propName = fld.getPropName();
		// if (propName.indexOf(".") < 0)
		// break;
		// }
		// }
		// if (propName != null)
		// src.append(this.genSrcOfToString(propType, propName));
		// }

		// 类声明语句结束
		src.append("\n}");

		return msg.toString();
	}

	/**
	 * 编译业务实体模块，现将业务实体模块类编译在临时目录下。
	 * <p>
	 * 编译业务实体模块时，将检查该实体模块所依赖的其他业务实体模块类是否存在，如不存在，则将同时编译依赖实体模块。
	 * 
	 * @param module
	 *            实体模块
	 * @param copyToClassesDir
	 *            编译成功后是否拷贝到/WEB-INF/classes目录下
	 * @return 编译业务实体模块后返回信息列表
	 * @throws CocException
	 *             编译业务实体类出错将抛出编译错误异常
	 */
	synchronized List<String> compileSystem(CocEntityService module, boolean copyToClassesDir) throws CocException {
		if (LogUtil.isInfoEnabled()) {
			LogUtil.info("编译<%s(%s,%s)>实体模块......", module.getName(), module.getKey(), module.getId());
		}

		List<CocEntityService> collectedModules = new LinkedList();
		collectedModules.add(module);

		if (LogUtil.isInfoEnabled()) {
			LogUtil.info("编译<%s(%s,%s)>实体模块: 计算需要编译的依赖实体模块...", module.getName(), module.getKey(), module.getId());
		}

		collectBizSystems(collectedModules, module);

		if (LogUtil.isInfoEnabled()) {
			StringBuffer logBuf = new StringBuffer();
			for (ICocEntity mdl : collectedModules) {
				logBuf.append(mdl).append("(" + mdl.getKey() + "," + mdl.getId() + ")").append(mdl.getClassName()).append("\n");
			}
			LogUtil.info("编译<%s(%s,%s)>实体模块: 计算需要编译的依赖实体模块: 结束. 共<%s>个实体模块需编译[\n%s]", module.getName(), module.getKey(), module.getId(), collectedModules.size(), logBuf);
		}

		// String tempDir = appconfig.getTempDir() + File.separator + "BIZSRC_" + (times++);
		String srcDir = new File(Cocit.me().getConfig().getContextDir()).getParentFile().getAbsolutePath() + File.separator + "entity";
		// Files.deleteDir(new File(srcDir));

		List<String> classNames = new LinkedList();
		List<String> srcCodes = new LinkedList();
		List<String> infos = new LinkedList();

		String error = null;
		try {
			/*
			 * 过滤已编译过的依赖类
			 */
			for (int i = collectedModules.size() - 1; i >= 0; i--) {
				CocEntityService mdl = collectedModules.get(i);
				boolean igloreSrc = false;
				if (!module.equals(mdl)) {
					try {
						ClassUtil.forName(mdl.getClassName());
						igloreSrc = true;
					} catch (Throwable e) {
					}
				}
				if (igloreSrc) {
					collectedModules.remove(i);
				}
			}

			/*
			 * 生成源代码
			 */
			String className;
			StringBuffer srcCode;
			String info;
			for (CocEntityService mdl : collectedModules) {
				className = mdl.getClassName();
				srcCode = new StringBuffer();
				info = makeModuleJavaCode(srcCode, mdl);

				if (info.length() > 0)
					infos.add(mdl.getName() + "(" + mdl.getKey() + "," + mdl.getId() + ")：" + info);

				if (srcCode.length() > 0) {
					classNames.add(className);
					srcCodes.add(srcCode.toString());
					makeJavaFile(srcDir, className, srcCode.toString());
				}
			}

			if (srcCodes.size() > 0)
				error = compileSrcFile(srcDir, classNames, srcCodes);

			if (error != null && error.trim().length() > 0) {
				infos.add(error);
			}

		} catch (Throwable e) {
			infos.add(ExceptionUtil.msg(e));
		}

		return infos;
	}

	/**
	 * 收集需要编译的业务实体模块
	 * 
	 * @param collectedModules
	 *            编译目标业务实体模块时同时需要依赖编译的实体模块
	 * @param targetModule
	 *            编译的目标业务实体模块
	 */
	private void collectBizSystems(List collectedModules, CocEntityService targetModule) {
		if (!collectedModules.contains(targetModule)) {
			collectedModules.add(targetModule);
		}
		List<? extends ICocField> fields = targetModule.getFkFields();
		for (ICocField fld : fields) {
			if (!fld.isDisabled()) {
				CocEntityService refModule = Cocit.me().getEntityServiceFactory().getEntity(fld.getFkTargetEntityKey());
				if (!collectedModules.contains(refModule) && !refModule.isDisabled()) {
					if (LogUtil.isDebugEnabled())
						LogUtil.debug("实体模块<%s(%s,%s)>字段<%s(%s,%s)>引用到实体模块<%s(%s,%s)>", targetModule.getName(), targetModule.getKey(), targetModule.getId(), fld.getName(), fld.getKey(), fld.getId(), refModule.getName(), refModule.getKey(),
								refModule.getId());

					collectBizSystems(collectedModules, refModule);
				}
			}
		}
		// fields = getExportFields(system);
		// for (IBizField fld : fields) {
		// if (!fld.isDisabled()) {
		// IBizSystem refSys = fld.getSystem();
		// if (LogUtil.isDebugEnabled())
		// LogUtil.debug("实体模块<%s(%s)>被实体模块<%s(%s)字段<%s(%s)>引用>", system.getName(),
		// system.getKey(), refSys.getName(), refSys.getKey(), fld.getName(),
		// fld.getKey());
		// if (!systems.contains(refSys))
		// buildBizSystems(systems, refSys);
		// }
		// }
	}

	/**
	 * 编译源文件并将编译成功后的类文件拷贝到/WEB-INF/classes目录同时删除临时目录
	 * 
	 * @param srcDir
	 *            编译时使用的临时目录
	 * @param classNames
	 *            待编译的类列表
	 * @param srcCodes
	 *            源代码列表
	 * @return 编译成功返回 null， 编译出错返回错误信息。
	 * @throws IOException
	 */
	private String compileSrcFile(String srcDir, List<String> classNames, List<String> srcCodes) throws IOException {
		List<String> classpath = new LinkedList();

		// java 类路径
		StringTokenizer token = new StringTokenizer(System.getProperty("java.class.path") + ";" + System.getProperty("sun.boot.class.path"), ";");
		while (token.hasMoreElements()) {
			String path = token.nextToken();
			File jar = new File(path);
			if (jar.exists() && (jar.isFile() && (path.endsWith(".jar") || path.endsWith(".zip")))) {
				path = jar.getAbsolutePath();
				if (!classpath.contains(path))
					classpath.add(path);
			}
		}

		String webinfo = Cocit.me().getContextDir() + File.separator + "WEB-INF" + File.separator;

		// WEB-INF/classes 路径
		String path = webinfo + "classes" + File.separator;
		path = new File(path).getAbsolutePath();
		if (!classpath.contains(path))
			classpath.add(path);
		if (!Cocit.me().getConfig().isProductMode())
			classpath.add(StaticResourceFilter.classPathForDir);

		// WEB-INF/lib/*.jar zip 路径
		File libDir = new File(webinfo + "lib" + File.separator);
		if (libDir.exists() && libDir.isDirectory()) {
			for (File jar : libDir.listFiles()) {
				path = jar.getAbsolutePath();
				if (jar.exists() && jar.isFile() && (path.endsWith(".jar") || path.endsWith(".zip"))) {
					if (!classpath.contains(path))
						classpath.add(path);
				}
			}
		}
		libDir = new File("E:\\jsoftcocit\\build\\lib" + File.separator);
		if (libDir.exists() && libDir.isDirectory()) {
			for (File jar : libDir.listFiles()) {
				path = jar.getAbsolutePath();
				if (jar.exists() && jar.isFile() && (path.endsWith(".jar") || path.endsWith(".zip"))) {
					if (!classpath.contains(path))
						classpath.add(path);
				}
			}
		}
		libDir = new File("E:\\jsoftcocit\\build\\jars" + File.separator);
		if (libDir.exists() && libDir.isDirectory()) {
			for (File jar : libDir.listFiles()) {
				path = jar.getAbsolutePath();
				if (jar.exists() && jar.isFile() && (path.endsWith(".jar") || path.endsWith(".zip"))) {
					if (!classpath.contains(path))
						classpath.add(path);
				}
			}
		}

		// 实体模块编译单元
		SystemCompilationUnit[] units = new SystemCompilationUnit[classNames.size()];
		int unitCount = 0;
		File javaFile = null;
		for (String className : classNames) {
			String fileName = srcDir + File.separator + className.replace(".", File.separator);
			String srcCode = srcCodes.get(unitCount);
			javaFile = new File(fileName + ".java");

			units[unitCount] = new SystemCompilationUnit(className.substring(className.lastIndexOf(".") + 1), srcCode, javaFile, null);

			unitCount++;
		}

		File pkgDir = javaFile.getParentFile();

		String error = compileUnits(units, classpath, pkgDir);
		// String error = null;
		// try {
		// compile(pkgDir.getAbsolutePath(), tempDir);
		// } catch (Exception e1) {
		// error = e1.getMessage();
		// }
		if (!StringUtil.isBlank(error)) {
			return error;
		}

		for (SystemCompilationUnit unit : units) {
			OutputStream os = null;
			try {
				File classFile = new File(pkgDir.getAbsolutePath() + File.separator + unit.getName() + ".class");

				if (!classFile.exists()) {
					if (!classFile.getParentFile().exists()) {
						classFile.getParentFile().mkdirs();
					}
					classFile.createNewFile();
				}

				os = new FileOutputStream(classFile);
				os.write((byte[]) unit.getCompileData());

				os.flush();
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
					}
				}
			}
		}

		return null;
	}

	// private void compile(String javaFilePath, String classTargetPath) throws Exception {
	// DCompile dc = new DCompile();
	// File dir = new File(javaFilePath);
	// File[] files = dir.listFiles();
	// for (File file : files) {
	// dc.initialize(javaFilePath + "/" + file.getName(), classTargetPath, "UTF-8");
	// }
	// dc.compile();
	// }

	@SuppressWarnings("deprecation")
	private String compileUnits(SystemCompilationUnit[] units, List<String> jars, File tempDirFile) {
		StringBuffer problemBuffer = new StringBuffer();

		for (int i = jars.size() - 1; i >= 0; i--) {
			String jar = jars.get(i);
			if (jar.indexOf("\\eclipse\\") > -1) {
				jars.remove(i);
			}
		}

		StringBuffer logPaths = new StringBuffer("CLASSPATH：");
		String[] classpath = new String[jars.size()];
		int count = 0;
		for (String jar : jars) {
			classpath[count++] = jar;
			logPaths.append("\n").append(jar);
		}
		LogUtil.debug(logPaths.toString());

		ICompilationUnit[] compilationUnits = new ICompilationUnit[units.length];
		for (int i = 0; i < compilationUnits.length; i++) {
			compilationUnits[i] = new CompilationUnit(units[i].getSrcCode().toCharArray(), units[i].getSourceFile().getAbsolutePath(), "UTF-8");
		}

		count = 0;
		String[] javafiles = new String[units.length];
		for (SystemCompilationUnit unit : units) {
			javafiles[count++] = unit.getSourceFile().getAbsolutePath();
		}

		INameEnvironment env = new FileSystem(classpath, javafiles, "UTF-8");

		IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.proceedWithAllProblems();

		Map settings = getJdtSettings();

		IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());

		ICompilerRequestor requestor = getCompilerRequestor(units, problemBuffer);

		Compiler compiler = new Compiler(env, policy, settings, requestor, problemFactory);
		compiler.compile(compilationUnits);

		if (problemBuffer.length() > 0) {
			return problemBuffer.toString();
		}

		return null;
	}

	private ICompilerRequestor getCompilerRequestor(final SystemCompilationUnit[] units, final StringBuffer problemBuffer) {
		return new ICompilerRequestor() {
			public void acceptResult(CompilationResult result) {
				String className = new String(((CompilationUnit) result.getCompilationUnit()).mainTypeName);

				int classIdx;
				for (classIdx = 0; classIdx < units.length; ++classIdx) {
					if (className.equals(units[classIdx].getName())) {
						break;
					}
				}

				if (result.hasErrors()) {
					String sourceCode = units[classIdx].getSrcCode();

					IProblem[] problems = result.getErrors();
					for (int i = 0; i < problems.length; i++) {
						IProblem problem = problems[i];
						problemBuffer.append(i + 1);
						problemBuffer.append(". ");
						problemBuffer.append(problem.getMessage());

						if (problem.getSourceStart() >= 0 && problem.getSourceEnd() >= 0) {
							int problemStartIndex = sourceCode.lastIndexOf("\n", problem.getSourceStart()) + 1;
							int problemEndIndex = sourceCode.indexOf("\n", problem.getSourceEnd());
							if (problemEndIndex < 0) {
								problemEndIndex = sourceCode.length();
							}

							problemBuffer.append("\n");
							problemBuffer.append(sourceCode.substring(problemStartIndex, problemEndIndex));
							problemBuffer.append("\n");
							for (int j = problemStartIndex; j < problem.getSourceStart(); j++) {
								problemBuffer.append(" ");
							}
							if (problem.getSourceStart() == problem.getSourceEnd()) {
								problemBuffer.append("^");
							} else {
								problemBuffer.append("<");
								for (int j = problem.getSourceStart() + 1; j < problem.getSourceEnd(); j++) {
									problemBuffer.append("-");
								}
								problemBuffer.append(">");
							}
						}

						problemBuffer.append("\n");
					}
					problemBuffer.append(problems.length);
					problemBuffer.append(" errors\n");
				}
				if (problemBuffer.length() == 0) {
					ClassFile[] resultClassFiles = result.getClassFiles();
					for (int i = 0; i < resultClassFiles.length; i++) {
						units[classIdx].setCompileData(resultClassFiles[i].getBytes());
					}
				}
			}
		};
	}

	private Map getJdtSettings() {
		Map settings = new HashMap();
		settings.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
		settings.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
		settings.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.IGNORE);
		// settings.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_5);
		// settings.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_5);
		// settings.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_5);

		settings.put(CompilerOptions.OPTION_Encoding, "UTF-8");
		// Source JVM
		settings.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_6);
		// Target JVM
		settings.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_6);
		settings.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_6);

		Properties systemProps = System.getProperties();
		for (Enumeration it = systemProps.propertyNames(); it.hasMoreElements();) {
			String propName = (String) it.nextElement();
			if (propName.startsWith("org.eclipse.jdt.core.")) {
				String propVal = systemProps.getProperty(propName);
				if (propVal != null && propVal.length() > 0) {
					settings.put(propName, propVal);
				}
			}
		}

		return settings;
	}

	private void makeJavaFile(String tempDir, String className, String srcCode) throws IOException {
		File javaFile = new File(tempDir + File.separator + className.replace(".", File.separator) + ".java");
		if (!javaFile.exists()) {
			javaFile.getParentFile().mkdirs();
			javaFile.createNewFile();
		}
		Reader reader = null;
		Writer writer = null;
		try {
			reader = new StringReader(srcCode);
			OutputStream os = new FileOutputStream(javaFile);
			writer = new OutputStreamWriter(os, "UTF-8");
			int bytesRead = 0;
			char[] buffer = new char[8192];
			while ((bytesRead = reader.read(buffer, 0, 8192)) != -1) {
				writer.write(buffer, 0, bytesRead);
			}
			writer.flush();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}

			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
				}

		}

	}

	@SuppressWarnings("unused")
	private static class SystemCompilationUnit {
		private final String name;

		private final String source;

		private final File sourceFile;

		private final List expressions;

		private Serializable compileData;

		public SystemCompilationUnit(String name, String sourceCode, File sourceFile, List expressions) {
			this.name = name;
			this.source = sourceCode;
			this.sourceFile = sourceFile;
			this.expressions = expressions;
		}

		public String getName() {
			return name;
		}

		public String getSrcCode() {
			return source;
		}

		public File getSourceFile() {
			return sourceFile;
		}

		public List getExpressions() {
			return expressions;
		}

		public void setCompileData(Serializable compileData) {
			this.compileData = compileData;
		}

		public Serializable getCompileData() {
			return compileData;
		}
	}
}
