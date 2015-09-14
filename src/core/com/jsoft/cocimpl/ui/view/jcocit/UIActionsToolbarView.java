package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.StatusCodes;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.control.UIActions;
import com.jsoft.cocit.ui.view.BaseModelView;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.WriteUtil;
import com.jsoft.cocit.util.Tree.Node;

public class UIActionsToolbarView extends BaseModelView<UIActions> {
	public String getName() {
		return ViewNames.VIEW_ACTIONS_TOOLBAR;
	}

	public void render(Writer out, UIActions model) throws Exception {

		printHtmlMenu(out, model);
	}

	private void printHtmlMenu(Writer out, UIActions model) throws Exception {
		Tree tree = model.getData();

		String token = Cocit.me().getHttpContext().getClientUIToken();

		List<String> list = model.getResultUI();
		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(",'" + s + "'");
		}
		String resultUI = "";
		if (sb.length() > 0) {
			resultUI = sb.substring(1);
		}

		/*
		 * 1.左边为工具栏菜单
		 */
		if (tree != null) {
			List<Node> nodes = tree.getChildren();
			if (!ObjectUtil.isNil(nodes)) {

				// toolbar
				write(out, "<div style=\"margin: 1px 0 1px 0; white-space: nowrap;\">");

				for (Node node : nodes) {
					if (("" + StatusCodes.STATUS_CODE_DISABLED).equals(node.getStatusCode())) {
						continue;
					}

					String title = node.get("title", "");
					write(out, "<a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-toolbar\" title=\"%s\" data-options=\"", title == null ? "" : StringUtil.escapeHtml(title));
					write(out, "name:'%s'", node.getName());
					if (node.get("noHeader") != null) {
						write(out, ", noHeader: true");
					}
					write(out, ", token:'%s'", token);

					if (resultUI.length() > 0) {
						write(out, ", resultUI: %s", resultUI);// 菜单通过该令牌获取DataGrid对象
					}

					String urlExpr = node.get("urlExpr", "");
					if (!StringUtil.isBlank(urlExpr)) {
						WriteUtil.write(out, ", urlExpr: '%s'", urlExpr);
					} else {
						String str = node.get("opUrl", "");
						if (!StringUtil.isBlank(str))
							write(out, ", opUrl: '%s'", str);
					}

					String str = node.get("opUrlTarget", "");
					if (!StringUtil.isBlank(str))
						write(out, ", opUrlTarget: '%s'", str);
					str = node.get("dialogStyle", "");
					if (!StringUtil.isBlank(str))
						write(out, ", dialogStyle: '%s'", str);

					String opMode = node.get("opMode", "");
					if (!StringUtil.isBlank(opMode))
						write(out, ", opMode: '%s'", opMode);

					str = node.get("opCode", "");
					if (!StringUtil.isBlank(str)) {
						write(out, ", opCode: %s", str);
						write(out, ", iconCls: 'icon-%s icon-%s-%s'", str, str, opMode);// iconCls 由菜单操作码决定
					}

					String msg = node.get("successMessage", "");
					if (!StringUtil.isBlank(msg))
						write(out, ", successMessage: '%s'", msg.replace("'", ""));
					msg = node.get("errorMessage", "");
					if (!StringUtil.isBlank(msg))
						write(out, ", errorMessage: '%s'", msg.replace("'", ""));
					msg = node.get("warnMessage", "");
					if (!StringUtil.isBlank(msg))
						write(out, ", warnMessage: '%s'", msg.replace("'", ""));

					// 子菜单
					if (node.size() > 0) {
						write(out, ", menu: '#submenu_%s_%s'", token, node.getId());
					} else {
						write(out, ", onClick: jCocit.entity.doAction");
					}

					write(out, "\">%s</a>", node.getName());
				}
				write(out, "</div>");

				// sub menu
				for (Node node : nodes) {
					if (node.size() > 0)
						printHtmlSubMenu(out, model, node, token, resultUI);
				}
			}
		}
	}

	private void printHtmlSubMenu(Writer out, UIActions model, Node node, String token, String resultUI) throws IOException {
		write(out, "<div id=\"submenu_%s_%s\" data-options=\"onClick: jCocit.entity.doAction\" style=\"width:120px;\">", //
		        token, node.getId());

		for (Node child : node.getChildren()) {
			if (("" + StatusCodes.STATUS_CODE_DISABLED).equals(node.getStatusCode())) {
				continue;
			}

			write(out, "<div data-options=\"", child.getId());
			write(out, "name:'%s'", node.getName());
			write(out, ", token: '%s'", token);

			if (resultUI.length() > 0) {
				write(out, ", resultUI: [%s]", resultUI);// 菜单通过该令牌获取DataGrid对象
			}

			String str = node.get("opUrl", "");
			if (!StringUtil.isBlank(str))
				write(out, ", opUrl: '%s'", str);

			str = node.get("opUrlTarget", "");
			if (!StringUtil.isBlank(str))
				write(out, ", opUrlTarget: '%s'", str);

			String opMode = node.get("opMode", "");
			if (!StringUtil.isBlank(opMode))
				write(out, ", opMode: '%s'", opMode);

			str = node.get("opCode", "");
			if (!StringUtil.isBlank(str)) {
				write(out, ", opCode: %s", str);
				write(out, ", iconCls: 'icon-%s icon-%s-%s'", str, str, opMode);// iconCls 由菜单操作码决定
			}

			String msg = node.get("successMessage", "");
			if (!StringUtil.isBlank(msg))
				write(out, ", successMessage: '%s'", msg.replace("'", ""));
			msg = node.get("errorMessage", "");
			if (!StringUtil.isBlank(msg))
				write(out, ", errorMessage: '%s'", msg.replace("'", ""));
			msg = node.get("warnMessage", "");
			if (!StringUtil.isBlank(msg))
				write(out, ", warnMessage: '%s'", msg.replace("'", ""));

			write(out, "\"><span>%s</span>", child.getName());

			// 输出子菜单
			if (child.size() > 0) {
				printHtmlSubMenu(out, model, child, token, resultUI);
			}

			write(out, "</div>");
		}

		write(out, "</div>");
	}
}
