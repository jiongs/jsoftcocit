package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.IExtNamedEntity;

public interface IExtCuiFormAction extends IExtNamedEntity, ICuiFormAction {
	public void setCocEntityKey(String cocEntityKey);

	public void setCuiEntityKey(String cuiEntityKey);

	public void setCuiFormKey(String cuiFormKey);

	public void setTitle(String title);
}
