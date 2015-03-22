package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.INamedEntity;

public interface ICuiForm extends INamedEntity {

	public String getCocEntityKey();

	public String getCuiEntityKey();

	public String getFields();

	public String getBatchFields();

	public byte getFieldLabelPos();

	public String getStyle();

	public String getStyleClass();

	public String getActions();

	public String getActionsPos();
}
