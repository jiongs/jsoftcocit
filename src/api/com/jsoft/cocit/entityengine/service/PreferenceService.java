package com.jsoft.cocit.entityengine.service;

import com.jsoft.cocit.entity.config.IPreference;

/**
 * 软件配置助理：用于辅助Cocit软件{@link TenantService}完成配置项管理工作。
 * 
 * @author jiongs753
 * 
 */
public interface PreferenceService extends IPreference {

	String getStr();

	String getStr(String defaultValue);

	int getInt();

	int getInt(int defaultValue);

	<T> T get(T defaultReturn);

}
