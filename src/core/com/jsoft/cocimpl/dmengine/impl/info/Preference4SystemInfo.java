package com.jsoft.cocimpl.dmengine.impl.info;

import com.jsoft.cocit.baseentity.config.ISystemPreferenceEntity;
import com.jsoft.cocit.dmengine.info.ISystemPreferenceInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class Preference4SystemInfo extends NamedEntityInfo<ISystemPreferenceEntity> implements ISystemPreferenceInfo {

	private ISystemInfo system;

	Preference4SystemInfo(ISystemPreferenceEntity obj, ISystemInfo system) {
		super(obj);
		this.system = system;
	}

	@Override
	public void release() {
		super.release();

		system = null;
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

	public String getSystemCode() {
		return entityData.getSystemCode();
	}

	public String getSystemName() {
		return entityData.getSystemName();
	}

	public String getStr() {
		return getStr("");
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

	public ISystemInfo getSystem() {
		return system;
	}

	@Override
	public String getValueText() {
		return entityData.getValueText();
	}

}
