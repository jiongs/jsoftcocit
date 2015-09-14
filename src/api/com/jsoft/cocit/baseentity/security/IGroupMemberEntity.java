package com.jsoft.cocit.baseentity.security;

import java.util.Date;

/**
 * 用于描述群组由哪些成员组成？成员可以是用户；也可以是群组？
 */
public interface IGroupMemberEntity {

	IGroupEntity getGroup();

	ISystemUserEntity getMemberUser();

	Date getExpiredFrom();

	Date getExpiredTo();

}
