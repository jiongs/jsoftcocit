package com.jsoft.cocimpl.ui;

import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.ui.view.UIFieldView;

/**
 * {@link UIView}工厂：用于创建并管理{@link UIView}对象。
 * 
 * @author yongshan.ji
 * 
 */
public interface UIViews {

	UIView getView(String viewName);

	void addView(UIView view);

	UIFieldView getFieldView(String viewName);

	void addFieldView(UIFieldView view);

	UICellView getCellView(String viewName);

	void addCellView(UICellView view);
}
