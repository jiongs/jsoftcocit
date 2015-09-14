package com.jsoft.cocimpl.dmengine.impl.info;

import com.jsoft.cocit.baseentity.config.ITenantPreferenceEntity;
import com.jsoft.cocit.dmengine.info.ITenantPreferenceInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class Preference4TenantInfo extends NamedEntityInfo<ITenantPreferenceEntity> implements ITenantPreferenceInfo {

	private ITenantInfo tenant;

	Preference4TenantInfo(ITenantPreferenceEntity obj, ITenantInfo tenant) {
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

	public String getTenantCode() {
		return entityData.getTenantCode();
	}

	public String getTenantName() {
		return entityData.getTenantName();
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

	public ITenantInfo getTenant() {
		return tenant;
	}

	@Override
	public String getValueText() {
		return entityData.getValueText();
	}

}
