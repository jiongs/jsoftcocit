package com.jsoft.cocit.entity.impl.security;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.security.IExtSystem;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

/**
 * 系统：
 */
@Entity
@CocEntity(name = "系统管理", key = Const.TBL_SEC_SYSTEM, sn = 2, actions = {
//
        @CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c")//
        , @CocAction(importFromFile = "EntityAction.data.js") //
           }//
           , groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "系统名称", propName = "name", mode = "c:M e:M")//
                   , @CocColumn(name = "系统编码", propName = "key", mode = "c:M e:R")//
                   , @CocColumn(name = "数据源", propName = "dataSourceKey", fkTargetEntity = Const.TBL_CFG_DATASOURCE)//
                   , @CocColumn(name = "访问所有租户数据", propName = "allowAccessAllTenantData", uiView = "radio", dicOptions = "1:允许, 0:禁止")//
                   , @CocColumn(name = "序号", propName = "sn", mode = "*:N v:P") //
                     }) // end group
           }// end groups
)
public class SystemEntity extends NamedEntity implements IExtSystem {
	protected boolean allowAccessAllTenantData;

	@Column(length = 64)
	protected String dataSourceKey;

	public String getDataSourceKey() {
		return dataSourceKey;
	}

	public void setDataSourceKey(String dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}

	public boolean isAllowAccessAllTenantData() {
		return allowAccessAllTenantData;
	}

	public void setAllowAccessAllTenantData(boolean supportTenantOwner) {
		this.allowAccessAllTenantData = supportTenantOwner;
	}
}
