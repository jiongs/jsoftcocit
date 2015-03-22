package com.jsoft.cocit.securityengine;

import java.io.Serializable;

public interface AuthorizedObject extends Serializable {
	String getAuthrizedName();
}