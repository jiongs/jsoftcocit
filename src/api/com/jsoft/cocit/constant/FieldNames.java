package com.jsoft.cocit.constant;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface FieldNames {
	static final String F_TENANT_CODE = "tenantCode";
	static final String F_TYPE = "type";
	static final String F_VERSION = "version";
	static final String F_ID = "id";
	static final String F_CODE = "code";
	static final String F_TEL = "telCode";
	static final String F_GRID_COLUMN_SN = "gridColumnSn";
	static final String F_CREATED_DATE = "createdDate";
	static final String F_CREATED_USER = "createdUser";
	static final String F_CREATED_OP_LOG = "createdOpLog";
	static final String F_UPDATED_DATE = "updatedDate";
	static final String F_UPDATED_USER = "updatedUser";
	static final String F_UPDATED_OP_LOG = "updatedOpLog";
	static final String F_PARENT_CODE = "parentCode";
	static final String F_CATALOG_CODE = "catalogCode";
	static final String F_FK_TARGET_ENTITY = "fkTargetEntityCode";
	static final String F_SN = "sn";
	static final String F_STATUS_CODE = "statusCode";
	static final String F_COC_ENTITY_CODE = "cocEntityCode";
	static final String F_CUI_ENTITY_CODE = "cuiEntityCode";
	static final String F_CUI_FORM_CODE = "cuiFormCode";
	static final String F_CUI_GRID_CODE = "cuiGridCode";
	static final String F_NAME = "name";
	static final String F_SYSTEM_CODE = "systemCode";
	static final String F_PROP_NAME = "code";
	static final String F_DOMAIN = "domain";

	/**
	 * 字段列表：租户ID,逻辑主键KEY
	 */
	static final String FLIST_TENANT_CODES = F_TENANT_CODE + "," + F_CODE;

	/**
	 * 字段列表：模块LEY,逻辑主键KEY
	 */
	static final String FLIST_COC_CODES = F_COC_ENTITY_CODE + "," + F_CODE;
	static final String FLIST_CUI_CODES = F_CUI_ENTITY_CODE + "," + F_CODE;
}
