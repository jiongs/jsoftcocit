package com.jsoft.cocimpl.entityengine.impl.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.entity.coc.ICocEntity;
import com.jsoft.cocit.entity.config.IDataSource;
import com.jsoft.cocit.entity.config.IDic;
import com.jsoft.cocit.entity.security.IExtTenant;
import com.jsoft.cocit.entity.security.ISystem;
import com.jsoft.cocit.entity.security.ITenant;
import com.jsoft.cocit.entity.security.IUser;
import com.jsoft.cocit.entityengine.EntityServiceFactory;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.DataSourceService;
import com.jsoft.cocit.entityengine.service.DicService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.entityengine.service.TenantService;
import com.jsoft.cocit.entityengine.service.UserService;
import com.jsoft.cocit.exception.CocDBException;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @preserve
 */
public class EntityServiceFactoryImpl implements EntityServiceFactory {

	private EntityDAOHelper helper;
	private Map<Serializable, CocEntityService> cocEntityMap;
	private Map<Serializable, DataSourceService> dataSourceMap;
	private TenantService emptyTenant;
	private Map<Serializable, TenantService> tenantMap;
	private SystemService emptySystem;
	private Map<Serializable, SystemService> systemMap;
	private Map<Serializable, DicService> dicMap;

	public EntityServiceFactoryImpl() {
		helper = EntityDAOHelper.get();
		tenantMap = new HashMap();
		cocEntityMap = new HashMap();
		dataSourceMap = new HashMap();
		systemMap = new HashMap();
		dicMap = new HashMap();
	}

	public void release() {
		// for (TenantService o : tenantMap.values()) {
		// o.release();
		// }
		tenantMap.clear();

		// for (CocEntityService o : cocEntityMap.values()) {
		// o.release();
		// }
		cocEntityMap.clear();

		// for (DataSourceService o : dataSourceMap.values()) {
		// o.release();
		// }
		dataSourceMap.clear();

		// for (SystemService o : systemMap.values()) {
		// o.release();
		// }
		systemMap.clear();

		// for (DicService o : dicMap.values()) {
		// o.release();
		// }
		dicMap.clear();
		emptyTenant = null;
		emptySystem = null;
	}

	@Override
	public List<ISystem> getSystems() {
		try {
			return helper.getSystems();
		} catch (Throwable e) {
			return null;
		}
	}

	public SystemService getSystem(String systemKey) {
		if (emptySystem == null) {
			try {
				ISystem system = EntityTypes.System.newInstance();
				emptySystem = new EmptySystemService(system);
			} catch (Throwable e) {
				throw new CocException(e);
			}
		}

		if (StringUtil.isBlank(systemKey)) {
			ICocConfig config = Cocit.me().getConfig();
			systemKey = config.getDefaultSystemKey();
		}
		if (StringUtil.isBlank(systemKey)) {
			ICocConfig config = Cocit.me().getConfig();
			systemKey = config.getCocitSystemKey();
		}

		if (StringUtil.hasContent(systemKey)) {
			try {
				SystemService ret = systemMap.get(systemKey);

				if (ret == null) {
					ISystem obj = helper.getSystem(systemKey);
					if (obj != null) {
						ret = new SystemServiceImpl(obj);
						systemMap.put(obj.getKey(), ret);
					}
				}

				return ret;
			} catch (CocDBException e) {
				return emptySystem;
			}
		}

		return emptySystem;
	}

	public TenantService getTenant(String tenantKey) {
		if (emptyTenant == null) {
			try {
				ITenant tenant = EntityTypes.Tenant.newInstance();
				emptyTenant = new EmptyTenantService(tenant);
			} catch (Throwable e) {
				throw new CocException(e);
			}
		}

		if (StringUtil.hasContent(tenantKey)) {
			try {
				TenantService ret = tenantMap.get(tenantKey);

				if (ret == null) {
					IExtTenant obj = (IExtTenant) helper.getTenant(tenantKey);

					if (obj == null) {
						ICocConfig config = Cocit.me().getConfig();
						if (config.getCocitTenantKey().equals(tenantKey)) {
							try {
								obj = (IExtTenant) EntityTypes.Tenant.newInstance();
								obj.setName(config.getCocitTenantName());
								obj.setKey(config.getCocitTenantKey());
							} catch (Throwable e) {
								throw new CocException(e);
							}
						}
					}

					if (obj != null) {
						ret = new TenantServiceImpl(obj);
						tenantMap.put(obj.getKey(), ret);
					}
				}

				return ret;
			} catch (CocDBException e1) {
				return emptyTenant;
			}
		}

		return emptyTenant;
	}

	public CocEntityService getEntity(Serializable moduleID) {
		if (moduleID == null)
			return null;

		CocEntityService ret = cocEntityMap.get(moduleID);

		if (ret == null) {
			ICocEntity obj = helper.getEntity(moduleID);
			if (obj != null) {
				ret = new CocEntityServiceImpl(obj);
				cocEntityMap.put(obj.getId(), ret);
				cocEntityMap.put(obj.getKey(), ret);
			}
		}

		return ret;
	}

	public DataSourceService getDataSource(Serializable dataSourceID) {
		if (dataSourceID == null)
			return null;

		DataSourceService ret = dataSourceMap.get(dataSourceID);

		if (ret == null) {
			IDataSource obj = (IDataSource) helper.getDataSource(dataSourceID);
			if (obj != null) {
				ret = new DataSourceServiceImpl(obj);
				dataSourceMap.put(obj.getId(), ret);
				dataSourceMap.put(obj.getKey(), ret);
			}
		}

		return ret;
	}

	public UserService getUser(String tenantKey, String username) {
		IUser obj = helper.getSystemUser(tenantKey, username);
		if (obj == null)
			return null;

		return new UserServiceImpl(obj);
	}

	public DicService getDic(Serializable id) {

		if (id == null)
			return null;

		DicService ret = dicMap.get(id);

		if (ret == null) {
			IDic obj = (IDic) helper.get(EntityTypes.Dic, id);
			if (obj != null) {
				ret = new DicServiceImpl(obj);
				dicMap.put(obj.getId(), ret);
				dicMap.put(obj.getKey(), ret);
			}
		}

		return ret;
	}
}
