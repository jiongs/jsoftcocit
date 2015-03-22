package com.jsoft.cocit.entityengine.service;

import java.util.List;

import com.jsoft.cocit.entity.cui.ICuiForm;

public interface CuiFormService extends NamedEntityService<ICuiForm>, ICuiForm {

	CuiEntityService getCuiEntity();

	List<List<String>> getFieldsList();

	CuiFormFieldService getFormField(String fieldName);

	List<String> getBatchFieldsList();

	List<String> getActionsList();

	CuiFormActionService getFormAction(String action);

}
