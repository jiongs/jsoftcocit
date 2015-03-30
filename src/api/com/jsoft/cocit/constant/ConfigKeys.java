package com.jsoft.cocit.constant;

public interface ConfigKeys {

	/**
	 * 该属性用来统一配置开发平台自动生成的表单操作按钮的显示位置，可选值：<br/>
	 * bottom：按钮显示在窗口底部<br/>
	 * top：按钮显示在窗口顶部<br/>
	 */
	public static final String VIEW_DIALOG_BUTTON_POSITION = "view.dialog_button_position";

	/**
	 * 
	 * 该属性用来统一配置DataGrid行操作按钮风格，可选值：<br/>
	 * link：链接按钮(A)<br/>
	 * button：普通按钮(BUTTON)<br/>
	 */
	public static final String VIEW_BUTTON_STYLE_FOR_GRIDCELL = "view.button_style_for_gridcell";

	/**
	 * 该属性用来统一配置实体操作按钮风格，可选值：<br/>
	 * button：普通按钮(BUTTON)，不支持小图标、不支持下拉。<br/>
	 * linkbutton：圆弧形风格(A class="jCocit-ui jCocit-button")，支持小图标、不支持下拉。<br/>
	 */
	public static final String VIEW_BUTTON_STYLE = "view.button_style";
}
