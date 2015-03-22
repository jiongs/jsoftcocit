package com.jsoft.cocit.entity;

import javax.persistence.Column;

/**
 * 
 * “租户拥有的树形实体”基类：
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public class TenantOwnerTreeEntity extends TreeEntity implements IExtTenantOwnerEntity {

	@Column(length = 128, name = "tenant_key_", nullable = false)
	protected String tenantKey = "";

	@Column(length = 128, name = "tenant_name_")
	protected String tenantName;

	public String getTenantKey() {
		return tenantKey;
	}

	public void setTenantKey(String id) {
		this.tenantKey = id;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String name) {
		tenantName = name;
	}

	protected void toJson(StringBuffer sb) {
		super.toJson(sb);

		this.toJson(sb, "tenantKey", tenantKey);
		this.toJson(sb, "tenantName", tenantName);
	}
}
