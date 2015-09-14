package com.jsoft.cocit.dmengine.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nutz.trans.Atom;
import org.nutz.trans.Trans;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.ExtHttpContext;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.dmengine.IDataManager;
import com.jsoft.cocit.dmengine.IEntityInfoFactory;
import com.jsoft.cocit.dmengine.info.ICocActionInfo;
import com.jsoft.cocit.dmengine.info.ICocCatalogInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormInfo;
import com.jsoft.cocit.dmengine.info.ICuiGridInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.mvc.CocEntityParam;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.ui.UIModelFactory;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;

public class WebCommandContext implements ICommandContext {

	public static final String REQUEST_KEY_HTTPCOMMAND = "commandContext";

	protected ExtHttpContext	httpContext;
	protected Throwable			exception;
	protected String			funcExpr;
	protected String			dataArgs;
	protected String			systemMenuID;
	protected String			cocEntityID;
	protected String			cocActionID;
	protected ITenantInfo		tenant;
	protected ISystemInfo		system;
	protected ISystemMenuInfo	systemMenu;
	protected ICocEntityInfo	cocEntity;
	protected ICocActionInfo	cocAction;
	protected UIModelFactory	uiModelFactory;
	protected IDataManager		dataManager;
	// protected Object dataObject;
	// protected String[] dataID;
	protected String			treeField;
	protected CndExpr			queryExpr	= null;
	protected CocEntityParam	entityParamNode;

	protected int			commandType	= 0;
	protected List<String>	prevCommands	= new ArrayList();
	protected List<String>	postCommands	= new ArrayList();

	protected Map context;

	protected Object result;

	/**
	 * 创建一个“操作助手”对象
	 * 
	 * @param funcExpr
	 *            功能参数：“moduleID:tableID:opMode”
	 * @return
	 */
	public static WebCommandContext make(String funcExpr) {
		WebCommandContext ret = new WebCommandContext(funcExpr, null, null, 0);

		if (ret.httpContext != null) {
			ret.httpContext.getRequest().setAttribute(REQUEST_KEY_HTTPCOMMAND, ret);
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
	public static WebCommandContext make(String funcExpr, String dataArgs, CocEntityParam httpParams) {
		WebCommandContext ret = new WebCommandContext(funcExpr, dataArgs, httpParams, 0);

		if (ret.httpContext != null) {
			ret.httpContext.getRequest().setAttribute(REQUEST_KEY_HTTPCOMMAND, ret);
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
	 * @param interceptorType
	 *            拦截器类型：{@link CommandNames#INTERCEPTOR_TYPE_FORM}; {@link CommandNames#INTERCEPTOR_TYPE_GRID}; {@link CommandNames#INTERCEPTOR_TYPE_COLUMN}。
	 * @return
	 */
	public static WebCommandContext make(String funcExpr, String dataArgs, CocEntityParam httpParams, int interceptorType) {
		WebCommandContext ret = new WebCommandContext(funcExpr, dataArgs, httpParams, interceptorType);

		if (ret.httpContext != null) {
			ret.httpContext.getRequest().setAttribute(REQUEST_KEY_HTTPCOMMAND, ret);
		}

		return ret;
	}

	protected WebCommandContext(String funcExpr, String dataArgs, CocEntityParam entityParamNode, int commandType) {
		LogUtil.debug("OpContext... {funcExpr:%s, dataArgs:%s, entityParamNode:%s}", funcExpr, dataArgs, entityParamNode);

		Cocit coc = Cocit.me();
		httpContext = (ExtHttpContext) coc.getHttpContext();

		this.funcExpr = funcExpr;
		this.dataArgs = dataArgs;

		this.commandType = commandType;

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

			this.entityParamNode = entityParamNode;

			// loadDataObject(dataArgs, entityParamNode);

			// selectProxyAction();

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

		IEntityInfoFactory entityServiceFactory = Cocit.me().getEntityServiceFactory();
		if (!StringUtil.isBlank(systemMenuID)) {
			systemMenu = system.getSystemMenu(systemMenuID);
			if (systemMenu == null) {
				try {
					systemMenu = system.getSystemMenu(Long.parseLong(systemMenuID));
				} catch (NumberFormatException e) {
				}
			}
			if (systemMenu == null) {
				ISystemInfo cocSystem = entityServiceFactory.getSystem(Cocit.me().getConfig().getCocitSystemCode());
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

			if (this.commandType == 0) {
				this.prevCommands.addAll(this.getCommonPrevInterceptors());
				this.prevCommands.addAll(StringUtil.toList(cocAction.getPrevInterceptors()));

				this.postCommands.addAll(StringUtil.toList(cocAction.getPostInterceptors()));
				this.postCommands.addAll(this.getCommonPostInterceptors());
			}
		}

		if (commandType != 0) {
			ICuiEntityInfo cuiEntityInfo = this.cocEntity.getCuiEntity(this.systemMenu.getUiView());
			if (cuiEntityInfo != null) {
				switch (commandType) {
					case CommandNames.INTERCEPTOR_TYPE_GRID:
						this.prevCommands.addAll(this.getCommonPrevInterceptors());

						ICuiGridInfo cuiGrid = cuiEntityInfo.getCuiGrid();
						if (cuiGrid != null) {
							this.prevCommands.addAll(StringUtil.toList(cuiGrid.getPrevInterceptors()));
							this.postCommands.addAll(StringUtil.toList(cuiGrid.getPostInterceptors()));
						}

						this.postCommands.addAll(this.getCommonPostInterceptors());

						break;
					case CommandNames.INTERCEPTOR_TYPE_FORM:
						this.prevCommands.addAll(this.getCommonPrevInterceptors());

						if (StringUtil.hasContent(this.cocAction.getUiForm())) {
							ICuiFormInfo cuiForm = cuiEntityInfo.getCuiForm(this.cocAction.getUiForm());

							if (cuiForm != null) {
								this.prevCommands.addAll(StringUtil.toList(cuiForm.getPrevInterceptors()));
								this.postCommands.addAll(StringUtil.toList(cuiForm.getPostInterceptors()));
							}
						}

						this.postCommands.addAll(this.getCommonPostInterceptors());

						break;
				}
			}
		}

		// System.out.println("LR：systemMenu=" + (systemMenu == null ? "" : systemMenu.hashCode()) + //
		// ", entityModule=" + (entityModule == null ? "" : entityModule.hashCode()) + //
		// ", entityAction=" + (entityAction == null ? "" : entityAction.hashCode())//
		// );

		// LogUtil.debug("OpContext.parseFuncExpr: systemMenu = %s, entityModule = %s", systemMenu, cocEntity);
	}

	public List<String> getCommonPrevInterceptors() {
		List<String> ret = new ArrayList();

		if (this.cocEntity != null) {
			addCommonPrevInterceptors(this.cocEntity.getCatalogInfo(), ret);
			ret.addAll(StringUtil.toList(this.cocEntity.getPrevInterceptors()));
		}

		return ret;
	}

	public List<String> getCommonPostInterceptors() {
		List<String> ret = new ArrayList();

		if (this.cocEntity != null) {
			ret.addAll(StringUtil.toList(this.cocEntity.getPostInterceptors()));
			addCommonPostInterceptors(this.cocEntity.getCatalogInfo(), ret);
		}

		return ret;
	}

	private void addCommonPrevInterceptors(ICocCatalogInfo catalogInfo, List<String> interceptors) {
		if (catalogInfo == null)
			return;

		addCommonPrevInterceptors(catalogInfo.getParentCatalog(), interceptors);

		interceptors.addAll(StringUtil.toList(catalogInfo.getPrevInterceptors()));
	}

	private void addCommonPostInterceptors(ICocCatalogInfo catalogInfo, List<String> interceptors) {
		if (catalogInfo == null)
			return;

		interceptors.addAll(StringUtil.toList(catalogInfo.getPostInterceptors()));

		addCommonPostInterceptors(catalogInfo.getParentCatalog(), interceptors);
	}

	//
	// private void selectProxyAction() {
	// if (this.cocAction == null)
	// return;
	//
	// List<String> proxyActions = this.cocAction.getProxyActionsList();
	// if (proxyActions == null || proxyActions.size() == 0) {
	// return;
	// }
	//
	// for (String actionKey : proxyActions) {
	// ICocActionInfo action = this.cocEntity.getAction(actionKey);
	// if (action == null) {
	// this.exception = new CocException("操作不存在！%s", actionKey);
	// }
	// CndExpr expr = action.getWhere();
	// boolean match = false;
	// if (result instanceof List) {
	// List list = (List) result;
	// for (Object obj : list) {
	// if (!ExprUtil.match(obj, expr)) {
	// match = false;
	// break;
	// }
	// }
	// } else {
	// match = ExprUtil.match(result, expr);
	// }
	//
	// if (match) {
	// this.cocAction = action;
	// break;
	// }
	// }
	// }

	private CndExpr makeRuleExprFromJson(String jsonExpr, StringBuffer logExpr) {

		CndExpr retExpr = null;
		if (StringUtil.isBlank(jsonExpr)) {
			return null;
		} else if (jsonExpr.charAt(0) != '{') {
			retExpr = Expr.contains(Const.F_NAME, jsonExpr).or(Expr.contains(Const.F_CODE, jsonExpr));
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
	public CndExpr getQueryExpr() {
		if (queryExpr != null)
			return queryExpr;

		StringBuffer logExpr = new StringBuffer();

		String menuParamExpr = null;
		if (this.systemMenu != null) {
			menuParamExpr = this.systemMenu.getWhereRule();
		}
		String querySqlExpr = httpContext.getParameterValue("query.sqlExpr", "");
		// String queryFilterExpr = httpContext.getParameterValue("query.filterExpr", "");
		String queryJsonExpr = httpContext.getParameterValue("query.jsonExpr", "");
		// String queryKeywords = httpContext.getParameterValue("query.keywords", "");

		CndExpr menuExpr = ExprUtil.makeInExprFromJson(menuParamExpr, logExpr);
		CndExpr sqlExpr = this.makeRuleExprFromJson(querySqlExpr, logExpr);
		// CndExpr filterExpr = this.makeInExprFromJson(queryFilterExpr, logExpr);
		CndExpr jsonExpr = ExprUtil.makeInExprFromJson(queryJsonExpr, logExpr);
		// CndExpr searchBoxExpr = this.makeRuleExprFromJson(queryKeywords, logExpr);

		if (menuExpr != null) {
			if (queryExpr == null)
				queryExpr = menuExpr;
			else
				queryExpr = queryExpr.and(menuExpr);
		}
		if (sqlExpr != null) {
			if (queryExpr == null)
				queryExpr = sqlExpr;
			else
				queryExpr = queryExpr.and(sqlExpr);
		}
		// if (filterExpr != null) {
		// if (retExpr == null)
		// retExpr = filterExpr;
		// else
		// retExpr = retExpr.and(filterExpr);
		// }
		if (jsonExpr != null) {
			if (queryExpr == null)
				queryExpr = jsonExpr;
			else
				queryExpr = queryExpr.and(jsonExpr);
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
		// if (queryExpr == null) {
		// queryExpr = Expr.notNull("id");
		// logExpr.append("(id not null)");
		// }

		// if (StringUtil.hasContent(treeField)) {
		// retExpr = retExpr.addAsc(treeField);
		// }

		/*
		 * 解析排序
		 */
		String sortField = httpContext.getParameterValue("sortField", "id");
		String sortOrder = httpContext.getParameterValue("sortOrder", "desc");
		if (!StringUtil.isBlank(sortField)) {
			if (queryExpr == null) {
				if (sortOrder.toLowerCase().equals("asc")) {
					queryExpr = Expr.orderby(sortField + " asc");
				} else {
					queryExpr = Expr.orderby(sortField + " desc");
				}
			} else {
				if (sortOrder.toLowerCase().equals("asc")) {
					queryExpr = queryExpr.addAsc(sortField);
				} else {
					queryExpr = queryExpr.addDesc(sortField);
				}
			}
			logExpr.append("( order by " + sortField + " " + sortOrder + ")");
		}

		/*
		 * 解析分页
		 */
		int pageIndex = httpContext.getParameterValue("pageIndex", 0);
		int pageSize = httpContext.getParameterValue("pageSize", 0);
		if (pageIndex > 0 && pageSize > 0) {
			if (queryExpr == null) {
				queryExpr = Expr.page(pageIndex, pageSize);
			} else {
				queryExpr = queryExpr.setPager(pageIndex, pageSize);
			}

			logExpr.append("(pageIndex=" + pageIndex + " pageSize=" + pageSize + ")");
		}

		/*
		 * 返回解析结果
		 */
		LogUtil.debug("查询条件：funcExpr = %s, queryExpr = %s", funcExpr, logExpr.toString());

		return queryExpr;
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

	public ITenantInfo getTenant() {
		return tenant;
	}

	public ISystemInfo getSystem() {
		return system;
	}

	public ISystemMenuInfo getSystemMenu() {
		return systemMenu;
	}

	public ICocEntityInfo getCocEntity() {
		return cocEntity;
	}

	public ICocActionInfo getCocAction() {
		return cocAction;
	}

	public UIModelFactory getUiModelFactory() {
		return uiModelFactory;
	}

	public IDataManager getDataManager() {
		return dataManager;
	}

	public Object getDataObject() {
		if (result != null && result instanceof List) {
			List list = (List) result;
			if (list.size() > 0) {
				return list.get(0);
			}

			return null;
		}

		return result;
	}

	// public String[] getDataID() {
	// return dataID;
	// }

	public IOrm getOrm() {
		return this.dataManager.getDataEngine().orm();
	}

	public String getTreeField() {
		return treeField;
	}

	public String getRuntimeFuncExpr() {
		if (this.systemMenu != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(this.systemMenu.getCode());
			if (this.cocEntity != null) {
				sb.append(":").append(this.cocEntity.getCode());
				if (this.cocAction != null)
					sb.append(":").append(this.cocAction.getCode());
			}

			return sb.toString();
		}

		return this.funcExpr;
	}

	/**
	 * 获取环境变量
	 */
	public Map getContext() {
		return context;
	}

	/**
	 * 设置环境变量
	 * 
	 * @param varName
	 *            变量名称
	 * @param varValue
	 *            变量值
	 */
	public void put(String varName, Object varValue) {
		if (context == null) {
			context = new HashMap();
		}

		context.put(varName, varValue);
	}

	/**
	 * 获取环境变量
	 * 
	 * @param varName
	 *            变量名称
	 * @return 变量值
	 */
	public Object get(String varName) {
		if (context != null)
			return context.get(varName);

		return null;
	}

	public void release() {
	}

	@Override
	public void execute(final List prevInterceptorOrNames, String commandExecutorName, final List postInterceptorOrNames) {
		ICommandInterceptors cmds = Cocit.me().getCommandInterceptors();

		List<ICommandInterceptor> prevInterceptors = new ArrayList();
		if (prevInterceptorOrNames != null) {
			for (Object obj : prevInterceptorOrNames) {
				if (obj instanceof ICommandInterceptor) {
					prevInterceptors.add((ICommandInterceptor) obj);
				} else if (obj instanceof String) {
					ICommandInterceptor cmd = cmds.getCommandInterceptor((String) obj);
					if (cmd == null) {
						throw new CocException("命令拦截器  %s 不存在！", obj);
					}
					prevInterceptors.add(cmd);
				}
			}
		}

		List<ICommandInterceptor> postInterceptors = new ArrayList();
		if (postInterceptorOrNames != null) {
			for (Object obj : postInterceptorOrNames) {
				if (obj instanceof ICommandInterceptor) {
					postInterceptors.add((ICommandInterceptor) obj);
				} else if (obj instanceof String) {
					ICommandInterceptor cmd = cmds.getCommandInterceptor((String) obj);
					if (cmd == null) {
						throw new CocException("命令拦截器  %s 不存在！", obj);
					}
					postInterceptors.add(cmd);
				}
			}
		}

		this.execute(prevInterceptors, cmds.getCommandInterceptor(commandExecutorName), postInterceptors);

	}

	@Override
	public void execute(final List<ICommandInterceptor> prevInterceptors, final ICommandInterceptor commandExecutor, final List<ICommandInterceptor> postInterceptors) {
		PlatformTransactionManager transactionManager = Cocit.me().getBean("transactionManager");
		if (transactionManager != null) {

			TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

			transactionTemplate.execute(new TransactionCallback() {
				public Object doInTransaction(TransactionStatus status) {

					Trans.exec(new Atom() {
						public void run() {
							_doInTransaction(prevInterceptors, commandExecutor, postInterceptors);
						}
					});

					return null;
				}
			});

		} else {

			Trans.exec(new Atom() {
				public void run() {
					_doInTransaction(prevInterceptors, commandExecutor, postInterceptors);
				}
			});

		}
	}

	private void _doInTransaction(List<ICommandInterceptor> prevInterceptors, ICommandInterceptor commandExecutor, List<ICommandInterceptor> postInterceptors) {
		final ICommandInterceptors cmds = Cocit.me().getCommandInterceptors();

		if (prevInterceptors != null) {
			for (ICommandInterceptor prev : prevInterceptors) {
				if (prev.execute(this)) {
					return;
				}
			}
		}

		if (prevCommands != null) {
			for (String cmdName : prevCommands) {
				ICommandInterceptor cmd = cmds.getCommandInterceptor(cmdName);
				if (cmd == null) {
					throw new CocException("命令拦截器  %s 不存在！", cmdName);
				}
				if (cmd.execute(this)) {
					return;
				}
			}
		}

		if (commandExecutor != null) {
			if (commandExecutor.execute(this)) {
				return;
			}
		}

		if (postCommands != null) {
			for (String cmdName : postCommands) {
				ICommandInterceptor cmd = cmds.getCommandInterceptor(cmdName);
				if (cmd == null) {
					throw new CocException("命令拦截器  %s 不存在！", cmdName);
				}
				if (cmd.execute(this)) {
					return;
				}
			}
		}

		if (postInterceptors != null) {
			for (ICommandInterceptor post : postInterceptors) {
				if (post.execute(this)) {
					return;
				}
			}
		}
	}

	@Override
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public void setQueryExpr(CndExpr queryExpr) {
		this.queryExpr = queryExpr;
	}

	public CocEntityParam getEntityParamNode() {
		return entityParamNode;
	}

	// public void setDataObject(Object dataObject) {
	// this.dataObject = dataObject;
	// }

	public void setException(CocException ex) {
		this.exception = ex;
	}

	// public int getCommandType() {
	// return commandType;
	// }
	//
	// public void setCommandType(int commandType) {
	// this.commandType = commandType;
	// }

	// public List<String> getPrevCommands() {
	// return prevCommands;
	// }
	//
	// public void setPrevCommands(List<String> prevCommands) {
	// this.prevCommands = prevCommands;
	// }

	// public List<String> getPostCommands() {
	// return postCommands;
	// }

	// public void setPostCommands(List<String> postCommands) {
	// this.postCommands = postCommands;
	// }

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public void setFuncExpr(String funcExpr) {
		this.funcExpr = funcExpr;
	}

	public void setDataArgs(String dataArgs) {
		this.dataArgs = dataArgs;
	}

	public void setSystemMenuID(String systemMenuID) {
		this.systemMenuID = systemMenuID;
	}

	public void setCocEntityID(String cocEntityID) {
		this.cocEntityID = cocEntityID;
	}

	public void setCocActionID(String cocActionID) {
		this.cocActionID = cocActionID;
	}

	public void setTenant(ITenantInfo tenant) {
		this.tenant = tenant;
	}

	public void setSystem(ISystemInfo system) {
		this.system = system;
	}

	public void setSystemMenu(ISystemMenuInfo systemMenu) {
		this.systemMenu = systemMenu;
	}

	public void setCocEntity(ICocEntityInfo cocEntity) {
		this.cocEntity = cocEntity;
	}

	public void setCocAction(ICocActionInfo cocAction) {
		this.cocAction = cocAction;
	}

	public void setTreeField(String treeField) {
		this.treeField = treeField;
	}

	public void setContext(Map context) {
		this.context = context;
	}
}
