package com.jsoft.cocit.constant;

public interface Const extends EntityConst, TableNames, FieldNames, FieldTypes, StatusCodes, MenuTypes, OpCodes, EntityCodes, PrincipalTypes {

	/**
	 * top框架高度
	 */
	public static final String CONFIG_KEY_UI_TOPHEIGHT = "admin.ui.topHeight";
	/**
	 * top框架宽度
	 */
	public static final String CONFIG_KEY_UI_LEFTWIDTH = "admin.ui.leftWidth";
	/**
	 * body框架宽度：body区域包括TABS和内容区域
	 */
	public static final String CONFIG_KEY_UI_BODYWIDTH = "admin.ui.bodyWidth";
	/**
	 * body框架高度：body区域包括TABS和内容区域
	 */
	public static final String CONFIG_KEY_UI_BODYHEIGHT = "admin.ui.bodyHeight";
	/**
	 * 内容区域宽度：内容区域不包括TABS
	 */
	public static final String CONFIG_KEY_UI_CONTENTWIDTH = "admin.ui.contentWidth";
	/**
	 * 内容区域高度：内容区域不包括TABS
	 */
	public static final String CONFIG_KEY_UI_CONTENTHEIGHT = "admin.ui.contentHeight";
	/**
	 * 内容区域内部间隙：内容区域不包括TABS
	 */
	public static final String CONFIG_KEY_UI_CONTENTPADDING = "admin.ui.contentPadding";
	/**
	 * bottom框架高度
	 */
	public static final String CONFIG_KEY_UI_BOTTOMHEIGHT = "admin.ui.bottomHeight";

	public static final int DEFAULT_PAGE_SIZE = 20;

	/**
	 * JSP 目录
	 */
	public static final String JSP_DIR = "/WEB-INF/jsp/coc";

	public static final String ST_DIR = "templates/defaults";

	static final String SESSION_KEY_LOGIN_SESSION = "COCIT.LOGIN.KEY";

	/*
	 * CKEditor 配置文件中用到
	 */
	static final String SESSION_KEY_CKFINDER_USER_ROLE = "UserRole";
	static final String SESSION_KEY_LATEST_URI = "latestURI";

	// /**
	// * 参数用户类型KEY：用于从HTTP请求中获取参数用户类型( {@link com.Cocit.cocit.kmetop.Cocit.security.Coc#realm()})
	// */
	static final String REQUEST_KEY_TENANT = "_logintenant_";
	static final String REQUEST_KEY_SYSTEM = "_loginsystem_";
	static final String REQUEST_KEY_REALM = "_loginrealm_";
	static final String REQUEST_KEY_USER = "_loginuser_";
	static final String REQUEST_KEY_PWD = "_loginpwd_";
	static final String REQUEST_VAL_CODE = "_loginvalcode_";

	static final String REQUEST_KEY_CLIENT_WIDTH = "clientWidth";
	static final String REQUEST_KEY_CLIENT_HEIGHT = "clientHeight";
	

	static final String TEST_TABLE_NAME = "COC_TEST_C3P0";

	static final String CONTENT_TYPE_HTML = "text/html; charset=UTF-8";
	static final String CONTENT_TYPE_JSON = "text/json; charset=UTF-8";
	static final String CONTENT_TYPE_XML = "text/xml; charset=UTF-8";

	static final String VAR_PREFIX = "${";
	static final String VAR_POSTFIX = "}";

	// public static final String FORM_FIELD_PREFIX = "entity";

	/*
	 * JSON 响应码
	 */
	public static final int RESPONSE_CODE_SUCCESS = 200;
	public static final int RESPONSE_CODE_ERROR = 300;
	public static final int RESPONSE_CODE_ERROR_NO_PERMISSION = 301;
	public static final String CONFIG_KEY_THEMES_CSS = "themesCSS";
	public static final String CONFIG_KEY_THEMES_JS = "themesJS";

}
