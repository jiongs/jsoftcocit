package com.jsoft.cocit.dmengine.info;

import com.jsoft.cocit.baseentity.config.ITenantPreferenceEntity;

/**
 * 软件配置助理：用于辅助Cocit软件{@link ITenantInfo}完成配置项管理工作。
 * 
 * @author jiongs753
 * 
 */
public interface ITenantPreferenceInfo extends ITenantPreferenceEntity, IPreferenceInfo {

	ITenantInfo getTenant();

}
