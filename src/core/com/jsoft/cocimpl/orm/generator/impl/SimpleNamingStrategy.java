package com.jsoft.cocimpl.orm.generator.impl;

import com.jsoft.cocimpl.orm.generator.INamingStrategy;
import com.jsoft.cocit.util.StringUtil;

public class SimpleNamingStrategy implements INamingStrategy {
	private static INamingStrategy me;

	protected SimpleNamingStrategy() {
	}

	public static INamingStrategy get() {
		if (me == null) {
			me = new SimpleNamingStrategy();
		}

		return me;
	}

	public String tableNameFromClassName(String className) {
		return StringUtil.addUnderscores(StringUtil.unqualify(className));
	}

	public String propertyToColumnName(String propertyName) {
		return StringUtil.addUnderscores(StringUtil.unqualify(propertyName));
	}

	public String tableName(String tableName) {
		return StringUtil.addUnderscores(tableName);
	}

	public String columnName(String columnName) {
		return StringUtil.addUnderscores(columnName);
	}

	public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable, String propertyName) {
		return tableName(ownerEntityTable + '_' + propertyToColumnName(propertyName));
	}

	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		return columnName(joinedColumn);
	}

	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
		String header = propertyName != null ? StringUtil.unqualify(propertyName) : propertyTableName;
		if (header == null)
			throw new RuntimeException("NamingStrategy not properly filled");
		return columnName(header);
	}

	public String logicalColumnName(String columnName, String propertyName) {
		return StringUtil.isBlank(columnName) ? StringUtil.unqualify(propertyName) : columnName;
	}

	public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {
		if (tableName != null) {
			return tableName;
		} else {
			return new StringBuffer(ownerEntityTable).append("_").append(associatedEntityTable != null ? associatedEntityTable : StringUtil.unqualify(propertyName)).toString();
		}
	}

	public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
		return StringUtil.isBlank(columnName) ? StringUtil.unqualify(propertyName) + "_" + referencedColumn : columnName;
	}

	public String fkName(String entity, String columnName, String refrenceEntity) {
		int result = 0;
		if (columnName != null) {
			result += columnName.hashCode();
		}
		if (refrenceEntity != null) {
			result += refrenceEntity.hashCode();
		}
		return "FK_" + (Integer.toHexString(entity.hashCode()) + Integer.toHexString(result)).toUpperCase();
	}

	
	public String getTablePrefix() {
		return "";
	}
}
