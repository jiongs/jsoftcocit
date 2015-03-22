package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.INamedEntity;

public interface ICuiFormAction extends INamedEntity {

	public String getCocEntityKey();

	public String getCuiEntityKey();

	public String getCuiFormKey();

	public String getTitle();

}
