package com.jsoft.cocimpl.ui.view.gridcell;

import java.io.Writer;
import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.WriteUtil;

public class LinkToFormCellView implements UICellView {

	@Override
	public String getName() {
		return ViewNames.CELL_VIEW_LINK_TO_FORM;
	}

	@Override
	public void render(Writer out, UIFieldModel col, Object dataObject, String fieldName, Object fieldValue) throws Exception {
		List<String> list = col.getResultUI();
		StringBuffer sb = new StringBuffer();
		for (String s : list) {
			sb.append(",'" + s + "'");
		}
		String resultUI = "";
		if (sb.length() > 0) {
			resultUI = sb.substring(1);
		}

		String linkUrl = col.getLinkUrl();
		if (StringUtil.hasContent(linkUrl)) {
			linkUrl = MVCUtil.makeUrl(MVCUtil.parseUrlExpr(linkUrl, dataObject, null));
		} else {
			linkUrl = "";
		}
		String linkTarget = col.getLinkTarget();
		if (linkTarget == null) {
			linkTarget = "";
		}
		String strValue = StringUtil.escapeHtml(col.format(fieldValue));
		String token = Cocit.me().getHttpContext().getClientUIToken();

		out.write("<a href=\"javascript: void(0);\" onclick=\"jCocit.entity.doRowAction(event, this)\" data-options=\"");
		WriteUtil.write(out, "name: '%s'", "查看");
		WriteUtil.write(out, ", token: '%s'", token);
		WriteUtil.write(out, ", opUrl: '%s'", linkUrl);
		WriteUtil.write(out, ", opUrlTarget: '%s'", linkTarget);
		if (resultUI.length() > 0) {
			WriteUtil.write(out, ", resultUI: [%s]", resultUI);
		}

		out.write("\">");
		out.write(strValue);
		out.write("</a>");
	}

}
