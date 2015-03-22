package com.jsoft.cocit.entityengine.service;

import com.jsoft.cocit.entity.cui.ICuiFormField;
import com.jsoft.cocit.util.Option;

public interface CuiFormFieldService extends NamedEntityService<ICuiFormField>, ICuiFormField {
	CuiFormService getCuiForm();

	int getModeValue();

	/**
	 * 获取字典字段的选项数组
	 * 
	 * @return
	 */
	Option[] getDicOptionsArray();

	/**
	 * 根据字段值获取字典选项
	 * 
	 * @param fieldValue
	 *            字段值
	 * @return
	 */
	Option getDicOption(Object fieldValue);
}
