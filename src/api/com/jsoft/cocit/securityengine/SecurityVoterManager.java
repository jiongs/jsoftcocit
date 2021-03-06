package com.jsoft.cocit.securityengine;

public interface SecurityVoterManager {

	void decide(Authentication authentication, Object object, AuthorizedObjectDefinition definition);

	boolean supports(AuthorizedObjectDefinition definition);

	boolean supports(Class typeOfAuthentication);

}