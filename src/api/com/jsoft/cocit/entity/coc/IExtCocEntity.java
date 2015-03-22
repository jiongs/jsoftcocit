package com.jsoft.cocit.entity.coc;

import com.jsoft.cocit.entity.IExtNamedEntity;

/**
 * 实体模块：用来描述“实体类、实体表、实体UI”等实体管理相关的属性。
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface IExtCocEntity extends IExtNamedEntity, ICocEntity {

	void setCompileVersion(String version);

	void setSortExpr(String dataSortExpr);

	void setUiView(String uiView);

	void setPathPrefix(String pathPrefix);

	void setClassName(String name);

	void setExtendsClassName(String className);

	void setCatalogKey(String catalogKey);

	void setCatalogName(String catalogName);

	void setTableName(String tableName);

	void setDataAuthFields(String dataAuthFields);
}
