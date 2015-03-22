package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.entity.cui.ICuiForm;
import com.jsoft.cocit.entity.cui.ICuiFormAction;
import com.jsoft.cocit.entity.cui.ICuiFormField;
import com.jsoft.cocit.entityengine.service.CuiEntityService;
import com.jsoft.cocit.entityengine.service.CuiFormActionService;
import com.jsoft.cocit.entityengine.service.CuiFormFieldService;
import com.jsoft.cocit.entityengine.service.CuiFormService;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.StringUtil;

public class CuiFormServiceImpl extends NamedEntityServiceImpl<ICuiForm> implements CuiFormService {

	private CuiEntityService cuiEntity;
	private Map<String, CuiFormFieldService> cuiFieldMap;
	private Map<String, CuiFormActionService> cuiActionMap;

	CuiFormServiceImpl(ICuiForm obj, CuiEntityService cuiEntity) {
		super(obj);
		this.cuiEntity = cuiEntity;
	}

	public void release() {
		super.release();
		this.cuiEntity = null;
		if (cuiFieldMap != null) {
			for (CuiFormFieldService f : cuiFieldMap.values()) {
				f.release();
			}
			cuiFieldMap.clear();
			cuiFieldMap = null;
		}
		if (cuiActionMap != null) {
			for (CuiFormActionService f : cuiActionMap.values()) {
				f.release();
			}
			cuiActionMap.clear();
			cuiActionMap = null;
		}
	}

	@Override
	public CuiFormFieldService getFormField(String fieldName) {
		if (cuiFieldMap == null) {
			cuiFieldMap = new HashMap();
			List<ICuiFormField> fields = (List<ICuiFormField>) orm().query(EntityTypes.CuiFormField, Expr.eq(Const.F_COC_ENTITY_KEY, this.getCocEntityKey())//
			        .and(Expr.eq(Const.F_CUI_ENTITY_KEY, this.getCuiEntityKey()))//
			        .and(Expr.eq(Const.F_CUI_FORM_KEY, this.getKey()))//
			        );
			if (fields != null) {
				for (ICuiFormField field : fields) {
					cuiFieldMap.put(field.getKey(), new CuiFormFieldServiceImpl(field, this));
				}
			}
		}
		return this.cuiFieldMap.get(fieldName);
	}

	@Override
	public List<List<String>> getFieldsList() {
		List<List<String>> ret = new ArrayList();

		List<String> rowsList;
		List<String> groupsList = StringUtil.toList(this.getFields(), ";");
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
	public CuiFormActionService getFormAction(String action) {
		if (cuiActionMap == null) {
			cuiActionMap = new HashMap();
			List<ICuiFormAction> list = (List<ICuiFormAction>) orm().query(EntityTypes.CuiFormAction, Expr.eq(Const.F_COC_ENTITY_KEY, this.getCocEntityKey())//
			        .and(Expr.eq(Const.F_CUI_ENTITY_KEY, this.getCuiEntityKey()))//
			        .and(Expr.eq(Const.F_CUI_FORM_KEY, this.getKey()))//
			        );
			if (list != null) {
				for (ICuiFormAction obj : list) {
					cuiActionMap.put(obj.getKey(), new CuiFormActionServiceImpl(obj, this));
				}
			}
		}
		return this.cuiActionMap.get(action);
	}

	@Override
	public CuiEntityService getCuiEntity() {
		return this.cuiEntity;
	}

	@Override
	public String getCocEntityKey() {
		return entityData.getCocEntityKey();
	}

	@Override
	public String getCuiEntityKey() {
		return entityData.getCuiEntityKey();
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
}
