package com.jsoft.cocit.entity.impl.config;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.config.IUserPreference;
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
@CocEntity(name = "用户个性化配置", key = Const.TBL_CFG_USERPREF, sn = 6, actions = {
//
		@CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c")//
		, @CocAction(importFromFile = "EntityAction.data.js") //
}// end actions
, groups = {
//
@CocGroup(name = "基本信息", key = "basic", fields = {
//
		@CocColumn(name = "用户帐号", propName = "userKey", mode = "c:M e:M", fkTargetEntity = Const.TBL_SEC_SYSUSER)//
		, @CocColumn(name = "用户名称", propName = "userName", mode = "c:M e:M")//
		, @CocColumn(name = "配置项名称", propName = "name", mode = "c:M e:M")//
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
public class UserPreferenceImpl extends PreferenceImpl implements IUserPreference {

	@Column(length = 64, name = "tenant_key_")
	protected String tenantKey;

	@Column(length = 128, name = "tenant_name_")
	protected String tenantName;

	@Column(length = 64)
	private String userKey;

	@Column(length = 128)
	private String userName;

	private int userType;

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String systemName) {
		this.userName = systemName;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getTenantKey() {
		return tenantKey;
	}

	public void setTenantKey(String tenantKey) {
		this.tenantKey = tenantKey;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

}
