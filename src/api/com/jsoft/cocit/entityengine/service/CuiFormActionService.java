package com.jsoft.cocit.entityengine.service;

import com.jsoft.cocit.entity.cui.ICuiFormAction;

public interface CuiFormActionService extends NamedEntityService<ICuiFormAction>, ICuiFormAction {
	CuiFormService getCuiForm();

}
