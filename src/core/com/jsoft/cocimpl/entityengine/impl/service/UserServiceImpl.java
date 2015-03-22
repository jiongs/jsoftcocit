package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.entity.security.IUser;
import com.jsoft.cocit.entityengine.service.UserPreferenceService;
import com.jsoft.cocit.entityengine.service.UserService;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class UserServiceImpl extends NamedEntityServiceImpl<IUser> implements UserService {

	private Map<String, UserPreferenceService> configs;

	protected UserServiceImpl(IUser obj) {
		super(obj);
	}

	@Override
	public void release() {
		super.release();

		if (configs != null) {
			for (UserPreferenceService v : configs.values()) {
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

	public String getReferencedKey() {
		return entityData.getReferencedKey();
	}

	public int getPrincipalType() {
		return entityData.getPrincipalType();
	}

	public String getTenantKey() {
		return entityData.getTenantKey();
	}

	public String getTenantName() {
		return entityData.getTenantName();
	}

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
		UserPreferenceService config = configs.get(key);
		if (config != null)
			return config.get(defaultValue);

		return defaultValue;
	}

	@Override
	public List<String> getRolesList() {
		return StringUtil.toList(this.getRoles());
	}
}
