package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.IExtDataEntity;

public interface IExtCuiGrid extends IExtDataEntity, ICuiGrid {

	public void setCocEntityKey(String cocEntityKey);

	public void setCuiEntityKey(String cuiEntityKey);

	public void setFrozenFields(String frozenFields);

	public void setFields(String fields);

	public void setRownumbers(boolean rownumbers);

	public void setMultiSelect(boolean multiSelect);

	public void setSelectOnCheck(boolean selectOnCheck);

	public void setCheckOnSelect(boolean checkOnSelect);

	public void setPaginationPos(byte paginationPos);

	public void setPaginationActions(String paginationActions);

	public void setPageIndex(int pageIndex);

	public void setPageSize(int pageSize);

	public void setPageOptions(String pageOptions);

	public void setSortExpr(String sortExpr);

	public void setShowHeader(boolean showHeader);

	public void setShowFooter(boolean showFooter);

	public void setRowActions(String rowActions);

	public void setRowActionsView(String rowActionsView);

	public void setRowActionsPos(byte rowActionsPos);

	public void setTreeField(String treeField);

	public void setRowStyleRule(String rowStyle);

	public void setUiView(String uiView);

	public void setSingleRowEdit(boolean singleRowEdit);
}
