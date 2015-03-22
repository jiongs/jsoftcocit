package com.jsoft.cocit.entity.website;

import java.util.List;

import com.jsoft.cocit.entity.INamedEntity;

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
