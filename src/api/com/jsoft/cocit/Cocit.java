package com.jsoft.cocit;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsoft.cocimpl.context.StopWatch;
import com.jsoft.cocimpl.entityengine.EntityServiceFactory;
import com.jsoft.cocimpl.entityengine.PatternAdapters;
import com.jsoft.cocimpl.mvc.nutz.CocActionHandler;
import com.jsoft.cocimpl.orm.generator.INamingStrategy;
import com.jsoft.cocimpl.ui.UIViews;
import com.jsoft.cocit.config.IBeansConfig;
import com.jsoft.cocit.config.ICommonConfig;
import com.jsoft.cocit.config.IDSConfig;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.entityengine.DataManagerFactory;
import com.jsoft.cocit.entityengine.EntityEngine;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.generator.EntityGenerators;
import com.jsoft.cocit.securityengine.SecurityEngine;
import com.jsoft.cocit.ui.UIModelFactory;

/**
 * 
 * @author Ji Yongshan
 * @preserve
 * 
 */
public abstract class Cocit {
	protected static Cocit me;

	public static Cocit me() {
		return me;
	}

	public abstract String getContextPath();

	public abstract String getContextDir();

	public abstract Map<String, String> getContextParameters();

	public abstract String getContextParameter(String name);

	public abstract <T> T getContextAttribute(String name);

	public abstract void setContextAttribute(String name, Object value);

	public abstract <T> T getBean(String name);

	public abstract <T> T getBean(Class<T> type);

	public abstract EntityServiceFactory getEntityServiceFactory();

	public abstract UIModelFactory getUiModelFactory();

	public abstract UIViews getViews();

	public abstract DataManagerFactory getDataManagerFactory();

	public abstract IDSConfig commonDataSource();

	public abstract StopWatch makeStopWatch();

	public abstract StopWatch getStopWatch();

	public abstract HttpContext makeHttpContext(HttpServletRequest req, HttpServletResponse res);

	public abstract HttpContext getHttpContext();

	public abstract void releaseHttpConext();

	public abstract Orm orm();

	public abstract Orm getProxiedORM();

	public abstract Orm getORM(IDSConfig dsConfig);

	public abstract void close();

	public abstract boolean isUpgradding();

	public abstract void setUpgradding(boolean v);

	public abstract ServletContext getServletContext();

	public abstract INamingStrategy getNamingStrategy();

	public abstract EntityEngine getEntityEngine();

	public abstract SecurityEngine getSecurityEngine();

	public abstract ICommonConfig getConfig();

	public abstract IBeansConfig getBeansConfig();

	public abstract CocActionHandler actionHandler();

	public abstract PatternAdapters getPatternAdapters();

	public abstract EntityGenerators getEntityGenerators();

	public abstract void destroy(ServletContext context);

	public abstract IMessageConfig getMessages();

}
