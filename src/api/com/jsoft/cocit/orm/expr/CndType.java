package com.jsoft.cocit.orm.expr;

/**
 * 
 *
 */
public enum CndType {
	/**
	 * 大于 Greater
	 */
	gt,
	/**
	 * 大于等于 Greater Equals
	 */
	ge,
	/**
	 * 小于 Greater
	 */
	lt,
	/**
	 * 小于等于 Less Equals
	 */
	le,
	/**
	 * 不等于 Not Equals
	 */
	ne,
	/**
	 * 等于 Equals
	 */
	eq,
	/**
	 * 在列表中 In
	 */
	in,
	/**
	 * 不在列表中 Not In
	 */
	ni,
	/**
	 * 匹配 Like
	 */
	lk,
	/**
	 * 包含 Contiains
	 */
	cn,
	/**
	 * 不匹配Not Like
	 */
	nl,
	/**
	 * 在什么之间 Between
	 */
	bt,
	/**
	 * 为空 Null
	 */
	nu,
	/**
	 * 非空 Not Nulll
	 */
	nn,
	/**
	 * 以...结尾 End With ...
	 */
	ew,
	/**
	 * 以...开头 Begin With ...
	 */
	bw
}
