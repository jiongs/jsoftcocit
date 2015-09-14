package com.jsoft.cocit.dmengine;

import java.io.Serializable;
import java.util.List;

import com.jsoft.cocit.baseentity.security.ISystemEntity;
import com.jsoft.cocit.dmengine.info.ICocCatalogInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.IDataSourceInfo;
import com.jsoft.cocit.dmengine.info.IDicInfo;
import com.jsoft.cocit.dmengine.info.IGroupInfo;
import com.jsoft.cocit.dmengine.info.IRoleInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.dmengine.info.IUserInfo;

public interface IEntityInfoFactory {

	void release();

	List<ISystemEntity> getSystems();

	ISystemInfo getSystem(String systemKey);

	ITenantInfo getTenant(String tenantKey);

	ICocCatalogInfo getCocCatalog(Serializable catalogID);

	ICocEntityInfo getEntity(Serializable moduleID);

	IDataSourceInfo getDataSource(Serializable dataSourceID);

	IUserInfo getUserByCodeOrTel(String tenantKey, String username);

	IRoleInfo getRole(String tenantKey, String roleKey);

	IGroupInfo getGroup(String tenantKey, String groupKey);

	IDicInfo getDic(Serializable id);

}
