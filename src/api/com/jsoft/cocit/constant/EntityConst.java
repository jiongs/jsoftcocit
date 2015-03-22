package com.jsoft.cocit.constant;

interface EntityConst {

	/**
	 * 当没有指定String长度时，使用该默认值作为String字段长度。
	 */
	static final int ANN_COLUMN_DEFAULT_LENGTH = 50;

	/**
	 * String输入框最小长度：即当String长度大于该值时，自动将其转换成Text显示框。
	 */
	static final int STRING_BOX_MIN_LENGTH = 200;

	static final String[] ENTITY_PACKAGES = new String[] { "com.jsoft.cocit.entity.impl" };
}
