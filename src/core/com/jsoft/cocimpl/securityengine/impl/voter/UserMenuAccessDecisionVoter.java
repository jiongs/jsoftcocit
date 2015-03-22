package com.jsoft.cocimpl.securityengine.impl.voter;

import com.jsoft.cocimpl.securityengine.impl.RootUserService;
import com.jsoft.cocit.entity.security.IAuthority;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.securityengine.AccessDecisionVoter;
import com.jsoft.cocit.securityengine.Authentication;
import com.jsoft.cocit.securityengine.AuthorizedObject;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;
import com.jsoft.cocit.securityengine.GrantedAuthority;
import com.jsoft.cocit.securityengine.LoginSession;

public class UserMenuAccessDecisionVoter implements AccessDecisionVoter {

	@Override
	public boolean supports(AuthorizedObject obj) {
		if (SystemMenuService.class.isAssignableFrom(obj.getClass()))
			return true;

		return false;
	}

	@Override
	public boolean supports(Class typeOfAuthentication) {
		if (LoginSession.class.isAssignableFrom(typeOfAuthentication))
			return true;

		return false;
	}

	@Override
	public int vote(Authentication authentication, Object object, AuthorizedObjectDefinition definition) {

		LoginSession login = (LoginSession) authentication;
		if (login.getUser() instanceof RootUserService) {
			return ACCESS_GRANTED;
		}

		GrantedAuthority[] authorities = authentication.getAuthorities();

		SystemMenuService menu = (SystemMenuService) object;
		for (GrantedAuthority a : authorities) {
			IAuthority auth = (IAuthority) a;
			if (auth.getMenuKey().equals(menu.getKey())) {
				return ACCESS_GRANTED;
			}
		}

		return ACCESS_ABSTAIN;
	}

}
