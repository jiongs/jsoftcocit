package com.jsoft.cocit.baseentity;


/**
 * 【树形结构实体】接口：该接口是【{@link INamedEntity}】接口的子接口，实现该接口的所有实体类均为树形实体类，实体对象集是一棵自身递归树。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface ITreeObjectEntity<T> extends INamedEntity {

	T getParent();

}
