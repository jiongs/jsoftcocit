package com.jsoft.cocit.baseentity.config;

import com.jsoft.cocit.baseentity.INamedEntity;
import com.jsoft.cocit.baseentity.security.ISystemEntity;

/**
 * 系统个性化设置：
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface ISystemPreferenceEntity extends INamedEntity, IPreferenceEntity {

	/**
	 * 系统KEY：逻辑外键，关联到{@link ISystemEntity#getCode()}字段。
	 * 
	 * @return
	 */
	public String getSystemCode();

	/**
	 * 系统名称：冗余字段，关联到{@link ISystemEntity#getName()}字段。
	 * 
	 * @return
	 */
	public String getSystemName();

}
