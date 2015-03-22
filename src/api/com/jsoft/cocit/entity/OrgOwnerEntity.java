package com.jsoft.cocit.entity;

import javax.persistence.Column;

/**
 * “组织机构的数据实体”基类：该类的子类所映射的数据表用来存储组织机构相关的基础业务数据。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public class OrgOwnerEntity extends TenantOwnerEntity implements IExtOrgOwnerEntity {

	@Column(length = 128, name = "org_key_", nullable = false)
	protected String orgKey = "";

	@Column(length = 128, name = "org_name_")
	protected String orgName;

	public String getOrgKey() {
		return orgKey;
	}

	public void setOrgKey(String orgKey) {
		this.orgKey = orgKey;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	protected void toJson(StringBuffer sb) {
		super.toJson(sb);

		this.toJson(sb, "orgKey", orgKey);
		this.toJson(sb, "orgName", orgName);
	}
}
