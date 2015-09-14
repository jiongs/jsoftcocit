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

public class UIActionsButtonView extends BaseModelView<UIActions> {

	public String getName() {
		return ViewNames.VIEW_ACTIONS_BUTTON;
	}

	public void render(Writer out, UIActions model) throws Exception {
		printHtml(out, model);
	}

	private void printHtml(Writer out, UIActions model) throws Exception {
		int width = model.get("width", 0);
		width = width - 15;

		Tree tree = model.getData();

		/*
		 * 1.左边为工具栏菜单
		 */
		if (tree != null) {
			List<Node> nodes = tree.getChildren();
			if (!ObjectUtil.isNil(nodes)) {

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

				// toolbar
				write(out, "<div class=\"entityButtons\" style=\"padding: 0 20px 5px 0;\">");

				this.printButtons(out, model, nodes, token, resultUI);

				write(out, "</div>");
			}
		}
	}

	private void printButtons(Writer out, UIActions model, List<Node> nodes, String token, String resultUI) throws IOException {

		// sub menu
		for (Node node : nodes) {
			if (("" + StatusCodes.STATUS_CODE_DISABLED).equals(node.getStatusCode())) {
				continue;
			}

			// 子菜单
			if (node.size() > 0) {
				printButtons(out, model, node.getChildren(), token, resultUI);
			} else {
				String title = node.get("title", "");
				write(out, "<button title=\"%s\" onClick=\"jCocit.entity.doAction(this)\" data-options=\"", title == null ? "" : StringUtil.escapeHtml(title));
				write(out, "name: '%s'", node.getName());
				if (node.get("noHeader") != null) {
					write(out, ", noHeader: true");
				}
				write(out, ", token: '%s'", token);

				if (resultUI.length() > 0) {
					write(out, ", resultUI: [%s]", resultUI);// 菜单通过该令牌获取DataGrid对象
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
				Integer h = (Integer) node.get("windowHeight");
				if (h != null && h > 0)
					write(out, ", windowHeight: %s", h);
				Integer w = (Integer) node.get("windowWidth");
				if (w != null && w > 0)
					write(out, ", windowWidth: %s", w);

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

				write(out, "\">%s</button>", node.getName());
			}

		}
	}
}
