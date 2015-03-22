package com.jsoft.cocit.entity.config;

import com.jsoft.cocit.entity.INamedEntity;
import com.jsoft.cocit.entity.ITenantOwnerEntity;
import com.jsoft.cocit.entity.security.IUser;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 *
 */
public interface IUserPreference extends INamedEntity, ITenantOwnerEntity, IPreference {

	/**
	 * 用户编号：逻辑外键，关联到{@link IUser#getKey()}字段。
	 * 
	 * @return
	 */
	String getUserKey();

	/**
	 * 用户名称：冗余字段，关联到{@link IUser#getName()}字段。
	 * 
	 * @return
	 */
	String getUserName();

	int getUserType();

}
