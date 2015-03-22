package com.jsoft.cocit.entityengine.service;

import com.jsoft.cocit.entity.cui.ICuiGridField;
import com.jsoft.cocit.ui.view.StyleRule;

public interface CuiGridFieldService extends NamedEntityService<ICuiGridField>, ICuiGridField {
	CuiGridService getCuiGrid();

	public StyleRule[] getCellStyles();
}
