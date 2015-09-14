package com.jsoft.cocit.constant;

public interface FieldTypes {

	// =======================================
	// 数据字段类型
	// =======================================
	/*
	 * 数字类型
	 */
	static final int FIELD_TYPE_BYTE = 1;

	static final int FIELD_TYPE_SHORT = 2;

	static final int FIELD_TYPE_INTEGER = 3;

	static final int FIELD_TYPE_LONG = 4;

	static final int FIELD_TYPE_NUMBER = 5;

	static final int FIELD_TYPE_FLOAT = 6;

	static final int FIELD_TYPE_DOUBLE = 7;

	static final int FIELD_TYPE_DECIMAL = 8;

	/*
	 * 文本类型
	 */
	static final int FIELD_TYPE_STRING = 20;

	static final int FIELD_TYPE_TEXT = 21;

	static final int FIELD_TYPE_RICHTEXT = 22;

	static final int FIELD_TYPE_UPLOAD = 23;

	/*
	 * 其他类型
	 */
	static final int FIELD_TYPE_BOOLEAN = 50;

	static final int FIELD_TYPE_DATE = 51;

	/*
	 * 外键类型
	 */
	static final int FIELD_TYPE_FK = 90;

	static final int FIELD_TYPE_FK_REDUNDANT = 91;

	static final int FIELD_TYPE_ONE2MANY = 101;

	static final int FIELD_TYPE_EXT = 199;

}
