package com.jsoft.cocit.entity.config;

import com.jsoft.cocit.entity.INamedEntity;

/**
 * 【字典条目】接口：该接口是【{@link INamedEntity}】和接口的子接口，用来描述字典配置项信息。
 * <OL>
 * <LI>字典编码【{@link #getDicKey()}】：逻辑外键，关联到【{@link IDic#getKey()}】字段，用来描述该字典条目所属的字典。
 * <LI>字典名称【{@link #getDicName()}】：冗余字段，关联到【{@link IDic#getName()}】字段。
 * <LI>配置项编码【{@link #getText()}】：配置项KEY，该方法是【{@link #getKey()}】的代理方法。
 * <LI>配置项内容【{@link #getValue()}】：
 * <LI>配置项描述【{@link #getDescription()}】：
 * </OL>
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IDicItem extends INamedEntity, IPreference {

	String getDicKey();

	// String getDicName();
}
