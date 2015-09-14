package com.jsoft.cocimpl.ui.view.gridcell;

import java.io.Writer;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.WriteUtil;

public class LinkPiDiagramCellView implements UICellView {

	@Override
	public String getName() {
		return ViewNames.CELL_VIEW_LINK_PIDIAGRAM;
	}

	@Override
	public void render(Writer out, UIFieldModel col, Object dataObject, String fieldName, Object fieldValue) throws Exception {
		String pid = ObjectUtil.getStringValue(dataObject, "activitiExecutionId");
		String strValue = StringUtil.escapeHtml(col.format(fieldValue));

		WriteUtil.write(out, "<a href=\"javascript: void(0);\" pid=\"%s\" data-noselect=\"true\" class=\"jCocit-ui jCocit-pidiagram\" data-options=\"", pid);
		WriteUtil.write(out, "name: '%s'", strValue);
		out.write("\">");
		out.write(strValue);
		out.write("</a>");
	}

}
