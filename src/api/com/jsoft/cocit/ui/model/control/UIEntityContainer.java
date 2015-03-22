package com.jsoft.cocit.ui.model.control;

import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.ui.model.UIModel;

/**
 * 
 * @author yongshan.ji
 * 
 */
public class UIEntityContainer extends UIControlModel {

	private String panelUrl;
	private UIModel panelContent;

	public static UIEntityContainer make(UIModel panelContent) {
		return new UIEntityContainer(panelContent);
	}

	private UIEntityContainer(UIModel panelContent) {
		this.panelContent = panelContent;
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

	public UIEntityContainer setPanelUrl(String tabUrl) {
		this.panelUrl = tabUrl;

		return this;
	}

	public UIModel getPanelContent() {
		return panelContent;
	}

	public UIEntityContainer setPanelContent(UIModel tabContent) {
		this.panelContent = tabContent;

		return this;
	}

}
