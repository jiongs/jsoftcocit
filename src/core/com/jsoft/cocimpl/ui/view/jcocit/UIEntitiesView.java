package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.Writer;
import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.control.UIEntities;
import com.jsoft.cocit.ui.model.control.UIEntity;
import com.jsoft.cocit.ui.model.control.UIPanel;
import com.jsoft.cocit.ui.view.BaseModelView;
import com.jsoft.cocit.util.StringUtil;

public class UIEntitiesView extends BaseModelView<UIEntities> {
	public String getName() {
		return ViewNames.VIEW_MAINS;
	}

	public void render(Writer out, UIEntities model) throws Exception {

		write(out, "<div>");

		/**
		 * 准备参数:
		 * <UL>
		 * <LI>token: 操作令牌，通过该令牌将“NaviTree,Toolbar,DataGrid,ChildrenTabs,SearchBox”等串联在一起。
		 * <LI>width: 模块界面宽度
		 * <LI>height: 模块界面高度
		 * <LI>mainTabsHeight: 业务主表Tabs高度
		 * <LI>childrenTabsHeight: 业务子表Tabs高度
		 * </UL>
		 */
		HttpContext ctx = Cocit.me().getHttpContext();
		String token = ctx.getClientUIToken();
		int width = model.get("width", ctx.getClientUIWidth());
		int height = model.get("height", ctx.getClientUIHeight());
		int mainTabsHeight = height;
		int childrenTabsHeight = height;
		int tabsSpaceHeight = 70;
		int tabsSpaceWidth = 25;
		int gridWidth = width - tabsSpaceWidth;

		List<UIPanel> panels = model.getPanels();
		int panelSize = panels.size();

		UIPanel panel = panels.get(0);
		UIEntity mainEntity = (UIEntity) panel.getPanelContent();

		if (panelSize > 1) {
			if (height < 1000)
				height = 1000;
			mainTabsHeight = height / 2 - 5;
			childrenTabsHeight = height - mainTabsHeight - 5;
		}
		int mainGridHeight = mainTabsHeight - tabsSpaceHeight;
		int childGridHeight = childrenTabsHeight - tabsSpaceHeight;

		// 模块界面 TABLE
		// print(out, "<table><tr><td>");

		/*
		 * 业务主表 Tabs
		 */
		if (mainEntity != null) {

			mainEntity.set("width", "" + gridWidth);
			mainEntity.set("height", "" + mainGridHeight);

			// 业务主表 Tabs DIV
			write(out, "<div class=\"jCocit-ui jCocit-tabs\" data-options=\"tabPosition:'top'\" style=\"width:%spx; height:%spx;\">"//
			        , width, mainTabsHeight);

			// 业务主表 Tab DIV
			if (StringUtil.isBlank(panel.getPanelUrl())) {
				write(out, "<div title=\"%s\" class=\"jCocit-gridtab\" data-options=\"closable: false\" style=\"padding:5px; overflow:hidden;\">"//
				        , panel.getTitle());
				mainEntity.render(out);
				write(out, "</div>");// end: 业务主表 Tab DIV
			} else {
				write(out, "<div title=\"%s\" class=\"jCocit-gridtab\" data-options=\"token:'%s', url: '%s?_uiToken=%s&_uiHeight=%s&_uiWidth=%s',closable: false, cache: true\" style=\"padding:5px\"></div>"//
				        , panel.getTitle(), token, panel.getPanelUrl(), token, mainGridHeight, gridWidth);
			}

			write(out, "</div>");// end: 业务主表 Tabs DIV

		}

		/**
		 * 业务子表 Tabs
		 */
		if (panelSize > 1) {
			// 业务子表 TR
			// print(out, "</td></tr><tr><td>");

			// 业务主表与业务子表之间的间隙
			write(out, "<div style=\"height: 5px;\"></div>");

			// 业务子表 Tabs DIV
			write(out, "<div id=\"childrentabs_%s\" class=\"jCocit-ui jCocit-tabs\" data-options=\"onSelect: jCocit.entity.doSelectTabs, tabPosition:'top'\" style=\"width:%spx; height:%spx; \">"//
			        , token, width, childrenTabsHeight);

			// fkfield: 表示业务子表将通过哪个外键字段关联到业务主表？
			String fkField;
			String fkTargetField;
			for (int i = 1; i < panelSize; i++) {
				panel = panels.get(i);

				String panelUrl = panel.getPanelUrl();

				fkField = panel.get("fkField", "");
				fkTargetField = panel.get("fkTargetField", "");

				write(out,
				        "<div title=\"%s\" class=\"jCocit-gridtab\" data-options=\"token:'%s', url: '%s?_uiToken=%s&_uiHeight=%s&_uiWidth=%s&_paramUI=%s&_resultUI=%s&_fkField=%s&_fkTargetField=%s',closable: false, cache: true\" style=\"padding:5px\"></div>",//
				        panel.getTitle(),//
				        token, //
				        panelUrl, //
				        token, //
				        childGridHeight, //
				        gridWidth,//
				        StringUtil.join(panel.getParamUI()), StringUtil.join(panel.getResultUI()),//
				        fkField, //
				        fkTargetField//
				);
			}

			write(out, "</div>");// end: 业务子表 Tabs DIV

			// 子模块工具菜单——位于主模块TABS右边
			// print(out, "<div id=\"tabtools_%s\">", model.getId());
			// for (EntityTableWidgetModel child : children) {
			// print(out, "<a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-button\" data-options=\"plain: false");
			// // print(out, ", iconCls:'icon-add'");
			// print(out, "\">%s</a>", child.getName());
			// }
			// print(out, "</div>");

		}

		// print(out, "</td></tr></table>");//end: 模块界面 TABLE

		write(out, "</div>");

	}
}
