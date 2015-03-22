package com.jsoft.cocit.entity.config;

import com.jsoft.cocit.entity.INamedEntity;

/**
 * 租户个性化设置：
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface ITenantPreference extends INamedEntity, IPreference {

	String getTenantKey();

	String getTenantName();
}
