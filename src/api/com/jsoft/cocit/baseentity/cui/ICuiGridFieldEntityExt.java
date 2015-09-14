package com.jsoft.cocit.baseentity.cui;

import com.jsoft.cocit.baseentity.INamedEntityExt;

public interface ICuiGridFieldEntityExt extends INamedEntityExt, ICuiGridFieldEntity {

	public void setCocEntityCode(String cocEntityCode);

	public void setCuiEntityCode(String cuiEntityCode);

	public void setCuiGridCode(String cuiGridCode);

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
