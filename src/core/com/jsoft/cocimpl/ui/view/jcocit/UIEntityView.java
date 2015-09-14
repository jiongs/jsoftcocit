package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.Writer;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.securityengine.ILoginSession;
import com.jsoft.cocit.ui.model.control.UIActions;
import com.jsoft.cocit.ui.model.control.UIEntity;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.model.control.UISearchBox;
import com.jsoft.cocit.ui.model.control.UITree;
import com.jsoft.cocit.ui.view.BaseModelView;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.UIPositionUtil;

public class UIEntityView extends BaseModelView<UIEntity> {
	public String getName() {
		return ViewNames.VIEW_MAIN;
	}

	public void render(Writer out, UIEntity model) throws Exception {

		HttpContext ctx = Cocit.me().getHttpContext();
		ILoginSession login = ctx.getLoginSession();
		if (login == null) {
			throw new CocSecurityException("您尚未登录或登录已过期，请先登录！");
		}

		/*
		 * 窗口尺寸
		 */
		int contentPadding = (Integer) login.get(Const.CONFIG_KEY_UI_CONTENTPADDING);// 内容区域内部间距
		int contentWidth = (Integer) model.get("width", ctx.getClientUIWidth());
		int contentHeight = (Integer) model.get("height", ctx.getClientUIHeight());

		int actionsHeight = 0;
		int searchFormHeight = 0;
		boolean hasTop1 = false, hasTop2 = false;

		/*
		 * 操作按钮
		 */
		UIActions ations = model.getActions();
		int actionPos = model.getActionsPos();
		boolean actionIsTop1 = false;
		boolean actionIsTop2 = false;
		if (ations != null) {
			actionIsTop1 = UIPositionUtil.isTop1(actionPos);
			actionIsTop2 = UIPositionUtil.isTop2(actionPos, 0, 1);
			if (actionIsTop1 || actionIsTop2) {
				actionsHeight = 40;
			}

			hasTop1 = actionIsTop1;
			hasTop2 = actionIsTop2;
		}

		/*
		 * 组合查询表单
		 */
		UISearchBox searchBox = model.getSearchBox();
		int searchPos = model.getSearchBoxPos();
		boolean searchIsTop1 = false;
		boolean searchIsTop2 = false;
		if (searchBox != null) {
			searchIsTop1 = UIPositionUtil.isTop1(searchPos, 0, 1);
			searchIsTop2 = UIPositionUtil.isTop2(searchPos);

			if (searchIsTop1 && !hasTop1) {
				searchFormHeight = 45;
			} else if (searchIsTop2 && !hasTop2) {
				searchFormHeight = 40;
			}

			hasTop1 = hasTop1 || searchIsTop1;
			hasTop2 = hasTop2 || searchIsTop2;
		}

		/*
		 * 计算GIRD宽度和高度
		 */
		int treeWidth = 250;
		int gridWidth = contentWidth;
		int treeHeight = model.get("treeHeight", contentHeight - actionsHeight - searchFormHeight);
		int gridHeight = model.get("gridHeight", treeHeight);

		String token = ctx.getClientUIToken();
		UITree filterTree = model.getFilter();
		byte filterPosition = 0;
		if (filterTree != null) {
			filterPosition = (Byte) filterTree.get("filterPosition");
			switch (filterPosition) {
				case 0:// auto
				case 3:// left of grid
					treeWidth = model.get("treeWidth", treeWidth);
					gridWidth = gridWidth - treeWidth - 15;
					break;
				case 4:// right of search box
					break;
			}
		}

		/*
		 * 主界面容器
		 */
		write(out, "<div style=\"padding: %spx; position: relative\">", contentPadding);

		/*
		 * 是否需要表单？
		 */
		if (model.isForm()) {
			write(out, "<form onsubmit=\"return false;\">");
		}

		/*
		 * 顶部：查询表单 位于操作按钮 顶部
		 */
		if (searchBox != null && (searchPos == 0 || searchPos == 1 || searchIsTop1)//
		) {
			write(out, "<div id=\"searchform_%s\" style=\"padding: 0 5px 15px 5px; width: %spx; height: %spx; \">",//
			        token, //
			        contentWidth - 32,//
			        searchFormHeight - 15//
			);
			searchBox.put("width", contentWidth);
			searchBox.render(out);
			write(out, "</div>");
		}

		/*
		 * 用一个Table将工具栏容器分成两部分：1.左边为工具栏菜单，2.右边为搜索框。
		 */
		write(out, "<div id=\"toolbar_%s\" class=\"entityToolbar\" style=\"padding: 0 5px 5px 5px; width: %spx; height: %spx; \">",//
		        token, //
		        contentWidth - 32,//
		        actionsHeight - 5//
		);
		write(out, "<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td class=\"entityActions\" valign=\"top\" style=\"white-space: nowrap;\">");

		/*
		 * 顶部：左边操作按钮
		 */
		if (ations != null) {
			ations.set("width", contentWidth);
			ations.render(out);
		}

		/*
		 * 顶部 快速过滤
		 */
		if (filterTree != null && filterPosition == 1) {// top:
			write(out, "</td><td align=\"left\" class=\"entityFilter\" valign=\"top\" style=\"white-space: nowrap;\">");
			write(out, "<div style=\"padding: 0 5px 4px 5px; \">",//
			        token //
			);
			write(out,
			        "%s: <input id=\"%s\" class=\"jCocit-ui jCocit-combotree\" data-options=\"token:'%s', resultUI: %s, height: 27, width: 200, panelWidth: 220, dataURL:'%s', multiple: true, checkOnSelect: true, selectOnCheck: true, onCheck: jCocit.entity.doSelectFilter\">", //
			        filterTree.getTitle(),//
			        filterTree.getId(),//
			        token,//
			        StringUtil.toJSArray(filterTree.getResultUI()),//
			        filterTree.getDataLoadUrl());
			write(out, "</div>");
		}

		if (searchBox != null && (searchPos == 4 || searchIsTop2)//
		) {
			write(out, "</td><td align=\"left\" width=\"2\" class=\"entitySearch\" valign=\"top\" style=\"white-space: nowrap;\">");
			write(out, "<div id=\"searchform_%s\" style=\"padding: 0 5px 5px 5px; \">",//
			        token //
			);
			searchBox.set("width", contentWidth);
			searchBox.render(out);
			write(out, "</div>");
		}

		write(out, "</td></tr></table>");
		write(out, "</div>");

		/**
		 * 用一个Table将数据表界面分成两部分：1.左边为导航树，2.右边为DataGrid。
		 * <UL>
		 * <LI>token: 业务令牌，工具栏操作菜单通过业务令牌与该table相关联；
		 * </UL>
		 */
		write(out, "<table class=\"entityTable\" token=\"%s\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr>", token);

		/*
		 * 1.左边为导航树
		 */
		if (filterTree != null) {
			switch (filterPosition) {
				case 0:// auto
				case 3:// left of grid
					filterTree.set("height", treeHeight);
					filterTree.set("width", treeWidth);
					write(out, "<td valign=\"top\" width=\"%spx\" style=\"padding-right: 5px;\">", treeWidth);

					/*
					 * 输出检索框
					 */
					if (searchBox != null //
					        && model.getSearchBoxPos() == 3// left
					) {
						searchBox.set("width", treeWidth + 6);
						searchBox.render(out);
						write(out, "<div style=\"height: 1px;\"></div>");
					}

					filterTree.render(out);

					write(out, "</td>");

					break;
				case 4:// right of search box
			}
		}

		/*
		 * 2.右边为DataGrid
		 */
		UIGrid gridWidget = model.getGrid();
		if (gridWidget != null) {
			gridWidget.set("width", gridWidth);
			gridWidget.set("height", gridHeight);
			if (searchBox != null //
			        && model.getActionsPos() == 5// gridtop
			) {
				gridWidget.set("toolbarID", "toolbar" + token);
			}

			write(out, "<td valign=\"top\" width=\"%s\" style=\"\">", gridWidth);

			gridWidget.render(out);

			write(out, "</td>");
		}

		//
		write(out, "</tr></table>");

		/*
		 * 是否需要表单？
		 */
		if (model.isForm()) {
			write(out, "</form>");
		}

		write(out, "</div>");// 主界面容器

	}
}
