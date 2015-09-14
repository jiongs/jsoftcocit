package com.jsoft.cocit.baseentity.security;

import com.jsoft.cocit.baseentity.INamedEntityExt;

public interface ITenantEntityExt extends ITenantEntity, INamedEntityExt {

	void setDataSourceCode(String key);

	void setDomain(String domain);

}
