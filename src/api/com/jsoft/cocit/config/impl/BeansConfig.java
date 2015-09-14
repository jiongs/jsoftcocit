package com.jsoft.cocit.config.impl;

import java.util.List;

import com.jsoft.cocit.config.IBeansConfig;

public class BeansConfig implements IBeansConfig {

	private List<String> entityPackages;
	private List<String> fieldViews;
	private List<String> views;
	private List<String> cellViews;
	private List<String> patternAdapters;
	private List<String> commandInterceptors;
	private List<String> entityGenerators;
	private List<String> actionPackages;

	public List<String> getEntityPackages() {
		return entityPackages;
	}

	public void setEntityPackages(List<String> entityPackages) {
		this.entityPackages = entityPackages;
	}

	public List<String> getFieldViews() {
		return fieldViews;
	}

	@Override
	public List<String> getViews() {
		return views;
	}

	public void setFieldViews(List<String> fieldViews) {
		this.fieldViews = fieldViews;
	}

	public void setViews(List<String> views) {
		this.views = views;
	}

	public List<String> getPatternAdapters() {
		return patternAdapters;
	}

	public void setPatternAdapters(List<String> patternAdapters) {
		this.patternAdapters = patternAdapters;
	}

	public List<String> getCellViews() {
		return cellViews;
	}

	public void setCellViews(List<String> cellViews) {
		this.cellViews = cellViews;
	}

	public List<String> getEntityGenerators() {
		return entityGenerators;
	}

	public void setEntityGenerators(List<String> entityListeners) {
		this.entityGenerators = entityListeners;
	}

	public List<String> getActionPackages() {
		return actionPackages;
	}

	public void setActionPackages(List<String> actionPackages) {
		this.actionPackages = actionPackages;
	}

	public List<String> getCommandInterceptors() {
		return commandInterceptors;
	}

	public void setCommandInterceptors(List<String> commandReceivers) {
		this.commandInterceptors = commandReceivers;
	}
}
