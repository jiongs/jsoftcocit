package com.jsoft.cocit.entityengine.service;

import com.jsoft.cocit.entity.security.ITenant;
import com.jsoft.cocit.entityengine.DataManager;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public interface TenantService extends NamedEntityService<ITenant>, ITenant {

	DataManager getEntityManager(SystemMenuService systemMenu);

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
	SystemService getSystem();

	DataSourceService getDataSource();
}
