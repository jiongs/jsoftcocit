package com.jsoft.cocit.baseentity.websitedef;

import com.jsoft.cocit.baseentity.INamedEntity;

/**
 * 视图组件库：描述可供使用的视图组件
 * 
 * @author yongshan.ji
 * 
 */
public interface IUIViewComponent extends INamedEntity {

	public String getViewTemplate();

	public String getViewExpression();

	public String getViewController();

	public IUIViewComponent getParent();

	// public Upload getImage();

	public int getDefaultWidth();

	public int getDefaultHeight();

	public boolean isSummOptions();

	public boolean isScrollOptions();

	public boolean isTitleOptions();

	public boolean isImageOptions();

}
