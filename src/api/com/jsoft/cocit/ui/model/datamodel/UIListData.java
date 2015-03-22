package com.jsoft.cocit.ui.model.datamodel;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIDataModel;
import com.jsoft.cocit.ui.model.control.UIList;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author jiongsoft
 * 
 */
public class UIListData extends UIDataModel<UIList> {

	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_LISTDATA;

		return viewName;
	}
}
