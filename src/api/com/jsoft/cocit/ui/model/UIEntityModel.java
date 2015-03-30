package com.jsoft.cocit.ui.model;

import java.io.Writer;

public interface UIEntityModel {
	public UITreeModel getFilter();

	public UIActionsModel getActions();

	public UISearchBoxModel getSearchBox();

	public UIGridModel getGrid();

	public void render(Writer out) throws Exception;
}
