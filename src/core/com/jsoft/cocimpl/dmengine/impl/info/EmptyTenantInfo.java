package com.jsoft.cocimpl.dmengine.impl.info;

import com.jsoft.cocit.baseentity.security.ITenantEntity;
import com.jsoft.cocit.dmengine.info.IDataSourceInfo;

public class EmptyTenantInfo extends TenantInfo {

	@Override
	public void release() {
		super.release();
	}

	EmptyTenantInfo(ITenantEntity obj) {
		super(obj);
	}

	public IDataSourceInfo getDataSource() {
		return null;
	}

}