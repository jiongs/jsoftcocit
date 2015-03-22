package com.jsoft.cocit.entity.impl.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.TenantOwnerEntity;
import com.jsoft.cocit.entity.security.IRoleMember;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

@Entity
@CocEntity(name = "角色成员管理", key = Const.TBL_SEC_ROLEMEMBER, sn = 9, //
           actions = {
                   //
                   @CocAction(name = "添加功能", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c1"),//
                   @CocAction(name = "编辑", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d"), //
           }//
           , groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "菜单编号", propName = "menuKey", fkTargetEntity = Const.TBL_SEC_SYSUSER)//
                   , @CocColumn(name = "角色编号", propName = "roleKey", fkTargetEntity = Const.TBL_SEC_ROLE) //
                   , @CocColumn(name = "编号", propName = "key", mode = "c:M e:M")//
                   , @CocColumn(name = "有效期自", propName = "expiredFrom", pattern = "datetime") //
                   , @CocColumn(name = "有效期至", propName = "expiredTo", pattern = "datetime") //
                     }) // end group
           }// end groups
)
public class RoleMember extends TenantOwnerEntity implements IRoleMember {
	@Column(length = 64)
	private String menuKey;

	@Column(length = 64)
	private String roleKey;

	protected Date expiredFrom;

	protected Date expiredTo;

	public String getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(String userKey) {
		this.menuKey = userKey;
	}

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
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
}
