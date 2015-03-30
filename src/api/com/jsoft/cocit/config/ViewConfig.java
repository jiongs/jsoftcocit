package com.jsoft.cocit.config;

import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.constant.ConfigKeys;
import com.jsoft.cocit.constant.ViewNames;

/**
 * 视图配置项
 * 
 * @author Ji Yongshan
 * 
 */
public class ViewConfig implements ConfigKeys {
	private ICocConfig config;
	private Map<String, Object> cache;

	public ViewConfig(ICocConfig cocConfig) {
		this.config = cocConfig;
		cache = new HashMap();

		this.init();
	}

	private void init() {
		String item;

		/*
		 * 计算弹出窗口按钮位置
		 */
		item = getDialogButtonPosition();
		if ("bottom".equals(item)) {
			cache.put("formButtonsCSS", "dialog-buttons");
		} else {// top
			cache.put("formButtonsCSS", "dialog-toolbar");
		}

		/*
		 * 计算数据网格行操作视图名称
		 */
		item = getButtonStyleForGridCell();
		if ("link".equals(item)) {
			cache.put("cellViewForRowActions", ViewNames.CELL_VIEW_LINK_FOR_ROWACTIONS);
		} else {
			cache.put("cellViewForRowActions", ViewNames.CELL_VIEW_BUTTON_FOR_ROWACTIONS);
		}

		/*
		 * 计算数据网格操作视图名称
		 */
		item = getButtonStyle();
		if ("button".equals(item)) {
			cache.put("viewForGridActions", ViewNames.VIEW_ACTIONS_BUTTON);
		} else {
			cache.put("viewForGridActions", ViewNames.VIEW_ACTIONS_LINKBUTTON);
		}
	}

	/**
	 * 获取配置项（view.dialog_button_position）内容
	 */
	public String getDialogButtonPosition() {
		return config.get(VIEW_DIALOG_BUTTON_POSITION, "bottom");
	}

	/**
	 * 获取配置项（view.button_style）内容
	 */
	public String getButtonStyle() {
		return config.get(VIEW_BUTTON_STYLE, "button");
	}

	/**
	 * 获取配置项（view.button_style_for_gridcell）内容
	 */
	public String getButtonStyleForGridCell() {
		return config.get(VIEW_BUTTON_STYLE_FOR_GRIDCELL, "link");
	}

	/**
	 * 获取表单按扭CSS：用来控制表单按钮的显示位置。
	 * 
	 * @return
	 */
	public String getFormButtonsCSS() {
		return (String) cache.get("formButtonsCSS");
	}

	/**
	 * 获取数据网格行操作视图名称
	 * 
	 * @return
	 */
	public String getCellViewForRowActions() {
		return (String) cache.get("cellViewForRowActions");
	}

	/**
	 * 获取数据网格操作视图名称
	 * 
	 * @return
	 */
	public String getViewForGridActions() {
		return (String) cache.get("viewForGridActions");
	}
}
