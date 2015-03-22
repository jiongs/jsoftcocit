package com.jsoft.cocit.service;

/**
 * 
 * @author Ji Yongshan
 * 
 */
public interface ISearchKeyword {

	/**
	 * 获取“搜索关键字”所属分类，关联到{@link ISearchCategory#getCategory()}。
	 * 
	 * @return
	 */
	public String getCategory();

	/**
	 * 获取“搜索关键字”
	 * 
	 * @return
	 */
	public String getKeyword();
}
