package com.jsoft.cocimpl.securityengine;

import com.jsoft.cocit.entity.security.IUser;
import com.jsoft.cocit.entityengine.service.UserService;

public interface RootUserFactory {

	UserService getRootUser(String username);

	void saveRootUser(IUser user);

	void release();
}
