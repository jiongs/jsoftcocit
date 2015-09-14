package com.jsoft.cocimpl.ui.view;

import java.util.HashMap;
import java.util.Map;

import org.nutz.log.Logs;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.log.Log;
import com.jsoft.cocit.ui.UIView;
import com.jsoft.cocit.ui.UIViews;
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
	private Log log = Logs.getLog(UIViewsImpl.class);

	private Map<String, UIView> views;

	private Map<String, UIFieldView> fieldViews;
	private Map<String, UICellView> cellViews;

	public UIViewsImpl() {
		views = new HashMap();
		fieldViews = new HashMap();
		cellViews = new HashMap();
	}

	public UIView getView(String viewName) {
		UIView view = null;
		if (viewName == null)
			return null;

		view = views.get(viewName.trim().toLowerCase());

		if (view == null) {
			log.errorf("UIView not be found! viewName = %s", viewName);
		}

		return view;
	}

	public void addView(UIView view) {
		if (view == null || StringUtil.isBlank(view.getName())) {
			throw new CocException("UIViews.addView. Fail！ [viewName: %s, view: %s]", //
			        view == null ? "<NULL>" : view.getName(), //
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
			log.errorf("UIFieldView not be found! viewName = %s", viewName);

			return fieldViews.get(ViewNames.FIELD_VIEW_DEFAULT);
		}

		return view;
	}

	@Override
	public void addFieldView(UIFieldView view) {
		if (view == null || StringUtil.isBlank(view.getName())) {
			throw new CocException("UIViews.addFieldView. Fail！ [viewName: %s, view: %s]", //
			        view == null ? "<NULL>" : view.getName(), //
			        view == null ? "<NULL>" : view.getClass().getName()//
			);
		}

		fieldViews.put(view.getName().trim().toLowerCase(), view);
	}

	@Override
	public UICellView getCellView(String viewName) {
		UICellView ret = null;
		if (viewName != null)
			ret = cellViews.get(viewName.trim().toLowerCase());

		if (ret == null)
			log.errorf("UICellView not be found! viewName = %s", viewName);

		return ret;
	}

	@Override
	public void addCellView(UICellView view) {
		if (view == null || StringUtil.isBlank(view.getName())) {
			throw new CocException("UIViews.addFieldView. Fail！ [viewName: %s, view: %s]", //
			        view == null ? "<NULL>" : view.getName(), //
			        view == null ? "<NULL>" : view.getClass().getName()//
			);
		}

		cellViews.put(view.getName().trim().toLowerCase(), view);
	}
}
