package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.IExtNamedEntity;

public interface IExtCuiEntity extends IExtNamedEntity, ICuiEntity {

	public void setCocEntityKey(String cocEntityKey);

	public void setRows(String rows);

	public void setCols(String cols);

	public void setFilterFields(String filterFields);
	
	public void setFilterFieldsPos(byte filterFieldsPos);

	public void setFilterFieldsView(String filterFieldsView);

	public void setQueryFields(String queryFields);

	public void setQueryFieldsPos(byte queryFieldsPos);

	public void setQueryFieldsView(String queryFieldsView);

	public void setActions(String actions);

	public void setActionsView(String actionsView);

	public void setActionsPos(byte actionsPos);

	public void setUiView(String uiView);
}
