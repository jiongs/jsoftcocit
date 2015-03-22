package com.jsoft.cocit.config;

import java.util.Properties;

/**
 * 动态实体接口：实现该接口的所有实体类支持支持动态属性，其实体对象支持动态字段值。
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface IDynaBean {

	Object get(String extField);

	IDynaBean set(String extField, Object fieldValue);

	Properties getProperties();
}
