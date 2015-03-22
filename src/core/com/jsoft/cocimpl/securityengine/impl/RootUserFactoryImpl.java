package com.jsoft.cocimpl.securityengine.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocimpl.config.impl.BaseConfig;
import com.jsoft.cocimpl.securityengine.RootUserFactory;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.IConfig;
import com.jsoft.cocit.entity.security.IUser;
import com.jsoft.cocit.entityengine.service.UserService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.StringUtil;

public class RootUserFactoryImpl extends BaseConfig implements RootUserFactory {
	private Map<String, RootUser> rootUserMap;

	public RootUserFactoryImpl() {
	}

	private void initRootUsers() {
		if (rootUserMap == null) {
			rootUserMap = new HashMap();
			initPath(Cocit.me().getConfig().getUserConfig());
			init();
		}
	}

	public void release() {
		if (rootUserMap != null) {
			rootUserMap.clear();
			rootUserMap = null;
		}
	}

	public UserService getRootUser(final String username) {
		initRootUsers();

		final String password = this.get(username);
		if (password == null) {
			return null;
		}

		RootUser user = rootUserMap.get(username);
		if (user == null) {
			user = new RootUser();

			user.setUsername(username);
			user.setPassword(password);

			rootUserMap.put(username, user);
		}

		return new RootUserService(user);
	}

	public void saveRootUser(IUser newUser) {
		if (newUser == null) {
			return;
		}
		String pwd = newUser.getPassword();
		if (!StringUtil.isBlank(pwd)) {
			this.put(newUser.getUsername(), pwd);
			try {
				this.save();
			} catch (IOException e) {
				throw new CocException(e);
			}
		}
	}

	public IConfig copy() {
		return this;
	}

	public static RootUser makeRootUser(String username, String rawpassword) {
		RootUser root = new RootUser();
		root.setKey(username);
		root.setRawPassword(rawpassword);
		root.setRawPassword2(rawpassword);

		return root;
	}

}
