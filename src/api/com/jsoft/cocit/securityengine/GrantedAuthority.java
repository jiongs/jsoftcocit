package com.jsoft.cocit.securityengine;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable {
	String getAuthority();
}