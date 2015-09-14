package com.jsoft.cocit.ui.model.control;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.ui.model.UIControlModel;

/**
 * 
 * @author yongshan.ji
 * 
 */
public class UIPanels extends UIControlModel {

	private List<UIPanel> panels;

	public static UIPanels make() {
		return new UIPanels();
	}

	private UIPanels() {
		this.panels = new ArrayList();
	}

	/**
	 *
	 */
	public void addPanel(UIPanel tab) {
		this.panels.add(tab);
	}

	public List<UIPanel> getPanels() {
		return panels;
	}

	public void setPanels(List<UIPanel> panels) {
		this.panels = panels;
	}
}
