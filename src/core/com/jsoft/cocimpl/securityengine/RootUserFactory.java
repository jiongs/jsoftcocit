package com.jsoft.cocimpl.securityengine;

import com.jsoft.cocit.baseentity.security.IUserEntity;
import com.jsoft.cocit.dmengine.info.IUserInfo;

public interface RootUserFactory {

	IUserInfo getRootUser(String username);

	void saveRootUser(IUserEntity user);

	void release();
}
