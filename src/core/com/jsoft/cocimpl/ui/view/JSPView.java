package com.jsoft.cocimpl.ui.view;

import java.io.Writer;

import org.nutz.mvc.view.JspView;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.UIView;
import com.jsoft.cocit.ui.model.JSPModel;

public class JSPView implements UIView<JSPModel> {

	@Override
	public String getName() {
		return ViewNames.VIEW_JSP;
	}

	public void render(Writer out, JSPModel model) throws Exception {
		new JspView(model.getJsp()).render(model.getRequest(), model.getResponse(), model.getContext());
	}

}
