package com.jsoft.cocit.baseentity;

import com.jsoft.cocit.baseentity.security.ITenantEntity;

/**
 * 【租户拥有的数据实体】接口：是【{@link IDataEntity}】接口的子接口，该接口的实现类所映射的实体表用来存储指定租户的基础业务数据。
 * <OL>
 * <LI>租户KEY【{@link #getTenantCode()}】：逻辑外键，关联到【{@link ITenantEntity#getCode()}】字段，表示这条数据属于哪个租户（单位、企业）所拥有？
 * <LI>租户名称【{@link #getTenantName()}】：冗余字段，关联到【{@link ITenantEntity#getName()}】字段。
 * </OL>
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IOfTenantEntity extends IDataEntity {

	String getTenantCode();

//	String getTenantName();
}
