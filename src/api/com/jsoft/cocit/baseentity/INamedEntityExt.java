package com.jsoft.cocit.baseentity;

/**
 * 命名实体：即可以被命名的，有名字的实体。
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface INamedEntityExt extends IDataEntityExt, INamedEntity {

	void setName(String name);

	void setNamePinyin(String pinyin);

	void setNameAbbr(String name);

	void setNameAbbrPinyin(String pinyin);

	void setSn(Integer sn);
}
