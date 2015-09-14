package com.jsoft.cocit.baseentity.cui;

import com.jsoft.cocit.baseentity.INamedEntity;

public interface ICuiFormAction extends INamedEntity {

	public String getCocEntityCode();

	public String getCuiEntityCode();

	public String getCuiFormCode();

	public String getTitle();

}
