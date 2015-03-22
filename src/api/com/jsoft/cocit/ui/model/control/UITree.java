package com.jsoft.cocit.ui.model.control;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.ui.model.UITreeModel;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;

/**
 * 树窗体界面模型：可以包含树所需要的数据，如果数据不存在则表示将异步获取JSON格式的树型数据。
 * 
 * <B>属性设置：</B>
 * <UL>
 * <LI>checkbox: bool值，true——支持checkbox多选框；falst——不支持checkbox多选框
 * </UL>
 * 
 * @author jiongsoft
 * 
 */
public class UITree extends UIControlModel implements UITreeModel{

	// Tree数据，如果该值为Null，则将通过AJAX方式加载树数据。
	private Tree data;

	// Grid数据“增、删、查、改”操作的URL地址
	private String dataLoadUrl;

	private String dataDeleteUrl;

	private String dataEditUrl;

	private String dataAddUrl;

	public void release() {
		super.release();
		if (data != null) {
			data.release();
			data = null;
		}
		this.dataAddUrl = null;
		this.dataDeleteUrl = null;
		this.dataEditUrl = null;
		this.dataLoadUrl = null;
	}

	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_TREE;

		return viewName;
	}

	public String getDataLoadUrl() {
		return dataLoadUrl;
	}

	public void setDataLoadUrl(String dataLoadUrl) {
		this.dataLoadUrl = dataLoadUrl;
	}

	public String getDataDeleteUrl() {
		return dataDeleteUrl;
	}

	public void setDataDeleteUrl(String dataDeleteUrl) {
		this.dataDeleteUrl = dataDeleteUrl;
	}

	public String getDataEditUrl() {
		return dataEditUrl;
	}

	public void setDataEditUrl(String dataEditUrl) {
		this.dataEditUrl = dataEditUrl;
	}

	public String getDataAddUrl() {
		return dataAddUrl;
	}

	public void setDataAddUrl(String dataUpdateUrl) {
		this.dataAddUrl = dataUpdateUrl;
	}

	public Tree getData() {
		return data;
	}

	public void setData(Tree data) {
		this.data = data;
	}

}
