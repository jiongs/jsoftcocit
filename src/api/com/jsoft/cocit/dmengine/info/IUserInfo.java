package com.jsoft.cocit.dmengine.info;

import java.util.List;

import com.jsoft.cocit.baseentity.security.IUserEntity;

public interface IUserInfo extends INamedEntityInfo<IUserEntity>, IUserEntity {

	<T> T getConfigItem(String key, T defaultValue);

	List<String> getRolesList();
}
