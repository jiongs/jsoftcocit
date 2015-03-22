package com.jsoft.cocit.ui.model.control;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.UISearchBoxModel;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

public class UISearchBox extends UIControlModel implements UISearchBoxModel {
	private List<Option> data;
	private List<UIFieldModel> fields;

	public UISearchBox() {
		this.fields = new ArrayList();
	}

	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_SEARCHFORM;

		return viewName;
	}

	public void release() {
		super.release();
		if (data != null) {
			data.clear();
			data = null;
		}
	}

	public List<Option> getData() {
		return data;
	}

	public UISearchBox setData(List<Option> data) {
		this.data = data;

		return this;
	}

	public void addField(UIFieldModel field) {
		fields.add(field);
	}

	public List<UIFieldModel> getFields() {
		return fields;
	}

}
