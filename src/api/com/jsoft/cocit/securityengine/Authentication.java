package com.jsoft.cocit.securityengine;

import java.io.Serializable;
import java.security.Principal;

public interface Authentication extends Principal, Serializable {
	GrantedAuthority[] getAuthorities();

	Object getPrincipal();

	// Object getCredentials();
	//
	// Object getDetails();
	//
	boolean isAuthenticated();

	void setAuthenticated(boolean authenticated);
}