package com.jsoft.cocit.ui.model.control;

import java.util.List;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.util.StringUtil;

/**
 * @author yongshan.ji
 * 
 */
public class UIList extends UIControlModel {

	// List数据，如果该值为Null，则将通过AJAX方式加载List数据。
	private List data;

	// List数据“增、删、查、改”操作的URL地址
	private String dataLoadUrl;

	// List列
	private List<UIField> columns;

	public UIList() {
		super();
	}

	public void release() {
		super.release();
		if (data != null) {
			data.clear();
			data = null;
		}
		this.dataLoadUrl = null;
		if (columns != null) {
			columns.clear();
			columns = null;
		}
	}

	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_LIST;

		return viewName;
	}

	public UIList addColumn(UIField col) {
		columns.add(col);
		return this;
	}

	public String getDataLoadUrl() {
		return dataLoadUrl;
	}

	public UIList setDataLoadUrl(String dataLoadUrl) {
		this.dataLoadUrl = dataLoadUrl;
		return this;
	}

	public List getData() {
		return data;
	}

	public UIList setData(List data) {
		this.data = data;
		return this;
	}

	public List<UIField> getColumns() {
		return columns;
	}

	public void setColumns(List<UIField> columns) {
		this.columns = columns;
	}

}
