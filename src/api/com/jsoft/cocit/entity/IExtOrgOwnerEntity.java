package com.jsoft.cocit.entity;


/**
 * “组织机构的数据实体”接口：该接口的实现类所映射的数据表用来存储组织机构相关的基础业务数据。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IExtOrgOwnerEntity extends IExtTenantOwnerEntity, IOrgOwnerEntity {

	public void setOrgKey(String orgKey);

	void setOrgName(String orgName);
}
