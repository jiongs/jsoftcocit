package com.jsoft.cocimpl.securityengine.impl.voter;

import com.jsoft.cocimpl.securityengine.impl.RootUserInfo;
import com.jsoft.cocit.baseentity.security.IPermissionEntity;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.securityengine.SecurityVoter;
import com.jsoft.cocit.securityengine.Authentication;
import com.jsoft.cocit.securityengine.AuthorizedObject;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;
import com.jsoft.cocit.securityengine.GrantedAuthority;
import com.jsoft.cocit.securityengine.ILoginSession;

public class SecurityVoter4UserMenu implements SecurityVoter {

	@Override
	public boolean supports(AuthorizedObject obj) {
		if (ISystemMenuInfo.class.isAssignableFrom(obj.getClass()))
			return true;

		return false;
	}

	@Override
	public boolean supports(Class typeOfAuthentication) {
		if (ILoginSession.class.isAssignableFrom(typeOfAuthentication))
			return true;

		return false;
	}

	@Override
	public int vote(Authentication authentication, Object object, AuthorizedObjectDefinition definition) {

		ILoginSession login = (ILoginSession) authentication;
		if (login.getUser() instanceof RootUserInfo) {
			return ACCESS_GRANTED;
		}

		GrantedAuthority[] authorities = authentication.getAuthorities();

		ISystemMenuInfo menu = (ISystemMenuInfo) object;
		for (GrantedAuthority a : authorities) {
			IPermissionEntity auth = (IPermissionEntity) a;
			if (auth.getMenuCode().equals(menu.getCode())) {
				return ACCESS_GRANTED;
			}
		}

		return ACCESS_ABSTAIN;
	}

}
