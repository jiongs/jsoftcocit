package com.jsoft.cocit.config;

import java.util.List;

import com.jsoft.cocit.dmengine.IDataModelEngine;
import com.jsoft.cocit.dmengine.IPatternAdapters;
import com.jsoft.cocit.ui.UIViews;

public interface IBeansConfig {

	/**
	 * {@link IDataModelEngine}所需配置
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
	 * {@link IPatternAdapters}所需配置
	 * 
	 * @return
	 */
	List<String> getPatternAdapters();

	List<String> getCommandInterceptors();

	List<String> getEntityGenerators();

	List<String> getActionPackages();
}
