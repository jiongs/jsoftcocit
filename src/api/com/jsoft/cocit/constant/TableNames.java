package com.jsoft.cocit.constant;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
interface TableNames {
	/*
	 * 日志相关
	 */
	static final String TBL_LOG_RUNNING = "coc_log_running";
	static final String TBL_LOG_VISIT = "coc_log_visit";
	static final String TBL_LOG_LOGIN = "coc_log_login";
	static final String TBL_LOG_OPERATION = "coc_log_operation";

	/*
	 * 实体相关
	 */
	static final String TBL_COC_CATALOG = "coc_def_catalog";
	static final String TBL_COC_ENTITY = "coc_def_entity";
	static final String TBL_COC_ACTION = "coc_def_action";
	static final String TBL_COC_FIELD = "coc_def_field";
	static final String TBL_COC_GROUP = "coc_def_group";

	/*
	 * 实体界面相关
	 */
	static final String TBL_CUI_ENTITY = "coc_cui_entity";
	static final String TBL_CUI_GRID = "coc_cui_grid";
	static final String TBL_CUI_GRIDFIELD = "coc_cui_gridfield";
	static final String TBL_CUI_FORM = "coc_cui_form";
	static final String TBL_CUI_FORMFIELD = "coc_cui_formfield";
	static final String TBL_CUI_FORMACTION = "coc_cui_formaction";

	/*
	 * 安全相关
	 */
	static final String TBL_SEC_SYSTEM = "coc_sec_system";
	static final String TBL_SEC_GROUP = "coc_sec_group";
	static final String TBL_SEC_GROUPMEMBER = "coc_sec_groupmember";
	static final String TBL_SEC_GROUPROLE = "coc_sec_grouprole";
	static final String TBL_SEC_AUTHORITY = "coc_sec_authority";
	static final String TBL_SEC_ROLE = "coc_sec_role";
	static final String TBL_SEC_ROLEMEMBER = "coc_sec_rolemember";
	static final String TBL_SEC_SYSMENU = "coc_sec_sysmenu";
	static final String TBL_SEC_TENANT = "coc_sec_tenant";
	static final String TBL_SEC_SYSTENANCY = "coc_sec_systenancy";
	static final String TBL_SEC_SYSUSER = "coc_sec_sysuser";
	static final String TBL_SEC_DISABLEDIP = "coc_sec_disabledip";

	/*
	 * 配置相关
	 */
	static final String TBL_CFG_DATASOURCE = "coc_cfg_datasource";
	static final String TBL_CFG_DIC = "coc_cfg_dic";
	static final String TBL_CFG_DICITEM = "coc_cfg_dicitem";
	static final String TBL_CFG_SYSPREF = "coc_cfg_syspref";
	static final String TBL_CFG_TENANTPREF = "coc_cfg_tenantpref";
	static final String TBL_CFG_USERPREF = "coc_cfg_userpref";

}
