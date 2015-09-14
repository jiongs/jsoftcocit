package com.jsoft.cocit.securityengine;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jsoft.cocimpl.securityengine.RootUserFactory;
import com.jsoft.cocit.baseentity.security.IPermissionEntity;
import com.jsoft.cocit.baseentity.security.IRoleEntity;
import com.jsoft.cocit.baseentity.security.ISystemMenuEntity;
import com.jsoft.cocit.baseentity.security.ITenantEntity;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.dmengine.info.IUserInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.orm.expr.CndExpr;

/**
 * Cocit安全管理器接口
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface SecurityService {

	RootUserFactory getRootUserFactory();

	/**
	 * 检查登录用户是否为超级用户?
	 * 
	 * @param login
	 *            登录信息
	 * @return
	 */
	boolean isRootUser(ILoginSession login);

	void checkLoginUserType(int userType);

	// ***登录 API***

	ILoginSession login(HttpServletRequest request, ITenantInfo tenant, ISystemInfo system, String realm, String username, String password);

	ILoginSession logout(HttpServletRequest request, ITenantInfo tenant, ISystemInfo system);

	ILoginSession getLoginSession(HttpServletRequest request);

	// ***用户 API***

	IUserInfo checkUser(ITenantEntity tenant, String realm, String username, String password);

	PasswordEncoder getPasswordEncoder();

	void release();

	boolean allowAccessMenu(ILoginSession login, ISystemMenuInfo menu);

	boolean allowAccessAction(ILoginSession login, ISystemMenuInfo menu, String actionCode);

	List<IPermissionEntity> getAuthorities(IRoleEntity role);

	CndExpr getDataFilter(ISystemMenuInfo systemMenu);

	CndExpr getFkDataFilter(ISystemMenuEntity funcMenu, String fkField);
}
