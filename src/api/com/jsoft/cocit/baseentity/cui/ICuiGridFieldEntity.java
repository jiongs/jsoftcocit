package com.jsoft.cocit.baseentity.cui;

import com.jsoft.cocit.baseentity.INamedEntity;

public interface ICuiGridFieldEntity extends INamedEntity {

	public String getCocEntityCode();

	public String getCuiEntityCode();

	public String getCuiGridCode();

	public String getFieldName();

	public int getWidth();

	public String getAlign();

	public String getHalign();

	public String getCellView();

	public String getCellViewLinkUrl();

	public String getCellViewLinkTarget();

	public String getCellStyleRule();

	public boolean isHidden();

	public boolean isShowCellTips();
}
