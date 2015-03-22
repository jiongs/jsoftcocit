package com.jsoft.cocit.entity.website;

import com.jsoft.cocit.entity.IExtTree2Entity;

/**
 * 网站栏目
 * 
 * @author yongshan.ji
 * 
 */
public interface IWebCategory<T> extends IExtTree2Entity<T> {
	String getRuntimeCategoryLinkUrl();

	String getRuntimeContentLinkUrl(IWebContent content);
}
