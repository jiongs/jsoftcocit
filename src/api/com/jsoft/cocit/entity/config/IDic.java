package com.jsoft.cocit.entity.config;

import com.jsoft.cocit.entity.INamedEntity;
import com.jsoft.cocit.entity.ITreeEntity;

/**
 * 【字典】接口：是【{@link INamedEntity}】和【{@link ITreeEntity}】接口的子接口，用来对字典信息进行分类。
 * <p>
 * 字典内容由字典条目【{@link IDicItem}】组成。
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IDic extends INamedEntity, ITreeEntity {
}
