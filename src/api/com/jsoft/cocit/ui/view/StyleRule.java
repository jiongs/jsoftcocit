package com.jsoft.cocit.ui.view;

import java.util.Map;

/**
 * 该类用来接收实体定义中的条件样式
 * 
 */
public class StyleRule {
	/*
	 * 不能被加。
	 */
	protected Map where;// 条件规则：如果GRID行数据与该条件规则匹配，则指定的样式将被应用
	protected String style;// 样式：如果满足GRID行数据满足上述where条件，则将使用该样式。

	public Map getWhere() {
		return where;
	}

	public void setWhere(Map where) {
		this.where = where;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
