package com.jsoft.cocit.entity.impl.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.security.IExtTenant;
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
@CocEntity(name = "租户管理", key = Const.TBL_SEC_TENANT, sn = 1, actions = {
//
        @CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c")//
        , @CocAction(importFromFile = "EntityAction.data.js") //
           }// end actions
           , groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "租户名称", propName = "name", mode = "c:M e:M")//
                   , @CocColumn(name = "租户编号", propName = "key", mode = "c:M e:R")//
                   , @CocColumn(name = "系统名称", propName = "systemKey", fkTargetAsGroup = true, fkTargetEntity = Const.TBL_SEC_SYSTEM, mode = "c:M e:M") //
                   , @CocColumn(name = "租户域名", propName = "domain") //
                   , @CocColumn(name = "有效期自", propName = "expiredFrom") //
                   , @CocColumn(name = "有效期至", propName = "expiredTo") //
                   , @CocColumn(name = "数据源", propName = "dataSourceKey", fkTargetEntity = Const.TBL_CFG_DATASOURCE) //
                   , @CocColumn(name = "序号", propName = "sn", mode = "*:N v:P") //
                   , @CocColumn(name = "创建时间", propName = "createdDate", mode = "*:N v:P", pattern = "datetime") //
                   , @CocColumn(name = "创建帐号", propName = "createdUser", mode = "*:N v:P") //
                   , @CocColumn(name = "修改时间", propName = "updatedDate", mode = "*:N v:P", pattern = "datetime") //
                   , @CocColumn(name = "修改帐号", propName = "updatedUser", mode = "*:N v:P") //
                     }) // end group
           }// end groups
)
public class Tenant extends NamedEntity implements IExtTenant {

	@Column(length = 64)
	private String systemKey;

	@Column(length = 64)
	private String dataSourceKey;

	protected Date expiredFrom;

	protected Date expiredTo;

	@Column(length = 64)
	private String domain;

	public String getSystemKey() {
		return systemKey;
	}

	public void setSystemKey(String systemKey) {
		this.systemKey = systemKey;
	}

	public String getDataSourceKey() {
		return dataSourceKey;
	}

	public void setDataSourceKey(String dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}

	public Date getExpiredFrom() {
		return expiredFrom;
	}

	public void setExpiredFrom(Date expiredFrom) {
		this.expiredFrom = expiredFrom;
	}

	public Date getExpiredTo() {
		return expiredTo;
	}

	public void setExpiredTo(Date expiredTo) {
		this.expiredTo = expiredTo;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
