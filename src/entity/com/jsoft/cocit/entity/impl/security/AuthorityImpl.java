package com.jsoft.cocit.entity.impl.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.TenantOwnerEntity;
import com.jsoft.cocit.entity.plugin.AuthorityPlugins;
import com.jsoft.cocit.entity.security.IAuthority;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;
import com.jsoft.cocit.entityengine.annotation.Cui;
import com.jsoft.cocit.entityengine.annotation.CuiEntity;
import com.jsoft.cocit.util.StringUtil;

@Entity
@CocEntity(name = "权限管理", key = Const.TBL_SEC_AUTHORITY, sn = 10, uniqueFields = "systemKey,userKey,groupKey,menuKey,roleKey", indexFields = "systemKey,userKey,groupKey,menuKey,roleKey", //
           actions = {//
           //
                   @CocAction(name = "添加用户权限", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c", uiForm = "/WEB-INF/jsp/coc/user_authority_c.jsp", plugin = AuthorityPlugins.c.class), //
                   @CocAction(name = "添加群组授权", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c1", uiForm = "group_perm"), //
                   // @CocAction(name = "批量授权", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c9"), //
                   // @CocAction(name = "修改", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e"), //
                   // @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v"), //
                   // @CocAction(name = "收回权限", opCode = OpCodes.OP_REMOVE_ROWS, key = "r"), //
                   @CocAction(name = "收回权限", opCode = OpCodes.OP_DELETE_ROWS, key = "d") //
           },//
           groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "用户帐号", field = "userKey", fkTargetEntity = Const.TBL_SEC_SYSUSER), //
                   @CocColumn(name = "用户群组", field = "groupKey", fkTargetEntity = Const.TBL_SEC_GROUP),//
                   @CocColumn(name = "菜单编号", field = "menuKey", fkTargetEntity = Const.TBL_SEC_SYSMENU),//
                   @CocColumn(name = "角色编号", field = "roleKey", fkTargetEntity = Const.TBL_SEC_ROLE),//
                   @CocColumn(name = "操作权限", field = "menuActions"),//
                   @CocColumn(name = "行(数据)权限", field = "dataRows"),//
                   @CocColumn(name = "列(字段)权限", field = "dataCols"),//
                   @CocColumn(name = "所属系统", field = "systemKey", mode = "*:N v:S"),//
                   @CocColumn(name = "编号", field = "key", mode = "*:N v:S", generator = "timesn"),//
                   @CocColumn(name = "权限类型", field = "denied", dicOptions = "0:允许, 1:拒绝", uiView = "radio"), //
                   @CocColumn(name = "有效期自", field = "expiredFrom", pattern = "datetime"), //
                   @CocColumn(name = "有效期至", field = "expiredTo", pattern = "datetime"), //
                   @CocColumn(name = "权限状态", field = "statusCode", mode = "*:N v:S", dicOptions = "0:正常, -99999:已收回"),//
           }//
           ) // end group
           }// end: groups
)
@Cui({//
@CuiEntity(uiView = "/WEB-INF/jsp/coc/authority.jsp") //
        , @CuiEntity(key = "user", uiView = "/WEB-INF/jsp/coc/user_authority.jsp") //
        , @CuiEntity(key = "group", uiView = "/WEB-INF/jsp/coc/group_authority.jsp") //
})
public class AuthorityImpl extends TenantOwnerEntity implements IAuthority {

	private static final long serialVersionUID = -5013744703216842763L;

	@Column(length = 64, nullable = false)
	protected String systemKey = "";

	/*
	 * 主体：用户、用户组等
	 */
	protected String userKey = "";
	protected String groupKey = "";

	/*
	 * 功能：菜单、角色等
	 */
	protected String menuKey = "";
	protected String roleKey = "";

	/*
	 * 操作权限、数据权限、字段权限
	 */
	@Column(length = 512)
	protected String menuActions = "";
	@Column(length = 1024)
	protected String menuActionsNames = "";
	@Column(length = 512)
	protected String dataRows = "";
	@Column(length = 1024)
	protected String dataRowsNames = "";
	@Column(length = 512)
	protected String dataCols = "";
	@Column(length = 1024)
	protected String dataColsNames = "";

	/*
	 * 权限有效期
	 */
	protected boolean denied;
	protected Date expiredFrom;
	protected Date expiredTo;

	public String getSystemKey() {
		return systemKey;
	}

	public void setSystemKey(String systemKey) {
		this.systemKey = systemKey;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public String getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	public String getMenuActions() {
		return menuActions;
	}

	public void setMenuActions(String menuActions) {
		this.menuActions = menuActions;
	}

	public String getDataRows() {
		return dataRows;
	}

	public void setDataRows(String dataRows) {
		this.dataRows = dataRows;
	}

	public String getDataCols() {
		return dataCols;
	}

	public void setDataCols(String dataFields) {
		this.dataCols = dataFields;
	}

	public boolean isDenied() {
		return denied;
	}

	public void setDenied(boolean denied) {
		this.denied = denied;
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

	@Override
	public String getAuthority() {
		if (StringUtil.isBlank(menuKey))
			return menuKey;
		else if (StringUtil.isBlank(roleKey))
			return roleKey;

		return null;
	}

	public String getMenuActionsNames() {
		return menuActionsNames;
	}

	public void setMenuActionsNames(String menuActionsNames) {
		this.menuActionsNames = menuActionsNames;
	}

	public String getDataRowsNames() {
		return dataRowsNames;
	}

	public void setDataRowsNames(String dataRowsNames) {
		this.dataRowsNames = dataRowsNames;
	}

	public String getDataColsNames() {
		return dataColsNames;
	}

	public void setDataColsNames(String dataColsNames) {
		this.dataColsNames = dataColsNames;
	}

}
