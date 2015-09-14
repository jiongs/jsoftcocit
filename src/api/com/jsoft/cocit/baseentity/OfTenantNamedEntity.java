package com.jsoft.cocit.baseentity;

import javax.persistence.Column;

import com.jsoft.cocit.baseentity.security.ITenantEntity;

/**
 * 
 * “租户的数据实体”基类：该类是【{@link NamedEntity}】的子类，该类的子类所映射的数据表用来存储指定租户的基础业务数据。
 * <p>
 * 其子类所映射的数据表除了拥有父类【{@link NamedEntity}】中所描述的公共字段外，还将拥有如下公共字段：
 * <UL>
 * <LI>tenantCode：企业编码（租户KEY）,字段类型【逻辑外键，关联到企业（租户）信息表的逻辑主键{@link ITenantEntity#getCode()}】
 * </UL>
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public abstract class OfTenantNamedEntity extends NamedEntity implements IOfTenantEntityExt {

	@Column(length = 64, name = "tenant_key_", nullable = false)
	protected String tenantCode = "";

	@Column(length = 128, name = "tenant_name_")
	protected String tenantName = "";

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
