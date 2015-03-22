package com.jsoft.cocit.orm.mapping;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.nutz.lang.Mirror;

import com.jsoft.cocimpl.orm.generator.EntityIdGenerator;

public interface EnMapping<T> {

	String getIdProperty();

	/**
	 * 获取实体类型
	 * <p>
	 * 单表存储具有继承关系数据时需要一个字段来却别数据对应的实体类型
	 * 
	 * @return
	 */
	EnColumnMapping getDtype();

	String getTableName();

	EntityIdGenerator getIdGenerator();

	List getRelations(String regex);

	List getManyMany(String regex);

	<X extends EnColumnMapping> Collection<X> fields();

	Class getType();

	boolean isReadonly();

	List<String[]> getUniqueFields();

	List<String[]> getIndexFields();

	public Iterator<EnColumnMapping> getGeneratorColumns();

	Mirror<? extends T> getAgentMirror();

	Mirror<? extends T> getLazyAgentMirror();

	void release();
}
