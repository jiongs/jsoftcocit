package com.jsoft.cocit.baseentity.security;

import java.util.Date;

public interface IRoleMemberEntity {

	IRoleEntity getRole();

	ISystemUserEntity getMemberUser();

	IGroupEntity getMemberGroup();

	Date getExpiredFrom();

	Date getExpiredTo();

}
