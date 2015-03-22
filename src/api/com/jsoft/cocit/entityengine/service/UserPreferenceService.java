package com.jsoft.cocit.entityengine.service;

import com.jsoft.cocit.entity.config.IUserPreference;

/**
 * 软件配置助理：用于辅助Cocit软件{@link TenantService}完成配置项管理工作。
 * 
 * @author jiongs753
 * 
 */
public interface UserPreferenceService extends IUserPreference, PreferenceService {

	UserService getUser();

}
