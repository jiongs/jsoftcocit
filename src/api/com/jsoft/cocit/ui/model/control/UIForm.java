package com.jsoft.cocit.ui.model.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.UIFormModel;
import com.jsoft.cocit.util.StringUtil;

/**
 * 实体表单模型
 * 
 * @author yongshan.ji
 * 
 */
public class UIForm extends UIControlModel implements UIFormModel {

	private Object dataObject;
	private String submitUrl;// 提交表单的目标URL地址

	//
	private String actionID;

	// 字段组
	private Map<String, UIFieldGroup> fieldGroupMap;
	private List<String> fieldGroups;
	// 字段
	private List<UIFieldModel> fieldList;
	private Map<String, UIField> fieldMap;
	private List<List<String>> fields;// 行字段：将所有字段分解成多少行？每行显示哪些字段？
	private List<String> batchFields;// 批处理字段：哪些字段支持批量数据添加、修改等？
	//
	private byte fieldLabelPos;// 字段标签的位置：自动布局时生效
	private int rowFieldsSize;// 每行最多显示多少个字段？自动布局时生效
	private String beanName = "entity";
	private boolean autoButtons = true;
	private boolean ignoreFormTag = false;
	private UIActions actions;

	public UIForm() {
		super();

		fieldList = new ArrayList();
		fieldGroups = new ArrayList();
		fieldGroupMap = new HashMap();
		fieldMap = new HashMap();
	}

	public void release() {
		super.release();
		this.dataObject = null;
		Iterator<String> keys = fieldMap.keySet().iterator();
		while (keys.hasNext()) {
			UIField f = fieldMap.get(keys.next());
			f.release();
		}
		fieldMap.clear();
		fieldMap = null;
		fieldList.clear();
		fieldList = null;

		for (UIFieldGroup group : fieldGroupMap.values()) {
			group.release();
		}
		this.fieldGroups.clear();
		this.fieldGroups = null;
		this.submitUrl = null;
	}

	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_FORM;

		return viewName;
	}

	public Object getDataObject() {
		return dataObject;
	}

	public void setDataObject(Object data) {
		this.dataObject = data;
	}

	public List<String> getFieldGroups() {
		return fieldGroups;
	}

	public void addFieldGroup(UIFieldGroup fieldGroup) {
		this.fieldGroups.add(fieldGroup.getId());
		this.fieldGroupMap.put(fieldGroup.getId(), fieldGroup);
	}

	public String getSubmitUrl() {
		return submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public void addField(UIField field) {
		fieldList.add(field);
		this.fieldMap.put(field.getPropName(), field);
	}

	public UIField getField(String propName) {
		return fieldMap.get(propName);
	}

	public UIFieldGroup getFieldGroup(String groupKey) {
		return fieldGroupMap.get(groupKey);
	}

	public void setBatchFields(List<String> batching) {
		this.batchFields = batching;
	}

	public List<String> getBatchFields() {
		return batchFields;
	}

	public void setFieldLabelPos(byte fieldLabelPos) {
		this.fieldLabelPos = fieldLabelPos;
	}

	public byte getFieldLabelPos() {
		return fieldLabelPos;
	}

	public List<List<String>> getFields() {
		return fields;
	}

	public void setFields(List<List<String>> fieldRows) {
		this.fields = fieldRows;
	}

	public void setRowFieldsSize(int cols) {
		this.rowFieldsSize = cols;
	}

	public int getRowFieldsSize() {
		return rowFieldsSize;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getActionID() {
		return actionID;
	}

	public void setActionID(String actionID) {
		this.actionID = actionID;
	}

	public List<UIFieldModel> getFieldList() {
		return fieldList;
	}

	public UIActions getActions() {
		return actions;
	}

	public void setActions(UIActions actions) {
		this.actions = actions;
	}

	public boolean isAutoButtons() {
		return autoButtons;
	}

	public void setAutoButtons(boolean autoButtons) {
		this.autoButtons = autoButtons;
	}

	public boolean isIgnoreFormTag() {
		return ignoreFormTag;
	}

	public void setIgnoreFormTag(boolean ignoreFormTag) {
		this.ignoreFormTag = ignoreFormTag;
	}
}
