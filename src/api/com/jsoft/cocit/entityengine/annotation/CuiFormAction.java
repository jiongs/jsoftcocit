package com.jsoft.cocit.entityengine.annotation;

/**
 * 表单操作：
 * <p>
 * 功能：用来指定表单操作按钮
 * 
 * @author Ji Yongshan
 * 
 */
public @interface CuiFormAction {

	/**
	 * 操作编号：其值来自({@link CocAction#key()})
	 * 
	 * @return
	 */
	String action();

	/**
	 * 操作按钮名称：用来替换({@link CocAction#name()})
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * 操作按钮名称：用来替换({@link CocAction#title()})
	 * 
	 * @return
	 */
	String title() default "";
}
