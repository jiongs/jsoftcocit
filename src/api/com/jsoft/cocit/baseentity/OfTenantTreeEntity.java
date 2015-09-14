package com.jsoft.cocit.baseentity;

import javax.persistence.Column;

/**
 * 
 * “租户拥有的树形实体”基类：
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public abstract class OfTenantTreeEntity extends TreeEntity implements IOfTenantEntityExt {

	@Column(length = 128, name = "tenant_key_", nullable = false)
	protected String tenantCode = "";

	@Column(length = 128, name = "tenant_name_")
	protected String tenantName;

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String id) {
		this.tenantCode = id;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String name) {
		tenantName = name;
	}

	protected void toJson(StringBuffer sb) {
		super.toJson(sb);

		this.toJson(sb, "tenantCode", tenantCode);
		this.toJson(sb, "tenantName", tenantName);
	}
}
