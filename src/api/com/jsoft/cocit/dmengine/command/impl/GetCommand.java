package com.jsoft.cocit.dmengine.command.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;

import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.dmengine.IDataManager;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.command.WebCommandInterceptor;
import com.jsoft.cocit.dmengine.info.ICocActionInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.mvc.CocEntityParam;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;

public class GetCommand extends WebCommandInterceptor {

	@Override
	public String getName() {
		return CommandNames.COC_GET;
	}

	@Override
	protected boolean execute(WebCommandContext cmdctx) {

		ICocActionInfo cocAction = cmdctx.getCocAction();
		ICocEntityInfo cocEntity = cmdctx.getCocEntity();
		String dataArgs = cmdctx.getDataArgs();
		String[] dataID;
		ISystemMenuInfo systemMenu = cmdctx.getSystemMenu();
		IDataManager dataManager = cmdctx.getDataManager();
		Object dataObject = null;
		CocException exception = null;
		CocEntityParam entityParamNode = cmdctx.getEntityParamNode();
		String cocActionID = cmdctx.getCocActionID();

		if (cocAction == null)
			return false;

		Class type = cocEntity.getClassOfEntity();

		/*
		 * 查询数据对象
		 */
		int opCode = cocAction.getOpCode();
		if (opCode == OpCodes.OP_INSERT_FORM_DATA || opCode == OpCodes.OP_INSERT_GRID_ROW || opCode == OpCodes.OP_SAVE_GRID_ROWS) {// 添加操作

		} else {// 修改类操作

			/*
			 * 计算主键字段
			 */
			String pkFld = Const.F_ID;
			if (dataArgs.startsWith("${") && dataArgs.indexOf("}") > 0) {
				int dot = dataArgs.indexOf("}");
				pkFld = dataArgs.substring(2, dot);
				dataArgs = dataArgs.substring(dot + 1);
			}

			/*
			 * 计算主键字段值
			 */
			List idList = new ArrayList();
			if (Const.F_ID.equals(pkFld)) {
				dataID = StringUtil.toArray(dataArgs);
				for (String s : dataID) {
					try {
						idList.add(Long.parseLong(s));
					} catch (Throwable e) {
					}
				}
			} else {
				idList = StringUtil.toList(dataArgs);
			}

			/*
			 * 创建数据加载条件
			 */
			CndExpr expr = null;
			if (idList.size() > 0) {
				expr = Expr.in(pkFld, idList);
			}

			/*
			 * 获取菜单查询条件
			 */
			CndExpr menuExpr = null;
			if (StringUtil.hasContent(systemMenu.getWhereRule())) {
				menuExpr = ExprUtil.makeInExprFromJson(systemMenu.getWhereRule(), new StringBuffer());
			}

			/*
			 * 合并查询条件
			 */
			Object id = null;
			if (idList.size() == 1) {
				id = idList.get(0);
			}
			Integer firstRowID = new Integer(-1);
			if (firstRowID.equals(id)) {// 自动获取满足条件的第一条数据
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
			if (expr != null) {
				if ((opCode >= OpCodes.OP_UPDATE_FORM_DATA && opCode <= 150) || (opCode > 200 && opCode <= 250)) {// 查询单条数据
					// if (fireLoadEvent) {
					// dataObject = dataManager.load(systemMenu, cocEntity, cocActionID, expr);
					// } else {
					dataObject = dataManager.getDataEngine().load(type, expr);
					// }
					if (dataObject == null && !firstRowID.equals(id)) {
						exception = new CocException("访问的数据不存在！");
					}
				} else if ((opCode > 150 && opCode <= 190) || (opCode > 250 && opCode <= 290)) {// 查询多条数据
					// if (fireLoadEvent) {
					// dataObject = dataManager.query(systemMenu, cocEntity, cocActionID, expr);
					// } else {
					dataObject = dataManager.getDataEngine().query(type, expr);
					// }
					List list = (List) dataObject;
					if ((list == null || list.size() == 0) && !firstRowID.equals(id)) {
						exception = new CocException("访问的数据不存在！");
					}
				}
			}
		}

		/*
		 * 检查待访问的数据是否满足操作条件？
		 */
		if (dataObject != null) {
			String msg = "";
			if (cocAction != null) {
				msg = cocAction.getErrorMessage();
				if (StringUtil.isBlank(msg))
					msg = "数据不能被“" + cocAction.getName() + "”";
			}

			if (dataObject instanceof List) {
				List list = (List) dataObject;
				for (Object obj : list) {
					if (!ExprUtil.match(obj, cocAction.getWhere())) {
						exception = new CocException(msg);
						return false;
					}
				}
			} else {
				if (!ExprUtil.match(dataObject, cocAction.getWhere())) {
					exception = new CocException(msg);
					return false;
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
						initDataObjectWithAssignValues(cmdctx, obj);

						initDataObjectWithDefaultValues(cmdctx, obj);
						cocEntity.initDataObjectWithDefaultValues(cocActionID, obj);
					}
				} else {
					initDataObjectWithAssignValues(cmdctx, dataObject);

					initDataObjectWithDefaultValues(cmdctx, dataObject);
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
					initDataObjectWithAssignValues(cmdctx, obj);
				}
			} else {
				initDataObjectWithAssignValues(cmdctx, dataObject);
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
					initDataObjectWithAssignValues(cmdctx, obj);
					initDataObjectWithDefaultValues(cmdctx, obj);
					cocEntity.initDataObjectWithDefaultValues(cocActionID, obj);
				}
			} else {
				initDataObjectWithDefaultValues(cmdctx, dataObject);
				cocEntity.initDataObjectWithDefaultValues(cocActionID, dataObject);
			}
		}

		cmdctx.setResult(dataObject);
		cmdctx.setException(exception);

		return false;
	}

	private void initDataObjectWithAssignValues(WebCommandContext cmdctx, Object obj) {
		ObjectUtil.setValues(obj, cmdctx.getCocAction().getAssignValues());
	}

	private void initDataObjectWithDefaultValues(WebCommandContext cmdctx, Object obj) {

		/*
		 * 获取默认值
		 */
		Map defaultValues = new HashMap();
		// 菜单默认值
		Map map = cmdctx.getSystemMenu().getDefaultValues();
		if (map != null) {
			defaultValues.putAll(map);
		}
		// 操作按钮默认值
		map = cmdctx.getCocAction().getDefaultValues();
		if (map != null) {
			defaultValues.putAll(map);
		}
		// 参数默认值
		map = cmdctx.getQueryJsonExpr();
		if (map != null) {
			defaultValues.putAll(map);
		}

		// 过滤器字段默认值
		String filterExpr = cmdctx.getHttpContext().getParameterValue("query.filterExpr", "");
		map = JsonUtil.loadFromJson(Map.class, filterExpr);
		if (map != null) {
			defaultValues.putAll(map);
		}

		/*
		 * 设置默认值
		 */
		ObjectUtil.setDefaultValues(obj, defaultValues);
	}

}