package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.IExtNamedEntity;

public interface IExtCuiGridField extends IExtNamedEntity, ICuiGridField {

	public void setCocEntityKey(String cocEntityKey);

	public void setCuiEntityKey(String cuiEntityKey);

	public void setCuiGridKey(String cuiGridKey);

	public void setFieldName(String fieldName);

	public void setWidth(int width);

	public void setAlign(String align);

	public void setCellView(String cellView);

	public void setCellViewLinkUrl(String cellViewFkAction);

	public void setCellViewLinkTarget(String cellViewLinkTarget);

	public void setCellStyleRule(String cellStyle);

	public void setHidden(boolean hidden);

	public void setShowCellTips(boolean cellTips);

	public void setHalign(String halign);

}
