package com.jsoft.cocimpl.entityengine.impl.service;

import com.jsoft.cocit.entity.cui.ICuiFormAction;
import com.jsoft.cocit.entityengine.service.CuiFormActionService;
import com.jsoft.cocit.entityengine.service.CuiFormService;

public class CuiFormActionServiceImpl extends NamedEntityServiceImpl<ICuiFormAction> implements CuiFormActionService {

	private CuiFormServiceImpl cuiForm;

	CuiFormActionServiceImpl(ICuiFormAction action, CuiFormServiceImpl cuiForm) {
		super(action);
		this.cuiForm = cuiForm;
	}

	@Override
	public String getCocEntityKey() {
		return entityData.getCocEntityKey();
	}

	@Override
	public String getCuiEntityKey() {
		return entityData.getCuiEntityKey();
	}

	@Override
	public String getCuiFormKey() {
		return entityData.getCuiFormKey();
	}

	@Override
	public String getTitle() {
		return entityData.getTitle();
	}

	@Override
	public CuiFormService getCuiForm() {
		return cuiForm;
	}

}
