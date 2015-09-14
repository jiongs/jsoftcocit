package com.jsoft.cocit.baseentity;



/**
 * “组织机构的数据实体”接口：该接口的实现类所映射的数据表用来存储组织机构相关的基础业务数据。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IOfOrgEntityExt extends IOfTenantEntityExt, IOfOrgEntity {

	public void setOrgCode(String orgCode);

	void setOrgName(String orgName);
}
