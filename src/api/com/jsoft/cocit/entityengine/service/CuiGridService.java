package com.jsoft.cocit.entityengine.service;

import java.util.List;

import com.jsoft.cocit.entity.cui.ICuiGrid;
import com.jsoft.cocit.ui.view.StyleRule;

public interface CuiGridService extends DataEntityService<ICuiGrid>, ICuiGrid {
	CuiEntityService getCuiEntity();

	CuiGridFieldService getField(String fieldName);

	List<String> getFrozenFieldsList();

	List<String> getFieldsList();

	public StyleRule[] getRowStyles();
}
