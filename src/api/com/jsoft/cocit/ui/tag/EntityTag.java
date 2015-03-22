package com.jsoft.cocit.ui.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.ui.model.UIEntityModel;
import com.jsoft.cocit.util.StringUtil;

public class EntityTag extends BodyTagSupport {

	private static final long serialVersionUID = 5808247656189119577L;

	protected String funcExpr = null;
	protected boolean usedToSubEntity = false;

	public int doStartTag() throws JspException {

		UIEntityModel uiModel = (UIEntityModel) pageContext.getAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);

		if (uiModel == null && StringUtil.hasContent(funcExpr)) {

			OpContext opContext = OpContext.make(funcExpr, null, null);
			if (opContext.getException() != null) {
				throw new JspException(opContext.getException());
			}

			uiModel = opContext.getUiModelFactory().getMain(opContext.getSystemMenu(), opContext.getCocEntity(), usedToSubEntity);
		}

		return EVAL_BODY_BUFFERED;
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
}
