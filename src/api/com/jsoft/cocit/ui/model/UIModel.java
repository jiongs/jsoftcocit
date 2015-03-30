package com.jsoft.cocit.ui.model;

import java.io.Writer;

import com.jsoft.cocimpl.ui.UIView;
import com.jsoft.cocit.constant.Const;

/**
 * UI模型：表示界面窗体的逻辑模型。
 * <UL>
 * <LI>可以是不含数据的窗体模型；
 * <LI>可以是包含数据的窗体模型；
 * <LI>可以是没有窗体的数据模型；
 * <LI>UI模型由{@link CuiModuleMnR}创建；
 * <LI>UI模型通过Action方法返回；
 * <LI>UI模型被{@link UIView}输出到浏览器；
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public interface UIModel {
	String getViewName();

	/**
	 * Get HttpServletResponse Content Type
	 * 
	 * <B>可选值</B>
	 * <UL>
	 * <LI>HTML: {@link Const#CONTENT_TYPE_HTML}
	 * <LI>Json: {@link Const#CONTENT_TYPE_JSON};
	 * <LI>XML: {@link Const#CONTENT_TYPE_XML};
	 * </UL>
	 * 
	 * @return
	 */
	String getContentType();

	/**
	 * 判断是否支持浏览器端cache?
	 * 
	 * @return
	 */
	boolean isCachable();

	boolean isAjax();

	Throwable getException();

	void release();

	String getTitle();

	<T> T get(String propName, T defaultReturn);

	public void render(Writer out) throws Exception;

}
