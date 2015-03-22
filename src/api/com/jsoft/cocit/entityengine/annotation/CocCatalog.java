package com.jsoft.cocit.entityengine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用在接口 "package_info.java" 类上，用来生成“模块分类”或“菜单分类(文件夹菜单)”
 * <p>
 * 每个实体包或实体包所在的上级包可以逐级包含一个接口文件(package_info.java)，该注解用在接口文件上，平台升级后会自动创建实体分类和文件夹菜单。
 */
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CocCatalog {

	/**
	 * 分类名称|菜单名称
	 */
	public String name();

	/**
	 * 分类编号|菜单编号
	 */
	public String key();

	/**
	 * 父类编号|父菜单编号
	 */
	public String parentKey() default "";

	/**
	 * 分类顺序|菜单顺序
	 * 
	 */
	public int sn() default 0;

	/**
	 * 备注信息
	 */
	public String notes() default "";

}
