package com.jsoft.cocimpl.context;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.castor.Castors;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.impl.ActionInvoker;

import com.jsoft.cocimpl.mvc.nutz.CocUrlMappingImpl;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.ExtHttpContext;
import com.jsoft.cocit.config.IDSConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.entity.security.IUser;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.entityengine.service.TenantService;
import com.jsoft.cocit.exception.CocConfigException;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.securityengine.LoginSession;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

public class HttpContextImpl implements ExtHttpContext {

	static Pattern configPath = Pattern.compile("^/(config|login|logout)/*", Pattern.CASE_INSENSITIVE);

	private HttpServletRequest request;

	private HttpServletResponse response;

	private ActionContext actionContext;

	private ActionInvoker actionInvoker;

	private LoginSession loginSession;

	private TenantService tenant;

	private SystemService system;

	private String url;

	private Boolean isDevHost;
	private Boolean isLAN;

	public HttpContextImpl(HttpServletRequest req, HttpServletResponse res) {
		if (Cocit.me().isUpgradding()) {
			throw new CocException("正在升级系统......请稍候再试!");
		}
		this.request = req;
		this.response = res;

		loginSession = Cocit.me().getSecurityEngine().getLoginSession(req);

		url = Mvcs.getRequestPath(req);

		initInvoker();

		// String domain = request.getServerName();
		// EntityProxyFactory entityProxyFactory = Cocit.me().getEntityProxyFactory();
		//
		// tenantProxy = entityProxyFactory.getTenant("");
		// if (tenantProxy == null) {
		// tenantProxy = entityProxyFactory.getTenant("");
		// }

	}

	public void release() {
		request = null;
		response = null;
		this.actionContext = null;
		this.actionInvoker = null;
		this.loginSession = null;
		this.tenant = null;
		this.system = null;
		this.url = null;
		this.isDevHost = null;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public TenantService getLoginTenant() {
		if (tenant == null) {
			if (loginSession != null)
				tenant = loginSession.getTenant();
		}
		if (tenant == null) {
			tenant = Cocit.me().getEntityServiceFactory().getTenant("");
		}

		return tenant;
	}

	//
	// public <T> T getConfigItem(String configKey, T defaultReturn) {
	// return (T) tenant.getConfigItem(configKey, defaultReturn);
	// }

	public String[] getParameterValues(String key) {
		return request.getParameterValues(key);
	}

	private void initInvoker() throws SecurityException {
		actionContext = new ActionContext().setRequest(request).setResponse(response).setServletContext(Cocit.me().getServletContext());
		CocUrlMappingImpl urlMapping = (CocUrlMappingImpl) Cocit.me().actionHandler().getMapping();

		// String domain = request.getServerName();
		// if (StringUtil.isIP(domain)) {
		// String domain = "";
		// }

		/*
		 * 试图将URI的第一个节点作为租户编号
		 */
		String tenantKey = "";
		if (!StringUtil.isBlank(url)) {
			actionInvoker = urlMapping.get(actionContext, url);
			if (actionInvoker == null) {
				String url1 = url;
				int appIdxFrom = 0;
				if (url.startsWith("/") || url.startsWith("\\")) {
					url1 = url.substring(1);
					appIdxFrom = 1;
				}
				int idx = url1.indexOf("/");
				if (idx > -1) {
					url1 = url1.substring(idx);
					actionInvoker = urlMapping.get(actionContext, url1);
					if (actionInvoker != null) {// 路径的第一组斜杠(/.../)之间的部分为租户编号
						tenantKey = url.substring(appIdxFrom, idx + appIdxFrom);
					}
				}
			}
		}

		/*
		 * 路径的第一个节点不是应用编号
		 */
		try {
			tenant = Cocit.me().getEntityServiceFactory().getTenant(tenantKey);

			// if (tenant == null) {
			// throw new CocConfigException("未知应用系统!");
			// }

			// if (!StringUtil.isEmpty(domain) && !StringUtil.isEmpty(tenant.getDomain()) && !domain.equals(tenant.getDomain())) {
			// throw new CocSecurityException(404);
			// }

			// if (!StringUtil.isEmpty(appcode) && !StringUtil.isEmpty(tenant.getKey()) && !appcode.equals(tenant.getKey())) {
			// throw new CocSecurityException(404);
			// }
		} catch (CocConfigException e) {
			if (!configPath.matcher(url).find())
				throw e;
		} catch (Throwable e) {
			LogUtil.error("", e);
		}
	}

	public String getParameterValue(String key) {
		return request.getParameter(key);
	}

	public <T> T getParameterValue(String key, T defaultReturn) {
		String value = request.getParameter(key);

		if (value == null)
			return defaultReturn;
		if (defaultReturn == null)
			return (T) value;

		Class valueType = defaultReturn.getClass();

		try {
			return (T) StringUtil.castTo(value, valueType);
		} catch (Throwable e) {
			LogUtil.error("StringUtil.getParameterValue: 出错！ {key:%s, defaultReturn:%s, valueType:%s}", key, defaultReturn, valueType.getName(), e);
		}

		return defaultReturn;
	}

	public <T> T getRequestAttribute(String key) {
		return (T) request.getAttribute(key);
	}

	public <T> T setRequestAttribute(String key, T value) {
		request.setAttribute(key, value);

		return value;
	}

	public <T> T getSessionAttribute(String key) {
		return (T) request.getSession().getAttribute(key);
	}

	public <T> T setSessionAttribute(String key, T value) {
		request.getSession().setAttribute(key, value);

		return value;
	}

	public int getBrowserWidth() {
		if (getLoginSession() != null) {
			Double d = getLoginSession().getBrowserWidth();
			return d.intValue();
		}
		return 1280;
	}

	public int getBrowserHeight() {
		if (getLoginSession() != null) {
			Double d = getLoginSession().getBrowserHeight();
			return d.intValue();
		}
		return 768;
	}

	//
	// public int getAdminTopHeight() {
	// int ret = 0;
	// int browserWidth = getBrowserHeight();
	// TenantService tp = loginSession.getTenant();
	// String w = tp == null ? "" : tp.getConfigItem("admin.ui.topHeight", "");
	//
	// try {
	// if (!StringUtil.isBlank(w))
	// if (w.endsWith("%"))
	// ret = browserWidth * Integer.parseInt(w.substring(0, w.length() - 1)) / 100;
	// else
	// ret = Integer.parseInt(w);
	// } catch (Throwable e) {
	// LogUtil.warn("", e);
	// ret = 95;
	// }
	//
	// return ret;
	// // return 30;
	// }
	//
	// public int getAdminLeftWidth() {
	// int ret = 0;
	// int browserWidth = getBrowserWidth();
	// TenantService tp = loginSession.getTenant();
	// String w = tp == null ? "" : tp.getConfigItem("admin.ui.leftWidth", "");
	//
	// try {
	// if (!StringUtil.isBlank(w)) {
	// if (w.endsWith("%"))
	// ret = browserWidth * Integer.parseInt(w.substring(0, w.length() - 1)) / 100;
	// else
	// ret = Integer.parseInt(w);
	// } else {
	// ret = new Double(browserWidth * 0.2).intValue();
	// }
	// } catch (Throwable e) {
	// LogUtil.warn("", e);
	// ret = new Double(browserWidth * 0.2).intValue();
	// }
	//
	// return ret;
	// }

	@Override
	public String getClientUIToken() {
		String token = this.getRequestAttribute("uiToken");
		if (StringUtil.isBlank(token)) {
			token = this.getParameterValue("_uiToken");
			if (StringUtil.isBlank(token)) {
				token = Long.toHexString(System.currentTimeMillis());
			}
			this.setRequestAttribute("uiToken", token);
		}
		return token;
	}

	@Override
	public String getClientResultUI() {
		String resultUI = this.getRequestAttribute("resultUI");
		if (StringUtil.isBlank(resultUI)) {
			resultUI = this.getParameterValue("_resultUI");
			resultUI = StringUtil.toJSArray(StringUtil.toList(resultUI));
			this.setRequestAttribute("resultUI", resultUI);
		}
		return resultUI;
	}

	public int getClientUIHeight() {
		String str = this.getParameterValue("_uiHeight");
		try {
			if (StringUtil.hasContent(str)) {
				return new Double(str).intValue();
			}
		} catch (Throwable e) {

		}

		try {
			Integer ret = this.loginSession.get(Const.CONFIG_KEY_UI_CONTENTHEIGHT);
			if (ret != null) {
				return ret;
			}
		} catch (Throwable e) {

		}

		return this.getBrowserHeight();
	}

	public int getClientUIWidth() {
		String str = this.getParameterValue("_uiWidth");
		try {
			if (StringUtil.hasContent(str)) {
				return new Double(str).intValue();
			}
		} catch (Throwable e) {

		}

		try {
			Integer ret = this.loginSession.get(Const.CONFIG_KEY_UI_CONTENTWIDTH);
			if (ret != null) {
				return ret;
			}
		} catch (Throwable e) {

		}

		return this.getBrowserWidth();
	}

	public String getLoginTenantKey() {
		TenantService tenant = getLoginTenant();
		if (tenant != null && tenant.getKey() != null)
			return tenant.getKey();

		return "";
	}

	public String getLoginSystemKey() {
		SystemService system = getLoginSystem();
		if (system != null && system.getKey() != null)
			return system.getKey();

		return "";
	}

	public <T> T getParameterValue(String parameterName, Class<T> classOfParameter, T defaultValue) {
		if (classOfParameter.isAssignableFrom(String[].class)) {
			return (T) request.getParameterValues(parameterName);
		}
		String v = request.getParameter(parameterName);
		if (StringUtil.isBlank(v)) {
			return defaultValue;
		}
		return (T) Castors.me().castTo(v, classOfParameter);
	}

	public LoginSession getLoginSession() {
		return Cocit.me().getSecurityEngine().getLoginSession(request);
	}

	public IUser getLoginUser() throws CocSecurityException {
		LoginSession login = this.getLoginSession();
		if (login != null)
			return login.getUser();

		return null;
	}

	public ActionContext actionContext() {
		return actionContext;
	}

	public ActionInvoker actionInvoker() {
		return actionInvoker;
	}

	public String getDomain() {
		return request.getServerName();
	}

	public int getServerPort() {
		return request.getServerPort();
	}

	public boolean isDevHost() {
		if (isDevHost != null) {
			return isDevHost;
		}

		isDevHost = ("127.0.0.1".equals(this.getDomain()) || "localhost".equals(this.getDomain())) && 9999 == this.getServerPort();

		return isDevHost;
	}

	public boolean isLAN() {
		if (isLAN != null) {
			return isLAN;
		}

		String domain = this.getDomain();
		isLAN = domain.startsWith("192.") || domain.equals("localhost") || domain.startsWith("127.") || domain.startsWith("10.");

		return isLAN;
	}

	public IDSConfig getLoginTenantDataSource() {
		TenantService tenant = this.getLoginTenant();
		if (tenant != null) {
			return tenant.getDataSource();
		}

		return null;
	}

	public String getLoginUsername() {
		if (getLoginUser() != null)
			return getLoginUser().getUsername();

		return "";
	}

	public SystemService getLoginSystem() {
		if (system == null) {
			if (loginSession != null)
				system = loginSession.getSystem();
		}
		if (system == null) {
			system = getLoginTenant().getSystem();
		}

		return system;
	}

	public <T> T getConfigItem(String key, T defaultValue) {
		T ret = null;

		TenantService tenant = this.getLoginTenant();
		if (tenant != null) {
			ret = tenant.getConfigItem(key, defaultValue);
		}
		if (defaultValue.equals(ret)) {
			SystemService system = this.getLoginSystem();
			if (system != null) {
				ret = system.getConfigItem(key, defaultValue);
			}
		}

		return ret == null ? defaultValue : ret;
	}

	public String getLoginLogKey() {
		LoginSession login = this.getLoginSession();
		if (login != null)
			return login.getLoginLogKey();

		return null;
	}
}
