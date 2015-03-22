package com.jsoft.cocit.entity;


/**
 * 命名实体：即可以被命名的，有名字的实体。
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface IExtNamedEntity extends IExtDataEntity, INamedEntity {

	void setName(String name);

	void setSn(Integer sn);
}
