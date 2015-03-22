package com.jsoft.cocit.orm.mapping;

public interface EnColumnMapping {
	EnMapping getMapping();

	String getColumnName();

	String getFieldName();

	String getDefaultValue(Object object);

	String getSqlType();

	int getScale();

	int getLength();

	int getPrecision();

	String getGenerator();
}
