package com.jsoft.cocit.baseentity;

import javax.persistence.Column;

/**
 * 可命名的“组织机构的数据实体”基类：该类的子类所映射的数据表用来存储组织机构相关的基础业务数据。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public abstract class OfOrgNamedEntity extends OfTenantNamedEntity implements IOfOrgEntityExt {

	@Column(length = 128, name = "org_key_", nullable = false)
	protected String orgCode = "";

	@Column(length = 128, name = "org_name_")
	protected String orgName;

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	protected void toJson(StringBuffer sb) {
		super.toJson(sb);

		this.toJson(sb, "orgCode", orgCode);
		this.toJson(sb, "orgName", orgName);
	}
}
