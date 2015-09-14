package com.jsoft.cocimpl.dmengine.impl.info;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.baseentity.coc.ICocCatalogEntity;
import com.jsoft.cocit.baseentity.coc.ICocEntity;
import com.jsoft.cocit.baseentity.config.IDataSourceEntity;
import com.jsoft.cocit.baseentity.config.IDicEntity;
import com.jsoft.cocit.baseentity.security.ISystemEntity;
import com.jsoft.cocit.baseentity.security.ITenantEntity;
import com.jsoft.cocit.baseentity.security.ITenantEntityExt;
import com.jsoft.cocit.baseentity.security.IUserEntity;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.dmengine.IEntityInfoFactory;
import com.jsoft.cocit.dmengine.info.ICocCatalogInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.IDataSourceInfo;
import com.jsoft.cocit.dmengine.info.IDicInfo;
import com.jsoft.cocit.dmengine.info.IGroupInfo;
import com.jsoft.cocit.dmengine.info.IRoleInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.dmengine.info.IUserInfo;
import com.jsoft.cocit.exception.CocDBException;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @preserve
 */
public class EntityInfoFactoryImpl implements IEntityInfoFactory {

	private EntityDao helper;
	private Map<Serializable, ICocCatalogInfo> cocCatalogMap;
	private Map<Serializable, ICocEntityInfo> cocEntityMap;
	private Map<Serializable, IDataSourceInfo> dataSourceMap;
	private ITenantInfo emptyTenant;
	private Map<Serializable, ITenantInfo> tenantMap;
	private ISystemInfo emptySystem;
	private Map<Serializable, ISystemInfo> systemMap;
	private Map<Serializable, IDicInfo> dicMap;

	public EntityInfoFactoryImpl() {
		helper = EntityDao.get();
		tenantMap = new HashMap();
		cocCatalogMap = new HashMap();
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

		cocCatalogMap.clear();

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
	public List<ISystemEntity> getSystems() {
		try {
			return helper.getSystems();
		} catch (Throwable e) {
			return null;
		}
	}

	public ISystemInfo getSystem(String systemCode) {
		if (emptySystem == null) {
			try {
				ISystemEntity system = EntityTypes.System.newInstance();
				emptySystem = new EmptySystemInfo(system);
			} catch (Throwable e) {
				throw new CocException(e);
			}
		}

		// if (StringUtil.isBlank(systemCode)) {
		// ICocConfig config = Cocit.me().getConfig();
		// systemCode = config.getDefaultSystemCode();
		// }
		// if (StringUtil.isBlank(systemCode)) {
		// ICocConfig config = Cocit.me().getConfig();
		// systemCode = config.getCocitSystemCode();
		// }

		if (StringUtil.hasContent(systemCode)) {
			try {
				ISystemInfo ret = systemMap.get(systemCode);

				if (ret == null) {
					ISystemEntity obj = helper.getSystem(systemCode);
					if (obj != null) {
						ret = new SystemInfo(obj);
						systemMap.put(obj.getCode(), ret);
					}
				}

				if (ret == null) {
					return emptySystem;
				}

				return ret;
			} catch (CocDBException e) {
				return emptySystem;
			}
		}

		return emptySystem;
	}

	public ITenantInfo getTenant(String tenantCode) {
		if (emptyTenant == null) {
			try {
				ITenantEntity tenant = EntityTypes.Tenant.newInstance();
				emptyTenant = new EmptyTenantInfo(tenant);
			} catch (Throwable e) {
				throw new CocException(e);
			}
		}

		if (StringUtil.hasContent(tenantCode)) {
			try {
				ITenantInfo ret = tenantMap.get(tenantCode);

				if (ret == null) {
					ITenantEntityExt obj = (ITenantEntityExt) helper.getTenant(tenantCode);

					if (obj == null) {
						ICocConfig config = Cocit.me().getConfig();
						if (config.getCocitTenantCode().equals(tenantCode)) {
							try {
								obj = (ITenantEntityExt) EntityTypes.Tenant.newInstance();
								obj.setName(config.getCocitTenantName());
								obj.setCode(config.getCocitTenantCode());
							} catch (Throwable e) {
								throw new CocException(e);
							}
						}
					}

					if (obj != null) {
						ret = new TenantInfo(obj);
						tenantMap.put(obj.getCode(), ret);
					}
				}

				return ret;
			} catch (CocDBException e1) {
				return emptyTenant;
			}
		}

		return emptyTenant;
	}

	public ICocCatalogInfo getCocCatalog(Serializable catalogID) {
		if (catalogID == null || StringUtil.isBlank(catalogID.toString()))
			return null;

		ICocCatalogInfo ret = cocCatalogMap.get(catalogID);

		if (ret == null) {
			ICocCatalogEntity obj = helper.getCocCatalog(catalogID);
			if (obj != null) {
				ret = new CocCatalogInfo(obj);
				cocCatalogMap.put(obj.getId(), ret);
				cocCatalogMap.put(obj.getCode(), ret);
			}
		}

		return ret;
	}

	public ICocEntityInfo getEntity(Serializable moduleID) {
		if (moduleID == null)
			return null;

		ICocEntityInfo ret = cocEntityMap.get(moduleID);

		if (ret == null) {
			ICocEntity obj = helper.getEntity(moduleID);
			if (obj != null) {
				ret = new CocEntityInfo(obj);
				cocEntityMap.put(obj.getId(), ret);
				cocEntityMap.put(obj.getCode(), ret);
			}
		}

		return ret;
	}

	public IDataSourceInfo getDataSource(Serializable dataSourceID) {
		if (dataSourceID == null)
			return null;

		IDataSourceInfo ret = dataSourceMap.get(dataSourceID);

		if (ret == null) {
			IDataSourceEntity obj = (IDataSourceEntity) helper.getDataSource(dataSourceID);
			if (obj != null) {
				ret = new DataSourceInfo(obj);
				dataSourceMap.put(obj.getId(), ret);
				dataSourceMap.put(obj.getCode(), ret);
			}
		}

		return ret;
	}

	public IUserInfo getUserByCodeOrTel(String tenantCode, String username) {
		IUserEntity obj = helper.getSystemUserByCodeOrTel(tenantCode, username);
		if (obj == null)
			return null;

		return new SecurityUserInfo(obj);
	}

	public IDicInfo getDic(Serializable id) {

		if (id == null)
			return null;

		IDicInfo ret = dicMap.get(id);

		if (ret == null) {
			IDicEntity obj = (IDicEntity) helper.get(EntityTypes.Dic, id);
			if (obj != null) {
				ret = new DicInfo(obj);
				dicMap.put(obj.getId(), ret);
				dicMap.put(obj.getCode(), ret);
			}
		}

		return ret;
	}

	@Override
	public IRoleInfo getRole(String tenantCode, String roleCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGroupInfo getGroup(String tenantCode, String groupCode) {
		// TODO Auto-generated method stub
		return null;
	}
}
