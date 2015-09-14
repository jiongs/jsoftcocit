package com.jsoft.cocit.dmengine.info;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.baseentity.security.ISystemEntity;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;

public interface ISystemInfo extends INamedEntityInfo<ISystemEntity>, ISystemEntity, AuthorizedObjectDefinition {
	ISystemMenuInfo getSystemMenu(Serializable systemMenuID);

	ISystemMenuInfo getSystemMenuByModule(String moduleKey);

	<T> T getConfigItem(String key, T defaultValue);

	Map getConfigs();

	List<ISystemMenuInfo> getSystemMenus();

	// Tree makeFuncMenu(LoginSession login, String entityUrlPrefix);
}
