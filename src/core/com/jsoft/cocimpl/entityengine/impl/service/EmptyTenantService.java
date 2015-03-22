package com.jsoft.cocimpl.entityengine.impl.service;

import com.jsoft.cocit.entity.security.ITenant;
import com.jsoft.cocit.entityengine.service.DataSourceService;

public class EmptyTenantService extends TenantServiceImpl {

	@Override
	public void release() {
		super.release();
	}

	EmptyTenantService(ITenant obj) {
		super(obj);
	}

	public DataSourceService getDataSource() {
		return null;
	}

}