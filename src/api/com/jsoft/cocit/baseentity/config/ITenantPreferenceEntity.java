package com.jsoft.cocit.baseentity.config;

import com.jsoft.cocit.baseentity.INamedEntity;

/**
 * 租户个性化设置：
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface ITenantPreferenceEntity extends INamedEntity, IPreferenceEntity {

	String getTenantCode();

	String getTenantName();
}
