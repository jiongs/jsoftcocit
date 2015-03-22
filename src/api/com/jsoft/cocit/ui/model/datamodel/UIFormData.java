package com.jsoft.cocit.ui.model.datamodel;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIDataModel;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.util.StringUtil;

/**
 * 表单数据模型
 * 
 * @author yongshan.ji
 * 
 */
public class UIFormData extends UIDataModel<UIForm> {
	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_FORMDATA;

		return viewName;
	}
}
