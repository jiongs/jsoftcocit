package com.jsoft.cocit.securityengine;

import javax.servlet.http.HttpServletRequest;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.ExtHttpContext;
import com.jsoft.cocit.util.StringUtil;

public abstract class SecurityVoter4Runtime {
	
	public static final String VOTER_KEY = "security.voterKey";

	public static final SecurityVoter ALWAYS_ALLOW;

	static {
		ALWAYS_ALLOW = new SecurityVoter() {

			@Override
			public boolean supports(AuthorizedObject obj) {
				return true;
			}

			@Override
			public boolean supports(Class typeOfAuthentication) {
				return true;
			}

			@Override
			public int vote(Authentication authentication, Object object, AuthorizedObjectDefinition objectDefinition) {
				return ACCESS_GRANTED;
			}

		};
	}

	public static void allow() {
		allow(false);
	}

	/**
	 * 生成一个Key放入Session中。
	 * 
	 * @return
	 */
	public static String allow(boolean isSession) {
		Cocit coc = Cocit.me();
		ExtHttpContext httpContext = (ExtHttpContext) coc.getHttpContext();
		ILoginSession loginSession = httpContext.getLoginSession();

		String voterKey = StringUtil.encodeHex(Long.toHexString(System.currentTimeMillis()));

		if (loginSession != null) {
			HttpServletRequest req = httpContext.getRequest();
			String referer = req.getHeader("referer");

			if (referer != null && referer.startsWith(loginSession.getAppURL())) {
				if (isSession)
					loginSession.putAccessVoter(voterKey, SecurityVoter4Runtime.ALWAYS_ALLOW);
				else
					httpContext.addAccessVoter(SecurityVoter4Runtime.ALWAYS_ALLOW);
			}
		}

		return voterKey;
	}
}
