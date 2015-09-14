package com.jsoft.cocit.constant;

public interface CommandNames {

	String	COC_SAVE		= "coc.save";
	String	COC_DELETE	= "coc.delete";
	String	COC_QUERY	= "coc.query";
	String	COC_GET		= "coc.get";

	String COC_SET_PROXY_ACTION = "coc.setProxyAction";

	String	COC_COMBOGRID	= "coc.combogrid";
	String	COC_COMBOLIST	= "coc.combolist";
	String	COC_COMBOTREE	= "coc.combotree";
	String	COC_FILTERTREE	= "coc.filtertree";

	String	WF_START		= "wf.start";
	String	WF_RESTART	= "wf.restart";
	String	WF_DO		= "wf.do";
	String	WF_DELETE	= "wf.delete";
	String	PD_DEPLOY	= "pd.deploy";
	String	PD_DELETE	= "pd.delete";

	String FM_INIT = "fm.init";

	/**
	 * 拦截器来源：@CocGrid
	 */
	byte INTERCEPTOR_TYPE_GRID = 1;

	/**
	 * 拦截器来源：@CocForm
	 */
	byte INTERCEPTOR_TYPE_FORM = 2;

	/**
	 * 拦截器来源：@CocColumn
	 */
	byte INTERCEPTOR_TYPE_COLUMN = 3;

}
