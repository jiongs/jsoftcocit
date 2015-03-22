package com.jsoft.cocimpl.securityengine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jsoft.cocimpl.entityengine.EntityServiceFactory;
import com.jsoft.cocimpl.entityengine.impl.service.EmptySystemService;
import com.jsoft.cocimpl.entityengine.impl.service.EmptyTenantService;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICommonConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.constant.PrincipalTypes;
import com.jsoft.cocit.entity.security.IAuthority;
import com.jsoft.cocit.entity.security.ISystemMenu;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.entityengine.service.TenantService;
import com.jsoft.cocit.entityengine.service.UserService;
import com.jsoft.cocit.exception.CocDBException;
import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.securityengine.GrantedAuthority;
import com.jsoft.cocit.securityengine.LoginSession;
import com.jsoft.cocit.securityengine.SecurityEngine;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

public class LoginSessionImpl implements LoginSession {
	private static final long serialVersionUID = -2227024696863349919L;

	private Map<String, Object> cachedData = new HashMap();
	private List<IAuthority> authorities;
	private UserService user;
	private TenantService tenant;
	private SystemService system;
	private String username;
	private int userType;
	private double clientWidth = 1024.0;
	private double clientHeight = 768.0;
	private String loginLogKey;
	private boolean authenticated;

	public void release() {
		if (this.cachedData != null) {
			this.cachedData.clear();
			this.cachedData = null;
		}
		user = null;
		this.tenant = null;
		this.system = null;
		this.username = null;
		this.loginLogKey = null;
		authenticated = false;
	}

	LoginSessionImpl(SecurityEngine securityEngine, HttpServletRequest request, TenantService tenant, SystemService system, String userTypeKey, String username, String password) throws CocSecurityException {
		LogUtil.debug("创建登录信息对象......");

		this.username = username;

		// if (!StringUtil.isEmpty(realm)) {
		// IRealm realmObj = moduleEngine.getRealm(soft, realm);
		// if (realmObj != null && realmObj.getUserModule() != null)
		// this.module = realmObj.getUserModule().getId();
		// }

		if (StringUtil.isBlank(username)) {
			throw new CocSecurityException("10012");
		} else {
			user = securityEngine.checkUser(tenant, userTypeKey, username, password);

			this.tenant = tenant;
			this.system = system;

			Cocit coc = Cocit.me();
			ICommonConfig config = coc.getConfig();
			EntityServiceFactory sf = coc.getEntityServiceFactory();

			if (user instanceof RootUserService) {
				if (system == null || system instanceof EmptySystemService) {
					this.system = sf.getSystem(config.getCocitSystemKey());
				}
				if (tenant == null || tenant instanceof EmptyTenantService) {
					this.tenant = sf.getTenant(config.getCocitTenantKey());
				}
			} else {
				if (config.getCocitSystemKey().equals(system.getKey())) {
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

			Orm orm = Cocit.me().orm();

			CndExpr expr = Expr.eq("userKey", user.getUsername());
			if (system != null) {
				CndExpr sexpr = ExprUtil.systemIs(system.getKey());
				if (sexpr != null) {
					expr = expr.and(sexpr);
				}
			}
			if (tenant != null) {
				CndExpr texpr = ExprUtil.tenantIs(tenant.getKey());
				if (texpr != null) {
					expr = expr.and(texpr);
				}
			}

			this.authorities = (List<IAuthority>) orm.query(EntityTypes.Authority, expr);
		} catch (CocDBException e) {
			LogUtil.error("加载权限失败！%s", ExceptionUtil.msg(e));
		}
	}

	private void initClientParams(HttpServletRequest request) {

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

	public UserService getUser() {
		return user;
	}

	public TenantService getTenant() {
		return tenant;
	}

	public String getName() {
		return username;
	}

	public Object get(String key) {
		return cachedData.get(key);
	}

	public LoginSession set(String key, Object value) {
		cachedData.put(key, value);
		return this;
	}

	public int getUserType() {
		return userType;
	}

	public SystemService getSystem() {
		return system;
	}

	public double getBrowserWidth() {
		return clientWidth;
	}

	public double getBrowserHeight() {
		return clientHeight;
	}

	public String getLoginLogKey() {
		return loginLogKey;
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
		UserService user = this.getUser();

		if (user instanceof RootUserService//
		        || user.getRolesList().contains(PrincipalTypes.ROLE_ROOT)//
		) {
			if (system == null || system instanceof EmptySystemService)
				return root;

			for (SystemMenuService menu : system.getSystemMenus()) {

				if (menu.isDisabled() || menu.isHidden())
					continue;

				makeSystemMenuItem(root, menu, urlPrefix);
			}
		} else {

			for (IAuthority auth : this.authorities) {
				SystemMenuService menu = system.getSystemMenu(auth.getMenuKey());

				if (menu.isDisabled() || menu.isHidden())
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

	private void makeSystemMenuItem(Tree root, SystemMenuService menu, String entityUrlPrefix) {
		Node node;
		if (!StringUtil.isBlank(menu.getParentKey())) {
			node = root.addNode(menu.getParentKey(), menu.getKey());
		} else {
			node = root.addNode(null, menu.getKey());
		}
		if (node == null) {
			return;
		}

		// node.setParams(moduleID);
		node.setSn(menu.getSn());
		node.setName(menu.getName());
		// node.set("key", menu.getKey());
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

		List<SystemMenuService> parentMenus = new ArrayList();
		Map<String, Node> nodes = tree.getAllMap();
		for (Node node : nodes.values()) {
			Node parentNode = node.getParent();
			if (parentNode != null) {
				ISystemMenu menu = (ISystemMenu) node.getReferObj();
				String parentKey = menu.getParentKey();
				SystemMenuService parentMenu = this.system.getSystemMenu(parentKey);
				if (parentMenu != null) {
					parentMenus.add(parentMenu);
				} else {
					LogUtil.error("创建菜单父节点失败：没有找到系统(%s)菜单(%s)！", system.getKey(), parentKey);
				}
			}
		}

		if (parentMenus.size() > 0) {
			this.makeParentNodes(tree, parentMenus, ++depth);
		}
	}

	private void makeParentNodes(Tree tree, List<SystemMenuService> parentMenus, int depth) {
		for (ISystemMenu parentMenu : parentMenus) {
			String key = parentMenu.getKey();
			String parentKey = parentMenu.getParentKey();

			tree.addNode(parentKey, key).setName(parentMenu.getName()).setReferObj(parentMenu);
		}

		this.makeParentNodes(tree, depth);
	}

	@Override
	public String getUsername() {
		return getName();
	}

}
