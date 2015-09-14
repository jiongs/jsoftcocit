package com.jsoft.cocit.dmengine.info;

import com.jsoft.cocit.baseentity.cui.ICuiFormAction;

public interface ICuiFormActionInfo extends INamedEntityInfo<ICuiFormAction>, ICuiFormAction {
	ICuiFormInfo getCuiForm();

}
