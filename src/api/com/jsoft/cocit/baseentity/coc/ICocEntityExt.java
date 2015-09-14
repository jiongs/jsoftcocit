package com.jsoft.cocit.baseentity.coc;

import com.jsoft.cocit.baseentity.INamedEntityExt;

/**
 * 实体模块：用来描述“实体类、实体表、实体UI”等实体管理相关的属性。
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface ICocEntityExt extends INamedEntityExt, ICocEntity {

	void setCompileVersion(String version);

	void setSortExpr(String dataSortExpr);

	void setUiView(String uiView);

	void setPathPrefix(String pathPrefix);

	void setClassName(String name);

	void setExtendsClassName(String className);

	void setCatalogCode(String catalogCode);

	void setCatalogName(String catalogName);

	void setTableName(String tableName);

	void setDataAuthFields(String dataAuthFields);

	/**
	 * 设置该业务模块是否需要走工作流引擎？
	 * 
	 * @param workflow
	 *            true: 该业务模块需要走工作流引擎 <br/>
	 *            false: 该业务模块无需走工作流引擎
	 * @return
	 */
	void setWorkflow(boolean workflow);

	void setPrevInterceptors(String prevIntecerptors);

	void setPostInterceptors(String postInterceptors);
}
