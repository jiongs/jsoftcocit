package com.jsoft.cocit.dmengine.info;

import com.jsoft.cocit.baseentity.cui.ICuiGridFieldEntity;
import com.jsoft.cocit.ui.view.StyleRule;

public interface ICuiGridFieldInfo extends INamedEntityInfo<ICuiGridFieldEntity>, ICuiGridFieldEntity {
	ICuiGridInfo getCuiGrid();

	public StyleRule[] getCellStyles();
}
