package com.jsoft.cocit.securityengine;

import javax.servlet.http.HttpServletRequest;

import com.jsoft.cocimpl.securityengine.RootUserFactory;
import com.jsoft.cocit.entity.security.ISystemMenu;
import com.jsoft.cocit.entity.security.ITenant;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.entityengine.service.TenantService;
import com.jsoft.cocit.entityengine.service.UserService;
import com.jsoft.cocit.orm.expr.CndExpr;

/**
 * Cocit安全管理器接口
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface SecurityEngine {

	RootUserFactory getRootUserFactory();

	/**
	 * 检查登录用户是否为超级用户?
	 * 
	 * @param login
	 *            登录信息
	 * @return
	 */
	boolean isRootUser(LoginSession login);

	void checkLoginUserType(int userType);

	// ***登录 API***

	LoginSession login(HttpServletRequest request, TenantService tenant, SystemService system, String realm, String username, String password);

	LoginSession logout(HttpServletRequest request, TenantService tenant, SystemService system);

	LoginSession getLoginSession(HttpServletRequest request);

	// ***用户 API***

	UserService checkUser(ITenant tenant, String realm, String username, String password);

	PasswordEncoder getPasswordEncoder();

	void release();

	boolean allowAccessMenu(LoginSession login, SystemMenuService menu);

	boolean allowAccessAction(LoginSession login, SystemMenuService menu, String actionMode);

	CndExpr getDataFilter(SystemMenuService systemMenu);

	CndExpr getFkDataFilter(ISystemMenu funcMenu, String fkField);
}
