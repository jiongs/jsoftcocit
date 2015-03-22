package com.jsoft.cocit.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.jsoft.cocimpl.ExtHttpContext;
import com.jsoft.cocimpl.action.AdminAction;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.entity.security.ISystemMenu;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.securityengine.LoginSession;
import com.jsoft.cocit.securityengine.SecurityEngine;
import com.jsoft.cocit.service.IndexService;
import com.jsoft.cocit.ui.model.SmartyModel;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.util.DateUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class AdminIndexAction extends AdminAction {

	protected void init() {
		super.init();

		bodyTabsSpaceHeight = 40;
		bodyTabsSpaceWidth = 0;
		frameGap = 5;
	}

	@At(ADMIN_URL)
	@Fail("redirect:" + ADMIN_URL + "/login")
	public UIModel admin() {
		SmartyModel sm = (SmartyModel) super.admin();

		sm.put("bodyUrl", MVCUtil.makeUrl(adminUrl + "/index", ""));

		return sm;
	}

	@At(ADMIN_URL + "/top")
	public UIModel top() throws CocException {
		Cocit coc = Cocit.me();

		SecurityEngine securityEngine = coc.getSecurityEngine();
		securityEngine.checkLoginUserType(Const.USER_SYSTEM);
		IMessageConfig messages = coc.getMessages();

		SmartyModel ret = (SmartyModel) super.top();

		ret.put("indexPageTitle", messages.getMsg("10004"));

		return ret;
	}

	@At(ADMIN_URL + "/index/*")
	@Fail("redirect:" + ADMIN_URL + "/login")
	public UIModel index(boolean isAjax) {
		Cocit coc = Cocit.me();
		HttpContext context = coc.getHttpContext();

		SecurityEngine securityEngine = coc.getSecurityEngine();
		securityEngine.checkLoginUserType(Const.USER_SYSTEM);
		IndexService indexService = coc.getBean(IndexService.class);
		LoginSession loginSession = context.getLoginSession();
		IMessageConfig messages = coc.getMessages();

		String username = loginSession.getUsername();

		int bodyHeight = (Integer) loginSession.get(Const.CONFIG_KEY_UI_BODYHEIGHT);
		int bodyWidth = (Integer) loginSession.get(Const.CONFIG_KEY_UI_BODYWIDTH);

		/*
		 * 准备“全局”变量
		 */
		Map data = new HashMap();
		data.put("adminUrl", adminUrl);
		data.put("resourcePath", resourcePath);
		data.put("indexPageTitle", messages.getMsg("10004"));
		data.put("closeAllTitle", messages.getMsg("10005"));

		/*
		 * 准备“一级页面”变量
		 */
		data.put("tabs1Height", bodyHeight + bodyTabsSpaceHeight);
		data.put("tabs1Width", bodyWidth + leftWidth);

		/*
		 * 准备“二级页面”变量
		 */
		data.put("tabs2Height", bodyHeight);
		data.put("tabs2Width", bodyWidth + leftWidth);

		/*
		 * 准备“二级首页（个人工作台）”变量
		 */
		int ulliPadding = 8;
		int blockPadding = 10;
		int hDiff = bodyHeight - 700;
		if (hDiff > 0) {
			ulliPadding += hDiff / 100;
			blockPadding += hDiff / 20;
		}
		int itemHeight = 20 + ulliPadding * 2;
		int titleHeight = 30 + blockPadding;

		data.put("ulliPadding", ulliPadding);
		data.put("blockPadding", blockPadding);

		int contentHeight = bodyHeight - bodyTabsSpaceHeight - 18;

		/*
		 * 计算左边高度
		 */
		int schCalendarHeight = 290;
		data.put("schCalendarHeight", schCalendarHeight);
		int schTasksHeight = 3 * itemHeight - 10;
		data.put("schTasksHeight", schTasksHeight);
		int publicMsgsHeight = contentHeight - schCalendarHeight - schTasksHeight - blockPadding * 4 - 2;
		data.put("publicMsgsHeight", publicMsgsHeight);

		/*
		 * 计算中间高度
		 */
		int wfStatHeight = 215;
		data.put("wfStatHeight", wfStatHeight);
		int searchBoxHeight = 65;
		data.put("searchBoxHeight", searchBoxHeight);
		int wfTasksHeight = contentHeight - wfStatHeight - searchBoxHeight - blockPadding * 4 - 2;
		data.put("wfTasksHeight", wfTasksHeight);

		/*
		 * 计算右边高度
		 */
		int appCenterHeight = 362;
		data.put("appCenterHeight", appCenterHeight);
		int myMsgsHeight = contentHeight - appCenterHeight - blockPadding * 3 - 2;
		data.put("myMsgsHeight", myMsgsHeight);

		/*
		 * 首页数据
		 */
		int publicMessagesNum = (publicMsgsHeight - titleHeight) / itemHeight;
		data.put("publicMessages", indexService.getPublicMessages(publicMessagesNum));
		int myMessagesNum = (myMsgsHeight - titleHeight) / itemHeight;
		data.put("myMessages", indexService.getMyMessages(myMessagesNum, username));
		data.put("searchCategories", indexService.getSearchCategories(3));
		data.put("searchKeywords", indexService.getSearchKeywords(3));
		data.put("workflowStatistics", indexService.getMyWorkflowTaskStatistics(username));
		int workflowTasksNum = wfTasksHeight / itemHeight;
		data.put("workflowTasks", indexService.getMyWorkflowTasks(workflowTasksNum, username, 0));
		Date today = DateUtil.getToday();
		int schTasksNum = schTasksHeight / itemHeight;
		data.put("scheduleTasks", indexService.getMyScheduleTasks(schTasksNum, username, today, DateUtil.getNext(today, 7)));
		data.put("leaderScheduleTasks", indexService.getLeaderScheduleTasks(schTasksNum, username, today, DateUtil.getNext(today, 7)));
		data.put("depScheduleTasks", indexService.getDepartmentScheduleTasks(schTasksNum, username, today, DateUtil.getNext(today, 7)));

		/*
		 * 准备“应用中心”功能菜单
		 */
		Tree tree = loginSession.makeFuncMenu(UrlAPI.ENTITY_GET_MAIN_UI);
		List<Node> menus = tree.getChildren();

		List<Node> funcMenus = new ArrayList();
		ISystemMenu menu;
		for (Node menu1 : menus) {
			for (Node menu2 : menu1.getChildren()) {
				menu = (ISystemMenu) menu2.getReferObj();
				if (menu.getType() != Const.MENU_TYPE_FOLDER) {
					funcMenus.add(menu2);
				}
				for (Node menu3 : menu2.getChildren()) {
					menu = (ISystemMenu) menu3.getReferObj();
					if (menu.getType() != Const.MENU_TYPE_FOLDER) {
						funcMenus.add(menu3);
					}
					for (Node menu4 : menu3.getChildren()) {
						menu = (ISystemMenu) menu4.getReferObj();
						if (menu.getType() != Const.MENU_TYPE_FOLDER) {
							funcMenus.add(menu4);
						}
					}
				}
			}
		}
		int count = 0, maxNumber = 9, rowNumber = 3;
		List gridMenus = new ArrayList();
		List rowMenus = new ArrayList();
		for (Node node : funcMenus) {

			if (count >= maxNumber) {
				break;
			}

			if (count % rowNumber == 0) {
				if (rowMenus.size() > 0) {
					gridMenus.add(rowMenus);
				}
				rowMenus = new ArrayList();
			}
			rowMenus.add(node);
			count++;
		}
		if (rowMenus.size() > 0) {
			gridMenus.add(rowMenus);
		}
		data.put("appMenus", gridMenus);

		return SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, resourcePath + "/admin_index", data).setAjax(isAjax);
	}

	@At(ADMIN_URL + "/index2/*")
	@Fail("redirect:" + ADMIN_URL + "/login")
	public UIModel index2(String menuID, boolean isAjax) {

		Cocit coc = Cocit.me();
		SecurityEngine securityEngine = coc.getSecurityEngine();
		securityEngine.checkLoginUserType(Const.USER_SYSTEM);

		ExtHttpContext context = (ExtHttpContext) Cocit.me().getHttpContext();
		LoginSession loginSession = context.getLoginSession();
		IMessageConfig messages = coc.getMessages();

		int bodyHeight = (Integer) loginSession.get(Const.CONFIG_KEY_UI_BODYHEIGHT);
		int bodyWidth = (Integer) loginSession.get(Const.CONFIG_KEY_UI_BODYWIDTH);
		int contentHeight = (Integer) loginSession.get(Const.CONFIG_KEY_UI_CONTENTHEIGHT);
		int contentWidth = (Integer) loginSession.get(Const.CONFIG_KEY_UI_CONTENTWIDTH);

		/*
		 * 准备“全局”变量
		 */
		Map data = new HashMap();
		data.put("adminUrl", adminUrl);
		data.put("resourcePath", resourcePath);
		data.put("closeAllTitle", messages.getMsg("10005"));

		/*
		 * 准备“一级页面”变量
		 */
		data.put("tabs1Height", bodyHeight + bodyTabsSpaceHeight);
		data.put("tabs1Width", bodyWidth + leftWidth);

		/*
		 * 准备“二级页面”变量
		 */
		data.put("leftWidth", leftWidth - 1);// 1:为左右间隔线条宽度
		data.put("leftHeight", bodyHeight);
		data.put("tabs2Height", bodyHeight);
		data.put("tabs2Width", bodyWidth);
		data.put("contentHeight", contentHeight);
		data.put("contentWidth", contentWidth);

		/*
		 * 准备“二级菜单”树数据
		 */
		Tree tree = loginSession.makeFuncMenu(UrlAPI.ENTITY_GET_MAIN_UI);
		List<Node> menus = tree.getChildren();
		if (!StringUtil.isBlank(menuID)) {
			for (Node node : menus) {
				if (node.getId().equals(menuID)) {
					menus = node.getChildren();

					data.put("menu1", node);

					break;
				}
			}
		}

		/*
		 * 准备“二级首页”数据： 功能菜单
		 */
		List<Node> funcMenus = new ArrayList();
		ISystemMenu menu;
		for (Node menu2 : menus) {
			menu = (ISystemMenu) menu2.getReferObj();
			if (menu.getType() != Const.MENU_TYPE_FOLDER) {
				funcMenus.add(menu2);
			}
			for (Node menu3 : menu2.getChildren()) {
				menu = (ISystemMenu) menu3.getReferObj();
				if (menu.getType() != Const.MENU_TYPE_FOLDER) {
					funcMenus.add(menu3);
				}
				for (Node menu4 : menu3.getChildren()) {
					menu = (ISystemMenu) menu4.getReferObj();
					if (menu.getType() != Const.MENU_TYPE_FOLDER) {
						funcMenus.add(menu4);
					}
				}
			}
		}

		int count = 0, maxNumber = 100, rowNumber = 8;
		List gridMenus = new ArrayList();
		List rowMenus = new ArrayList();
		for (Node node : funcMenus) {
			if (count % rowNumber == 0) {
				if (rowMenus.size() > 0) {
					gridMenus.add(rowMenus);
				}
				rowMenus = new ArrayList();
			}
			rowMenus.add(node);

			count++;
			if (count == maxNumber) {
				break;
			}
		}
		if (rowMenus.size() > 0) {
			gridMenus.add(rowMenus);
		}
		data.put("funcMenus", gridMenus);

		return SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, resourcePath + "/admin_index2", data).setAjax(isAjax);
	}
	//
	// @At(ADMIN_URL + "/left2/*")
	// public UIModel left2(String menuID) {
	// Cocit coc = Cocit.me();
	// HttpContext context = coc.getHttpContext();
	//
	// SecurityEngine securityEngine = coc.getSecurityEngine();
	// securityEngine.checkLoginUserType(Const.USER_SYSTEM);
	// LoginSession loginSession = context.getLoginSession();
	//
	// Map var = new HashMap();
	// var.put("adminUrl", adminUrl);
	// var.put("resourcePath", resourcePath);
	//
	// int uiHeight = (Integer) loginSession.get(Const.CONFIG_KEY_UI_CONTENTHEIGHT);
	// int uiWidth = (Integer) loginSession.get(Const.CONFIG_KEY_UI_CONTENTWIDTH);
	//
	// var.put("contentHeight", uiHeight);
	// var.put("contentWidth", uiWidth);
	// var.put("menuID", menuID);
	//
	// return SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, resourcePath + "/admin_left", var);
	// }
	//
	// @At(ADMIN_URL + "/getFuncMenus/*")
	// public UIModel getFuncMenus(String menuID) {
	// Cocit coc = Cocit.me();
	// HttpContext context = coc.getHttpContext();
	//
	// SecurityEngine securityEngine = coc.getSecurityEngine();
	// securityEngine.checkLoginUserType(Const.USER_SYSTEM);
	// LoginSession loginSession = context.getLoginSession();
	//
	// Map var = new HashMap();
	// var.put("adminUrl", adminUrl);
	// var.put("resourcePath", resourcePath);
	//
	// Tree tree = loginSession.makeFuncMenu(UrlAPI.ENTITY_GET_UI);
	// List<Node> menus = tree.getChildren();
	// if (!StringUtil.isBlank(menuID)) {
	// for (Node node : menus) {
	// if (node.getId().equals(menuID)) {
	// menus = node.getChildren();
	// break;
	// }
	// }
	// }
	//
	// List<Node> funcMenus = new ArrayList();
	// ISystemMenu menu;
	// for (Node menu2 : menus) {
	// menu = (ISystemMenu) menu2.getReferObj();
	// if (menu.getType() != Const.MENU_TYPE_FOLDER) {
	// funcMenus.add(menu2);
	// }
	// for (Node menu3 : menu2.getChildren()) {
	// menu = (ISystemMenu) menu3.getReferObj();
	// if (menu.getType() != Const.MENU_TYPE_FOLDER) {
	// funcMenus.add(menu3);
	// }
	// for (Node menu4 : menu3.getChildren()) {
	// menu = (ISystemMenu) menu4.getReferObj();
	// if (menu.getType() != Const.MENU_TYPE_FOLDER) {
	// funcMenus.add(menu4);
	// }
	// }
	// }
	// }
	//
	// int count = 0, maxNumber = 100, rowNumber = 8;
	// List gridMenus = new ArrayList();
	// List rowMenus = new ArrayList();
	// for (Node node : funcMenus) {
	// if (count % rowNumber == 0) {
	// if (rowMenus.size() > 0) {
	// gridMenus.add(rowMenus);
	// }
	// rowMenus = new ArrayList();
	// }
	// rowMenus.add(node);
	//
	// count++;
	// if (count == maxNumber) {
	// break;
	// }
	// }
	// if (rowMenus.size() > 0) {
	// gridMenus.add(rowMenus);
	// }
	// var.put("funcMenus", gridMenus);
	//
	// int uiHeight = (Integer) loginSession.get(Const.CONFIG_KEY_UI_CONTENTHEIGHT);
	// int uiWidth = (Integer) loginSession.get(Const.CONFIG_KEY_UI_CONTENTWIDTH);
	//
	// var.put("contentHeight", uiHeight);
	// var.put("contentWidth", uiWidth);
	// var.put("menuID", menuID);
	//
	// return SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, resourcePath + "/get_menus", var);
	// }
}
