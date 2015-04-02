package com.jsoft.cocit.ui.tag;

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
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.StringUtil;

public class FormActionsTag extends BodyTagSupport {

	private static final long serialVersionUID = 3902335085986034089L;

	protected String modelName = null;
	protected String funcExpr = null;

	public int doStartTag() throws JspException {

		UIForm uiForm = null;
		if (StringUtil.isBlank(modelName)) {
			modelName = ViewKeys.UI_MODEL_KEY;
		}
		UIModel baseModel = (UIModel) pageContext.getAttribute(modelName, PageContext.REQUEST_SCOPE);
		if (baseModel != null && baseModel instanceof UIForm) {
			uiForm = (UIForm) baseModel;
		}

		Writer out = null;
		try {
			out = pageContext.getOut();

			UIForm formActions = uiForm;

			if (formActions == null && StringUtil.hasContent(funcExpr)) {

				HttpContext httpContext = Cocit.me().getHttpContext();
				if (httpContext == null) {
					Cocit.me().makeHttpContext((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
				}

				OpContext opContext = OpContext.make(funcExpr, null, null);
				formActions = opContext.getUiModelFactory().getForm(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getCocAction(), null);
				if (opContext.getException() != null) {
					throw new JspException(opContext.getException());
				}
			}

			if (formActions == null)
				throw new CocException("标记库(coc:fields)用法错误！请参见相关文档。");

			UIViews views = Cocit.me().getViews();
			UIView view = views.getView(ViewNames.VIEW_FORM_BUTTONS);
			view.render(out, formActions);

			return EVAL_BODY_INCLUDE;

		} catch (Throwable e) {
			try {
				out.write(ExceptionUtil.msg(e));
			} catch (Exception ex) {
				throw new JspException(e);
			}

			return SKIP_BODY;
		}
	}

	public void release() {
		super.release();
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getFuncExpr() {
		return funcExpr;
	}

	public void setFuncExpr(String funcExpr) {
		this.funcExpr = funcExpr;
	}
}
