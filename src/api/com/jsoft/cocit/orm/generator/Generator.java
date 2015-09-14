package com.jsoft.cocit.orm.generator;

import java.io.Serializable;

import com.jsoft.cocit.dmengine.annotation.CocEntity;
import com.jsoft.cocit.orm.IExtDao;
import com.jsoft.cocit.orm.mapping.EnColumnMapping;
import com.jsoft.cocit.orm.mapping.EnMapping;

/**
 * 字段生成器接口，该接口以单例模式运行。
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public interface Generator<T> {

	/**
	 * 关联到{@link CocEntity#auto()}
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
	public Serializable generate(IExtDao dao, EnMapping entity, EnColumnMapping column, Object dataObject, String... params);

}
