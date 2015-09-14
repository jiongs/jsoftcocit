package com.jsoft.cocit.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jsoft.cocimpl.securityengine.RootUserFactory;
import com.jsoft.cocimpl.securityengine.impl.RootUserInfo;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.ExtHttpContext;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.baseentity.log.ILogLoginEntity;
import com.jsoft.cocit.baseentity.security.ISystemUserEntity;
import com.jsoft.cocit.baseentity.security.ITenantEntity;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.constant.CocUrl;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.dmengine.IEntityInfoFactory;
import com.jsoft.cocit.dmengine.info.IUserInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.exception.CocConfigException;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.securityengine.ILoginSession;
import com.jsoft.cocit.securityengine.SecurityService;
import com.jsoft.cocit.service.LogService;
import com.jsoft.cocit.ui.model.SmartyModel;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.ui.model.datamodel.AlertModel;
import com.jsoft.cocit.ui.model.datamodel.JSONModel;
import com.jsoft.cocit.ui.model.datamodel.UITreeData;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.HttpUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

public abstract class BaseAdminAction {

	protected String resourcePath = "/admin";
	protected String adminUrl = "/admin";
	protected int topHeight = 98;
	protected int bottomHeight = 0;
	protected int leftWidth = 240;
	/**
	 * 框架之间的间距
	 */
	protected int frameGap = 0;
	/**
	 * 内容区域高度差
	 */
	protected int bodyHGap = 10;
	/**
	 * 内容区域宽度差
	 */
	protected int bodyWGap = 5;
	protected int bodyTabsSpaceHeight = 58;
	protected int bodyTabsSpaceWidth = 20;
	protected int contentPadding = 10;

	public BaseAdminAction() {
		this.init();
	}

	protected abstract void init();

	public UIModel login(String funcExpr) {

		String[] array = MVCUtil.decodeArgs(funcExpr);

		String tenantKey = array.length > 0 ? array[0] : null;
		String systemKey = array.length > 1 ? array[1] : null;

		Cocit coc = Cocit.me();
		ExtHttpContext httpContext = (ExtHttpContext) coc.getHttpContext();
		HttpServletRequest request = httpContext.getRequest();
		IEntityInfoFactory serviceFactory = Cocit.me().getEntityServiceFactory();
		IMessageConfig messages = coc.getMessages();
		LogService logService = coc.getLogService();

		if (StringUtil.isBlank(systemKey)) {
			systemKey = request.getParameter(Const.REQUEST_KEY_SYSTEM);
		}
		if (StringUtil.isBlank(tenantKey)) {
			tenantKey = request.getParameter(Const.REQUEST_KEY_TENANT);
		}

		ITenantInfo tenant = serviceFactory.getTenant(tenantKey);
		ISystemInfo system = serviceFactory.getSystem(systemKey);
		// if (StringUtil.isBlank(systemKey)) {
		// system = tenant.getSystem();
		// }
		// if (systemKey != null) {
		// system =
		// }

		String realm = request.getParameter(Const.REQUEST_KEY_REALM);
		String user = request.getParameter(Const.REQUEST_KEY_USER);
		String pwd = request.getParameter(Const.REQUEST_KEY_PWD);
		String valcode = request.getParameter(Const.REQUEST_VAL_CODE);

		/*
		 * 返回“登录”页面
		 */
		if (user == null || pwd == null) {
			LogUtil.debug("访问后台登录页面...");

			Map context = new HashMap();

			if (StringUtil.isBlank(tenantKey)) {
				context.put("loginUrl", adminUrl + "/login");
			} else {
				context.put("loginUrl", adminUrl + "/login/" + funcExpr);
			}
			context.put("adminUrl", adminUrl);
			context.put("resourcePath", resourcePath);
			context.put("logoutUrl", adminUrl + "/logout");
			context.put("copyright", Cocit.me().getConfig().get("copyright"));

			context.put("loginSession", httpContext.getLoginSession());
			context.put("cocconfig", coc.getConfig());

			/*
			 * 网页标题
			 */
			String title = messages.getMsg("10001", system.getName());
			context.put("title", title);

			/*
			 * 创建登录模型
			 */
			SmartyModel model = SmartyModel.make(httpContext.getRequest(), httpContext.getResponse(), Const.ST_DIR, resourcePath + "/admin_login", context);
			model.setTitle(title);

			/*
			 * 返回登录模型
			 */
			return model;
		}
		/*
		 * 返回“登录”结果
		 */
		else {
			LogUtil.debug("登录...[tenantKey=%s]", tenantKey);

			try {

				if (!httpContext.isLAN() || coc.getConfig().isProductMode()) {
					HttpUtil.checkImgVerifyCode(request, valcode, "您输入的验证码不对！");
				}

				SecurityService securityEngine = Cocit.me().getSecurityEngine();
				ILoginSession loginSession = securityEngine.login(request, tenant, system, realm, user, pwd);

				String uri = (String) request.getSession().getAttribute(Const.SESSION_KEY_LATEST_URI);
				request.getSession().removeAttribute(Const.SESSION_KEY_LATEST_URI);

				LogUtil.debug("登录成功. [%s]", loginSession);

				AlertModel alert = AlertModel.makeSuccess("登录成功");
				alert.set(Const.SESSION_KEY_LATEST_URI, uri);

				HttpUtil.removeImgVarifyCode(request);

				ILogLoginEntity loginLog = logService.makeLoginLog(system == null ? "" : system.getCode(), user, LogService.STATUS_SUCCESS);
				loginSession.setLoginLog(loginLog);

				return alert;
			} catch (Throwable e) {
				String info = ExceptionUtil.msg(e);
				LogUtil.debug("登录失败! %s", info);

				AlertModel alert = AlertModel.makeError(info);

				logService.makeLoginLog(system == null ? "" : system.getCode(), user, LogService.STATUS_FAILED);

				return alert;
			}
		}
	}

	public JSONModel logout(String tenantKey) {
		LogUtil.debug("注销...");

		HttpContext context = Cocit.me().getHttpContext();
		ILoginSession loginSession = context.getLoginSession();
		Cocit.me().getSecurityEngine().logout(context.getRequest(), context.getLoginTenant(), context.getLoginSystem());

		LogUtil.debug("注销成功. [%s]", loginSession);
		return AlertModel.makeSuccess("注销成功！");
	}

	public UIModel admin() {
		Cocit coc = Cocit.me();
		IMessageConfig messages = coc.getMessages();
		SecurityService securityEngine = coc.getSecurityEngine();
		securityEngine.checkLoginUserType(Const.USER_SYSTEM);

		ExtHttpContext context = (ExtHttpContext) Cocit.me().getHttpContext();
		ILoginSession loginSession = context.getLoginSession();
		IUserInfo user = loginSession.getUser();
		ISystemInfo system = loginSession.getSystem();

		/*
		 * 准备“全局”变量
		 */
		Map data = new HashMap();
		String title = messages.getMsg("10002", system.getName());
		data.put("title", title);

		/*
		 * 准备“顶部和底部”链接地址
		 */
		data.put("topUrl", MVCUtil.makeUrl(adminUrl + "/top", ""));
		data.put("bottomUrl", MVCUtil.makeUrl(adminUrl + "/bottom", ""));
		/*
		 * 准备“左边菜单树”链接地址
		 */
		Tree tree = loginSession.makeFuncMenu("");
		List<Node> menus = tree.getChildren();
		String parentMenuID = "";
		if (menus.size() > 0) {
			parentMenuID = menus.get(menus.size() - 1).getId();
		}
		data.put("leftUrl", MVCUtil.makeUrl(adminUrl + "/left/" + parentMenuID, ""));
		/*
		 * 准备“右边内容页”链接地址
		 */
		String bodyDefaultUrl = "#";
		if (user instanceof RootUserInfo) {
			bodyDefaultUrl = MVCUtil.makeUrl(adminUrl + "/config");
		} else {
			bodyDefaultUrl = MVCUtil.makeUrl(user.getConfigItem("", "#"));
		}
		data.put("bodyDefaultUrl", bodyDefaultUrl);
		data.put("bodyUrl", MVCUtil.makeUrl(adminUrl + "/body", ""));

		/*
		 * 准备“框架”尺寸
		 */
		data.put("topHeight", topHeight);
		data.put("bottomHeight", bottomHeight);
		data.put("leftWidth", leftWidth);
		data.put("frameGap", frameGap);

		/*
		 * 缓存“浏览器窗口尺寸”到SESSION中
		 */
		int bodyHeight = context.getBrowserHeight() - topHeight - bottomHeight - bodyHGap;
		int bodyWidth = context.getBrowserWidth() - leftWidth - bodyWGap;
		loginSession.set(Const.CONFIG_KEY_UI_BODYHEIGHT, bodyHeight);
		loginSession.set(Const.CONFIG_KEY_UI_BODYWIDTH, bodyWidth);
		loginSession.set(Const.CONFIG_KEY_UI_TOPHEIGHT, topHeight);
		loginSession.set(Const.CONFIG_KEY_UI_LEFTWIDTH, leftWidth);
		int contentWidth = bodyWidth - bodyTabsSpaceWidth - contentPadding * 2;
		int contentHeight = bodyHeight - bodyTabsSpaceHeight - contentPadding * 2;
		loginSession.set(Const.CONFIG_KEY_UI_CONTENTWIDTH, contentWidth);
		loginSession.set(Const.CONFIG_KEY_UI_CONTENTHEIGHT, contentHeight);
		loginSession.set(Const.CONFIG_KEY_UI_CONTENTPADDING, contentPadding);

		return SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, resourcePath + "/admin", data).setAjax(true);
	}

	public UIModel top() throws CocException {
		LogUtil.debug("访问后台顶部...");

		SecurityService securityEngine = Cocit.me().getSecurityEngine();
		securityEngine.checkLoginUserType(Const.USER_SYSTEM);

		HttpContext context = Cocit.me().getHttpContext();
		ILoginSession loginSession = context.getLoginSession();
		ITenantInfo tenant = loginSession.getTenant();
		IUserInfo user = loginSession.getUser();

		Map var = new HashMap();

		/*
		 * 设置环境变量
		 */
		var.put("loginSession", loginSession);

		/*
		 * 计算框架右边URL
		 */
		String bodyDefaultUrl = "#";
		if (user instanceof RootUserInfo) {
			bodyDefaultUrl = MVCUtil.makeUrl(adminUrl + "/config");
		} else {
			bodyDefaultUrl = MVCUtil.makeUrl(user.getConfigItem("", "#"));
		}
		var.put("bodyDefaultUrl", bodyDefaultUrl);
		var.put("adminUrl", adminUrl);
		var.put("resourcePath", resourcePath);

		/*
		 * 获取一级菜单
		 */
		Tree tree = loginSession.makeFuncMenu("");
		List<Node> menus = tree.getChildren();
		if (menus.size() > 0 && leftWidth > 100) {
			menus.get(menus.size() - 1).setStatusCode("open");
		}
		var.put("menus", menus);

		/*
		 * 计算TOP高度
		 */
		var.put("topHeight", (String) tenant.getConfigItem("ui.topHeight", "" + topHeight));

		return SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, resourcePath + "/admin_top", var);
	}

	public UIModel left(String parentMenuID) throws CocException {
		String title = "访问后台功能菜单";
		LogUtil.debug("%s....", title);

		SecurityService securityEngine = Cocit.me().getSecurityEngine();
		securityEngine.checkLoginUserType(Const.USER_SYSTEM);

		HttpContext context = Cocit.me().getHttpContext();
		ILoginSession loginSession = context.getLoginSession();

		try {

			Map var = new HashMap();
			Tree tree = loginSession.makeFuncMenu(CocUrl.ENTITY_GET_MAIN_UI);
			List<Node> menus = tree.getChildren();
			if (!StringUtil.isBlank(parentMenuID)) {
				for (Node node : menus) {
					if (node.getId().equals(parentMenuID)) {
						menus = node.getChildren();
						break;
					}
				}
			}

			int count2 = 0;
			int count3 = 0;
			for (Node menu2 : menus) {
				/*
				 * 展开二级菜单中的第一个菜单
				 */
				if (count2 == 0) {
					menu2.setStatusCode("open");
				}

				count3 = 0;
				for (Node menu3 : menu2.getChildren()) {
					/*
					 * 展开三级菜单中的第一个菜单
					 */
					if (count3 == 0) {
						menu3.setStatusCode("open");
						break;
					}
				}

				count2++;
			}

			var.put("menus", menus);
			var.put("adminUrl", adminUrl);
			var.put("resourcePath", resourcePath);
			double h = loginSession.getBrowserHeight();
			var.put("leftHeight", h - topHeight - bottomHeight);
			var.put("leftWidth", leftWidth);

			return SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, resourcePath + "/admin_left", var);
		} catch (CocConfigException e) {
			throw e;
		} catch (CocSecurityException e) {
			throw e;
		} catch (Throwable e) {
			String msg = String.format("%s出错： %s", title, ExceptionUtil.msg(e));
			LogUtil.error(msg, e);

			throw new CocException(msg);
		}
	}

	public UIModel body() throws CocException {
		HttpContext context = Cocit.me().getHttpContext();

		SecurityService securityEngine = Cocit.me().getSecurityEngine();
		securityEngine.checkLoginUserType(Const.USER_SYSTEM);
		ILoginSession loginSession = context.getLoginSession();

		Map var = new HashMap();
		var.put("adminUrl", adminUrl);
		var.put("resourcePath", resourcePath);

		var.put("bodyHeight", loginSession.get(Const.CONFIG_KEY_UI_BODYHEIGHT));
		var.put("bodyWidth", loginSession.get(Const.CONFIG_KEY_UI_BODYWIDTH));

		return SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, resourcePath + "/admin_body", var);
	}

	public UITreeData getFuncMenuJson() throws CocException {
		String title = "访问后台功能菜单";
		LogUtil.debug("%s....", title);

		HttpContext context = Cocit.me().getHttpContext();
		SecurityService securityEngine = Cocit.me().getSecurityEngine();
		ILoginSession loginSession = context.getLoginSession();

		try {
			securityEngine.checkLoginUserType(Const.USER_SYSTEM);

			UITreeData treeData = new UITreeData();

			Tree funcMenu = loginSession.makeFuncMenu(CocUrl.ENTITY_GET_MAINS_UI);
			treeData.setData(funcMenu);

			LogUtil.debug("%s成功.", title);

			return treeData;

		} catch (CocConfigException e) {
			throw e;
		} catch (CocSecurityException e) {
			throw e;
		} catch (Throwable e) {
			String msg = String.format("%s出错： %s", title, ExceptionUtil.msg(e));
			LogUtil.error(msg, e);

			throw new CocException(msg);
		}
	}

	public UIModel bottom() throws CocException {
		HttpContext context = Cocit.me().getHttpContext();

		Map var = new HashMap();
		var.put("adminUrl", adminUrl);
		var.put("resourcePath", resourcePath);
		var.put("bottomHeight", bottomHeight);
		var.put("copyright2", Cocit.me().getConfig().get("copyright2"));

		return SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, resourcePath + "/admin_bottom", var);
	}

	public UIModel changePassword(boolean isAjax) throws SecurityException {
		HttpContext context = Cocit.me().getHttpContext();
		HttpServletRequest request = context.getRequest();
		ILoginSession loginSession = context.getLoginSession();
		SecurityService securityEngine = Cocit.me().getSecurityEngine();
		ITenantEntity tenant = context.getLoginTenant();

		if (loginSession == null) {
			throw new CocException("您尚未登录，请先登录!");
		}

		String username = loginSession.getName(); // request.getParameter(Const.REQUEST_KEY_USER);
		String oldPassword = request.getParameter("oldPassword");
		String rawPassword = request.getParameter("rawPassword");
		String rawPassword2 = request.getParameter("rawPassword2");

		/*
		 * 返回“修改密码”页面
		 */
		if (username == null //
		        || oldPassword == null//
		        || rawPassword == null//
		        || rawPassword2 == null//
		) {

			Map var = new HashMap();

			if (tenant != null) {
				var.put("title", tenant.getName() + "——修改密码");
			} else {
				var.put("title", "修改密码");
			}

			var.put("loginSession", loginSession);
			var.put("adminUrl", adminUrl);
			var.put("resourcePath", resourcePath);

			SmartyModel model = SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, resourcePath + "/admin_changePassword", var).setAjax(isAjax);

			return model;
		}
		/*
		 * 返回“修改密码”结果
		 */
		else {
			try {

				if (StringUtil.isBlank(rawPassword) || StringUtil.isBlank(rawPassword2)) {
					throw new CocException("请输入新密码!");
				}
				if (!rawPassword.equals(rawPassword2)) {
					throw new CocException("两次密码不一致!");
				}
				StringUtil.validatePassword(rawPassword);

				if (securityEngine.isRootUser(loginSession)) {
					RootUserFactory rootUserFactory = securityEngine.getRootUserFactory();
					ISystemUserEntity user = (ISystemUserEntity) rootUserFactory.getRootUser(username);

					String pwd = user.getPassword();
					String pwd1 = Cocit.me().getSecurityEngine().getPasswordEncoder().encodePassword(oldPassword, username);
					if (!pwd.equals(pwd1)) {
						throw new CocException("原始密码不对!");
					}

					user.setRawPassword(rawPassword);
					user.setRawPassword2(rawPassword2);
					rootUserFactory.saveRootUser(user);
				} else {
					IOrm orm = Cocit.me().orm();
					ISystemUserEntity user = (ISystemUserEntity) orm.get(EntityTypes.SystemUser, Expr.eq(Const.F_CODE, username));
					if (user == null) {
						throw new CocException("用户不存在!");
					}

					String pwd = user.getPassword();
					String pwd1 = Cocit.me().getSecurityEngine().getPasswordEncoder().encodePassword(oldPassword, username);
					if (!pwd.equals(pwd1)) {
						throw new CocException("原始密码不对!");
					}

					user.setRawPassword(rawPassword);
					user.setRawPassword2(rawPassword2);
					orm.save(user);
				}

				return AlertModel.makeSuccess("修改密码成功.");
			} catch (Throwable e) {
				String info = ExceptionUtil.msg(e);
				LogUtil.debug("修改密码失败! %s", info);

				return AlertModel.makeError("修改密码失败: " + info);
			}
		}
	}
}
