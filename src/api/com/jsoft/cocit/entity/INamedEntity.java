package com.jsoft.cocit.entity;

/**
 * 【有名字的实体】接口：是【{@link IDataEntity}】接口的子接口，该接口的数据对象所映射的数据是可以被命名的，有名字的数据实体。
 * <OL>
 * <LI>数据名称【{@link #getName()}】：大多数数据实体都是带名字(或标题)的、可命名的实体。如单位名称、产品名称等等。
 * <LI>数据序号【{@link #getSn()}】：通常可命名的数据，都有个序号，用来对数据列表进行排序。
 * </OL>
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface INamedEntity extends IDataEntity {

	String getName();

	Integer getSn();

}
