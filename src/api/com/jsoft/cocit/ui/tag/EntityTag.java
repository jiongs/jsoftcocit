package com.jsoft.cocit.ui.tag;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.ui.UIView;
import com.jsoft.cocit.ui.UIViews;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.ui.model.control.UIEntity;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.StringUtil;

public class EntityTag extends BodyTagSupport {

	private static final long serialVersionUID = 5808247656189119577L;

	protected String modelName;
	protected String viewName;
	protected String funcExpr = null;
	protected boolean usedToSubEntity = false;

	public int doStartTag() throws JspException {

		UIEntity mainModel = null;

		if (StringUtil.isBlank(modelName)) {
			modelName = ViewKeys.UI_MODEL_KEY;
		}
		UIModel uiModel = (UIModel) pageContext.getAttribute(modelName, PageContext.REQUEST_SCOPE);
		if (uiModel != null && uiModel instanceof UIEntity) {
			mainModel = (UIEntity) uiModel;
		}

		Writer out = null;
		try {
			out = pageContext.getOut();

			if (StringUtil.hasContent(funcExpr)) {

				HttpContext httpContext = Cocit.me().getHttpContext();
				if (httpContext == null) {
					Cocit.me().makeHttpContext((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
				}

				OpContext opContext = OpContext.make(funcExpr, null, null);
				mainModel = opContext.getUiModelFactory().getMain(opContext.getSystemMenu(), opContext.getCocEntity(), usedToSubEntity);
				if (opContext.getException() != null) {
					throw new JspException(opContext.getException());
				}
			}

			if (mainModel == null) {
				throw new CocException("标记库(coc:entity)用法错误！请参见相关文档。");
			}

			if (StringUtil.isBlank(viewName)) {
				viewName = ViewNames.VIEW_MAIN;
			}
			UIViews views = Cocit.me().getViews();
			UIView view = views.getView(viewName);
			if (view == null) {
				throw new CocException("UI视图不存在！viewName = %s", viewName);
			}

			view.render(out, mainModel);

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

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
