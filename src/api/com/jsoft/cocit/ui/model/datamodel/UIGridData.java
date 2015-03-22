package com.jsoft.cocit.ui.model.datamodel;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIDataModel;
import com.jsoft.cocit.ui.model.UIGridDataModel;
import com.jsoft.cocit.ui.model.UIGridModel;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.util.StringUtil;

/**
 * Grid数据模型：由Grid界面模型和Grid数据组成。
 * 
 * @author jiongsoft
 * 
 */
public class UIGridData extends UIDataModel<UIGrid> implements UIGridDataModel {
	private int total;

	@Override
	public void release() {
		super.release();
		this.total = 0;
	}

	public String getViewName() {
		if (StringUtil.isBlank(viewName)) {
			String view = this.getModel().getViewName();
			if (StringUtil.hasContent(view)) {
				return view + "data";
			}
			if (StringUtil.hasContent(this.getModel().getTreeField())) {
				return ViewNames.VIEW_TREEGRIDDATA;
			}
			return ViewNames.VIEW_GRIDDATA;
		}

		return viewName;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public void setModel(UIGridModel grid) {
		super.setModel((UIGrid) grid);
	}

}
