package com.jsoft.cocit.entityengine;

import java.io.Serializable;
import java.util.List;

import com.jsoft.cocit.entity.security.ISystem;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.DataSourceService;
import com.jsoft.cocit.entityengine.service.DicService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.entityengine.service.TenantService;
import com.jsoft.cocit.entityengine.service.UserService;

public interface EntityServiceFactory {

	void release();

	List<ISystem> getSystems();

	SystemService getSystem(String systemKey);

	TenantService getTenant(String tenantKey);

	CocEntityService getEntity(Serializable moduleID);

	DataSourceService getDataSource(Serializable dataSourceID);

	UserService getUser(String tenantKey, String username);

	DicService getDic(Serializable id);

}
