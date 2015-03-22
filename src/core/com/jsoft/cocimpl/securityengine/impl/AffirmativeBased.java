package com.jsoft.cocimpl.securityengine.impl;

import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.securityengine.AccessDecisionVoter;
import com.jsoft.cocit.securityengine.Authentication;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;

public class AffirmativeBased extends AbstractAccessDecisionManager {
	public void decide(Authentication authentication, Object object, AuthorizedObjectDefinition config) {
		int deny = 0;

		for (AccessDecisionVoter voter : getDecisionVoters()) {
			int result = voter.vote(authentication, object, config);

			switch (result) {
				case AccessDecisionVoter.ACCESS_GRANTED:
					return;

				case AccessDecisionVoter.ACCESS_DENIED:
					deny++;

					break;

				default:
					break;
			}
		}

		if (deny > 0) {
			throw new CocSecurityException("您没有足够的权限！");
		}

		checkAllowIfAllAbstainDecisions();
	}
}