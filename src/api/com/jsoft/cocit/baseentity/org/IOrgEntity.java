package com.jsoft.cocit.baseentity.org;

import com.jsoft.cocit.baseentity.IOfTenantEntity;
import com.jsoft.cocit.baseentity.ITreeEntity;

/**
 * 【组织机构】接口：该接口是【{@link ITreeEntity}】和【{@link IOfTenantEntity}】的子接口，其实现类将映射到组织机构数据表，用来存储组织机构信息。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IOrgEntity extends ITreeEntity, IOfTenantEntity {

}
