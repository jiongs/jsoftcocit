package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.baseentity.security.IUserEntity;
import com.jsoft.cocit.dmengine.info.IUserPreferenceInfo;
import com.jsoft.cocit.dmengine.info.IUserInfo;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class SecurityUserInfo extends NamedEntityInfo<IUserEntity> implements IUserInfo {

	private Map<String, IUserPreferenceInfo> configs;

	protected SecurityUserInfo(IUserEntity obj) {
		super(obj);
	}

	@Override
	public void release() {
		super.release();

		if (configs != null) {
			for (IUserPreferenceInfo v : configs.values()) {
				v.release();
			}
			configs.clear();
			configs = null;
		}
	}

	public String getUsername() {
		return entityData.getUsername();
	}

	public String getPassword() {
		return entityData.getPassword();
	}

	public String getImage() {
		return entityData.getImage();
	}

	public String getLogo() {
		return entityData.getLogo();
	}

	public String getRawPassword() {
		return entityData.getRawPassword();
	}

	public Date getExpiredFrom() {
		return entityData.getExpiredFrom();
	}

	public Date getExpiredTo() {
		return entityData.getExpiredTo();
	}

	public boolean isLocked() {
		return entityData.isLocked();
	}

	public String getReferencedCode() {
		return entityData.getReferencedCode();
	}

	public int getPrincipalType() {
		return entityData.getPrincipalType();
	}

	public String getTenantCode() {
		return entityData.getTenantCode();
	}

	// public String getTenantName() {
	// return entityData.getTenantName();
	// }

	@Override
	public String getRoles() {
		return entityData.getRoles();
	}

	private void initConfigs() {
		configs = new HashMap();
		// TODO
	}

	public <T> T getConfigItem(String key, T defaultValue) {
		if (configs == null) {
			this.initConfigs();
		}
		IUserPreferenceInfo config = configs.get(key);
		if (config != null)
			return config.get(defaultValue);

		return defaultValue;
	}

	@Override
	public List<String> getRolesList() {
		return StringUtil.toList(this.getRoles());
	}
}
