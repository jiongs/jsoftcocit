package com.jsoft.cocit.entity.org;

import com.jsoft.cocit.entity.ITenantOwnerEntity;
import com.jsoft.cocit.entity.ITreeEntity;

/**
 * 【组织机构】接口：该接口是【{@link ITreeEntity}】和【{@link ITenantOwnerEntity}】的子接口，其实现类将映射到组织机构数据表，用来存储组织机构信息。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IOrg extends ITreeEntity, ITenantOwnerEntity {

}
