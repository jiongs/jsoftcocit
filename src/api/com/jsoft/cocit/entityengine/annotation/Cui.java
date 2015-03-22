package com.jsoft.cocit.entityengine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <B>实体主界面UI注解：</B>用来定义实体模块主界面。
 * 
 * @author Ji Yongshan
 * 
 */
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Cui {
	CuiEntity[] value();
}
