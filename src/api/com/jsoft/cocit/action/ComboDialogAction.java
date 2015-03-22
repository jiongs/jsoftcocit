package com.jsoft.cocit.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.ui.model.JSPModel;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class ComboDialogAction {

	@At("/cocentity/utils/userComboDialog")
	public JSPModel selectUser() {
		Cocit coc = Cocit.me();
		HttpContext ctx = coc.getHttpContext();
		HttpServletRequest req = ctx.getRequest();
		HttpServletResponse res = ctx.getResponse();

		return JSPModel.make(req, res, "/WEB-INF/jsp/utils/userComboDialog.jsp").setAjax(true);
	}
}
