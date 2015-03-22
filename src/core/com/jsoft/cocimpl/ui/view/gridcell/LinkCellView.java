package com.jsoft.cocimpl.ui.view.gridcell;

import java.io.Writer;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;

public class LinkCellView implements UICellView {

	@Override
	public String getName() {
		return ViewNames.CELL_VIEW_LINK;
	}

	@Override
	public void render(Writer out, UIFieldModel col, Object dataObject, String fieldName, Object fieldValue) throws Exception {

		String strValue = StringUtil.escapeHtml(col.format(fieldValue));

		String linkTarget = col.getLinkTarget();
		if (linkTarget == null) {
			linkTarget = "";
		}

		String linkUrl = col.getLinkUrl();
		if (StringUtil.hasContent(linkUrl)) {
			linkUrl = MVCUtil.parseUrlExpr(linkUrl, dataObject, null);
		}
		if (StringUtil.isBlank(linkUrl)) {
			out.write(strValue);
		} else {
			linkUrl = MVCUtil.makeUrl(linkUrl);

			out.write("<a title=\"" + strValue + "\" href=\"" + linkUrl + "\" target=\"" + linkTarget + "\" >");
			out.write(strValue);
			out.write("</a>");
		}
	}

}
