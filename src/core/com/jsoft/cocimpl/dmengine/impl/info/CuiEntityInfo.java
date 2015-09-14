package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.baseentity.cui.ICuiEntity;
import com.jsoft.cocit.baseentity.cui.ICuiFormEntity;
import com.jsoft.cocit.baseentity.cui.ICuiGridEntity;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormInfo;
import com.jsoft.cocit.dmengine.info.ICuiGridInfo;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.StringUtil;

public class CuiEntityInfo extends NamedEntityInfo<ICuiEntity> implements ICuiEntityInfo {

	private ICocEntityInfo cocEntity;
	private Map<String, ICuiFormInfo> cuiFormMap;
	private ICuiGridInfo cuiGrid;

	CuiEntityInfo(ICuiEntity obj, ICocEntityInfo cocEntity) {
		super(obj);
		this.cocEntity = cocEntity;
	}

	public void release() {
		super.release();
		this.cocEntity = null;
		if (cuiFormMap != null) {
			for (ICuiFormInfo obj : cuiFormMap.values()) {
				obj.release();
			}
			cuiFormMap.clear();
			cuiFormMap = null;
		}
		if (cuiGrid != null) {
			cuiGrid.release();
			cuiGrid = null;
		}
	}

	@Override
	public ICuiFormInfo getCuiForm(String formCode) {
		if (cuiFormMap == null) {
			cuiFormMap = new HashMap();
		}

		ICuiFormInfo ret = cuiFormMap.get(formCode);

		if (ret == null) {
			ICuiFormEntity form = orm().get(EntityTypes.CuiForm, Expr.eq(Const.F_COC_ENTITY_CODE, this.getCocEntityCode())//
			        .and(Expr.eq(Const.F_CUI_ENTITY_CODE, this.getCode()))//
			        .and(Expr.eq(Const.F_CODE, formCode))//
			        );

			if (form != null) {
				ret = new CuiFormInfo(form, this);
				cuiFormMap.put(formCode, ret);
			}
		}

		return ret;
	}

	@Override
	public ICuiGridInfo getCuiGrid() {
		if (cuiGrid == null) {
			ICuiGridEntity obj = orm().get(EntityTypes.CuiGrid, Expr.eq(Const.F_COC_ENTITY_CODE, this.getCocEntityCode())//
			        .and(Expr.eq(Const.F_CUI_ENTITY_CODE, this.getCode()))//
			        );

			if (obj != null) {
				cuiGrid = new CuiGridInfo(obj, this);
			}
		}

		return cuiGrid;
	}

	@Override
	public List<String> getActionsList() {
		return StringUtil.toList(this.getActions(), "|");
	}

	@Override
	public List<String> getFilterFieldsList() {
		return StringUtil.toList(this.getFilterFields(), "|");
	}

	@Override
	public List<String> getQueryFieldsList() {
		return StringUtil.toList(this.getQueryFields(), "|");
	}

	@Override
	public List<String> getColsList() {
		return StringUtil.toList(this.getCols(), ",");
	}

	@Override
	public List<String> getRowsList() {
		return StringUtil.toList(this.getRows(), ",");
	}

	@Override
	public ICocEntityInfo getCocEntity() {
		return cocEntity;
	}

	@Override
	public String getCocEntityCode() {
		return entityData.getCocEntityCode();
	}

	@Override
	public String getCols() {
		return entityData.getCols();
	}

	@Override
	public String getRows() {
		return entityData.getRows();
	}

	@Override
	public String getFilterFields() {
		return entityData.getFilterFields();
	}

	@Override
	public String getFilterFieldsView() {
		return entityData.getFilterFieldsView();
	}

	@Override
	public String getQueryFields() {
		return entityData.getQueryFields();
	}

	@Override
	public String getQueryFieldsView() {
		return entityData.getQueryFieldsView();
	}

	@Override
	public String getActions() {
		return entityData.getActions();
	}

	@Override
	public String getActionsView() {
		return entityData.getActionsView();
	}

	@Override
	public byte getActionsPos() {
		return entityData.getActionsPos();
	}

	@Override
	public byte getQueryFieldsPos() {
		return entityData.getQueryFieldsPos();
	}

	@Override
	public String getUiView() {
		return entityData.getUiView();
	}

	@Override
    public byte getFilterFieldsPos() {
		return entityData.getFilterFieldsPos();
    }
}
