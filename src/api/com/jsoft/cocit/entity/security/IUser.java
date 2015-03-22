package com.jsoft.cocit.entity.security;

import java.util.Date;

/**
 * <b> 用户: </b> 使用Cocit系统功能的人。
 * <p>
 * <b>用户可以有一下几种方式使用Cocit提供的功能</b>
 * <p>
 * 1. 任何人均可使用授权给特殊功能角色（任何人）的功能模块
 * <p>
 * 2. 拥有特殊功能角色（超级用户）的人可以使用Cocit提供的所有功能模块
 * <p>
 * 3. 上述两种以外的人可以根据其拥有的功能角色来使用对应的功能模块
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface IUser extends IPrincipal {

	/**
	 * 用户名：是{@link #getKey()}的别名。
	 * 
	 * @return
	 */
	String getUsername();

	/**
	 * 密码：加密后的密码
	 * 
	 * @return
	 */
	String getPassword();

	String getImage();

	String getLogo();

	String getRawPassword();

	Date getExpiredFrom();

	Date getExpiredTo();

	boolean isLocked();

	String getRoles();
}
