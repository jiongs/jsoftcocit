package com.jsoft.cocit.securityengine;

/**
 * 定义“被授权对象”：即定义哪些对象必须授权才能访问？
 * 
 * @author Ji Yongshan
 * 
 */
public interface AuthorizedObjectDefinition {

	/**
	 * 获取“被授权对象”：即该列表中的对象需要授权才能访问。
	 * 
	 * @return
	 */
	public AuthorizedObject[] getAuthorizedObjects();
}