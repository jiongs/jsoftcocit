package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.INamedEntity;

public interface ICuiEntity extends INamedEntity {

	public String getCocEntityKey();

	public String getCols();

	public String getRows();

	public String getFilterFields();

	public byte getFilterFieldsPos();

	public String getFilterFieldsView();

	public String getQueryFields();

	public byte getQueryFieldsPos();

	public String getQueryFieldsView();

	public String getActions();

	public String getActionsView();

	public byte getActionsPos();

	public String getUiView();
}
