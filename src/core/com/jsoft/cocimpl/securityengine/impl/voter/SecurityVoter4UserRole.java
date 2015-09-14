package com.jsoft.cocimpl.securityengine.impl.voter;

import com.jsoft.cocit.constant.PrincipalTypes;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.dmengine.info.IUserInfo;
import com.jsoft.cocit.securityengine.SecurityVoter;
import com.jsoft.cocit.securityengine.Authentication;
import com.jsoft.cocit.securityengine.AuthorizedObject;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;
import com.jsoft.cocit.securityengine.ILoginSession;

public class SecurityVoter4UserRole implements SecurityVoter {

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
		IUserInfo user = login.getUser();

		for (String role : user.getRolesList()) {
			if (PrincipalTypes.ROLE_ROOT.equals(role)) {
				return ACCESS_GRANTED;
			}
			
			
			
		}

		return ACCESS_ABSTAIN;
	}
}
