package com.jsoft.cocit.securityengine;

public interface AccessDecisionVoter {

	int ACCESS_GRANTED = 1;
	int ACCESS_ABSTAIN = 0;
	int ACCESS_DENIED = -1;

	boolean supports(AuthorizedObject obj);

	boolean supports(Class typeOfAuthentication);

	int vote(Authentication authentication, Object object, AuthorizedObjectDefinition objectDefinition);
}
