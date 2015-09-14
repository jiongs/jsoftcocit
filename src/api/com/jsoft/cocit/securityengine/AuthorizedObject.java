package com.jsoft.cocit.securityengine;

import java.io.Serializable;

/**
 * 被授权对象：即需要授权才能访问的对象，如“网站资源、菜单”等。
 */
public interface AuthorizedObject extends Serializable {

	/**
	 * “被授权对象”的名称
	 * 
	 * @return
	 */
	String getAuthorizedName();
}