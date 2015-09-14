package com.jsoft.cocit.baseentity.cui;

import com.jsoft.cocit.baseentity.INamedEntity;

public interface ICuiFormEntity extends INamedEntity {

	public String getCocEntityCode();

	public String getCuiEntityCode();

	public String getFields();

	public String getBatchFields();

	public String getGroup1Fields();

	public String getGroup2Fields();

	public String getGroup3Fields();

	public String getGroup4Fields();

	public String getGroup5Fields();

	public String getGroup1Name();

	public String getGroup2Name();

	public String getGroup3Name();

	public String getGroup4Name();

	public String getGroup5Name();

	public byte getFieldLabelPos();

	public String getStyle();

	public String getStyleClass();

	public String getActions();

	public String getActionsPos();

	String getUiView();

	String getPrevInterceptors();

	String getPostInterceptors();

}
