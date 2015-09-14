package com.jsoft.cocit;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsoft.cocit.baseentity.security.IUserEntity;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.securityengine.SecurityVoter;
import com.jsoft.cocit.securityengine.ILoginSession;

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

	ILoginSession getLoginSession();

	IUserEntity getLoginUser();

	ITenantInfo getLoginTenant();

	ISystemInfo getLoginSystem();

	String getLoginUsername();

	String getLoginTenantCode();

	String getLoginSystemCode();

	int getBrowserWidth();

	int getBrowserHeight();

	String getClientUIToken();

	/**
	 * 弹出表单操作完毕后将自动刷新哪些UI组件？
	 * 
	 * @return
	 */
	String getClientResultUI();

	List<String> getClientResultUIList();

	List<String> getClientParamUIList();

	int getClientUIHeight();

	int getClientUIWidth();

	String getParameterValue(String key);

	String[] getParameterValues(String key);

	<T> T getParameterValue(String key, T defaultReturn);

	<T> T getParameterValue(String parameterName, Class<T> classOfParameter, T defaultValue);

	/**
	 * 为用户添加临时权限
	 */
	void addAccessVoter(SecurityVoter voter);

	List<SecurityVoter> getAccessVoters();

	void release();
}
