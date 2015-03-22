package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.entity.security.ITenant;
import com.jsoft.cocit.entityengine.DataManager;
import com.jsoft.cocit.entityengine.service.DataSourceService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.entityengine.service.TenantPreferenceService;
import com.jsoft.cocit.entityengine.service.TenantService;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class TenantServiceImpl extends NamedEntityServiceImpl<ITenant> implements TenantService {

	private Map<String, TenantPreferenceService> configs;

	protected TenantServiceImpl(ITenant obj) {
		super(obj);
	}

	@Override
	public void release() {
		super.release();

		if (configs != null) {
			for (TenantPreferenceService v : configs.values()) {
				v.release();
			}
			configs.clear();
			configs = null;
		}
	}

	private void initConfigs() {
		configs = new HashMap();
		// TODO
	}

	public String getSystemKey() {
		return entityData.getSystemKey();
	}

	public String getDataSourceKey() {
		return entityData.getDataSourceKey();
	}

	public Date getExpiredFrom() {
		return entityData.getExpiredFrom();
	}

	public Date getExpiredTo() {
		return entityData.getExpiredTo();
	}

	public String getDomain() {
		return entityData.getDomain();
	}

	public DataManager getEntityManager(SystemMenuService systemMenu) {
		return dmf().getManager(systemMenu);
	}

	public <T> T getConfigItem(String key, T defaultValue) {
		if (configs == null) {
			this.initConfigs();
		}
		TenantPreferenceService config = configs.get(key);
		if (config != null)
			return config.get(defaultValue);

		return defaultValue;
	}

	public SystemService getSystem() {
		return esf().getSystem(getSystemKey());
	}

	public DataSourceService getDataSource() {
		return esf().getDataSource(this.getDataSourceKey());
	}

}
