package com.jsoft.cocit.util;

import java.util.Properties;

/**
 * Key Value 配对值，如下拉框选择项中，key即为Label。
 * 
 * @author jiongs753
 * 
 * @param <T>
 */
public class Option {
	private String text;

	private String value;

	private Properties extProps;

	public static Option make(String text, String value) {
		return new Option(text, value);
	}

	public Option() {

	}

	private Option(String key, String value) {
		this.text = key;
		this.value = value;

		extProps = new Properties();
	}

	public String getText() {
		return text;
	}

	public void setText(String key) {
		this.text = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public <T> T get(String propName, T defaultReturn) {
		String value = extProps.getProperty(propName);

		if (value == null)
			return defaultReturn;
		if (defaultReturn == null)
			return null;

		Class valueType = defaultReturn.getClass();

		try {
			return (T) StringUtil.castTo(value, valueType);
		} catch (Throwable e) {
			LogUtil.warn("", e);
		}

		return defaultReturn;
	}

	public Option set(String propName, String value) {
		extProps.put(propName, value);

		return this;
	}
}
