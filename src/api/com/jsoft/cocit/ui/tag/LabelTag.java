package com.jsoft.cocit.ui.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.jsoft.cocimpl.ui.tag.TagUtils;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UIForm;

public class LabelTag extends BodyTagSupport {

	private static final long serialVersionUID = -143365568431817938L;

	/**
	 * The name of the field (and associated property) being processed.
	 */
	protected String property = null;

	public int doStartTag() throws JspException {

		UIForm uiForm = (UIForm) pageContext.getAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);
		UIField field = null;
		if (uiForm != null) {
			field = uiForm.getField(this.property);
		}

		if (field != null) {
			if (FieldModes.isM(field.getMode())) {
				TagUtils.getInstance().write(this.pageContext, "<span class=\"icon-mode-M\">&nbsp;&nbsp;&nbsp;&nbsp;</span>");
			}
			TagUtils.getInstance().write(this.pageContext, field.getTitle());
		} else {
			TagUtils.getInstance().write(this.pageContext, property);
		}

		return (EVAL_BODY_INCLUDE);
	}

	public void release() {
		super.release();
		property = null;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
