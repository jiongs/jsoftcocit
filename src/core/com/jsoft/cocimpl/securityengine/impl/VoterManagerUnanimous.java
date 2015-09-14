package com.jsoft.cocimpl.securityengine.impl;

import java.util.Iterator;
import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.securityengine.SecurityVoter;
import com.jsoft.cocit.securityengine.Authentication;
import com.jsoft.cocit.securityengine.AuthorizedObjectDefinition;
import com.jsoft.cocit.securityengine.ILoginSession;
import com.jsoft.cocit.securityengine.SecurityVoter4Runtime;

/**
 * 全票通过、一票否决制
 * 
 * @author Ji Yongshan
 * 
 */
public class VoterManagerUnanimous extends VoterManager {
	public void decide(Authentication authentication, Object object, AuthorizedObjectDefinition config) {

		int grant = 0;

		/*
		 * 计算临时权限
		 */
		// int abstain = 0;
		HttpContext httpContext = Cocit.me().getHttpContext();
		if (httpContext != null) {

			// Request临时权限
			List<SecurityVoter> voters = httpContext.getAccessVoters();
			for (SecurityVoter voter : voters) {
				switch (voter.vote(authentication, object, config)) {
					case SecurityVoter.ACCESS_GRANTED:
						grant++;
						break;
					case SecurityVoter.ACCESS_DENIED:
						throw new CocSecurityException("您没有足够的权限！");
					default:
						break;
				}
			}

			// Session临时权限
			String voterKey = httpContext.getRequest().getParameter(SecurityVoter4Runtime.VOTER_KEY);
			ILoginSession lsession = httpContext.getLoginSession();
			if (voterKey != null && lsession != null) {
				SecurityVoter voter = lsession.removeAccessVoter(voterKey);
				if (voter != null) {
					switch (voter.vote(authentication, object, config)) {
						case SecurityVoter.ACCESS_GRANTED:
							grant++;
							break;
						case SecurityVoter.ACCESS_DENIED:
							throw new CocSecurityException("您没有足够的权限！");
						default:
							break;
					}
				}
			}

			//
		}

		/*
		 * 临时允许
		 */
		if (grant > 0) {
			return;
		}

		/*
		 * 计算正常权限
		 */
		Iterator<SecurityVoter> voters = this.getDecisionVoters().iterator();
		while (voters.hasNext()) {
			SecurityVoter voter = voters.next();
			int result = voter.vote(authentication, object, config);

			switch (result) {
				case SecurityVoter.ACCESS_GRANTED:
					grant++;

					break;

				case SecurityVoter.ACCESS_DENIED:
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