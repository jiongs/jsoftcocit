package com.jsoft.cocit.ui.model.control;

import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.ui.model.UIModel;

/**
 * 
 * @author yongshan.ji
 * 
 */
public class UIPanel extends UIControlModel {

	private String panelUrl;
	private UIModel panelContent;

	public static UIPanel make(UIModel panelContent) {
		return new UIPanel(panelContent);
	}

	public static UIPanel make(String panelUrl) {
		return new UIPanel(panelUrl);
	}

	private UIPanel(UIModel panelContent) {
		this.panelContent = panelContent;
	}

	private UIPanel(String panelUrl) {
		this.panelUrl = panelUrl;
	}

	public void release() {
		super.release();
		this.panelUrl = null;
		if (panelContent != null) {
			panelContent.release();
			panelContent = null;
		}
	}

	public String getPanelUrl() {
		return panelUrl;
	}

	public UIPanel setPanelUrl(String tabUrl) {
		this.panelUrl = tabUrl;

		return this;
	}

	public UIModel getPanelContent() {
		return panelContent;
	}

	public UIPanel setPanelContent(UIModel tabContent) {
		this.panelContent = tabContent;

		return this;
	}

}
