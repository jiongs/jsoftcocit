package com.jsoft.cocit.ui.model.control;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.ui.model.UIControlModel;

public class UIFieldGroup extends UIControlModel {

	private List<UIField> hiddenFields;
	private List<UIField> visibleFields;

	public UIFieldGroup() {
		super();

		this.visibleFields = new ArrayList();
		this.hiddenFields = new ArrayList();
	}

	@Override
	public void release() {
		super.release();

		this.hiddenFields.clear();
		this.hiddenFields = null;

		this.visibleFields.clear();
		this.visibleFields = null;

	}

	public UIFieldGroup addField(UIField field) {
		// N：不显示
		if (FieldModes.isN(field.getMode()))
			return this;

		// H：隐藏字段
		if (FieldModes.isH(field.getMode()))
			this.hiddenFields.add(field);
		else
			this.visibleFields.add(field);

		return this;
	}

	public List<UIField> getVisibleFields() {
		return visibleFields;
	}

	public List<UIField> getHiddenFields() {
		return hiddenFields;
	}

}
