package com.jsoft.cocit.dmengine.info;

import java.util.List;

import com.jsoft.cocit.baseentity.cui.ICuiGridEntity;
import com.jsoft.cocit.ui.view.StyleRule;

public interface ICuiGridInfo extends IDataEntityInfo<ICuiGridEntity>, ICuiGridEntity {
	ICuiEntityInfo getCuiEntity();

	ICuiGridFieldInfo getField(String fieldName);

	List<String> getFrozenFieldsList();

	List<String> getFieldsList();

	StyleRule[] getRowStyles();

	// String getSQL();
	//
	// List<String> getSQLParams();
}
