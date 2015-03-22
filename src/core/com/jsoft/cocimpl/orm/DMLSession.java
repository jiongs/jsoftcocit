package com.jsoft.cocimpl.orm;

import java.util.Map;

import com.jsoft.cocimpl.orm.metadata.ColumnMetadata;
import com.jsoft.cocimpl.orm.metadata.ForeignKeyMetadata;
import com.jsoft.cocimpl.orm.metadata.TableMetadata;
import com.jsoft.cocit.orm.mapping.EnMapping;

public interface DMLSession {

	/**
	 * 清空数据库表
	 */
	void clear();

	/**
	 * 统计数据库中有多少张数据表
	 * 
	 * @return
	 */
	int getTableCount();

	/**
	 * 计算实体有多少个字段
	 * 
	 * @param entity
	 * @return
	 */
	int getColumnCount(EnMapping mapping);

	/**
	 * 计算实体表外键数量
	 * 
	 * @param mapping
	 * @return
	 */
	int getImportFKCount(EnMapping mapping);

	/**
	 * 计算实体表被其他表外键引用的次数
	 * 
	 * @param mapping
	 * @return
	 */
	int getExportFKCount(EnMapping mapping);

	/**
	 * 删除实体表，同时删除多对多关联表
	 * 
	 * @param mapping
	 */
	void dropTable(EnMapping mapping);

	/**
	 * 检查实体表： 如果实体表不存在，则自动创建。
	 * <p>
	 * 该方法供EntityHolder加载实体时调用。
	 * 
	 * @param entity
	 *            待检查的实体
	 * @param dependMe
	 *            哪些实体引用到该实体
	 * @param alterFKTable
	 *            是否同步外键关联表
	 */
	void createOrAlterTable(EnMapping<?> mapping, boolean alterFKTable);

	/**
	 * 获取数据库中的所有数据表
	 * 
	 * @return
	 */
	Map<String, TableMetadata> getTableMetadatas();

	/**
	 * 获取实体表元数据信息
	 * 
	 * @param mapping
	 * @return
	 */
	TableMetadata getTableMetadata(EnMapping mapping);

	/**
	 * 获取表元数据信息
	 * 
	 * @param tableName
	 * @return
	 */
	TableMetadata getTableMetadata(String tableName);

	/**
	 * 获取实体的所有字段
	 * 
	 * @param mapping
	 * @return
	 */
	Map<String, ColumnMetadata> getColumnMetadatas(EnMapping mapping);

	/**
	 * 获取实体的所有字段
	 * 
	 * @param mapping
	 * @return
	 */
	Map<String, ColumnMetadata> getColumnMetadatas(String tableName);

	/**
	 * 获取实体被哪些外键所引用
	 * 
	 * @param mapping
	 * @return
	 */
	Map<String, ForeignKeyMetadata> getExportFKMetadatas(String tableName);

	/**
	 * 获取实体被哪些外键所引用
	 * 
	 * @param mapping
	 * @return
	 */
	Map<String, ForeignKeyMetadata> getExportFKMetadatas(EnMapping mapping);

	/**
	 * 获取实体外键
	 * 
	 * @param tableName
	 * @return
	 */
	Map<String, ForeignKeyMetadata> getImportFKMetadatas(String tableName);

	/**
	 * 获取实体外键
	 * 
	 * @param mapping
	 * @return
	 */
	Map<String, ForeignKeyMetadata> getImportFKMetadatas(EnMapping mapping);
}
