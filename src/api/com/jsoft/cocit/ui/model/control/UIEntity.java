package com.jsoft.cocit.ui.model.control;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.ui.model.UIEntityModel;
import com.jsoft.cocit.util.StringUtil;

/**
 * 实体管理UI(扩展的Grid模型)：由“左边过滤树、顶部操作菜单、顶部查询框、Grid”等组成。
 * 
 * @author yongshan.ji
 * 
 */
public class UIEntity extends UIControlModel implements UIEntityModel {

	private UITree filter;// 数据过滤器
	private UIGrid grid;// Grid
	private UIActions actions;// 操作菜单
	private byte actionsPos;
	private UISearchBox searchBox;// 检索框
	private byte searchBoxPos;

	public void release() {
		super.release();
		if (filter != null) {
			this.filter.release();
			this.filter = null;
		}
		if (actions != null) {
			this.actions.release();
			this.actions = null;
		}
		if (searchBox != null) {
			this.searchBox.release();
			this.searchBox = null;
		}
		if (grid != null) {
			this.grid.release();
			this.grid = null;
		}
	}

	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_MAIN;

		return viewName;
	}

	public UIEntity setGrid(UIGrid grid) {
		this.grid = grid;

		return this;
	}

	public UITree getFilter() {
		return filter;
	}

	public UIEntity setFilter(UITree filter) {
		this.filter = filter;

		return this;
	}

	public UIActions getActions() {
		return actions;
	}

	public UIEntity setActions(UIActions as) {
		this.actions = as;

		return this;
	}

	public UISearchBox getSearchBox() {
		return searchBox;
	}

	public UIEntity setSearchBox(UISearchBox searchBox) {
		this.searchBox = searchBox;

		return this;
	}

	public UIGrid getGrid() {
		return grid;
	}

	public void setActionsPos(byte actionsPos) {
		this.actionsPos = actionsPos;
	}

	public byte getActionsPos() {
		return actionsPos;
	}

	public byte getSearchBoxPos() {
		return searchBoxPos;
	}

	public void setSearchBoxPos(byte searchBoxPos) {
		this.searchBoxPos = searchBoxPos;
	}

}
