package com.jsoft.cocit.baseentity.coc;

import com.jsoft.cocit.baseentity.INamedEntityExt;

/**
 * 实体字段：用于描述“实体属性、数据表字段、字段UI”等信息。
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface ICocFieldEntityExt extends INamedEntityExt, ICocFieldEntity {

	void setCocEntityCode(String moduleCode);

	void setCocGroupCode(String fieldGroupCode);

	void setFieldType(int dataType);

	void setManyTargetEntityCode(String manyTargetEntityCode);

	void setFkTargetEntityCode(String fkTargetEntity);

	void setFkTargetFieldCode(String fkTargetField);

	void setFkDependFieldCode(String fkDependField);

	void setFkTargetAsParent(boolean fkTargetAsParent);

	void setFkTargetAsGroup(boolean fkTargetAsGroup);

	void setScale(Integer scale);

	void setPrecision(Integer precision);

	void setMode(String mode);

	void setUomOptions(String uomOptions);

	void setUomOption(String uomOption);

	void setDicOptions(String options);

	void setDefaultValue(String defaultValue);

	void setPattern(String pattern);

	void setFieldName(String fieldName);

	void setTransient(boolean isTransientField);

	void setAsGridColumn(boolean asGridColumn);

	void setGridColumnSn(Integer gridSn);

	void setGridColumnWidth(Integer gridWidth);

	void setUiView(String viewName);

	void setAsFilterNode(boolean asFilterNode);

	void setUiCascading(String cascadeMode);

	void setDataCascading(String cascadeMode);

	void setLength(int len);

	void setDbColumnName(String dbColumnName);

	void setDbColumnDefinition(String dbColumnDefinition);

	void setDbColumnNotNull(boolean dbColumnNotNull);

	void setMultiSelect(boolean multipleValue);

	void setGenerator(String generator);

	void setPrevInterceptors(String prevIntecerptors);

	void setPostInterceptors(String postInterceptors);

	void setFkComboUrl(String fkComboUrl);

	void setFkCascadeDelete(boolean fkCascadeDelete);

}
