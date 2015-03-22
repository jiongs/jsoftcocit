package com.jsoft.cocimpl.entityengine.impl.service;

import com.jsoft.cocit.entity.config.ISystemPreference;
import com.jsoft.cocit.entityengine.service.SystemPreferenceService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class SystemPreferenceServiceImpl extends NamedEntityServiceImpl<ISystemPreference> implements SystemPreferenceService {

	private SystemService tenant;

	SystemPreferenceServiceImpl(ISystemPreference obj, SystemService tenant) {
		super(obj);
		this.tenant = tenant;
	}

	@Override
	public void release() {
		super.release();

		tenant = null;
	}

	public String getText() {
		return entityData.getText();
	}

	public String getValue() {
		return entityData.getValue();
	}

	// public String getDescription() {
	// return entity.getDescription();
	// }

	public String getSystemKey() {
		return entityData.getSystemKey();
	}

	public String getSystemName() {
		return entityData.getSystemName();
	}

	public String getStr() {
		return get("");
	}

	public String getStr(String defaultValue) {
		if (entityData != null && !StringUtil.isBlank(entityData.getValue()))
			return entityData.getValue();

		return defaultValue;
	}

	public <T> T get(T defaultReturn) {
		String value = this.getStr();

		try {
			return (T) StringUtil.castTo(value, defaultReturn);
		} catch (Throwable e) {
			LogUtil.error("CoudSoftConfigImpl.get: 出错！ {key:%s, defaultReturn:%s}", entityData.getText(), defaultReturn, e);
		}

		return defaultReturn;
	}

	public int getInt() {
		return getInt(0);
	}

	public int getInt(int defaultValue) {
		try {
			if (entityData != null && !StringUtil.isBlank(entityData.getValue()))
				return Integer.parseInt(entityData.getValue());
		} catch (Throwable e) {
		}

		return defaultValue;
	}

	public SystemService getSystem() {
		return tenant;
	}

}
