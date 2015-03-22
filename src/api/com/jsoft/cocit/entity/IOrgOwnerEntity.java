package com.jsoft.cocit.entity;

import com.jsoft.cocit.entity.org.IOrg;

/**
 * 【组织机构的数据实体】接口：该接口是【{@link ITenantOwnerEntity}】接口的自接口，该接口的实现类所映射的数据表用来存储组织机构相关的基础业务数据。
 * <OL>
 * <LI>组织机构KEY【{@link #getOrgKey()}】：逻辑外键，关联到【{@link IOrg#getKey()}】字段，用来表示这条数据是哪个部门或哪个下级单位的？
 * <LI>组织机构名称【{@link #getOrgName()}】：冗余字段，关联到【{@link IOrg#getName()}】字段。
 * </OL>
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IOrgOwnerEntity extends ITenantOwnerEntity {

	String getOrgKey();

	String getOrgName();

}
