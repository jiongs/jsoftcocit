package com.jsoft.cocit.constant;

public interface PrincipalTypes {

	/**
	 * 超级用户：不存储在数据库中
	 */
	static int USER_ROOT = 999;

	/**
	 * 匿名用户：匿名访问网站的人
	 */
	static int USER_ANONYMOUS = 0;

	/**
	 * 网站会员：网站会员登录即为此类型的用户
	 */
	static int USER_WEBSITE = 1;

	/**
	 * 系统(后台)用户：登录系统后台的用户
	 */
	static int USER_SYSTEM = 10;

	static int GROUP_NORMAL = 101;

	static int GROUP_ADMIN = 199;

	public static final String ROLE_ROOT = "$_coc_root_role_$";
	public static final String GROUP_ANYONE = "$_coc_any_one_$";
}
