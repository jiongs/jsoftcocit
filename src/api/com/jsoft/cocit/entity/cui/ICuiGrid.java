package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.IDataEntity;

public interface ICuiGrid extends IDataEntity {

	public String getCocEntityKey();

	public String getCuiEntityKey();

	public String getFrozenFields();

	public String getFields();

	public boolean isRownumbers();

	public boolean isMultiSelect();

	public boolean isSelectOnCheck();

	public boolean isCheckOnSelect();

	public byte getPaginationPos();

	public String getPaginationActions();

	public int getPageIndex();

	public int getPageSize();

	public String getPageOptions();

	public String getSortExpr();

	public boolean isShowHeader();

	public boolean isShowFooter();

	public String getRowActions();

	public String getRowActionsView();

	public byte getRowActionsPos();

	public String getTreeField();

	public String getRowStyleRule();

	public String getUiView();
}
