package com.jsoft.cocit.baseentity.website;

import java.util.List;

import com.jsoft.cocit.baseentity.INamedEntity;

/**
 * 网站内容
 * 
 * @author yongshan.ji
 * 
 */
public interface IWebContent extends INamedEntity {
	public List<String> getImagesList();

	public String getRuntimeLinkUrl();
}
