package com.jsoft.cocit.entity.impl.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.TenantOwnerEntity;
import com.jsoft.cocit.entity.security.IGroupMember;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;
import com.jsoft.cocit.entityengine.annotation.CuiEntity;
import com.jsoft.cocit.entityengine.annotation.CuiForm;
import com.jsoft.cocit.entityengine.annotation.CuiGrid;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
@Entity
@CocEntity(name = "群组成员管理", key = Const.TBL_SEC_GROUPMEMBER, sn = 8, uniqueFields = "systemUser,group", //
           actions = {
                   //
                   @CocAction(name = "添加用户", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c1", uiForm = "batch_adduser"),//
                   @CocAction(name = "添加组", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c2", uiForm = "batch_addgroup"),//
                   @CocAction(name = "编辑", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d"), //
           }//
           , groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "系统用户", field = "memberUser", fkTargetField = "key", mode = "c1:M c2:M"),//
                   @CocColumn(name = "所属组", field = "group", fkTargetField = "key", mode = "c1:M c2:M"),//
                   @CocColumn(name = "有效期自", field = "expiredFrom", pattern = "datetime"), //
                   @CocColumn(name = "有效期至", field = "expiredTo", pattern = "datetime"), //
                   @CocColumn(name = "编号", field = "key", mode = "*:N") //
                     }) // end group
           }// end groups
)
@CuiEntity(//
           grid = @CuiGrid(fields = "memberUser|group|expiredFrom|expiredTo"),//
           forms = {
                   //
                   @CuiForm(key = "batch_adduser", fields = "group", batchFields = "memberUser|expiredFrom|expiredTo"), //
                   @CuiForm(key = "batch_addgroup", fields = "memberUser", batchFields = "group|expiredFrom|expiredTo") //
           }//
)
public class GroupMember extends TenantOwnerEntity implements IGroupMember {
	@Column(name = "group_")
	@ManyToOne
	protected Group group;
	@ManyToOne
	protected SystemUser memberUser;
	@ManyToOne
	protected Group memberGroup;
	protected Date expiredFrom;
	protected Date expiredTo;

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

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public SystemUser getMemberUser() {
		return memberUser;
	}

	public void setMemberUser(SystemUser userMember) {
		this.memberUser = userMember;
	}

	public Group getMemberGroup() {
		return memberGroup;
	}

	public void setMemberGroup(Group memberGroup) {
		this.memberGroup = memberGroup;
	}
}
