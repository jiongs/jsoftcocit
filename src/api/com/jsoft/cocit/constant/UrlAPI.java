package com.jsoft.cocit.constant;

/**
 * @author yongshan.ji
 * @preserve all
 * 
 */
public interface UrlAPI {
	// =================================================================================================================
	//
	// 下面是实体模块功能服务 URL API
	//
	// =================================================================================================================

	public static final String ENTITY_URL_PREFIX = "/cocentity";

	/**
	 * <b>“实体功能模块”之“主界面”访问地址</b>
	 * <p>
	 * <b>语法：</b>/cocentity/main/${menuID}[/${isAjax}]
	 * <p>
	 * <b>参数：</b> <br>
	 * ${menuID}：必需，功能菜单ID，可以是功能菜单物理主键(Long)或逻辑主键(String)。 <br>
	 * ${isAjax}：可选，是否通过AJAX方式访问此功能？默认值(0|false)，值选项包括“0|1|false|true”，“true|1”表示通过AJAX方式访问，即返回的HTML内容不包含HTML头及JS/CSS代码。
	 */
	public static final String ENTITY_GET_MAINS_UI = ENTITY_URL_PREFIX + "/mains/*";
	public static final String ENTITY_GET_MAIN_UI = ENTITY_URL_PREFIX + "/main/*";
	public static final String ENTITY_GET_GRID = ENTITY_URL_PREFIX + "/getGrid/*";
	public static final String ENTITY_GET_GRID_DATA = ENTITY_URL_PREFIX + "/getGridData/*";
	/**
	 * @deprecated 用 {@link #ENTITY_GET_GRID_DATA} 代替
	 */
	public static final String ENTITY_GET_GRID_JSON = ENTITY_GET_GRID_DATA;
	public static final String ENTITY_GET_COMBO_GRID = ENTITY_URL_PREFIX + "/getComboGrid/*";
	/**
	 * 获取Combo Grid数据。
	 * <p>
	 * 语法：/cocentity/getComboGridData/${fkTargetMenuID}:${fkTargetEntityID}/${entityID}:${fkFieldID}
	 * <p>
	 * fkTargetMenuID:当前模块外键字段引用了哪个菜单？
	 * <p>
	 * fkTargetEntityID:当前模块外键字段引用了哪个实体？
	 * <p>
	 * entityID:当前实体KEY
	 * <p>
	 * fkFieldID:当前实体外键字段KEY
	 */
	public static final String ENTITY_GET_COMBOGRID_DATA = ENTITY_URL_PREFIX + "/getComboGridData/*";
	/**
	 * 获取Combo List数据。
	 * <p>
	 * 语法：/cocentity/getComboListData/${fkTargetMenuID}:${fkTargetEntityID}/${entityID}:${fkFieldID}
	 * <p>
	 * fkTargetMenuID:当前模块外键字段引用了哪个菜单？
	 * <p>
	 * fkTargetEntityID:当前模块外键字段引用了哪个实体？
	 * <p>
	 * entityID:当前实体KEY
	 * <p>
	 * fkFieldID:当前实体外键字段KEY
	 */
	public static final String ENTITY_GET_COMBOLIST_DATA = ENTITY_URL_PREFIX + "/getComboListData/*";
	/**
	 * 获取Combo Tree数据。
	 * <p>
	 * 语法：/cocentity/getComboTreeData/${fkTargetMenuID}:${fkTargetEntityID}/${entityID}:${fkFieldID}
	 * <p>
	 * fkTargetMenuID:当前模块外键字段引用了哪个菜单？
	 * <p>
	 * fkTargetEntityID:当前模块外键字段引用了哪个实体？
	 * <p>
	 * entityID:当前实体KEY
	 * <p>
	 * fkFieldID:当前实体外键字段KEY
	 */
	public static final String ENTITY_GET_COMBOTREE_DATA = ENTITY_URL_PREFIX + "/getComboTreeData/*";
	public static final String ENTITY_GET_FILTER = ENTITY_URL_PREFIX + "/getFilter/*";
	public static final String ENTITY_GET_FILTER_DATA = ENTITY_URL_PREFIX + "/getFilterData/*";
	public static final String ENTITY_GET_TREE = ENTITY_URL_PREFIX + "/getTree/*";
	public static final String ENTITY_GET_TREE_DATA = ENTITY_URL_PREFIX + "/getTreeData/*";
	public static final String ENTITY_GET_ACTIONS = ENTITY_URL_PREFIX + "/getActions/*";
	public static final String ENTITY_GET_ACTIONS_DATA = ENTITY_URL_PREFIX + "/getActionsData/*";
	public static final String ENTITY_GET_ROWS_AUTH_DATA = ENTITY_URL_PREFIX + "/getRowsAuthData/*";

	/**
	 * <b>语法：</b>/cocentity/getFormToSave/${menuID}[:${entityID}[:${opID}]][/${dataID}]
	 * <p>
	 * 参数：
	 * <UL>
	 * <LI>dataID: 正数数值表示根据数据ID(物理主键)获取数据表单，0——表示获取添加表单，-1——表示自动获取满足条件的第一条数据。
	 * </UL>
	 */
	public static final String ENTITY_GET_FORM_TO_SAVE = ENTITY_URL_PREFIX + "/getFormToSave/*";
	public static final String ENTITY_SAVE = ENTITY_URL_PREFIX + "/save/*";
	public static final String ENTITY_GET_DATAOBJECT = ENTITY_URL_PREFIX + "/getDataObject/*";
	public static final String ENTITY_GET_FORM_TO_REMOVE = ENTITY_URL_PREFIX + "/getFormToRemove/*";
	public static final String ENTITY_REMOVE = ENTITY_URL_PREFIX + "/remove/*";
	public static final String ENTITY_GET_FORM_TO_DELETE = ENTITY_URL_PREFIX + "/getFormToDelete/*";
	public static final String ENTITY_DELETE = ENTITY_URL_PREFIX + "/delete/*";
	public static final String ENTITY_GET_FORM_TO_EXPORT_XLS = ENTITY_URL_PREFIX + "/getFormToExportXls/*";
	public static final String ENTITY_EXPORT_XLS = ENTITY_URL_PREFIX + "/exportXls/*";
	public static final String ENTITY_GET_FORM_TO_IMPORT_XLS = ENTITY_URL_PREFIX + "/getFormToImportXls/*";
	public static final String ENTITY_IMPORT_XLS = ENTITY_URL_PREFIX + "/importXls/*";
	public static final String ENTITY_GET_FORM_TO_RUN = ENTITY_URL_PREFIX + "/getFormToRun/*";
	public static final String ENTITY_RUN = ENTITY_URL_PREFIX + "/run/*";
	public static final String ENTITY_CLEAR = ENTITY_URL_PREFIX + "/clear/*";

	public static final String ENTITY_SORT_MOVE_UP = ENTITY_URL_PREFIX + "/sortMoveUp/*";
	public static final String ENTITY_SORT_MOVE_DOWN = ENTITY_URL_PREFIX + "/sortMoveDown/*";
	public static final String ENTITY_SORT_MOVE_TOP = ENTITY_URL_PREFIX + "/sortMoveTop/*";
	public static final String ENTITY_SORT_MOVE_BOTTOM = ENTITY_URL_PREFIX + "/sortMoveBottom/*";
	public static final String ENTITY_SORT_REVERSE = ENTITY_URL_PREFIX + "/sortReverse/*";
	public static final String ENTITY_SORT_CANCEL = ENTITY_URL_PREFIX + "/sortCancel/*";

	// =================================================================================================================
	//
	// 下面是工具类 URL API
	//
	// =================================================================================================================
	/**
	 * 获取手机验证码
	 */
	public static final String GET_IMG_VERIFY_CODE = "/coc/getImgVerifyCode";
	public static final String CHK_IMG_VERIFY_CODE = "/coc/chkImgVerifyCode/*";

	/**
	 * 获取短信验证码
	 * <p>
	 * 参数：手机号码
	 */
	public static final String GET_SMS_VERIFY_CODE = "/coc/getSmsVerifyCode/*";
	public static final String GET_SMS_VERIFY_CODE2 = "/coc/getSmsVerifyCode2/*";
	public static final String CHK_SMS_VERIFY_CODE = "/coc/chkSmsVerifyCode/*";

	/** 心跳监测API，供AJAX调用，用来确保浏览器Session永不过期。 */
	public static final String CHK_HEARTBEAT = "/coc/chkHeartbeat/*";
	public static final String GET_FILE_MANAGER = "/coc/getFileManager/*";
	public static final String GET_FILE_GRID_JSON = "/coc/getFileGridJson/*";
	public static final String GET_FILE_TREE_JSON = "/coc/getFileTreeJson/*";
	public static final String DEL_DISK_FILES = "/coc/deleteDiskFiles/*";

	/** 文件上传API：用于CKFinder */
	public static final String URL_CKFINDER = "/coc/getCKFinder/*";
	/** 文件上传API：用于表单中的上传组件 */
	public static final String URL_UPLOAD = "/coc/upload/*";

	// =================================================================================================================
	//
	// 下面是安全管理类 URL API
	//
	// =================================================================================================================
	public static final String URL_ADMIN = "/admin0";
	public static final String URL_ADMIN_CONFIG = URL_ADMIN + "/config";
	public static final String URL_ADMIN_LOGIN = URL_ADMIN + "/login";

	// =================================================================================================================
	//
	// 以下是报表管理模块相关功能的访问路径
	//
	// =================================================================================================================
	// TODO:

	// =================================================================================================================
	//
	// 以下是流程管理模块相关功能的访问路径
	//
	// =================================================================================================================
	// TODO:

	// =================================================================================================================
	//
	// 以下是访问网站页面的相关路径
	//
	// =================================================================================================================
	public static final String GET_JSP_PAGE = "/jsp/*";

}
