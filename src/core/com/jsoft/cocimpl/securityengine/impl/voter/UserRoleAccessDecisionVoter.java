package com.jsoft.cocimpl.securityengine.impl.voter;

import com.jsoft.cocit.constant.PrincipalTypes;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.entityengine.service.UserService;
import com.jsoft.cocit.securityengine.AccessDecisionVoter;
import com.jsoft.cocit.securityengine.Authentication;
import com.jsoft.cocit.securityengine.AuthorizedObject;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;
import com.jsoft.cocit.securityengine.LoginSession;

public class UserRoleAccessDecisionVoter implements AccessDecisionVoter {

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
		UserService user = login.getUser();

		for (String role : user.getRolesList()) {
			if (PrincipalTypes.ROLE_ROOT.equals(role)) {
				return ACCESS_GRANTED;
			}
		}

		return ACCESS_ABSTAIN;
	}
}
