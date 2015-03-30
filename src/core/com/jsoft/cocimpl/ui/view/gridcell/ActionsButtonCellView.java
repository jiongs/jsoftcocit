package com.jsoft.cocimpl.ui.view.gridcell;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.ui.model.UIActionsModel;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;
import com.jsoft.cocit.util.WriteUtil;

public class ActionsButtonCellView implements UICellView {

	public String getName() {
		return ViewNames.CELL_VIEW_BUTTON_FOR_ROWACTIONS;
	}

	@Override
	public void render(Writer out, UIFieldModel fieldModel, Object dataObject, String fieldName, Object fieldValue) throws Exception {
		this.printHtml(out, dataObject, (UIActionsModel) fieldValue);
	}

	private void printHtml(Writer out, Object dataObject, UIActionsModel model) throws Exception {
		int width = model.get("width", 0);
		width = width - 15;

		Tree tree = model.getData();

		if (tree != null) {
			List<Node> nodes = tree.getChildren();
			if (!ObjectUtil.isNil(nodes)) {
				this.printButtons(out, model, dataObject, nodes);
			}
		}
	}

	private void printButtons(Writer out, UIActionsModel model, Object dataObject, List<Node> nodes) throws IOException {
		List<String> list = model.getResultUI();
		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(",'" + s + "'");
		}
		String resultUI = "";
		if (sb.length() > 0) {
			resultUI = sb.substring(1);
		}

		// sub menu
		for (Node node : nodes) {

			CocActionService action = (CocActionService) node.getReferObj();
			if (!ExprUtil.match(dataObject, action.getWhere())) {
				continue;
			}

			// 子菜单
			if (node.size() > 0) {
				printButtons(out, model, dataObject, node.getChildren());
			} else {
				Long dataID = (Long) ObjectUtil.getId(dataObject);
				String title = node.get("title", "");
				WriteUtil.write(out, "<button title=\"%s\" onclick=\"jCocit.entity.doRowAction(event, this)\" data-options=\"", title == null ? "" : StringUtil.escapeHtml(title));
				WriteUtil.write(out, "name:'%s'", node.getName());

				String token = Cocit.me().getHttpContext().getClientUIToken();
				WriteUtil.write(out, ", token: '%s'", token);

				if (resultUI.length() > 0) {
					WriteUtil.write(out, ", resultUI: [%s]", resultUI);
				}

				String opMode = node.get("opMode", "");
				if (!StringUtil.isBlank(opMode))
					WriteUtil.write(out, ", opMode: '%s'", opMode);

				String str = node.get("opCode", "");
				if (!StringUtil.isBlank(str)) {
					WriteUtil.write(out, ", opCode: %s", str);
				}

				String urlExpr = node.get("urlExpr", "");
				if (!StringUtil.isBlank(urlExpr)) {
					String url = MVCUtil.parseUrlExpr(urlExpr, dataObject, null);
					WriteUtil.write(out, ", urlExpr: '%s'", url);
				} else {
					str = node.get("opUrl", "");
					if (!StringUtil.isBlank(str))
						WriteUtil.write(out, ", opUrl: '%s/%s'", str, dataID);
				}

				str = node.get("opUrlTarget", "");
				if (!StringUtil.isBlank(str))
					WriteUtil.write(out, ", opUrlTarget: '%s'", str);

				String msg = node.get("successMessage", "");
				if (!StringUtil.isBlank(msg))
					WriteUtil.write(out, ", successMessage: '%s'", msg.replace("'", ""));
				msg = node.get("errorMessage", "");
				if (!StringUtil.isBlank(msg))
					WriteUtil.write(out, ", errorMessage: '%s'", msg.replace("'", ""));
				msg = node.get("warnMessage", "");
				if (!StringUtil.isBlank(msg))
					WriteUtil.write(out, ", warnMessage: '%s'", msg.replace("'", ""));

				WriteUtil.write(out, "\">%s</button>", node.getName());
			}

		}
	}

}
