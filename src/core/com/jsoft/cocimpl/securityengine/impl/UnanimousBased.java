package com.jsoft.cocimpl.securityengine.impl;

import java.util.Iterator;

import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.securityengine.AccessDecisionVoter;
import com.jsoft.cocit.securityengine.Authentication;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;

public class UnanimousBased extends AbstractAccessDecisionManager {
	public void decide(Authentication authentication, Object object, AuthorizedObjectDefinition config) {

		int grant = 0;
		// int abstain = 0;

		Iterator<AccessDecisionVoter> voters = this.getDecisionVoters().iterator();
		while (voters.hasNext()) {
			AccessDecisionVoter voter = voters.next();
			int result = voter.vote(authentication, object, config);

			switch (result) {
				case AccessDecisionVoter.ACCESS_GRANTED:
					grant++;

					break;

				case AccessDecisionVoter.ACCESS_DENIED:
					throw new CocSecurityException("您没有足够的权限！");

				default:
					// abstain++;

					break;
			}
		}

		if (grant > 0) {
			return;
		}

		checkAllowIfAllAbstainDecisions();
	}
}