package com.jsoft.cocit.entity.security;

import java.util.Date;

/**
 * 用于描述群组由哪些成员组成？成员可以是用户；也可以是群组？
 */
public interface IGroupMember {

	IGroup getGroup();

	ISystemUser getMemberUser();

	IGroup getMemberGroup();

	Date getExpiredFrom();

	Date getExpiredTo();

}
