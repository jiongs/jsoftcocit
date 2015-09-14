package com.jsoft.cocit.securityengine;

import java.io.Serializable;
import java.security.Principal;

/**
 * 认证对象：用来描述“权限主体对象(权限拥有者)被授予了些什么权限？”或“权限主体对象(权限拥有者)拥有哪些权限？”
 * 
 * @author Ji Yongshan
 * 
 */
public interface Authentication extends Principal, Serializable {

	/**
	 * 授予的权限列表
	 * 
	 * @return
	 */
	GrantedAuthority[] getAuthorities();

	/**
	 * 权限主体对象：即有用权限的“人、角色、组”等
	 * 
	 * @return
	 */
	Object getPrincipal();

	/**
	 * 检查是否已经认证过了？
	 * 
	 * @return
	 */
	boolean isAuthenticated();

	/**
	 * 设置认证状态
	 */
	void setAuthenticated(boolean authenticated);
}