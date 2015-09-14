package com.jsoft.cocit.baseentity.config;

import com.jsoft.cocit.baseentity.INamedEntity;
import com.jsoft.cocit.baseentity.ITreeEntity;

/**
 * 【字典】接口：是【{@link INamedEntity}】和【{@link ITreeEntity}】接口的子接口，用来对字典信息进行分类。
 * <p>
 * 字典内容由字典条目【{@link IDicItemEntity}】组成。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IDicEntity extends INamedEntity, ITreeEntity {
}
