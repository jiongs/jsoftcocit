package com.jsoft.cocimpl.entityengine.impl;

import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.entityengine.DataEngine;
import com.jsoft.cocit.entityengine.DataManager;
import com.jsoft.cocit.entityengine.DataManagerFactory;
import com.jsoft.cocit.entityengine.service.DataSourceService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.Orm;

public class DataManagerFactoryImpl implements DataManagerFactory {

	private Map<Orm, DataManager> dataManagers;

	public DataManagerFactoryImpl() {
		dataManagers = new HashMap();
	}

	public void release() {
		// for (DataManager obj : dataManagers.values()) {
		// obj.release();
		// }
		dataManagers.clear();
	}

	public DataManager getManager(SystemMenuService systemMenu) throws CocException {

		if (systemMenu == null) {
			return null;
		}
		if (systemMenu.getType() != Const.MENU_TYPE_ENTITY)
			throw new CocException("业务模块不存在! [moduleID=%s]", systemMenu);

		// LoginSession login = Cocit.me().getHttpContext().getLoginSession();

		// if (!Cocit.me().getSecurityEngine().allowVisitFuncMenu(login, systemMenu, false)) {
		// throw new CocException("无权执行该操作!");
		// }

		String dsKey = systemMenu.getDataSourceKey();
		DataSourceService ds = Cocit.me().getEntityServiceFactory().getDataSource(dsKey);
		Orm orm = null;
		if (ds != null) {
			orm = Cocit.me().getORM(ds);
		} else {
			orm = Cocit.me().orm();
		}

		DataManager ret = this.dataManagers.get(orm);
		if (ret == null) {
			DataEngine entityEngine = new DataEngineImpl(orm);
			ret = new DataManagerImpl(entityEngine);
			this.dataManagers.put(orm, ret);
		}

		return ret;
	}
	// public EntityManager getManager(SystemMenuService funcMenu, EntityModuleService moduleService) throws CocException {
	// if (moduleService == null) {
	// return null;
	// }
	//
	// if (funcMenu == null) {
	// return new EntityManagerImpl(EntityEngineImpl.make(Cocit.me().orm()), funcMenu, moduleService);
	// }
	//
	// LoginSession login = Cocit.me().getHttpContext().getLoginSession();
	//
	// if (!securityEngine.allowVisitFuncMenu(login, funcMenu, false)) {
	// throw new CocException("无权执行该操作!");
	// }
	//
	// String dsKey = funcMenu.getDataSourceKey();
	// DataSourceService ds = Cocit.me().getEntityServiceFactory().getDataSource(dsKey);
	// if (ds != null) {
	// return new EntityManagerImpl(EntityEngineImpl.make(Cocit.me().getORM(ds)), funcMenu, moduleService);
	// } else {
	// return new EntityManagerImpl(EntityEngineImpl.make(Cocit.me().orm()), funcMenu, moduleService);
	// }
	// }
}
