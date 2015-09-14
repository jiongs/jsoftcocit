package com.jsoft.cocimpl.securityengine.impl;

import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.securityengine.SecurityVoter;
import com.jsoft.cocit.securityengine.Authentication;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;

/**
 * 一票否决：即只要有一个人投了反对票，即授权失败！
 * 
 * @author Ji Yongshan
 * 
 */
public class VoterManagerAffirmative extends VoterManager {
	public void decide(Authentication authentication, Object object, AuthorizedObjectDefinition config) {
		int deny = 0;

		for (SecurityVoter voter : getDecisionVoters()) {
			int result = voter.vote(authentication, object, config);

			switch (result) {
				case SecurityVoter.ACCESS_GRANTED:
					return;

				case SecurityVoter.ACCESS_DENIED:
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