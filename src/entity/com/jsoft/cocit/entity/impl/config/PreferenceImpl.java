package com.jsoft.cocit.entity.impl.config;

import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.config.IPreference;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public abstract class PreferenceImpl extends NamedEntity implements IPreference {

	private String value;

	private String description;

	public String getText() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return getText();
	}

	public String getKey() {
		return getValue();
	}
}
