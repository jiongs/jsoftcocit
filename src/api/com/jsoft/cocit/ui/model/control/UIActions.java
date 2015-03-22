package com.jsoft.cocit.ui.model.control;

import com.jsoft.cocit.ui.model.UIActionsModel;
import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

/**
 * 菜单窗体界面模型：包括菜单属性和菜单所需要的数据。
 * 
 * @author yongshan.ji
 * 
 */
public class UIActions extends UIControlModel implements UIActionsModel {

	// private UISearchBox searchBox;

	private Tree data;

	public void release() {
		super.release();
		if (data != null) {
			data.release();
			data = null;
		}
		// if (searchBox != null) {
		// searchBox.release();
		// this.searchBox = null;
		// }
	}

	public Tree getData() {
		return data;
	}

	public UIActions setData(Tree menu) {
		this.data = menu;

		return this;
	}

	public Node findAction(String key, String val) {
		if (data == null)
			return null;

		for (Node node : data.getAll()) {
			String str = (String) node.get(key);
			if (StringUtil.hasContent(str) && str.equals(val)) {
				return node;
			}
		}

		return null;
	}

	// public UISearchBox getSearchBox() {
	// return searchBox;
	// }
	//
	// public UIActions setSearchBox(UISearchBox searchBox) {
	// this.searchBox = searchBox;
	//
	// return this;
	// }

}
