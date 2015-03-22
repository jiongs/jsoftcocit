package com.jsoft.cocit.entity.impl.config;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.config.ITenantPreference;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
@Entity
@CocEntity(name = "企业用户个性化配置", key = Const.TBL_CFG_TENANTPREF, sn = 5, actions = {
//
		@CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c")//
		, @CocAction(importFromFile = "EntityAction.data.js") //
}// end actions
, groups = {
//
@CocGroup(name = "基本信息", key = "basic", fields = {
//
		@CocColumn(name = "配置项名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "配置项编码", propName = "key", mode = "c:M e:M")//
		, @CocColumn(name = "配置项内容", propName = "value", mode = "c:M e:M")//
		, @CocColumn(name = "配置项描述", propName = "description", mode = "c:E e:E")//
		, @CocColumn(name = "配置项序号", propName = "sn", mode = "*:N v:P") //
		, @CocColumn(name = "创建时间", propName = "createdDate", mode = "*:N v:P", pattern = "datetime") //
		, @CocColumn(name = "创建帐号", propName = "createdUser", mode = "*:N v:P") //
		, @CocColumn(name = "修改时间", propName = "updatedDate", mode = "*:N v:P", pattern = "datetime") //
		, @CocColumn(name = "修改帐号", propName = "updatedUser", mode = "*:N v:P") //

}) // end group
}// end groups
)
public class TenantPreferenceImpl extends PreferenceImpl implements ITenantPreference {

	@Column(length = 64, name = "tenant_key_")
	protected String tenantKey;

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
}
