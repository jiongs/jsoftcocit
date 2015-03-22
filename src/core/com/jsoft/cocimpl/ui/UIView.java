package com.jsoft.cocimpl.ui;

import java.io.Writer;

import com.jsoft.cocit.ui.model.UIModel;

/**
 * UI视图： 用于输出指定类型的{@link UIModel}到浏览器。
 * 
 * @author jiongs753
 * 
 */
public interface UIView<T extends UIModel> {

	String getName();

	/**
	 * 输出窗体界面
	 * 
	 * @param out
	 * @param model
	 * @throws Exception
	 * @throws Throwable
	 */
	void render(Writer out, T model) throws Exception;

}
