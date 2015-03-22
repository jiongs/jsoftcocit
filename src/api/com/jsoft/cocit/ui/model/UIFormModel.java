package com.jsoft.cocit.ui.model;

import java.util.List;

public interface UIFormModel {

	public void release();

	public List<UIFieldModel> getFieldList();

	public String getSubmitUrl();

	public Object getDataObject();

	public void setSubmitUrl(String submitUrl);

	public void setDataObject(Object dataObject);
}
