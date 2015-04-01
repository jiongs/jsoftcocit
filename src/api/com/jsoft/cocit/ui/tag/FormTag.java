package com.jsoft.cocit.ui.tag;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.jsoft.cocimpl.util.ResponseUtil;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;

public class FormTag extends TagSupport {
	private static final long serialVersionUID = -4678364914584269550L;

	protected static String lineEnd = System.getProperty("line.separator");

	private static IMessageConfig messages = Cocit.me().getMessages();

	// ---------------------------------------------------
	// FORM 属性
	// ---------------------------------------------------

	/**
	 * The action URL to which this form should be submitted, if any.
	 */
	protected String action = null;

	/**
	 * Autocomplete non standard attribute
	 */
	private String autocomplete = null;

	// /**
	// * A postback action URL to which this form should be submitted, if any.
	// */
	// private String postbackAction = null;

	/**
	 * The content encoding to be used on a POST submit.
	 */
	protected String enctype = null;

	/**
	 * The name of the field to receive focus, if any.
	 */
	protected String focus = null;

	/**
	 * The index in the focus field array to receive focus. This only applies if the field given in the focus attribute is actually an array of fields. This allows a specific field in a radio button array to receive focus while still allowing indexed
	 * field names like "myRadioButtonField[1]" to be passed in the focus attribute.
	 * 
	 */
	protected String focusIndex = null;

	/**
	 * The request method used when submitting this form.
	 */
	protected String method = null;

	/**
	 * The onReset event script.
	 */
	protected String onreset = null;

	/**
	 * The onSubmit event script.
	 */
	protected String onsubmit = null;

	/**
	 * Include language attribute in the focus script's &lt;script&gt; element. This property is ignored in XHTML mode.
	 * 
	 */
	protected boolean scriptLanguage = true;

	/**
	 * The style attribute associated with this tag.
	 */
	protected String style = null;

	/**
	 * The style class associated with this tag.
	 */
	protected String styleClass = null;

	/**
	 * The identifier associated with this tag.
	 */
	protected String styleId = null;

	/**
	 * The window target.
	 */
	protected String target = null;

	/**
	 * The name of the form bean to (create and) use. This is either the same as the 'name' attribute, if that was specified, or is obtained from the associated <code>ActionMapping</code> otherwise.
	 */
	protected String beanName = null;

	/**
	 * The scope of the form bean to (create and) use. This is either the same as the 'scope' attribute, if that was specified, or is obtained from the associated <code>ActionMapping</code> otherwise.
	 */
	protected String beanScope = null;

	/**
	 * The type of the form bean to (create and) use. This is either the same as the 'type' attribute, if that was specified, or is obtained from the associated <code>ActionMapping</code> otherwise.
	 */
	protected String beanType = null;

	/**
	 * The list of character encodings for input data that the server should accept.
	 */
	protected String acceptCharset = null;

	/**
	 * Controls whether child controls should be 'disabled'.
	 */
	private boolean disabled = false;

	/**
	 * Controls whether child controls should be 'readonly'.
	 */
	protected boolean readonly = false;

	/**
	 * The language code of this element.
	 */
	private String lang = null;

	/**
	 * The direction for weak/neutral text of this element.
	 */
	private String dir = null;

	// ---------------------------------------------------
	// COC 属性
	// ---------------------------------------------------

	/**
	 * UI模型表达式：由三部分组成，包括“系统菜单编号、实体模块编号、操作按钮编号（systemMenuID:entityModuleID:opID）”
	 * <p>
	 * 如果表达式存在，则自动将表达式解析成 UIModel 。
	 */
	private String opExpr;

	/**
	 * 数据ID：实体数据在数据库表中的物理主键、或逻辑主键
	 */
	private String dataID;

	/**
	 * 表单模型：可以通过如下方式得到UIForm对象。
	 * <UL>
	 * <LI>可以从{@link #opExpr}解析而来，如果为指定{@link #opExpr}；
	 * <LI>则从request中获取(key={@link ViewKeys#UI_MODEL_KEY})；
	 * <LI>
	 * </UL>
	 */
	private UIForm uiModel;

	private boolean makedHttpContext = false;

	// --------------------------------------------------------- Public Methods

	public int doStartTag() throws JspException {
		Writer out = null;
		try {

			out = pageContext.getOut();

			// Look up the form bean name, scope, and type if necessary
			this.lookup();

			// Create an appropriate "form" element based on our parameters
			StringBuffer results = new StringBuffer();

			results.append(this.renderFormStartElement());

			results.append(this.renderToken());

			out.write(results.toString());

			// Store this tag itself as a page attribute
			pageContext.setAttribute(ViewKeys.TAG_FORM_KEY, this, PageContext.REQUEST_SCOPE);

			return (EVAL_BODY_INCLUDE);

		} catch (Throwable e) {

			try {

				ResponseUtil.writeError(out, e);

			} catch (IOException e1) {
			}

			return SKIP_BODY;
		} finally {
		}

	}

	protected void lookup() throws JspException {
		int scope = PageContext.SESSION_SCOPE;

		if ("request".equalsIgnoreCase(beanScope)) {
			scope = PageContext.REQUEST_SCOPE;
		}

		if (beanName == null) {
			beanName = ViewKeys.BEAN_KEY;
		}

		/*
		 * 从Request中获取UIForm
		 */
		UIModel baseModel = (UIModel) pageContext.getAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);
		if (baseModel != null && baseModel instanceof UIForm) {
			uiModel = (UIForm) baseModel;
		}

		/*
		 * Request中获取UIForm失败：从“操作表达式”解析UIForm
		 */
		if (uiModel == null && StringUtil.hasContent(opExpr)) {

			HttpContext httpContext = Cocit.me().getHttpContext();
			if (httpContext == null) {
				makedHttpContext = true;
				Cocit.me().makeHttpContext((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
			}

			OpContext opContext = OpContext.make(opExpr, dataID, null);
			if (opContext.getException() != null) {
				throw new JspException(opContext.getException());
			}

			uiModel = (UIForm) opContext.getUiModelFactory().getForm(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getCocAction(), opContext.getDataObject());

			uiModel.setSubmitUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_SAVE, opExpr, dataID));
			uiModel.setDataObject(opContext.getDataObject());

		}

		/*
		 * 计算表单 action 属性
		 */
		if (this.action == null && uiModel != null)
			this.action = uiModel.getSubmitUrl();

		/*
		 * 缓存UI模型(UIModel)，子标记库会用到。
		 */
		pageContext.setAttribute(ViewKeys.UI_MODEL_KEY, uiModel, PageContext.REQUEST_SCOPE);

		/*
		 * 获取实体数据对象 bean
		 */
		Object bean = pageContext.getAttribute(beanName, scope);
		if (bean == null && uiModel != null) {
			bean = uiModel.getDataObject();
		}

		/*
		 * 缓存实体数据对象(bean)，子标记库会用到。
		 */
		pageContext.setAttribute(ViewKeys.BEAN_KEY, bean, PageContext.REQUEST_SCOPE);
	}

	protected String renderFormStartElement() throws JspException {
		StringBuffer results = new StringBuffer("<form");

		// render attributes
		renderName(results);

		renderAttribute(results, "method", (getMethod() == null) ? "post" : getMethod());
		renderAction(results);
		renderAttribute(results, "accept-charset", getAcceptCharset());
		renderAttribute(results, "class", getStyleClass());
		renderAttribute(results, "dir", getDir());
		renderAttribute(results, "enctype", getEnctype());
		renderAttribute(results, "lang", getLang());
		renderAttribute(results, "onreset", getOnreset());
		renderAttribute(results, "onsubmit", getOnsubmit());
		renderAttribute(results, "style", getStyle());
		renderAttribute(results, "target", getTarget());
		renderAttribute(results, "id", getId());
		if (!isXhtml()) {
			renderAttribute(results, "autocomplete", getAutocomplete());
		}

		renderOtherAttributes(results);

		results.append(">");

		return results.toString();
	}

	protected void renderName(StringBuffer results) throws JspException {
		if (this.isXhtml()) {
			if (getStyleId() == null) {
				renderAttribute(results, "id", beanName);
			} else {
				throw new JspException("formTag.ignoredId");
			}
		} else {
			renderAttribute(results, "name", beanName);
			renderAttribute(results, "id", getStyleId());
		}
	}

	protected void renderAction(StringBuffer results) {
		HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();

		results.append(" action=\"");
		if (this.action != null)
			results.append(response.encodeURL(this.action));

		results.append("\"");
	}

	protected void renderOtherAttributes(StringBuffer results) {
	}

	protected String renderToken() {
		StringBuffer results = new StringBuffer();
		HttpSession session = pageContext.getSession();

		if (session != null) {
			String token = (String) session.getAttribute(ViewKeys.TRANSACTION_TOKEN_KEY);

			if (token != null) {
				results.append("<div><input type=\"hidden\" name=\"");
				results.append(ViewKeys.TOKEN_KEY);
				results.append("\" value=\"");
				results.append(token);

				if (this.isXhtml()) {
					results.append("\" />");
				} else {
					results.append("\">");
				}

				results.append("</div>");
			}
		}

		return results.toString();
	}

	protected void renderAttribute(StringBuffer results, String attribute, String value) {
		if (value != null) {
			results.append(" ");
			results.append(attribute);
			results.append("=\"");
			results.append(value);
			results.append("\"");
		}
	}

	public int doEndTag() throws JspException {
		pageContext.removeAttribute(ViewKeys.BEAN_KEY, PageContext.REQUEST_SCOPE);
		pageContext.removeAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);
		pageContext.removeAttribute(ViewKeys.TAG_FORM_KEY, PageContext.REQUEST_SCOPE);

		// Render a tag representing the end of our current form
		StringBuffer results = new StringBuffer("</form>");

		// Render JavaScript to set the input focus if required
		if (this.focus != null) {
			results.append(this.renderFocusJavascript());
		}

		// Print this value to our output writer
		JspWriter writer = pageContext.getOut();

		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(messages.get("common.io", e.toString()));
		}

		if (makedHttpContext) {
			Cocit.me().getHttpContext().release();
			Cocit.me().releaseHttpConext();
		}

		this.release();

		// Continue processing this page
		return (EVAL_PAGE);
	}

	protected String renderFocusJavascript() {
		StringBuffer results = new StringBuffer();

		results.append(lineEnd);
		results.append("<script type=\"text/javascript\"");

		if (!this.isXhtml() && this.scriptLanguage) {
			results.append(" language=\"JavaScript\"");
		}

		results.append(">");
		results.append(lineEnd);

		// xhtml script content shouldn't use the browser hiding trick
		if (!this.isXhtml()) {
			results.append("  <!--");
			results.append(lineEnd);
		}

		// Construct the index if needed and insert into focus statement
		String index = "";
		if (this.focusIndex != null) {
			StringBuffer sb = new StringBuffer("[");
			sb.append(this.focusIndex);
			sb.append("]");
			index = sb.toString();
		}

		// Construct the control name that will receive focus.
		StringBuffer focusControl = new StringBuffer("document.forms[\"");
		focusControl.append(beanName);
		focusControl.append("\"].elements[\"");
		focusControl.append(this.focus);
		focusControl.append("\"]");
		focusControl.append(index);

		results.append("  var focusControl = ");
		results.append(focusControl.toString());
		results.append(";");
		results.append(lineEnd);
		results.append(lineEnd);

		results.append("  if (focusControl != null && ");
		results.append("focusControl.type != \"hidden\" && ");
		results.append("!focusControl.disabled && ");
		results.append("focusControl.style.display != \"none\") {");
		results.append(lineEnd);

		results.append("     focusControl");
		results.append(".focus();");
		results.append(lineEnd);

		results.append("  }");
		results.append(lineEnd);

		if (!this.isXhtml()) {
			results.append("  // -->");
			results.append(lineEnd);
		}

		results.append("</script>");
		results.append(lineEnd);

		return results.toString();
	}

	/**
	 * Release any acquired resources.
	 */
	public void release() {
		super.release();

		// ----FORM 属性
		acceptCharset = null;
		action = null;
		autocomplete = null;
		beanName = null;
		beanScope = null;
		dir = null;
		disabled = false;
		enctype = null;
		focus = null;
		focusIndex = null;
		lang = null;
		method = null;
		onreset = null;
		onsubmit = null;
		readonly = false;
		style = null;
		styleClass = null;
		styleId = null;
		target = null;

		// ----COC 属性
		uiModel = null;
		opExpr = null;
		dataID = null;

		makedHttpContext = false;
	}

	// ------------------------------------------------------------- Properties

	public String getBeanName() {
		return beanName;
	}

	public String getAction() {
		return (this.action);
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAutocomplete() {
		return autocomplete;
	}

	public void setAutocomplete(String autocomplete) {
		this.autocomplete = autocomplete;
	}

	public String getEnctype() {
		return (this.enctype);
	}

	public void setEnctype(String enctype) {
		this.enctype = enctype;
	}

	public String getFocus() {
		return (this.focus);
	}

	public void setFocus(String focus) {
		this.focus = focus;
	}

	public String getMethod() {
		return (this.method);
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getOnreset() {
		return (this.onreset);
	}

	public void setOnreset(String onReset) {
		this.onreset = onReset;
	}

	public String getOnsubmit() {
		return (this.onsubmit);
	}

	public void setOnsubmit(String onSubmit) {
		this.onsubmit = onSubmit;
	}

	public String getStyle() {
		return (this.style);
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return (this.styleClass);
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getStyleId() {
		return (this.styleId);
	}

	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	public String getTarget() {
		return (this.target);
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getAcceptCharset() {
		return acceptCharset;
	}

	public void setAcceptCharset(String acceptCharset) {
		this.acceptCharset = acceptCharset;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public String getLang() {
		return this.lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getDir() {
		return this.dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	private boolean isXhtml() {
		return false;
	}

	public String getFocusIndex() {
		return focusIndex;
	}

	public void setFocusIndex(String focusIndex) {
		this.focusIndex = focusIndex;
	}

	public boolean getScriptLanguage() {
		return this.scriptLanguage;
	}

	public void setScriptLanguage(boolean scriptLanguage) {
		this.scriptLanguage = scriptLanguage;
	}

	public String getOpExpr() {
		return opExpr;
	}

	public void setOpExpr(String opExpr) {
		this.opExpr = opExpr;
	}

	public String getFuncExpr() {
		return opExpr;
	}

	public void setFuncExpr(String opExpr) {
		this.opExpr = opExpr;
	}

	public String getDataID() {
		return dataID;
	}

	public void setDataID(String dataID) {
		this.dataID = dataID;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanScope() {
		return beanScope;
	}

	public void setBeanScope(String beanScope) {
		this.beanScope = beanScope;
	}

}
