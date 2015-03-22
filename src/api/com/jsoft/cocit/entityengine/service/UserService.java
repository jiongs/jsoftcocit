package com.jsoft.cocit.entityengine.service;

import java.util.List;

import com.jsoft.cocit.entity.security.IUser;

public interface UserService extends NamedEntityService<IUser>, IUser {

	<T> T getConfigItem(String key, T defaultValue);

	List<String> getRolesList();
}
