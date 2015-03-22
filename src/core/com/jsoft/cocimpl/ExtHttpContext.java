package com.jsoft.cocimpl;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.impl.ActionInvoker;

import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.config.IDSConfig;

public interface ExtHttpContext extends HttpContext {
	ActionContext actionContext();

	ActionInvoker actionInvoker();

	<T> T getConfigItem(String key, T defaultValue);

	<T> T getRequestAttribute(String key);

	<T> T setRequestAttribute(String key, T value);

	<T> T getSessionAttribute(String key);

	<T> T setSessionAttribute(String key, T value);

	boolean isDevHost();

	boolean isLAN();

	String getDomain();

	int getServerPort();

	String getLoginLogKey();

	IDSConfig getLoginTenantDataSource();

}
