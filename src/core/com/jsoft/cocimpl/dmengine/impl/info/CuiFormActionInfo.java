package com.jsoft.cocimpl.dmengine.impl.info;

import com.jsoft.cocit.baseentity.cui.ICuiFormAction;
import com.jsoft.cocit.dmengine.info.ICuiFormActionInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormInfo;

public class CuiFormActionInfo extends NamedEntityInfo<ICuiFormAction> implements ICuiFormActionInfo {

	private CuiFormInfo cuiForm;

	CuiFormActionInfo(ICuiFormAction action, CuiFormInfo cuiForm) {
		super(action);
		this.cuiForm = cuiForm;
	}

	@Override
	public String getCocEntityCode() {
		return entityData.getCocEntityCode();
	}

	@Override
	public String getCuiEntityCode() {
		return entityData.getCuiEntityCode();
	}

	@Override
	public String getCuiFormCode() {
		return entityData.getCuiFormCode();
	}

	@Override
	public String getTitle() {
		return entityData.getTitle();
	}

	@Override
	public ICuiFormInfo getCuiForm() {
		return cuiForm;
	}

}
