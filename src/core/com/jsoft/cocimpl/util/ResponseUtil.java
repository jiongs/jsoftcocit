/*
 * $Id: ResponseUtils.java 471754 2006-11-06 14:55:09Z husted $
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
package com.jsoft.cocimpl.util;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.LogUtil;

/**
 * General purpose utility methods related to generating a servlet response in the Struts controller framework.
 * 
 */
public abstract class ResponseUtil {
	// ------------------------------------------------------- Static Variables

	/**
	 * Java 1.4 encode method to use instead of deprecated 1.3 version.
	 */
	private static Method encode = null;

	/**
	 * Initialize the encode variable with the Java 1.4 method if available.
	 */
	static {
		try {
			// get version of encode method with two String args
			Class[] args = new Class[] { String.class, String.class };

			encode = URLEncoder.class.getMethod("encode", args);
		} catch (NoSuchMethodException e) {
			LogUtil.debug("Could not find Java 1.4 encode method.  Using deprecated version.", e);
		}
	}

	// --------------------------------------------------------- Public Methods

	/**
	 * Filter the specified string for characters that are senstive to HTML interpreters, returning the string with these characters replaced by the corresponding character entities.
	 * 
	 * @param value
	 *            The string to be filtered and returned
	 */
	public static String escapeHtml(String value) {
		if ((value == null) || (value.length() == 0)) {
			return value;
		}

		StringBuffer result = null;
		String filtered = null;

		for (int i = 0; i < value.length(); i++) {
			filtered = null;

			switch (value.charAt(i)) {
			case '<':
				filtered = "&lt;";

				break;

			case '>':
				filtered = "&gt;";

				break;

			case '&':
				filtered = "&amp;";

				break;

			case '"':
				filtered = "&quot;";

				break;

			case '\'':
				filtered = "&#39;";

				break;
			}

			if (result == null) {
				if (filtered != null) {
					result = new StringBuffer(value.length() + 50);

					if (i > 0) {
						result.append(value.substring(0, i));
					}

					result.append(filtered);
				}
			} else {
				if (filtered == null) {
					result.append(value.charAt(i));
				} else {
					result.append(filtered);
				}
			}
		}

		return (result == null) ? value : result.toString();
	}

	/**
	 * URLencodes a string assuming the character encoding is UTF-8.
	 * 
	 * @param url
	 * @return String The encoded url in UTF-8
	 */
	public static String encodeURL(String url) {
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
	@SuppressWarnings("deprecation")
	public static String encodeURL(String url, String enc) {
		try {
			if ((enc == null) || (enc.length() == 0)) {
				enc = "UTF-8";
			}

			// encode url with new 1.4 method and UTF-8 encoding
			if (encode != null) {
				return (String) encode.invoke(null, new Object[] { url, enc });
			}
		} catch (IllegalAccessException e) {
			LogUtil.debug("Could not find Java 1.4 encode method.  Using deprecated version.", e);
		} catch (InvocationTargetException e) {
			LogUtil.debug("Could not find Java 1.4 encode method. Using deprecated version.", e);
		}

		return URLEncoder.encode(url);
	}

	public static String makeJson(int statusCode, String message, Map context) {
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		sb.append("\"statusCode\": ").append(statusCode);
		sb.append(", \"success\": ").append(statusCode == Const.RESPONSE_CODE_SUCCESS);
		sb.append(", \"message\": ").append(JsonUtil.toJson(message));

		if (context != null) {
			Iterator<String> keys = context.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				Object value = context.get(key);
				sb.append(", \"" + key + "\": ").append(JsonUtil.toJson(value));
			}
		}

		sb.append('}');

		return sb.toString();
	}

	public static void writeError(Writer out, Throwable ex) throws IOException {
		out.write("<div style=\"color: red; font-size: 14px;\">");
		out.write(ExceptionUtil.msg(ex));
		out.write("</div>");
	}

}
