package com.jsoft.cocit.securityengine;

import java.io.Serializable;

/**
 * 授予的权限：如“菜单编号、资源相对路径”等。
 * 
 * @author Ji Yongshan
 * 
 */
public interface GrantedAuthority extends Serializable {

	/**
	 * 获取“授予的权限”：如“菜单编号、资源相对路径”等。
	 * 
	 * @return
	 */
	String getAuthority();
}