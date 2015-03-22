package com.jsoft.cocit.ui.model;

import java.util.List;
import java.util.Properties;

import com.jsoft.cocit.constant.FieldTypes;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.util.Option;

public interface UIFieldModel {

	public String getId();

	/**
	 * 获取字段UI显示模式
	 */
	public int getMode();

	/**
	 * 获取字段类型，参见{@link FieldTypes#FIELD_TYPE_xxx}
	 */
	public int getFieldType();

	/**
	 * 获取字段名（JAVA属性名）
	 */
	public String getPropName();

	/**
	 * 获取字段UI对齐方式
	 */
	public String getAlign();

	/**
	 * 获取字段UI宽度
	 */
	public int getWidth();

	/**
	 * 获取字段校验名称
	 */
	public String getPatternName();

	/**
	 * 获取“字段”校验表达式
	 */
	public String getPattern();

	/**
	 * 格式化字段值
	 */
	public String format(Object fieldValue);

	/**
	 * 检查该字段是否需要独立显示在一行
	 * 
	 * @return
	 */
	public boolean isNewRow();

	/**
	 * 获取“字典字段”选项列表
	 */
	public Option[] getDicOptions();

	/**
	 * 获取“字典字段”当前值选项
	 */
	public Option getDicOption(Object fieldValue);

	/**
	 * 获取“数字字段”的小数位数
	 */
	public int getScale();

	/**
	 * 获取“外键字段”树形数据异步加载的URL
	 */
	public String getFkComboTreeUrl();

	public String getFkComboListUrl();
	
	public String getFkComboGridUrl();

	/**
	 * 获取字段UI属性
	 */
	public Properties getAttributes();

	public void release();

	public String getLinkUrl();

	public String getLinkTarget();

	public <T> T get(String key);

	public StyleRule[] getCellStyles();

	public boolean isHidden();

	public boolean isShowTips();

	public String getTitle();

	public String getViewName();

	public UIFieldModel setPropName(String propname);

	public String getHalign();

	public List<String> getResultUI();

	public void addResultUI(String resultUI);

	public boolean isMultiple();

	public <T extends BaseUIModel> T setViewName(String viewName);
	
	public <T extends BaseUIModel> T setTitle(String title);
}
