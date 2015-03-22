package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.entity.cui.ICuiEntity;
import com.jsoft.cocit.entity.cui.ICuiForm;
import com.jsoft.cocit.entity.cui.ICuiGrid;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CuiEntityService;
import com.jsoft.cocit.entityengine.service.CuiFormService;
import com.jsoft.cocit.entityengine.service.CuiGridService;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.StringUtil;

public class CuiEntityServiceImpl extends NamedEntityServiceImpl<ICuiEntity> implements CuiEntityService {

	private CocEntityService cocEntity;
	private Map<String, CuiFormService> cuiFormMap;
	private CuiGridService cuiGrid;

	CuiEntityServiceImpl(ICuiEntity obj, CocEntityService cocEntity) {
		super(obj);
		this.cocEntity = cocEntity;
	}

	public void release() {
		super.release();
		this.cocEntity = null;
		if (cuiFormMap != null) {
			for (CuiFormService obj : cuiFormMap.values()) {
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
	public CuiFormService getCuiForm(String formKey) {
		if (cuiFormMap == null) {
			cuiFormMap = new HashMap();
		}

		CuiFormService ret = cuiFormMap.get(formKey);

		if (ret == null) {
			ICuiForm form = orm().get(EntityTypes.CuiForm, Expr.eq(Const.F_COC_ENTITY_KEY, this.getCocEntityKey())//
			        .and(Expr.eq(Const.F_CUI_ENTITY_KEY, this.getKey()))//
			        .and(Expr.eq(Const.F_KEY, formKey))//
			        );

			if (form != null) {
				ret = new CuiFormServiceImpl(form, this);
				cuiFormMap.put(formKey, ret);
			}
		}

		return ret;
	}

	@Override
	public CuiGridService getCuiGrid() {
		if (cuiGrid == null) {
			ICuiGrid obj = orm().get(EntityTypes.CuiGrid, Expr.eq(Const.F_COC_ENTITY_KEY, this.getCocEntityKey())//
			        .and(Expr.eq(Const.F_CUI_ENTITY_KEY, this.getKey()))//
			        );

			if (obj != null) {
				cuiGrid = new CuiGridServiceImpl(obj, this);
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
	public CocEntityService getCocEntity() {
		return cocEntity;
	}

	@Override
	public String getCocEntityKey() {
		return entityData.getCocEntityKey();
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
