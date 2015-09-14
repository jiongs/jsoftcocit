package com.jsoft.cocit.baseentity.coc;

import com.jsoft.cocit.baseentity.ITreeEntityExt;

/**
 * 动作：即在功能模块上执行的操作。
 * 
 * <UL>
 * <LI>代表一个运行时的自定义数据操作，通常由定义在数据库中的数据实体解析而来；
 * <LI>与数据表的关系：每个数据操作只能隶属于一个数据表，而一个数据表可以包含多个数据操作；
 * <LI>同一个数据表内数据操作之间的关系：数据操作可以是树形结构，用于构成树形结构的操作菜单；
 * </UL>
 * <p>
 * <b>操作模式：</b>
 * <UL>
 * <LI>c: 用于新增操作，所有新增操作都以c开头
 * <LI>e: 用于编辑操作，所有编辑操作都以e开头
 * <LI>d: 用于删除操作，所有删除操作都以d开头
 * <LI>v: 用于浏览操作，所有浏览操作都以v开头
 * <LI>bu: 批量修改
 * </UL>
 * <p>
 * <b>操作码：</b>
 * <UL>
 * <LI>
 * <LI>打开主界面：TYPE_BZMAIN = 1;
 * <LI>打开业务表界面：TYPE_BZSYS = 2;
 * <LI>TYPE_BZGRID = 3;
 * <LI>TYPE_BZMENU = 4;
 * <LI>TYPE_BZNAVI = 5;
 * <LI>TYPE_BZSYS_COMB_EXPR = 21;
 * <LI>TYPE_BZSYS_COMB_FK = 22;
 * <LI>TYPE_BZSYS_COMB_CATALOG_EXPR = 23;
 * 
 * <LI>添加按钮：打开添加表单 TYPE_BZFORM_NEW = 101;
 * <LI>TYPE_BZFORM_EDIT = 102;
 * <LI>TYPE_BZFORM_EDIT_N = 103;
 * <LI>TYPE_BZFORM_EXEC_SYNC = 104;
 * <LI>TYPE_BZFORM_EXEC_ASYN = 105;
 * <LI>TYPE_BZFORM_PRINT = 106;
 * <LI>TYPE_BZFORM_EXPORT_XLS = 107;
 * <LI>TYPE_BZFORM_IMPORT_XLS = 108;
 * <LI>TYPE_BZFORM_ADD_N = 109;
 * <LI>TYPE_BZFORM_LOAD = 199;
 * 
 * <LI>执行保存逻辑：TYPE_BZ_SAVE = 201;
 * <LI>TYPE_BZ_EXEC_SYNC = 204;
 * <LI>TYPE_BZ_EXEC_ASYN = 205;
 * <LI>TYPE_BZ_EXPORT_XLS = 207;
 * <LI>TYPE_BZ_IMPORT_XLS = 208;
 * <LI>TYPE_BZ_ORDERBY = 250;
 * <LI>TYPE_BZ_ORDERBY_UP = 251;
 * <LI>TYPE_BZ_ORDERBY_DOWN = 252;
 * <LI>TYPE_BZ_ORDERBY_REVERSE = 253;
 * <LI>TYPE_BZ_ORDERBY_TOP = 254;
 * <LI>TYPE_BZ_ORDERBY_BOTTOM = 255;
 * <LI>TYPE_BZ_ORDERBY_CANCEL = 256;
 * <LI>TYPE_BZ_DEL = 299;
 * <LI>TYPE_BZ_CLEAR = 298;
 * <LI>TYPE_BZ_AUTO_MAKED_UPDATE_MENUS = 901;
 * </UL>
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface ICocActionEntityExt extends ITreeEntityExt, ICocActionEntity {

	void setCocEntityCode(String key);

	void setOpCode(Integer opCode);

	void setUiForm(String uiForm);

	void setUiFormUrl(String uiFormUrl);

	void setUiFormTarget(String uiFormTarget);

	void setPlugin(String pluginName);

	void setBtnImage(String image);

	void setLogo(String logo);

	void setDefaultValuesRule(String rule);

	void setAssignValuesRule(String rule);

	void setWhereRule(String rule);

	void setSuccessMessage(String info);

	void setErrorMessage(String error);

	void setWarnMessage(String warn);

	void setTitle(String title);

	void setProxyActions(String proxyActions);

	void setUiWindowHeight(int uiWindowHeight);

	void setUiWindowWidth(int uiWindowWidth);

	void setUiWindowHeader(boolean uiWindowHeader);

	void setPrevInterceptors(String prevIntecerptors);

	void setPostInterceptors(String postInterceptors);

}
