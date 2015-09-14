package com.jsoft.cocit.ui.model.control;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author yongshan.ji
 * 
 */
public class UIEntities extends UIControlModel {

	private List<UIPanel> panels;

	private List<String> cols = null;
	private List<String> rows = null;

	public UIEntities() {
		super();
		panels = new ArrayList();
	}

	public void release() {
		super.release();
		for (UIPanel p : panels) {
			p.release();
		}
		if (panels != null) {
			panels.clear();
			panels = null;
		}
	}

	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_MAINS;

		return viewName;
	}

	public List<UIPanel> getPanels() {
		return panels;
	}

	public void addPanel(UIPanel panel) {
		panel.setAjax(true);

		panels.add(panel);
	}

	public List<String> getCols() {
		return cols;
	}

	public void setCols(List<String> cols) {
		this.cols = cols;
	}

	public List<String> getRows() {
		return rows;
	}

	public void setRows(List<String> rows) {
		this.rows = rows;
	}

	public void setPanels(List<UIPanel> panels) {
		this.panels = panels;
	}
}
