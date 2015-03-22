package com.jsoft.cocit.entity;


/**
 * “树形结构实体”接口：实现该接口的所有实体类均为树形实体类，实体对象集是一棵自身递归树。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IExtTreeEntity extends IExtNamedEntity, ITreeEntity {

	public void setParentKey(String parentKey);

	void setParentName(String parentName);
}
