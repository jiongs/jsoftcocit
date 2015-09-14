package com.jsoft.cocit.baseentity.security;

import java.util.Date;

import com.jsoft.cocit.baseentity.IOfTenantEntity;
import com.jsoft.cocit.securityengine.GrantedAuthority;

public interface IPermissionEntity extends IOfTenantEntity, GrantedAuthority {

	String getSystemCode();

	String getUserCode();

	String getGroupCode();

	String getRoleCode();

	String getMenuCode();

	String getMenuActions();

	String getMenuActionsNames();

	/**
	 * 列(字段)权限：多字段之间用 | 分隔。
	 */
	String getDataCols();

	String getDataColsNames();

	/**
	 * 数据行权限：数据查询过滤表达式。
	 */
	String getDataRows();

	String getDataRowsNames();

	boolean isDenied();

	Date getExpiredFrom();

	Date getExpiredTo();
}
