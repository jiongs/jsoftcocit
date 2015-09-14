package com.jsoft.cocit.ui.tag;

import java.lang.reflect.Method;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.jsoft.cocimpl.ui.tag.TagUtils;
import com.jsoft.cocimpl.util.RequestUtil;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.ui.UIViews;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.ui.view.UIFieldView;
import com.jsoft.cocit.util.IteratorAdapter;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

public class FieldTag extends BodyTagSupport {

	private static final long serialVersionUID = 7786730040243417436L;

	private static IMessageConfig messages = Cocit.me().getMessages();

	// ==========================================================
	// CUI Variables
	// ==========================================================
	private String mode;
	private String dicOptions;

	// ==========================================================
	// Instance Variables
	// ==========================================================

	// ----------------------------------------------------------
	// Navigation Management
	// ----------------------------------------------------------

	/**
	 * Access key character.
	 */
	private String accesskey = null;

	/**
	 * Tab index value.
	 */
	private String tabindex = null;

	// ----------------------------------------------------------
	// Indexing ability for Iterate
	// ----------------------------------------------------------

	/**
	 * Whether to created indexed names for fields
	 * 
	 * @since 1.1
	 */
	private boolean indexed = false;

	/**
	 * Autocomplete non standard attribute
	 */
	private String autocomplete = null;

	/**
	 * The number of character columns for this field, or negative for no limit.
	 */
	private String cols = null;

	/**
	 * The maximum number of characters allowed, or negative for no limit.
	 */
	private String maxlength = null;

	/**
	 * The name of the field (and associated property) being processed.
	 */
	private String property = null;

	/**
	 * The number of rows for this field, or negative for no limit.
	 */
	private String rows = null;

	/**
	 * The value for this field, or <code>null</code> to retrieve the corresponding property from our associated bean.
	 */
	private String value = null;

	/**
	 * The name of the bean containing our underlying property.
	 */
	private String name = ViewKeys.BEAN_KEY;

	/**
	 * Comma-delimited list of content types that a server processing this form will handle correctly. This property is defined only for the <code>file</code> tag, but is implemented here because it affects the rendered HTML of the corresponding
	 * &lt;input&gt; tag.
	 */
	private String accept = null;

	/**
	 * The "redisplay contents" flag (used only on <code>password</code>).
	 */
	private boolean redisplay = true;

	/**
	 * The type of input field represented by this tag (text, password, or hidden).
	 * <p>
	 * 对应COC 的 uiView
	 */
	private String type = null;

	/**
	 * Component is disabled.
	 */
	private boolean disabled = false;

	/**
	 * Indicates whether 'disabled' is a valid attribute
	 */
	private boolean doDisabled = true;

	/**
	 * Component is readonly.
	 */
	private boolean readonly = false;

	/**
	 * <p>
	 * Indicates whether 'readonly' is a valid attribute.
	 * </p>
	 * 
	 * <p>
	 * According to the HTML 4.0 Specification &lt;readonly&gt; is valid for &lt;input type="text"&gt;, &lt;input type="password"&gt; and &lt;textarea"&gt; elements. Therefore, except for those tags this value is set to <code>false</code>.
	 * </p>
	 */
	private boolean doReadonly = false;

	// ----------------------------------------------------------
	// CSS Style Support
	// ----------------------------------------------------------

	/**
	 * Style attribute associated with component.
	 */
	private String style = null;

	/**
	 * Named Style class associated with component.
	 */
	private String styleClass = null;

	/**
	 * Identifier associated with component.
	 */
	private String styleId = null;

	// /**
	// * The request attribute key for our error messages (if any).
	// */
	// private String errorKey = Globals.ERROR_KEY;

	/**
	 * Style attribute associated with component when errors exist.
	 */
	private String errorStyle = null;

	/**
	 * Named Style class associated with component when errors exist.
	 */
	private String errorStyleClass = null;

	/**
	 * Identifier associated with component when errors exist.
	 */
	private String errorStyleId = null;

	// ----------------------------------------------------------
	// Other Common Attributes
	// ----------------------------------------------------------

	/**
	 * The alternate text of this element.
	 */
	private String alt = null;

	/**
	 * The message resources key of the alternate text.
	 */
	private String altKey = null;

	/**
	 * The name of the message resources bundle for message lookups.
	 */
	private String bundle = null;

	// /**
	// * The name of the session attribute key for our locale.
	// */
	// private String locale = Globals.LOCALE_KEY;

	/**
	 * The advisory title of this element.
	 */
	private String title = null;

	/**
	 * The language code of this element.
	 */
	private String lang = null;

	/**
	 * The direction for weak/neutral text of this element.
	 */
	private String dir = null;

	/**
	 * The message resources key of the advisory title.
	 */
	private String titleKey = null;

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

	// ----------------------------------------------------------
	// Text Events
	// ----------------------------------------------------------

	/**
	 * Text selected in component event.
	 */
	private String onselect = null;

	/**
	 * Content changed after component lost focus event.
	 */
	private String onchange = null;

	// Focus Events and States

	/**
	 * Component lost focus event.
	 */
	private String onblur = null;

	/**
	 * Component has received focus event.
	 */
	private String onfocus = null;

	// ----------------------------------------------------------
	// JSTL support
	// ----------------------------------------------------------
	private Class loopTagClass = null;

	private Method loopTagGetStatus = null;

	private Class loopTagStatusClass = null;

	private Method loopTagStatusGetIndex = null;

	private boolean triedJstlInit = false;

	private boolean triedJstlSuccess = false;

	// ---------------------------------------------------------
	// Public Methods
	// ---------------------------------------------------------

	/**
	 * Generate the required input tag.
	 * <p>
	 * Support for indexed property since 1.1
	 * 
	 * @throws JspException
	 *             if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {

		UIForm uiForm = null;
		UIModel baseModel = (UIModel) pageContext.getAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);
		if (baseModel != null && baseModel instanceof UIForm) {
			uiForm = (UIForm) baseModel;
		}
		
		UIField field = null;
		if (uiForm != null) {
			field = uiForm.getField(this.property);
		}

		if (field != null) {

			UIViews views = Cocit.me().getViews();

			Object bean = uiForm.getDataObject();
			String viewName = type;
			if (viewName == null) {
				viewName = field.getViewName();
			}
			UIFieldView view = views.getFieldView(viewName);
			ICocFieldInfo fieldService = field.getFieldService();
			String propName = fieldService.getFieldName();
			String fieldName = uiForm.getBeanName() + "." + propName;
			Object fieldValue = ObjectUtil.getValue(bean, propName);
			Properties attrs = this.prepareAttributes();
			field.setAttributes(attrs);
			if (StringUtil.hasContent(mode))
				field.setMode(FieldModes.parseMode(mode));
			if (StringUtil.hasContent(dicOptions)) {
				Option[] array = StringUtil.toOptions(dicOptions);
				field.setDicOptions(array);
			}

			try {
				view.render(pageContext.getOut(), field, bean, fieldName, fieldValue);
			} catch (Exception e) {
				throw new JspException(e);
			}

		} else {

			TagUtils.getInstance().write(this.pageContext, this.renderInputElement());

		}

		this.release();

		return (EVAL_BODY_INCLUDE);
	}

	private String renderInputElement() throws JspException {
		StringBuffer results = new StringBuffer("<input");

		prepareAttribute(results, "type", this.type);
		prepareAttribute(results, "name", prepareName());
		prepareAttribute(results, "id", getId());
		prepareValue(results);

		Properties attrs = this.prepareAttributes();

		IteratorAdapter keys = new IteratorAdapter(attrs.keys());
		while (keys.hasNext()) {
			String key = (String) keys.next();
			prepareAttribute(results, key, attrs.get(key));
		}

		results.append(this.getElementClose());

		return results.toString();
	}

	/**
	 * Renders a fully formed &lt;input&gt; element.
	 * 
	 * @throws JspException
	 * @since 1.2
	 */
	private Properties prepareAttributes() throws JspException {
		Properties attributes = new Properties();

		// prepareAttribute(attributes, "type", this.type);
		// prepareAttribute(attributes, "name", prepareName());
		// prepareValue(results);

		prepareAttribute(attributes, "accesskey", getAccesskey());
		prepareAttribute(attributes, "accept", getAccept());
		prepareAttribute(attributes, "maxlength", getMaxlength());
		prepareAttribute(attributes, "size", getCols());
		prepareAttribute(attributes, "tabindex", getTabindex());

		prepareMouseEvents(attributes);
		prepareKeyEvents(attributes);
		prepareTextEvents(attributes);
		prepareFocusEvents(attributes);
		prepareStyles(attributes);

		prepareAttribute(attributes, "title", message(getTitle(), getTitleKey()));
		prepareAttribute(attributes, "alt", message(getAlt(), getAltKey()));
		prepareAttribute(attributes, "lang", getLang());
		prepareAttribute(attributes, "dir", getDir());

		if (!isXhtml()) {
			prepareAttribute(attributes, "autocomplete", getAutocomplete());
		}

		return attributes;
	}

	/**
	 * Prepare the name element
	 * 
	 * @return The element name.
	 */
	private String prepareName() throws JspException {
		if (property == null) {
			return null;
		}

		// * @since 1.1
		if (indexed) {
			StringBuffer results = new StringBuffer();

			prepareIndex(results, name);
			results.append(property);

			return results.toString();
		}

		return property;
	}

	/**
	 * Render the value element
	 * 
	 * @param results
	 *            The StringBuffer that output will be appended to.
	 */
	private void prepareValue(StringBuffer results) throws JspException {
		results.append(" value=\"");

		if (value != null) {
			results.append(this.formatValue(value));
		} else if (redisplay || !"password".equals(type)) {
			Object value = TagUtils.getInstance().lookup(pageContext, name, property, null);

			results.append(this.formatValue(value));
		}

		results.append('"');
	}

	/**
	 * Return the given value as a formatted <code>String</code>. This implementation escapes potentially harmful HTML characters.
	 * 
	 * @param value
	 *            The value to be formatted. <code>null</code> values will be returned as the empty String "".
	 * @throws JspException
	 *             if a JSP exception has occurred
	 * @since 1.2
	 */
	private String formatValue(Object value) throws JspException {
		if (value == null) {
			return "";
		}

		return TagUtils.getInstance().filter(value.toString());
	}

	/**
	 * Prepares the mouse event handlers, appending them to the the given StringBuffer.
	 * 
	 * @param handlers
	 *            The StringBuffer that output will be appended to.
	 */
	private void prepareMouseEvents(Properties handlers) {
		prepareAttribute(handlers, "onclick", getOnclick());
		prepareAttribute(handlers, "ondblclick", getOndblclick());
		prepareAttribute(handlers, "onmouseover", getOnmouseover());
		prepareAttribute(handlers, "onmouseout", getOnmouseout());
		prepareAttribute(handlers, "onmousemove", getOnmousemove());
		prepareAttribute(handlers, "onmousedown", getOnmousedown());
		prepareAttribute(handlers, "onmouseup", getOnmouseup());
	}

	/**
	 * Prepares the keyboard event handlers, appending them to the the given StringBuffer.
	 * 
	 * @param handlers
	 *            The StringBuffer that output will be appended to.
	 */
	private void prepareKeyEvents(Properties handlers) {
		prepareAttribute(handlers, "onkeydown", getOnkeydown());
		prepareAttribute(handlers, "onkeyup", getOnkeyup());
		prepareAttribute(handlers, "onkeypress", getOnkeypress());
	}

	/**
	 * Prepares the text event handlers, appending them to the the given StringBuffer.
	 * 
	 * @param handlers
	 *            The StringBuffer that output will be appended to.
	 */
	private void prepareTextEvents(Properties handlers) {
		prepareAttribute(handlers, "onselect", getOnselect());
		prepareAttribute(handlers, "onchange", getOnchange());
	}

	/**
	 * Prepares the focus event handlers, appending them to the the given StringBuffer.
	 * 
	 * @param handlers
	 *            The StringBuffer that output will be appended to.
	 */
	private void prepareFocusEvents(Properties handlers) {
		prepareAttribute(handlers, "onblur", getOnblur());
		prepareAttribute(handlers, "onfocus", getOnfocus());

		// Get the parent FormTag (if necessary)
		FormTag formTag = null;

		if ((doDisabled && !getDisabled()) || (doReadonly && !getReadonly())) {
			formTag = (FormTag) pageContext.getAttribute(ViewKeys.TAG_FORM_KEY, PageContext.REQUEST_SCOPE);
		}

		// Format Disabled
		if (doDisabled) {
			boolean formDisabled = (formTag == null) ? false : formTag.isDisabled();

			if (formDisabled || getDisabled()) {
				// handlers.append(" disabled=\"disabled\"");
				handlers.setProperty("disabled", "disabled");
			}
		}

		// Format Read Only
		if (doReadonly) {
			boolean formReadOnly = (formTag == null) ? false : formTag.isReadonly();

			if (formReadOnly || getReadonly()) {
				// handlers.append(" readonly=\"readonly\"");
				handlers.setProperty("readonly", "readonly");
			}
		}
	}

	/**
	 * Prepares an attribute if the value is not null, appending it to the the given StringBuffer.
	 * 
	 * @param handlers
	 *            The StringBuffer that output will be appended to.
	 */
	private void prepareAttribute(Properties attributes, String name, Object value) {
		if (value != null) {
			attributes.setProperty(name, value.toString());
			// handlers.append(" ");
			// handlers.append(name);
			// handlers.append("=\"");
			// handlers.append(value);
			// handlers.append("\"");
		}
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

	// ------------------------------------------------------ Protected Methods

	/**
	 * Return the text specified by the literal value or the message resources key, if any; otherwise return <code>null</code>.
	 * 
	 * @param literal
	 *            Literal text value or <code>null</code>
	 * @param key
	 *            Message resources key or <code>null</code>
	 * @throws JspException
	 *             if both arguments are non-null
	 */
	private String message(String literal, String key) throws JspException {
		if (literal != null) {
			if (key != null) {
				JspException e = new JspException(messages.get("common.both"));

				// TagUtils.getInstance().saveException(pageContext, e);
				throw e;
			} else {
				return (literal);
			}
		} else {
			if (key != null) {
				return TagUtils.getInstance().message(pageContext, getBundle(), null, key);
			} else {
				return null;
			}
		}
	}

	private Integer getJstlLoopIndex() {
		if (!triedJstlInit) {
			triedJstlInit = true;

			try {
				loopTagClass = RequestUtil.applicationClass("javax.servlet.jsp.jstl.core.LoopTag");

				loopTagGetStatus = loopTagClass.getDeclaredMethod("getLoopStatus");

				loopTagStatusClass = RequestUtil.applicationClass("javax.servlet.jsp.jstl.core.LoopTagStatus");

				loopTagStatusGetIndex = loopTagStatusClass.getDeclaredMethod("getIndex");

				triedJstlSuccess = true;
			} catch (ClassNotFoundException ex) {
				// These just mean that JSTL isn't loaded, so ignore
			} catch (NoSuchMethodException ex) {
			}
		}

		if (triedJstlSuccess) {
			try {
				Object loopTag = findAncestorWithClass(this, loopTagClass);

				if (loopTag == null) {
					return null;
				}

				Object status = loopTagGetStatus.invoke(loopTag);

				return (Integer) loopTagStatusGetIndex.invoke(status);
			} catch (Throwable ex) {
				LogUtil.error(ex.getMessage(), ex);
			}
		}

		return null;
	}

	/**
	 * Appends bean name with index in brackets for tags with 'true' value in 'indexed' attribute.
	 * 
	 * @param handlers
	 *            The StringBuffer that output will be appended to.
	 * @throws JspException
	 *             if 'indexed' tag used outside of iterate tag.
	 */
	private void prepareIndex(StringBuffer handlers, String name) throws JspException {
		if (name != null) {
			handlers.append(name);
		}

		handlers.append("[");
		handlers.append(getIndexValue());
		handlers.append("]");

		if (name != null) {
			handlers.append(".");
		}
	}

	/**
	 * Returns the index value for tags with 'true' value in 'indexed' attribute.
	 * 
	 * @return the index value.
	 * @throws JspException
	 *             if 'indexed' tag used outside of iterate tag.
	 */
	private int getIndexValue() throws JspException {
		// look for outer iterate tag
		IterateTag iterateTag = (IterateTag) findAncestorWithClass(this, IterateTag.class);

		if (iterateTag != null) {
			return iterateTag.getIndex();
		}

		// Look for JSTL loops
		Integer i = getJstlLoopIndex();

		if (i != null) {
			return i.intValue();
		}

		// this tag should be nested in an IterateTag or JSTL loop tag, if it's not, throw exception
		JspException e = new JspException(messages.get("indexed.noEnclosingIterate"));

		// TagUtils.getInstance().saveException(pageContext, e);
		throw e;
	}

	/**
	 * Prepares the style attributes for inclusion in the component's HTML tag.
	 * 
	 * @return The prepared String for inclusion in the HTML tag.
	 * @throws JspException
	 *             if invalid attributes are specified
	 */
	private Properties prepareStyles(Properties styles) throws JspException {

		boolean errorsExist = doErrorsExist();

		// if (errorsExist && (getErrorStyleId() != null)) {
		// prepareAttribute(styles, "id", getErrorStyleId());
		// } else {
		// prepareAttribute(styles, "id", getStyleId());
		// }

		if (errorsExist && (getErrorStyle() != null)) {
			prepareAttribute(styles, "style", getErrorStyle());
		} else {
			prepareAttribute(styles, "style", getStyle());
		}

		if (errorsExist && (getErrorStyleClass() != null)) {
			prepareAttribute(styles, "class", getErrorStyleClass());
		} else {
			prepareAttribute(styles, "class", getStyleClass());
		}

		return styles;
	}

	/**
	 * Determine if there are errors for the component.
	 * 
	 * @return Whether errors exist.
	 */
	private boolean doErrorsExist() throws JspException {
		boolean errorsExist = false;

		// if ((getErrorStyleId() != null) || (getErrorStyle() != null) || (getErrorStyleClass() != null)) {
		// String actualName = prepareName();
		//
		// if (actualName != null) {
		// List<String> errors = null;// TagUtils.getInstance().getActionMessages(pageContext, errorKey);
		//
		// errorsExist = ((errors != null) && (errors.size(actualName) > 0));
		// }
		// }

		return errorsExist;
	}

	// /**
	// * Allows HTML tags to find out if they're nested within an %lt;html:html&gt; tag that has xhtml set to true.
	// *
	// * @return true if the tag is nested within an html tag with xhtml set to true, false otherwise.
	// * @since 1.1
	// */
	// private boolean isXhtml() {
	// return TagUtils.getInstance().isXhtml(this.pageContext);
	// }

	// /**
	// * Returns the closing brace for an input element depending on xhtml status. The tag must be nested within an %lt;html:html&gt; tag that has xhtml set to true.
	// *
	// * @return String - &gt; if xhtml is false, /&gt; if xhtml is true
	// * @since 1.1
	// */
	private String getElementClose() {
		return this.isXhtml() ? " />" : ">";
	}

	/**
	 * Release any acquired resources.
	 */
	public void release() {
		super.release();

		accesskey = null;
		alt = null;
		altKey = null;
		bundle = null;
		dir = null;
		// errorKey = Globals.ERROR_KEY;
		errorStyle = null;
		errorStyleClass = null;
		errorStyleId = null;
		indexed = false;
		lang = null;
		// locale = Globals.LOCALE_KEY;
		onclick = null;
		ondblclick = null;
		onmouseover = null;
		onmouseout = null;
		onmousemove = null;
		onmousedown = null;
		onmouseup = null;
		onkeydown = null;
		onkeyup = null;
		onkeypress = null;
		onselect = null;
		onchange = null;
		onblur = null;
		onfocus = null;
		disabled = false;
		readonly = false;
		style = null;
		styleClass = null;
		styleId = null;
		tabindex = null;
		title = null;
		titleKey = null;

		autocomplete = null;
		name = ViewKeys.BEAN_KEY;
		cols = null;
		maxlength = null;
		property = null;
		rows = null;
		value = null;

		accept = null;
		name = ViewKeys.BEAN_KEY;
		redisplay = true;
		type = null;
	}

	public String getAccept() {
		return (this.accept);
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public boolean getRedisplay() {
		return (this.redisplay);
	}

	public void setRedisplay(boolean redisplay) {
		this.redisplay = redisplay;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Return autocomplete
	 * 
	 * @since 1.3.10
	 */
	public String getAutocomplete() {
		return autocomplete;
	}

	/**
	 * Activate/disactivate autocompletion (on/off)
	 * 
	 * @since 1.3.10
	 */
	public void setAutocomplete(String autocomplete) {
		this.autocomplete = autocomplete;
	}

	public String getName() {
		return (this.name);
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the number of columns for this field.
	 */
	public String getCols() {
		return (this.cols);
	}

	/**
	 * Set the number of columns for this field.
	 * 
	 * @param cols
	 *            The new number of columns
	 */
	public void setCols(String cols) {
		this.cols = cols;
	}

	/**
	 * Return the maximum length allowed.
	 */
	public String getMaxlength() {
		return (this.maxlength);
	}

	/**
	 * Set the maximum length allowed.
	 * 
	 * @param maxlength
	 *            The new maximum length
	 */
	public void setMaxlength(String maxlength) {
		this.maxlength = maxlength;
	}

	/**
	 * Return the property name.
	 */
	public String getProperty() {
		return (this.property);
	}

	/**
	 * Set the property name.
	 * 
	 * @param property
	 *            The new property name
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * Return the number of rows for this field.
	 */
	public String getRows() {
		return (this.rows);
	}

	/**
	 * Set the number of rows for this field.
	 * 
	 * @param rows
	 *            The new number of rows
	 */
	public void setRows(String rows) {
		this.rows = rows;
	}

	/**
	 * Return the size of this field (synonym for <code>getCols()</code>).
	 */
	public String getSize() {
		return (getCols());
	}

	/**
	 * Set the size of this field (synonym for <code>setCols()</code>).
	 * 
	 * @param size
	 *            The new size
	 */
	public void setSize(String size) {
		setCols(size);
	}

	/**
	 * Return the field value (if any).
	 */
	public String getValue() {
		return (this.value);
	}

	/**
	 * Set the field value (if any).
	 * 
	 * @param value
	 *            The new field value, or <code>null</code> to retrieve the corresponding property from the bean
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Sets the accessKey character.
	 */
	public void setAccesskey(String accessKey) {
		this.accesskey = accessKey;
	}

	/**
	 * Returns the accessKey character.
	 */
	public String getAccesskey() {
		return (this.accesskey);
	}

	/**
	 * Sets the tabIndex value.
	 */
	public void setTabindex(String tabIndex) {
		this.tabindex = tabIndex;
	}

	/**
	 * Returns the tabIndex value.
	 */
	public String getTabindex() {
		return (this.tabindex);
	}

	// Indexing ability for Iterate [since 1.1]

	/**
	 * Sets the indexed value.
	 * 
	 * @since 1.1
	 */
	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
	}

	/**
	 * Returns the indexed value.
	 * 
	 * @since 1.1
	 */
	public boolean getIndexed() {
		return (this.indexed);
	}

	// Mouse Events

	/**
	 * Sets the onClick event handler.
	 */
	public void setOnclick(String onClick) {
		this.onclick = onClick;
	}

	/**
	 * Returns the onClick event handler.
	 */
	public String getOnclick() {
		return onclick;
	}

	/**
	 * Sets the onDblClick event handler.
	 */
	public void setOndblclick(String onDblClick) {
		this.ondblclick = onDblClick;
	}

	/**
	 * Returns the onDblClick event handler.
	 */
	public String getOndblclick() {
		return ondblclick;
	}

	/**
	 * Sets the onMouseDown event handler.
	 */
	public void setOnmousedown(String onMouseDown) {
		this.onmousedown = onMouseDown;
	}

	/**
	 * Returns the onMouseDown event handler.
	 */
	public String getOnmousedown() {
		return onmousedown;
	}

	/**
	 * Sets the onMouseUp event handler.
	 */
	public void setOnmouseup(String onMouseUp) {
		this.onmouseup = onMouseUp;
	}

	/**
	 * Returns the onMouseUp event handler.
	 */
	public String getOnmouseup() {
		return onmouseup;
	}

	/**
	 * Sets the onMouseMove event handler.
	 */
	public void setOnmousemove(String onMouseMove) {
		this.onmousemove = onMouseMove;
	}

	/**
	 * Returns the onMouseMove event handler.
	 */
	public String getOnmousemove() {
		return onmousemove;
	}

	/**
	 * Sets the onMouseOver event handler.
	 */
	public void setOnmouseover(String onMouseOver) {
		this.onmouseover = onMouseOver;
	}

	/**
	 * Returns the onMouseOver event handler.
	 */
	public String getOnmouseover() {
		return onmouseover;
	}

	/**
	 * Sets the onMouseOut event handler.
	 */
	public void setOnmouseout(String onMouseOut) {
		this.onmouseout = onMouseOut;
	}

	/**
	 * Returns the onMouseOut event handler.
	 */
	public String getOnmouseout() {
		return onmouseout;
	}

	// Keyboard Events

	/**
	 * Sets the onKeyDown event handler.
	 */
	public void setOnkeydown(String onKeyDown) {
		this.onkeydown = onKeyDown;
	}

	/**
	 * Returns the onKeyDown event handler.
	 */
	public String getOnkeydown() {
		return onkeydown;
	}

	/**
	 * Sets the onKeyUp event handler.
	 */
	public void setOnkeyup(String onKeyUp) {
		this.onkeyup = onKeyUp;
	}

	/**
	 * Returns the onKeyUp event handler.
	 */
	public String getOnkeyup() {
		return onkeyup;
	}

	/**
	 * Sets the onKeyPress event handler.
	 */
	public void setOnkeypress(String onKeyPress) {
		this.onkeypress = onKeyPress;
	}

	/**
	 * Returns the onKeyPress event handler.
	 */
	public String getOnkeypress() {
		return onkeypress;
	}

	// Text Events

	/**
	 * Sets the onChange event handler.
	 */
	public void setOnchange(String onChange) {
		this.onchange = onChange;
	}

	/**
	 * Returns the onChange event handler.
	 */
	public String getOnchange() {
		return onchange;
	}

	/**
	 * Sets the onSelect event handler.
	 */
	public void setOnselect(String onSelect) {
		this.onselect = onSelect;
	}

	/**
	 * Returns the onSelect event handler.
	 */
	public String getOnselect() {
		return onselect;
	}

	// Focus Events and States

	/**
	 * Sets the onBlur event handler.
	 */
	public void setOnblur(String onBlur) {
		this.onblur = onBlur;
	}

	/**
	 * Returns the onBlur event handler.
	 */
	public String getOnblur() {
		return onblur;
	}

	/**
	 * Sets the onFocus event handler.
	 */
	public void setOnfocus(String onFocus) {
		this.onfocus = onFocus;
	}

	/**
	 * Returns the onFocus event handler.
	 */
	public String getOnfocus() {
		return onfocus;
	}

	/**
	 * Sets the disabled event handler.
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * Returns the disabled event handler.
	 */
	public boolean getDisabled() {
		return disabled;
	}

	/**
	 * Sets the readonly event handler.
	 */
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	/**
	 * Returns the readonly event handler.
	 */
	public boolean getReadonly() {
		return readonly;
	}

	// CSS Style Support

	/**
	 * Sets the style attribute.
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Returns the style attribute.
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Sets the style class attribute.
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	/**
	 * Returns the style class attribute.
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * Sets the style id attribute.
	 */
	public void setStyleId(String styleId) {
		this.styleId = styleId;
	}

	/**
	 * Returns the style id attribute.
	 */
	public String getStyleId() {
		return styleId;
	}

	// /**
	// * Returns the error key attribute.
	// */
	// public String getErrorKey() {
	// return errorKey;
	// }
	//
	// /**
	// * Sets the error key attribute.
	// */
	// public void setErrorKey(String errorKey) {
	// this.errorKey = errorKey;
	// }

	/**
	 * Returns the error style attribute.
	 */
	public String getErrorStyle() {
		return errorStyle;
	}

	/**
	 * Sets the error style attribute.
	 */
	public void setErrorStyle(String errorStyle) {
		this.errorStyle = errorStyle;
	}

	/**
	 * Returns the error style class attribute.
	 */
	public String getErrorStyleClass() {
		return errorStyleClass;
	}

	/**
	 * Sets the error style class attribute.
	 */
	public void setErrorStyleClass(String errorStyleClass) {
		this.errorStyleClass = errorStyleClass;
	}

	/**
	 * Returns the error style id attribute.
	 */
	public String getErrorStyleId() {
		return errorStyleId;
	}

	/**
	 * Sets the error style id attribute.
	 */
	public void setErrorStyleId(String errorStyleId) {
		this.errorStyleId = errorStyleId;
	}

	// Other Common Elements

	/**
	 * Returns the alternate text attribute.
	 */
	public String getAlt() {
		return alt;
	}

	/**
	 * Sets the alternate text attribute.
	 */
	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * Returns the message resources key of the alternate text.
	 */
	public String getAltKey() {
		return altKey;
	}

	/**
	 * Sets the message resources key of the alternate text.
	 */
	public void setAltKey(String altKey) {
		this.altKey = altKey;
	}

	/**
	 * Returns the name of the message resources bundle to use.
	 */
	public String getBundle() {
		return bundle;
	}

	/**
	 * Sets the name of the message resources bundle to use.
	 */
	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	// /**
	// * Returns the name of the session attribute for our locale.
	// */
	// public String getLocale() {
	// return locale;
	// }
	//
	// /**
	// * Sets the name of the session attribute for our locale.
	// */
	// public void setLocale(String locale) {
	// this.locale = locale;
	// }

	/**
	 * Returns the advisory title attribute.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the advisory title attribute.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the message resources key of the advisory title.
	 */
	public String getTitleKey() {
		return titleKey;
	}

	/**
	 * Sets the message resources key of the advisory title.
	 */
	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	/**
	 * Returns the language code of this element.
	 * 
	 * @since 1.3.6
	 */
	public String getLang() {
		return this.lang;
	}

	/**
	 * Sets the language code of this element.
	 * 
	 * @since 1.3.6
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * Returns the direction for weak/neutral text this element.
	 * 
	 * @since 1.3.6
	 */
	public String getDir() {
		return this.dir;
	}

	/**
	 * Sets the direction for weak/neutral text of this element.
	 * 
	 * @since 1.3.6
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}

	private boolean isXhtml() {
		return false;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getDicOptions() {
		return dicOptions;
	}

	public void setDicOptions(String dicOptions) {
		this.dicOptions = dicOptions;
	}

}
