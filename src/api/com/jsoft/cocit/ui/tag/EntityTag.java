package com.jsoft.cocit.ui.tag;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.jsoft.cocimpl.ui.UIView;
import com.jsoft.cocimpl.ui.UIViews;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.StringUtil;

public class EntityTag extends BodyTagSupport {

	private static final long serialVersionUID = 5808247656189119577L;

	protected String viewName;
	protected String funcExpr = null;
	protected boolean usedToSubEntity = false;

	public int doStartTag() throws JspException {

		UIModel uiModel = (UIModel) pageContext.getAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);

		Writer out = null;
		try {
			out = pageContext.getOut();

			if (uiModel == null && StringUtil.hasContent(funcExpr)) {

				HttpContext httpContext = Cocit.me().getHttpContext();
				if (httpContext == null) {
					Cocit.me().makeHttpContext((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
				}

				OpContext opContext = OpContext.make(funcExpr, null, null);
				uiModel = opContext.getUiModelFactory().getMain(opContext.getSystemMenu(), opContext.getCocEntity(), usedToSubEntity);
				if (opContext.getException() != null) {
					throw new JspException(opContext.getException());
				}
			}

			if (StringUtil.isBlank(viewName)) {
				viewName = ViewNames.VIEW_MAIN;
			}
			UIViews views = Cocit.me().getViews();
			UIView view = views.getView(viewName);
			if (view == null) {
				throw new CocException("UI视图不存在！viewName = %s", viewName);
			}

			view.render(out, uiModel);

			return EVAL_BODY_INCLUDE;

		} catch (Throwable e) {
			try {
				out.write(ExceptionUtil.msg(e));
			} catch (IOException e1) {
			}

			return SKIP_BODY;

		}

	}

	public void release() {
		super.release();
		funcExpr = null;
	}

	public String getFuncExpr() {
		return funcExpr;
	}

	public void setFuncExpr(String funcExpr) {
		this.funcExpr = funcExpr;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
}
