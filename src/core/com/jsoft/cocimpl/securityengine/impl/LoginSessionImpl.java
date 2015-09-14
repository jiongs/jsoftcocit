package com.jsoft.cocimpl.securityengine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jsoft.cocimpl.dmengine.impl.info.EmptySystemInfo;
import com.jsoft.cocimpl.dmengine.impl.info.EmptyTenantInfo;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.baseentity.log.ILogLoginEntity;
import com.jsoft.cocit.baseentity.security.IPermissionEntity;
import com.jsoft.cocit.baseentity.security.ISystemMenuEntity;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.constant.PrincipalTypes;
import com.jsoft.cocit.dmengine.IEntityInfoFactory;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.dmengine.info.IUserInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.exception.CocDBException;
import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.securityengine.SecurityVoter;
import com.jsoft.cocit.securityengine.GrantedAuthority;
import com.jsoft.cocit.securityengine.ILoginSession;
import com.jsoft.cocit.securityengine.SecurityService;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

public class LoginSessionImpl implements ILoginSession {
	private static final long serialVersionUID = -2227024696863349919L;

	private Map<String, Object> cachedData = new HashMap();
	private List<IPermissionEntity> authorities;
	private IUserInfo user;
	private ITenantInfo tenant;
	private ISystemInfo system;
	private String username;
	private int userType;
	private double clientWidth = 1024.0;
	private double clientHeight = 768.0;
	private String loginLogCode;
	private boolean authenticated;
	private ILogLoginEntity loginLog;
	private String appURL;
	private Map<String, SecurityVoter> accessVoters;

	public void release() {
		if (this.cachedData != null) {
			this.cachedData.clear();
			this.cachedData = null;
		}
		user = null;
		this.tenant = null;
		this.system = null;
		this.username = null;
		this.loginLogCode = null;
		authenticated = false;
	}

	LoginSessionImpl(SecurityService securityEngine, HttpServletRequest request, ITenantInfo tenant, ISystemInfo system, String userTypeCode, String username, String password) throws CocSecurityException {
		LogUtil.debug("创建登录信息对象......");


		// if (!StringUtil.isEmpty(realm)) {
		// IRealm realmObj = moduleEngine.getRealm(soft, realm);
		// if (realmObj != null && realmObj.getUserModule() != null)
		// this.module = realmObj.getUserModule().getId();
		// }

		if (StringUtil.isBlank(username)) {
			throw new CocSecurityException("10012");
		} else {
			user = securityEngine.checkUser(tenant, userTypeCode, username, password);

			this.username = user.getCode();
			this.tenant = tenant;
			this.system = system;

			Cocit coc = Cocit.me();
			ICocConfig config = coc.getConfig();
			IEntityInfoFactory sf = coc.getEntityServiceFactory();

			if (user instanceof RootUserInfo) {
				if (system == null || system instanceof EmptySystemInfo) {
					this.system = sf.getSystem(config.getCocitSystemCode());
				}
				if (tenant == null || tenant instanceof EmptyTenantInfo) {
					this.tenant = sf.getTenant(config.getCocitTenantCode());
				}
			} else {
				if (tenant == null || tenant instanceof EmptyTenantInfo) {
					this.tenant = sf.getTenant(user.getTenantCode());
				}
				if (system == null || system instanceof EmptySystemInfo) {
					this.system = sf.getSystem(this.tenant.getSystemCode());
				}

				if (config.getCocitSystemCode().equals(system.getCode())) {
					throw new CocSecurityException("10006");
				}
			}
		}

		this.initAuthorities();
		this.initClientParams(request);

		LogUtil.debug("创建登录信息对象: 成功.");
	}

	private void initAuthorities() {
		try {
			userType = user.getPrincipalType();

			IOrm orm = Cocit.me().orm();

			CndExpr expr = Expr.eq("userCode", user.getUsername());
			if (system != null) {
				CndExpr sexpr = ExprUtil.systemIs(system.getCode());
				if (sexpr != null) {
					expr = expr.and(sexpr);
				}
			}
			if (tenant != null) {
				CndExpr texpr = ExprUtil.tenantIs(tenant.getCode());
				if (texpr != null) {
					expr = expr.and(texpr);
				}
			}

			this.authorities = (List<IPermissionEntity>) orm.query(EntityTypes.Authority, expr);
		} catch (CocDBException e) {
			LogUtil.error("加载权限失败！%s", ExceptionUtil.msg(e));
		}
	}

	private void initClientParams(HttpServletRequest request) {

		StringBuffer url = request.getRequestURL();
		int idx = url.indexOf("//") + 2;
		idx = idx + url.substring(idx).indexOf("/");
		this.appURL = url.substring(0, idx) + Cocit.me().getContextPath();

		String sClientWidth = request.getParameter(Const.REQUEST_KEY_CLIENT_WIDTH);
		String sClientHeight = request.getParameter(Const.REQUEST_KEY_CLIENT_HEIGHT);
		if (!StringUtil.isBlank(sClientWidth)) {
			try {
				clientWidth = Double.parseDouble(sClientWidth);
			} catch (Throwable e) {

			}
		}
		if (!StringUtil.isBlank(sClientHeight)) {
			try {
				clientHeight = Double.parseDouble(sClientHeight);
			} catch (Throwable e) {

			}
		}
	}

	public IUserInfo getUser() {
		return user;
	}

	public ITenantInfo getTenant() {
		return tenant;
	}

	public String getName() {
		return username;
	}

	public Object get(String key) {
		return cachedData.get(key);
	}

	public ILoginSession set(String key, Object value) {
		cachedData.put(key, value);
		return this;
	}

	public int getUserType() {
		return userType;
	}

	public ISystemInfo getSystem() {
		return system;
	}

	public double getBrowserWidth() {
		return clientWidth;
	}

	public double getBrowserHeight() {
		return clientHeight;
	}

	public String getLoginLogCode() {
		return loginLogCode;
	}

	@Override
	public GrantedAuthority[] getAuthorities() {
		GrantedAuthority[] ret = new GrantedAuthority[authorities.size()];
		int i = 0;
		for (GrantedAuthority a : authorities) {
			ret[i] = a;
			i++;
		}
		return ret;
	}

	@Override
	public Object getPrincipal() {
		return user;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public Tree makeFuncMenu(String urlPrefix) {

		Tree root = Tree.make();
		IUserInfo user = this.getUser();

		if (user instanceof RootUserInfo//
		        || user.getRolesList().contains(PrincipalTypes.ROLE_ROOT)//
		) {
			if (system == null || system instanceof EmptySystemInfo)
				return root;

			for (ISystemMenuInfo menu : system.getSystemMenus()) {

				if (menu.isDisabled() || menu.isHidden())
					continue;

				makeSystemMenuItem(root, menu, urlPrefix);
			}
		} else {

			for (IPermissionEntity auth : this.authorities) {
				ISystemMenuInfo menu = system.getSystemMenu(auth.getMenuCode());

				if (menu == null || menu.isDisabled() || menu.isHidden())
					continue;

				makeSystemMenuItem(root, menu, urlPrefix);
			}

			this.makeParentNodes(root, 0);
		}

		if (root.sizeAll() < 30) {
			for (Node node : root.getAll()) {
				node.set("open", "true");
			}
		} else {
			if (root.size() > 0) {
				root.getChildren().get(0).set("open", "true");
			}
		}

		return root;
	}

	private void makeSystemMenuItem(Tree root, ISystemMenuInfo menu, String entityUrlPrefix) {
		Node node;
		if (!StringUtil.isBlank(menu.getParentCode())) {
			node = root.addNode(menu.getParentCode(), menu.getCode());
		} else {
			node = root.addNode(null, menu.getCode());
		}
		if (node == null) {
			return;
		}

		// node.setParams(moduleID);
		node.setSn(menu.getSn());
		node.setName(menu.getName());
		// node.set("code", menu.getCode());
		node.setReferObj(menu);

		switch (menu.getType()) {
			case Const.MENU_TYPE_FOLDER:
				break;
			case Const.MENU_TYPE_ENTITY:
				if (StringUtil.isBlank(menu.getPath())) {
					node.set("linkURL", MVCUtil.makeUrl(entityUrlPrefix, menu));
				} else {
					node.set("linkURL", MVCUtil.makeUrl(menu.getPath()));
				}
				break;
			case Const.MENU_TYPE_STATIC:
				node.set("linkURL", MVCUtil.makeUrl(menu.getPath()));
				break;
		}
	}

	private void makeParentNodes(Tree tree, int depth) {
		if (depth > 5) {
			return;
		}

		List<ISystemMenuInfo> parentMenus = new ArrayList();
		Map<String, Node> nodes = tree.getAllMap();
		for (Node node : nodes.values()) {
			Node parentNode = node.getParent();
			if (parentNode != null) {
				ISystemMenuEntity menu = (ISystemMenuEntity) node.getReferObj();
				String parentCode = menu.getParentCode();
				ISystemMenuInfo parentMenu = this.system.getSystemMenu(parentCode);
				if (parentMenu != null) {
					parentMenus.add(parentMenu);
				} else {
					LogUtil.error("创建菜单父节点失败：没有找到系统(%s)菜单(%s)！", system.getCode(), parentCode);
				}
			}
		}

		if (parentMenus.size() > 0) {
			this.makeParentNodes(tree, parentMenus, ++depth);
		}
	}

	private void makeParentNodes(Tree tree, List<ISystemMenuInfo> parentMenus, int depth) {
		for (ISystemMenuEntity parentMenu : parentMenus) {
			String key = parentMenu.getCode();
			String parentCode = parentMenu.getParentCode();

			tree.addNode(parentCode, key).setName(parentMenu.getName()).setReferObj(parentMenu);
		}

		this.makeParentNodes(tree, depth);
	}

	@Override
	public String getUsername() {
		return getName();
	}

	public ILogLoginEntity getLoginLog() {
		return loginLog;
	}

	public void setLoginLog(ILogLoginEntity loginLog) {
		if (loginLog != null) {
			this.loginLogCode = loginLog.getCode();
		}
		this.loginLog = loginLog;
	}

	public String getAppURL() {
		return appURL;
	}

	@Override
	public void putAccessVoter(String voterCode, SecurityVoter voter) {
		if (accessVoters == null) {
			accessVoters = new HashMap();
		}

		this.accessVoters.put(voterCode, voter);
	}

	@Override
	public SecurityVoter removeAccessVoter(String voterCode) {
		if (accessVoters != null) {
			return accessVoters.remove(voterCode);
		}

		return null;
	}
}
