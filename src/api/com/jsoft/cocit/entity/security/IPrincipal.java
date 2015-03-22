package com.jsoft.cocit.entity.security;

import com.jsoft.cocit.entity.INamedEntity;
import com.jsoft.cocit.entity.ITenantOwnerEntity;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IPrincipal extends INamedEntity, ITenantOwnerEntity {

	/**
	 * 关联KEY：逻辑外键，可以关联到员工、岗位、组织机构等。
	 * <p>
	 * <UL>
	 * <LI>如果主体类型是人员{@link IUser}：则该字段关联到{@link IUser#getKey()}字段；
	 * <LI>如果主体类型是组{@link IGroup}：则该字段关联到{@link IGroup#getKey()}字段；
	 * </UL>
	 * 
	 * @return
	 */
	String getReferencedKey();

	int getPrincipalType();
}
