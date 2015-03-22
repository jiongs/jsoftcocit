package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.IExtNamedEntity;

public interface IExtCuiForm extends IExtNamedEntity, ICuiForm {

	public void setCocEntityKey(String cocEntityKey);

	public void setCuiEntityKey(String cuiEntityKey);

	public void setFields(String fields);

	public void setBatchFields(String batchFields);

	public void setFieldLabelPos(byte fieldLabelPos);

	public void setStyle(String style);

	public void setStyleClass(String styleClass);

	public void setActions(String actions);

	public void setActionsPos(String pos);
}
