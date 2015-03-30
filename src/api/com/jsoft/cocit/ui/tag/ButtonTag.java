package com.jsoft.cocit.ui.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.jsoft.cocimpl.ui.tag.TagUtils;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.util.StringUtil;

public class ButtonTag extends BodyTagSupport {

	private static final long serialVersionUID = 3902335085986034089L;

	protected String dataOptions = null;
	// ----------------------------------------------------------
	// CSS Style Support
	// ----------------------------------------------------------

	private String style = null;
	private String styleClass = null;
	private String title = null;
	private boolean disabled = false;

	// ----------------------------------------------------------
	// Mouse Events
	// ----------------------------------------------------------

	/**
	 * Mouse click event.
	 */
	private String onclick = null;

	/**
	 * Mouse double click event.
	 */
	private String ondblclick = null;

	/**
	 * Mouse over component event.
	 */
	private String onmouseover = null;

	/**
	 * Mouse exit component event.
	 */
	private String onmouseout = null;

	/**
	 * Mouse moved over component event.
	 */
	private String onmousemove = null;

	/**
	 * Mouse pressed on component event.
	 */
	private String onmousedown = null;

	/**
	 * Mouse released on component event.
	 */
	private String onmouseup = null;

	// ----------------------------------------------------------
	// Keyboard Events
	// ----------------------------------------------------------

	/**
	 * Key down in component event.
	 */
	private String onkeydown = null;

	/**
	 * Key released in component event.
	 */
	private String onkeyup = null;

	/**
	 * Key down and up together in component event.
	 */
	private String onkeypress = null;

	private String type;

	public int doStartTag() throws JspException {
		String buttonStyle = Cocit.me().getConfig().getViewConfig().getButtonStyle();
		if (StringUtil.isBlank(type)) {
			if ("button".equals(buttonStyle)) {
				type = "button";
				if (StringUtil.hasContent(styleClass)) {
					styleClass = "coc-btn " + styleClass;
				} else {
					styleClass = "coc-btn";
				}
			} else {
				type = "a";
				if (StringUtil.hasContent(styleClass)) {
					styleClass = "jCocit-ui jCocit-button " + styleClass;
				} else {
					styleClass = "jCocit-ui jCocit-button";
				}
			}
		}

		TagUtils.getInstance().write(this.pageContext, this.renderInputElement());

		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		TagUtils.getInstance().write(this.pageContext, "</" + type + ">");

		this.release();

		return (EVAL_PAGE);
	}

	public void release() {
		super.release();
		this.dataOptions = null;
		this.disabled = false;
		this.onclick = null;
		this.onkeydown = null;
		this.onkeypress = null;
		this.ondblclick = null;
		this.onkeyup = null;
		this.onmousedown = null;
		this.onmousemove = null;
		this.onmouseout = null;
		this.onmouseover = null;
		this.onmouseup = null;
		this.style = null;
		this.styleClass = null;
		this.title = null;
		this.type = null;
	}

	private String renderInputElement() throws JspException {
		StringBuffer sb = new StringBuffer();

		sb.append("<").append(type);
		if ("a".equalsIgnoreCase(type)) {
			sb.append(" href=\"javascript: void(0)\"");
		}

		prepareAttribute(sb, "id", id);
		prepareAttribute(sb, "style", style);
		prepareAttribute(sb, "class", styleClass);
		prepareAttribute(sb, "title", title);
		prepareAttribute(sb, "data-options", dataOptions);
		prepareAttribute(sb, "onclick", onclick);
		prepareAttribute(sb, "ondblclick", ondblclick);
		prepareAttribute(sb, "onkeydown", onkeydown);
		prepareAttribute(sb, "onkeypress", onkeypress);
		prepareAttribute(sb, "onkeyup", onkeyup);
		prepareAttribute(sb, "onmousedown", onmousedown);
		prepareAttribute(sb, "onmousemove", onmousemove);
		prepareAttribute(sb, "onmouseout", onmouseout);
		prepareAttribute(sb, "onmouseover", onmouseover);
		prepareAttribute(sb, "onmouseup", onmouseup);
		if (this.disabled) {
			sb.append(" disabled=\"disabled\"");
		}

		sb.append(">");

		return sb.toString();
	}

	private void prepareAttribute(StringBuffer handlers, String name, Object value) {
		if (value != null) {
			handlers.append(" ");
			handlers.append(name);
			handlers.append("=\"");
			handlers.append(value);
			handlers.append("\"");
		}
	}

	public String getDataOptions() {
		return dataOptions;
	}

	public void setDataOptions(String dataOptions) {
		this.dataOptions = dataOptions;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getOndblclick() {
		return ondblclick;
	}

	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	public String getOnmouseover() {
		return onmouseover;
	}

	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}

	public String getOnmouseout() {
		return onmouseout;
	}

	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}

	public String getOnmousemove() {
		return onmousemove;
	}

	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}

	public String getOnmousedown() {
		return onmousedown;
	}

	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}

	public String getOnmouseup() {
		return onmouseup;
	}

	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}

	public String getOnkeydown() {
		return onkeydown;
	}

	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}

	public String getOnkeyup() {
		return onkeyup;
	}

	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}

	public String getOnkeypress() {
		return onkeypress;
	}

	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}

	public String getType() {
		return type;
	}

	public void setType(String tagName) {
		this.type = tagName;
	}
}
