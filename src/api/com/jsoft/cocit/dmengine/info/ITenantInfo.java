package com.jsoft.cocit.dmengine.info;

import com.jsoft.cocit.baseentity.security.ITenantEntity;
import com.jsoft.cocit.dmengine.IDataManager;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public interface ITenantInfo extends INamedEntityInfo<ITenantEntity>, ITenantEntity {

	IDataManager getEntityManager(ISystemMenuInfo systemMenu);

	/**
	 * 获取租户配置项
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	<T> T getConfigItem(String key, T defaultValue);

	/**
	 * 获取租户所使用的子系统
	 */
	ISystemInfo getSystem();

	IDataSourceInfo getDataSource();
}
