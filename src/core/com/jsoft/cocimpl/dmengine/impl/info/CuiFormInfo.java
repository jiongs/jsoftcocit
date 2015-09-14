package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.baseentity.cui.ICuiFormAction;
import com.jsoft.cocit.baseentity.cui.ICuiFormEntity;
import com.jsoft.cocit.baseentity.cui.ICuiFormFieldEntity;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.dmengine.info.ICuiEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormActionInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormFieldInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormInfo;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.StringUtil;

public class CuiFormInfo extends NamedEntityInfo<ICuiFormEntity>implements ICuiFormInfo {

	private ICuiEntityInfo					cuiEntity;
	private Map<String, ICuiFormFieldInfo>	cuiFieldMap;
	private Map<String, ICuiFormActionInfo>	cuiActionMap;
	private Map<String, Integer>			fieldModes;

	CuiFormInfo(ICuiFormEntity obj, ICuiEntityInfo cuiEntity) {
		super(obj);
		this.cuiEntity = cuiEntity;
	}

	public void release() {
		super.release();
		this.cuiEntity = null;
		if (cuiFieldMap != null) {
			for (ICuiFormFieldInfo f : cuiFieldMap.values()) {
				f.release();
			}
			cuiFieldMap.clear();
			cuiFieldMap = null;
		}
		if (cuiActionMap != null) {
			for (ICuiFormActionInfo f : cuiActionMap.values()) {
				f.release();
			}
			cuiActionMap.clear();
			cuiActionMap = null;
		}
	}

	@Override
	public ICuiFormFieldInfo getFormField(String fieldName) {
		if (cuiFieldMap == null || cuiFieldMap.size() == 0) {
			List<ICuiFormFieldEntity> fields = (List<ICuiFormFieldEntity>) orm().query(EntityTypes.CuiFormField,
			        Expr.eq(Const.F_COC_ENTITY_CODE, this.getCocEntityCode())//
			                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, this.getCuiEntityCode()))//
			                .and(Expr.eq(Const.F_CUI_FORM_CODE, this.getCode()))//
			);
			if (fields != null) {
				cuiFieldMap = new HashMap();
				for (ICuiFormFieldEntity field : fields) {
					cuiFieldMap.put(field.getCode(), new CuiFormFieldInfo(field, this));
				}
			}
		}
		return this.cuiFieldMap.get(fieldName);
	}

	@Override
	public List<List<String>> getFieldsList() {
		return this.parseFieldsList(this.getFields());
	}

	@Override
	public List<List<String>> getGroup1FieldsList() {
		return this.parseFieldsList(this.getGroup1Fields());
	}

	@Override
	public List<List<String>> getGroup2FieldsList() {
		return this.parseFieldsList(this.getGroup2Fields());
	}

	@Override
	public List<List<String>> getGroup3FieldsList() {
		return this.parseFieldsList(this.getGroup3Fields());
	}

	@Override
	public List<List<String>> getGroup4FieldsList() {
		return this.parseFieldsList(this.getGroup4Fields());
	}

	@Override
	public List<List<String>> getGroup5FieldsList() {
		return this.parseFieldsList(this.getGroup5Fields());
	}

	private List<List<String>> parseFieldsList(String str) {
		List<List<String>> ret = new ArrayList();

		List<String> rowsList;
		List<String> groupsList = StringUtil.toList(str, ";");
		for (String group : groupsList) {
			rowsList = StringUtil.toList(group, ",");
			for (String row : rowsList) {
				ret.add(StringUtil.toList(row, "|"));
			}
		}

		return ret;
	}

	@Override
	public List<String> getBatchFieldsList() {
		return StringUtil.toList(this.getBatchFields(), "|");
	}

	@Override
	public List<String> getActionsList() {
		return StringUtil.toList(getActions());
	}

	@Override
	public ICuiFormActionInfo getFormAction(String action) {
		if (cuiActionMap == null) {
			cuiActionMap = new HashMap();
			List<ICuiFormAction> list = (List<ICuiFormAction>) orm().query(EntityTypes.CuiFormAction,
			        Expr.eq(Const.F_COC_ENTITY_CODE, this.getCocEntityCode())//
			                .and(Expr.eq(Const.F_CUI_ENTITY_CODE, this.getCuiEntityCode()))//
			                .and(Expr.eq(Const.F_CUI_FORM_CODE, this.getCode()))//
			);
			if (list != null) {
				for (ICuiFormAction obj : list) {
					cuiActionMap.put(obj.getCode(), new CuiFormActionInfo(obj, this));
				}
			}
		}
		return this.cuiActionMap.get(action);
	}

	@Override
	public ICuiEntityInfo getCuiEntity() {
		return this.cuiEntity;
	}

	@Override
	public String getCocEntityCode() {
		return entityData.getCocEntityCode();
	}

	@Override
	public String getCuiEntityCode() {
		return entityData.getCuiEntityCode();
	}

	@Override
	public String getFields() {
		return entityData.getFields();
	}

	@Override
	public byte getFieldLabelPos() {
		return entityData.getFieldLabelPos();
	}

	@Override
	public String getStyle() {
		return entityData.getStyle();
	}

	@Override
	public String getStyleClass() {
		return entityData.getStyleClass();
	}

	@Override
	public String getActions() {
		return entityData.getActions();
	}

	@Override
	public String getBatchFields() {
		return entityData.getBatchFields();
	}

	@Override
	public String getActionsPos() {
		return entityData.getActionsPos();
	}

	@Override
	public String getPrevInterceptors() {
		return entityData.getPrevInterceptors();
	}

	@Override
	public String getPostInterceptors() {
		return entityData.getPostInterceptors();
	}

	@Override
	public String getUiView() {
		return entityData.getUiView();
	}

	@Override
	public Map<String, Integer> getFieldModes() {
		if (fieldModes != null) {
			return fieldModes;
		}
		fieldModes = new HashMap();

		List<List<String>> fieldsList = this.getFieldsList();
		String fld, mode;
		for (List<String> fields : fieldsList) {
			for (String field : fields) {
				int dot = field.indexOf(":");
				if (dot > 0) {
					fld = field.substring(0, dot);
					mode = field.substring(dot + 1);
					fieldModes.put(fld, FieldModes.parseMode(mode));
				}
			}
		}

		return fieldModes;
	}

	@Override
	public String getGroup1Fields() {
		return entityData.getGroup1Fields();
	}

	@Override
	public String getGroup2Fields() {
		return entityData.getGroup2Fields();
	}

	@Override
	public String getGroup3Fields() {
		return entityData.getGroup3Fields();
	}

	@Override
	public String getGroup4Fields() {
		return entityData.getGroup4Fields();
	}

	@Override
	public String getGroup5Fields() {
		return entityData.getGroup5Fields();
	}

	@Override
	public String getGroup1Name() {
		return entityData.getGroup1Name();
	}

	@Override
	public String getGroup2Name() {
		return entityData.getGroup2Name();
	}

	@Override
	public String getGroup3Name() {
		return entityData.getGroup3Name();
	}

	@Override
	public String getGroup4Name() {
		return entityData.getGroup4Name();
	}

	@Override
	public String getGroup5Name() {
		return entityData.getGroup5Name();
	}
}
