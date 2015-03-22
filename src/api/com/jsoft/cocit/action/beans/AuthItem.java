package com.jsoft.cocit.action.beans;

import java.util.Date;
import java.util.List;

import com.jsoft.cocit.entityengine.service.CocActionService;

/**
 * 菜单权限条目
 */
public class AuthItem {

	protected String systemKey;
	protected String systemName;

	protected String id;

	/*
	 * 主体：用户、用户组等
	 */
	protected String userKey;
	protected String userName;
	protected String groupKey;
	protected String groupNme;

	/*
	 * 功能：菜单、角色等
	 */
	protected String menuKey;
	protected String menuName;
	protected String roleKey;
	protected String roleName;
	protected String menuParentKey;

	/*
	 * 操作权限、数据权限、字段权限
	 */
	protected String actionsAuthUrl;
	protected List<CocActionService> actionsAuth;
	protected String rowsAuthUrl;
	protected String colsAuthUrl;

	/*
	 * 权限有效期
	 */
	protected boolean denied;
	protected String deniedName;
	protected Date expiredFrom;
	protected Date expiredTo;

	protected int statusCode;

	/*
	 * 
	 */
	protected boolean allowMenu;
	// 格式：操作编号列表，多个操作之间用"|"分隔。如：c|e|d
	protected String allowActions;
	protected String allowActionsNames;
	// 格式：JSON表达式或伪SQL表达式，目前仅支持JSON表达式。如：{field1:[1,2,3,4,5], field2:['aa', 'bb', 'cc']}
	protected String allowRows;
	protected String allowRowsNames;
	protected String allowCols;
	protected String allowColsNames;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}

	public String getGroupNme() {
		return groupNme;
	}

	public void setGroupNme(String groupNme) {
		this.groupNme = groupNme;
	}

	public String getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(String menuKey) {
		this.menuKey = menuKey;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRowsAuthUrl() {
		return rowsAuthUrl;
	}

	public void setRowsAuthUrl(String dataRows) {
		this.rowsAuthUrl = dataRows;
	}

	public String getColsAuthUrl() {
		return colsAuthUrl;
	}

	public void setColsAuthUrl(String dataFields) {
		this.colsAuthUrl = dataFields;
	}

	public boolean isDenied() {
		return denied;
	}

	public void setDenied(boolean denied) {
		this.denied = denied;
	}

	public String getDeniedName() {
		return deniedName;
	}

	public void setDeniedName(String deniedName) {
		this.deniedName = deniedName;
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

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getMenuParentKey() {
		return menuParentKey;
	}

	public void setMenuParentKey(String menuParentKey) {
		this.menuParentKey = menuParentKey;
	}

	public String getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = "" + id;
	}

	public void setId(String id) {
		this.id = "" + id;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isAllowMenu() {
		return allowMenu;
	}

	public void setAllowMenu(boolean allowMenu) {
		this.allowMenu = allowMenu;
	}

	public String getAllowActions() {
		return allowActions;
	}

	public void setAllowActions(String allowActions) {
		this.allowActions = allowActions;
	}

	public String getAllowRows() {
		return allowRows;
	}

	public void setAllowRows(String allowDatas) {
		this.allowRows = allowDatas;
	}

	public String getActionsAuthUrl() {
		return actionsAuthUrl;
	}

	public void setActionsAuthUrl(String actionsAuthUrl) {
		this.actionsAuthUrl = actionsAuthUrl;
	}

	public String getAllowActionsNames() {
		return allowActionsNames;
	}

	public void setAllowActionsNames(String allowActionsNames) {
		this.allowActionsNames = allowActionsNames;
	}

	public String getAllowRowsNames() {
		return allowRowsNames;
	}

	public void setAllowRowsNames(String allowRowsNames) {
		this.allowRowsNames = allowRowsNames;
	}

	public String getAllowCols() {
		return allowCols;
	}

	public void setAllowCols(String allowCols) {
		this.allowCols = allowCols;
	}

	public String getAllowColsNames() {
		return allowColsNames;
	}

	public void setAllowColsNames(String allowColsNames) {
		this.allowColsNames = allowColsNames;
	}

	public List<CocActionService> getActionsAuth() {
		return actionsAuth;
	}

	public void setActionsAuth(List<CocActionService> actionsAuth) {
		this.actionsAuth = actionsAuth;
	}
}