package com.jsoft.cocimpl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.trans.Trans;

import com.jsoft.cocimpl.config.impl.CocConfig;
import com.jsoft.cocimpl.config.impl.DSConfig;
import com.jsoft.cocimpl.config.impl.LogConfig;
import com.jsoft.cocimpl.config.impl.MessageConfig;
import com.jsoft.cocimpl.context.HttpContextImpl;
import com.jsoft.cocimpl.context.StopWatch;
import com.jsoft.cocimpl.entityengine.impl.DataManagerFactoryImpl;
import com.jsoft.cocimpl.entityengine.impl.EntityEngineImpl;
import com.jsoft.cocimpl.entityengine.impl.service.EntityServiceFactoryImpl;
import com.jsoft.cocimpl.mvc.nutz.CocActionHandler;
import com.jsoft.cocimpl.mvc.nutz.CocNutConfig;
import com.jsoft.cocimpl.orm.generator.INamingStrategy;
import com.jsoft.cocimpl.orm.generator.impl.EncodeNamingStrategy;
import com.jsoft.cocimpl.orm.generator.impl.SimpleNamingStrategy;
import com.jsoft.cocimpl.orm.listener.EntityListeners;
import com.jsoft.cocimpl.orm.listener.impl.CocEntityListener;
import com.jsoft.cocimpl.orm.nutz.EnMappingHolder;
import com.jsoft.cocimpl.orm.nutz.EnMappingMaker;
import com.jsoft.cocimpl.orm.nutz.impl.CocTransaction;
import com.jsoft.cocimpl.orm.nutz.impl.OrmImpl;
import com.jsoft.cocimpl.orm.nutz.impl.ProxyOrm;
import com.jsoft.cocimpl.securityengine.impl.SecurityEngineImpl;
import com.jsoft.cocimpl.ui.UIViews;
import com.jsoft.cocimpl.ui.impl.UIModelFactoryImpl;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.config.IBeansConfig;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.config.IDSConfig;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.entityengine.DataManagerFactory;
import com.jsoft.cocit.entityengine.EntityEngine;
import com.jsoft.cocit.entityengine.EntityServiceFactory;
import com.jsoft.cocit.entityengine.PatternAdapters;
import com.jsoft.cocit.log.Log;
import com.jsoft.cocit.log.Logs;
import com.jsoft.cocit.orm.ExtOrm;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.generator.EntityGenerators;
import com.jsoft.cocit.orm.listener.EntityListener;
import com.jsoft.cocit.securityengine.LoginSession;
import com.jsoft.cocit.securityengine.SecurityEngine;
import com.jsoft.cocit.ui.UIModelFactory;
import com.jsoft.cocit.util.StringUtil;

public class CocitImpl extends Cocit {
	private static Log log = Logs.getLog(CocitImpl.class);

	private static final String WEB_APP_ROOT_KEY_PARAM = "webAppRootKey";
	private static final String DEFAULT_WEB_APP_ROOT_KEY = "webapp.root";

	private static ThreadLocal<HttpContext> httpContextHolder = new ThreadLocal<HttpContext>();

	private boolean upgradding = false;
	private boolean initialized = false;

	private ServletContext servletContext;
	private String contextPath = "";
	private String contextDir = "";
	private Map<String, String> contextParameters = new HashMap();
	private ThreadLocal<StopWatch> stopWatchHolder = new ThreadLocal();
	private Map<IDSConfig, ExtOrm> ormMap = new HashMap();
	private ICocConfig cocconfig = null;
	private IMessageConfig i18nconfig = null;
	private INamingStrategy namingStrategy;
	private INamingStrategy namingStrategyForEncoding;
	private CocActionHandler actionHandler;
	// private IPSeeker ipSeeker;
	private EnMappingHolder entityHolder;
	private EnMappingMaker entityMaker;
	private BeanFactory beanFactory;
	private EntityEngine entityEngine;
	// private SystemEngine systemEngine;
	private SecurityEngine securityEngine;
	private EntityServiceFactory entityServiceFactory;
	private UIModelFactory uiModelFactory;
	private DataManagerFactory dataManagerFactory;
	private IDSConfig commonDataSourceConfig;
	private EntityListeners listeners;
	private IBeansConfig beansConfig;

	public static void init(ServletContext context) {
		CocitImpl obj = new CocitImpl(context);

		Cocit.me = obj;

		obj.init();
	}

	public HttpContext makeHttpContext(HttpServletRequest req, HttpServletResponse res) {
		HttpContext ret = new HttpContextImpl(req, res);

		httpContextHolder.set(ret);

		log.debugf("Cocit.makeHttpContext: result = %s", ret);

		return ret;
	}

	/**
	 * 
	 * @preserve
	 * @return
	 */
	public HttpContext getHttpContext() {
		return httpContextHolder.get();
	}

	public void releaseHttpConext() {
		httpContextHolder.remove();
	}

	private void init() {

		/*
		 * 加载国际化配置文件
		 */
		i18nconfig = new MessageConfig();

		/*
		 * 计算日志存放路径并缓存在系统属性表中
		 */
		String logsDir = this.contextParameters.get(ICocConfig.PATH_LOGS);
		if (StringUtil.isBlank(logsDir)) {
			logsDir = this.cocconfig.getLogsDir();
		}
		System.setProperty(ICocConfig.PATH_LOGS, logsDir);

		/*
		 * 计算临时目录
		 */
		String tempDir = this.contextParameters.get(ICocConfig.PATH_TEMP);
		if (StringUtil.isBlank(tempDir)) {
			tempDir = this.cocconfig.getTempDir();
		}

		/*
		 * 安装事务
		 */
		Trans.setup(CocTransaction.class);

		/*
		 * 加载日志配置
		 */
		new LogConfig();

		this.beanFactory = BeanFactory.make(this.servletContext);
		this.beansConfig = beanFactory.getBean("beansConfig");

		/*
		 * 创建服务对象
		 */
		this.namingStrategy = SimpleNamingStrategy.get();
		this.namingStrategyForEncoding = EncodeNamingStrategy.get();
		this.entityHolder = EnMappingHolder.make();
		this.entityMaker = EnMappingMaker.make();
		this.entityMaker.setHolder(this.entityHolder);
		this.entityMaker.setNamingStrategy(this.namingStrategy);
		this.entityMaker.setNamingStrategyForEncoding(this.namingStrategyForEncoding);
		this.actionHandler = new CocActionHandler(new CocNutConfig());

		this.entityEngine = new EntityEngineImpl();
		this.securityEngine = new SecurityEngineImpl();
		// systemEngine = bean("systemEngine");
		this.entityServiceFactory = new EntityServiceFactoryImpl();
		this.uiModelFactory = new UIModelFactoryImpl();
		this.dataManagerFactory = new DataManagerFactoryImpl();
		this.commonDataSourceConfig = new DSConfig();

		List<EntityListener> listeners = new ArrayList();
		listeners.add(new CocEntityListener());
		this.listeners = new EntityListeners(listeners);

		// try {
		// ipSeeker = new IPSeeker("QQWry.Dat", contextDir + File.separator + "WEB-INF" + File.separator + "lib");
		// } catch (Throwable e) {
		// log.error("加载IP地址转换包失败！");
		// }

		// /*
		// * 验证
		// */
		// Assert.isTrue(entityModuleEngine != null, "entityModuleEngine cannot be null!");
		// Assert.isTrue(securityEngine != null, "securityEngine cannot be null!");
		// Assert.isTrue(entityProxyEngine != null, "entityProxyEngine cannot be null!");
		// Assert.isTrue(widgetModelFactory != null, "widgetModelFactory cannot be null!");
		// Assert.isTrue(widgetRenderFactory != null, "widgetRenderFactory cannot be null!");
		// Assert.isTrue(entityManagerFactory != null, "entityManagerFactory cannot be null!");
		// Assert.isTrue(beansConfig != null, "beansConfig cannot be null!");

		/*
		 * ENGINE初始化
		 */
		this.entityEngine.initEntityTypes();

		this.initialized = true;
		/*
		 * 日志信息
		 */
		Iterator<String> keys = this.contextParameters.keySet().iterator();
		StringBuffer initParamLog = new StringBuffer();
		initParamLog.append("---------------------------------");
		while (keys.hasNext()) {
			String key = keys.next();
			Object val = this.contextParameters.get(key);
			initParamLog.append(key).append("=").append(val).append("\n");
		}
		initParamLog.append("---------------------------------");
		initParamLog.append("\npath.context=").append(this.cocconfig.getContextPath());
		initParamLog.append("\ndir.context=").append(this.cocconfig.getContextDir());
		initParamLog.append("\ndir.config=").append(this.cocconfig.getConfigDir());
		initParamLog.append("\ndir.logs=").append(this.cocconfig.getLogsDir());
		initParamLog.append("\ndir.temp=").append(this.cocconfig.getTempDir());
		initParamLog.append("\ndir.WEB-INF=").append(this.cocconfig.getWebInfoDir());
		initParamLog.append("\ndir.classes=").append(this.cocconfig.getClassDir());

		log.infof("初始化Cocit平台: 配置信息:\n%s%s", this.cocconfig, initParamLog);

		log.info("初始化Cocit数据......");

		try {
			if (this.getConfig().isAutoUpgrade()) {
				this.entityEngine.setupCocitFromPackage();
			}
		} catch (Throwable e) {
			log.error("初始化Cocit数据出错！", e);
		}
		System.out.println("初始化Cocit平台: 结束.");
		log.info("初始化Cocit平台: 结束.");
	}

	private CocitImpl(ServletContext context) {
		synchronized (CocitImpl.class) {
			if (initialized) {
				log.warn("警告：不能重复初始化Cocit平台!");
				return;
			}

			log.info("初始化Cocit平台...");

			servletContext = context;

			/*
			 * Initialize ServletContext Parameters
			 */
			Enumeration names = servletContext.getInitParameterNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				contextParameters.put(name, servletContext.getInitParameter(name));
			}

			/*
			 * Initialize webapp key
			 */
			String webappRootKey = contextParameters.get(WEB_APP_ROOT_KEY_PARAM);
			if (StringUtil.isBlank(webappRootKey)) {
				webappRootKey = DEFAULT_WEB_APP_ROOT_KEY;
			}

			/*
			 * Initialize contextPath
			 */
			contextPath = servletContext.getContextPath().trim();
			if (contextPath.endsWith("/")) {
				contextPath = contextPath.substring(0, contextPath.length() - 1);
			}
			if (contextPath.length() > 0 && contextPath.charAt(0) != '/') {
				contextPath = "/" + contextPath;
			}

			/*
			 * Initialize contextDir
			 */
			contextDir = servletContext.getRealPath("/").replace("\\", "/");
			if (contextDir.endsWith("/"))
				contextDir = contextDir.substring(0, contextDir.length() - 1);
			else if (contextDir.endsWith("/."))
				contextDir = contextDir.substring(0, contextDir.length() - 2);

			/*
			 * 设置系统属性：
			 */
			System.setProperty(webappRootKey, contextDir);

			/*
			 * 加载Cocit平台配置文件
			 */
			cocconfig = new CocConfig(contextPath, contextDir, contextParameters.get(ICocConfig.PATH_CONFIG));
		}

	}

	public String getContextPath() {
		return contextPath;
	}

	public String getContextDir() {
		return contextDir;
	}

	public Map<String, String> getContextParameters() {
		return contextParameters;
	}

	public String getContextParameter(String name) {
		return (String) contextParameters.get(name);
	}

	public <T> T getContextAttribute(String name) {
		return (T) servletContext.getAttribute(name);
	}

	public void setContextAttribute(String name, Object value) {
		servletContext.setAttribute(name, value);
	}

	public <T> T getBean(String name) {
		return (T) beanFactory.getBean(name);
	}

	public <T> T getBean(Class<T> type) {
		return beanFactory.getBean(type);
	}

	public EntityServiceFactory getEntityServiceFactory() {
		return entityServiceFactory;
	}

	public UIModelFactory getUiModelFactory() {
		return uiModelFactory;
	}

	public UIViews getViews() {
		return beanFactory.getViews();
	}

	public DataManagerFactory getDataManagerFactory() {
		return dataManagerFactory;
	}

	public IDSConfig commonDataSource() {
		return commonDataSourceConfig;
	}

	public void destroy(ServletContext context) {
		beanFactory.clear();

		contextPath = null;
		beanFactory = null;
	}

	public StopWatch makeStopWatch() {
		synchronized (stopWatchHolder) {
			StopWatch obj = StopWatch.begin();
			stopWatchHolder.set(obj);

			return obj;
		}
	}

	public StopWatch getStopWatch() {
		return stopWatchHolder.get();
	}

	public Orm getProxiedORM() {
		Orm orm = orm();

		if (orm instanceof ProxyOrm)
			return ((ProxyOrm) orm).getProxiedOrm();

		return orm;
	}

	public Orm orm() {
		ExtHttpContext ctx = (ExtHttpContext) getHttpContext();
		IDSConfig db = null;

		if (ctx != null) {
			db = ctx.getLoginTenantDataSource();
		}

		if (db == null)
			db = commonDataSource();

		return getORM(db);
	}

	/**
	 * 获取访问特定数据库的ORM
	 * 
	 * @param dsConfig
	 *            数据库配置
	 * @return
	 */
	public Orm getORM(IDSConfig dsConfig) {
		synchronized (ormMap) {
			ExtOrm orm = ormMap.get(dsConfig);

			if (orm == null) {
				EntityListeners ls = listeners;
				orm = new ProxyOrm(new OrmImpl(dsConfig, entityHolder, entityMaker, ls));
				ormMap.put(dsConfig, orm);
			}

			ExtHttpContext ctx = (ExtHttpContext) getHttpContext();
			if (ctx != null) {
				LoginSession login = ctx.getLoginSession();
				if (login != null) {
					if (this.cocconfig.getCocitSystemKey().equals(login.getSystem().getKey())) {
						ProxyOrm proxyOrm = (ProxyOrm) orm;
						return proxyOrm.getProxiedOrm();
					}
				}
			}

			return orm;
		}
	}

	// /**
	// * 获取用于操作特定数据库的ORM
	// *
	// * @param url
	// * 连接数据库的URL
	// * @param driver
	// * 数据库JDBC驱动
	// * @param user
	// * 数据库登录帐号
	// * @param pwd
	// * 数据库登录密码
	// * @return
	// */
	// public ExtOrm orm(String url, String driver, String database, String user, String pwd) {
	// return orm(new CommonDataSourceConfig(url, driver, user, pwd));
	// }

	// /**
	// * 获取用于操作特定数据库的ORM
	// *
	// * @param dbCfgName
	// * 数据库配置：IOC配置名称
	// * @return
	// */
	// public ExtOrm orm(String dbCfgName) {
	// return orm((IDataSourceConfig) bean(dbCfgName));
	// }

	public void close() {
		if (actionHandler != null) {
			actionHandler.depose();
		}
	}

	public boolean isUpgradding() {
		return upgradding;
	}

	public void setUpgradding(boolean v) {
		upgradding = v;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public INamingStrategy getNamingStrategy() {
		return namingStrategy;
	}

	// public IPSeeker getIpSeeker() {
	// return ipSeeker;
	// }

	/**
	 * 
	 * @preserve
	 * @return
	 */
	public EntityEngine getEntityEngine() {
		return entityEngine;
	}

	// public SystemEngine getSystemEngine() {
	// return systemEngine;
	// }

	/**
	 * 
	 * @preserve
	 * @return
	 */
	public SecurityEngine getSecurityEngine() {
		return securityEngine;
	}

	public ICocConfig getConfig() {
		return cocconfig;
	}

	public CocActionHandler actionHandler() {
		return actionHandler;
	}

	public IBeansConfig getBeansConfig() {
		return beansConfig;
	}

	@Override
	public PatternAdapters getPatternAdapters() {
		return beanFactory.getPatternAdapters();
	}

	@Override
	public IMessageConfig getMessages() {
		return i18nconfig;
	}

	@Override
	public EntityGenerators getEntityGenerators() {
		return beanFactory.getEntityGenerators();
	}

}
