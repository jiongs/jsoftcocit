package com.jsoft.cocit.baseentity.config;

import com.jsoft.cocit.baseentity.INamedEntity;
import com.jsoft.cocit.baseentity.IOfTenantEntity;
import com.jsoft.cocit.baseentity.security.IUserEntity;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 *
 */
public interface IUserPreferenceEntity extends INamedEntity, IOfTenantEntity, IPreferenceEntity {

	/**
	 * 用户编号：逻辑外键，关联到{@link IUserEntity#getCode()}字段。
	 * 
	 * @return
	 */
	String getUserCode();

	/**
	 * 用户名称：冗余字段，关联到{@link IUserEntity#getName()}字段。
	 * 
	 * @return
	 */
	String getUserName();

	int getUserType();

}
