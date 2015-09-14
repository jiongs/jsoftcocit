package com.jsoft.cocit.dmengine.info;

import com.jsoft.cocit.baseentity.cui.ICuiFormFieldEntity;
import com.jsoft.cocit.util.Option;

public interface ICuiFormFieldInfo extends INamedEntityInfo<ICuiFormFieldEntity>, ICuiFormFieldEntity {
	ICuiFormInfo getCuiForm();

	// int getModeValue();

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
