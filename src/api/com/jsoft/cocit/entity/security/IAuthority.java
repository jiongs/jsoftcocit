package com.jsoft.cocit.entity.security;

import java.util.Date;

import com.jsoft.cocit.entity.ITenantOwnerEntity;
import com.jsoft.cocit.securityengine.GrantedAuthority;

public interface IAuthority extends ITenantOwnerEntity, GrantedAuthority {

	String getSystemKey();

	String getUserKey();

	String getGroupKey();

	String getRoleKey();

	String getMenuKey();

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
