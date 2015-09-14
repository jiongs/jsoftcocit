package com.jsoft.cocit.dmengine.info;

import com.jsoft.cocit.baseentity.config.IUserPreferenceEntity;

/**
 * 软件配置助理：用于辅助Cocit软件{@link ITenantInfo}完成配置项管理工作。
 * 
 * @author jiongs753
 * 
 */
public interface IUserPreferenceInfo extends IUserPreferenceEntity, IPreferenceInfo {

	IUserInfo getUser();

}
