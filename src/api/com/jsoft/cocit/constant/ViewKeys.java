package com.jsoft.cocit.constant;

import java.io.Serializable;

import com.jsoft.cocit.ui.model.UIModel;

public class ViewKeys implements Serializable {
	private static final long serialVersionUID = -638936628403154868L;

	/*
	 * ------------------------- 已整理
	 */

	/**
	 * 用于存储 {@link UIModel} 对象。 <BR>
	 * 在JSP中可以通过如下代码获取到UIModel对象， UIModel uiModel = request.getAttribute(ViewKeys.UI_MODEL_KEY); <BR>
	 * 在ST中可以通过如下变量获取到UIModel对象，{$uiModel}
	 */
	public static final String UI_MODEL_KEY = "uiModel";

	public static final String BEAN_KEY = "entity";

	public static final String TAG_FORM_KEY = "tag.form.key";

	public static final String TRANSACTION_TOKEN_KEY = "com.jsoft.cocit.TOKEN";

	public static final String TAGLIB_PACKAGE = "com.jsoft.cocit";

	public static final String TOKEN_KEY = TAGLIB_PACKAGE + ".TOKEN";

	public static final String LOCALE_KEY = TAGLIB_PACKAGE + ".LOCALE";

	public static final String XHTML_KEY = TAGLIB_PACKAGE + ".XHTML";

	public static final String EXCEPTION_KEY = "com.jsoft.cocit.EXCEPTION";

	// /*
	// * ------------------------- 未整理
	// */
	//
	// public static final String ACTION_SERVLET_KEY = "com.jsoft.cocit.ACTION_SERVLET";
	//
	// public static final String CANCEL_KEY = "com.jsoft.cocit.CANCEL";
	//
	// public static final String MODULE_KEY = "com.jsoft.cocit.MODULE";
	//
	// public static final String MODULE_PREFIXES_KEY = "com.jsoft.cocit.MODULE_PREFIXES";
	//
	// public static final String ORIGINAL_URI_KEY = "com.jsoft.cocit.ORIGINAL_URI_KEY";
	//
	// public static final String ERROR_KEY = "com.jsoft.cocit.ERROR";
	//
	//
	// public static final String LOCALE_KEY = "com.jsoft.cocit.LOCALE";
	//
	// public static final String MAPPING_KEY = "com.jsoft.cocit.mapping.instance";
	//
	// public static final String MESSAGE_KEY = "com.jsoft.cocit.ACTION_MESSAGE";
	//
	// public static final String MESSAGES_KEY = "com.jsoft.cocit.MESSAGE";
	//
	// public static final String MULTIPART_KEY = "com.jsoft.cocit.mapping.multipartclass";
	//
	// public static final String PLUG_INS_KEY = "com.jsoft.cocit.PLUG_INS";
	//
	// public static final String REQUEST_PROCESSOR_KEY = "com.jsoft.cocit.REQUEST_PROCESSOR";
	//
	// public static final String SERVLET_KEY = "com.jsoft.cocit.SERVLET_MAPPING";
	//
	//
	//
	//
	// public static final String CANCEL_PROPERTY = TAGLIB_PACKAGE + ".CANCEL";
	//
	// public static final String CANCEL_PROPERTY_X = TAGLIB_PACKAGE + ".CANCEL.x";
	//
}
