package com.jsoft.cocit.entityengine.service;

import java.io.Serializable;
import java.util.List;

import com.jsoft.cocit.entity.security.ISystem;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;

public interface SystemService extends NamedEntityService<ISystem>, ISystem, AuthorizedObjectDefinition {
	SystemMenuService getSystemMenu(Serializable systemMenuID);

	SystemMenuService getSystemMenuByModule(String moduleKey);

	<T> T getConfigItem(String key, T defaultValue);

	List<SystemMenuService> getSystemMenus();

	// Tree makeFuncMenu(LoginSession login, String entityUrlPrefix);
}
