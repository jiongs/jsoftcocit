package com.jsoft.cocit.baseentity.security;

import com.jsoft.cocit.baseentity.ITreeEntityExt;

/**
 * <b>功能菜单：</b> 即系统提供给用户使用的功能模块。
 * <p>
 * <b>功能菜单授权：</b>
 * <p>
 * 可以将模块授权给“功能角色”；可以将模块授权给“用户”；可以将模块授权给“组”。
 * <p>
 * <b>模块分类：</b>
 * <p>
 * 分类模块： 不包含具体的功能操作，通常没有链接地址，不能链接到特定的模块页面，用来对模块进行归类。
 * <p>
 * 业务模块： 用于完成基本的业务数据处理。
 * <p>
 * 报表模块： 用于完成数据分析统计。
 * <p>
 * 流程模块： 用于调度多任务按特定流程执行。
 * <p>
 * 静态模块： 以上模块都是动态模块，静态模块即具有独立的MVC部分，即具有独立的Action类、数据实体、展现JSP等。
 * <p>
 * <b>备注：</b>
 * <p>
 * 功能菜单具有树形递归结构，通过分类菜单对模块进行归类后从而形成了模块的树形递归结构。
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface ISystemMenuEntityExt extends ITreeEntityExt, ISystemMenuEntity {
	void setSystemCode(String systemCode);

	void setDataSourceCode(String dsCode);

	void setLogo(String logo);

	void setImage(String image);

	void setType(int type);

	void setPath(String path);

	void setRefEntity(String key);

	void setActions(String actions);

	void setFields(String fields);

	void setWhereRule(String whereRule);

	void setDefaultValuesRule(String rule);

	void setUiView(String uiView);

	void setHidden(boolean hidden);
}
