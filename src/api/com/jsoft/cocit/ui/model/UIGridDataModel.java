package com.jsoft.cocit.ui.model;

public interface UIGridDataModel {

	UIGridModel getModel();

	void setData(Object data);

	void setException(Throwable e);

	void setTotal(int total);

	void setViewName(String viewName);

	void setModel(UIGridModel grid);

}
