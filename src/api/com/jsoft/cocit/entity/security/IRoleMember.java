package com.jsoft.cocit.entity.security;

import java.util.Date;

public interface IRoleMember {

	String getMenuKey();

	String getRoleKey();

	Date getExpiredFrom();

	Date getExpiredTo();

}
