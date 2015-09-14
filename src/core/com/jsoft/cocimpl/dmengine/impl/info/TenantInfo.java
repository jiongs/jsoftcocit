package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.baseentity.security.ITenantEntity;
import com.jsoft.cocit.dmengine.IDataManager;
import com.jsoft.cocit.dmengine.info.IDataSourceInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.dmengine.info.ITenantPreferenceInfo;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class TenantInfo extends NamedEntityInfo<ITenantEntity> implements ITenantInfo {

	private Map<String, ITenantPreferenceInfo> configs;

	protected TenantInfo(ITenantEntity obj) {
		super(obj);
	}

	@Override
	public void release() {
		super.release();

		if (configs != null) {
			for (ITenantPreferenceInfo v : configs.values()) {
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

	public String getSystemCode() {
		return entityData.getSystemCode();
	}

	public String getDataSourceCode() {
		return entityData.getDataSourceCode();
	}

	// public Date getExpiredFrom() {
	// return entityData.getExpiredFrom();
	// }
	//
	// public Date getExpiredTo() {
	// return entityData.getExpiredTo();
	// }

	public String getDomain() {
		return entityData.getDomain();
	}

	public IDataManager getEntityManager(ISystemMenuInfo systemMenu) {
		return getDataManagerFactory().getManager(systemMenu);
	}

	public <T> T getConfigItem(String key, T defaultValue) {
		if (configs == null) {
			this.initConfigs();
		}
		ITenantPreferenceInfo config = configs.get(key);
		if (config != null)
			return config.get(defaultValue);

		return defaultValue;
	}

	public ISystemInfo getSystem() {
		return getEntityInfoFactory().getSystem(getSystemCode());
	}

	public IDataSourceInfo getDataSource() {
		return getEntityInfoFactory().getDataSource(this.getDataSourceCode());
	}

}
