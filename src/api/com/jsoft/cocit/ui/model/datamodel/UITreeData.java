package com.jsoft.cocit.ui.model.datamodel;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIDataModel;
import com.jsoft.cocit.ui.model.control.UITree;
import com.jsoft.cocit.util.StringUtil;

/**
 * 树数据模型：
 * 
 * @author jiongsoft
 * 
 */
public class UITreeData extends UIDataModel<UITree> {

	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_TREEDATA;

		return viewName;
	}
}
