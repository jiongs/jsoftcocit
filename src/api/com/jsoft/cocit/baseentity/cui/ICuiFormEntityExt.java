package com.jsoft.cocit.baseentity.cui;

import com.jsoft.cocit.baseentity.INamedEntityExt;

public interface ICuiFormEntityExt extends INamedEntityExt, ICuiFormEntity {

	public void setCocEntityCode(String cocEntityCode);

	public void setCuiEntityCode(String cuiEntityCode);

	public void setFields(String fields);

	public void setGroup1Fields(String fields);

	public void setGroup2Fields(String fields);

	public void setGroup3Fields(String fields);

	public void setGroup4Fields(String fields);

	public void setGroup5Fields(String fields);

	public void setGroup1Name(String name);

	public void setGroup2Name(String name);

	public void setGroup3Name(String name);

	public void setGroup4Name(String name);

	public void setGroup5Name(String name);

	public void setBatchFields(String batchFields);

	public void setFieldLabelPos(byte fieldLabelPos);

	public void setStyle(String style);

	public void setStyleClass(String styleClass);

	public void setActions(String actions);

	public void setActionsPos(String pos);

	void setUiView(String uiView);

	void setPrevInterceptors(String prevIntecerptors);

	void setPostInterceptors(String postInterceptors);

}
