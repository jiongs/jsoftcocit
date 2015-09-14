package com.jsoft.cocimpl.dmengine.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.nutz.castor.Castors;
import org.nutz.json.Json;
import org.nutz.lang.Files;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.resource.Scans;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.jsoft.cocimpl.CocitImpl;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.baseentity.DataEntity;
import com.jsoft.cocit.baseentity.IDataEntity;
import com.jsoft.cocit.baseentity.INamedEntity;
import com.jsoft.cocit.baseentity.IOfTenantEntity;
import com.jsoft.cocit.baseentity.ITreeEntity;
import com.jsoft.cocit.baseentity.ITreeEntityExt;
import com.jsoft.cocit.baseentity.ITreeObjectEntityExt;
import com.jsoft.cocit.baseentity.NamedEntity;
import com.jsoft.cocit.baseentity.coc.ICocActionEntity;
import com.jsoft.cocit.baseentity.coc.ICocActionEntityExt;
import com.jsoft.cocit.baseentity.coc.ICocCatalogEntity;
import com.jsoft.cocit.baseentity.coc.ICocCatalogEntityExt;
import com.jsoft.cocit.baseentity.coc.ICocEntity;
import com.jsoft.cocit.baseentity.coc.ICocEntityExt;
import com.jsoft.cocit.baseentity.coc.ICocFieldEntity;
import com.jsoft.cocit.baseentity.coc.ICocFieldEntityExt;
import com.jsoft.cocit.baseentity.coc.ICocGroupEntity;
import com.jsoft.cocit.baseentity.coc.ICocGroupEntityExt;
import com.jsoft.cocit.baseentity.cui.ICuiEntityExt;
import com.jsoft.cocit.baseentity.cui.ICuiFormActionExt;
import com.jsoft.cocit.baseentity.cui.ICuiFormEntityExt;
import com.jsoft.cocit.baseentity.cui.ICuiFormFieldEntityExt;
import com.jsoft.cocit.baseentity.cui.ICuiGridEntityExt;
import com.jsoft.cocit.baseentity.cui.ICuiGridFieldEntityExt;
import com.jsoft.cocit.baseentity.security.ISystemEntityExt;
import com.jsoft.cocit.baseentity.security.ISystemMenuEntity;
import com.jsoft.cocit.baseentity.security.ISystemMenuEntityExt;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.constant.FieldNames;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.constant.StatusCodes;
import com.jsoft.cocit.dmengine.IDataModelEngine;
import com.jsoft.cocit.dmengine.annotation.CocAction;
import com.jsoft.cocit.dmengine.annotation.CocCatalog;
import com.jsoft.cocit.dmengine.annotation.CocColumn;
import com.jsoft.cocit.dmengine.annotation.CocEntity;
import com.jsoft.cocit.dmengine.annotation.CocGroup;
import com.jsoft.cocit.dmengine.annotation.Cui;
import com.jsoft.cocit.dmengine.annotation.CuiEntity;
import com.jsoft.cocit.dmengine.annotation.CuiForm;
import com.jsoft.cocit.dmengine.annotation.CuiFormAction;
import com.jsoft.cocit.dmengine.annotation.CuiFormField;
import com.jsoft.cocit.dmengine.annotation.CuiGrid;
import com.jsoft.cocit.dmengine.annotation.CuiGridField;
import com.jsoft.cocit.dmengine.bizplugin.IBizPlugin;
import com.jsoft.cocit.dmengine.field.IExtField;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.log.Log;
import com.jsoft.cocit.log.Logs;
import com.jsoft.cocit.orm.IExtOrm;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.orm.expr.ExprRule;
import com.jsoft.cocit.securityengine.ILoginSession;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ConstUtil;
import com.jsoft.cocit.util.DateUtil;
import com.jsoft.cocit.util.ExcelUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.FileUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.SortUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

/**
 * 
 * @preserve
 * @author Ji Yongshan
 * 
 */
public class CocEntityEngineImpl implements IDataModelEngine {

	private static Log log = Logs.getLog(IDataModelEngine.class);

	private Map<Long, Class> dynamicEntityTypes;

	private Map<String, Class> packageEntityTypes;

	private Map<Long, IBizPlugin[]> bizPlugins;

	private CocEntityCompiler compiler;

	public CocEntityEngineImpl() {
		compiler = new CocEntityCompiler(this);

		dynamicEntityTypes = new HashMap();
		packageEntityTypes = new HashMap();
		bizPlugins = new HashMap();
	}

	public void release() {
		synchronized (CocEntityEngineImpl.class) {
			dynamicEntityTypes.clear();
			// packageEntityTypes.clear();
			bizPlugins.clear();
		}
	}

	public synchronized void setupCocitDBTables() {
		final CocitImpl coc = (CocitImpl) Cocit.me();
		IExtOrm orm = (IExtOrm) coc.orm();

		Iterator<Class> types = packageEntityTypes.values().iterator();
		while (types.hasNext()) {
			try {
				orm.getEnMapping(types.next());
			} catch (Throwable e) {
			}
		}
	}

	public synchronized void setupCocitFromPackage() {
		final CocitImpl coc = (CocitImpl) Cocit.me();

		Trans.exec(new Atom() {
			public void run() {

				LogUtil.info("EntityEngine..setupCocitFromPackage: ......");

				ICocConfig config = coc.getConfig();
				IOrm orm = coc.orm();// coc.getProxiedORM();

				parseDefaultSystemAndTenant(orm);

				parseCocEntitiesFromPackage(orm);

				String tenantCode = "";
				parseEntityDatasFromPackage(orm, tenantCode);

				String systemCode = config.getCocitSystemCode();
				parseCocEntityMenus(orm, systemCode);

				String contextDir = Cocit.me().getContextDir();
				File dataDir = new File(contextDir + "/WEB-INF/data");
				File[] files = dataDir.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.isDirectory())
							continue;

						if (file.exists()) {
							String newName = file.getParentFile().getAbsolutePath() + File.separator + DateUtil.getNowDate() + File.separator + file.getName();
							FileUtil.rename(file, newName);
						}
					}
				}
			}
		});
	}

	public synchronized void setupCocitFromXls(final File excelFile) {
		Trans.exec(new Atom() {
			public void run() {

				LogUtil.info("EntityEngine..setupCocitFromXls: ...... [excel: %s]", excelFile.getAbsolutePath());

				IOrm orm = Cocit.me().orm();
				List list = parseCocEntitiesFromXls(orm, excelFile);

				// if (LogUtil.isDebugEnabled()) {
		        // StringBuffer sb = new StringBuffer();
		        // for (Object obj : list) {
		        // if (obj instanceof IDataEntity)
		        // sb.append("\n").append(obj.getClass().getSimpleName()).append(((IDataEntity)
		        // obj).toJson());
		        // }
		        // LogUtil.debug(sb.toString());
		        // }

				orm.save(list);

				for (Object obj : list) {
					if (obj instanceof ICocEntity) {
						// IEntityModule mdl = (IEntityModule) obj;
		                // List<String> msgs =
		                // compileModule(Cocit.me().getEntityServiceFactory().getEntityModule(mdl.getId()));

						// if (LogUtil.isDebugEnabled()) {
		                // StringBuffer sb = new StringBuffer();
		                // for (String msg : msgs) {
		                // sb.append("\n").append(msg);
		                // }
		                // LogUtil.debug(sb.toString());
		                // }

					}
				}
			}
		});
	}

	protected IExtOrm orm() {
		return (IExtOrm) Cocit.me().orm();
	}

	public void initEntityTypes() {
		List<String> entityPackages = new ArrayList();
		List<String> list = Cocit.me().getBeansConfig().getEntityPackages();
		for (String p : list) {
			entityPackages.add(p);
		}
		for (String p : Const.ENTITY_PACKAGES) {
			entityPackages.add(p);
		}
		for (String pkg : entityPackages) {

			List<Class<?>> types = Scans.me().scanPackage(pkg);

			for (Class type : types) {

				try {

					CocEntity ann = (CocEntity) type.getAnnotation(CocEntity.class);
					if (ann == null)
						continue;

					int status = EntityTypes.setupEntityType(type);
					if (status == 2) {// COC预置类已经存在
						continue;
					}

					if (ann != null) {
						String entityCode = ann.code();
						if (StringUtil.isBlank(entityCode)) {
							entityCode = type.getSimpleName();
						}

						packageEntityTypes.put(entityCode, type);
					}

				} catch (Throwable e) {
					LogUtil.error("EntityEngine..initEntityTypes: Error! %s", e);
				}
			}
		}
	}

	public Iterator<Class> getClassOfEntityIterator() {
		return this.packageEntityTypes.values().iterator();
	}

	public Class getClassOfEntity(String moduleCode) {
		return this.packageEntityTypes.get(moduleCode);
	}

	private void parseDefaultSystemAndTenant(IOrm orm) throws CocException {
		ICocConfig config = Cocit.me().getConfig();

		List<ISystemEntityExt> systems = new ArrayList();
		/*
		 * 安装平台管理系统
		 */
		String systemCode = config.getCocitSystemCode();
		String systemName = config.getCocitSystemName();
		ISystemEntityExt system = (ISystemEntityExt) orm.get(EntityTypes.System, systemCode);
		if (system == null) {
			system = (ISystemEntityExt) Mirror.me(EntityTypes.System).born();
		}
		system.setCode(systemCode);
		system.setName(systemName);
		system.setStatusCode(Const.STATUS_CODE_BUILDIN);
		orm.save(system);
		systems.add(system);

		/*
		 * 安装默认应用系统
		 */
		systemCode = config.getDefaultSystemCode();
		systemName = config.getDefaultSystemName();
		if (StringUtil.hasContent(systemCode)) {
			system = (ISystemEntityExt) orm.get(EntityTypes.System, systemCode);
			if (system == null) {
				system = (ISystemEntityExt) Mirror.me(EntityTypes.System).born();
			}
			system.setCode(systemCode);
			system.setName(systemName);
			system.setStatusCode(Const.STATUS_CODE_BUILDIN);
			orm.save(system);
			systems.add(system);
		}

		/*
		 * 清除垃圾系统
		 */
		List list = orm.query(EntityTypes.System, Expr.eq(Const.F_STATUS_CODE, Const.STATUS_CODE_BUILDIN));
		for (Object sys : list) {
			if (!systems.contains(sys)) {
				orm.delete(sys);
			}
		}

		/*
		 * 租户不支持默认值
		 */
		// IExtTenant tenant = (IExtTenant) orm.get(EntityTypes.Tenant,
		// tenantCode);
		// if (tenant == null) {
		// tenant = (IExtTenant) Mirror.me(EntityTypes.Tenant).born();
		// tenant.setCode(tenantCode);
		// tenant.setName(tenantName);
		// tenant.setSystemCode(systemCode);
		// tenant.setStatusCode(Const.STATUS_CODE_BUILDIN);
		// orm.save(tenant);
		// }
		//
		// list = (List<IExtTenant>) orm.query(EntityTypes.Tenant);
		// for (Object t : list) {
		// if (!tenant.equals(t)) {
		// orm.delete(t);
		// }
		// }
	}

	private List parseCocEntitiesFromXls(IOrm orm, File excelFile) throws CocException {
		List result = new LinkedList();
		List<String[]> excelRows = null;

		try {
			excelRows = ExcelUtil.parseExcel(excelFile);

			/*
			 * log 查看Excel内容
			 */
			if (LogUtil.isDebugEnabled()) {
				StringBuffer sb = new StringBuffer();
				for (String[] row : excelRows) {
					sb.append("\n");
					for (String str : row) {
						sb.append(str).append(",    ");
					}
				}
				LogUtil.debug("EntityEngine..parseCocEntitiesFromXls: excelRows = %s", sb);
			}

			List<String[]> moduleRows = new ArrayList();
			List<String[]> actionRows = new ArrayList();
			List<String[]> groupRows = new ArrayList();
			int flag = 0;// 1:module rows; 2:action rows; 3 group rows;
			for (String[] row : excelRows) {
				String flagStr = row[0];
				if (flagStr == null) {
					flagStr = "";
				} else {
					flagStr = flagStr.trim();
				}

				if (flagStr.contains("(@CocEntity)") || flagStr.contains("（@CocEntity）")) {
					flag = 1;
					if (moduleRows.size() > 0) {
						result.addAll(parseCocEntityFromXls(orm, moduleRows, actionRows, groupRows));
					}

					moduleRows = new ArrayList();
					actionRows = new ArrayList();
					groupRows = new ArrayList();
					continue;
				} else if (flagStr.contains("(@CocAction)") || flagStr.contains("（@CocAction）")) {
					flag = 2;
					continue;
				} else if (flagStr.contains("(@CocGroup)") || flagStr.contains("（@CocGroup）")) {
					flag = 3;
					groupRows.add(row);
					continue;
				}

				if (flag == 1) {
					moduleRows.add(row);
				} else if (flag == 2) {
					actionRows.add(row);
				} else if (flag == 3) {
					groupRows.add(row);
				}
			}

			if (moduleRows.size() > 0) {
				result.addAll(parseCocEntityFromXls(orm, moduleRows, actionRows, groupRows));
			}

		} catch (Throwable e) {
			LogUtil.error("EntityEngine..parseCocEntitiesFromXls: 解析Excel实体模块出错！", e);

			throw new CocException(e);
		}

		LogUtil.info("EntityEngine..parseCocEntitiesFromXls: result.size = %s [excelFile: %s, excelRows.size: %s]", result.size(), excelFile.getAbsolutePath(), excelRows == null ? 0 : excelRows.size());

		return result;
	}

	private List parseCocEntityFromXls(IOrm orm, List<String[]> moduleRows, List<String[]> actionRows, List<String[]> groupRows) {
		List ret = new ArrayList();

		if (moduleRows == null || moduleRows.size() < 2) {
			return null;
		}

		String[] titles = moduleRows.get(0);
		String[] props = new String[titles.length];
		int keyIndex = -1;

		for (int i = 0; i < titles.length; i++) {
			String title = titles[i];

			if (title == null || title.trim().length() == 0)
				continue;

			int from = title.indexOf("(");
			if (from < 0) {
				from = title.indexOf("（");
			}
			int to = title.indexOf(")");
			if (to < 0) {
				to = title.indexOf("）");
			}

			if (from < 0 || to < 0) {
				continue;
			}

			String propName = title.substring(from + 1, to).trim();
			props[i] = propName;

			if (Const.F_CODE.equals(propName)) {
				keyIndex = i;
			}

		}

		String[] row = moduleRows.get(1);

		/*
		 * 创建EntityModule
		 */
		String moduleCode = null;
		if (keyIndex >= 0)
			moduleCode = row[keyIndex];
		if (moduleCode == null || moduleCode.trim().length() == 0) {
			return ret;
		}

		Map<String, String> entityModulePropsMap = new HashMap();
		entityModulePropsMap.put("catalog", "catalogCode");

		ICocEntityExt module = (ICocEntityExt) orm.get(EntityTypes.CocEntity, CndExpr.eq(Const.F_CODE, moduleCode));
		if (module == null)
			module = (ICocEntityExt) ClassUtil.newInstance(EntityTypes.CocEntity);

		String packageName = null;
		String className = null;
		String extendsName = "";
		for (int i = 0; i < row.length; i++) {
			String propName = props[i];
			String propValue = row[i];

			if (propName == null) {
				continue;
			}

			String mappingPropName = entityModulePropsMap.get(propName);
			if (mappingPropName != null) {
				propName = mappingPropName;
			}

			if ("package".equals(propName)) {
				packageName = propValue;
			} else if ("class".equals(propName)) {
				className = propValue;
			} else if ("extends".equals(propName)) {
				extendsName = propValue;
			} else if ("properties".equals(propName)) {
				List<String> extProps = StringUtil.toList(propValue, "\n\r");
				for (String extProp : extProps) {
					try {
						int idx = extProp.indexOf("=");
						int idx2 = extProp.indexOf(":");
						if (idx2 < 0) {
							idx2 = extProp.indexOf("：");
						}
						if (idx < 0) {
							idx = idx2;
						} else if (idx2 > 0 && idx2 < idx) {
							idx = idx2;
						}

						if (idx < 0) {
							continue;
						}

						String extPropName = extProp.substring(0, idx);
						String extPropValue = extProp.substring(idx + 1);

						mappingPropName = entityModulePropsMap.get(extPropName);
						if (mappingPropName != null) {
							extPropName = mappingPropName;
						}

						ObjectUtil.setValue(module, extPropName, extPropValue);

					} catch (Throwable e) {
						LogUtil.warn("EntityEngine..parseCocEntityFromXls: Warn! [%s = %s] %s", propName, propValue, e);
					}
				}
			} else {
				try {
					ObjectUtil.setValue(module, propName, propValue);
				} catch (Throwable e) {
					LogUtil.warn("EntityEngine..parseCocEntityFromXls: Warn! [%s = %s]  %s", propName, propValue, e);
				}
			}
		}

		module.setClassName(packageName + "." + className);
		module.setExtendsClassName(extendsName);

		LogUtil.info("EntityEngine..parseCocEntityFromXls: entityModule = %s", module.toJsonString());

		ret.add(module);

		List<ICocActionEntity> actions = this.parseCocActionsFromXls(orm, module.getCode(), actionRows);
		ret.addAll(actions);
		List groups = this.parseCocFieldOrGroupsFromXls(orm, module.getCode(), groupRows);
		ret.addAll(groups);

		/*
		 * 安装模块菜单
		 */
		String systemCode = "";
		ISystemMenuEntityExt systemMenu = (ISystemMenuEntityExt) orm.get(EntityTypes.SystemMenu, CndExpr.eq(Const.F_CODE, moduleCode).and(com.jsoft.cocit.util.ExprUtil.systemIs(systemCode)));
		if (systemMenu == null) {
			systemMenu = (ISystemMenuEntityExt) ClassUtil.newInstance(EntityTypes.SystemMenu);
		}
		systemMenu.setName(module.getName());
		systemMenu.setSn(module.getSn());
		systemMenu.setCode(moduleCode);
		systemMenu.setType(Const.MENU_TYPE_ENTITY);
		systemMenu.setRefEntity(module.getCode());
		// systemMenu.setEntityModuleName(module.getName());
		// systemMenu.setPathPrefix(module.getPathPrefix());
		systemMenu.setParentCode(module.getCatalogCode());
		systemMenu.setParentName(module.getCatalogName());
		systemMenu.setSystemCode(systemCode);

		LogUtil.info("EntityEngine..parseCocEntityFromXls: systemMenu = %s", systemMenu.toJsonString());

		ret.add(systemMenu);

		/*
		 * 清除垃圾数据
		 */
		// if (false) {
		List<ICocActionEntity> oldActions = (List<ICocActionEntity>) orm.query(EntityTypes.CocAction, Expr.eq(Const.F_COC_ENTITY_CODE, module.getCode()));
		for (ICocActionEntity a : oldActions) {
			if (!ret.contains(a)) {

				if (LogUtil.isInfoEnabled()) {
					LogUtil.info("EntityEngine..parseCocEntityFromXls: 清除垃圾操作！ %s", a.toJsonString());
				}

				orm.delete(a);
			}
		}
		// }
		List<ICocFieldEntityExt> oldFields = (List<ICocFieldEntityExt>) orm.query(EntityTypes.CocField, Expr.eq(Const.F_COC_ENTITY_CODE, module.getCode()));
		for (ICocFieldEntityExt a : oldFields) {
			if (!ret.contains(a)) {

				if (LogUtil.isInfoEnabled()) {
					LogUtil.info("EntityEngine..parseCocEntityFromXls: 清除垃圾字段！ %s", a.toJsonString());
				}

				orm.delete(a);
			}
		}
		List<ICocGroupEntityExt> oldGroups = (List<ICocGroupEntityExt>) orm.query(EntityTypes.CocGroup, Expr.eq(Const.F_COC_ENTITY_CODE, module.getCode()));
		for (ICocGroupEntityExt a : oldGroups) {
			if (!ret.contains(a)) {

				if (LogUtil.isInfoEnabled()) {
					LogUtil.info("EntityEngine..parseCocEntityFromXls: 清除垃圾字段组！ %s", a.toJsonString());
				}

				orm.delete(a);
			}
		}

		return ret;
	}

	private List parseCocFieldOrGroupsFromXls(IOrm orm, String moduleCode, List<String[]> excelRows) {
		List ret = new LinkedList();

		if (excelRows == null) {
			return null;
		}

		ICocGroupEntityExt entityFieldGroup = null;
		List<String[]> fieldRows = new ArrayList();
		boolean isField = false;
		int groupCount = 1;
		for (String[] row : excelRows) {
			String title = row[0];
			if (title.contains("(@CocGroup)") || title.contains("（@CocGroup）")) {
				if (fieldRows.size() > 0 && entityFieldGroup != null) {
					List<ICocFieldEntity> fields = parseCocFieldsFromXls(orm, moduleCode, entityFieldGroup.getCode(), fieldRows);
					ret.addAll(fields);
				}
				fieldRows = new ArrayList();

				isField = false;

				int from = title.indexOf("(");
				if (from < 0) {
					from = title.indexOf("（");
				}
				if (from > -1) {
					title = title.substring(0, from);
				}

				/*
				 * 创建FieldGroup
				 */
				String groupCode = StringUtil.toPinyinFirstChar(title);
				entityFieldGroup = (ICocGroupEntityExt) orm.get(EntityTypes.CocGroup, CndExpr.eq(Const.F_CODE, groupCode).and(CndExpr.eq(Const.F_COC_ENTITY_CODE, moduleCode)));
				if (entityFieldGroup == null) {
					entityFieldGroup = (ICocGroupEntityExt) ClassUtil.newInstance(EntityTypes.CocGroup);
				}
				entityFieldGroup.setName(title);
				entityFieldGroup.setCode(groupCode);
				entityFieldGroup.setCocEntityCode(moduleCode);
				entityFieldGroup.setSn(groupCount);

				LogUtil.info("EntityEngine..parseCocFieldOrGroupsFromXls: result = %s", entityFieldGroup.toJsonString());

				ret.add(entityFieldGroup);

				groupCount++;
			} else {
				isField = true;
			}

			if (isField) {
				fieldRows.add(row);
			}
		}

		if (fieldRows.size() > 0 && entityFieldGroup != null) {
			List<ICocFieldEntity> fields = parseCocFieldsFromXls(orm, moduleCode, entityFieldGroup.getCode(), fieldRows);
			ret.addAll(fields);
		}

		return ret;
	}

	private List<ICocFieldEntity> parseCocFieldsFromXls(IOrm orm, String moduleCode, String groupCode, List<String[]> excelRows) {
		List<ICocFieldEntity> ret = new LinkedList();

		if (excelRows == null || excelRows.size() < 2) {
			return null;
		}

		Map<String, String> entityFieldPropsMap = new HashMap();
		entityFieldPropsMap.put("fieldName", "dbColumnName");
		entityFieldPropsMap.put("refEntity", "fkTargetModule");
		entityFieldPropsMap.put("refColumn", "fkTargetField");
		entityFieldPropsMap.put("fieldName", "dbColumnName");
		entityFieldPropsMap.put("fieldName", "dbColumnName");

		String[] titles = excelRows.get(0);
		String[] props = new String[titles.length];
		int nameIndex = -1;
		int propNameIndex = -1;
		int dbColumnNameIndex = -1;

		for (int i = 0; i < titles.length; i++) {
			String title = titles[i];

			if (title == null || title.trim().length() == 0)
				continue;

			int from = title.indexOf("(");
			if (from < 0) {
				from = title.indexOf("（");
			}
			int to = title.indexOf(")");
			if (to < 0) {
				to = title.indexOf("）");
			}

			if (from < 0 || to < 0) {
				continue;
			}

			String propName = title.substring(from + 1, to).trim();
			String mappingPropName = entityFieldPropsMap.get(propName);
			if (mappingPropName != null) {
				propName = mappingPropName;
			}

			props[i] = propName;

			if (Const.F_PROP_NAME.equals(propName)) {
				propNameIndex = i;
			}
			if ("dbColumnName".equals(propName)) {
				dbColumnNameIndex = i;
			}
			if (Const.F_NAME.equals(propName)) {
				nameIndex = i;
			}
		}

		int len = excelRows.size();
		for (int i = 1; i < len; i++) {
			String[] row = excelRows.get(i);

			/*
			 * 计算字段名、属性名、中文名
			 */
			String name = null;
			if (nameIndex >= 0)
				name = row[nameIndex];
			if (name == null || name.trim().length() == 0) {
				continue;
			}

			String propName = null;
			if (propNameIndex >= 0)
				propName = row[propNameIndex];
			String dbColumnName = null;
			if (dbColumnNameIndex >= 0)
				dbColumnName = row[dbColumnNameIndex];

			if (StringUtil.isBlank(dbColumnName)) {
				if (StringUtil.isBlank(propName)) {
					propName = StringUtil.toPinyinFirstChar(name).toLowerCase();
				}
			} else {
				if (StringUtil.isBlank(propName)) {
					propName = dbColumnName.toLowerCase();
				}
			}

			if (StringUtil.isBlank(propName)) {
				continue;
			}

			/*
			 * 创建EntityField
			 */
			ICocFieldEntityExt entityField = (ICocFieldEntityExt) orm.get(EntityTypes.CocField, CndExpr.eq(Const.F_PROP_NAME, propName).and(CndExpr.eq(Const.F_COC_ENTITY_CODE, moduleCode)));
			if (entityField == null) {
				entityField = (ICocFieldEntityExt) ClassUtil.newInstance(EntityTypes.CocField);
			}

			for (int j = 0; j < row.length; j++) {
				String value = row[j];

				if (j >= props.length)
					continue;

				String prop = props[j];
				String mappingPropName = entityFieldPropsMap.get(prop);
				if (mappingPropName != null) {
					prop = mappingPropName;
				}

				if (prop == null || prop.trim().length() == 0) {
					continue;
				}
				prop = prop.trim();

				if ("type".equals(prop)) {
					entityField.setFieldType(ConstUtil.getTypeCode(value));
				}

				/*
				 * 解析扩展属性
				 */
				if ("properties".equals(prop)) {
					List<String> extProps = StringUtil.toList(value, "\n\r");
					for (String extProp : extProps) {
						try {
							int idx = extProp.indexOf("=");
							int idx2 = extProp.indexOf(":");
							if (idx2 < 0) {
								idx2 = extProp.indexOf("：");
							}
							if (idx < 0) {
								idx = idx2;
							} else if (idx2 > 0 && idx2 < idx) {
								idx = idx2;
							}

							if (idx < 0) {
								continue;
							}

							String extPropName = extProp.substring(0, idx).trim();
							mappingPropName = entityFieldPropsMap.get(extPropName);
							if (mappingPropName != null) {
								extPropName = mappingPropName;
							}

							String extPropValue = extProp.substring(idx + 1);

							ObjectUtil.setValue(entityField, extPropName, extPropValue);
						} catch (Throwable e) {
							LogUtil.warn("EntityEngine..parseCocFieldsFromXls: Warn! [%s = %s] %s", prop, value, e);
						}
					}
				} else {
					try {
						ObjectUtil.setValue(entityField, prop, value);
					} catch (Throwable e) {
						LogUtil.warn("EntityEngine..parseCocFieldsFromXls: Warn! [%s = %s] %s", prop, value, e);
					}
				}
			}

			entityField.setFieldName(propName);
			entityField.setName(name);
			entityField.setCode(dbColumnName);

			entityField.setCocEntityCode(moduleCode);
			entityField.setCocGroupCode(groupCode);

			LogUtil.info("EntityEngine..parseCocFieldsFromXls: entityField = %s", entityField.toJsonString());

			ret.add(entityField);
		}

		return ret;

	}

	private List<ICocActionEntity> parseCocActionsFromXls(IOrm orm, String moduleCode, List<String[]> excelRows) {
		List<ICocActionEntity> ret = new LinkedList();

		if (excelRows == null || excelRows.size() < 2) {
			return null;
		}

		String[] titles = excelRows.get(0);
		String[] props = new String[titles.length];
		int keyIndex = -1;

		for (int i = 0; i < titles.length; i++) {
			String title = titles[i];

			if (title == null || title.trim().length() == 0)
				continue;

			int from = title.indexOf("(");
			if (from < 0) {
				from = title.indexOf("（");
			}
			int to = title.indexOf(")");
			if (to < 0) {
				to = title.indexOf("）");
			}

			if (from < 0 || to < 0) {
				continue;
			}

			String propName = title.substring(from + 1, to).trim();
			props[i] = propName;

			if (Const.F_CODE.equals(propName)) {
				keyIndex = i;
			}

		}

		int len = excelRows.size();
		for (int i = 1; i < len; i++) {
			String[] row = excelRows.get(i);

			/*
			 * 创建EntityAction
			 */
			String actionCode = null;
			if (keyIndex >= 0) {
				actionCode = row[keyIndex];
			}
			if (actionCode == null || actionCode.trim().length() == 0) {
				continue;
			}
			ICocActionEntityExt entityAction = (ICocActionEntityExt) orm.get(EntityTypes.CocAction, CndExpr.eq(Const.F_CODE, actionCode).and(CndExpr.eq(Const.F_COC_ENTITY_CODE, moduleCode)));
			if (entityAction == null) {
				entityAction = (ICocActionEntityExt) ClassUtil.newInstance(EntityTypes.CocAction);
			}
			for (int j = 0; j < row.length; j++) {
				String value = row[j];
				String prop = props[j];

				if (prop == null || prop.trim().length() == 0) {
					continue;
				}

				/*
				 * 根据操作编号，自动设置操作码；以c开头为创建；以e开头为修改；以d开头为物理删除；以r开头为逻辑删除；以v开头为查看； 以xi开头为XLS导入；以xo开头为导出到XLS；
				 */
				if (prop.equals("code")) {
					if (entityAction.getOpCode() == null || entityAction.getOpCode() <= 0) {
						if (value.startsWith("c")) {
							entityAction.setOpCode(OpCodes.OP_INSERT_FORM_DATA);
						} else if (value.startsWith("e")) {
							entityAction.setOpCode(OpCodes.OP_UPDATE_FORM_DATA);
						} else if (value.startsWith("d")) {
							entityAction.setOpCode(OpCodes.OP_DELETE_ROWS);
						} else if (value.startsWith("r")) {
							entityAction.setOpCode(OpCodes.OP_REMOVE_ROWS);
						} else if (value.startsWith("v")) {
							entityAction.setOpCode(OpCodes.OP_UPDATE_FORM_DATA);
						} else if (value.startsWith("xi")) {
							entityAction.setOpCode(OpCodes.OP_IMPORT_XLS);
						} else if (value.startsWith("xo")) {
							entityAction.setOpCode(OpCodes.OP_EXPORT_XLS);
						}
					}
				}

				/*
				 * 解析扩展属性
				 */
				if ("properties".equals(prop)) {
					List<String> extProps = StringUtil.toList(value, "\n\r");
					for (String extProp : extProps) {
						try {
							int idx = extProp.indexOf("=");
							int idx2 = extProp.indexOf(":");
							if (idx2 < 0) {
								idx2 = extProp.indexOf("：");
							}
							if (idx < 0) {
								idx = idx2;
							} else if (idx2 < idx) {
								idx = idx2;
							}

							if (idx < 0) {
								continue;
							}

							String extPropName = extProp.substring(0, idx);
							String extPropValue = extProp.substring(idx + 1);

							ObjectUtil.setValue(entityAction, extPropName, extPropValue);
						} catch (Throwable e) {
							LogUtil.warn("EntityEngine..parseCocActionsFromXls: Warn! [%s = %s] %s", prop, value, e);
						}
					}
				} else {
					try {
						ObjectUtil.setValue(entityAction, prop, value);
					} catch (Throwable e) {
						LogUtil.warn("EntityEngine..parseCocActionsFromXls: Warn! [%s = %s] %s", prop, value, e);
					}
				}
			}

			entityAction.setCocEntityCode(moduleCode);

			LogUtil.info("EntityEngine..parseCocActionsFromXls: entityAction = %s", entityAction.toJsonString());

			ret.add(entityAction);
		}

		return ret;

	}

	private List<ICocEntity> parseCocEntitiesFromPackage(IOrm orm) throws CocException {
		List<ICocEntity> newEntityList = new LinkedList();

		Iterator<Class> types = packageEntityTypes.values().iterator();
		while (types.hasNext()) {
			ICocEntity module = parseCocEntityFromClass(orm, types.next(), true);
			if (module != null) {
				newEntityList.add(module);
			}
		}

		List<ICocEntityExt> oldEntityList = (List<ICocEntityExt>) orm.query(EntityTypes.CocEntity);
		for (ICocEntityExt a : oldEntityList) {
			if (!newEntityList.contains(a) && a.isBuildin()) {

				if (LogUtil.isInfoEnabled()) {
					LogUtil.info("EntityEngine..parseCocEntitiesFromPackage: 删除垃圾实体！%s", a.toJsonString());
				}

				orm.delete(a);
			}
		}

		return newEntityList;
	}

	private ICocEntity parseCocEntityFromClass(IOrm orm, Class classOfEntity, boolean autoUpdate) throws CocException {
		LogUtil.info("EntityEngine..parseCocEntityFromClass ... [classOfEntity: %s]", classOfEntity);

		if (classOfEntity == null) {
			return null;
		}

		Mirror mirrorOfEntity = Mirror.me(classOfEntity);
		String tenantCode = null;

		/*
		 * 获取@CocEntity注释
		 */
		CocEntity $CocEntity = (CocEntity) classOfEntity.getAnnotation(CocEntity.class);
		if ($CocEntity == null) {
			return null;
		}

		// Entity $Entity = (Entity) classOfEntity.getAnnotation(Entity.class);
		// if ($Entity == null) {
		// return null;
		// }

		/*
		 * 解析 @CocEntity
		 */
		int cocEntitySN = $CocEntity.sn();
		String cocEntityCode = $CocEntity.code();
		if (StringUtil.isBlank(cocEntityCode)) {
			cocEntityCode = classOfEntity.getSimpleName();
		}

		ICocEntityExt cocEntity = (ICocEntityExt) orm.get(EntityTypes.CocEntity, CndExpr.eq(Const.F_CODE, cocEntityCode));
		try {
			cocEntity = this.parseCocEntityFromAnnotation(orm, tenantCode, classOfEntity, cocEntity, $CocEntity, cocEntitySN, cocEntityCode);
		} catch (Throwable e) {
			throw new CocException("解析实体定义(%s)出错：%s ", classOfEntity.getSimpleName(), ExceptionUtil.msg(e));
		}
		if (cocEntity != null) {
			if (!classOfEntity.getName().equals(cocEntity.getClassName())) {
				throw new CocException("有两个实体使用了相同的KEY！[classOfEntity:%s, existedClassOfEntity: %s]", //
				        classOfEntity.getName(), //
				        cocEntity.getClassName()//
				);
			}

			if (!autoUpdate) {
				return cocEntity;
			}
		}

		orm.save(cocEntity);

		/*
		 * 解析 @CocAction
		 */
		LogUtil.info("解析实体操作... [classOfEntity: %s]", classOfEntity.getSimpleName());

		List<ICocActionEntity> newCocActionList = new LinkedList();
		CocAction[] $CocActions = $CocEntity.actions();
		if ($CocActions != null) {
			int cocActionSN = cocEntitySN * 100;
			for (int i = 0; i < $CocActions.length; i++) {
				CocAction $CocAction = $CocActions[i];
				try {
					if (StringUtil.isBlank($CocAction.importFromFile())) {
						ICocActionEntity cocAction = this.parseCocAction(orm, tenantCode, cocEntity, $CocAction, cocActionSN++);

						orm.save(cocAction);

						newCocActionList.add(cocAction);
					} else {
						List<ICocActionEntity> cocActionList = this.parseCocActionsFromJson(orm, tenantCode, cocEntity, $CocAction.importFromFile(), cocActionSN++);

						for (ICocActionEntity cocAction : cocActionList) {

							orm.save(cocAction);

							newCocActionList.add(cocAction);
						}

					}

				} catch (Throwable e) {
					throw new CocException("解析操作定义(%s.%s)出错：%s ", classOfEntity.getName(), $CocAction.code(), e);
				}
			}
		}

		/*
		 * 解析 @CocGroup 和 @CocColumn
		 */
		List<ICocGroupEntityExt> newCocGroupList = new LinkedList();
		List<ICocFieldEntity> newCocFieldList = new LinkedList();
		List<String> propNames = new ArrayList();
		CocGroup[] $CocGroups = $CocEntity.groups();
		if ($CocGroups != null) {
			int groupSN = cocEntitySN * 10;
			int fieldSN = cocEntitySN * 100;
			int gridSN = 100;
			for (int i = 0; i < $CocGroups.length; i++) {
				CocGroup $CocGroup = $CocGroups[i];
				ICocGroupEntityExt group = this.parseCocGroup(orm, tenantCode, cocEntity, $CocGroup, groupSN++);

				orm.save(group);

				newCocGroupList.add(group);

				/*
				 * 计算实体字段
				 */
				CocColumn[] $CocColumns = $CocGroup.fields();
				if ($CocColumns != null) {
					for (int j = 0; j < $CocColumns.length; j++) {
						CocColumn $CocColumn = $CocColumns[j];
						try {
							ICocFieldEntityExt field = this.parseCocField(orm, tenantCode, mirrorOfEntity, cocEntity, group, $CocColumn, gridSN++, fieldSN++);

							orm.save(field);

							propNames.add(field.getFieldName());
							newCocFieldList.add(field);
						} catch (Throwable e) {
							throw new CocException("解析字段(%s.%s)定义出错：%s ", classOfEntity.getSimpleName(), $CocColumn.field(), e);
						}
					}
				}

			}
		}

		/*
		 * 计算实体字段
		 */
		CocColumn[] $CocColumns = $CocEntity.fields();
		if ($CocColumns != null) {
			int gridSN = 100;
			int groupSN = cocEntitySN * 10;
			int fieldSN = cocEntitySN * 100;

			String entityCode = $CocEntity.code();
			String groupCode = "basic";
			ICocGroupEntityExt basicGroup = (ICocGroupEntityExt) orm.get(EntityTypes.CocGroup, CndExpr.eq(Const.F_CODE, groupCode).and(CndExpr.eq(Const.F_COC_ENTITY_CODE, entityCode)));
			if (basicGroup == null) {
				basicGroup = (ICocGroupEntityExt) ClassUtil.newInstance(EntityTypes.CocGroup);
			}
			basicGroup.setCocEntityCode(entityCode);
			basicGroup.setName("基本信息");
			basicGroup.setCode(groupCode);
			basicGroup.setSn(groupSN);
			basicGroup.setStatusCode(Const.STATUS_CODE_BUILDIN);
			orm.save(basicGroup);
			newCocGroupList.add(basicGroup);

			for (int j = 0; j < $CocColumns.length; j++) {
				CocColumn $CocColumn = $CocColumns[j];
				try {
					ICocFieldEntityExt field = this.parseCocField(orm, tenantCode, mirrorOfEntity, cocEntity, basicGroup, $CocColumn, gridSN++, fieldSN++);

					orm.save(field);

					propNames.add(field.getFieldName());
					newCocFieldList.add(field);
				} catch (Throwable e) {
					throw new CocException("解析字段(%s.%s)定义出错：%s ", classOfEntity.getSimpleName(), $CocColumn.field(), e);
				}
			}

		}

		/*
		 * 检验必需的字段
		 */
		boolean isDataEntity = IDataEntity.class.isAssignableFrom(classOfEntity);
		boolean isNamedEntity = INamedEntity.class.isAssignableFrom(classOfEntity);
		boolean isTreeEntity = ITreeEntity.class.isAssignableFrom(classOfEntity);
		if (isDataEntity) {
			if (!propNames.contains(Const.F_CODE)) {
				throw new CocException("没有找到“逻辑主键”字段注解“@CocColumn(propName=\"%s\")”！[classOfEntity: %s]", Const.F_CODE, classOfEntity.getName());
			}
			try {
				Field fld = classOfEntity.getField(Const.F_CODE);
				if (fld != null) {
					throw new CocException("“逻辑主键”字段注解是公共字段，不应该重复声明！[%s]", fld);
				}
			} catch (Exception e) {
			}
		}
		if (isNamedEntity) {
			if (!propNames.contains(Const.F_NAME)) {
				throw new CocException("没有找到“名称”字段注解“@CocColumn(propName=\"%s\")”！[classOfEntity: %s]", Const.F_NAME, classOfEntity.getName());
			}
			try {
				Field fld = classOfEntity.getField(Const.F_NAME);
				if (fld != null) {
					throw new CocException("“名称”字段注解是公共字段，不应该重复声明！[%s]", fld);
				}
			} catch (Exception e) {
			}
		}
		if (isTreeEntity) {
			if (!propNames.contains(Const.F_PARENT_CODE)) {
				throw new CocException("没有找到“父节点”字段注解“@CocColumn(propName=\"%s\")”！[classOfEntity: %s]", Const.F_PARENT_CODE, classOfEntity.getName());
			}
			try {
				Field fld = classOfEntity.getField(Const.F_PARENT_CODE);
				if (fld != null) {
					throw new CocException("EntityEngine.parseCocEntityFromClass: “父节点”字段注解是公共字段，不应该重复声明！[%s]", fld);
				}
			} catch (Exception e) {
			}
		}

		/*
		 * 解析 @CuiEntity
		 */

		/*
		 * 清除垃圾数据
		 */
		// if (false) {
		List<ICocActionEntity> oldActions = (List<ICocActionEntity>) orm.query(EntityTypes.CocAction, Expr.eq(Const.F_COC_ENTITY_CODE, cocEntity.getCode()));
		for (ICocActionEntity a : oldActions) {
			if (!newCocActionList.contains(a)) {

				if (LogUtil.isInfoEnabled()) {
					LogUtil.info("EntityEngine.parseCocEntityFromClass: 清除垃圾操作！ %s", a.toJsonString());
				}

				orm.delete(a);
			}
		}
		// }
		List<ICocFieldEntityExt> oldFields = (List<ICocFieldEntityExt>) orm.query(EntityTypes.CocField, Expr.eq(Const.F_COC_ENTITY_CODE, cocEntity.getCode()));
		for (ICocFieldEntityExt a : oldFields) {
			if (!newCocFieldList.contains(a)) {

				if (LogUtil.isInfoEnabled()) {
					LogUtil.info("EntityEngine.parseCocEntityFromClass: 清除垃圾字段！ %s", a.toJsonString());
				}

				orm.delete(a);
			}
		}
		List<ICocGroupEntityExt> oldGroups = (List<ICocGroupEntityExt>) orm.query(EntityTypes.CocGroup, Expr.eq(Const.F_COC_ENTITY_CODE, cocEntity.getCode()));
		for (ICocGroupEntityExt a : oldGroups) {
			if (!newCocGroupList.contains(a)) {

				if (LogUtil.isInfoEnabled()) {
					LogUtil.info("EntityEngine.parseCocEntityFromClass: 清除垃圾字段组！ %s", a.toJsonString());
				}

				orm.delete(a);
			}
		}

		/*
		 * 解析实体界面
		 */
		List<ICuiEntityExt> newCuiList = new ArrayList();
		CuiEntity $CuiEntity = (CuiEntity) classOfEntity.getAnnotation(CuiEntity.class);
		int sn = 0;
		if ($CuiEntity != null) {
			sn++;
			try {
				ICuiEntityExt ui = this.parseCuiEntityFromClass(orm, classOfEntity, cocEntity, $CuiEntity, sn);
				newCuiList.add(ui);
			} catch (Throwable e) {
				throw new CocException("解析实体界面定义出错：%s {%s}", e, classOfEntity.getSimpleName());
			}
		}
		Cui $Cui = (Cui) classOfEntity.getAnnotation(Cui.class);
		if ($Cui != null) {
			CuiEntity[] $CuiEntityList = $Cui.value();
			for (CuiEntity ann : $CuiEntityList) {
				sn++;
				try {
					ICuiEntityExt ui = this.parseCuiEntityFromClass(orm, classOfEntity, cocEntity, ann, sn);
					newCuiList.add(ui);
				} catch (Throwable e) {
					throw new CocException("解析实体界面定义出错：%s {%s}", e, classOfEntity.getSimpleName());
				}
			}
		}

		/*
		 * 删除冗余表单
		 */
		List<ICuiEntityExt> oldCuiList = (List<ICuiEntityExt>) orm.query(EntityTypes.CuiEntity, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_STATUS_CODE, Const.STATUS_CODE_BUILDIN))//
		);
		for (ICuiEntityExt old : oldCuiList) {
			if (!newCuiList.contains(old))
				orm.delete(old);
		}

		/*
		 * 返回
		 */
		return cocEntity;
	}

	private ICocEntityExt parseCocEntityFromAnnotation(IOrm orm, String tenantCode, Class klass, ICocEntityExt cocEntity, CocEntity sysann, int sysOrder, String code) {
		if (cocEntity == null) {
			cocEntity = (ICocEntityExt) ClassUtil.newInstance(EntityTypes.CocEntity);
		}

		cocEntity.setName(sysann.name());
		cocEntity.setCode(code);
		cocEntity.setSortExpr(sysann.sortExpr());
		cocEntity.setSn(sysOrder);
		cocEntity.setUiView(sysann.uiView());
		cocEntity.setPathPrefix(sysann.urlPrefix());
		cocEntity.setDataAuthFields(sysann.dataAuthFields());
		// if (IProcessInstanceEntity.class.isAssignableFrom(klass)) {
		// cocEntity.setWorkflow(true);
		// } else {
		// cocEntity.setWorkflow(false);
		// }
		cocEntity.setClassName(klass.getName());
		cocEntity.setExtendsClassName(klass.getSuperclass().getSimpleName());
		if (StringUtil.isBlank(sysann.tableName())) {
			cocEntity.setTableName(code);
		} else {
			cocEntity.setTableName(sysann.tableName());
		}

		ICocCatalogEntity catalog = parseCocCatalogFromPackage(orm, tenantCode, klass.getPackage());
		if (catalog != null) {
			if (StringUtil.isBlank(sysann.catalog())) {
				cocEntity.setCatalogCode(catalog.getCode());
			} else {
				cocEntity.setCatalogCode(sysann.catalog());
			}
		} else {
			cocEntity.setCatalogCode(sysann.catalog());
		}

		catalog = orm.get(EntityTypes.CocCatalog, cocEntity.getCatalogCode());
		if (catalog != null)
			cocEntity.setCatalogName(catalog.getName());

		cocEntity.setStatusCode(Const.STATUS_CODE_BUILDIN);

		LogUtil.info("EntityEngine.parseCocEntityFromAnnotation： result = %s", cocEntity.toJsonString());

		return cocEntity;
	}

	private ICocActionEntityExt parseCocAction(IOrm orm, String tenantCode, ICocEntity module, CocAction actann, int actionSN) {
		ICocActionEntityExt entityAction = (ICocActionEntityExt) orm.get(EntityTypes.CocAction, CndExpr.eq(Const.F_CODE, actann.code()).and(CndExpr.eq(Const.F_COC_ENTITY_CODE, module.getCode())));
		if (entityAction == null) {
			entityAction = (ICocActionEntityExt) ClassUtil.newInstance(EntityTypes.CocAction);
		}
		entityAction.setCocEntityCode(module.getCode());
		entityAction.setName(actann.name());
		entityAction.setTitle(actann.title());
		entityAction.setOpCode(actann.opCode());
		entityAction.setCode(actann.code());
		entityAction.setSn(actionSN);
		Class plugin = actann.plugin();
		if (!plugin.equals(void.class)) {
			entityAction.setPlugin(plugin.getName());
		} else {
			entityAction.setPlugin("");
		}
		entityAction.setBtnImage(actann.btnImage());
		entityAction.setLogo(actann.logo());
		entityAction.setUiForm(actann.uiForm());
		entityAction.setUiFormUrl(actann.uiFormUrl());
		entityAction.setUiFormTarget(actann.uiFormTarget());
		entityAction.setUiWindowHeight(actann.uiWindowHeight());
		entityAction.setUiWindowWidth(actann.uiWindowWidth());
		entityAction.setUiWindowHeader(actann.uiWindowHeader());
		entityAction.setSuccessMessage(actann.successMessage());
		entityAction.setErrorMessage(actann.errorMessage());
		entityAction.setWarnMessage(actann.warnMessage());
		entityAction.setDefaultValuesRule(actann.defaultValues());
		entityAction.setAssignValuesRule(actann.assignValues());
		entityAction.setWhereRule(actann.where());
		entityAction.setProxyActions(actann.proxyActions());
		entityAction.setPrevInterceptors(actann.prevInterceptors());
		entityAction.setPostInterceptors(actann.postInterceptors());
		entityAction.setStatusCode(Const.STATUS_CODE_BUILDIN);

		LogUtil.info("EntityEngine.parseCocAction： entityAction = %s", entityAction.toJsonString());

		return entityAction;
	}

	private List<ICocActionEntity> parseCocActionsFromJson(IOrm orm, String tenantCode, ICocEntity module, String jsonData, int actionOrder) {
		List<ICocActionEntity> actions = (List<ICocActionEntity>) JsonUtil.loadFromJsonOrFile(EntityTypes.CocAction, jsonData);
		List<ICocActionEntity> ret = new ArrayList();
		for (ICocActionEntity newAction : actions) {
			this.parseCocActionFromJson(ret, orm, tenantCode, module, newAction, null, actionOrder++);
		}

		return ret;
	}

	private void parseCocActionFromJson(List<ICocActionEntity> list, IOrm orm, String tenantCode, ICocEntity module, ICocActionEntity newAction, ICocActionEntity parentAction, int actionOrder) {
		ICocActionEntityExt entityAction = (ICocActionEntityExt) orm.get(EntityTypes.CocAction, CndExpr.eq(Const.F_CODE, newAction.getCode()).and(CndExpr.eq(Const.F_COC_ENTITY_CODE, module.getCode())));
		if (entityAction == null) {
			entityAction = (ICocActionEntityExt) newAction;
		}

		entityAction.setCocEntityCode(module.getCode());
		entityAction.setName(newAction.getName());
		entityAction.setOpCode(newAction.getOpCode());
		entityAction.setCode(newAction.getCode());
		entityAction.setSn(actionOrder++);
		entityAction.setUiFormTarget(newAction.getUiFormTarget());
		entityAction.setPlugin(newAction.getPlugin());
		entityAction.setBtnImage(newAction.getBtnImage());
		entityAction.setLogo(newAction.getLogo());
		entityAction.setUiForm(newAction.getUiForm());
		entityAction.setSuccessMessage(newAction.getSuccessMessage());
		entityAction.setErrorMessage(newAction.getErrorMessage());
		entityAction.setWarnMessage(newAction.getWarnMessage());
		entityAction.setParentCode(newAction.getParentCode());
		// entityAction.setParentName(newAction.getParentName());

		entityAction.setStatusCode(Const.STATUS_CODE_BUILDIN);

		LogUtil.info("EntityEngine.parseCocActionFromJson： entityAction = %s", entityAction.toJsonString());

		list.add(entityAction);
	}

	private ICocGroupEntityExt parseCocGroup(IOrm orm, String tenantCode, ICocEntity module, CocGroup grpann, int groupSN) {
		ICocGroupEntityExt entityFieldGroup = (ICocGroupEntityExt) orm.get(EntityTypes.CocGroup, CndExpr.eq(Const.F_CODE, grpann.code()).and(CndExpr.eq(Const.F_COC_ENTITY_CODE, module.getCode())));
		if (entityFieldGroup == null) {
			entityFieldGroup = (ICocGroupEntityExt) ClassUtil.newInstance(EntityTypes.CocGroup);
		}
		entityFieldGroup.setCocEntityCode(module.getCode());
		entityFieldGroup.setName(grpann.name());
		entityFieldGroup.setCode(grpann.code());
		entityFieldGroup.setSn(groupSN);
		entityFieldGroup.setStatusCode(Const.STATUS_CODE_BUILDIN);

		LogUtil.info("EntityEngine.parseCocGroup： entityFieldGroup = %s", entityFieldGroup.toJsonString());

		return entityFieldGroup;
	}

	@SuppressWarnings("deprecation")
	private ICocFieldEntityExt parseCocField(IOrm orm, String tenantCode, Mirror entityMirror, ICocEntity entityModule, ICocGroupEntityExt group, CocColumn $CocColumnOnType, int fieldGridSN, int fieldSN) {

		String propName = $CocColumnOnType.field();
		if (StringUtil.isBlank(propName)) {
			propName = $CocColumnOnType.propName();
		}

		Class fieldType = null;
		Class fieldGenericType = null;// Many字段泛型【包括OneToMany(mappedBy="")或OneToOne(mappedBy="")】
		Field field = null;
		try {
			field = entityMirror.getField(propName);
			fieldType = field.getType();
			Class<?>[] genericTypes = Mirror.getGenericTypes(field);
			if (genericTypes != null && genericTypes.length > 0) {
				fieldGenericType = genericTypes[0];
			}
		} catch (NoSuchFieldException e) {
			String uprop = propName.substring(0, 1).toUpperCase() + propName.substring(1);
			Method m = null;
			try {
				m = entityMirror.findMethod("get" + uprop);
			} catch (Throwable e1) {
				try {
					m = entityMirror.findMethod("is" + uprop);
				} catch (Throwable e2) {
				}
			}
			if (m != null) {
				fieldType = m.getReturnType();
			}
		}
		if (fieldType == null)
			throw new CocException(" @CocColumn(propName=\"%s\") 字段[%s.%s]不存在！", propName, entityMirror.getType().getSimpleName(), propName);

		/*
		 * 加载或创建实体字段对象
		 */
		ICocFieldEntityExt entityField = (ICocFieldEntityExt) orm.get(EntityTypes.CocField, CndExpr.eq(Const.F_PROP_NAME, propName).and(CndExpr.eq(Const.F_COC_ENTITY_CODE, entityModule.getCode())));
		if (entityField == null) {
			entityField = (ICocFieldEntityExt) ClassUtil.newInstance(EntityTypes.CocField);
		}

		/*
		 * 字段注释
		 */
		CocColumn $CocColumnOnField = null;
		Column $Column = null;
		ManyToOne $ManyToOne = null;
		OneToMany $OneToMany = null;
		OneToOne $OneToOne = null;
		CocColumn $CocColumnOnFieldType = null;
		if (field != null) {
			$CocColumnOnField = field.getAnnotation(CocColumn.class);
			$Column = field.getAnnotation(Column.class);
			$ManyToOne = field.getAnnotation(ManyToOne.class);
			$OneToMany = field.getAnnotation(OneToMany.class);
			$OneToOne = field.getAnnotation(OneToOne.class);
			$CocColumnOnFieldType = null;
		}
		if (IExtField.class.isAssignableFrom(fieldType)) {
			$CocColumnOnFieldType = (CocColumn) fieldType.getAnnotation(CocColumn.class);
		}

		/*
		 * Many字段关联到那个实体？
		 */
		String manyTargetEntity = null;
		if ($OneToMany != null && fieldGenericType != null) {
			ICocEntity targetCocEntity = parseCocEntityFromClass(orm, fieldGenericType, false);
			manyTargetEntity = targetCocEntity.getCode();
		} else if ($OneToOne != null && StringUtil.hasContent($OneToOne.mappedBy())) {
			ICocEntity targetCocEntity = parseCocEntityFromClass(orm, fieldType, false);
			manyTargetEntity = targetCocEntity.getCode();
		}

		/*
		 * 外键字段关联到那个实体？
		 */
		String fkTargetEntity = $CocColumnOnType.fkTargetEntity();
		if (StringUtil.isBlank(fkTargetEntity)) {
			if ($CocColumnOnField != null) {
				fkTargetEntity = $CocColumnOnField.fkTargetEntity();
			}

			if (StringUtil.isBlank(fkTargetEntity)) {
				if ($ManyToOne != null) {
					if (fieldType.equals(entityMirror.getType())) {
						fkTargetEntity = entityModule.getCode();
					} else {
						ICocEntity targetCocEntity = parseCocEntityFromClass(orm, fieldType, false);
						fkTargetEntity = targetCocEntity.getCode();
					}
				} else if ($OneToOne != null && StringUtil.isBlank($OneToOne.mappedBy())) {
					if (fieldType.equals(entityMirror.getType())) {
						fkTargetEntity = entityModule.getCode();
					} else {
						ICocEntity targetCocEntity = parseCocEntityFromClass(orm, fieldType, false);
						fkTargetEntity = targetCocEntity.getCode();
					}
				}
			} else {
				if (!fieldType.equals(entityMirror.getType())) {
					Class targetEntityType = getClassOfEntity(fkTargetEntity);
					parseCocEntityFromClass(orm, targetEntityType, false);
				}
			}
		}

		/*
		 * 外键字段关联到实体的哪个字段？
		 */
		String fkTargetField = null;
		String tmp = $CocColumnOnType.fkTargetField();
		if (StringUtil.hasContent(fkTargetEntity) || (tmp != null && tmp.indexOf(".") > 0)) {
			fkTargetField = $CocColumnOnType.fkTargetField();

			if (StringUtil.isBlank(fkTargetField) && $CocColumnOnField != null) {
				fkTargetField = $CocColumnOnField.fkTargetField();
			}

			if (StringUtil.isBlank(fkTargetField)) {
				if ($ManyToOne != null) {
					fkTargetField = Const.F_ID;
				} else if ($OneToOne != null && StringUtil.isBlank($OneToOne.mappedBy())) {
					fkTargetField = Const.F_ID;
				} else {
					fkTargetField = Const.F_CODE;
				}
			}
		}
		if (StringUtil.hasContent(fkTargetField)) {
			int dot = fkTargetField.indexOf(".");
			if (dot > 0) {
				fkTargetEntity = fkTargetField.substring(0, dot);
				fkTargetField = fkTargetField.substring(dot + 1);
			}
		}

		/*
		 * 冗余外键字段依赖字段
		 */
		String fkDependField = null;
		if (StringUtil.hasContent(fkTargetEntity)) {
			fkDependField = $CocColumnOnType.fkDependField();

			if (StringUtil.isBlank(fkDependField) && $CocColumnOnField != null) {
				fkDependField = $CocColumnOnField.fkDependField();
			}
		}

		/*
		 * 解析实体字段类型码
		 */
		int fieldTypeCode = 0;
		if (StringUtil.isBlank(fkTargetEntity)) {
			fieldTypeCode = ConstUtil.getTypeCode(fieldType.getSimpleName());
		} else {
			fieldTypeCode = Const.FIELD_TYPE_FK;
		}

		if (IExtField.class.isAssignableFrom(fieldType)) {
			fieldTypeCode = Const.FIELD_TYPE_EXT;
		}

		if (fieldTypeCode == 0) {
			throw new CocException("@CocColumn(propName=\"%s\") 不支持的字段类型！[classOfEntity: %s, fieldType: %s]", propName, entityMirror.getType().getSimpleName(), fieldType.getSimpleName());
		}

		/*
		 * 业务名称
		 */
		String name = $CocColumnOnType.name();
		if (StringUtil.isBlank(name)) {
			if ($CocColumnOnField != null) {
				name = $CocColumnOnField.name();
			}
		}

		/*
		 * 数据表列名
		 */
		String columnName = $CocColumnOnType.dbColumnName();
		if (StringUtil.isBlank(columnName)) {
			if ($CocColumnOnField != null) {
				columnName = $CocColumnOnField.dbColumnName();
			}
			if (StringUtil.isBlank(columnName) && $Column != null) {
				columnName = $Column.name();
			}
		}

		/**
		 * String类型默认字段长度
		 */
		int length = $CocColumnOnType.length();
		if (length == 0) {
			if ($CocColumnOnField != null) {
				length = $CocColumnOnField.length();
			}
			if (length == 0 && $Column != null) {
				length = $Column.length();
			}
			if (length == 0 && $CocColumnOnFieldType != null) {
				length = $CocColumnOnFieldType.length();
			}
			if (length == 0 && fieldTypeCode == Const.FIELD_TYPE_STRING) {
				length = Const.ANN_COLUMN_DEFAULT_LENGTH;
			}
		}

		/*
		 * 字段精度
		 */
		int precision = $CocColumnOnType.precision();
		if (precision == 0) {
			if ($CocColumnOnField != null) {
				precision = $CocColumnOnField.precision();
			}
			if (precision == 0 && $Column != null) {
				precision = $Column.precision();
			}
		}

		/*
		 * 字段标度
		 */
		int scale = $CocColumnOnType.scale();
		if (scale == 0) {
			if ($CocColumnOnField != null) {
				scale = $CocColumnOnField.scale();
			}
			if (scale == 0 && $Column != null) {
				scale = $Column.scale();
			}
		}

		/**
		 * 数据表列类型
		 */
		String columnDefinition = $CocColumnOnType.dbColumnDefinition();
		if (StringUtil.isBlank(columnDefinition)) {
			if ($CocColumnOnField != null) {
				columnDefinition = $CocColumnOnField.dbColumnDefinition();
			}
			if (StringUtil.isBlank(columnDefinition) && $Column != null) {
				columnDefinition = $Column.columnDefinition();
			}
			if (StringUtil.isBlank(columnDefinition) && $CocColumnOnFieldType != null) {
				columnDefinition = $CocColumnOnFieldType.dbColumnDefinition();
			}
		}

		/*
		 * 嵌套字段: 冗余字段
		 */
		int dot = propName.indexOf(".");
		if (dot > 0) {
			String parentPropName = propName.substring(0, dot);
			ICocFieldEntityExt parentEntityField = (ICocFieldEntityExt) orm().get(EntityTypes.CocField, CndExpr.eq(Const.F_PROP_NAME, parentPropName).and(CndExpr.eq(Const.F_COC_ENTITY_CODE, entityModule.getCode())));

			ICocEntityExt targetModule = (ICocEntityExt) getModule(parentEntityField.getFkTargetEntityCode());
			String targetPropName = propName.substring(dot + 1);
			fkTargetEntity = targetModule.getCode();

			ICocFieldEntityExt targetEntityField = (ICocFieldEntityExt) orm().get(EntityTypes.CocField, CndExpr.eq(Const.F_PROP_NAME, targetPropName).and(CndExpr.eq(Const.F_COC_ENTITY_CODE, targetModule.getCode())));
			fkTargetField = targetEntityField.getCode();
		}

		/*
		 * 字段序号
		 */
		int sn = $CocColumnOnType.sn();
		if (sn == 0) {
			if ($CocColumnOnField != null) {
				sn = $CocColumnOnField.sn();
			}

			if (sn == 0) {
				sn = fieldSN;
			}
		}

		/*
		 * Grid序号
		 */
		int gridSn = $CocColumnOnType.gridColumnSn();
		if (gridSn == 0) {
			if ($CocColumnOnField != null) {
				gridSn = $CocColumnOnField.gridColumnSn();
			}
			// if (gridSn == 0) {
			// gridSn = fieldGridSN;
			// }
		}

		/*
		 * Grid列宽
		 */
		int gridWidth = $CocColumnOnType.gridColumnWidth();
		if (gridWidth == 0) {
			if ($CocColumnOnField != null) {
				gridWidth = $CocColumnOnField.gridColumnWidth();
			}
		}

		/*
		 * 设置实体字段属性
		 */
		entityField.setCocEntityCode(entityModule.getCode());
		entityField.setCocGroupCode(group.getCode());
		entityField.setName(name);
		entityField.setFieldName(propName);
		entityField.setDbColumnName(columnName);
		entityField.setDbColumnDefinition(columnDefinition);
		entityField.setDbColumnNotNull(!$CocColumnOnType.nullable());
		entityField.setFieldType(fieldTypeCode);
		entityField.setManyTargetEntityCode(manyTargetEntity);
		entityField.setFkTargetEntityCode(fkTargetEntity);
		entityField.setFkTargetFieldCode(fkTargetField);
		entityField.setFkDependFieldCode(fkDependField);
		entityField.setFkTargetAsParent($CocColumnOnType.fkTargetAsParent());
		entityField.setFkTargetAsGroup($CocColumnOnType.fkTargetAsGroup());
		entityField.setFkCascadeDelete($CocColumnOnType.fkCascadeDelete());
		entityField.setFkComboUrl($CocColumnOnType.fkComboUrl());
		entityField.setLength(length);
		entityField.setPrecision(precision);
		entityField.setScale(scale);
		entityField.setSn(sn);
		entityField.setAsGridColumn($CocColumnOnType.asGridColumn());
		if (gridSn > 0)
			entityField.setGridColumnSn(gridSn);
		else
			entityField.setGridColumnSn(null);
		entityField.setGridColumnWidth(gridWidth);

		entityField.setDicOptions($CocColumnOnType.dicOptions());
		entityField.setUiView($CocColumnOnType.uiView());
		entityField.setMode($CocColumnOnType.mode());
		entityField.setPattern($CocColumnOnType.pattern());
		entityField.setUiCascading($CocColumnOnType.uiCascading());
		entityField.setDataCascading($CocColumnOnType.dataCascading());
		entityField.setDefaultValue($CocColumnOnType.defalutValue());
		entityField.setGenerator($CocColumnOnType.auto());

		entityField.setAsFilterNode($CocColumnOnType.asFilterNode());
		entityField.setTransient($CocColumnOnType.isTransient());
		entityField.setPrevInterceptors($CocColumnOnType.prevInterceptors());
		entityField.setPostInterceptors($CocColumnOnType.postInterceptors());

		entityField.setMultiSelect($CocColumnOnType.multiSelect());

		entityField.setStatusCode(Const.STATUS_CODE_BUILDIN);

		LogUtil.info("EntityEngine.parseCocField： entityField = %s", entityField.toJsonString());

		return entityField;
	}

	private ICocCatalogEntityExt parseCocCatalogFromPackage(IOrm orm, String tenantCode, Package pkg) {
		LogUtil.info("EntityEngine.parseCocCatalogFromPackage： ...... [pkg: %s]", //
		        pkg == null ? "<NULL>" : pkg.getName()//
		);

		if (pkg == null)
			return null;

		CocCatalog $CocCatalog = pkg.getAnnotation(CocCatalog.class);
		if ($CocCatalog == null) {
			try {
				Class pkginfo = ClassUtil.forName(pkg.getName() + ".package_info");
				if (pkginfo != null)
					$CocCatalog = (CocCatalog) pkginfo.getAnnotation(CocCatalog.class);
			} catch (Throwable e) {
			}
		}
		LogUtil.info("EntityEngine.parseCocCatalogFromPackage： ...... [$CocCatalog: %s, pkg: %s]", //
		        $CocCatalog == null ? "package-info.class或package_info.class注解不存在！" : $CocCatalog, //
		        pkg == null ? "<NULL>" : pkg.getName()//
		);
		if ($CocCatalog == null) {
			return null;
		}

		// 计算code
		String key = $CocCatalog.code();
		String pkgname = pkg.getName();
		int dot = pkgname.lastIndexOf(".");
		String ppkgname = pkgname.substring(0, dot);
		if (StringUtil.isBlank(key)) {
			key = "_" + pkgname.substring(dot + 1);
		}

		// 计算对象
		ICocCatalogEntityExt entityCatalog = (ICocCatalogEntityExt) orm.get(EntityTypes.CocCatalog, CndExpr.eq(Const.F_CODE, key));
		if (entityCatalog == null) {
			entityCatalog = (ICocCatalogEntityExt) ClassUtil.newInstance(EntityTypes.CocCatalog);
		}
		entityCatalog.setName($CocCatalog.name());
		entityCatalog.setCode(key);
		entityCatalog.setSn($CocCatalog.sn());
		entityCatalog.setPrevInterceptors($CocCatalog.prevInterceptors());
		entityCatalog.setPostInterceptors($CocCatalog.postInterceptors());
		entityCatalog.setStatusCode(Const.STATUS_CODE_BUILDIN);

		ICocCatalogEntityExt parentCatalog = parseCocCatalogFromPackage(orm, tenantCode, Package.getPackage(ppkgname));
		if (parentCatalog != null) {
			if (StringUtil.isBlank($CocCatalog.parentCode())) {
				entityCatalog.setParentCode(parentCatalog.getCode());
			} else {
				entityCatalog.setParentCode($CocCatalog.parentCode());
			}
		} else {
			entityCatalog.setParentCode($CocCatalog.parentCode());
		}

		orm.save(entityCatalog);

		LogUtil.info("EntityEngine.parseCocCatalogFromPackage： entityCatalog = %s", entityCatalog.toJsonString());

		return entityCatalog;
	}

	private List parseEntityDatasFromPackage(IOrm orm, String tenantCode) {
		List ret = new LinkedList();

		Iterator<Class> types = packageEntityTypes.values().iterator();
		while (types.hasNext()) {
			List list = parseEntityDataFromClass(orm, tenantCode, types.next());
			if (list != null)
				ret.addAll(list);
		}

		return ret;
	}

	private List<ISystemMenuEntity> parseCocEntityMenus(IOrm orm, String systemCode) {
		List<ISystemMenuEntity> list = new ArrayList();

		List<ICocCatalogEntity> entityCatalogs = (List<ICocCatalogEntity>) orm.query(EntityTypes.CocCatalog);

		/*
		 * 转换EntityCatalog为文件夹菜单
		 */
		for (ICocCatalogEntity catalog : entityCatalogs) {
			String key = catalog.getCode();

			ISystemMenuEntityExt systemMenu = (ISystemMenuEntityExt) orm.get(EntityTypes.SystemMenu, CndExpr.eq(Const.F_CODE, key).and(com.jsoft.cocit.util.ExprUtil.systemIs(systemCode)));
			if (systemMenu == null) {
				systemMenu = (ISystemMenuEntityExt) ClassUtil.newInstance(EntityTypes.SystemMenu);
			}
			systemMenu.setName(catalog.getName());
			systemMenu.setSn(catalog.getSn());
			systemMenu.setCode(key);
			// systemMenu.setEntityCode(catalog.getCode());
			// systemMenu.setEntityModuleName(catalog.getName());
			systemMenu.setParentCode(catalog.getParentCode());
			// systemMenu.setParentName(catalog.getParentName());
			systemMenu.setSystemCode(systemCode);

			systemMenu.setType(Const.MENU_TYPE_FOLDER);

			systemMenu.setStatusCode(Const.STATUS_CODE_BUILDIN);

			LogUtil.info("EntityEngine.parseCocEntityMenus： systemMenu = %s", systemMenu.toJsonString());

			list.add(systemMenu);

			orm.save(systemMenu);
		}

		List<ICocEntity> entityList = (List<ICocEntity>) orm.query(EntityTypes.CocEntity);
		for (ICocEntity module : entityList) {
			String key = module.getCode();

			ISystemMenuEntityExt systemMenu = (ISystemMenuEntityExt) orm.get(EntityTypes.SystemMenu, CndExpr.eq(Const.F_CODE, key).and(com.jsoft.cocit.util.ExprUtil.systemIs(systemCode)));
			if (systemMenu == null) {
				systemMenu = (ISystemMenuEntityExt) ClassUtil.newInstance(EntityTypes.SystemMenu);
			}
			systemMenu.setName(module.getName());
			systemMenu.setSn(module.getSn());
			systemMenu.setCode(key);
			systemMenu.setRefEntity(module.getCode());
			// systemMenu.setEntityModuleName(module.getName());
			// systemMenu.setPathPrefix(module.getPathPrefix());
			systemMenu.setParentCode(module.getCatalogCode());
			systemMenu.setParentName(module.getCatalogName());
			systemMenu.setSystemCode(systemCode);

			systemMenu.setType(Const.MENU_TYPE_ENTITY);

			systemMenu.setStatusCode(Const.STATUS_CODE_BUILDIN);

			LogUtil.info("EntityEngine.parseCocEntityMenus： systemMenu = %s", systemMenu.toJsonString());

			list.add(systemMenu);

			orm.save(systemMenu);
		}

		List<ISystemMenuEntity> oldMenus = (List<ISystemMenuEntity>) orm.query(EntityTypes.SystemMenu);
		for (ISystemMenuEntity a : oldMenus) {
			if (!list.contains(a) && a.isBuildin()) {

				if (LogUtil.isInfoEnabled()) {
					LogUtil.info("EntityEngine.parseCocEntityMenus: 清除垃圾菜单! %s", a.toJsonString());
				}

				orm.delete(a);
			}
		}

		return list;
	}

	private ICuiEntityExt parseCuiEntityFromClass(IOrm orm, Class classOfEntity, ICocEntityExt cocEntity, CuiEntity $CuiEntity, int sn) {
		if ($CuiEntity == null)
			return null;

		Mirror mirror = Mirror.me(classOfEntity);

		/*
		 * 校验
		 */
		for (String fld : StringUtil.toList($CuiEntity.filterFields())) {
			try {
				mirror.getField(fld);
			} catch (Throwable e) {
				try {
					mirror.getGetter(fld);
				} catch (Throwable e1) {
					throw new CocException("%s.%s 字段不存在！@CuiEntity(filterFields=\"%s\")", classOfEntity.getSimpleName(), fld, $CuiEntity.filterFields());
				}
			}
		}
		for (String fld : StringUtil.toList($CuiEntity.queryFields())) {
			try {
				mirror.getField(fld);
			} catch (Throwable e) {
				try {
					mirror.getGetter(fld);
				} catch (Throwable e1) {
					throw new CocException("%s.%s 字段不存在！@CuiEntity(queryFields=\"%s\")", classOfEntity.getSimpleName(), fld, $CuiEntity.queryFields());
				}
			}
		}

		/*
		 * 获取KEY
		 */
		String cocEntityCode = cocEntity.getCode();
		String cuiEntityCode = $CuiEntity.code();
		if (StringUtil.isBlank(cuiEntityCode)) {
			cuiEntityCode = cocEntityCode;
		}

		/*
		 * 创建对象
		 */
		ICuiEntityExt obj = (ICuiEntityExt) orm.get(EntityTypes.CuiEntity, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CODE, cuiEntityCode))//
		);
		if (obj == null) {
			obj = (ICuiEntityExt) Mirror.me(EntityTypes.CuiEntity).born();
		}

		/*
		 * 属性设置
		 */
		obj.setActions($CuiEntity.actions());
		obj.setActionsPos($CuiEntity.actionsPos());
		obj.setActionsView($CuiEntity.actionsView());
		obj.setCocEntityCode(cocEntityCode);
		obj.setCols($CuiEntity.cols());
		obj.setFilterFields($CuiEntity.filterFields());
		obj.setFilterFieldsPos($CuiEntity.filterFieldsPos());
		obj.setFilterFieldsView($CuiEntity.filterFieldsView());
		obj.setCode(cuiEntityCode);
		obj.setName($CuiEntity.name());
		obj.setQueryFields($CuiEntity.queryFields());
		obj.setQueryFieldsPos($CuiEntity.queryFieldsPos());
		obj.setQueryFieldsView($CuiEntity.queryFieldsView());
		obj.setRows($CuiEntity.rows());
		obj.setStatusCode(Const.STATUS_CODE_BUILDIN);
		obj.setSn(sn);
		obj.setUiView($CuiEntity.uiView());

		/*
		 * 保存
		 */
		orm.save(obj);

		/*
		 * 解析表单UI
		 */
		List<ICuiFormEntityExt> newFormList = new ArrayList();
		CuiForm[] annForms = $CuiEntity.forms();
		if (annForms != null) {
			int formSN = 1;
			for (CuiForm annForm : annForms) {
				newFormList.add(this.parseCuiForm(orm, mirror, obj, annForm, formSN++));
			}
		}

		/*
		 * 删除冗余表单
		 */
		List<ICuiFormEntityExt> oldFormList = (List<ICuiFormEntityExt>) orm.query(EntityTypes.CuiForm, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, cuiEntityCode))//
		                .and(Expr.eq(Const.F_STATUS_CODE, Const.STATUS_CODE_BUILDIN))//
		);
		for (ICuiFormEntityExt old : oldFormList) {
			if (!newFormList.contains(old))
				orm.delete(old);
		}

		/*
		 * 解析Grid UI
		 */
		List<ICuiGridEntityExt> newGridList = new ArrayList();
		CuiGrid annGrid = $CuiEntity.grid();
		if (annGrid != null && annGrid.fields().length() > 0) {
			newGridList.add(this.parseCuiGrid(orm, mirror, obj, annGrid, 1));
		}

		/*
		 * 删除冗余 GRID
		 */
		List<ICuiGridEntityExt> oldGridList = (List<ICuiGridEntityExt>) orm.query(EntityTypes.CuiGrid, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, cuiEntityCode))//
		                .and(Expr.eq(Const.F_STATUS_CODE, Const.STATUS_CODE_BUILDIN))//
		);
		for (ICuiGridEntityExt old : oldGridList) {
			if (!newGridList.contains(old))
				orm.delete(old);
		}

		/*
		 * 返回
		 */
		return obj;
	}

	private ICuiFormEntityExt parseCuiForm(IOrm orm, Mirror mirror, ICuiEntityExt cuiEntity, CuiForm $CuiForm, int sn) {

		/*
		 * 校验
		 */
		Class typeOfClass = mirror.getType();
		for (String fld : StringUtil.toList($CuiForm.batchFields())) {
			try {
				mirror.getField(fld);
			} catch (Throwable e) {
				try {
					mirror.getGetter(fld);
				} catch (Throwable e1) {
					throw new CocException("%s.%s 字段不存在！@CuiForm(batchFields=\"%s\")", typeOfClass.getSimpleName(), fld, $CuiForm.batchFields());
				}
			}
		}
		for (String fld : StringUtil.toList($CuiForm.fields())) {
			int dot = fld.indexOf(":");
			if (dot > -1) {
				fld = fld.substring(0, dot);
			}
			try {
				mirror.getField(fld);
			} catch (Throwable e) {
				try {
					mirror.getGetter(fld);
				} catch (Throwable e1) {
					throw new CocException("%s.%s 字段不存在！@CuiForm(fields=\"%s\")", typeOfClass.getSimpleName(), fld, $CuiForm.fields());
				}
			}
		}

		String cocEntityCode = cuiEntity.getCocEntityCode();
		String cuiEntityCode = cuiEntity.getCode();
		String cuiFormCode = $CuiForm.code();

		/*
		 * 创建对象
		 */
		ICuiFormEntityExt obj = (ICuiFormEntityExt) orm.get(EntityTypes.CuiForm, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, cuiEntityCode))//
		                .and(Expr.eq(Const.F_CODE, cuiFormCode))//
		);
		if (obj == null) {
			obj = (ICuiFormEntityExt) Mirror.me(EntityTypes.CuiForm).born();
		}

		/*
		 * 属性设置
		 */
		obj.setBatchFields($CuiForm.batchFields());
		obj.setCocEntityCode(cocEntityCode);
		obj.setCuiEntityCode(cuiEntityCode);
		obj.setFieldLabelPos($CuiForm.fieldLabelPos());
		obj.setFields($CuiForm.fields());
		obj.setGroup1Fields($CuiForm.group1Fields());
		obj.setGroup2Fields($CuiForm.group2Fields());
		obj.setGroup3Fields($CuiForm.group3Fields());
		obj.setGroup4Fields($CuiForm.group4Fields());
		obj.setGroup5Fields($CuiForm.group5Fields());
		obj.setGroup1Name($CuiForm.group1Name());
		obj.setGroup2Name($CuiForm.group2Name());
		obj.setGroup3Name($CuiForm.group3Name());
		obj.setGroup4Name($CuiForm.group4Name());
		obj.setGroup5Name($CuiForm.group5Name());

		obj.setCode(cuiFormCode);
		obj.setName($CuiForm.name());
		obj.setSn(sn);
		obj.setStatusCode(Const.STATUS_CODE_BUILDIN);
		obj.setActions($CuiForm.actions());
		obj.setActionsPos($CuiForm.actionsPos());
		obj.setStyle($CuiForm.style());
		obj.setStyleClass($CuiForm.styleClass());
		obj.setUiView($CuiForm.uiView());
		obj.setPrevInterceptors($CuiForm.prevInterceptors());
		obj.setPostInterceptors($CuiForm.postInterceptors());

		/*
		 * 保存
		 */
		orm.save(obj);

		/*
		 * 解析表单字段
		 */
		List<ICuiFormFieldEntityExt> newList = new ArrayList();
		CuiFormField[] annFields = $CuiForm.fieldsDetail();
		if (annFields != null) {
			int fieldSN = 1;
			for (CuiFormField annField : annFields) {
				newList.add(this.parseCuiFormField(orm, mirror, obj, annField, fieldSN++));
			}
		}

		/*
		 * 删除冗余字段
		 */
		List<ICuiFormFieldEntityExt> oldList = (List<ICuiFormFieldEntityExt>) orm.query(EntityTypes.CuiFormField, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, cuiEntityCode))//
		                .and(Expr.eq(Const.F_CUI_FORM_CODE, cuiFormCode))//
		                .and(Expr.eq(Const.F_STATUS_CODE, Const.STATUS_CODE_BUILDIN))//
		);
		for (ICuiFormFieldEntityExt old : oldList) {
			if (!newList.contains(old))
				orm.delete(old);
		}

		/*
		 * 解析表单操作
		 */
		List<ICuiFormActionExt> newActionsList = new ArrayList();
		CuiFormAction[] annActions = $CuiForm.actionsDetail();
		if (annActions != null) {
			int actionSN = 1;
			for (CuiFormAction ann : annActions) {
				newActionsList.add(this.parseCuiFormAction(orm, mirror, obj, ann, actionSN++));
			}
		}

		/*
		 * 删除冗余操作
		 */
		List<ICuiFormActionExt> oldActionsList = (List<ICuiFormActionExt>) orm.query(EntityTypes.CuiFormAction, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, cuiEntityCode))//
		                .and(Expr.eq(Const.F_CUI_FORM_CODE, cuiFormCode))//
		                .and(Expr.eq(Const.F_STATUS_CODE, Const.STATUS_CODE_BUILDIN))//
		);
		for (ICuiFormActionExt old : oldActionsList) {
			if (!newActionsList.contains(old))
				orm.delete(old);
		}

		/*
		 * 返回
		 */
		return obj;
	}

	private ICuiFormActionExt parseCuiFormAction(IOrm orm, Mirror mirror, ICuiFormEntityExt cuiForm, CuiFormAction $CuiFormAction, int sn) {

		String cocEntityCode = cuiForm.getCocEntityCode();
		String cuiEntityCode = cuiForm.getCuiEntityCode();
		String cuiFormCode = cuiForm.getCode();
		String action = $CuiFormAction.action();

		/*
		 * 创建对象
		 */
		ICuiFormActionExt obj = (ICuiFormActionExt) orm.get(EntityTypes.CuiFormAction, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, cuiEntityCode))//
		                .and(Expr.eq(Const.F_CUI_FORM_CODE, cuiFormCode))//
		                .and(Expr.eq(Const.F_CODE, action))//
		);
		if (obj == null) {
			obj = (ICuiFormActionExt) Mirror.me(EntityTypes.CuiFormAction).born();
		}

		/*
		 * 属性设置
		 */
		obj.setCocEntityCode(cocEntityCode);
		obj.setCuiEntityCode(cuiEntityCode);
		obj.setCuiFormCode(cuiFormCode);
		obj.setName($CuiFormAction.name());
		obj.setSn(sn);
		obj.setStatusCode(Const.STATUS_CODE_BUILDIN);
		obj.setCode(action);
		obj.setTitle($CuiFormAction.action());

		/*
		 * 保存
		 */
		orm.save(obj);

		/*
		 * 返回
		 */
		return obj;
	}

	private ICuiFormFieldEntityExt parseCuiFormField(IOrm orm, Mirror mirror, ICuiFormEntityExt cuiForm, CuiFormField $CuiFormField, int sn) {

		/*
		 * 校验
		 */
		Class typeOfClass = mirror.getType();
		try {
			mirror.getField($CuiFormField.field());
		} catch (Throwable e) {
			try {
				mirror.getGetter($CuiFormField.field());
			} catch (Throwable e1) {
				throw new CocException("字段(%s.%s)不存在！@CuiFormField(field=\"%s\")", typeOfClass.getSimpleName(), $CuiFormField.field(), $CuiFormField.field());
			}
		}

		String cocEntityCode = cuiForm.getCocEntityCode();
		String cuiEntityCode = cuiForm.getCuiEntityCode();
		String cuiFormCode = cuiForm.getCode();
		String field = $CuiFormField.field();

		/*
		 * 创建对象
		 */
		ICuiFormFieldEntityExt obj = (ICuiFormFieldEntityExt) orm.get(EntityTypes.CuiFormField, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, cuiEntityCode))//
		                .and(Expr.eq(Const.F_CUI_FORM_CODE, cuiFormCode))//
		                .and(Expr.eq(Const.F_CODE, field))//
		);
		if (obj == null) {
			obj = (ICuiFormFieldEntityExt) Mirror.me(EntityTypes.CuiFormField).born();
		}

		/*
		 * 属性设置
		 */
		obj.setOneToManyTargetAction($CuiFormField.oneToManyTargetAction());
		obj.setAlign($CuiFormField.align());
		obj.setColspan($CuiFormField.colspan());
		obj.setRowspan($CuiFormField.rowspan());
		obj.setCocEntityCode(cocEntityCode);
		obj.setCuiEntityCode(cuiEntityCode);
		obj.setCuiFormCode(cuiFormCode);
		obj.setDicOptions($CuiFormField.dicOptions());
		obj.setDefaultValue($CuiFormField.defaultValue());
		obj.setFieldName(field);
		obj.setLabelPos($CuiFormField.labelPos());
		// obj.setMode($CuiFormField.mode());
		obj.setName($CuiFormField.name());
		obj.setSn(sn);
		obj.setStatusCode(Const.STATUS_CODE_BUILDIN);
		obj.setStyle($CuiFormField.style());
		obj.setStyleClass($CuiFormField.styleClass());
		obj.setFkComboUrl($CuiFormField.fkComboUrl());
		obj.setFkComboWhere($CuiFormField.fkComboWhere());
		obj.setUiView($CuiFormField.uiView());
		obj.setUiViewLinkUrl($CuiFormField.uiViewLinkUrl());
		obj.setUiViewLinkTarget($CuiFormField.uiViewLinkTarget());

		/*
		 * 保存
		 */
		orm.save(obj);

		/*
		 * 返回
		 */
		return obj;
	}

	private ICuiGridEntityExt parseCuiGrid(IOrm orm, Mirror mirror, ICuiEntityExt cuiEntity, CuiGrid ann, int sn) {

		/*
		 * 校验
		 */
		Class typeOfClass = mirror.getType();
		for (String fld : StringUtil.toList(ann.fields())) {
			try {
				mirror.getField(fld);
			} catch (Throwable e) {
				try {
					mirror.getGetter(fld);
				} catch (Throwable e1) {
					throw new CocException("字段(%s.%s)不存在！@CuiGrid(fields=\"%s\")", typeOfClass.getSimpleName(), fld, ann.fields());
				}
			}
		}
		for (String fld : StringUtil.toList(ann.frozenFields())) {
			try {
				mirror.getField(fld);
			} catch (Throwable e) {
				try {
					mirror.getGetter(fld);
				} catch (Throwable e1) {
					throw new CocException("字段(%s.%s)不存在！@CuiGrid(frozenFields=\"%s\")", typeOfClass.getSimpleName(), fld, ann.frozenFields());
				}
			}
		}
		for (String fld : StringUtil.toList(ann.treeField())) {
			try {
				mirror.getField(fld);
			} catch (Throwable e) {
				try {
					mirror.getGetter(fld);
				} catch (Throwable e1) {
					throw new CocException("字段(%s.%s)不存在！@CuiGrid(treeField=\"%s\")", typeOfClass.getSimpleName(), fld, ann.treeField());
				}
			}
		}

		String cocEntityCode = cuiEntity.getCocEntityCode();
		String cuiEntityCode = cuiEntity.getCode();
		String cuiGridCode = ann.code();
		if (StringUtil.isBlank(cuiGridCode)) {
			cuiGridCode = cocEntityCode;
		}

		/*
		 * 创建对象
		 */
		ICuiGridEntityExt obj = (ICuiGridEntityExt) orm.get(EntityTypes.CuiGrid, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, cuiEntityCode))//
		// .and(Expr.eq(Const.F_KEY, cuiGridCode))//
		);
		if (obj == null) {
			obj = (ICuiGridEntityExt) Mirror.me(EntityTypes.CuiGrid).born();
		}

		/*
		 * 属性设置
		 */
		obj.setCheckOnSelect(ann.checkOnSelect());
		obj.setCocEntityCode(cocEntityCode);
		obj.setCuiEntityCode(cuiEntityCode);
		obj.setFields(ann.fields());
		obj.setFrozenFields(ann.frozenFields());
		obj.setCode(cuiGridCode);
		obj.setPageIndex(ann.pageIndex());
		obj.setPageOptions(ann.pageOptions());
		obj.setPageSize(ann.pageSize());
		obj.setPaginationActions(ann.paginationActions());
		obj.setPaginationPos(ann.paginationPos());
		obj.setRowActions(ann.rowActions());
		obj.setRowActionsPos(ann.rowActionsPos());
		obj.setRowStyleRule(ann.rowStyle());
		obj.setRowActionsView(ann.rowActionsView());
		obj.setRownumbers(ann.rownumbers());
		obj.setSelectOnCheck(ann.selectOnCheck());
		obj.setShowFooter(ann.showFooter());
		obj.setShowHeader(ann.showHeader());
		obj.setMultiSelect(ann.multiSelect());
		obj.setSortExpr(ann.sortExpr());
		obj.setStatusCode(Const.STATUS_CODE_BUILDIN);
		obj.setTreeField(ann.treeField());
		obj.setUiView(ann.uiView());
		obj.setSingleRowEdit(ann.singleRowEdit());
		obj.setPrevInterceptors(ann.prevInterceptors());
		obj.setPostInterceptors(ann.postInterceptors());

		/*
		 * 保存
		 */
		orm.save(obj);

		/*
		 * 解析表单字段
		 */
		List<ICuiGridFieldEntityExt> newList = new ArrayList();
		CuiGridField[] annFields = ann.fieldsDetail();
		if (annFields != null) {
			int fieldSN = 1;
			for (CuiGridField annField : annFields) {
				newList.add(this.parseCuiGridField(orm, mirror, obj, annField, fieldSN++));
			}
		}

		/*
		 * 删除冗余字段
		 */
		List<ICuiGridFieldEntityExt> oldList = (List<ICuiGridFieldEntityExt>) orm.query(EntityTypes.CuiGridField, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, cuiEntityCode))//
		                .and(Expr.eq(Const.F_CUI_GRID_CODE, cuiGridCode))//
		                .and(Expr.eq(Const.F_STATUS_CODE, Const.STATUS_CODE_BUILDIN))//
		);
		for (ICuiGridFieldEntityExt old : oldList) {
			if (!newList.contains(old))
				orm.delete(old);
		}

		/*
		 * 返回
		 */
		return obj;
	}

	private ICuiGridFieldEntityExt parseCuiGridField(IOrm orm, Mirror mirror, ICuiGridEntityExt cuiGrid, CuiGridField ann, int sn) {

		/*
		 * 校验
		 */
		Class typeOfClass = mirror.getType();
		try {
			mirror.getField(ann.field());
		} catch (Throwable e) {
			try {
				mirror.getGetter(ann.field());
			} catch (Throwable e1) {
				throw new CocException("字段(%s.%s)不存在！@CuiGridField(field=\"%s\")", typeOfClass.getSimpleName(), ann.field(), ann.field());
			}
		}

		String cocEntityCode = cuiGrid.getCocEntityCode();
		String cuiEntityCode = cuiGrid.getCuiEntityCode();
		String cuiGridCode = cuiGrid.getCode();
		String field = ann.field();

		/*
		 * 创建对象
		 */
		ICuiGridFieldEntityExt obj = (ICuiGridFieldEntityExt) orm.get(EntityTypes.CuiGridField, //
		        Expr.eq(Const.F_COC_ENTITY_CODE, cocEntityCode)//
		                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, cuiEntityCode))//
		                .and(Expr.eq(Const.F_CUI_GRID_CODE, cuiGridCode))//
		                .and(Expr.eq(Const.F_CODE, field))//
		);
		if (obj == null) {
			obj = (ICuiGridFieldEntityExt) Mirror.me(EntityTypes.CuiGridField).born();
		}

		/*
		 * 属性设置
		 */
		obj.setAlign(ann.align());
		obj.setHalign(ann.halign());
		obj.setCellView(ann.cellView());
		obj.setCellViewLinkUrl(ann.cellViewLinkUrl());
		obj.setCellViewLinkTarget(ann.cellViewLinkTarget());
		obj.setCellStyleRule(ann.cellStyle());
		obj.setShowCellTips(ann.cellTips());
		obj.setCocEntityCode(cocEntityCode);
		obj.setCuiEntityCode(cuiEntityCode);
		obj.setCuiGridCode(cuiGridCode);
		obj.setFieldName(field);
		obj.setName(ann.name());
		obj.setSn(sn);
		obj.setStatusCode(Const.STATUS_CODE_BUILDIN);
		obj.setWidth(ann.width());
		obj.setHidden(ann.hidden());

		/*
		 * 保存
		 */
		orm.save(obj);

		/*
		 * 返回
		 */
		return obj;
	}

	// public void validateModules() throws CocException {
	// List<? extends IEntityModule> modules = getModules();
	// StringBuffer sb = new StringBuffer();
	// for (IEntityModule module : modules) {
	// try {
	// this.getTypeOfEntity(module);
	// } catch (Throwable e) {
	// LogUtil.error("校验实体模块失败<%s>! 错误详情: \n%s", module.getName(),
	// ExceptionUtil.msg(e));
	// sb.append(module.getName()).append("\n");
	// }
	// }
	// if (sb.length() > 0) {
	// throw new CocException(sb.toString());
	// }
	// }

	public ICocEntity setupModuleFromDB(String tableName) {
		// TODO:
		return null;
	}

	public String getPackageOfUDFEntity(ICocEntity module) {
		return "com.jsoft.cocit.dynaentity";
	}

	public int getPrecision(ICocFieldEntity fld) {
		Integer p = fld.getPrecision();
		if (p == null || p == 0) {
			if (isString(fld))
				return Const.ANN_COLUMN_DEFAULT_LENGTH;
			else
				return 0;
		}

		return p;
	}

	public boolean isFkField(ICocFieldEntity fld) {
		if (//
		!StringUtil.isBlank(fld.getFkTargetEntityCode()) //
		        && !StringUtil.isBlank(fld.getFkTargetFieldCode()) //
		        && !this.isSubModule(fld)//
		) {

			return true;

		}

		return false;
	}

	public boolean isEnabled(ICocFieldEntity fld) {
		ICocEntity module = this.getModule(fld.getCocEntityCode());

		if (module.isDisabled()) {
			return false;
		}

		ICocGroupEntity group = getFieldGroup(fld.getCocGroupCode());

		// if (group == null) {
		// return false;
		// }

		if (group.isDisabled())
			return false;

		// String groupMode = group.getMode();
		// if (groupMode != null && groupMode.contains("*:N"))
		// return false;

		if (fld.isDisabled())
			return false;

		// else if (this.getFkEntityModule(fld) != null &&
		// getFkEntityModule(fld).isDisabled())
		// return false;

		// else if (StringUtil.isEmpty(getClassName(fld)))
		// return false;

		return true;
	}

	public List<? extends ICocFieldEntity> getFieldsOfEnabled(ICocEntity module) {
		List<? extends ICocFieldEntity> list = this.getFields(module);

		List<ICocFieldEntity> ret = new LinkedList();
		for (ICocFieldEntity f : list) {
			if (this.isEnabled(f))
				ret.add(f);
		}

		return ret;
	}

	public List<? extends ICocFieldEntity> getFieldsOfEnabled(ICocGroupEntity group) {
		List<? extends ICocFieldEntity> list = this.getFields(group);

		List<ICocFieldEntity> ret = new LinkedList();
		for (ICocFieldEntity f : list) {
			if (this.isEnabled(f))
				ret.add(f);
		}

		return ret;
	}

	public ICocFieldEntity getFieldOfGroupBy(ICocEntity module) {
		// if (module == null) {
		// return null;
		// }
		//
		// List<? extends ICocField> fields = null;//
		// cacheEntityModule(module.getId()).fields();
		// for (ICocField fld : fields) {
		// if (fld.isFkTargetAsGroup()) {
		// return fld;
		// }
		// }

		return null;
	}

	public ICocFieldEntity getFieldOfSelfTree(ICocEntity module) {
		if (module == null) {
			return null;
		}

		// List<? extends IEntityField> refFields =
		// cacheEntityModule(module.getId()).fields();
		//
		// for (IEntityField fld : refFields) {
		// if (fld.getModuleCode().equals(fld.getFkModuleCode()))
		// return fld;
		// }

		for (ICocFieldEntity fld : getFkFields(module)) {
			if (fld.getCocEntityCode().equals(fld.getFkTargetEntityCode()))
				return fld;
		}

		return null;
	}

	public List<? extends ICocFieldEntity> getFkFields(ICocEntity module) {
		List<ICocFieldEntity> ret = new LinkedList();

		List<? extends ICocFieldEntity> fields = this.getFieldsOfEnabled(module);

		if (fields != null) {
			for (ICocFieldEntity field : fields) {
				if (this.isFkField(field)) {
					ret.add(field);
				}
			}
		}

		return ret;
	}

	public List<? extends ICocFieldEntity> getFkFields(ICocEntity module, Class fkType) {
		List<ICocFieldEntity> ret = new LinkedList();

		List<? extends ICocFieldEntity> fields = getFieldsOfEnabled(module);
		if (fields != null) {
			for (ICocFieldEntity field : fields) {
				if (this.isFkField(field)) {
					Class fieldType = this.getTypeOfField(field);
					if (fkType.isAssignableFrom(fieldType) && !getTypeOfEntity(module).equals(fieldType))
						ret.add(field);
				}
			}
		}

		return ret;
	}

	public Option[] getOptions(ICocFieldEntity field) {
		if (!this.isFkField(field)) {
			String str = field.getDicOptions();
			if (!StringUtil.isBlank(str)) {
				return evalOptions(str);
			} else if (isBoolean(field)) {
				return new Option[] { Option.make(ObjectUtil.toKey(true), "是"), Option.make(ObjectUtil.toKey(false), "否") };
			}
		}
		return new Option[0];
	}

	protected Option[] evalOptions(String str) {
		str = str.trim();
		if (str.startsWith("[") && str.endsWith("]")) {
			try {
				return Json.fromJson(Option[].class, str);
			} catch (Throwable e) {
				LogUtil.error("解析字段选项出错：%s", str);
			}
		} else if (str.startsWith("{") && str.endsWith("}")) {
			String key = str.substring(1, str.length() - 1);
			ITenantInfo tenant = Cocit.me().getHttpContext().getLoginTenant();
			String value = tenant.getConfigItem(key, "");
			if (!StringUtil.isBlank(value)) {
				return evalOptions(value);
			}
		} else {
			String[] strs = StringUtil.toArray(str, ",;，；\r\t\n");
			Option[] options = new Option[strs.length];
			int i = 0;
			for (String item : strs) {
				item = item.trim();
				int idx = item.indexOf(":");
				if (idx < 0) {
					idx = item.indexOf("：");
				}
				if (idx > -1) {
					options[i++] = Option.make(item.substring(0, idx).trim(), item.substring(idx + 1).trim());
				} else {
					options[i++] = Option.make(item, item);
				}
			}
			return options;
		}

		return new Option[0];
	}

	public List<? extends ICocFieldEntity> getFieldsOfFilter(ICocEntity module) {
		List<ICocFieldEntity> ret = new LinkedList();

		List<? extends ICocFieldEntity> fields = getFieldsOfEnabled(module);
		for (ICocFieldEntity field : fields) {
			if (!field.isAsFilterNode()) {
				continue;
			}
			if (this.isFkField(field) || isBoolean(field) || !StringUtil.isBlank(field.getDicOptions()))
				ret.add(field);
		}

		return ret;
	}

	public int getGridWidth(ICocFieldEntity field) {
		int w = field.getGridColumnWidth();
		if (w > 0)
			return w;

		if (isNumber(field) || isBoolean(field)) {
			if (this.getOptions(field).length > 0) {
				return 120;
			}
			return 80;
		} else if (isText(field)) {
			return 300;
		} else if (isDate(field)) {
			String p = field.getPattern();
			if (p != null && p.length() > 10)
				return 150;
			else
				return 100;
		} else if (isString(field)) {
			int l = getPrecision(field) + 100;
			if (l > 200)
				return 200;
			else
				return l;
		} else if (isFkField(field)) {
			return 200;
		} else if (isUpload(field)) {
			return 200;
		}

		return 100;
	}

	public boolean isManyToOne(ICocFieldEntity field) {
		return this.isFkField(field) && !field.isMultiSelect();
	}

	public boolean isOneToOne(ICocFieldEntity field) {
		return false;
	}

	public boolean isOneToMany(ICocFieldEntity field) {
		return this.isFkField(field) && field.isMultiSelect();
	}

	public boolean isManyToMany(ICocFieldEntity field) {
		return false;
	}

	public boolean isBoolean(ICocFieldEntity field) {
		return field.getFieldType() == Const.FIELD_TYPE_BOOLEAN;
	}

	public boolean isNumber(ICocFieldEntity field) {
		return field.getFieldType() == Const.FIELD_TYPE_NUMBER;
	}

	public boolean isInteger(ICocFieldEntity field) {
		return field.getFieldType() == Const.FIELD_TYPE_INTEGER;
	}

	public boolean isRichText(ICocFieldEntity field) {
		return field.getFieldType() == Const.FIELD_TYPE_TEXT;
	}

	public boolean isDate(ICocFieldEntity field) {
		return field.getFieldType() == Const.FIELD_TYPE_DATE;
	}

	public boolean isText(ICocFieldEntity field) {
		if (isString(field)) {
			return getPrecision(field) > Const.ANN_COLUMN_DEFAULT_LENGTH;
		}

		return false;
	}

	public boolean isString(ICocFieldEntity field) {
		return field.getFieldType() == Const.FIELD_TYPE_STRING;
	}

	public boolean isUpload(ICocFieldEntity field) {
		return field.getFieldType() == Const.FIELD_TYPE_UPLOAD;
	}

	//
	// public boolean isImage(IEntityField field) {
	// boolean isUpload = isUpload(field);
	// if (isUpload) {
	// String[] exts = StringUtil.toArray(field.getUploadType(), ",;|");
	// for (String ext : exts) {
	// if (ImageUtil.isImage(ext)) {
	// return true;
	// }
	// }
	// }
	//
	// return false;
	// }

	//
	//
	// public boolean isMultiUpload(IEntityField field) {
	// return isUpload(field) || field.isMultipleValue();
	// }
	//
	//
	// public boolean isMultiImage(IEntityField field) {
	// boolean upload = isMultiUpload(field);
	// if (upload) {
	// String[] exts = StringUtil.toArray(field.getUploadType(), ",;|");
	// for (String ext : exts) {
	// if (ImageUtil.isImage(ext)) {
	// return true;
	// }
	// }
	// }
	//
	// return false;
	// }

	public boolean isSubModule(ICocFieldEntity field) {
		// String type = field.getType().getCode();
		// return "SubSystem".equals(type) || "FakeSubSystem".equals(type);

		return false;
	}

	public boolean isFakeSubSystem(ICocFieldEntity field) {
		// String type = field.getType().getCode();
		// return "FakeSubSystem".equals(type);

		return false;
	}

	public boolean isBuildin(ICocEntity module, String prop) {
		try {
			Class cls = ClassUtil.forName(getExtendsClassName(module));
			Mirror me = Mirror.me(cls);
			Field[] fields = me.getFields();
			if (fields != null)
				for (Field f : fields) {
					if (f.getName().toLowerCase().equals(prop == null ? "" : prop.toLowerCase())) {
						return true;
					}
				}
		} catch (Throwable e) {
			return false;
		}
		return false;
	}

	public String parseMode(String actionMode, String mode) {
		if (actionMode == null || actionMode.trim().length() == 0) {
			actionMode = "v";
		}
		if (mode == null || mode.trim().length() == 0) {
			return "";
		}
		mode = mode.trim();
		String[] dataModes = StringUtil.toArray(mode, " ");
		String defaultActionMode = "*";
		String defaultMode = "";
		for (String dataMode : dataModes) {
			if (dataMode == null) {
				continue;
			}
			dataMode = dataMode.trim();
			int index = dataMode.indexOf(":");
			if (index < 0) {
				continue;
			}
			String actMode = dataMode.substring(0, index);
			if (actMode.equals(actionMode)) {
				return dataMode.substring(index + 1);
			}
			if (defaultActionMode.equals(actMode)) {
				defaultMode = dataMode.substring(index + 1);
			}
		}
		return defaultMode;
	}

	public String getUiMode(String mode) {
		if (mode == null)
			return null;

		if (mode.length() > 1)
			return mode.replace("M", "");

		return mode;
	}

	public String getMode(ICocGroupEntity group, ICocActionEntity entityAction) {
		if (group == null) {
			return "";
		}
		String actionMode = entityAction == null ? "" : entityAction.getCode();

		String mode = group.getMode();
		return parseMode(actionMode, mode);
	}

	public String getMode(ICocFieldEntity field, ICocActionEntity entityAction, boolean mustPriority, String defalutMode) {
		if (field == null) {
			return "";
		}

		String actionMode = entityAction == null ? "" : entityAction.getCode();
		String groupMode = getMode(getFieldGroup(field.getCocGroupCode()), entityAction);
		String mode = field.getMode();
		String ret = parseMode(actionMode, mode);
		if (ret == null || ret.length() == 0) {
			if (groupMode != null && groupMode.length() > 0) {
				return groupMode;
			}
		}
		if (mustPriority && ret.indexOf("M") > -1) {
			return "M";
		}
		if (!ret.equals("M")) {
			ret = ret.replace("M", "");
		}
		if (ret.length() > 0) {
			return ret;
		}
		// 创建或编辑时：默认可编辑
		if (actionMode.startsWith("c") || actionMode.startsWith("e")) {
			if (StringUtil.isBlank(defalutMode))
				return "E";
			else
				return defalutMode;
		}
		// 批量修改时：默认不显示
		if (actionMode.startsWith("bu")) {
			if (StringUtil.isBlank(defalutMode))
				return "N";
			else
				return defalutMode;
		}
		// 浏览数据时：默认检查模式显示
		if (actionMode.startsWith("v")) {
			if (StringUtil.isBlank(defalutMode))
				return "S";
			else
				return defalutMode;
		}

		// 检查模式显示
		return "S";
	}

	public Map<String, String> getMode(ICocEntity module, ICocActionEntity entityAction, Object data) {
		List<? extends ICocFieldEntity> fields = this.getFieldsOfEnabled(module);
		Map<String, String> fieldMode = new HashMap();
		for (ICocFieldEntity field : fields) {
			String mode = this.getMode(field, entityAction, true, null);
			String cascadeMode = this.getCascadeMode(field, data)[1];
			if (getModeValue(mode) < getModeValue(cascadeMode)) {
				mode = cascadeMode;
			}
			fieldMode.put(this.getPropName(field), mode);
		}

		return fieldMode;
	}

	//
	// public List<IEntityField> getChildren(IEntityField parent) {
	// List<IEntityField> ret = new ArrayList();
	// // 哪些字段引用了该系统
	// List<IEntityField> fields =
	// orm().query(this.getStaticType(SYS_BIZ_FIELD),
	// CndExpr.eq(F_PARENT, parent).addAsc(F_ORDER_BY));
	// if (fields != null) {
	// for (IEntityField field : fields) {
	// if (this.isEnabled(field)) {
	// ret.add(field);
	// }
	// }
	// }
	//
	// return ret;
	// }

	public List<ICocFieldEntity> getFieldsOfSlave(ICocEntity module) {
		List<ICocFieldEntity> ret = new LinkedList();

		List<ICocFieldEntity> fields = this.getFieldsByReferenced(module);
		for (ICocFieldEntity fld : fields) {
			if (!isMappingToMaster(fld)) {
				continue;
			}
			ICocEntity sys = getModule(fld.getCocEntityCode());
			if (!fld.isDisabled() && !sys.isDisabled()) {
				if (!ret.contains(fld) && !module.equals(sys))
					ret.add(fld);
			}
		}

		return ret;
	}

	public boolean isSlave(ICocEntity module) {
		List<? extends ICocFieldEntity> fields = this.getFkFields(module);
		for (ICocFieldEntity f : fields) {
			if (isMappingToMaster(f)) {
				return true;
			}
		}

		return false;
	}

	public List<? extends ICocEntity> getModulesOfSlave(ICocEntity module) {
		List<ICocEntity> ret = new LinkedList();

		List<ICocFieldEntity> fields = this.getFieldsOfSlave(module);
		for (ICocFieldEntity f : fields) {
			ret.add(getModule(f.getCocEntityCode()));
		}

		return ret;
	}

	public List<ICocFieldEntity> getFieldsByReferenced(ICocEntity module) {
		// List<ICocField> ret = new ArrayList();
		//
		// // 哪些字段引用了该模块？
		// List<? extends ICocField> fields = null;//
		// cacheEntityModule(module.getId()).fieldsOfExport();
		// for (ICocField field : fields) {
		// // if (field.getRefrenceField() != null) {
		// // continue;
		// // }
		// if (!ret.contains(field)) {
		// ret.add(field);
		// }
		// }
		//
		// return ret;
		return null;
	}

	public List<? extends ICocFieldEntity> getFieldsOfGrid(ICocEntity module, String fields) {
		List<? extends ICocFieldEntity> list = this.getFieldsOfEnabled(module);
		SortUtil.sort(list, Const.F_SN, true);

		List<ICocFieldEntity> ret = new LinkedList();
		List<ICocFieldEntity> other = new LinkedList();

		List<String> names = StringUtil.toList(fields, ",");
		int byName = names == null ? 0 : names.size();
		int byNum = 0;
		if (byName == 1) {
			try {
				byNum = Integer.parseInt(names.get(0));
				byName = 0;
			} catch (Throwable e) {
			}
		}
		int count = 0;
		for (ICocFieldEntity f : list) {
			count++;
			if (byName > 0) {
				if (names.contains(f.getFieldName())) {
					ret.add(f);
				}
			} else {
				if (isGridField(f))
					ret.add(f);
				else
					other.add(f);
			}

			if (byNum > 0 && count >= byNum) {
				break;
			}
		}

		ret.addAll(other);

		return ret;
	}

	public List<? extends ICocFieldEntity> getFields(ICocEntity module) {
		if (module == null)
			return new LinkedList();

		return orm().query(EntityTypes.CocField, Expr.eq(Const.F_COC_ENTITY_CODE, module.getCode()));
	}

	public List<? extends ICocActionEntity> getActions(ICocEntity module) {
		if (module == null)
			return new LinkedList();

		return orm().query(EntityTypes.CocAction, Expr.eq(Const.F_COC_ENTITY_CODE, module.getCode()));
	}

	public List<? extends ICocFieldEntity> getFields(ICocGroupEntity group) {
		// List<? extends ICocField> list = null;//
		// cacheEntityModule(getModule(group.getEntityCode()).getId()).fields();
		// List<ICocField> ret = new LinkedList();
		// for (ICocField f : list) {
		// if (group.getCode().equals(f.getCocGroupCode())) {
		// ret.add(f);
		// }
		// }
		// return ret;

		return null;
	}

	public String getPropName(ICocFieldEntity f) {
		if (f == null)
			return null;

		String prop = f.getFieldName();
		if (StringUtil.isBlank(prop)) {
			String code = f.getCode();
			prop = code == null ? "" : code.toLowerCase();

			// } else if (prop.startsWith(".")) {
			// String pp = getPropName(f.getParent());
			// if (StringUtil.isEmpty(pp))
			// return null;
			//
			// return pp + prop;
		}

		return prop;
	}

	public String getTargetClassName(ICocFieldEntity field) {
		return "List<" + getSimpleClassName(getModule(field.getCocEntityCode())) + ">";
	}

	public String getClassName(ICocFieldEntity field) {
		String name = "";

		ICocEntity fkModule = getModule(field.getFkTargetEntityCode());
		if (fkModule != null) {
			if (this.isManyToMany(field))
				name = "List<" + this.getSimpleClassName(fkModule) + ">";
			else
				name = this.getClassName(fkModule);
		} else {
			name = getDataTypeName(field.getFieldType());
		}

		if (name != null) {
			int idx = name.lastIndexOf(".");
			if (idx > -1) {
				return name.substring(idx + 1);
			}
		}

		return name;
	}

	public String getClassName(ICocEntity module) {
		return getPackageOfUDFEntity(module) + "." + getSimpleClassName(module);
	}

	public Class getFkFieldGenericType(ICocFieldEntity field) throws CocException {
		Class type = getTypeOfField(field);
		if (type.equals(List.class)) {
			return getTypeOfEntity(getModule(field.getFkTargetEntityCode()));
		}
		return type;
	}

	public Class getTypeOfField(ICocFieldEntity field) throws CocException {
		Class type = this.getTypeOfEntity(getModule(field.getCocEntityCode()));
		String propname = this.getPropName(field);
		if (propname == null)
			return null;

		try {
			return ClassUtil.getType(type, propname);
		} catch (Throwable e) {
			throw new CocException(e);
		}
	}

	public Class getTypeOfEntity(ICocEntity entityModule) throws CocException {
		if (Cocit.me().getConfig().isProductMode()) {
			ICocEntityInfo module = (ICocEntityInfo) entityModule;

			if (entityModule == null || entityModule.getId() <= 0) {
				return null;
			}

			String className = module.getClassName();
			Class classOfEntity = null;
			if (!StringUtil.isBlank(className)) {
				classOfEntity = dynamicEntityTypes.get(module.getId());
				try {
					if (classOfEntity == null) {
						classOfEntity = ClassUtil.forName(className);
						dynamicEntityTypes.put(module.getId(), classOfEntity);
					}
				} catch (Throwable e) {
					LogUtil.debug("EntityEngine.getTypeOfEntity: %s", ExceptionUtil.msg(e));
				}
			}

			return classOfEntity;
		}

		return ClassUtil.forName(entityModule.getClassName());
	}

	public String getComplireVersion(ICocEntity module) {
		String version = module.getCompileVersion();
		if (version == null) {
			long v = module.getUpdatedDate().getTime();
			List<? extends ICocFieldEntity> fields = this.getFieldsOfEnabled(module);
			for (ICocFieldEntity f : fields) {
				Date d = f.getUpdatedDate();
				if (d != null && d.getTime() > v) {
					v = d.getTime();
				}
			}
			version = DateUtil.formatDateTime(new Date()) + "_" + v;
			((ICocEntityExt) module).setCompileVersion(version);
		}

		return version;
	}

	public synchronized List<String> compileModule(ICocEntityInfo entityModule) throws CocException {
		// EntityModuleProxy module = (EntityModuleProxy) entityModule;
		//
		// if (entityModule == null || entityModule.getId() <= 0) {
		// return null;
		// }
		//
		// String className = module.getClassName();
		// Class classOfEntity = null;
		// if (!StringUtil.isEmpty(className)) {
		// classOfEntity = dynamicEntityTypes.get(module.getId());
		// try {
		// if (classOfEntity == null) {
		// classOfEntity = ClassUtil.forName(className);
		// dynamicEntityTypes.put(module.getId(), classOfEntity);
		// }
		// } catch (Throwable e) {
		// LogUtil.debug("EntityEngine.getTypeOfEntity: %s",
		// ExceptionUtil.msg(e));
		// }
		// }
		//
		// /*
		// * 版本验证
		// */
		// if (classOfEntity != null) {
		//
		// CocEntity ann = (CocEntity)
		// classOfEntity.getAnnotation(CocEntity.class);
		// String newVersion = getComplireVersion(module);
		// String oldVersion = ann.version();
		//
		// if (oldVersion != null && !newVersion.equals(oldVersion)) {
		// LogUtil.debug("业务系统类版本不一致[oldVersion=%s, newVersion=%s]", oldVersion,
		// newVersion);
		// } else {
		// return classOfEntity;
		// }
		//
		// }
		//
		// /*
		// * 重新编译实体源代码
		// */
		// synchronized (dynamicEntityTypes) {
		//
		// StringBuffer sb = new StringBuffer();
		//
		// try {
		// LogUtil.info("编译实体模块<%s>......", module.getName());
		// List<String> errors = compiler.compileSystem(module, true);
		// for (String error : errors) {
		// sb.append("\n").append(error);
		// }
		// LogUtil.info("编译实体模块<%s>: 结束. %s", module, sb);
		//
		// orm().removeMapping(classOfEntity);
		//
		// classOfEntity = ClassUtil.reloadClass(className);
		// dynamicEntityTypes.put(module.getId(), classOfEntity);
		//
		// return classOfEntity;
		// } catch (Throwable e) {
		// e.printStackTrace();
		// throw new CocException(sb.toString());
		// }
		//
		// }
		return compiler.compileSystem((ICocEntityInfo) entityModule, false);
	}

	public String getTableName(ICocEntity module) {
		return module.getTableName();
	}

	public Map<String, ICocFieldEntity> getFieldsMap(ICocEntity module) {
		return this.getFieldsMap(this.getFieldsOfEnabled(module));
	}

	public Map<String, ICocFieldEntity> getFieldsMap(List<? extends ICocFieldEntity> list) {
		Map<String, ICocFieldEntity> map = new HashMap();
		for (ICocFieldEntity fld : list) {
			map.put(this.getPropName(fld), fld);
		}
		return map;
	}

	//
	// public <T> T loadEntity(Class<T> classOfEntity, Serializable entityID) {
	// if (entityID instanceof Number) {
	// return (T) orm().load(classOfEntity, ((Number) entityID).longValue());
	// } else if (entityID instanceof String) {
	// return (T) orm().load(classOfEntity, Expr.eq(Consts.KEY, entityID));
	// }
	//
	// return null;
	// }
	//
	// protected List sortedEntity(Class classOfEntity) {
	// return orm().query(classOfEntity, bizEngine.hasField(classOfEntity,
	// F_ORDER_BY) ? CndExpr.asc(F_ORDER_BY) : null);
	// }
	//
	// protected List sortedEntity(Class classOfEntity, CndExpr expr) {
	// if (bizEngine.hasField(classOfEntity, F_ORDER_BY))
	// expr = expr.addAsc(F_ORDER_BY);
	//
	// return orm().query(classOfEntity, expr);
	// }

	// CacheEntityModule cacheEntityModule(Long systemID) {
	// CacheEntityModule biz = cacheEntityModuleMap.get(systemID);
	// if (biz == null) {
	// biz = new CacheEntityModule(this, systemID);
	// }
	//
	// return biz;
	// }

	public ICocEntity getModule(Long systemID) {
		// return cacheEntityModule(systemID).get();
		return null;
	}

	// public IEntityAction getAction(Long moduleID, String opMode) {
	// return cacheEntityModule(moduleID).action(opMode);
	// }

	//
	// public IEntitySystem getModule(ICocSoft tenantCode, String systemID) {
	// List<IEntitySystem> systems =
	// sortedEntity(bizEngine.getStaticType(SYS_BIZ_SYSTEM), Expr.eq(Consts.KEY,
	// systemID));
	// if (systems.size() > 0) {
	// long softID = 0;
	// if (tenantCode != null) {
	// softID = tenantCode.getId();
	// }
	// for (IEntitySystem sys : systems) {
	// long sid = 0;
	// if (sys.getSoftID() != null) {
	// sid = sys.getSoftID().longValue();
	// }
	// if (softID == sid) {
	// return sys;
	// }
	// }
	//
	// for (IEntitySystem sys : systems) {
	// if (sys.getSoftID() == null || sys.getSoftID() == 0) {
	// return sys;
	// }
	// }
	// }
	// return null;
	// }

	public List<? extends ICocEntity> getModules() {
		return orm().query(EntityTypes.CocEntity);
	}

	public List<? extends ICocGroupEntity> getFieldGroups(ICocEntity module) {
		// return cacheEntityModule(module.getId()).groups();
		return null;
	}

	public int getModeValue(String mode) {
		if (mode != null && mode.length() > 1) {
			mode = mode.replace("M", "");
		}
		if ("E".equals(mode))
			return 1;
		if ("M".equals(mode))
			return 2;
		if ("R".equals(mode))
			return 3;
		if ("D".equals(mode))
			return 4;
		if ("P".equals(mode))
			return 5;
		if ("I".equals(mode))
			return 6;
		if ("S".equals(mode))
			return 7;
		if ("H".equals(mode))
			return 8;
		if ("N".equals(mode))
			return 9;

		return 0;
	}

	public void loadFieldValue(Object obj, ICocEntity module) {
		if (LogUtil.isTraceEnabled())
			LogUtil.trace("加载业务数据... [%s] %s", module, JsonUtil.toJson(obj));

		List<? extends ICocFieldEntity> fks = this.getFkFields(module);
		for (ICocFieldEntity fk : fks) {
			try {
				loadFkFieldValue(obj, fk);
			} catch (CocException e) {
				LogUtil.error("加载字段值出错! [module: %s, field: %s] %s", module.getName(), fk.getName(), e);
			}
		}

		for (ICocFieldEntity fk : this.getFieldsOfEnabled(module)) {
			try {
				loadFieldDefaultValue(obj, fk);
			} catch (CocException e) {
				LogUtil.error("加载字段值出错! [module: %s, field: %s] %s", module.getName(), fk.getName(), e);
			}
		}

		if (LogUtil.isTraceEnabled())
			LogUtil.trace("加载业务结束. [%s] %s", module, JsonUtil.toJson(obj));
	}

	public String[] getCascadeMode(ICocFieldEntity field, Object obj) {
		String[] strs = StringUtil.toArray(field.getDataCascading(), " ");
		if (strs == null) {
			return new String[2];
		}

		Map<String, String> excludeModes = new HashMap();
		Map<String, String> includeModes = new HashMap();
		for (String str : strs) {
			String[] array = StringUtil.toArray(str, ":");
			if (array == null || array.length < 3) {
				continue;
			}

			String cascadeField = array[0];
			int dot = cascadeField.indexOf(".");
			if (dot > 0) {
				cascadeField = cascadeField.substring(0, dot);
			}
			String cascadeList = array[1];
			String cascadeMode = array[2];

			// 选项值级联
			if ("*".equals(cascadeList)) {
				includeModes.put(cascadeField, "*");
			} else {// 模式级联
				try {
					List<String> valueList = StringUtil.toList(cascadeList, ",");

					Object parentValue = ObjectUtil.getValue(obj, cascadeField);
					ICocFieldEntity parentField = this.getField(getModule(field.getCocEntityCode()), cascadeField);
					String code = "";
					if (parentValue == null) {
					} else if (this.isFkField(parentField)) {
						String fld = dot > 0 ? array[0].substring(dot + 1) : Const.F_CODE;
						Object v = ObjectUtil.getValue(orm().get(this.getTypeOfEntity(getModule(parentField.getFkTargetEntityCode())), Expr.eq(Const.F_ID, ObjectUtil.getId(parentValue))), fld);
						if (v instanceof Boolean) {
							if ((Boolean) v) {
								code = "1";
							} else {
								code = "0";
							}
						} else {
							code = v == null ? "" : v.toString();
						}
					} else if (this.isBoolean(parentField)) {
						if ((Boolean) parentValue) {
							code = "1";
						} else {
							code = "0";
						}
					} else {
						code = parentValue.toString();
					}

					if (!valueList.contains(code)) {
						excludeModes.put(cascadeField, cascadeMode);
					} else {
						includeModes.put(cascadeField, cascadeMode);
					}
				} catch (Throwable e) {
					LogUtil.error("加载字段级联模式出错! [%s(%s)] %s", field.getName(), getPropName(field), e);
				}
			}

		}

		StringBuffer prop = new StringBuffer();
		String mode = null;
		Iterator<String> keys = includeModes.keySet().iterator();
		int size = includeModes.size();
		while (keys.hasNext()) {
			String key = keys.next();
			String value = includeModes.get(key);
			if (size > 1 && "*".equals(value)) {
				continue;
			}
			if (value.length() == 0) {
				if (mode == null) {
					mode = "";
				}
			} else {
				if (!StringUtil.isBlank(mode)) {
					if (this.getModeValue(value) > this.getModeValue(mode)) {
						mode = value;
					}
				} else {
					mode = value;
				}
			}

			prop.append(",").append(key);
		}
		keys = excludeModes.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();

			if (!StringUtil.isBlank(includeModes.get(key))) {
				continue;
			}

			String value = excludeModes.get(key);
			if (value.indexOf("N") > -1 || value.indexOf("H") > -1) {
				value = "";
			} else {
				value = "N";
			}

			if (!StringUtil.isBlank(mode)) {
				if (this.getModeValue(value) > this.getModeValue(mode)) {
					mode = value;
				}
			} else {
				mode = value;
			}

			prop.append(",").append(key);
		}

		if (prop.length() > 0) {
			String fld = prop.substring(1);
			LogUtil.trace("字段级联模式：[%s(%s): %s]", field, fld, mode);
			return new String[] { fld, mode };
		} else {
			return new String[2];
		}
	}

	public CndExpr toExpr(List<String> rules) {
		if (rules == null || rules.size() == 0) {
			return null;
		}

		CndExpr expr = null;
		if (rules != null)
			for (String naviRule : rules) {
				if (StringUtil.isBlank(naviRule)) {
					continue;
				}

				ExprRule rule = new ExprRule(naviRule);
				if (expr == null) {
					expr = rule.toExpr();
				} else {
					expr = expr.and(rule.toExpr());
				}
			}

		return expr;
	}

	private Object getFieldValue(Object obj, ICocFieldEntity field) {
		return ObjectUtil.getValue(obj, getPropName(field));
	}

	private Object loadFkFieldValue(Object obj, ICocFieldEntity fkField) throws CocException {
		String fldname = this.getPropName(fkField);
		Object fldvalue = ObjectUtil.getValue(obj, fldname);
		if (fldvalue == null) {
			return null;
		}

		// if (ObjectUtil.isAgent(fldvalue)) {
		// return fldvalue;
		// }

		// 加载多对一字段值
		Class fkFieldType = this.getTypeOfEntity(getModule(fkField.getFkTargetEntityCode()));
		if (fldvalue instanceof String) {
			fldvalue = orm().get(fkFieldType, Expr.eq(Const.F_CODE, fldvalue));
		} else
			fldvalue = orm().get(fkFieldType, Expr.eq(Const.F_ID, fldvalue));

		if (fldvalue == null) {
			return null;
		}

		// 是否虚拟外键？不是则回写字段值
		if (fkFieldType.isAssignableFrom(fldvalue.getClass())) {
			ObjectUtil.setValue(obj, fldname, fldvalue);
		}

		String[] cascadeStrs = StringUtil.toArray(fkField.getDataCascading(), " ");
		if (cascadeStrs == null || cascadeStrs.length == 0) {
			return fldvalue;
		}

		for (String cascadeStr : cascadeStrs) {
			String[] array = StringUtil.toArray(cascadeStr, ":");
			if (array == null || array.length < 3 || !"*".equals(array[1])) {
				continue;
			}

			String prevFld = array[0];
			String refPrevFld = array[2];

			// 检查级联上级，如果级联上级没有值，则自动根据当前字段加载
			int dot = prevFld.indexOf(".");
			if (dot > 0) {
				prevFld = prevFld.substring(0, dot);
			}
			Object prevValue = ObjectUtil.getValue(obj, prevFld);
			if (prevValue == null) {
				prevValue = ObjectUtil.getValue(fldvalue, refPrevFld);
			}
			if (prevValue != null) {
				ObjectUtil.setValue(obj, refPrevFld, prevValue);
				if (!prevValue.equals(ObjectUtil.getValue(fldvalue, refPrevFld))) {
					ObjectUtil.setValue(obj, fldname, null);
				}
			}
		}

		return fldvalue;
	}

	private Object loadFieldDefaultValue(Object obj, ICocFieldEntity field) throws CocException {
		String fldname = this.getPropName(field);
		Object fldvalue = ObjectUtil.getValue(obj, fldname);
		if (fldvalue != null)
			return fldvalue;

		String expr = field.getDefaultValue();
		if (StringUtil.isBlank(expr))
			return null;

		if (expr.startsWith("{") && expr.endsWith("}")) {
			expr = expr.substring(1, expr.length() - 1);
			fldvalue = ObjectUtil.getValue(obj, expr);
		} else {
			try {
				fldvalue = Castors.me().cast(expr, String.class, this.getTypeOfField(field));
			} catch (Throwable e) {
				LogUtil.error("加载字段默认值出错! [field: %s, defaultValue: %s]", field, expr);
			}
		}
		if (fldvalue != null) {
			ObjectUtil.setValue(obj, fldname, fldvalue);
		}

		return fldvalue;
	}

	public void validate(ICocEntity module, ICocActionEntity entityAction, Object data, Map<String, String> fieldMode) throws CocException {
		if (fieldMode == null) {
			fieldMode = this.getMode(module, entityAction, data);
		}
		Iterator<String> keys = fieldMode.keySet().iterator();
		List<String> props = new LinkedList();
		while (keys.hasNext()) {
			String key = keys.next();
			String mode = fieldMode.get(key);
			if (mode != null && mode.indexOf("M") > -1) {
				Object v = ObjectUtil.getValue(data, key);
				// if (v instanceof IExtField) {
				// v = v.toString();
				// }
				if (v == null) {
					props.add(key);
				} else if (v instanceof String) {
					if (StringUtil.isBlank((String) v))
						props.add(key);
				} else if (ObjectUtil.isEntity(v)) {
					if (ObjectUtil.isEmpty(null, v))
						props.add(key);
				}
			}
		}

		if (props.size() > 0) {
			Map<String, ICocFieldEntity> map = this.getFieldsMap(this.getFieldsOfEnabled(module));
			StringBuffer sb = new StringBuffer();
			for (String prop : props) {
				sb.append(",").append(map.get(prop).getName());
			}
			throw new CocException("“%s”必需填写!", sb.toString().substring(1));
		}
	}

	private ICocFieldEntity getField(ICocEntity module, String prop) {
		return this.getFieldsMap(this.getFieldsOfEnabled(module)).get(prop);
	}

	public Tree makeOptionNodes(ICocFieldEntity field, String mode, Object data, String idField) {
		Tree root = Tree.make();

		try {
			if (("E".equals(mode) || "M".equals(mode))) {
				this.makeFieldOptions(root, null, data, field, mode, null, idField);

				if (isFkField(field)) {
					Object value = getFieldValue(data, field);
					if (value != null) {
						value = loadFkFieldValue(data, field);
						if (value != null) {
							String key = ObjectUtil.toKey(value, idField);
							String name = value.toString();
							if (!StringUtil.isBlank(key))
								root.addNode(null, key).setName(name).set("params", value);
						}
					}
				}
			} else if (data != null) {
				Object value = getFieldValue(data, field);
				if (value != null) {
					if (isFkField(field)) {
						value = loadFkFieldValue(data, field);
						if (value != null) {
							String key = ObjectUtil.toKey(value);
							String name = value.toString();
							if (!StringUtil.isBlank(key))
								root.addNode(null, key).setName(name).set("params", value);
						}
					} else {
						Option[] options = getOptions(field);
						for (Option o : options) {
							if (ObjectUtil.toKey(value).equals(ObjectUtil.toKey(o.getValue()))) {
								root.addNode(null, o.getValue()).setName(o.getText()).set("params", o.getValue());
							}
						}
					}
				}
			}
		} catch (CocException e) {
			LogUtil.error("加载字段Options出错! [%s(%s)] %s", field.getName(), getPropName(field), e);
		}

		return root;
	}

	// 解析自身树节点
	public Node mountToSelf(Tree maker, Object obj, String rootID, String group, String selfTree, String groupTree, String groupParam, String paramPrefix, String prefix, boolean selectable, String idField, List selfList) {
		boolean isSelfTree = !StringUtil.isBlank(selfTree);

		if (isSelfTree) {// 自身树
			Object parentObj = ObjectUtil.getValue(obj, selfTree);
			boolean pselect = true;
			if (selfList != null && !selfList.contains(parentObj)) {
				pselect = false;
			}
			if (parentObj != null && !parentObj.equals(obj)) {
				// 将节点挂在父亲的下面
				Serializable parentID = ObjectUtil.getId(parentObj);
				makeNode(maker, obj, prefix + parentID, prefix, paramPrefix, pselect, idField);

				// 解析自身树上级
				return mountToSelf(maker, parentObj, rootID, group, selfTree, groupTree, groupParam, paramPrefix, prefix, false, idField, selfList);
			} else {
				return mountToGroup(maker, obj, rootID, group, groupTree, groupParam, paramPrefix, prefix, selectable, idField);
			}
		} else {
			return mountToGroup(maker, obj, rootID, group, groupTree, groupParam, paramPrefix, prefix, selectable, idField);
		}
	}

	// 将节点挂在分组上
	private Node mountToGroup(Tree maker, Object obj, String rootID, String group, String groupTree, String groupParam, String paramPrefix, String prefix, boolean selectable, String idField) {
		if (!StringUtil.isBlank(group)) {
			Object groupObj = ObjectUtil.getValue(obj, group);
			if (groupObj != null) {
				// 将节点挂在分组的下面
				String groupPrefix = prefix + "_";
				Serializable groupID = ObjectUtil.getId(groupObj);
				makeNode(maker, obj, groupPrefix + groupID, prefix, paramPrefix, selectable, idField);

				// 构建分组树
				return mountToSelf(maker, groupObj, rootID, null, groupTree, null, null, groupParam, groupPrefix, !StringUtil.isBlank(groupParam), idField, null);
			} else {
				return makeNode(maker, obj, rootID, prefix, paramPrefix, selectable, idField);
			}
		} else {
			return this.makeNode(maker, obj, rootID, prefix, paramPrefix, selectable, idField);
		}
	}

	private Node makeNode(Tree maker, Object obj, String parentNode, String prefix, String paramPrefix, boolean selectable, String idField) {
		Serializable id = ObjectUtil.getId(obj);

		Node item = maker.addNode(parentNode, prefix + id).setName(obj.toString());
		if (StringUtil.isBlank(paramPrefix)) {
			item.set("params", ObjectUtil.getId(obj, idField));// 为保证下拉框value为entityCode
		} else {
			item.set("params", paramPrefix + ObjectUtil.getId(obj, idField));
		}
		if (selectable)
			item.set("isSelf", selectable);

		Integer ord = ObjectUtil.getValue(obj, Const.F_SN);
		item.setSn(ord);

		return item;
	}

	public List<String> makeCascadeExpr(Object obj, ICocFieldEntity field, String mode) {
		if (obj == null) {
			return null;
		}

		// 字段选项查询条件
		List<String> rules = new LinkedList();

		String optionsRule = field.getDicOptions();
		if (!Strings.isEmpty(optionsRule)) {
			if (optionsRule.startsWith("[") && optionsRule.endsWith("]")) {
				rules = (List) Json.fromJson(optionsRule);
			} else {
				rules = StringUtil.toList(optionsRule, ",");
			}
		}

		// 计算字段级联表达式
		String[] cascadeStrs = StringUtil.toArray(field.getDataCascading(), " ");
		if (cascadeStrs != null) {
			for (String cascadeStr : cascadeStrs) {
				String[] array = StringUtil.toArray(cascadeStr, ":");
				if (array == null || array.length < 3) {
					continue;
				}

				if (!"*".equals(array[1])) {
					continue;
				}

				String cascadeField = array[0];
				String cascadeFK = array[2];

				Object parentValue = ObjectUtil.getValue(obj, cascadeField);
				if (parentValue == null) {
					rules.add(cascadeFK + " nu");
				} else if (ObjectUtil.isEntity(parentValue)) {
					rules.add(cascadeFK + " eq " + ObjectUtil.getId(parentValue));
				} else {
					rules.add(cascadeFK + " eq " + parentValue);
				}
			}
		}

		return rules;
	}

	private void makeFieldOptions(Tree root, Node node, Object obj, ICocFieldEntity field, String mode, String paramPrefix, String idField) throws CocException {
		String parentNode = node == null ? "" : "" + node.getId();
		String nodePrefix = StringUtil.isBlank(parentNode) ? "_" : parentNode + "_";

		StringBuffer notInList = new StringBuffer();
		ILoginSession loginSession = Cocit.me().getHttpContext().getLoginSession();
		if (isFkField(field)) {
			List<String> rules = this.makeCascadeExpr(obj, field, mode);
			CndExpr expr = this.toExpr(rules);

			// 获取该字段引用的外键系统
			ISystemInfo system = loginSession.getSystem();
			ISystemMenuEntity funcMenu = system.getSystemMenuByModule(field.getCocEntityCode());
			ICocEntity fkModule = getModule(field.getFkTargetEntityCode());
			String fkField = getPropName(field);
			Class fkType = getTypeOfEntity(fkModule);

			CndExpr fkExpr = Cocit.me().getSecurityEngine().getFkDataFilter(funcMenu, fkField);
			if (fkExpr != null) {
				if (expr == null)
					expr = fkExpr;
				else
					expr = expr.and(fkExpr);
			}

			// 获取外键系统业务管理器
			IOrm orm = Cocit.me().orm();
			// IEntityManager bizManager =
			// bizManagerFactory.getManager(moduleEngine.getModule(Coc.me().getSoft(),
			// refSys));

			String group = null;// 数据分组
			String selfTree = null;// 数据自身树
			String groupTree = null;// 数据非自身树

			// 计算数据分组、自身分组
			ICocFieldEntity groupByFld = getFieldOfGroupBy(fkModule);
			if (groupByFld != null) {
				// if (node != null) {
				// root.getChildren().remove(node);
				// }
				// parentNode = null;

				group = getPropName(groupByFld);
				ICocFieldEntity groupTreeFld = getFieldOfSelfTree(getModule(groupByFld.getFkTargetEntityCode()));
				if (groupTreeFld != null) {
					groupTree = getPropName(groupTreeFld);
				}
			}
			ICocFieldEntity selfTreeFld = getFieldOfSelfTree(fkModule);
			if (selfTreeFld != null) {
				selfTree = getPropName(selfTreeFld);
			}

			// 记录数太多将被忽略或采用Combobox方式
			if (orm.count(fkType, expr) > 200) {
				if (node == null) {// 创建下拉选项时
					root.set("combobox", true);// 设置标志以便使用Combobox
					return;
				} else if (selfTree == null) {// 非自身树不能创建导航树
					return;
				}
			}

			// 计算排序表达式
			Class type = getTypeOfEntity(fkModule);
			if (ClassUtil.hasField(type, Const.F_SN)) {
				expr = expr == null ? CndExpr.asc(Const.F_SN) : expr.addAsc(Const.F_SN);
			}
			// if (ClassUtil.hasField(type, F_DISABLED)) {
			expr = expr == null ? //
			        Expr.ne(Const.F_STATUS_CODE, Const.STATUS_CODE_REMOVED).and(Expr.ne(Const.F_STATUS_CODE, Const.STATUS_CODE_DISABLED)) //
			        : expr.and(Expr.ne(Const.F_STATUS_CODE, Const.STATUS_CODE_REMOVED).and(Expr.ne(Const.F_STATUS_CODE, Const.STATUS_CODE_DISABLED)));
			        // }

			// 计算字段是否属于真实的外键引用
			try {
				if (ClassUtil.isEntityType(getTypeOfField(field)) && !StringUtil.isBlank(paramPrefix)) {
					paramPrefix += StringUtil.isBlank(idField) ? ".id" : ("." + idField);
				}
			} catch (Throwable igl) {
				LogUtil.info(ExceptionUtil.msg(igl));
			}

			// 计算参数前缀
			if (!StringUtil.isBlank(paramPrefix)) {
				paramPrefix += " eq ";
			}

			// 查询外键数据
			List<Node> result = new LinkedList();

			List datas = orm.query(fkType, expr);
			if (ClassUtil.hasField(type, Const.F_SN))
				SortUtil.sort(datas, Const.F_SN, true);

			// if (datas.size() == 1 && obj != null) {
			// String propname = field.getPropName();
			// Object v = Mirrors.getValue(obj, field.getPropName());
			// if (v == null) {
			// if (EnObj.isEntityType(getType(field))) {
			// Mirrors.setValue(obj, propname, datas.get(0));
			// } else {
			// Mirrors.setValue(obj, propname, EnObj.getId(datas.get(0)));
			// }
			// }
			// }

			for (Object ele : datas) {
				Node item = this.mountToSelf(root, ele, parentNode, group, selfTree, groupTree, null, paramPrefix, nodePrefix, true, idField, datas);

				if (!result.contains(item)) {
					result.add(item);
				}
				notInList.append(",").append(ObjectUtil.getValue(ele, StringUtil.isBlank(idField) ? Const.F_ID : idField));
			}

			// 如果只有一个分组则移除
			if (result.size() == 1) {
				Node item = result.get(0);
				if (!item.get("isSelf", false)) {
					List<Node> list = root.getChildren();
					if (node != null) {
						list = node.getChildren();
					}
					int idx = list.indexOf(item);
					list.remove(item);
					for (Node ele : item.getChildren()) {
						list.add(idx, ele);
						idx++;
					}
				}
			}
		} else {
			Option[] options = this.getOptions(field);
			if (options == null || options.length == 0 || options.length > 50) {
				return;
			}

			if (!StringUtil.isBlank(paramPrefix)) {
				paramPrefix += " eq ";
			} else {
				paramPrefix = "";
			}

			for (Option op : options) {
				root.addNode(parentNode, parentNode + op.getValue().hashCode()).setName(op.getText()).set("params", paramPrefix + op.getValue());
				notInList.append(",").append(op.getValue());
			}
		}

		if (loginSession != null && loginSession.getUserType() >= Const.USER_SYSTEM && !StringUtil.isBlank(paramPrefix) && node != null && node.size() > 0) {
			String param = paramPrefix.replace("eq", "ni") + (notInList.length() > 0 ? notInList.substring(1) : "");
			// String param = paramPrefix.replace("eq", "nu");
			root.addNode(parentNode, nodePrefix + "nu").setName("---其他---").set("params", param);
		}
	}

	public Tree makeFilterNodes(ICocEntity entityModule, String idField, boolean removeSelfLeaf) {
		LogUtil.debug("计算业务系统导航菜单数据......[module=%s]", entityModule);

		Tree root = Tree.make();

		List<? extends ICocFieldEntity> fkFields = this.getFieldsOfFilter(entityModule);
		ICocFieldEntity selfTreeFld = getFieldOfSelfTree(entityModule);

		for (ICocFieldEntity fld : fkFields) {

			Node node = root.addNode(null, "fld_" + fld.getId()).setName("按 " + fld.getName());

			String propname = getPropName(fld);

			try {
				this.makeFieldOptions(root, node, null, fld, "E", propname, idField);
			} catch (CocException e) {
				LogUtil.error("加载字段快速查询项出错! [%s(%s)] %s", fld.getName(), getPropName(fld), e);
			}
			if (node.size() == 0) {
				root.getChildren().remove(node);
			}

			// 自身树导航字段：移除叶子节点，叶子节点不参与导航。
			if (removeSelfLeaf && fld.equals(selfTreeFld)) {
				node.removeAllLeaf();
			}

		}

		// 如果导航树节点总数没有超过边框则全部展开
		root.optimizeStatus();

		LogUtil.debug("计算业务系统导航菜单数据: 结束. [module=%s]", entityModule);

		root.sort();

		return root;
	}

	private String dataFileExt(int count) {
		if (count >= 1000) {
			return "" + count;
		}
		if (count >= 100) {
			return "0" + count;
		}
		if (count >= 10) {
			return "00" + count;
		}
		if (count >= 0) {
			return "000" + count;
		}

		return "" + count;
	}

	public <T> List<T> parseEntityDataFromClass(IOrm orm, String tenantCode, Class<?> classOfEntity) {
		if (classOfEntity == null) {
			return null;
		}
		CocEntity sysann = (CocEntity) classOfEntity.getAnnotation(CocEntity.class);
		if (sysann != null && StringUtil.hasContent(sysann.dataFile())) {
			setupEntityDataFromJson(orm, tenantCode, classOfEntity, sysann.dataFile());
		}

		List<T> result = new ArrayList();

		String contextDir = Cocit.me().getContextDir();
		int count = 0;

		/*
		 * 从JSON文件初始化数据
		 */
		String dataFile = "/WEB-INF/data/" + classOfEntity.getSimpleName() + ".data.json";
		File file = new File(contextDir + dataFile);
		if (file.exists()) {
			while (true) {

				List<T> tmpResult = (List<T>) setupEntityDataFromJson(orm, tenantCode, classOfEntity, dataFile);
				result.addAll(tmpResult);

				count++;
				dataFile = "/WEB-INF/data/" + classOfEntity.getSimpleName() + "-" + dataFileExt(count) + ".data.json";
				file = new File(contextDir + dataFile);

				if (!file.exists()) {
					break;
				}
			}
		} else {
			dataFile = "/WEB-INF/data/" + sysann.name() + ".xls";
			file = new File(contextDir + dataFile);
			if (file.exists()) {
				log.info("初始化数据：正在解析XLS文件 " + dataFile + " ...");
				try {
					ICocEntityInfo dmInfo = Cocit.me().getEntityServiceFactory().getEntity(sysann.code());
					List<T> tmpResult = dmInfo.parseDataFromExcel(file);

					List<String> uniqueFields = StringUtil.toList(sysann.uniqueFields(), ";");
					List<String> pkFields = new ArrayList();
					if (uniqueFields != null && uniqueFields.size() > 0) {
						pkFields = StringUtil.toList(uniqueFields.get(0));
					}
					if (pkFields.size() == 0) {
						pkFields.add(Const.F_CODE);
					}
					int sn = 1;
					for (T obj : tmpResult) {
						if (obj instanceof NamedEntity) {
							NamedEntity o = (NamedEntity) obj;
							o.setSn(sn++);
						}
						List list = saveObj(orm, obj, pkFields);
						result.addAll(list);
					}

					log.infof("初始化数据成功 %s 条 【 %s】", tmpResult.size(), dataFile);

				} catch (Throwable e) {
					log.error("初始化数据出错！ ", e);
				}
			}
		}

		if (result != null && result.size() > 0) {
			List<T> oldResult = null;
			try {
				oldResult = (List<T>) orm.query(classOfEntity, Expr.eq(FieldNames.F_STATUS_CODE, StatusCodes.STATUS_CODE_INITED));
			} catch (Throwable e) {
				log.warnf("查询实体数据失败！%s, %s", classOfEntity, e.getMessage());
			}
			if (oldResult != null && oldResult.size() > 0) {
				for (Object old : oldResult) {
					if (!result.contains(old)) {
						orm.delete(old);
					}
				}
			}
		}

		return result;
	}

	public <T> List<T> setupEntityDataFromJson(IOrm orm, String tenantCode, Class<T> klass, String json) {
		CocEntity ann = klass.getAnnotation(CocEntity.class);
		List<String> pkFields = StringUtil.toList(ann.uniqueFields());
		if (pkFields.size() == 0) {
			pkFields.add(Const.F_CODE);
		}

		return setupEntityDataFromJson(orm, tenantCode, klass, pkFields, json);
	}

	private <T> List<T> setupEntityDataFromJson(IOrm orm, String tenantCode, Class<T> klass, List<String> pkFields, String jsonData) {
		List<T> newList = new ArrayList();

		List<T> array = (List<T>) JsonUtil.loadFromJsonOrFile(klass, jsonData);
		if (array != null) {

			try {
				for (T ele : array) {

					List savedlist = saveObj(orm, ele, pkFields);
					for (Object obj : savedlist) {
						if (klass.isAssignableFrom(obj.getClass()))
							newList.add((T) obj);
					}
				}
			} catch (Throwable e) {
				throw new CocException("安装初始化数据出错！%s", ExceptionUtil.msg(e));
			}
		}

		return newList;
	}

	private List saveObj(IOrm orm, Object newObj, List<String> pkFields) {
		List ret = new LinkedList();

		Mirror me = Mirror.me(newObj);
		Field[] fields = me.getFields();

		CndExpr expr = null;
		for (String pkFld : pkFields) {
			Object pkVal = ObjectUtil.getValue(newObj, pkFld);
			if (pkVal == null) {
				throw new CocException("逻辑主键字段不允许为空！{field: %s.%s, data: %s}", newObj.getClass().getName(), pkFld, newObj);
			}

			CndExpr expr0 = CndExpr.eq(pkFld, pkVal);
			if (pkVal == null || pkVal.toString().trim().length() == 0) {
				expr0 = expr0.or(CndExpr.isNull(pkFld));
			}
			if (expr == null) {
				expr = expr0;
			} else {
				expr = expr.and(expr0);
			}
		}

		Object obj = orm.get(newObj.getClass(), expr);
		if (obj == null) {
			if (LogUtil.isTraceEnabled())
				LogUtil.trace("创建数据记录... [%s] %s", newObj.getClass().getSimpleName(), JsonUtil.toJson(newObj));

			obj = newObj;
		} else {// 拷贝字段值
			if (LogUtil.isTraceEnabled())
				LogUtil.trace("编辑数据记录... [%s] %s", newObj.getClass().getSimpleName(), JsonUtil.toJson(obj));

			for (Field fld : fields) {
				String fldname = fld.getName();
				Object fldval = me.getValue(newObj, fldname);

				// load entity
				CndExpr fkexpr = null;
				if (ObjectUtil.isEntity(fldval) && ObjectUtil.isEmpty(fldval)) {
					for (String pkFld : pkFields) {
						Object pkVal = ObjectUtil.getValue(fldval, pkFld);

						if (fkexpr == null) {
							fkexpr = CndExpr.eq(pkFld, pkVal);
						} else {
							fkexpr = expr.and(CndExpr.eq(pkFld, pkVal));
						}
					}

					fldval = orm.get(fldval.getClass(), fkexpr);
				}

				if (fldval != null) {
					if (fldval instanceof String) {
						if (((String) fldval).trim().length() == 0)
							fldval = null;
					} else if (ClassUtil.isNumber(fldval)) {
						if (((Number) fldval).intValue() == 0)
							fldval = null;
					} else if (ClassUtil.isBoolean(fldval)) {
						if (((Boolean) fldval).booleanValue() == false)
							fldval = null;
					} else if (fldval instanceof Map || fldval instanceof Collection) {
						fldval = null;
					}

					// 回写
					if (fldval != null) {
						me.setValue(obj, fldname, fldval);
					}
				}
			}
		}

		boolean saveIt = true;
		if (obj instanceof IDataEntity) {
			IDataEntity dataObj = (IDataEntity) obj;
			if (dataObj.getId() != null && dataObj.getId() > 0 && dataObj.getStatusCode() != StatusCodes.STATUS_CODE_INITED) {
				saveIt = false;
			}
		}
		if (saveIt) {
			ObjectUtil.setValue(obj, Const.F_STATUS_CODE, StatusCodes.STATUS_CODE_INITED);
			orm.save(obj);
		}

		/*
		 * 处理列表字段
		 */
		for (Field f : fields) {
			String name = f.getName();
			Object v = me.getValue(newObj, name);
			if (v != null && v instanceof List) {

				List list = (List) v;
				int count = 1;
				for (Object ele : list) {
					if (ele instanceof ITreeEntityExt) {
						((ITreeEntityExt) ele).setParentCode((String) ObjectUtil.getValue(obj, Const.F_CODE));
					}
					if (ele instanceof ITreeObjectEntityExt) {
						((ITreeObjectEntityExt) ele).setParent(obj);
					}

					if (ObjectUtil.getValue(ele, Const.F_SN) == null) {
						ObjectUtil.setValue(ele, Const.F_SN, count++);
					}

					ret.addAll(saveObj(orm, ele, pkFields));
				}
			}
		}

		ret.add(obj);

		return ret;
	}

	public int importFromJson(final String tenantCode, final String folder) {
		Trans.exec(new Atom() {
			public void run() {
				// LogUtil.debug("导入JSON软件数据... [tenantCode: %s, folder: %s]",
		        // tenant, folder);
		        // if (tenant == null) {
		        // throw new CocException("未指定应用软件!");
		        // }
				IOrm orm = orm();

				File ffolder = new File(folder);

				LogUtil.trace("导入无外键数据... ");
				importFromJson(orm, tenantCode, ffolder, false);

				LogUtil.trace("导入含外键数据... ");
				int len = importFromJson(orm, tenantCode, ffolder, true);

				LogUtil.debug("导入JSON数据结束. [ret: %s]", len);

			}
		});

		return 0;
	}

	protected int importFromJson(IOrm orm, String tenantCode, File ffolder, boolean includeFK) {
		int len = 0;
		File[] files = ffolder.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				this.importFromJson(orm, tenantCode, file, includeFK);
				continue;
			}

			String className = file.getName();
			if (!className.endsWith(".json")) {
				continue;
			}

			className = className.substring(0, className.length() - 5);
			int idx = className.indexOf("-");
			if (idx > -1) {
				className = className.substring(0, idx);
			}

			List<String> pkFields = new ArrayList();
			try {
				CocEntity ann = (CocEntity) ClassUtil.forName(className).getAnnotation(CocEntity.class);
				pkFields = StringUtil.toList(ann.uniqueFields());
			} catch (Throwable e) {

			}
			if (pkFields.size() == 0) {
				pkFields.add(Const.F_CODE);
			}

			if (LogUtil.isTraceEnabled())
				LogUtil.trace("转换文件名为类名 [class: %s, file: %s]", className, file.getName());

			InputStream is = null;
			try {

				LogUtil.debug("加载JSON数据文件... [%s]", file.getName());
				is = Files.findFileAsStream(file.getAbsolutePath());
				String json = FileUtil.readAll(is, "UTF-8");

				List<String> refflds = new ArrayList();
				if (!includeFK) {
					LogUtil.trace("计算外键字段...");
					Class type = ClassUtil.forName(className);
					Field[] fields = Mirror.me(type).getFields();
					for (Field f : fields) {
						Class ftype = f.getType();
						if (ClassUtil.isEntityType(ftype)) {
							refflds.add(f.getName());
						}
					}
				}

				LogUtil.trace("解析JSON数据对象 [class: %s]", className);
				Object[] array = (Object[]) Json.fromJson(ClassUtil.forName(className + "[]"), json);

				LogUtil.trace("解析JSON数据对象 [size: %s]", array.length);
				len += array.length;
				for (Object obj : array) {
					ObjectUtil.setId(null, obj, null);
					for (String reffld : refflds) {
						ObjectUtil.setValue(obj, reffld, null);
					}

					// ObjectUtil.setValue(obj, Const.F_TENANT_KEY, tenantCode);

					this.saveObj(orm, obj, pkFields);

					if (LogUtil.isTraceEnabled())
						LogUtil.trace("导入数据 [cls: %s, includeFK: %s]\n%s", obj.getClass().getSimpleName(), includeFK, JsonUtil.toJson(obj));
				}

				LogUtil.debug("导入数据结束. [%s]", className);

			} catch (Throwable e) {
				throw new CocException("导入数据失败!", e);
			} finally {
				if (is != null)
					try {
						is.close();
					} catch (IOException e) {
					}
			}
		}

		return len;
	}

	public int exportToJson(String tenantCode, String folder, CndExpr expr) throws IOException {
		LogUtil.debug("导出JSON数据... [tenantCode: %s, folder: %s]", tenantCode, folder);

		int ret = 0;
		if (tenantCode == null) {
			throw new CocException("未指定应用软件!");
		}

		// List<? extends ICocEntity> systems = getModules();
		// for (ICocEntity sys : systems) {
		// ret += this.exportToJson(tenantCode, folder, expr, sys);
		// }

		LogUtil.debug("导出业务数据结束. [ret: %s]", ret);

		return ret;
	}

	public int exportToJson(String tenantCode, String folder, CndExpr expr, ICocEntityInfo cocEntity) throws IOException {
		LogUtil.debug("导出JSON数据... [tenantCode: %s, folder: %s]", tenantCode, folder);

		int ret = 0;

		Class type = null;
		try {
			type = cocEntity.getClassOfEntity();
		} catch (Throwable e) {
			LogUtil.warn("加载实体类出错! [entity: %s] %s", cocEntity, e);
		}
		if (type == null) {
			LogUtil.warn("实体类不存在! [entity: %s]", cocEntity);
			return 0;
		}

		if (IOfTenantEntity.class.isAssignableFrom(type)) {
			if (expr == null) {
				expr = ExprUtil.tenantIs(tenantCode);
			} else {
				expr = expr.and(ExprUtil.tenantIs(tenantCode));
			}
		}

		int pageIndex = 0;
		while (true) {
			pageIndex++;

			if (expr == null) {
				expr = Expr.page(pageIndex, 1000);
			} else {
				expr.setPager(pageIndex, 1000);
			}

			List<IDataEntity> datas = null;
			try {
				datas = orm().query(type, expr);
			} catch (Throwable e) {
				LogUtil.warn("查询数据出错![%s] %s", cocEntity, e);
				break;
			}
			if (datas == null || datas.size() == 0) {
				break;
			}

			String filePost = "" + pageIndex;
			if (pageIndex < 10) {
				filePost = "000" + filePost;
			} else if (pageIndex < 100) {
				filePost = "00" + filePost;
			} else if (pageIndex < 1000) {
				filePost = "0" + filePost;
			}

			ret = datas.size();
			Writer writer = null;
			try {
				File file = new File(folder + "/" + type.getSimpleName() + "-" + filePost + ".data.json");
				file.getParentFile().mkdirs();
				file.createNewFile();

				LogUtil.trace("写业务数据到文件... [%s]", file.getName());

				OutputStream os = new FileOutputStream(file);
				writer = new OutputStreamWriter(os, "UTF-8");

				writer.write("[");
				int len = datas.size();
				for (int i = 0; i < len; i++) {
					Object obj = datas.get(i);
					if (i != 0) {
						writer.write(",");
					}
					StringBuffer sb = new StringBuffer();
					for (ICocFieldInfo fld : cocEntity.getFields()) {
						String fldName = fld.getCode();
						Object fldValue = ObjectUtil.getValue(obj, fldName);
						if (fldValue != null) {
							if (fldValue instanceof String && StringUtil.isBlank(fldValue.toString())) {
								continue;
							}
							String jsonString = "";
							if (fldValue instanceof IDataEntity) {
								StringBuffer fldsb = new StringBuffer();
								fldsb.append("{");
								fldsb.append("code: " + JsonUtil.toJson(ObjectUtil.getStringValue(fldValue, "code")));
								fldsb.append("}");
								jsonString = fldsb.toString();
							} else {
								jsonString = JsonUtil.toJson(fldValue);
							}
							sb.append(", " + fldName + ":").append(jsonString);
						}
					}
					writer.write("{" + sb.substring(1) + "}");
				}
				writer.write("]");
				writer.flush();
			} finally {
				if (writer != null)
					writer.close();
			}
		}

		LogUtil.debug("导出业务数据结束. [ret: %s]", ret);

		return ret;
	}

	public ICocFieldEntity getField(Long fieldID) {
		return (ICocFieldEntity) orm().get(EntityTypes.CocField, fieldID);
	}

	public ICocEntity getModule(String moduleCode) {
		return (ICocEntity) orm().get(EntityTypes.CocEntity, Expr.eq(Const.F_CODE, moduleCode));
	}

	//
	// public Class getTypeOfEntity(String sysCode) {
	// return this.getTypeOfEntity(getModule(sysCode));
	// }

	public String getSimpleClassName(ICocEntity module) {
		return "CocEntity_" + Long.toHexString(module.getId());
	}

	public String getExtendsClassName(ICocEntity module) {
		String extClass = module.getExtendsClassName();
		if (StringUtil.isBlank(extClass)) {
			return DataEntity.class.getName();
		} else {
			extClass = extClass.substring(extClass.lastIndexOf(".") + 1);
		}
		if (extClass.indexOf(".") < 0) {
			return "com.jsoft.cocit.entity." + extClass;
		}
		return extClass;
	}

	public String getExtendsSimpleClassName(ICocEntity module) {
		String extClass = module.getExtendsClassName();
		if (StringUtil.isBlank(extClass)) {
			return DataEntity.class.getSimpleName();
		} else {
			extClass = extClass.substring(extClass.lastIndexOf(".") + 1);
		}
		return extClass;
	}

	//
	// public String getTargetPropName(IEntityField field) {
	// return ("childrenMapping_" +
	// Integer.toHexString(field.getModule().getCode().hashCode()) + "_" +
	// getPropName(field)).toLowerCase();
	// }

	// protected Option[] geDicOptions(IEntityField field) {
	// IExtEntityField fld = (IExtEntityField) field;
	//
	// List<Option> oplist = new ArrayList();
	// List<IDic> list = (List<IDic>) Cocit.me().orm().query(ENTypes.Dic,
	// Expr.eq(Consts.PARENT_KEY, fld.getDicCatalogCode()));
	// for (IDic dic : list) {
	// if (!dic.isDisabled() && !dic.isDisabled())
	// oplist.add(Option.make(dic.getName(), dic.getCode()));
	// }
	//
	// Option[] ret = new Option[oplist.size()];
	// ret = oplist.toArray(ret);
	//
	// return ret;
	// }

	// private void parseModuleByAnnotation(Orm orm, Class classOfEntity,
	// IEntityModule toModule) {
	// String tenantId = "";// toModule.getTenantCode();
	//
	// CocEntity $CocEntity = (CocEntity)
	// classOfEntity.getAnnotation(CocEntity.class);
	// if ($CocEntity == null) {
	// return;
	// }
	//
	// Integer sysOrder = toModule.getSn();
	//
	// Mirror me = Mirror.me(classOfEntity);
	//
	// // 计算业务操作
	// List<IEntityAction> newActions = new LinkedList();
	// CocAction[] actanns = $CocEntity.actions();
	// if (actanns != null) {
	// int actionOrder = sysOrder * 100;
	// for (int i = 0; i < actanns.length; i++) {
	// CocAction actann = actanns[i];
	//
	// if (StringUtil.isBlank(actann.importFromFile())) {
	// IEntityAction action = this.parseEntityAction(orm, tenantId, toModule,
	// actann, actionOrder++);
	// if (LogUtil.isInfoEnabled()) {
	// LogUtil.info("安装实体操作: %s", action);
	// }
	// orm.save(action);
	// newActions.add(action);
	// } else {
	// List<IEntityAction> actions = this.parseEntityActionsFromJson(orm,
	// tenantId, toModule, actann.importFromFile(), actionOrder++);
	//
	// for (IEntityAction action : actions) {
	//
	// if (LogUtil.isInfoEnabled()) {
	// LogUtil.info("安装实体操作: %s", action);
	// }
	//
	// orm.save(action);
	//
	// newActions.add(action);
	// }
	// }
	//
	// }
	// }
	// List<IExtEntityFieldGroup> newGroups = new LinkedList();
	// List<IEntityField> newFields = new LinkedList();
	// CocGroup[] grpanns = $CocEntity.groups();
	// if (grpanns != null) {
	// int groupOrder = sysOrder * 10;
	// int fieldOrder = sysOrder * 100;
	// int gridOrder = 100;
	// for (int i = 0; i < grpanns.length; i++) {
	// CocGroup grpann = grpanns[i];
	// IExtEntityFieldGroup group = this.parseEntityFieldGroup(orm, tenantId,
	// toModule, grpann, groupOrder++);
	// orm.save(group);
	// newGroups.add(group);
	//
	// // 计算业务字段
	// CocColumn[] fldanns = grpann.fields();
	// if (fldanns != null) {
	// for (int j = 0; j < fldanns.length; j++) {
	// CocColumn fldann = fldanns[j];
	// IExtEntityField fielparseCocFieldtityField(orm, tenantId, me, toModule,
	// group, fldann, gridOrder++, fieldOrder++);
	// orm.save(field);
	// newFields.add(field);
	// }
	// }
	// }
	// }
	// }

	protected boolean isMappingToMaster(ICocFieldEntity fld) {
		return fld.isFkTargetAsParent();
	}

	public boolean isGridField(ICocFieldEntity fld) {
		return fld.isAsGridColumn();
	}

	public Class getTypeOfEntity(Long moduleID) {
		return getTypeOfEntity(getModule(moduleID));
	}

	public ICocFieldEntity getField(String fieldCode) {
		return (ICocFieldEntity) orm().get(EntityTypes.CocField, fieldCode);
	}

	public ICocEntity getFkModule(ICocFieldEntity fld) {
		return getModule(fld.getFkTargetEntityCode());
	}

	public ICocFieldEntity getFkField(ICocFieldEntity fld) {
		return getField(fld.getFkTargetFieldCode());
	}

	public ICocGroupEntity getFieldGroup(String key) {
		return (ICocGroupEntity) orm().get(EntityTypes.CocGroup, key);
	}

	public String getDataTypeName(int dataType) {
		return ConstUtil.getType(dataType);
	}

	public IBizPlugin[] getBizPlugins(ICocActionEntity entityAction) {
		IBizPlugin[] ret = bizPlugins.get(entityAction.getId());
		if (ret != null)
			return ret;

		String[] pluginArray = StringUtil.toArray(entityAction.getPlugin(), ",");
		List<IBizPlugin> plugins = new ArrayList(pluginArray.length);
		for (String pstr : pluginArray) {
			try {
				IBizPlugin plugin = (IBizPlugin) Mirror.me(ClassUtil.forName(pstr)).born();
				plugins.add(plugin);
			} catch (Throwable e) {
				LogUtil.error("加载业务插件出错! [action=%s] %s", entityAction, e);
			}
		}

		ret = new IBizPlugin[plugins.size()];
		plugins.toArray(ret);
		bizPlugins.put(entityAction.getId(), ret);

		return ret;
	}

	// private void makeFields(IRuntimeField runtimeCustom,
	// List<IExtEntityField> fields) {
	// if (runtimeCustom == null)
	// return;
	//
	// makeFields(runtimeCustom.getParent(), fields);
	//
	// if (runtimeCustom.getCustomFields() == null)
	// return;
	//
	// List<IExtEntityField> list = (List<IExtEntityField>)
	// runtimeCustom.getCustomFields().getList();
	//
	// if (list != null) {
	// Map<String, IExtEntityField> map = new HashMap();
	// for (IExtEntityField fld : fields) {
	// String prop = fld.getPropName();
	// if (map.get(prop) == null)
	// map.put(prop, fld);
	// }
	// for (IExtEntityField fld : list) {
	// String prop = fld.getPropName();
	// if (map.get(prop) == null) {
	// map.put(prop, fld);
	// fields.add(fld);
	// }
	// }
	// }
	// }
	//
	// public List<? extends IEntityField> makeFields(IRuntimeField
	// runtimeCustom) {
	// List<IExtEntityField> fields = new LinkedList();
	//
	// Map<String, IFieldDataType> types = this.getFieldTypes();
	// try {
	// this.makeFields(runtimeCustom, fields);
	// } catch (Throwable e) {
	// LogUtil.error("创建运行时自定义字段出错！", e);
	// }
	//
	// for (IExtEntityField f : fields) {
	// if (f.getType() == null) {
	// f.setType(types.get("String"));
	// }
	// }
	//
	// return fields;
	// }
}
