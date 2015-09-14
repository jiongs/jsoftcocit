package com.jsoft.cocimpl.dmengine.impl;

import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.dmengine.IDataEngine;
import com.jsoft.cocit.dmengine.IDataManager;
import com.jsoft.cocit.dmengine.IDataManagerFactory;
import com.jsoft.cocit.dmengine.info.IDataSourceInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.IOrm;

public class DataManagerFactoryImpl implements IDataManagerFactory {

	private Map<IOrm, IDataManager> dataManagers;

	public DataManagerFactoryImpl() {
		dataManagers = new HashMap();
	}

	public void release() {
		// for (DataManager obj : dataManagers.values()) {
		// obj.release();
		// }
		dataManagers.clear();
	}

	public IDataManager getManager(ISystemMenuInfo systemMenu) throws CocException {
		String dsCode = null;
		if (systemMenu == null) {
			dsCode = null;
		} else {
			if (systemMenu.getType() != Const.MENU_TYPE_ENTITY)
				throw new CocException("业务模块不存在! [moduleID=%s]", systemMenu);

			dsCode = systemMenu.getDataSourceCode();
		}

		IDataSourceInfo ds = Cocit.me().getEntityServiceFactory().getDataSource(dsCode);
		IOrm orm = null;
		if (ds != null) {
			orm = Cocit.me().getORM(ds);
		} else {
			orm = Cocit.me().orm();
		}

		IDataManager ret = this.dataManagers.get(orm);
		if (ret == null) {
			IDataEngine entityEngine = new DataEngineImpl(orm);
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
	// String dsCode = funcMenu.getDataSourceCode();
	// DataSourceService ds = Cocit.me().getEntityServiceFactory().getDataSource(dsCode);
	// if (ds != null) {
	// return new EntityManagerImpl(EntityEngineImpl.make(Cocit.me().getORM(ds)), funcMenu, moduleService);
	// } else {
	// return new EntityManagerImpl(EntityEngineImpl.make(Cocit.me().orm()), funcMenu, moduleService);
	// }
	// }
}
