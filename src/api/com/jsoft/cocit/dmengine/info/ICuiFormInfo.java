package com.jsoft.cocit.dmengine.info;

import java.util.List;
import java.util.Map;

import com.jsoft.cocit.baseentity.cui.ICuiFormEntity;

public interface ICuiFormInfo extends INamedEntityInfo<ICuiFormEntity>, ICuiFormEntity {

	ICuiEntityInfo getCuiEntity();

	List<List<String>> getFieldsList();

	List<List<String>> getGroup1FieldsList();

	List<List<String>> getGroup2FieldsList();

	List<List<String>> getGroup3FieldsList();

	List<List<String>> getGroup4FieldsList();

	List<List<String>> getGroup5FieldsList();

	ICuiFormFieldInfo getFormField(String fieldName);

	List<String> getBatchFieldsList();

	List<String> getActionsList();

	Map<String, Integer> getFieldModes();

	ICuiFormActionInfo getFormAction(String action);

}
