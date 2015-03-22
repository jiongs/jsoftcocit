package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.INamedEntity;

public interface ICuiFormField extends INamedEntity {

	public String getCocEntityKey();

	public String getCuiEntityKey();

	public String getCuiFormKey();

	public String getMode();

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

	public String getFkComboUrl();

	public String getFkComboWhere();
}
