package com.jsoft.cocit.action;

import static com.jsoft.cocit.constant.UrlAPI.ENTITY_URL_PREFIX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.action.beans.AuthItem;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.entity.security.IAuthority;
import com.jsoft.cocit.entity.security.ISystemMenu;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.orm.ExtOrm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.ui.UIModelFactory;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.UIGridDataModel;
import com.jsoft.cocit.ui.model.UIGridModel;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class UserAuthAction {

	/**
	 * 获取“待授权”的用户
	 * 
	 * @param funcExpr
	 * @return
	 */
	@At("/cocentity/getAuthUsers")
	public UIGridDataModel getAuthUsers() {
		String funcExpr = Const.TBL_SEC_SYSUSER;
		OpContext opContext = OpContext.make(funcExpr, null, null);
		UIModelFactory uiFactory = opContext.getUiModelFactory();

		UIGridDataModel dataModel = uiFactory.makeGridData();
		if (opContext.getException() != null) {

			dataModel.setException(opContext.getException());

		} else {

			// 构造Grid数据模型
			UIGridModel grid = opContext.getUiModelFactory().getGrid(opContext.getSystemMenu(), opContext.getCocEntity());
			// grid.setTreeField(opContext.getTreeField());
			String view = grid.getViewName();
			if (StringUtil.hasContent(view)) {
				dataModel.setViewName(view + "data");
			}

			dataModel.setModel(grid);
			// 构造查询条件
			CndExpr expr = opContext.makeExpr();
			try {
				if (expr != null) {
					expr = expr.and(Expr.ne("key", Cocit.me().getHttpContext().getLoginUsername()));
				} else {
					expr = Expr.ne("key", Cocit.me().getHttpContext().getLoginUsername());
				}
				List data = opContext.getDataManager().query(opContext.getSystemMenu(), opContext.getCocEntity(), null, expr);
				int total = opContext.getDataManager().count(opContext.getSystemMenu(), opContext.getCocEntity(), null, expr);

				dataModel.setData(data);
				dataModel.setTotal(total);

				LogUtil.debug("EntityAction.getEntityGridData: total = %s", total);
			} catch (Throwable e) {
				LogUtil.error("获取网格JSON数据失败！", e);

				dataModel.setException(e);
			}
		}

		LogUtil.debug("EntityAction.getEntityGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return dataModel;
	}

	/**
	 * 获取用户“拥有的”权限条目：即“用户已经拥有了哪些权限？”
	 * 
	 * @param funcExpr
	 * @return
	 */
	@At(ENTITY_URL_PREFIX + "/getAuthsByUser/*")
	public UIGridDataModel getAuthsByUser(String strColumns) {
		String funcExpr = Const.TBL_SEC_AUTHORITY;

		/*
		 * 获取GRID列名
		 */
		strColumns = StringUtil.decodeHex(strColumns);
		List<String> columnList = StringUtil.toList(strColumns);

		/*
		 * 获取服务对象(API)
		 */
		Cocit coc = Cocit.me();
		OpContext opContext = OpContext.make(funcExpr, null, null);
		UIModelFactory uiFactory = opContext.getUiModelFactory();
		SystemService systemService = coc.getHttpContext().getLoginSystem();
		ExtOrm orm = (ExtOrm) opContext.getDataManager().getDataEngine().orm();

		/*
		 * 创建UI模型
		 */
		UIGridDataModel gridData = uiFactory.makeGridData();
		UIGridModel grid = gridData.getModel();
		try {
			Map params = opContext.getQueryJsonExpr();

			/*
			 * 添加字段到GRID中
			 */
			UIFieldModel column;
			for (String prop : columnList) {
				if ("id".equals(prop))
					continue;

				column = uiFactory.makeField();
				column.setPropName(prop);
				grid.addColumn(column);
				if ("actionsAuthUrl".equals(prop)) {
					column.setViewName("actionsauth");
				} else if ("rowsAuthUrl".equals(prop)) {
					column.setViewName("rowsauth");
				}
			}
			gridData.setViewName(ViewNames.VIEW_TREEGRIDDATA);

			String userKey = "";
			List<String> userList = (List) params.get("userKey");
			if (userList != null && userList.size() > 0) {
				userKey = userList.get(0);
			}
			String systemKey = systemService.getKey();

			Map<String, IAuthority> allowAuthMap = new HashMap();
			List<IAuthority> allowAuthList = null;
			if (StringUtil.hasContent(userKey) && StringUtil.hasContent(systemKey)) {
				allowAuthList = (List<IAuthority>) orm.query(EntityTypes.Authority, Expr.eq("userKey", userKey).and(Expr.eq("systemKey", systemKey)));
				for (IAuthority a : allowAuthList) {
					allowAuthMap.put(a.getMenuKey(), a);
				}
			}

			List<AuthItem> systemAuths = new ArrayList();
			List<SystemMenuService> menus = systemService.getSystemMenus();
			for (SystemMenuService menu : menus) {
				AuthItem menuAuth = makeMenuAuth(menu, systemKey, userKey);

				IAuthority allowAuth = allowAuthMap.get(menu.getKey());
				if (allowAuth != null) {
					menuAuth.setAllowMenu(true);

					menuAuth.setAllowActions(allowAuth.getMenuActions());
					menuAuth.setAllowActionsNames(allowAuth.getMenuActionsNames());
					menuAuth.setAllowRows(allowAuth.getDataRows());
					menuAuth.setAllowRowsNames(allowAuth.getDataRowsNames());
					menuAuth.setAllowCols(allowAuth.getDataCols());
					menuAuth.setAllowColsNames(allowAuth.getDataColsNames());
				}

				systemAuths.add(menuAuth);
			}

			/*
			 * 创建菜单
			 */
			Tree tree = Tree.make();
			this.makeNodes(tree, systemAuths, 0);

			/*
			 * 将查询结果放入数据模型
			 */
			// gridData.setTotal(total);
			gridData.setData(tree);

			opContext.release();
		} catch (Throwable e) {
			gridData.setException(e);
		}

		return gridData;
	}

	private AuthItem makeMenuAuth(SystemMenuService menu, String systemKey, String userKey) {
		AuthItem ret = new AuthItem();

		ret.setId(menu.getId());
		ret.setSystemKey(systemKey);
		ret.setUserKey(userKey);
		ret.setMenuKey(menu.getKey());
		ret.setMenuName(menu.getName());
		ret.setMenuParentKey(menu.getParentKey());
		if (menu.getActionKeys().size() > 0) {
			ret.setActionsAuthUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_GET_ACTIONS_DATA, menu));
		}
		ret.setActionsAuth(menu.getCocActions());
		ret.setColsAuthUrl(menu.getFields());
		CocEntityService entity = menu.getCocEntity();
		if (entity != null && StringUtil.hasContent(entity.getDataAuthFields())) {
			ret.setRowsAuthUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_GET_ROWS_AUTH_DATA, menu));
		}

		return ret;
	}

	// private List<IAuthority> getUserAuths(ExtOrm orm, Map params, String systemKey, String userKey) {
	// orm.getEnMapping(EntityTypes.SystemMenu);
	// orm.getEnMapping(EntityTypes.SystemUser);
	// orm.getEnMapping(EntityTypes.Authority);
	//
	// /*
	// * 查询数据
	// */
	// // String sqlCount = "select count(*) from coc_sec_sysmenu";
	// // int total = orm.execSql(sqlCount, sqlParams, columnToPropMap);
	//
	// StringBuffer sql = new StringBuffer();
	// sql.append("SELECT");
	// sql.append("\n auth.system_key AS systemKey,");
	// sql.append("\n auth.id_ AS id,");
	// sql.append("\n auth.status_code_ AS statusCode,");
	// sql.append("\n auth.menu_key AS menuKey,");
	// sql.append("\n menu.name_ AS menuName,");
	// sql.append("\n menu.parent_key_ AS menuParentKey,");
	// sql.append("\n auth.user_key AS userKey,");
	// sql.append("\n user.name_ AS userName,");
	// sql.append("\n auth.menu_actions AS allowActions");
	// sql.append("\n auth.data_rows AS allowDatas");
	// sql.append("\n");
	// sql.append("\n FROM ((coc_sec_authority auth LEFT JOIN coc_sec_sysmenu menu ON auth.menu_key = menu.key_ AND auth.system_key = menu.system_key)");
	// sql.append("\n LEFT JOIN coc_sec_sysuser user ON auth.user_key = user.key_ AND auth.tenant_key_ = user.tenant_key_)");
	// sql.append("\n");
	// sql.append("\n WHERE auth.user_key = ? and auth.system_key = ? and auth.tenant_key_ = ?");
	//
	// LogUtil.debug("查询用户权限：SQL=%s", sql);
	//
	// List sqlParams = new ArrayList();
	//
	// sqlParams.add(userKey);
	// sqlParams.add(systemKey);
	// sqlParams.add("");
	//
	// Map<String, String> columnToPropMap = new HashMap();
	// // columnToPropMap.put("key_", "menuKey");
	// // columnToPropMap.put("name_", "menuName");

	// return orm.query(EntityTypes.Authority, sql.toString(), sqlParams, columnToPropMap);
	// }

	private void makeNodes(Tree tree, List<AuthItem> menus, int depth) {
		for (AuthItem menu : menus) {
			String key = menu.getMenuKey();
			String parentKey = menu.getMenuParentKey();
			Node node = tree.addNode(parentKey, key);
			if (node != null) {
				node.setName(menu.getMenuName()).setReferObj(menu);
				if (menu.isAllowMenu()) {
					node.set("checked", true);
				}
			}
		}

		this.queryParents(tree, depth);
	}

	private void makeParentNodes(Tree tree, List<ISystemMenu> menus, int depth) {
		for (ISystemMenu menu : menus) {
			String key = menu.getKey();
			String parentKey = menu.getParentKey();

			AuthItem permmenu = new AuthItem();
			permmenu.setMenuKey(key);
			permmenu.setMenuName(menu.getName());
			permmenu.setMenuParentKey(parentKey);
			permmenu.setId("menu" + menu.getId());
			Node node = tree.addNode(parentKey, key);
			node.setName(permmenu.getMenuName()).setReferObj(permmenu);
		}

		this.queryParents(tree, depth);
	}

	private void queryParents(Tree tree, int depth) {
		if (depth > 5) {
			return;
		}

		List<String> parentKeys = new ArrayList();
		Map<String, Node> nodes = tree.getAllMap();
		for (Node node : nodes.values()) {
			Node parentNode = node.getParent();
			if (parentNode != null) {
				AuthItem menu = (AuthItem) node.getReferObj();
				String parentKey = menu.getMenuParentKey();
				if (parentNode.getReferObj() == null && StringUtil.hasContent(parentKey)) {
					parentKeys.add(parentKey);
				}
			}
		}

		if (parentKeys.size() > 0) {
			List<ISystemMenu> parentRows = (List<ISystemMenu>) Cocit.me().orm().query(EntityTypes.SystemMenu, Expr.in("key", parentKeys));
			this.makeParentNodes(tree, parentRows, ++depth);
		}
	}

}
