package com.jsoft.cocit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsoft.cocit.entity.security.IUser;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.entityengine.service.TenantService;
import com.jsoft.cocit.securityengine.LoginSession;

/**
 * Cocit HTTP环境：用来管理HTTP请求的一次生命周期。
 * 
 * @author jiongs753
 * @preserve all
 * 
 */
public interface HttpContext {

	HttpServletRequest getRequest();

	HttpServletResponse getResponse();

	LoginSession getLoginSession();

	IUser getLoginUser();

	TenantService getLoginTenant();

	SystemService getLoginSystem();

	String getLoginUsername();

	String getLoginTenantKey();

	String getLoginSystemKey();

	int getBrowserWidth();

	int getBrowserHeight();

	String getClientUIToken();

	/**
	 * 弹出表单操作完毕后将自动刷新哪些UI组件？
	 * 
	 * @return
	 */
	String getClientResultUI();

	int getClientUIHeight();

	int getClientUIWidth();

	String getParameterValue(String key);

	String[] getParameterValues(String key);

	<T> T getParameterValue(String key, T defaultReturn);

	<T> T getParameterValue(String parameterName, Class<T> classOfParameter, T defaultValue);

	void release();
}
