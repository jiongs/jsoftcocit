package com.jsoft.cocit.entity.cui;

import com.jsoft.cocit.entity.INamedEntity;

public interface ICuiGridField extends INamedEntity {

	public String getCocEntityKey();

	public String getCuiEntityKey();

	public String getCuiGridKey();

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
