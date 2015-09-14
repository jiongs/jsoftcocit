package com.jsoft.cocit;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.jsoft.cocimpl.mvc.nutz.CocActionHandler;
import com.jsoft.cocimpl.orm.generator.INamingStrategy;
import com.jsoft.cocit.config.IBeansConfig;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.config.IDSConfig;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.context.StopWatch;
import com.jsoft.cocit.dmengine.IDataManagerFactory;
import com.jsoft.cocit.dmengine.IDataModelEngine;
import com.jsoft.cocit.dmengine.IEntityInfoFactory;
import com.jsoft.cocit.dmengine.IPatternAdapters;
import com.jsoft.cocit.dmengine.command.ICommandInterceptors;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.generator.EntityGenerators;
import com.jsoft.cocit.securityengine.SecurityService;
import com.jsoft.cocit.service.LogService;
import com.jsoft.cocit.ui.UIModelFactory;
import com.jsoft.cocit.ui.UIViews;

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

	public abstract IEntityInfoFactory getEntityServiceFactory();

	public abstract UIModelFactory getUiModelFactory();

	public abstract UIViews getViews();

	public abstract IDataManagerFactory getDataManagerFactory();

	public abstract IDSConfig commonDataSource();

	public abstract StopWatch makeStopWatch();

	public abstract StopWatch getStopWatch();

	public abstract HttpContext makeHttpContext(HttpServletRequest req, HttpServletResponse res);

	public abstract HttpContext getHttpContext();

	public abstract void releaseHttpConext();

	public abstract IOrm orm();

	// public abstract Orm getProxiedORM();

	public abstract IOrm getORM(IDSConfig dsConfig);

	public abstract void close();

	public abstract boolean isUpgradding();

	public abstract void setUpgradding(boolean v);

	public abstract ServletContext getServletContext();

	public abstract INamingStrategy getNamingStrategy();

	public abstract IDataModelEngine getEntityEngine();

	public abstract SecurityService getSecurityEngine();

	public abstract ICocConfig getConfig();

	public abstract IBeansConfig getBeansConfig();

	public abstract CocActionHandler actionHandler();

	public abstract ICommandInterceptors getCommandInterceptors();

	public abstract IPatternAdapters getPatternAdapters();

	public abstract EntityGenerators getEntityGenerators();

	public abstract void destroy(ServletContext context);

	public abstract IMessageConfig getMessages();

	public abstract LogService getLogService();
	
	public abstract ApplicationContext getApplicationContext();

}
