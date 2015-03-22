package com.jsoft.cocit.entity.security;

import java.util.Date;

import com.jsoft.cocit.entity.IExtNamedEntity;

public interface IExtTenant extends ITenant, IExtNamedEntity {

	void setSystemKey(String key);

	void setDataSourceKey(String key);

	void setExpiredFrom(Date from);

	void setExpiredTo(Date to);

	void setDomain(String domain);

}
