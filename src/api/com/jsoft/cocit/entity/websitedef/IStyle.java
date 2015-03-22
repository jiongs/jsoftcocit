package com.jsoft.cocit.entity.websitedef;

public interface IStyle {
	public Long getId();

	/**
	 * 获取样式名字，用作html元素的class属性
	 * 
	 * @return
	 */
	public String getCssClass();

	/**
	 * 获取样式内容，用于生成&lt;style&gt;...&lt;style&gt;的内容部分。
	 * 
	 * @return
	 */
	public String getCssStyle();

	public static class SimpleStyle implements IStyle {

		private Long id;
		private String cssClass;
		private String cssStyle;

		public SimpleStyle(Long id, String cssClass, String cssStyle) {
			this.id = id;
			this.cssClass = cssClass;
			this.cssStyle = cssStyle;
		}

		
		public Long getId() {
			return id;
		}

		
		public String getCssClass() {
			return cssClass;
		}

		
		public String getCssStyle() {
			return cssStyle;
		}

	}
}
