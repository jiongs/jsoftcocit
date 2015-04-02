package com.jsoft.cocimpl.action;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.jsoft.cocimpl.config.impl.BaseConfig;
import com.jsoft.cocimpl.orm.DMLSession;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.ExtHttpContext;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.config.IConfig;
import com.jsoft.cocit.config.IDSConfig;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.entityengine.EntityEngine;
import com.jsoft.cocit.entityengine.EntityServiceFactory;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.exception.CocConfigException;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.mvc.CocEntityParam;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.orm.ExtOrm;
import com.jsoft.cocit.orm.NoTransConnCallback;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.securityengine.LoginSession;
import com.jsoft.cocit.securityengine.SecurityEngine;
import com.jsoft.cocit.ui.model.SmartyModel;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.ui.model.control.UITree;
import com.jsoft.cocit.ui.model.datamodel.AlertModel;
import com.jsoft.cocit.ui.model.datamodel.JSONModel;
import com.jsoft.cocit.util.DateUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class Admin0Action extends BaseAdminAction {

	private static final String ADMIN_URL = "/admin0";

	protected void init() {
		resourcePath = "/admin0";
		adminUrl = ADMIN_URL;
		topHeight = 80;
		bottomHeight = 0;
		frameGap = 5;
		leftWidth = 240;
		bodyHGap = 6;
		bodyWGap = 5;
		bodyTabsSpaceHeight = 0;
		bodyTabsSpaceWidth = 0;
	}

	@At(ADMIN_URL + "/login/*")
	public UIModel login(String funcExpr) {
		UIModel ret = super.login(funcExpr);

		if (ret instanceof SmartyModel) {
			SmartyModel sm = (SmartyModel) ret;

			Cocit coc = Cocit.me();
			IMessageConfig messages = coc.getMessages();

			List systems = Cocit.me().getEntityServiceFactory().getSystems();
			sm.set("systems", systems);
			String title = messages.getMsg("10001", coc.getConfig().getCocitSystemName());
			sm.set("title", title);
			sm.setTitle(title);
		}

		return ret;
	}

	@At(ADMIN_URL + "/logout/*")
	public JSONModel logout(String tenantKey) {
		return super.logout(tenantKey);
	}

	@At(ADMIN_URL)
	@Fail("redirect:" + ADMIN_URL + "/login")
	public UIModel admin() {
		return super.admin();
	}

	@At(ADMIN_URL + "/top")
	public UIModel top() throws CocException {
		return super.top();
	}

	@At(ADMIN_URL + "/left/*")
	@Fail("redirect:" + ADMIN_URL + "/login")
	public UIModel left(String parentMenuID) throws CocException {
		String title = "访问后台功能菜单";
		LogUtil.debug("%s....", title);

		SecurityEngine securityEngine = Cocit.me().getSecurityEngine();
		securityEngine.checkLoginUserType(Const.USER_SYSTEM);

		ExtHttpContext context = (ExtHttpContext) Cocit.me().getHttpContext();
		LoginSession loginSession = context.getLoginSession();

		try {
			securityEngine.checkLoginUserType(Const.USER_SYSTEM);

			UITree treeModel = new UITree();
			int h = (Integer) loginSession.get(Const.CONFIG_KEY_UI_BODYHEIGHT);
			treeModel.set("height", h);
			int w = (Integer) loginSession.get(Const.CONFIG_KEY_UI_LEFTWIDTH);
			treeModel.set("width", w);

			Tree funcMenu = loginSession.makeFuncMenu(UrlAPI.ENTITY_GET_MAIN_UI);

			treeModel.set("onSelect", "jCocit.entity.doSelectSystemMenuTree");
			treeModel.setAjax(false);
			treeModel.setData(funcMenu);

			LogUtil.debug("%s成功.", title);

			return treeModel;
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

	// @At(adminUrl + "/bottom/*")
	// public UIModel bottom() throws CocException {
	// return super.bottom();
	// }

	@At(ADMIN_URL + "/body/*")
	public UIModel body() throws CocException {
		return super.body();
	}

	// @At(adminUrl + "/getFuncMenuJson")
	// public UITreeData getFuncMenuJson() throws CocException {
	// return super.getFuncMenuJson();
	// }

	@At(ADMIN_URL + "/changePassword/*")
	@Fail("redirect:" + ADMIN_URL + "/login")
	public UIModel changePassword(boolean isAjax) throws SecurityException {
		return super.changePassword(isAjax);
	}

	// =================================================================================================================
	// Config API
	// =================================================================================================================

	@At(ADMIN_URL + "/config")
	@Fail("redirect:" + ADMIN_URL + "/login")
	public synchronized UIModel config() {
		LogUtil.debug("访问系统配置主界面...");

		SecurityEngine securityEngine = Cocit.me().getSecurityEngine();
		HttpContext context = Cocit.me().getHttpContext();
		LoginSession loginSession = context.getLoginSession();

		securityEngine.checkLoginUserType(Const.USER_ROOT);

		Map var = new HashMap();

		var.put("title", "平台配置");

		var.put("dataSource", Cocit.me().commonDataSource());
		var.put("config", Cocit.me().getConfig());
		var.put("uploadUrl", MVCUtil.makeUrl(Cocit.me().getConfig().getUploadPath(), ""));
		var.put("adminUrl", ADMIN_URL);
		var.put("bodyHeight", loginSession.get(Const.CONFIG_KEY_UI_BODYHEIGHT));

		LogUtil.debug("访问系统配置主界面结束.");

		return SmartyModel.make(context.getRequest(), context.getResponse(), Const.ST_DIR, ADMIN_URL + "/config", var);
	}

	@At(ADMIN_URL + "/saveConfig")
	public synchronized UIModel saveConfig(//
	        @Param("::db") CocEntityParam dbNode//
	        , @Param("::config") CocEntityParam configNode//
	) {

		try {
			SecurityEngine securityEngine = Cocit.me().getSecurityEngine();
			securityEngine.checkLoginUserType(Const.USER_ROOT);
		} catch (Throwable e) {
			return AlertModel.makeError("操作失败：" + ExceptionUtil.msg(e));
		}

		if (dbNode != null && dbNode.size() > 0) {
			LogUtil.debug("保存数据库配置...");
			try {

				IDSConfig dsConfig = Cocit.me().commonDataSource();

				IConfig tmp = ((IConfig) dsConfig).copy();
				dbNode.inject(Mirror.me(tmp), tmp, null);
				tmp.save();
				((BaseConfig) dsConfig).setProperties(tmp.getProperties());

				clearConfigCache();

				LogUtil.debug("保存数据库配置成功.");
				return AlertModel.makeSuccess("保存数据库配置成功.");

			} catch (Throwable e) {

				if (LogUtil.isDebugEnabled())
					LogUtil.debug("保存数据库配置失败. %s", ExceptionUtil.msg(e));

				return AlertModel.makeError("保存数据库配置失败：" + ExceptionUtil.msg(e));
			}
		}

		if (configNode != null && configNode.size() > 0) {
			LogUtil.debug("保存参数配置...");
			try {
				ICocConfig commonConfig = Cocit.me().getConfig();
				ICocConfig tmp = commonConfig.copy();
				configNode.inject(Mirror.me(tmp), tmp, null);
				tmp.save();
				((BaseConfig) commonConfig).setProperties(tmp.getProperties());

				clearConfigCache();

				LogUtil.debug("保存参数配置成功.");
				return AlertModel.makeSuccess("保存参数配置成功.");

			} catch (Throwable e) {

				if (LogUtil.isDebugEnabled())
					LogUtil.debug("保存参数配置出错. %s", ExceptionUtil.msg(e));

				return AlertModel.makeError("保存参数配置出错：" + ExceptionUtil.msg(e));
			}
		}

		return AlertModel.makeError("保存配置失败!");
	}

	@At(ADMIN_URL + "/testDBConfig")
	public synchronized UIModel testDBConfig(//
	        @Param("::db") CocEntityParam dbnode//
	) {
		try {
			SecurityEngine securityEngine = Cocit.me().getSecurityEngine();
			securityEngine.checkLoginUserType(Const.USER_ROOT);
		} catch (Throwable e) {
			return AlertModel.makeError("操作失败：" + ExceptionUtil.msg(e));
		}

		if (dbnode != null && dbnode.size() > 0) {
			LogUtil.debug("测试数据库连接...");
			try {
				IConfig dsConfig = (IConfig) Cocit.me().commonDataSource();
				IDSConfig tmp = (IDSConfig) dsConfig.copy();
				dbnode.inject(Mirror.me(tmp), tmp, null);
				Orm orm = Cocit.me().getORM(tmp);
				return (UIModel) orm.run(new NoTransConnCallback() {

					public Object invoke(Connection conn) throws Exception {
						return AlertModel.makeSuccess("数据库连接成功!");
					}
				});
			} catch (Throwable e) {

				if (LogUtil.isDebugEnabled())
					LogUtil.debug("测试数据库连接失败. %s", ExceptionUtil.msg(e));

				return AlertModel.makeError("数据库连接失败：" + ExceptionUtil.msg(e));
			}
		}

		return AlertModel.makeError("测试失败!");
	}

	@At(ADMIN_URL + "/clearConfigCache")
	public synchronized UIModel clearConfigCache() {
		LogUtil.debug("Admin0.clearConfigCache...");

		try {
			SecurityEngine securityEngine = Cocit.me().getSecurityEngine();
			securityEngine.checkLoginUserType(Const.USER_ROOT);
		} catch (Throwable e) {
			return AlertModel.makeError("操作失败：" + ExceptionUtil.msg(e));
		}

		try {
			Cocit cocit = Cocit.me();

			LogUtil.debug("清理前：%s", cocit.getStopWatch());

			cocit.getDataManagerFactory()//
			        .release();
			cocit.getEntityEngine()//
			        .release();
			cocit.getEntityServiceFactory()//
			        .release();
			cocit.getSecurityEngine()//
			        .release();
			((ExtOrm) cocit.orm())//
			        .clearMapping();

			System.gc();

			LogUtil.debug("清理后：%s", cocit.getStopWatch());

			return AlertModel.makeSuccess("清空缓存成功.");
		} catch (Throwable e) {

			if (LogUtil.isDebugEnabled())
				LogUtil.debug("Admin0.clearConfigCache：Error! %s", ExceptionUtil.msg(e));

			return AlertModel.makeError("清空缓存失败! " + ExceptionUtil.msg(e));
		}
	}

	@At(ADMIN_URL + "/setupCocitFromPackage")
	public synchronized UIModel setupCocitFromPackage() {
		LogUtil.debug("Admin0.setupCocitFromPackage: ...");

		try {
			SecurityEngine securityEngine = Cocit.me().getSecurityEngine();
			securityEngine.checkLoginUserType(Const.USER_ROOT);
		} catch (Throwable e) {
			return AlertModel.makeError("操作失败：" + ExceptionUtil.msg(e));
		}

		try {
			EntityEngine moduleEngine = Cocit.me().getEntityEngine();

			// if (Cocit.me().getConfig().isProductMode()) {
			moduleEngine.setupCocitFromPackage();
			clearConfigCache();

			// } else {
			// Cocit.me().getEntityModuleEngine().setupCocit();
			// }

			LogUtil.debug("Admin0.setupCocitFromPackage: SUCCESS!");

			return AlertModel.makeSuccess("安装平台功能模块成功.");
		} catch (Throwable e) {
			LogUtil.error("Admin0.setupCocitFromPackage: Error! %s", e);

			return AlertModel.makeError("安装平台功能模块出错：" + ExceptionUtil.msg(e));
		}
	}

	@At(ADMIN_URL + "/clearDBTables")
	public synchronized UIModel clearDBTables() {

		try {
			SecurityEngine securityEngine = Cocit.me().getSecurityEngine();
			securityEngine.checkLoginUserType(Const.USER_ROOT);

			ExtHttpContext context = (ExtHttpContext) Cocit.me().getHttpContext();
			if (!context.isDevHost()) {
				return AlertModel.makeError("非法操作！");
			}
		} catch (Throwable e) {
			return AlertModel.makeError("操作失败：" + ExceptionUtil.msg(e));
		}

		try {
			clearConfigCache();
			ExtOrm orm = (ExtOrm) Cocit.me().orm();
			DMLSession dml = orm.getDMLSession();
			dml.clear();
			clearConfigCache();

			return AlertModel.makeSuccess("清空数据库成功.");
		} catch (Throwable e) {
			LogUtil.error("清空数据库出错：%s", ExceptionUtil.msg(e), e);

			return AlertModel.makeError("清空数据库出错：" + ExceptionUtil.msg(e));
		}
	}

	@At(ADMIN_URL + "/setupCocitFromXls")
	public synchronized UIModel setupCocitFromXls() {
		LogUtil.info("从Excel导入实体模块...");

		try {
			SecurityEngine securityEngine = Cocit.me().getSecurityEngine();
			securityEngine.checkLoginUserType(Const.USER_ROOT);
		} catch (Throwable e) {
			return AlertModel.makeError("操作失败：" + ExceptionUtil.msg(e));
		}

		try {
			EntityEngine moduleEngine = Cocit.me().getEntityEngine();

			File contextDir = new File(Cocit.me().getContextDir());
			File docsDir = new File(contextDir.getParentFile().getAbsolutePath() + File.separator + "docs");
			if (!docsDir.exists()) {
				docsDir = new File(contextDir.getAbsolutePath() + File.separator + "docs");
			}

			LogUtil.info("解析Excel业务模型文档目录！docsDir = %s", docsDir);

			for (File file : docsDir.listFiles()) {
				LogUtil.info("解析Excel业务模型文件！file = %s", file.getAbsolutePath());

				if (!file.isDirectory() && file.getAbsolutePath().toLowerCase().endsWith(".xls")) {

					moduleEngine.setupCocitFromXls(file);

				}
			}
			clearConfigCache();

			LogUtil.debug("从Excel导入实体模块成功.");
			return AlertModel.makeSuccess("从Excel导入实体模块成功.");
		} catch (Throwable e) {
			LogUtil.error("从Excel导入实体模块模块出错：", e);

			return AlertModel.makeError("从Excel导入实体模块出错：" + ExceptionUtil.msg(e));
		}
	}

	@At(ADMIN_URL + "/exportToJson")
	public synchronized UIModel exportToJson(@Param("cocentity") String entityKeys) {
		Cocit coc = Cocit.me();
		LoginSession login = coc.getHttpContext().getLoginSession();
		EntityServiceFactory entityServiceFactory = coc.getEntityServiceFactory();

		try {
			SecurityEngine securityEngine = coc.getSecurityEngine();
			securityEngine.checkLoginUserType(Const.USER_ROOT);
		} catch (Throwable e) {
			return AlertModel.makeError("操作失败：" + ExceptionUtil.msg(e));
		}

		try {
			String tenantKey = login.getTenant().getKey();
			String folder = coc.getContextDir() + "/WEB-INF/tmp/" + DateUtil.getNowDate();
			EntityEngine entityEngine = coc.getEntityEngine();

			List<String> keyList = StringUtil.toList(entityKeys);
			for (String key : keyList) {
				CocEntityService cocEntity = entityServiceFactory.getEntity(key);
				entityEngine.exportToJson(tenantKey, folder, null, cocEntity);
			}

			LogUtil.debug("导出数据到JSON成功.");
			return AlertModel.makeSuccess("导出数据到JSON成功，数据所在目录\n" + folder);
		} catch (Throwable e) {
			LogUtil.error("导出数据到JSON出错：", e);

			return AlertModel.makeError("导出数据到JSON：" + ExceptionUtil.msg(e));
		}
	}
}
