package com.jsoft.cocimpl.securityengine.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jsoft.cocimpl.securityengine.RootUserFactory;
import com.jsoft.cocimpl.securityengine.impl.encoder.PasswordEncoderMD5;
import com.jsoft.cocimpl.securityengine.impl.voter.SecurityVoter4UserMenu;
import com.jsoft.cocimpl.securityengine.impl.voter.SecurityVoter4UserRole;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.baseentity.security.IPermissionEntity;
import com.jsoft.cocit.baseentity.security.IRoleEntity;
import com.jsoft.cocit.baseentity.security.ISystemMenuEntity;
import com.jsoft.cocit.baseentity.security.ITenantEntity;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.dmengine.info.IUserInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.exception.CocDBException;
import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.exception.CocUnloginException;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.securityengine.SecurityVoterManager;
import com.jsoft.cocit.securityengine.SecurityVoter;
import com.jsoft.cocit.securityengine.ILoginSession;
import com.jsoft.cocit.securityengine.PasswordEncoder;
import com.jsoft.cocit.securityengine.SecurityService;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @preserve public
 * @author Ji Yongshan
 * 
 */
public class SecurityServiceImpl implements SecurityService {
	private PasswordEncoder passwordEncoder;
	private RootUserFactory rootUserFactory;

	private List<SecurityVoterManager> acessDecisionManagers;

	// // <systemID,<menuID, Permission>>
	// private Map<String, Map<String, List<PermissionItem>>> systemMenuPermissions;
	// // <systemID,<roleID, Permission>>
	// private Map<String, Map<String, List<PermissionItem>>> systemRolePermissions;

	public SecurityServiceImpl() {
		passwordEncoder = new PasswordEncoderMD5();
		rootUserFactory = new RootUserFactoryImpl();
		acessDecisionManagers = new ArrayList();
		// systemMenuPermissions = new HashMap();
		// systemRolePermissions = new HashMap();

		setupAccessDecisionManagers();
	}

	public void release() {
		// systemMenuPermissions.clear();
		// systemRolePermissions.clear();
		rootUserFactory.release();
	}

	private void setupAccessDecisionManagers() {
		VoterManagerUnanimous manager = new VoterManagerUnanimous();

		List<SecurityVoter> voters = new ArrayList();
		voters.add(new SecurityVoter4UserMenu());
		voters.add(new SecurityVoter4UserRole());

		manager.setDecisionVoters(voters);

		acessDecisionManagers.add(manager);
	}

	// ===============================================================================================================
	// 登录相关的API实现
	// ===============================================================================================================
	public PasswordEncoder getPwdEncoder(String encoder, PasswordEncoder defaultPE) {
		return defaultPE;
	}

	protected String genLoginSessionKey() {
		// if (tenant == null) {
		return Const.SESSION_KEY_LOGIN_SESSION;
		// }
		// return tenant.getId() + "." + SEConsts.SESSION_KEY_LOGIN_SESSION;
	}

	public ILoginSession login(HttpServletRequest request, ITenantInfo tenant, ISystemInfo system, String realm, String username, String password) throws CocSecurityException {
		HttpSession session = request.getSession();
		LoginSessionImpl login = new LoginSessionImpl(this, request, tenant, system, realm, username, password);
		session.setAttribute(genLoginSessionKey(), login);

		if (login.getUserType() > 0) {
			session.setAttribute(Const.SESSION_KEY_CKFINDER_USER_ROLE, "" + login.getUserType());
		}

		return login;
	}

	public ILoginSession getLoginSession(HttpServletRequest request) {
		return (ILoginSession) request.getSession(true).getAttribute(genLoginSessionKey());
	}

	public ILoginSession logout(HttpServletRequest request, ITenantInfo tenant, ISystemInfo system) {
		HttpSession session = request.getSession();
		String key = genLoginSessionKey();
		ILoginSession login = (ILoginSession) session.getAttribute(key);
		session.removeAttribute(key);

		return login;
	}

	public IUserInfo checkUser(ITenantEntity tenant, String userTypeKey, String username, String pwd) throws CocSecurityException {
		LogUtil.debug("获取用户......[soft=%s, realm=%s, user=%s]", tenant, userTypeKey, username);

		IUserInfo user = null;

		/*
		 * 获取指定的用户
		 */
		Class typeOfUser = EntityTypes.SystemUser;
		if (!StringUtil.isBlank(userTypeKey)) {
			ICocEntityInfo userModule = Cocit.me().getEntityServiceFactory().getEntity(userTypeKey);
			if (userModule != null)
				typeOfUser = userModule.getClassOfEntity();
		}

		if (typeOfUser == null) {
			throw new CocSecurityException("用户类型不存在：[%s]", userTypeKey);
		}

		try {
			user = Cocit.me().getEntityServiceFactory().getUserByCodeOrTel(tenant.getCode(), username);
		} catch (CocDBException e) {
		}

		if (user == null) {
			user = rootUserFactory.getRootUser(username);
		}

		/*
		 * 检查用户是否存在
		 */
		if (user == null) {
			throw new CocSecurityException("10007", username);
		}

		/*
		 * 检查用户是否被禁用
		 */
		if (user.isDisabled()) {
			throw new CocSecurityException("10008", username);
		}

		/*
		 * 检查用户是否被锁定
		 */
		if (user.isLocked()) {
			throw new CocSecurityException("10009", username);
		}

		/*
		 * 检查帐号有效期
		 */
		Date now = new Date();
		Date from = user.getExpiredFrom();
		Date to = user.getExpiredTo();
		if ((from != null && now.getTime() < from.getTime()) || (to != null && now.getTime() > to.getTime())) {
			throw new CocSecurityException("10010", username);
		}

		/*
		 * 检查用户密码
		 */
		if (!passwordEncoder.isValidPassword(user.getPassword(), pwd, username)) {
			throw new CocSecurityException("10011", username);
		}

		LogUtil.debug("获取用户: 结束. [user=%s]", user);

		return user;
	}

	public boolean isRootUser(ILoginSession login) {
		if (login.getTenant() != null) {
			return false;
		}

		return this.rootUserFactory.getRootUser(login.getName()) != null;
	}

	public void checkLoginUserType(int userType) throws CocSecurityException {
		HttpContext ctx = Cocit.me().getHttpContext();
		ILoginSession login = ctx.getLoginSession();
		if (login == null) {
			ctx.getRequest().getSession().setAttribute(Const.SESSION_KEY_LATEST_URI, MVCUtil.getURI(ctx.getRequest()));
			throw new CocUnloginException("尚未登录或登录已过期！");
		}
		if (login.getUserType() < userType)
			throw new CocSecurityException("你没有足够的权限！");
	}

	public RootUserFactory getRootUserFactory() {
		return rootUserFactory;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	// =========================================================================================================
	// 以上为登录相关的API实现
	// ==========================================================================================================

	// =========================================================================================================
	// 授权管理系统相关API的实现
	// ==========================================================================================================

	@Override
	public boolean allowAccessMenu(ILoginSession login, ISystemMenuInfo menu) {
		ISystemInfo system = Cocit.me().getEntityServiceFactory().getSystem(login.getSystem().getCode());

		for (SecurityVoterManager adm : this.acessDecisionManagers) {
			adm.decide(login, menu, system);
		}

		return true;
	}

	@Override
	public boolean allowAccessAction(ILoginSession login, ISystemMenuInfo menu, String actionMode) {
		if(login==null){
			throw new CocSecurityException("您尚未登录或登录已过期，请先登录！");
		}
		
		ISystemInfo system = Cocit.me().getEntityServiceFactory().getSystem(login.getSystem().getCode());

		for (SecurityVoterManager adm : this.acessDecisionManagers) {
			if (!adm.supports(login.getClass())) {
				continue;
			}

			adm.decide(login, menu, system);
		}

		return true;
	}

	@Override
	public CndExpr getDataFilter(ISystemMenuInfo systemMenu) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CndExpr getFkDataFilter(ISystemMenuEntity funcMenu, String fkField) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public List<IPermissionEntity> getAuthorities(IRoleEntity role) {
	    // TODO Auto-generated method stub
	    return null;
    }

}
