package com.jsoft.cocit.baseentity;



/**
 * “租户拥有者数据实体”接口：该接口的实现类所映射的实体表用来存储指定租户的基础业务数据。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IOfTenantEntityExt extends IDataEntityExt, IOfTenantEntity {

	public void setTenantCode(String tenantCode);

	void setTenantName(String tenantName);
}
