package com.jsoft.cocit.constant;

public interface StatusCodes {

	/**
	 * “数据状态码”之“归档(999)”
	 * <UL>
	 * <LI>用来设置数据实体的“statusCode”字段；
	 * <LI>对于“归档(999)”状态的数据，不允许再作任何“update”类操作；
	 * <LI>对于“归档(999)”状态的数据，通常可以被移植到另外一张单独的数据表中，以完成物理层面的归档；
	 * <LI>定义新的状态码时，不允许和该常量值冲突；
	 * </UL>
	 */
	static final int STATUS_CODE_ARCHIVED = 999;

	/**
	 * “数据状态码”之“新建(0)”
	 * <UL>
	 * <LI>用来设置数据实体的“statusCode”字段；
	 * <LI>表示本条数据是新的，insert到数据表之后尚未对其作过任何update操作；
	 * <LI>定义新的状态码时，不允许和该常量值冲突；
	 * </UL>
	 */
	static final int STATUS_CODE_NEW = 0;

	/**
	 * “数据状态码”之“停用(-999)”
	 * <UL>
	 * <LI>用来设置数据实体的“statusCode”字段；
	 * <LI>对于“停用(-999)”状态的数据，不允许其参与任何业务逻辑的处理；
	 * <LI>对于“停用(-999)”状态的数据，可以对该数据执行“update”操作；
	 * <LI>定义新的状态码时，不允许和该常量值冲突；
	 * </UL>
	 */
	static final int STATUS_CODE_DISABLED = -999;

	/**
	 * “数据状态码”之“删除(-99999)”
	 * <UL>
	 * <LI>用来设置数据实体的“statusCode”字段；
	 * <LI>对于“删除(-99999)”状态的数据，表示其已被逻辑删除，不允许其参与任何业务逻辑的处理；
	 * <LI>对于“删除(-99999)”状态的数据，不允许对该数据执行“update”操作；
	 * <LI>对于“删除(-99999)”状态的数据，可以对该数据执行“delete”操作即物理删除；
	 * <LI>定义新的状态码时，不允许和该常量值冲突；
	 * </UL>
	 */
	static final int STATUS_CODE_REMOVED = -99999;

	/**
	 * “数据状态码”之“初始化数据(99990)”
	 */
	static final int STATUS_CODE_INITED = 99990;

	/**
	 * “数据状态码”之“预置(99999)”
	 * <UL>
	 * <LI>用来设置数据实体的“statusCode”字段；
	 * <LI>对于“预置(99999)”状态的数据，只能查询使用，不能对其执行update或delete操作，即“永久”之意；
	 * <LI>定义新的状态码时，不允许和该常量值冲突；
	 * </UL>
	 */
	static final int STATUS_CODE_BUILDIN = 99999;

}
