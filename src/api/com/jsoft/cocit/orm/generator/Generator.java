package com.jsoft.cocit.orm.generator;

import java.io.Serializable;

import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.orm.ExtDao;
import com.jsoft.cocit.orm.mapping.EnColumnMapping;
import com.jsoft.cocit.orm.mapping.EnMapping;

public interface Generator<T> {

	/**
	 * 关联到{@link CocEntity#generator()}
	 */
	public String getName();

	/**
	 * 
	 * @param dao
	 *            数据访问你对象
	 * @param entity
	 *            实体映射
	 * @param column
	 *            字段映射
	 * @param dataObj
	 *            数据对象
	 * @param params
	 *            参数
	 * @return
	 */
	public Serializable generate(ExtDao dao, EnMapping entity, EnColumnMapping column, Object dataObject, String... params);

}
