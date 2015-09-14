package com.jsoft.cocit.baseentity;

import com.jsoft.cocit.baseentity.org.IOrgEntity;

/**
 * 【组织机构的数据实体】接口：该接口是【{@link IOfTenantEntity}】接口的自接口，该接口的实现类所映射的数据表用来存储组织机构相关的基础业务数据。
 * <OL>
 * <LI>组织机构KEY【{@link #getOrgCode()}】：逻辑外键，关联到【{@link IOrgEntity#getCode()}】字段，用来表示这条数据是哪个部门或哪个下级单位的？
 * <LI>组织机构名称【{@link #getOrgName()}】：冗余字段，关联到【{@link IOrgEntity#getName()}】字段。
 * </OL>
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IOfOrgEntity extends IOfTenantEntity {

	String getOrgCode();

	String getOrgName();

}
