package com.jsoft.cocit.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;

import com.jsoft.cocimpl.ExtHttpContext;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entityengine.DataManager;
import com.jsoft.cocit.entityengine.EntityServiceFactory;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.entityengine.service.TenantService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.mvc.CocEntityParam;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.ui.UIModelFactory;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 功能助手类：用于将一个指定的功能操作表达式解析成一个操作助手对象。
 * <p>
 * 如：1:3:c 表示要对模块1上的数据表3中插入一条数据。
 * 
 * @author yongshan.ji
 * 
 */
public class OpContext {

	public static final String OPCONTEXT_REQUEST_KEY = "opcontext";

	private ExtHttpContext httpContext;
	private Throwable exception;
	private String funcExpr;
	private String dataArgs;
	private String systemMenuID;
	private String cocEntityID;
	private String cocActionID;
	private TenantService tenant;
	private SystemService system;
	private SystemMenuService systemMenu;
	private CocEntityService cocEntity;
	private CocActionService cocAction;
	private UIModelFactory uiModelFactory;
	private DataManager dataManager;
	private Object dataObject;
	private String[] dataID;
	private String treeField;

	public void release() {
		// this.httpContext = null;
		// this.exception = null;
		// this.funcExpr = null;
		// this.dataArgs = null;
		// this.systemMenuID = null;
		// this.cocEntityID = null;
		// this.cocActionID = null;
		// this.tenant = null;
		// this.system = null;
		// this.systemMenu = null;
		// this.cocEntity = null;
		// this.cocAction = null;
		// this.uiModelFactory = null;
		// this.dataManager = null;
		//
		// this.dataObject = null;
		// this.dataID = null;
	}

	/**
	 * 创建一个“操作助手”对象
	 * 
	 * @param funcExpr
	 *            功能参数：“moduleID:tableID:opMode”
	 * @return
	 */
	public static OpContext make(String funcExpr) {
		OpContext ret = new OpContext(funcExpr, null, null, false);

		if (ret.httpContext != null) {
			ret.httpContext.getRequest().setAttribute(OPCONTEXT_REQUEST_KEY, ret);
		}

		return ret;
	}

	/**
	 * 创建一个“操作环境”对象
	 * 
	 * @param funcExpr
	 *            功能表达式：该参数由“菜单编号:实体编号:操作编号”组成，其中菜单编号是必需的，后两个根据需要是可选的。<br/>
	 *            平台会自动根据该参数加载“菜单服务对象，实体服务对象，操作服务对象”。<br/>
	 *            菜单服务对象：可以调用opContext.getSystemMenu()方法获得；<br/>
	 *            实体服务对象：可以调用opContext.getCocEntity()方法获得；<br/>
	 *            操作服务对象：可以调用opContext.getCocAction()方法获得；
	 * @param dataArgs
	 *            数据参数：由数据ID(物理主键)组成，可以是单值也可以是多值，多值用逗号分隔。<br/>
	 *            平台会自动根据该参数加载“数据对象或数据列表”。<br>
	 *            如果该参数是单值，则调用opContext.getDataObject()方法可以获得“实体对象”；<br/>
	 *            如果该参数是多值，则调用opContext.getDataObject()方法可以获得“实体列表(List对象)”。<br/>
	 * @param httpParams
	 *            HTTP参数：浏览器上传入的参数如果以“entity.”开头的话，这些参数将被注入到httpParams中。<br/>
	 *            httpParams传入到OpContext.make方法后，这些参数将被注入到已经加载的“数据对象或对象列表”中。
	 * @return
	 */
	public static OpContext make(String funcExpr, String dataArgs, CocEntityParam httpParams) {
		OpContext ret = new OpContext(funcExpr, dataArgs, httpParams, false);

		if (ret.httpContext != null) {
			ret.httpContext.getRequest().setAttribute(OPCONTEXT_REQUEST_KEY, ret);
		}

		return ret;
	}

	/**
	 * 创建一个“操作环境”对象
	 * 
	 * @param funcExpr
	 *            功能表达式：该参数由“菜单编号:实体编号:操作编号”组成，其中菜单编号是必需的，后两个根据需要是可选的。<br/>
	 *            平台会自动根据该参数加载“菜单服务对象，实体服务对象，操作服务对象”。<br/>
	 *            菜单服务对象：可以调用opContext.getSystemMenu()方法获得；<br/>
	 *            实体服务对象：可以调用opContext.getCocEntity()方法获得；<br/>
	 *            操作服务对象：可以调用opContext.getCocAction()方法获得；
	 * @param dataArgs
	 *            数据参数：由数据ID(物理主键)组成，可以是单值也可以是多值，多值用逗号分隔。<br/>
	 *            平台会自动根据该参数加载“数据对象或数据列表”。<br>
	 *            如果该参数是单值，则调用opContext.getDataObject()方法可以获得“实体对象”；<br/>
	 *            如果该参数是多值，则调用opContext.getDataObject()方法可以获得“实体列表(List对象)”。<br/>
	 * @param httpParams
	 *            HTTP参数，浏览器上传入的参数如果以“entity.”开头的话，这些参数将被注入到httpParams中。<br/>
	 *            httpParams传入到OpContext.make方法后，这些参数将被注入到已经加载的“数据对象或对象列表”中。
	 * @param fireLoadEvent
	 * @return
	 */
	public static OpContext make(String funcExpr, String dataArgs, CocEntityParam httpParams, boolean fireLoadEvent) {
		OpContext ret = new OpContext(funcExpr, dataArgs, httpParams, fireLoadEvent);

		if (ret.httpContext != null) {
			ret.httpContext.getRequest().setAttribute(OPCONTEXT_REQUEST_KEY, ret);
		}

		return ret;
	}

	private OpContext(String funcExpr, String dataArgs, CocEntityParam entityParamNode, boolean fireLoadEvent) {
		LogUtil.debug("OpContext... {funcExpr:%s, dataArgs:%s, entityParamNode:%s}", funcExpr, dataArgs, entityParamNode);

		httpContext = (ExtHttpContext) Cocit.me().getHttpContext();

		this.funcExpr = funcExpr;
		this.dataArgs = dataArgs;

		try {

			uiModelFactory = Cocit.me().getUiModelFactory();

			/*
			 * 获取租户及租户所使用的系统
			 */
			tenant = httpContext.getLoginTenant();
			system = httpContext.getLoginSystem();

			// 解析操作参数
			parseFuncExpr(funcExpr);

			/*
			 * 初始化实体管理器
			 */
			dataManager = tenant.getEntityManager(systemMenu);

			loadDataObject(dataArgs, entityParamNode, fireLoadEvent);

			selectProxyAction();

		} catch (Throwable e) {
			LogUtil.error("解析模块参数出错：%s", ExceptionUtil.msg(e), e);

			exception = e;
		}
	}

	private void parseFuncExpr(String funcExpr) {
		if (StringUtil.isBlank(funcExpr))
			return;

		String[] array = MVCUtil.decodeArgs(funcExpr);

		systemMenuID = array.length > 0 ? array[0] : null;
		cocEntityID = array.length > 1 ? array[1] : null;
		cocActionID = array.length > 2 ? array[2] : null;

		LogUtil.debug("OpContext.parseFuncExpr: funcExpr = %s {moduleID:%s, tableID:%s, opMode:%s}", funcExpr, systemMenuID, cocEntityID, cocActionID);

		EntityServiceFactory entityServiceFactory = Cocit.me().getEntityServiceFactory();
		if (!StringUtil.isBlank(systemMenuID)) {
			systemMenu = system.getSystemMenu(systemMenuID);
			if (systemMenu == null) {
				try {
					systemMenu = system.getSystemMenu(Long.parseLong(systemMenuID));
				} catch (NumberFormatException e) {
				}
			}
			if (systemMenu == null) {
				SystemService cocSystem = entityServiceFactory.getSystem(Cocit.me().getConfig().getCocitSystemKey());
				systemMenu = cocSystem.getSystemMenu(systemMenuID);
			}
			if (systemMenu == null) {
				throw new CocException("功能菜单不存在！%s", this.funcExpr);
			}
		}

		if (!StringUtil.isBlank(cocEntityID)) {
			cocEntity = entityServiceFactory.getEntity(cocEntityID);
			if (cocEntity == null) {
				try {
					cocEntity = entityServiceFactory.getEntity(Long.parseLong(cocEntityID));
				} catch (NumberFormatException e) {
				}
			}
		} else if (systemMenu != null) {
			cocEntity = entityServiceFactory.getEntity(systemMenu.getRefEntity());
		}
		if (cocEntity == null) {
			throw new CocException("实体模块不存在！%s", this.funcExpr);
		}

		if (!StringUtil.isBlank(cocActionID)) {
			cocAction = cocEntity.getAction(cocActionID);
			if (cocAction == null) {
				try {
					cocAction = cocEntity.getAction(Long.parseLong(cocActionID));
				} catch (NumberFormatException e) {
				}
			}
			if (cocAction == null) {
				throw new CocException("实体操作不存在！%s", this.funcExpr);
			}
		}

		// System.out.println("LR：systemMenu=" + (systemMenu == null ? "" : systemMenu.hashCode()) + //
		// ", entityModule=" + (entityModule == null ? "" : entityModule.hashCode()) + //
		// ", entityAction=" + (entityAction == null ? "" : entityAction.hashCode())//
		// );

		LogUtil.debug("OpContext.parseFuncExpr: systemMenu = %s, entityModule = %s", systemMenu, cocEntity);
	}

	private void loadDataObject(String dataArgs, CocEntityParam entityParamNode, boolean fireLoadEvent) {
		if (cocAction == null)
			return;

		Class type = this.cocEntity.getClassOfEntity();

		/*
		 * 查询数据对象
		 */
		int opCode = this.cocAction.getOpCode();
		if (opCode == OpCodes.OP_INSERT_FORM_DATA || opCode == OpCodes.OP_INSERT_GRID_ROW) {// 添加操作

		} else {// 修改类操作

			/*
			 * 数据对象查询条件
			 */
			List<Long> idList = new ArrayList();
			dataID = StringUtil.toArray(dataArgs);
			for (String s : dataID) {
				try {
					idList.add(Long.parseLong(s));
				} catch (Throwable e) {
				}
			}
			CndExpr expr = null;
			if (idList.size() > 0) {
				expr = Expr.in(Const.F_ID, idList);
			}

			CndExpr menuExpr = null;
			if (StringUtil.hasContent(this.systemMenu.getWhereRule())) {
				menuExpr = makeInExprFromJson(systemMenu.getWhereRule(), new StringBuffer());
			}
			long id = 0;
			if (idList.size() == 1) {
				id = idList.get(0);
			}
			if (id == -1) {// 自动获取满足条件的数据
				expr = menuExpr;
			} else if (menuExpr != null) {// 合并查询条件
				if (expr == null) {
					expr = menuExpr;
				} else {
					expr = expr.and(menuExpr);
				}
			}

			/*
			 * 查询满足条件的数据
			 */
			if ((opCode >= OpCodes.OP_UPDATE_FORM_DATA && opCode <= 150) || (opCode > 200 && opCode <= 250)) {// 查询单条数据
				if (fireLoadEvent) {
					dataObject = dataManager.load(systemMenu, cocEntity, cocActionID, expr);
				} else {
					dataObject = dataManager.getDataEngine().load(type, expr);
				}
				if (dataObject == null && id != -1) {
					this.exception = new CocException("访问的数据不存在！");
				}
			} else if ((opCode > 150 && opCode <= 190) || (opCode > 250 && opCode <= 290)) {// 查询多条数据
				if (fireLoadEvent) {
					dataObject = dataManager.query(systemMenu, cocEntity, cocActionID, expr);
				} else {
					dataObject = dataManager.getDataEngine().query(type, expr);
				}
				List list = (List) dataObject;
				if ((list == null || list.size() == 0) && id != -1) {
					this.exception = new CocException("访问的数据不存在！");
				}
			}

		}

		/*
		 * 检查待访问的数据是否满足操作条件？
		 */
		if (dataObject != null) {
			String msg = "";
			if (this.cocAction != null) {
				msg = cocAction.getErrorMessage();
				if (StringUtil.isBlank(msg))
					msg = "数据不能被“" + cocAction.getName() + "”";
			}

			if (dataObject instanceof List) {
				List list = (List) dataObject;
				for (Object obj : list) {
					if (!ExprUtil.match(obj, cocAction.getWhere())) {
						exception = new CocException(msg);
						return;
					}
				}
			} else {
				if (!ExprUtil.match(dataObject, cocAction.getWhere())) {
					exception = new CocException(msg);
					return;
				}
			}
		}

		/*
		 * 将菜单参数和HTTP参数注入数据对象
		 */
		if (dataObject == null) {

			/*
			 * 用HTTP参数为数据对象赋值
			 */
			if (entityParamNode != null) {
				dataObject = entityParamNode.inject(Mirror.me(type), dataObject, null);

				if (dataObject instanceof List) {
					List list = (List) dataObject;
					for (Object obj : list) {
						initDataObjectWithAssignValues(obj);

						initDataObjectWithDefaultValues(obj);
						cocEntity.initDataObjectWithDefaultValues(cocActionID, obj);
					}
				} else {
					initDataObjectWithAssignValues(dataObject);

					initDataObjectWithDefaultValues(dataObject);
					cocEntity.initDataObjectWithDefaultValues(cocActionID, dataObject);
				}
			}
		} else {
			/*
			 * 先用菜单或操作按钮参数为数据对象赋值
			 */
			if (dataObject instanceof List) {
				List list = (List) dataObject;
				for (Object obj : list) {
					initDataObjectWithAssignValues(obj);
				}
			} else {
				initDataObjectWithAssignValues(dataObject);
			}

			/*
			 * 再用HTTP参数为数据对象赋值
			 */
			Object newObj = null;
			if (entityParamNode != null) {
				newObj = entityParamNode.inject(Mirror.me(type), dataObject, null);
			}
			if (dataObject instanceof List) {
				List list = (List) dataObject;
				List newList = (List) newObj;
				if (newList != null) {
					for (Object obj : newList) {
						if (!list.contains(obj)) {
							list.add(obj);
						}
					}
				}
				for (Object obj : list) {
					initDataObjectWithAssignValues(obj);
					initDataObjectWithDefaultValues(obj);
					cocEntity.initDataObjectWithDefaultValues(cocActionID, obj);
				}
			} else {
				initDataObjectWithDefaultValues(dataObject);
				cocEntity.initDataObjectWithDefaultValues(cocActionID, dataObject);
			}
		}
	}

	private void selectProxyAction() {
		if (this.cocAction == null)
			return;

		List<String> proxyActions = this.cocAction.getProxyActionsList();
		if (proxyActions == null || proxyActions.size() == 0) {
			return;
		}

		for (String actionKey : proxyActions) {
			CocActionService action = this.cocEntity.getAction(actionKey);
			if (action == null) {
				this.exception = new CocException("操作不存在！%s", actionKey);
			}
			CndExpr expr = action.getWhere();
			boolean match = false;
			if (dataObject instanceof List) {
				List list = (List) dataObject;
				for (Object obj : list) {
					if (!ExprUtil.match(obj, expr)) {
						match = false;
						break;
					}
				}
			} else {
				match = ExprUtil.match(dataObject, expr);
			}

			if (match) {
				this.cocAction = action;
				break;
			}
		}
	}

	private void initDataObjectWithAssignValues(Object obj) {
		ObjectUtil.setValues(obj, cocAction.getAssignValues());
	}

	private void initDataObjectWithDefaultValues(Object obj) {

		/*
		 * 获取默认值
		 */
		Map defaultValues = new HashMap();
		// 菜单默认值
		Map map = systemMenu.getDefaultValues();
		if (map != null) {
			defaultValues.putAll(map);
		}
		// 操作按钮默认值
		map = cocAction.getDefaultValues();
		if (map != null) {
			defaultValues.putAll(map);
		}
		// 参数默认值
		map = this.getQueryJsonExpr();
		if (map != null) {
			defaultValues.putAll(map);
		}

		// 过滤器字段默认值
		String filterExpr = httpContext.getParameterValue("query.filterExpr", "");
		map = JsonUtil.loadFromJson(Map.class, filterExpr);
		if (map != null) {
			defaultValues.putAll(map);
		}

		/*
		 * 设置默认值
		 */
		ObjectUtil.setDefaultValues(obj, defaultValues);
	}

	private CndExpr makeRuleExprFromJson(String jsonExpr, StringBuffer logExpr) {

		CndExpr retExpr = null;
		if (StringUtil.isBlank(jsonExpr)) {
			return null;
		} else if (jsonExpr.charAt(0) != '{') {
			retExpr = Expr.contains(Const.F_NAME, jsonExpr).or(Expr.contains(Const.F_KEY, jsonExpr));
			// retExpr = retExpr.or(Expr.contains("desc", jsonExpr));
		} else {
			Map map = JsonUtil.loadFromJson(Map.class, jsonExpr);
			Iterator<String> exprs = map.keySet().iterator();
			while (exprs.hasNext()) {
				String prop = exprs.next();

				if (!StringUtil.isBlank(prop)) {
					String str = map.get(prop).toString();

					if (!StringUtil.isBlank(str)) {
						String[] array = StringUtil.toArray(prop, " ");
						String field = array[0];
						int dot = field.indexOf(".");
						if (dot > 0) {
							field = field.substring(0, dot);
						}
						String op = array.length > 1 ? array[1] : "eq";
						List<String> list;
						if (str.startsWith("[")) {
							list = StringUtil.toList(str.substring(1, str.length() - 1));
						} else {
							list = new ArrayList();
							list.add(str);
						}
						if (list.size() == 1) {
							if (retExpr == null) {
								retExpr = Expr.rule(field, op, list.get(0));
							} else {
								retExpr = retExpr.and(Expr.rule(field, op, list.get(0)));
							}
						} else if (list.size() > 1) {
							if (retExpr == null) {
								retExpr = Expr.in(field, list);
							} else {
								retExpr = retExpr.and(Expr.in(field, list));
							}
						}
					}
				}
			}
		}

		return retExpr;
	}

	/**
	 * 将JSON表达式转换成 in 表达式，即JSON对象中的字段值为数组。
	 * <p>
	 * JSON格式：
	 * <p>
	 * <code>
	 * {field-1:["value-1","value-2",...,"value-n"], field-2:[...], ... , field-n: [...]}
	 * </code>
	 */
	private CndExpr makeInExprFromJson(String jsonExpr, StringBuffer logExpr) {

		if (StringUtil.isBlank(jsonExpr)) {
			return null;
		}
		if (jsonExpr.charAt(0) != '{') {
			return Expr.rules(jsonExpr);
		}

		CndExpr retExpr = null;

		Map map = JsonUtil.loadFromJson(Map.class, jsonExpr);
		Iterator<String> exprs = map.keySet().iterator();
		while (exprs.hasNext()) {
			String prop = exprs.next().trim();

			String fld = prop;
			String op = "";
			Object value = map.get(prop);

			int idx = prop.indexOf(" ");
			if (idx > 0) {
				fld = prop.substring(0, idx);
				op = prop.substring(idx + 1);
			}

			idx = fld.indexOf(".");
			if (idx > -1) {
				fld = fld.substring(0, idx);
			}

			if (value instanceof List) {
				retExpr = makeExpr(retExpr, fld, op, (List) value);
			} else {
				retExpr = makeExpr(retExpr, fld, op, value.toString());
			}
		}

		return retExpr;
	}

	private CndExpr makeExpr(CndExpr retExpr, String fld, String op, List valueList) {
		if (valueList == null || valueList.size() == 0) {
			return retExpr;
		}

		if (valueList.size() == 1) {
			return makeExpr(retExpr, fld, op, valueList.get(0).toString());
		}

		if (StringUtil.isBlank(op)) {
			op = "in";
		}

		if (retExpr == null) {
			retExpr = Expr.rule(fld, op, valueList);
		} else {
			retExpr = retExpr.and(Expr.rule(fld, op, valueList));
		}

		return retExpr;
	}

	private CndExpr makeExpr(CndExpr retExpr, String fld, String op, String value) {
		if (value == null || value.trim().length() == 0) {
			return retExpr;
		}

		if ("-keywords-".equals(fld)) {
			if (retExpr == null) {
				retExpr = Expr.contains(Const.F_NAME, value).or(Expr.contains(Const.F_KEY, value));
			} else {
				retExpr = retExpr.and(Expr.contains(Const.F_NAME, value).or(Expr.contains(Const.F_KEY, value)));
			}
		} else {

			if (StringUtil.isBlank(op)) {
				op = "eq";
			}

			if (retExpr == null) {
				retExpr = Expr.rule(fld, op, value);
			} else {
				retExpr = retExpr.and(Expr.rule(fld, op, value));
			}
		}

		return retExpr;
	}

	public Map getQueryJsonExpr() {
		String queryJsonExpr = httpContext.getParameterValue("query.jsonExpr", "");

		return JsonUtil.loadFromJson(Map.class, queryJsonExpr);
	}

	/**
	 * 创建查询表达式：
	 * <p>
	 * 调用该方法，COC平台会自动将HTTP参数中的“query.jsonExpr”和“query.sqlExpr”转换成CndExpr表达式。
	 * <p>
	 * query.jsonExpr: 查询条件，语法（JSON表达式） 如：“{fld1: val1, fld2: [val3, val4, ...], 'fld3 cn': 'val5', '-keywords-': 'val6'}” <br/>
	 * op：操作符，操作符可以是下列简写“ eq(equals)|ne(notEquals)|lt|le|gt|ge|bw(beginWith)|bn(notBeginWith)|ni(not in)|ew(endWith)|en(notEndWith)|cn(contains)|LIKE|nc(notContains)|nu(is null)|nn(not null) ”<br/>
	 * -keywords-：关键字模糊查询，指定了该值，则其值将被作为关键字作模糊查询。
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public CndExpr makeExpr() {
		StringBuffer logExpr = new StringBuffer();
		CndExpr retExpr = null;

		String menuParamExpr = null;
		if (this.systemMenu != null) {
			menuParamExpr = this.systemMenu.getWhereRule();
		}
		String querySqlExpr = httpContext.getParameterValue("query.sqlExpr", "");
		// String queryFilterExpr = httpContext.getParameterValue("query.filterExpr", "");
		String queryJsonExpr = httpContext.getParameterValue("query.jsonExpr", "");
		// String queryKeywords = httpContext.getParameterValue("query.keywords", "");

		CndExpr menuExpr = this.makeInExprFromJson(menuParamExpr, logExpr);
		CndExpr sqlExpr = this.makeRuleExprFromJson(querySqlExpr, logExpr);
		// CndExpr filterExpr = this.makeInExprFromJson(queryFilterExpr, logExpr);
		CndExpr jsonExpr = this.makeInExprFromJson(queryJsonExpr, logExpr);
		// CndExpr searchBoxExpr = this.makeRuleExprFromJson(queryKeywords, logExpr);

		if (menuExpr != null) {
			if (retExpr == null)
				retExpr = menuExpr;
			else
				retExpr = retExpr.and(menuExpr);
		}
		if (sqlExpr != null) {
			if (retExpr == null)
				retExpr = sqlExpr;
			else
				retExpr = retExpr.and(sqlExpr);
		}
		// if (filterExpr != null) {
		// if (retExpr == null)
		// retExpr = filterExpr;
		// else
		// retExpr = retExpr.and(filterExpr);
		// }
		if (jsonExpr != null) {
			if (retExpr == null)
				retExpr = jsonExpr;
			else
				retExpr = retExpr.and(jsonExpr);
		}
		// if (searchBoxExpr != null) {
		// if (retExpr == null)
		// retExpr = searchBoxExpr;
		// else
		// retExpr = retExpr.and(searchBoxExpr);
		// }

		/*
		 * TODO: 树形GRID 暂时不支持分页
		 */
		treeField = httpContext.getParameterValue("query.treeField", "");
		// if (StringUtil.hasContent(treeField)) {
		// String treeFieldValue = httpContext.getParameterValue(treeField, "");
		// CndExpr treeExpr;
		// if (StringUtil.isBlank(treeFieldValue)) {
		// treeExpr = Expr.isNull(treeField).or(Expr.eq(treeField, ""));
		// } else {
		// treeExpr = Expr.eq(treeField, treeFieldValue);
		// }
		// if (retExpr == null)
		// retExpr = treeExpr;
		// else
		// retExpr = retExpr.and(treeExpr);
		// }

		// 解析JSON表达式
		if (retExpr == null) {
			retExpr = Expr.notNull("id");
			logExpr.append("(id not null)");
		}

		// if (StringUtil.hasContent(treeField)) {
		// retExpr = retExpr.addAsc(treeField);
		// }

		/*
		 * 解析排序
		 */
		String sortField = httpContext.getParameterValue("sortField", "id");
		String sortOrder = httpContext.getParameterValue("sortOrder", "desc");
		if (!StringUtil.isBlank(sortField)) {
			if (sortOrder.toLowerCase().equals("asc")) {
				retExpr = retExpr.addAsc(sortField);
			} else {
				retExpr = retExpr.addDesc(sortField);
			}
			logExpr.append("( order by " + sortField + " " + sortOrder + ")");
		}

		/*
		 * 解析分页
		 */
		int pageIndex = httpContext.getParameterValue("pageIndex", 0);
		int pageSize = httpContext.getParameterValue("pageSize", 0);
		if (pageIndex > 0 && pageSize > 0) {
			retExpr = retExpr.setPager(pageIndex, pageSize);
			logExpr.append("(pageIndex=" + pageIndex + " pageSize=" + pageSize + ")");
		}

		/*
		 * 返回解析结果
		 */
		LogUtil.debug("查询条件：funcExpr = %s, queryExpr = %s", funcExpr, logExpr.toString());

		return retExpr;
	}

	public HttpContext getHttpContext() {
		return httpContext;
	}

	// public Orm getOrm() {
	// return orm;
	// }

	public Throwable getException() {
		return exception;
	}

	public String getFuncExpr() {
		return funcExpr;
	}

	public String getDataArgs() {
		return dataArgs;
	}

	public String getSystemMenuID() {
		return systemMenuID;
	}

	public String getCocEntityID() {
		return cocEntityID;
	}

	public String getCocActionID() {
		return cocActionID;
	}

	public TenantService getTenant() {
		return tenant;
	}

	public SystemService getSystem() {
		return system;
	}

	public SystemMenuService getSystemMenu() {
		return systemMenu;
	}

	public CocEntityService getCocEntity() {
		return cocEntity;
	}

	public CocActionService getCocAction() {
		return cocAction;
	}

	public UIModelFactory getUiModelFactory() {
		return uiModelFactory;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

	public Object getDataObject() {
		return dataObject;
	}

	public String[] getDataID() {
		return dataID;
	}

	public void save() {
		this.dataManager.save(systemMenu, cocEntity, dataObject, this.cocActionID);
	}

	public List query() {
		return this.dataManager.query(systemMenu, cocEntity, this.cocActionID, this.makeExpr());
	}

	public Orm getOrm() {
		return this.dataManager.getDataEngine().orm();
	}

	public void delete() {
		this.dataManager.delete(systemMenu, cocEntity, dataObject, this.cocActionID);
	}

	public String getTreeField() {
		return treeField;
	}

	public String getRuntimeFuncExpr() {
		if (this.systemMenu != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(this.systemMenu.getKey());
			if (this.cocEntity != null) {
				sb.append(":").append(this.cocEntity.getKey());
				if (this.cocAction != null)
					sb.append(":").append(this.cocAction.getKey());
			}

			return sb.toString();
		}

		return this.funcExpr;
	}

}
