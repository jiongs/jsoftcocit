/*
 * $Id: TagUtils.java 471754 2006-11-06 14:55:09Z husted $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.jsoft.cocimpl.ui.tag;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;

import com.jsoft.cocimpl.util.RequestUtil;
import com.jsoft.cocimpl.util.ResponseUtil;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.ObjectUtil;

/**
 * Provides helper methods for JSP tags.
 * 
 */
public class TagUtils {
	/**
	 * The Singleton instance.
	 * 
	 * @since 1.3.5 Changed to non-final so it may be overridden, use at your own risk (you've been warned!!)
	 */
	private static TagUtils instance = new TagUtils();

	//
	// /**
	// * The message resources for this package. TODO We need to move the relevant messages out of this properties file.
	// */
	// private static final MessageResources messages = MessageResources.getMessageResources("org.apache.struts.taglib.LocalStrings");
	/**
	 * The message resources for this package.
	 */
	private static IMessageConfig messages = Cocit.me().getMessages();

	/**
	 * Maps lowercase JSP scope names to their PageContext integer constant values.
	 */
	private static final Map scopes = new HashMap();

	/**
	 * Initialize the scope names map and the encode variable with the Java 1.4 method if available.
	 */
	static {
		scopes.put("page", new Integer(PageContext.PAGE_SCOPE));
		scopes.put("request", new Integer(PageContext.REQUEST_SCOPE));
		scopes.put("session", new Integer(PageContext.SESSION_SCOPE));
		scopes.put("application", new Integer(PageContext.APPLICATION_SCOPE));
	}

	/**
	 * Constructor for TagUtils.
	 */
	protected TagUtils() {
		super();
	}

	/**
	 * Returns the Singleton instance of TagUtils.
	 */
	public static TagUtils getInstance() {
		return instance;
	}

	/**
	 * Set the instance. This blatently violates the Singleton pattern, but then some say Singletons are an anti-pattern.
	 * 
	 * @since 1.3.5 Changed to non-final and added setInstance() so TagUtils may be overridden, use at your own risk (you've been warned!!)
	 * @param instance
	 *            The instance to set.
	 */
	public static void setInstance(TagUtils instance) {
		TagUtils.instance = instance;
	}

	/**
	 * Compute a set of query parameters that will be dynamically added to a generated URL. The returned Map is keyed by parameter name, and the values are either null (no value specified), a String (single value specified), or a String[] array
	 * (multiple values specified). Parameter names correspond to the corresponding attributes of the <code>&lt;html:link&gt;</code> tag. If no query parameters are identified, return <code>null</code>.
	 * 
	 * @param pageContext
	 *            PageContext we are operating in
	 * @param paramId
	 *            Single-value request parameter name (if any)
	 * @param paramName
	 *            Bean containing single-value parameter value
	 * @param paramProperty
	 *            Property (of bean named by <code>paramName</code> containing single-value parameter value
	 * @param paramScope
	 *            Scope containing bean named by <code>paramName</code>
	 * @param name
	 *            Bean containing multi-value parameters Map (if any)
	 * @param property
	 *            Property (of bean named by <code>name</code> containing multi-value parameters Map
	 * @param scope
	 *            Scope containing bean named by <code>name</code>
	 * @param transaction
	 *            Should we add our transaction control token?
	 * @return Map of query parameters
	 * @throws JspException
	 *             if we cannot look up the required beans
	 * @throws JspException
	 *             if a class cast exception occurs on a looked-up bean or property
	 */
	public Map computeParameters(PageContext pageContext, String paramId, String paramName, String paramProperty, String paramScope, String name, String property, String scope, boolean transaction) throws JspException {
		// Short circuit if no parameters are specified
		if ((paramId == null) && (name == null) && !transaction) {
			return (null);
		}

		// Locate the Map containing our multi-value parameters map
		Map map = null;

		try {
			if (name != null) {
				map = (Map) getInstance().lookup(pageContext, name, property, scope);
			}

			// @TODO - remove this - it is never thrown
			// } catch (ClassCastException e) {
			// saveException(pageContext, e);
			// throw new JspException(
			// messages.getMessage("parameters.multi", name, property, scope));
		} catch (JspException e) {
			saveException(pageContext, e);
			throw e;
		}

		// Create a Map to contain our results from the multi-value parameters
		Map results = null;

		if (map != null) {
			results = new HashMap(map);
		} else {
			results = new HashMap();
		}

		// Add the single-value parameter (if any)
		if ((paramId != null) && (paramName != null)) {
			Object paramValue = null;

			try {
				paramValue = TagUtils.getInstance().lookup(pageContext, paramName, paramProperty, paramScope);
			} catch (JspException e) {
				saveException(pageContext, e);
				throw e;
			}

			if (paramValue != null) {
				String paramString = null;

				if (paramValue instanceof String) {
					paramString = (String) paramValue;
				} else {
					paramString = paramValue.toString();
				}

				Object mapValue = results.get(paramId);

				if (mapValue == null) {
					results.put(paramId, paramString);
				} else if (mapValue instanceof String[]) {
					String[] oldValues = (String[]) mapValue;
					String[] newValues = new String[oldValues.length + 1];

					System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
					newValues[oldValues.length] = paramString;
					results.put(paramId, newValues);
				} else {
					String[] newValues = new String[2];

					newValues[0] = mapValue.toString();
					newValues[1] = paramString;
					results.put(paramId, newValues);
				}
			}
		}

		// Add our transaction control token (if requested)
		if (transaction) {
			HttpSession session = pageContext.getSession();
			String token = null;

			if (session != null) {
				token = (String) session.getAttribute(ViewKeys.TRANSACTION_TOKEN_KEY);
			}

			if (token != null) {
				results.put(ViewKeys.TOKEN_KEY, token);
			}
		}

		// Return the completed Map
		return (results);
	}

	public String computeURL(PageContext pageContext, String forward, String href, String page, String action, String module, Map params, String anchor, boolean redirect) throws MalformedURLException {
		return this.computeURLWithCharEncoding(pageContext, forward, href, page, action, module, params, anchor, redirect, false);
	}

	/**
	 * Compute a hyperlink URL based on the <code>forward</code>, <code>href</code>, <code>action</code> or <code>page</code> parameter that is not null. The returned URL will have already been passed to <code>response.encodeURL()</code> for adding a
	 * session identifier.
	 * 
	 * @param pageContext
	 *            PageContext for the tag making this call
	 * @param forward
	 *            Logical forward name for which to look up the context-relative URI (if specified)
	 * @param href
	 *            URL to be utilized unmodified (if specified)
	 * @param page
	 *            Module-relative page for which a URL should be created (if specified)
	 * @param action
	 *            Logical action name for which to look up the context-relative URI (if specified)
	 * @param params
	 *            Map of parameters to be dynamically included (if any)
	 * @param anchor
	 *            Anchor to be dynamically included (if any)
	 * @param redirect
	 *            Is this URL for a <code>response.sendRedirect()</code>?
	 * @return URL with session identifier
	 * @throws java.net.MalformedURLException
	 *             if a URL cannot be created for the specified parameters
	 */
	public String computeURLWithCharEncoding(PageContext pageContext, String forward, String href, String page, String action, String module, Map params, String anchor, boolean redirect, boolean useLocalEncoding) throws MalformedURLException {
		return computeURLWithCharEncoding(pageContext, forward, href, page, action, module, params, anchor, redirect, true, useLocalEncoding);
	}

	public String computeURL(PageContext pageContext, String forward, String href, String page, String action, String module, Map params, String anchor, boolean redirect, boolean encodeSeparator) throws MalformedURLException {
		return computeURLWithCharEncoding(pageContext, forward, href, page, action, module, params, anchor, redirect, encodeSeparator, false);
	}

	/**
	 * Compute a hyperlink URL based on the <code>forward</code>, <code>href</code>, <code>action</code> or <code>page</code> parameter that is not null. The returned URL will have already been passed to <code>response.encodeURL()</code> for adding a
	 * session identifier.
	 * 
	 * @param pageContext
	 *            PageContext for the tag making this call
	 * @param forward
	 *            Logical forward name for which to look up the context-relative URI (if specified)
	 * @param href
	 *            URL to be utilized unmodified (if specified)
	 * @param page
	 *            Module-relative page for which a URL should be created (if specified)
	 * @param action
	 *            Logical action name for which to look up the context-relative URI (if specified)
	 * @param params
	 *            Map of parameters to be dynamically included (if any)
	 * @param anchor
	 *            Anchor to be dynamically included (if any)
	 * @param redirect
	 *            Is this URL for a <code>response.sendRedirect()</code>?
	 * @param encodeSeparator
	 *            This is only checked if redirect is set to false (never encoded for a redirect). If true, query string parameter separators are encoded as &gt;amp;, else &amp; is used.
	 * @param useLocalEncoding
	 *            If set to true, urlencoding is done on the bytes of character encoding from ServletResponse#getCharacterEncoding. Use UTF-8 otherwise.
	 * @return URL with session identifier
	 * @throws java.net.MalformedURLException
	 *             if a URL cannot be created for the specified parameters
	 */
	public String computeURLWithCharEncoding(PageContext pageContext, String forward, String href, String page, String action, String module, Map params, String anchor, boolean redirect, boolean encodeSeparator, boolean useLocalEncoding)
			throws MalformedURLException {
		String charEncoding = "UTF-8";

		if (useLocalEncoding) {
			charEncoding = pageContext.getResponse().getCharacterEncoding();
		}

		// TODO All the computeURL() methods need refactoring!
		// Validate that exactly one specifier was included
		int n = 0;

		if (forward != null) {
			n++;
		}

		if (href != null) {
			n++;
		}

		if (page != null) {
			n++;
		}

		if (action != null) {
			n++;
		}

		if (n != 1) {
			throw new MalformedURLException(messages.get("computeURL.specifier"));
		}

		// Calculate the appropriate URL
		StringBuffer url = new StringBuffer();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		if (href != null) {
			url.append(href);
		} else if (action != null) {
			url.append(action);
		} else /* if (page != null) */
		{
			url.append(request.getContextPath());
			// url.append(this.pageURL(request, page, moduleConfig));
		}

		// Add anchor if requested (replacing any existing anchor)
		if (anchor != null) {
			String temp = url.toString();
			int hash = temp.indexOf('#');

			if (hash >= 0) {
				url.setLength(hash);
			}

			url.append('#');
			url.append(this.encodeURL(anchor, charEncoding));
		}

		// Add dynamic parameters if requested
		if ((params != null) && (params.size() > 0)) {
			// Save any existing anchor
			String temp = url.toString();
			int hash = temp.indexOf('#');

			if (hash >= 0) {
				anchor = temp.substring(hash + 1);
				url.setLength(hash);
				temp = url.toString();
			} else {
				anchor = null;
			}

			// Define the parameter separator
			String separator = null;

			if (redirect) {
				separator = "&";
			} else if (encodeSeparator) {
				separator = "&amp;";
			} else {
				separator = "&";
			}

			// Add the required request parameters
			boolean question = temp.indexOf('?') >= 0;
			Iterator keys = params.keySet().iterator();

			while (keys.hasNext()) {
				String key = (String) keys.next();
				Object value = params.get(key);

				if (value == null) {
					if (!question) {
						url.append('?');
						question = true;
					} else {
						url.append(separator);
					}

					url.append(this.encodeURL(key, charEncoding));
					url.append('='); // Interpret null as "no value"
				} else if (value instanceof String) {
					if (!question) {
						url.append('?');
						question = true;
					} else {
						url.append(separator);
					}

					url.append(this.encodeURL(key, charEncoding));
					url.append('=');
					url.append(this.encodeURL((String) value, charEncoding));
				} else if (value instanceof String[]) {
					String[] values = (String[]) value;

					for (int i = 0; i < values.length; i++) {
						if (!question) {
							url.append('?');
							question = true;
						} else {
							url.append(separator);
						}

						url.append(this.encodeURL(key, charEncoding));
						url.append('=');
						url.append(this.encodeURL(values[i], charEncoding));
					}
				} else /* Convert other objects to a string */
				{
					if (!question) {
						url.append('?');
						question = true;
					} else {
						url.append(separator);
					}

					url.append(this.encodeURL(key, charEncoding));
					url.append('=');
					url.append(this.encodeURL(value.toString(), charEncoding));
				}
			}

			// Re-add the saved anchor (if any)
			if (anchor != null) {
				url.append('#');
				url.append(this.encodeURL(anchor, charEncoding));
			}
		}

		// Perform URL rewriting to include our session ID (if any)
		// but only if url is not an external URL
		if ((href == null) && (pageContext.getSession() != null)) {
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

			if (redirect) {
				return (response.encodeRedirectURL(url.toString()));
			}

			return (response.encodeURL(url.toString()));
		}

		return (url.toString());
	}

	/**
	 * URLencodes a string assuming the character encoding is UTF-8.
	 * 
	 * @param url
	 * @return String The encoded url in UTF-8
	 */
	public String encodeURL(String url) {
		return encodeURL(url, "UTF-8");
	}

	/**
	 * Use the new URLEncoder.encode() method from Java 1.4 if available, else use the old deprecated version. This method uses reflection to find the appropriate method; if the reflection operations throw exceptions, this will return the url encoded
	 * with the old URLEncoder.encode() method.
	 * 
	 * @param enc
	 *            The character encoding the urlencode is performed on.
	 * @return String The encoded url.
	 */
	public String encodeURL(String url, String enc) {
		return ResponseUtil.encodeURL(url, enc);
	}

	/**
	 * Filter the specified string for characters that are senstive to HTML interpreters, returning the string with these characters replaced by the corresponding character entities.
	 * 
	 * @param value
	 *            The string to be filtered and returned
	 */
	public String filter(String value) {
		return ResponseUtil.escapeHtml(value);
	}

	/**
	 * Return the form action converted into an action mapping path. The value of the <code>action</code> property is manipulated as follows in computing the name of the requested mapping:
	 * 
	 * <ul>
	 * 
	 * <li>Any filename extension is removed (on the theory that extension mapping is being used to select the controller servlet).</li>
	 * 
	 * <li>If the resulting value does not start with a slash, then a slash is prepended.</li>
	 * 
	 * </ul>
	 */
	public String getActionMappingName(String action) {
		String value = action;
		int question = action.indexOf("?");

		if (question >= 0) {
			value = value.substring(0, question);
		}

		int pound = value.indexOf("#");

		if (pound >= 0) {
			value = value.substring(0, pound);
		}

		int slash = value.lastIndexOf("/");
		int period = value.lastIndexOf(".");

		if ((period >= 0) && (period > slash)) {
			value = value.substring(0, period);
		}

		return value.startsWith("/") ? value : ("/" + value);
	}

	/**
	 * Converts the scope name into its corresponding PageContext constant value.
	 * 
	 * @param scopeName
	 *            Can be "page", "request", "session", or "application" in any case.
	 * @return The constant representing the scope (ie. PageContext.REQUEST_SCOPE).
	 * @throws JspException
	 *             if the scopeName is not a valid name.
	 */
	public int getScope(String scopeName) throws JspException {
		Integer scope = (Integer) scopes.get(scopeName.toLowerCase());

		if (scope == null) {
			throw new JspException(messages.get("lookup.scope"));
		}

		return scope.intValue();
	}

	/**
	 * Look up and return current user locale, based on the specified parameters.
	 * 
	 * @param pageContext
	 *            The PageContext associated with this request
	 * @param locale
	 *            Name of the session attribute for our user's Locale. If this is <code>null</code>, the default locale key is used for the lookup.
	 * @return current user locale
	 */
	public Locale getUserLocale(PageContext pageContext, String locale) {
		return RequestUtil.getUserLocale((HttpServletRequest) pageContext.getRequest(), locale);
	}

	/**
	 * Returns true if the custom tags are in XHTML mode.
	 */
	public boolean isXhtml(PageContext pageContext) {
		String xhtml = (String) pageContext.getAttribute(ViewKeys.XHTML_KEY, PageContext.PAGE_SCOPE);

		return "true".equalsIgnoreCase(xhtml);
	}

	/**
	 * Locate and return the specified bean, from an optionally specified scope, in the specified page context. If no such bean is found, return <code>null</code> instead. If an exception is thrown, it will have already been saved via a call to
	 * <code>saveException()</code>.
	 * 
	 * @param pageContext
	 *            Page context to be searched
	 * @param name
	 *            Name of the bean to be retrieved
	 * @param scopeName
	 *            Scope to be searched (page, request, session, application) or <code>null</code> to use <code>findAttribute()</code> instead
	 * @return JavaBean in the specified page context
	 * @throws JspException
	 *             if an invalid scope name is requested
	 */
	public Object lookup(PageContext pageContext, String name, String scopeName) throws JspException {
		if (scopeName == null) {
			return pageContext.findAttribute(name);
		}

		try {
			return pageContext.getAttribute(name, instance.getScope(scopeName));
		} catch (JspException e) {
			saveException(pageContext, e);
			throw e;
		}
	}

	/**
	 * Locate and return the specified property of the specified bean, from an optionally specified scope, in the specified page context. If an exception is thrown, it will have already been saved via a call to <code>saveException()</code>.
	 * 
	 * @param pageContext
	 *            Page context to be searched
	 * @param name
	 *            Name of the bean to be retrieved
	 * @param property
	 *            Name of the property to be retrieved, or <code>null</code> to retrieve the bean itself
	 * @param scope
	 *            Scope to be searched (page, request, session, application) or <code>null</code> to use <code>findAttribute()</code> instead
	 * @return property of specified JavaBean
	 * @throws JspException
	 *             if an invalid scope name is requested
	 * @throws JspException
	 *             if the specified bean is not found
	 * @throws JspException
	 *             if accessing this property causes an IllegalAccessException, IllegalArgumentException, InvocationTargetException, or NoSuchMethodException
	 */
	public Object lookup(PageContext pageContext, String name, String property, String scope) throws JspException {
		// Look up the requested bean, and return if requested
		Object bean = lookup(pageContext, name, scope);

		if (bean == null) {
			return null;
		}

		if (property == null) {
			return bean;
		}

		// Locate and return the specified property
		return ObjectUtil.getValue(bean, property);
	}

	/**
	 * Look up and return a message string, based on the specified parameters.
	 * 
	 * @param pageContext
	 *            The PageContext associated with this request
	 * @param bundle
	 *            Name of the servlet context attribute for our message resources bundle
	 * @param locale
	 *            Name of the session attribute for our user's Locale
	 * @param key
	 *            Message key to be looked up and returned
	 * @return message string
	 * @throws JspException
	 *             if a lookup error occurs (will have been saved in the request already)
	 */
	public String message(PageContext pageContext, String bundle, String locale, String key) throws JspException {
		return message(pageContext, bundle, locale, key, null);
	}

	/**
	 * Look up and return a message string, based on the specified parameters.
	 * 
	 * @param pageContext
	 *            The PageContext associated with this request
	 * @param bundle
	 *            Name of the servlet context attribute for our message resources bundle
	 * @param locale
	 *            Name of the session attribute for our user's Locale
	 * @param key
	 *            Message key to be looked up and returned
	 * @param args
	 *            Replacement parameters for this message
	 * @return message string
	 * @throws JspException
	 *             if a lookup error occurs (will have been saved in the request already)
	 */
	public String message(PageContext pageContext, String bundle, String locale, String key, Object[] args) throws JspException {

		// Locale userLocale = getUserLocale(pageContext, locale);
		String message = null;

		if (args == null) {
			message = messages.get(key);
		} else {
			message = messages.getMsg(key, args);
		}

		if ((message == null) && LogUtil.isDebugEnabled()) {
			// log missing key to ease debugging
			LogUtil.debug(messages.getMsg("message.resources", key, bundle, locale));
		}

		return message;
	}

	/**
	 * Return true if a message string for the specified message key is present for the specified <code>Locale</code> and bundle.
	 * 
	 * @param pageContext
	 *            The PageContext associated with this request
	 * @param bundle
	 *            Name of the servlet context attribute for our message resources bundle
	 * @param locale
	 *            Name of the session attribute for our user's Locale
	 * @param key
	 *            Message key to be looked up and returned
	 * @return true if a message string for message key exists
	 * @throws JspException
	 *             if a lookup error occurs (will have been saved in the request already)
	 */
	public boolean present(PageContext pageContext, String bundle, String locale, String key) throws JspException {

		// Locale userLocale = getUserLocale(pageContext, locale);

		return false;
	}

	/**
	 * Save the specified exception as a request attribute for later use.
	 * 
	 * @param pageContext
	 *            The PageContext for the current page
	 * @param exception
	 *            The exception to be saved
	 */
	public void saveException(PageContext pageContext, Throwable exception) {
		pageContext.setAttribute(ViewKeys.EXCEPTION_KEY, exception, PageContext.REQUEST_SCOPE);
	}

	/**
	 * Write the specified text as the response to the writer associated with this page. <strong>WARNING</strong> - If you are writing body content from the <code>doAfterBody()</code> method of a custom tag class that implements <code>BodyTag</code>,
	 * you should be calling <code>writePrevious()</code> instead.
	 * 
	 * @param pageContext
	 *            The PageContext object for this page
	 * @param text
	 *            The text to be written
	 * @throws JspException
	 *             if an input/output error occurs (already saved)
	 */
	public void write(PageContext pageContext, String text) throws JspException {
		JspWriter writer = pageContext.getOut();

		try {
			writer.print(text);
		} catch (IOException e) {
			saveException(pageContext, e);
			throw new JspException(messages.get("write.io", e.toString()));
		}
	}

	/**
	 * Write the specified text as the response to the writer associated with the body content for the tag within which we are currently nested.
	 * 
	 * @param pageContext
	 *            The PageContext object for this page
	 * @param text
	 *            The text to be written
	 * @throws JspException
	 *             if an input/output error occurs (already saved)
	 */
	public void writePrevious(PageContext pageContext, String text) throws JspException {
		JspWriter writer = pageContext.getOut();

		if (writer instanceof BodyContent) {
			writer = ((BodyContent) writer).getEnclosingWriter();
		}

		try {
			writer.print(text);
		} catch (IOException e) {
			saveException(pageContext, e);
			throw new JspException(messages.get("write.io", e.toString()));
		}
	}
}
