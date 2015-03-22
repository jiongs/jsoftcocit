package com.jsoft.cocit.entity.impl.config;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.config.ISystemPreference;
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
@CocEntity(name = "系统个性化配置", key = Const.TBL_CFG_SYSPREF, sn = 4, actions = {
//
		@CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c")//
		, @CocAction(importFromFile = "EntityAction.data.js") //
}// end actions
, groups = {
//
@CocGroup(name = "基本信息", key = "basic", fields = {
//
		@CocColumn(name = "系统编码", propName = "systemKey", mode = "c:M e:M", fkTargetEntity = Const.TBL_SEC_SYSTEM, fkTargetAsParent = true)//
		, @CocColumn(name = "系统名称", propName = "systemName", mode = "c:M e:M")//
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
public class SystemPreferenceImpl extends PreferenceImpl implements ISystemPreference {

	@Column(length = 64)
	private String systemKey;

	@Column(length = 128)
	private String systemName;

	public String getSystemKey() {
		return systemKey;
	}

	public void setSystemKey(String systemKey) {
		this.systemKey = systemKey;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
}
