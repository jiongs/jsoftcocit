// $codepro.audit.disable unnecessaryCast
package com.jsoft.cocimpl.entityengine.impl.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import org.nutz.json.Json;
import org.nutz.lang.Mirror;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.entity.IDataEntity;
import com.jsoft.cocit.entity.coc.ICocField;
import com.jsoft.cocit.entityengine.PatternAdapter;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.entityengine.service.CocGroupService;
import com.jsoft.cocit.entityengine.service.CuiEntityService;
import com.jsoft.cocit.entityengine.service.CuiFormFieldService;
import com.jsoft.cocit.entityengine.service.CuiFormService;
import com.jsoft.cocit.entityengine.service.DicItemService;
import com.jsoft.cocit.entityengine.service.DicService;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class CocFieldServiceImpl extends NamedEntityServiceImpl<ICocField> implements CocFieldService {

	private Option[] dicOptions;
	private CocEntityService oneToManyTargetEntity;
	private CocEntityService fkTargetEntity;
	private CocEntityService cocEntity;
	private CocGroupService cocGroup;
	private Class classOfField;
	private String fullPropName;
	private Boolean fkManyToOneField;
	private List<UiCascading> uiCascadings;

	CocFieldServiceImpl(ICocField entity, CocEntityService module, CocGroupService fieldGroup) {
		super(entity);
		this.cocEntity = module;
		this.cocGroup = fieldGroup;

		initUiCascadings();
	}

	@Override
	public void release() {
		super.release();

		this.dicOptions = null;
		this.fkTargetEntity = null;
		this.cocEntity = null;
		this.cocGroup = null;
		this.classOfField = null;
		this.fullPropName = null;
		this.fkManyToOneField = null;
		if (uiCascadings != null) {
			uiCascadings.clear();
			uiCascadings = null;
		}
	}

	@Override
	public String getCocEntityKey() {
		return entityData.getCocEntityKey();
	}

	@Override
	public String getCocGroupKey() {
		return entityData.getCocGroupKey();
	}

	@Override
	public int getFieldType() {
		return entityData.getFieldType();
	}

	@Override
	public String getDbColumnName() {
		return entityData.getDbColumnName();
	}

	@Override
	public String getFkTargetEntityKey() {
		return entityData.getFkTargetEntityKey();
	}

	@Override
	public String getFkTargetFieldKey() {
		return entityData.getFkTargetFieldKey();
	}

	@Override
	public boolean isMultiSelect() {
		return entityData.isMultiSelect();
	}

	@Override
	public Integer getScale() {
		return entityData.getScale();
	}

	@Override
	public Integer getPrecision() {
		return entityData.getPrecision();
	}

	@Override
	public String getMode() {
		return entityData.getMode();
	}

	@Override
	public String getUomOptions() {
		return entityData.getUomOptions();
	}

	@Override
	public String getUomOption() {
		return entityData.getUomOption();
	}

	@Override
	public String getDicOptions() {
		return entityData.getDicOptions();
	}

	@Override
	public String getDefaultValue() {
		return entityData.getDefaultValue();
	}

	@Override
	public String getPattern() {
		return entityData.getPattern();
	}

	@Override
	public String getFieldName() {
		return entityData.getFieldName();
	}

	@Override
	public boolean isTransient() {
		return entityData.isTransient();
	}

	@Override
	public boolean isAsGridColumn() {
		return entityData.isAsGridColumn();
	}

	@Override
	public Integer getGridColumnSn() {
		return entityData.getGridColumnSn();
	}

	@Override
	public Integer getGridColumnWidth() {
		return entityData.getGridColumnWidth();
	}

	@Override
	public boolean isFkTargetAsParent() {
		return entityData.isFkTargetAsParent();
	}

	@Override
	public String getUiView() {
		return entityData.getUiView();
	}

	@Override
	public boolean isAsFilterNode() {
		return entityData.isAsFilterNode();
	}

	@Override
	public String getUiCascading() {
		return entityData.getUiCascading();
	}

	@Override
	public boolean isFkTargetAsGroup() {
		return entityData.isFkTargetAsGroup();
	}

	@Override
	public String getDataCascading() {
		return entityData.getDataCascading();
	}

	@Override
	public String getDbColumnDefinition() {
		return entityData.getDbColumnDefinition();
	}

	@Override
	public int getLength() {
		return entityData.getLength();
	}

	@Override
	public String getFkDependFieldKey() {
		return entityData.getFkDependFieldKey();
	}

	@Override
	public String getManyTargetEntityKey() {
		return entityData.getManyTargetEntityKey();
	}

	private void initFkTargetEntity() {
		fkTargetEntity = this.esf().getEntity(this.getFkTargetEntityKey());
	}

	private void initManyTargetEntity() {
		oneToManyTargetEntity = this.esf().getEntity(this.getManyTargetEntityKey());
	}

	@Override
	public Option getDicOption(Object fieldValue) {
		if (fieldValue == null)
			return null;

		String value = fieldValue.toString();
		if (fieldValue instanceof Boolean) {
			value = ((Boolean) fieldValue) ? "1" : "0";
		}

		return makeDicOptionsMap().get(value);
	}

	private Map<Object, Option> makeDicOptionsMap() {
		Map<Object, Option> ret = new HashMap();
		Option[] options = this.getDicOptionsArray();
		if (options != null) {
			for (Option kv : options) {
				ret.put(kv.getValue(), kv);
			}
		}

		return ret;
	}

	@Override
	public Option[] getDicOptionsArray() {
		if (dicOptions != null)
			return dicOptions;

		String strOptions = entityData.getDicOptions();
		if (!StringUtil.isBlank(strOptions)) {

			// 转换字符串为 KeyValue[]。
			dicOptions = makeDicOptions(strOptions);

		} else if (getFieldType() == Const.FIELD_TYPE_BOOLEAN) {

			// 转换Bool值为 KeyValue[]。
			dicOptions = new Option[] { Option.make("是", "1"), Option.make("否", "0") };

		} else
			dicOptions = new Option[0];

		return dicOptions;
	}

	private Option[] makeDicOptions(String strOptions) {
		strOptions = strOptions.trim();
		if (strOptions.charAt(0) == '[') {
			try {
				return Json.fromJson(Option[].class, strOptions);
			} catch (Throwable e) {
				LogUtil.error("解析字段字典选项列表时出错：非法字典选项表达式！(%s)", strOptions, e);
			}
		} else if (strOptions.startsWith(Const.VAR_PREFIX)) {

			List<Option> oplist = new ArrayList();
			DicService catalog = (DicService) this.esf().getDic(strOptions.substring(Const.VAR_PREFIX.length(), strOptions.length() - 1));
			if (catalog != null) {
				List<DicItemService> list = catalog.getItems();
				for (DicItemService item : list) {
					if (!item.isDisabled() && !item.isDisabled())
						oplist.add(Option.make(item.getText(), item.getValue()));
				}

			}
			Option[] dicOptions = new Option[oplist.size()];
			dicOptions = oplist.toArray(dicOptions);

			return dicOptions;
		} else {
			return StringUtil.toOptions(strOptions);
		}

		return new Option[0];
	}

	@Override
	public List<String> getUploadTypes() {
		return StringUtil.toList(entityData.getPattern(), "|,; ");
	}

	@Override
	public String formatFieldValue(Object value) {
		if (value == null)
			return "";

		Option item = this.getDicOption(value);
		if (item != null) {
			return item.getText();
		}
		if (this.getFieldType() == Const.FIELD_TYPE_FK) {
			return value.toString();
		}

		String pattern = getPattern();
		PatternAdapter adapter = Cocit.me().getPatternAdapters().getAdapter(pattern);
		if (adapter != null) {
			return adapter.format(value);
		}

		return ObjectUtil.format(value, getPattern());
	}

	@Override
	public int getMode(String actionID, Object entityObject) {
		int opMode = parseMode(actionID);

		int runtimeMode = 0;
		if (uiCascadings != null) {
			String strValue;
			int defaultMode = 0;
			Object fieldValue;
			List<Integer> modes = new ArrayList();
			for (UiCascading cascading : uiCascadings) {
				fieldValue = ObjectUtil.getValue(entityObject, cascading.propName);
				if (fieldValue == null) {
					strValue = "";
				} else {
					if (fieldValue instanceof Boolean) {
						if (((Boolean) fieldValue))
							strValue = "1";
						else
							strValue = "0";
					} else {
						strValue = fieldValue.toString();
					}
				}

				if (cascading.valueList.contains("*")) {
					defaultMode = cascading.mode;
				} else if (cascading.valueList.contains(strValue)) {
					if (!modes.contains(cascading.mode))
						modes.add(cascading.mode);

				}
			}

			if (modes.size() > 0) {
				for (int mode : modes) {
					if (mode < runtimeMode || runtimeMode == 0) {
						runtimeMode = mode;
					}
				}

			} else {
				if (defaultMode > 0) {
					runtimeMode = defaultMode;
				}
			}
		}

		return FieldModes.mergeMode(opMode, runtimeMode);
	}

	public int parseMode(String actionID) {
		CocEntityService cocEntity = this.getEntity();
		CuiEntityService cuiEntity = cocEntity.getCuiEntity(cocEntity.getUiView());
		if (cuiEntity != null) {
			CocActionService cocAction = cocEntity.getAction(actionID);
			CuiFormService cuiForm = cuiEntity.getCuiForm(cocAction.getUiForm());
			if (cuiForm != null) {
				CuiFormFieldService cuiField = cuiForm.getFormField(this.getFieldName());
				if (cuiField != null && StringUtil.hasContent(cuiField.getMode()))
					return FieldModes.parseMode(cuiField.getMode());
			}
		}

		int ret = splitMode(actionID);
		if (ret > 0) {
			return ret;
		}
		// 创建或编辑时：默认可编辑
		if (actionID.charAt(0) == 'c') {
			return FieldModes.E;
		}
		// 创建或编辑时：默认可编辑
		if (actionID.charAt(0) == 'e') {
			return FieldModes.E;
		}
		// 批量修改时：默认不显示
		if (actionID.startsWith("bu")) {
			return FieldModes.N;
		}
		// 浏览数据时：默认检查模式显示
		if (actionID.charAt(0) == 'v') {
			return FieldModes.S;
		}

		// 检查模式显示
		return FieldModes.S;
	}

	private int splitMode(String actionID) {
		String mode = entityData.getMode();
		if (actionID == null || actionID.trim().length() == 0) {
			actionID = "v";
		}
		if (mode == null || mode.trim().length() == 0) {
			return 0;
		}
		mode = mode.trim();
		String[] dataModes = StringUtil.toArray(mode, " ");
		String defaultActionMode = "*";
		int defaultMode = 0;
		for (String dataMode : dataModes) {
			if (dataMode == null) {
				continue;
			}
			dataMode = dataMode.trim();
			int index = dataMode.indexOf(":");
			if (index < 0) {
				continue;
			}
			String actMode = dataMode.substring(0, index);
			if (actMode.equals(actionID)) {
				return FieldModes.parseMode(dataMode.substring(index + 1));
			}
			if (defaultActionMode.equals(actMode)) {
				defaultMode = FieldModes.parseMode(dataMode.substring(index + 1));
			}
		}

		return defaultMode;
	}

	@Override
	public CocEntityService getFkTargetEntity() {
		if (fkTargetEntity == null) {
			initFkTargetEntity();
		}
		return fkTargetEntity;
	}

	@Override
	public CocEntityService getOneToManyTargetEntity() {
		if (oneToManyTargetEntity == null) {
			initManyTargetEntity();
		}
		return oneToManyTargetEntity;
	}

	@Override
	public boolean isFkField() {
		return entityData.getFieldType() == Const.FIELD_TYPE_FK;
	}

	@Override
	public boolean isNumber() {
		int type = entityData.getFieldType();

		return type == Const.FIELD_TYPE_FLOAT || //
		        type == Const.FIELD_TYPE_DOUBLE || //
		        type == Const.FIELD_TYPE_BYTE || //
		        type == Const.FIELD_TYPE_SHORT || //
		        type == Const.FIELD_TYPE_INTEGER || //
		        type == Const.FIELD_TYPE_LONG || //
		        type == Const.FIELD_TYPE_NUMBER || //
		        type == Const.FIELD_TYPE_DECIMAL //
		;
	}

	@Override
	public boolean isBoolean() {
		return entityData.getFieldType() == Const.FIELD_TYPE_BOOLEAN;
	}

	@Override
	public CocEntityService getEntity() {
		return cocEntity;
	}

	@Override
	public CocGroupService getGroup() {
		return cocGroup;
	}

	// private String getMode(String actionMode, boolean mustPriority, String defalutMode) {
	// String groupMode = this.getFieldGroupService().getModeByAction(actionMode);
	// String mode = getMode();
	// String ret = moduleEngine.parseMode(actionMode, mode);
	// if (ret == null || ret.length() == 0) {
	// if (groupMode != null && groupMode.length() > 0) {
	// return groupMode;
	// }
	// }
	// if (mustPriority && ret.indexOf("M") > -1) {
	// return "M";
	// }
	// if (!ret.equals("M")) {
	// ret = ret.replace("M", "");
	// }
	// if (ret.length() > 0) {
	// return ret;
	// }
	// // 创建或编辑时：默认可编辑
	// if (actionMode.startsWith("c") || actionMode.startsWith("e")) {
	// if (StringUtil.isBlank(defalutMode))
	// return "E";
	// else
	// return defalutMode;
	// }
	// // 批量修改时：默认不显示
	// if (actionMode.startsWith("bu")) {
	// if (StringUtil.isBlank(defalutMode))
	// return "N";
	// else
	// return defalutMode;
	// }
	// // 浏览数据时：默认检查模式显示
	// if (actionMode.startsWith("v")) {
	// if (StringUtil.isBlank(defalutMode))
	// return "S";
	// else
	// return defalutMode;
	// }
	//
	// // 检查模式显示
	// return "S";
	// }

	@Override
	public boolean isText() {

		if (getFieldType() == Const.FIELD_TYPE_TEXT) {
			return true;
		}

		if (this.isString()) {
			int len = getLength();
			if (len == 0) {
				Integer p = this.getPrecision();
				if (p != null)
					len = p.intValue();
			}
			if (len >= Const.STRING_BOX_MIN_LENGTH) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isString() {
		return getFieldType() == Const.FIELD_TYPE_STRING;
	}

	@Override
	public boolean isRichText() {
		return getFieldType() == Const.FIELD_TYPE_RICHTEXT;
	}

	@Override
	public boolean isManyToOne() {
		if (fkManyToOneField == null) {
			Class cls = getClassOfField();
			if (cls != null)
				fkManyToOneField = cls.getAnnotation(Entity.class) != null;
			else
				fkManyToOneField = false;
		}

		return fkManyToOneField;
	}

	@Override
	public boolean isOneToMany() {
		return getFieldType() == Const.FIELD_TYPE_ONE2MANY;
	}

	@Override
	public Class getClassOfField() {
		if (classOfField == null) {
			Class classOfEntity = this.cocEntity.getClassOfEntity();
			Mirror mirror = Mirror.me(classOfEntity);
			Field field;
			try {
				field = mirror.getField(this.getFieldName());
				classOfField = field.getType();
			} catch (NoSuchFieldException e) {
				try {
					Method m = mirror.getGetter(this.getFieldName());
					classOfField = m.getReturnType();
				} catch (NoSuchMethodException e1) {
					LogUtil.error("EntityFieldService.getClassOfField: Error! %s", e);
				}
			}

		}

		return classOfField;
	}

	@Override
	public String getFullPropName() {
		if (StringUtil.isBlank(fullPropName)) {
			Class classOfField = getClassOfField();

			fullPropName = this.getFieldName();
			if (IDataEntity.class.isAssignableFrom(classOfField)) {
				fullPropName = this.getFieldName() + "." + getFkTargetFieldKey();
			}
		}

		return fullPropName;
	}

	@Override
	public boolean isDic() {
		return this.getDicOptionsArray().length > 0;
	}

	/*
	 * 解析：uiCascading="field1={*:mode1, value1|value2:mode2}; field2={*:mode3, value3|value4:mode4}; ..."
	 */
	private void initUiCascadings() {
		String uiCascading = this.getUiCascading();
		if (StringUtil.isBlank(uiCascading)) {
			return;
		}

		String field, value, mode, exprString;
		List<String> pairList;
		int idx;

		/*
		 * 解析：field1={*:mode1, value1|value2:mode2}; field2={*:mode3, value3|value4:mode4}; ...
		 */
		List<String> cascadingList = StringUtil.toList(uiCascading, ";");
		if (cascadingList == null || cascadingList.size() == 0) {
			return;
		}

		this.uiCascadings = new ArrayList();

		for (String cascading : cascadingList) {
			idx = cascading.indexOf("=");
			if (idx <= 0)
				continue;

			field = cascading.substring(0, idx);

			// 解析： {*:mode1, value1|value2:mode2}
			exprString = cascading.substring(idx + 1).trim();
			if (!exprString.startsWith("{") || !exprString.endsWith("}"))
				continue;
			exprString = exprString.substring(1, exprString.length() - 1);

			// 解析： *:mode1, value1|value2:mode2
			pairList = StringUtil.toList(exprString, ",");
			if (pairList == null || pairList.size() == 0)
				continue;

			for (String pair : pairList) {
				idx = pair.indexOf(":");
				if (idx <= 0)
					continue;

				value = pair.substring(0, idx);
				mode = pair.substring(idx + 1);

				uiCascadings.add(new UiCascading(field, value, mode));
			}

		}
	}

	private static class UiCascading {
		private String propName;

		private List<String> valueList;

		private int mode;

		private UiCascading(String p, String v, String m) {
			this.propName = p;
			this.valueList = StringUtil.toList(v, "|");
			mode = FieldModes.parseMode(m);
		}

		public String toString() {
			return propName + "：" + StringUtil.toString(valueList) + "：" + mode;
		}

	}

	@Override
	public String getGenerator() {
		return this.entityData.getGenerator();
	}

	@Override
	public boolean isDbColumnNotNull() {
		return this.entityData.isDbColumnNotNull();
	}

	@Override
	public String getFkComboWhere() {
		return this.entityData.getFkComboWhere();
	}

	@Override
	public String getFkComboUrl() {
		return this.entityData.getFkComboUrl();
	}
}
