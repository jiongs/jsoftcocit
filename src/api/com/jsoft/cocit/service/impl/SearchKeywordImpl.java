package com.jsoft.cocit.service.impl;

import com.jsoft.cocit.service.ISearchKeyword;

public class SearchKeywordImpl implements ISearchKeyword {

	private String category;
	private String keyword;

	private SearchKeywordImpl(String category, String keyword) {
		this.category = category;
		this.keyword = keyword;
	}

	static SearchKeywordImpl make(String category, String keyword) {
		return new SearchKeywordImpl(category, keyword);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
