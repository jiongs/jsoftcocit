package com.jsoft.cocit.baseentity.cui;

import com.jsoft.cocit.baseentity.INamedEntityExt;

public interface ICuiFormActionExt extends INamedEntityExt, ICuiFormAction {
	public void setCocEntityCode(String cocEntityCode);

	public void setCuiEntityCode(String cuiEntityCode);

	public void setCuiFormCode(String cuiFormCode);

	public void setTitle(String title);
}
