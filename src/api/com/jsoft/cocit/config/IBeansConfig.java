package com.jsoft.cocit.config;

import java.util.List;

import com.jsoft.cocimpl.ui.UIViews;
import com.jsoft.cocit.entityengine.EntityEngine;
import com.jsoft.cocit.entityengine.PatternAdapters;

public interface IBeansConfig {

	/**
	 * {@link EntityEngine}所需配置
	 * 
	 * @return
	 */
	List<String> getEntityPackages();

	/**
	 * {@link UIViews}所需配置
	 * 
	 * @return
	 */
	List<String> getFieldViews();

	/**
	 * {@link UIViews}所需配置
	 * 
	 * @return
	 */
	List<String> getViews();

	List<String> getCellViews();

	/**
	 * {@link PatternAdapters}所需配置
	 * 
	 * @return
	 */
	List<String> getPatternAdapters();

	List<String> getEntityGenerators();

	List<String> getActionPackages();
}
