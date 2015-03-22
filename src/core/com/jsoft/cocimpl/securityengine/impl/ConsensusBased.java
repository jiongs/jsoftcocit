package com.jsoft.cocimpl.securityengine.impl;

import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.securityengine.AccessDecisionVoter;
import com.jsoft.cocit.securityengine.Authentication;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;

public class ConsensusBased extends AbstractAccessDecisionManager {

	private boolean allowIfEqualGrantedDeniedDecisions = true;

	public void decide(Authentication authentication, Object object, AuthorizedObjectDefinition config) {
		int grant = 0;
		int deny = 0;
		// int abstain = 0;

		for (AccessDecisionVoter voter : getDecisionVoters()) {
			int result = voter.vote(authentication, object, config);

			switch (result) {
				case AccessDecisionVoter.ACCESS_GRANTED:
					grant++;

					break;

				case AccessDecisionVoter.ACCESS_DENIED:
					deny++;

					break;

				default:
					// abstain++;

					break;
			}
		}

		if (grant > deny) {
			return;
		}

		if (deny > grant) {
			throw new CocSecurityException("您没有足够的权限！");
		}

		if ((grant == deny) && (grant != 0)) {
			if (this.allowIfEqualGrantedDeniedDecisions) {
				return;
			} else {
				throw new CocSecurityException("您没有足够的权限！");
			}
		}

		checkAllowIfAllAbstainDecisions();
	}

	public boolean isAllowIfEqualGrantedDeniedDecisions() {
		return allowIfEqualGrantedDeniedDecisions;
	}

	public void setAllowIfEqualGrantedDeniedDecisions(boolean allowIfEqualGrantedDeniedDecisions) {
		this.allowIfEqualGrantedDeniedDecisions = allowIfEqualGrantedDeniedDecisions;
	}
}
