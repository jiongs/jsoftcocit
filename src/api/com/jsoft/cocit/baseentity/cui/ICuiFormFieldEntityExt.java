package com.jsoft.cocit.baseentity.cui;

import com.jsoft.cocit.baseentity.INamedEntityExt;

public interface ICuiFormFieldEntityExt extends INamedEntityExt, ICuiFormFieldEntity {

	public void setCocEntityCode(String cocEntityCode);

	public void setCuiEntityCode(String cuiEntityCode);

	public void setCuiFormCode(String cuiFormCode);

	// public void setMode(String mode);

	public void setUiView(String uiView);

	public void setUiViewLinkUrl(String uiViewFkAction);

	public void setUiViewLinkTarget(String uiViewLinkTarget);

	public void setLabelPos(byte labelPos);

	public void setStyle(String style);

	public void setStyleClass(String styleClass);

	public void setOneToManyTargetAction(String oneToManyTargetAction);

	public void setFieldName(String field);

	public void setAlign(String align);

	public void setHalign(String align);

	public void setColspan(byte colspan);

	public void setRowspan(byte rowspan);

	public void setDicOptions(String dicOptions);

	public void setDefaultValue(String defaultValue);

	public void setFkComboUrl(String fkComboUrl);

	public void setFkComboWhere(String fkComboWhere);
}
