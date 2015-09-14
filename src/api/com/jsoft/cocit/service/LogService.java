package com.jsoft.cocit.service;

import com.jsoft.cocit.baseentity.log.ILogLoginEntity;
import com.jsoft.cocit.baseentity.log.ILogOperationEntity;
import com.jsoft.cocit.baseentity.log.ILogVisitEntity;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;

public interface LogService {
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAILED = 0;

	public ILogVisitEntity makeVisitLog(Throwable e);

	public ILogLoginEntity makeLoginLog(String system, String user, int statusCode);

	public ILogOperationEntity makeOpLog(ISystemMenuInfo menuService, ICocEntityInfo entityService, String actionKey, Object dataObject, int result);
}
