package com.jsoft.cocit.baseentity.cui;

import com.jsoft.cocit.baseentity.INamedEntity;

public interface ICuiFormFieldEntity extends INamedEntity {

	public String getCocEntityCode();

	public String getCuiEntityCode();

	public String getCuiFormCode();

	// public String getMode();

	public String getUiView();

	public String getUiViewLinkUrl();

	public String getUiViewLinkTarget();

	public byte getLabelPos();

	public String getStyle();

	public String getStyleClass();

	public String getOneToManyTargetAction();

	public String getFieldName();

	public String getAlign();

	public String getHalign();

	public byte getColspan();

	public byte getRowspan();

	public String getDicOptions();

	public String getDefaultValue();

	public String getFkComboUrl();

	public String getFkComboWhere();
}
