package com.jsoft.cocit.baseentity.website;

import com.jsoft.cocit.baseentity.ITreeObjectEntityExt;

/**
 * 网站栏目
 * 
 * @author yongshan.ji
 * 
 */
public interface IWebCategory<T> extends ITreeObjectEntityExt<T> {
	String getRuntimeCategoryLinkUrl();

	String getRuntimeContentLinkUrl(IWebContent content);
}
