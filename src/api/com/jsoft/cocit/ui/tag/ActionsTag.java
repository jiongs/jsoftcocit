package com.jsoft.cocit.ui.tag;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.ui.model.UIActionsModel;
import com.jsoft.cocit.ui.model.UIEntityModel;
import com.jsoft.cocit.util.StringUtil;

public class ActionsTag extends BodyTagSupport {

	private static final long serialVersionUID = 3902335085986034089L;

	protected String funcExpr = null;
	protected String resultUI = null;

	public int doStartTag() throws JspException {

		UIActionsModel actionsModel = null;

		UIEntityModel uiModel = (UIEntityModel) pageContext.getAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);

		if (uiModel == null && StringUtil.hasContent(funcExpr)) {

			OpContext opContext = OpContext.make(funcExpr, null, null);
			if (opContext.getException() != null) {
				throw new JspException(opContext.getException());
			}

			actionsModel = opContext.getUiModelFactory().getActions(opContext.getSystemMenu(), opContext.getCocEntity());
		} else {
			actionsModel = uiModel.getActions();
		}

		if (actionsModel != null) {
			try {
				List<String> list = StringUtil.toList(resultUI);
				for (String str : list) {
					actionsModel.getResultUI().clear();
					actionsModel.addResultUI(str);
				}
				actionsModel.render(pageContext.getOut());
			} catch (Exception e) {
				throw new JspException(e);
			}
		}

		return EVAL_BODY_BUFFERED;
	}

	public void release() {
		super.release();
		funcExpr = null;
		resultUI = null;
	}

	public String getFuncExpr() {
		return funcExpr;
	}

	public void setFuncExpr(String funcExpr) {
		this.funcExpr = funcExpr;
	}

	public String getResultUI() {
		return resultUI;
	}

	public void setResultUI(String resultUI) {
		this.resultUI = resultUI;
	}
}
