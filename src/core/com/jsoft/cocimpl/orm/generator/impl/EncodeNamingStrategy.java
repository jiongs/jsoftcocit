package com.jsoft.cocimpl.orm.generator.impl;

import com.jsoft.cocimpl.orm.generator.INamingStrategy;

public class EncodeNamingStrategy extends SimpleNamingStrategy {
	public static final String TABLE_PREFIX = "COC_T_";

	public static final String FIELD_PREFIX = "COC_F_";

	// protected static List<String> ENCODE_COLUMN_NAMES;
	//
	// protected static List<String> ENCODE_TABLE_NAMES;

	private static INamingStrategy me;

	public static INamingStrategy get() {
		if (me == null) {
			me = new EncodeNamingStrategy();
		}

		return me;
	}

	private EncodeNamingStrategy() {
	}

	private String encodeTable(String s) {
		return TABLE_PREFIX + Integer.toHexString(s.hashCode()).toUpperCase();
	}

	private String encodeField(String s) {
		return FIELD_PREFIX + Integer.toHexString(s.hashCode()).toUpperCase();
	}

	public String tableNameFromClassName(String className) {
		return encodeTable(super.tableNameFromClassName(className));
	}

	public String propertyToColumnName(String propertyName) {
		return encodeField(super.propertyToColumnName(propertyName));
	}

	public String tableName(String tableName) {
		return encodeTable(super.tableName(tableName));
	}

	public String columnName(String columnName) {
		return encodeField(super.columnName(columnName));
	}

	public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable, String propertyName) {
		return encodeTable(super.collectionTableName(ownerEntity, ownerEntityTable, associatedEntity, associatedEntityTable, propertyName));
	}

	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		return encodeField(super.joinKeyColumnName(joinedColumn, joinedTable));
	}

	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {

		return encodeField(super.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName, referencedColumnName));
	}

	public String logicalColumnName(String columnName, String propertyName) {
		return encodeField(super.logicalColumnName(columnName, propertyName));
	}

	public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {
		return encodeTable(super.logicalCollectionTableName(tableName, ownerEntityTable, associatedEntityTable, propertyName));
	}

	public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
		return encodeField(super.logicalCollectionColumnName(columnName, propertyName, referencedColumn));
	}

	public String fkName(String entityName, String columnName, String refrenceName) {
		return encodeField(super.fkName(entityName, columnName, refrenceName));
	}
}
