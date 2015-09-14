package com.jsoft.cocimpl.securityengine.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocimpl.config.impl.BaseConfig;
import com.jsoft.cocimpl.securityengine.RootUserFactory;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.baseentity.security.IUserEntity;
import com.jsoft.cocit.config.IConfig;
import com.jsoft.cocit.dmengine.info.IUserInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.StringUtil;

public class RootUserFactoryImpl extends BaseConfig implements RootUserFactory {
	private Map<String, RootUserEntity> rootUserMap;

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

	public IUserInfo getRootUser(final String username) {
		initRootUsers();

		final String password = this.get(username);
		if (password == null) {
			return null;
		}

		RootUserEntity user = rootUserMap.get(username);
		if (user == null) {
			user = new RootUserEntity();

			user.setUsername(username);
			user.setPassword(password);

			rootUserMap.put(username, user);
		}

		return new RootUserInfo(user);
	}

	public void saveRootUser(IUserEntity newUser) {
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

	public static RootUserEntity makeRootUser(String username, String rawpassword) {
		RootUserEntity root = new RootUserEntity();
		root.setCode(username);
		root.setRawPassword(rawpassword);
		root.setRawPassword2(rawpassword);

		return root;
	}

	public static void main(String[] args) {
		RootUserEntity root = makeRootUser("root", "akxx2015!@#$");
		System.out.println(root.getCode() + " = " + root.getPassword());
	}

}
