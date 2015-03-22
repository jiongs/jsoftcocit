package com.jsoft.cocit.ui.model.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.UIGridModel;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.util.StringUtil;

/**
 * 数据表Grid窗体界面模型：由多个Grid列和数据组成，如果数据不存在则表示将异步获取Grid数据。
 * 
 * <b>属性说明：</b>
 * <UL>
 * <LI>rownumbers: bool值，是否在Grid中显示行号？
 * <LI>checkbox: bool值，是否在Grid的第一列显示复选框？
 * <LI>singleSelect: bool值，表示Grid是否只能单选？
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class UIGrid extends UIControlModel implements UIGridModel {

	private String name;
	// Grid数据，如果该值为Null，则将通过AJAX方式加载Grid数据。
	private List data;
	private int pageSize = Const.DEFAULT_PAGE_SIZE;
	// Grid数据“增、删、查、改”操作的URL地址
	private String dataLoadUrl;
	private String dataDeleteUrl;
	private String dataEditUrl;
	private String dataAddUrl;

	// Grid列
	private List<UIFieldModel> columns;
	private Map<String, UIFieldModel> columnMap;
	private int columnsTotalWidth;

	// 行操作
	private UIActions rowActions;
	private byte rowActionsPos;
	private StyleRule[] rowStyles;
	private String sortExpr;
	private String treeField;
	
	private String entityKey;

	public UIGrid() {
		super();
		columns = new ArrayList();
		columnMap = new HashMap();
	}

	public void release() {
		super.release();
		this.name = null;
		if (data != null) {
			data.clear();
			data = null;
		}
		pageSize = Const.DEFAULT_PAGE_SIZE;
		this.dataLoadUrl = null;
		this.dataDeleteUrl = null;
		this.dataEditUrl = null;
		this.dataAddUrl = null;
		columnsTotalWidth = 0;
		// nestedMenu = false;
		if (columns != null) {
			columns.clear();
			columns = null;
		}
	}

	public String getViewName() {
		if (StringUtil.isBlank(viewName)) {
			if (StringUtil.hasContent(treeField)) {
				return ViewNames.VIEW_TREEGRID;
			}
			return ViewNames.VIEW_GRID;
		}

		return viewName;
	}

	public UIGrid addColumn(UIFieldModel col) {
		columns.add(col);
		columnMap.put(col.getPropName(), col);

		return this;
	}

	public UIFieldModel getColumn(String field) {
		return columnMap.get(field);
	}

	public String getDataLoadUrl() {
		return dataLoadUrl;
	}

	public UIGrid setDataLoadUrl(String dataLoadUrl) {
		this.dataLoadUrl = dataLoadUrl;

		return this;
	}

	public String getDataDeleteUrl() {
		return dataDeleteUrl;
	}

	public UIGrid setDataDeleteUrl(String dataDeleteUrl) {
		this.dataDeleteUrl = dataDeleteUrl;

		return this;
	}

	public String getDataEditUrl() {
		return dataEditUrl;
	}

	public UIGrid setDataEditUrl(String dataEditUrl) {
		this.dataEditUrl = dataEditUrl;

		return this;
	}

	public String getDataAddUrl() {
		return dataAddUrl;
	}

	public UIGrid setDataAddUrl(String dataUpdateUrl) {
		this.dataAddUrl = dataUpdateUrl;

		return this;
	}

	public List getData() {
		return data;
	}

	public UIGrid setData(List data) {
		this.data = data;

		return this;
	}

	public List<UIFieldModel> getColumns() {
		return columns;
	}

	public UIGrid setColumns(List<UIFieldModel> columns) {
		this.columns = columns;

		return this;
	}

	public String getName() {
		return name;
	}

	public UIGrid setName(String name) {
		this.name = name;

		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public UIGrid setPageSize(int pageSize) {
		this.pageSize = pageSize;

		return this;
	}

	public int getColumnsTotalWidth() {
		return columnsTotalWidth;
	}

	public UIGrid setColumnsTotalWidth(int columnsTotalWidth) {
		this.columnsTotalWidth = columnsTotalWidth;

		return this;
	}

	//
	// public boolean isNestedMenu() {
	// return nestedMenu;
	// }
	//
	// public UIGrid setNestedMenu(boolean nestedMenu) {
	// this.nestedMenu = nestedMenu;
	//
	// return this;
	// }

	public UIActions getRowActions() {
		return rowActions;
	}

	public void setRowActions(UIActions rowActions) {
		this.rowActions = rowActions;
	}

	public void setRowActionsPos(byte rowActionsPos) {
		this.rowActionsPos = rowActionsPos;
	}

	public byte getRowActionsPos() {
		return rowActionsPos;
	}

	public StyleRule[] getRowStyles() {
		return rowStyles;
	}

	public void setRowStyles(StyleRule[] styleRules) {
		this.rowStyles = styleRules;
	}

	public void setSortExpr(String sortExpr) {
		this.sortExpr = sortExpr;
	}

	public String getSortExpr() {
		return sortExpr;
	}

	public void setTreeField(String treeField) {
		this.treeField = treeField;
	}

	public String getTreeField() {
		return treeField;
	}

	public String getEntityKey() {
    	return entityKey;
    }

	public void setEntityKey(String entityKey) {
    	this.entityKey = entityKey;
    }

}
