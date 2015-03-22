package com.jsoft.cocit.constant;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface FieldNames {
	static final String F_TENANT_KEY = "tenantKey";
	static final String F_TYPE = "type";
	static final String F_VERSION = "version";
	static final String F_ID = "id";
	static final String F_KEY = "key";
	static final String F_GRID_COLUMN_SN = "gridColumnSn";
	static final String F_CREATED_DATE = "createdDate";
	static final String F_CREATED_USER = "createdUser";
	static final String F_CREATED_OP_LOG = "createdOpLog";
	static final String F_UPDATED_DATE = "updatedDate";
	static final String F_UPDATED_USER = "updatedUser";
	static final String F_UPDATED_OP_LOG = "updatedOpLog";
	static final String F_PARENT_KEY = "parentKey";
	static final String F_CATALOG_KEY = "catalogKey";
	static final String F_FK_TARGET_ENTITY = "fkTargetEntityKey";
	static final String F_SN = "sn";
	static final String F_STATUS_CODE = "statusCode";
	static final String F_COC_ENTITY_KEY = "cocEntityKey";
	static final String F_CUI_ENTITY_KEY = "cuiEntityKey";
	static final String F_CUI_FORM_KEY = "cuiFormKey";
	static final String F_CUI_GRID_KEY = "cuiGridKey";
	static final String F_NAME = "name";
	static final String F_SYSTEM_KEY = "systemKey";
	static final String F_PROP_NAME = "key";
	static final String F_DOMAIN = "domain";

	/**
	 * 字段列表：租户ID,逻辑主键KEY
	 */
	static final String FLIST_TENANT_KEYS = F_TENANT_KEY + "," + F_KEY;

	/**
	 * 字段列表：模块LEY,逻辑主键KEY
	 */
	static final String FLIST_COC_KEYS = F_COC_ENTITY_KEY + "," + F_KEY;
	static final String FLIST_CUI_KEYS = F_CUI_ENTITY_KEY + "," + F_KEY;
}
