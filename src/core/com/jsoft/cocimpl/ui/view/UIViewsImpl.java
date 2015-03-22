package com.jsoft.cocimpl.ui.view;

import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocimpl.ui.UIView;
import com.jsoft.cocimpl.ui.UIViews;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.ui.view.UIFieldView;
import com.jsoft.cocit.util.StringUtil;

/**
 * 输出支持jCocit.js jQuery框架的HTML/Json/XML。
 * 
 * @author yongshan.ji
 * @preserve
 * 
 */
public class UIViewsImpl implements UIViews {
	private Map<String, UIView> views;

	private Map<String, UIFieldView> fieldViews;
	private Map<String, UICellView> cellViews;

	public UIViewsImpl() {
		views = new HashMap();
		fieldViews = new HashMap();
		cellViews = new HashMap();
	}

	public UIView getView(String viewName) {
		if (viewName == null)
			return null;

		return views.get(viewName.trim().toLowerCase());
	}

	public void addView(UIView view) {
		if (view == null || StringUtil.isBlank(view.getName())) {
			throw new CocException("UIViews.addView. Fail！ [viewName: %s, view: %s]",//
			        view == null ? "<NULL>" : view.getName(),//
			        view == null ? "<NULL>" : view.getClass().getName()//
			);
		}

		views.put(view.getName(), view);
	}

	@Override
	public UIFieldView getFieldView(String viewName) {
		UIFieldView view = null;

		if (viewName != null)
			view = fieldViews.get(viewName.trim().toLowerCase());

		if (view == null) {
			return fieldViews.get(ViewNames.FIELD_VIEW_DEFAULT);
		}

		return view;
	}

	@Override
	public void addFieldView(UIFieldView view) {
		if (view == null || StringUtil.isBlank(view.getName())) {
			throw new CocException("UIViews.addFieldView. Fail！ [viewName: %s, view: %s]",//
			        view == null ? "<NULL>" : view.getName(),//
			        view == null ? "<NULL>" : view.getClass().getName()//
			);
		}

		fieldViews.put(view.getName().trim().toLowerCase(), view);
	}

	@Override
	public UICellView getCellView(String viewName) {
		if (viewName != null)
			return cellViews.get(viewName.trim().toLowerCase());

		return null;
	}

	@Override
	public void addCellView(UICellView view) {
		if (view == null || StringUtil.isBlank(view.getName())) {
			throw new CocException("UIViews.addFieldView. Fail！ [viewName: %s, view: %s]",//
			        view == null ? "<NULL>" : view.getName(),//
			        view == null ? "<NULL>" : view.getClass().getName()//
			);
		}

		cellViews.put(view.getName().trim().toLowerCase(), view);
	}
}
