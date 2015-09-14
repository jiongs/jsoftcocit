package com.jsoft.cocit.baseentity;

/**
 * 【树形结构实体】接口：该接口是【{@link INamedEntity}】接口的子接口，实现该接口的所有实体类均为树形实体类，实体对象集是一棵自身递归树。
 * <OL>
 * <LI>父节点KEY【{@link #getParentCode()}】：逻辑外键，关联到 【{@link ITreeEntity#getCode()}】，表示该节点的父节点是谁？
 * <LI>父节点名称【{@link #getParentName()}】：冗余字段，关联到【 {@link ITreeEntity#getName()}】。
 * </OL>
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface ITreeEntity extends INamedEntity {

	String getParentCode();

	// String getParentName();

}
