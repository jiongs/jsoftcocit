package com.jsoft.cocit.baseentity.coc;

import com.jsoft.cocit.baseentity.ITreeEntity;

/**
 * 实体分类：用来对“实体定义（{@link ICocEntity}）”进行分类。
 * 
 * @author jiongsoft
 * @preserve all
 * 
 */
public interface ICocCatalogEntity extends ITreeEntity {

	String getPrevInterceptors();

	String getPostInterceptors();
}
