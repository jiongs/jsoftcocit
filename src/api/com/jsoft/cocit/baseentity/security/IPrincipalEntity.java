package com.jsoft.cocit.baseentity.security;

import com.jsoft.cocit.baseentity.INamedEntity;
import com.jsoft.cocit.baseentity.IOfTenantEntity;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IPrincipalEntity extends INamedEntity, IOfTenantEntity {

	/**
	 * 关联KEY：逻辑外键，可以关联到员工、岗位、组织机构等。
	 * <p>
	 * <UL>
	 * <LI>如果主体类型是人员{@link IUserEntity}：则该字段关联到{@link IUserEntity#getCode()}字段；
	 * <LI>如果主体类型是组{@link IGroupEntity}：则该字段关联到{@link IGroupEntity#getCode()}字段；
	 * </UL>
	 * 
	 * @return
	 */
	String getReferencedCode();

	int getPrincipalType();
}
