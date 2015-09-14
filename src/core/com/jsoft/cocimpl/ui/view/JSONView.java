package com.jsoft.cocimpl.ui.view;

import java.io.Writer;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.UIView;
import com.jsoft.cocit.ui.model.datamodel.JSONModel;

public class JSONView implements UIView<JSONModel> {

	@Override
	public String getName() {
		return ViewNames.VIEW_JSON;
	}

	public void render(Writer out, JSONModel model) throws Exception {
		out.write(model.getContent());
	}

}
