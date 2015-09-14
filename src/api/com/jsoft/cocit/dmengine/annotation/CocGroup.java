package com.jsoft.cocit.dmengine.annotation;

/**
 * 字段分组注释：使用该注释自动对业务字段进行分组。
 * 
 * @author jiongsoft
 * @preserve all
 * 
 */
public @interface CocGroup {

	// public long id() default 0;

	public String name() default "";

	public String code() default "";

	public String notes() default "";

	public int sn() default 0;

	public CocColumn[] fields() default {};
}
