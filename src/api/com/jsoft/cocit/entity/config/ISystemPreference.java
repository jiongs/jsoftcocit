package com.jsoft.cocit.entity.config;

import com.jsoft.cocit.entity.INamedEntity;
import com.jsoft.cocit.entity.security.ISystem;

/**
 * 系统个性化设置：
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface ISystemPreference extends INamedEntity, IPreference {

	/**
	 * 系统KEY：逻辑外键，关联到{@link ISystem#getKey()}字段。
	 * 
	 * @return
	 */
	public String getSystemKey();

	/**
	 * 系统名称：冗余字段，关联到{@link ISystem#getName()}字段。
	 * 
	 * @return
	 */
	public String getSystemName();

}
