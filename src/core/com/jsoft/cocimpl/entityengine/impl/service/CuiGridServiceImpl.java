package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.entity.cui.ICuiGrid;
import com.jsoft.cocit.entity.cui.ICuiGridField;
import com.jsoft.cocit.entityengine.service.CuiEntityService;
import com.jsoft.cocit.entityengine.service.CuiGridFieldService;
import com.jsoft.cocit.entityengine.service.CuiGridService;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.StringUtil;

public class CuiGridServiceImpl extends DataEntityServiceImpl<ICuiGrid> implements CuiGridService {

	private CuiEntityService cuiEntity;
	private Map<String, CuiGridFieldService> cuiFieldMap;

	CuiGridServiceImpl(ICuiGrid obj, CuiEntityService cuiEntity) {
		super(obj);
		this.cuiEntity = cuiEntity;
	}

	public void release() {
		super.release();
		this.cuiEntity = null;
		if (cuiFieldMap != null) {
			for (CuiGridFieldService f : cuiFieldMap.values()) {
				f.release();
			}
			cuiFieldMap.clear();
			cuiFieldMap = null;
		}
	}

	@Override
	public CuiGridFieldService getField(String fieldName) {
		if (cuiFieldMap == null) {
			cuiFieldMap = new HashMap();
			List<ICuiGridField> fields = (List<ICuiGridField>) orm().query(EntityTypes.CuiGridField, Expr.eq(Const.F_COC_ENTITY_KEY, this.getCocEntityKey())//
			        .and(Expr.eq(Const.F_CUI_ENTITY_KEY, this.getCuiEntityKey()))//
			        .and(Expr.eq(Const.F_CUI_GRID_KEY, this.getKey()))//
			        );
			if (fields != null) {
				for (ICuiGridField field : fields) {
					cuiFieldMap.put(field.getKey(), new CuiGridFieldServiceImpl(field, this));
				}
			}
		}
		return this.cuiFieldMap.get(fieldName);
	}

	@Override
	public List<String> getFrozenFieldsList() {
		return StringUtil.toList(this.getFrozenFields(), "|");
	}

	@Override
	public List<String> getFieldsList() {
		return StringUtil.toList(this.getFields(), "|");
	}

	@Override
	public CuiEntityService getCuiEntity() {
		return this.cuiEntity;
	}

	@Override
	public String getCocEntityKey() {
		return this.entityData.getCocEntityKey();
	}

	@Override
	public String getCuiEntityKey() {
		return this.entityData.getCuiEntityKey();
	}

	@Override
	public String getFrozenFields() {
		return this.entityData.getFrozenFields();
	}

	@Override
	public String getFields() {
		return this.entityData.getFields();
	}

	@Override
	public boolean isRownumbers() {
		return this.entityData.isRownumbers();
	}

	@Override
	public boolean isMultiSelect() {
		return this.entityData.isMultiSelect();
	}

	@Override
	public boolean isSelectOnCheck() {
		return this.entityData.isSelectOnCheck();
	}

	@Override
	public boolean isCheckOnSelect() {
		return this.entityData.isCheckOnSelect();
	}

	@Override
	public byte getPaginationPos() {
		return this.entityData.getPaginationPos();
	}

	@Override
	public String getPaginationActions() {
		return this.entityData.getPaginationActions();
	}

	@Override
	public int getPageIndex() {
		return this.entityData.getPageIndex();
	}

	@Override
	public int getPageSize() {
		return this.entityData.getPageSize();
	}

	@Override
	public String getPageOptions() {
		return this.entityData.getPageOptions();
	}

	@Override
	public String getSortExpr() {
		return this.entityData.getSortExpr();
	}

	@Override
	public boolean isShowHeader() {
		return this.entityData.isShowHeader();
	}

	@Override
	public boolean isShowFooter() {
		return this.entityData.isShowFooter();
	}

	@Override
	public String getRowActions() {
		return this.entityData.getRowActions();
	}

	@Override
	public String getRowActionsView() {
		return this.entityData.getRowActionsView();
	}

	@Override
	public byte getRowActionsPos() {
		return this.entityData.getRowActionsPos();
	}

	@Override
	public String getTreeField() {
		return this.entityData.getTreeField();
	}

	@Override
	public String getRowStyleRule() {
		return this.entityData.getRowStyleRule();
	}

	@Override
	public StyleRule[] getRowStyles() {
		String strRule = this.getRowStyleRule();

		if (StringUtil.isBlank(strRule))
			return null;

		if (strRule.startsWith("[")) {
			return JsonUtil.loadFromJson(StyleRule[].class, strRule);
		} else if (strRule.startsWith("{")) {
			StyleRule rule = JsonUtil.loadFromJson(StyleRule.class, strRule);
			return new StyleRule[] { rule };
		} else {
			StyleRule rule = JsonUtil.loadFromJson(StyleRule.class, "{" + strRule + "}");
			return new StyleRule[] { rule };
		}
	}

	@Override
	public String getUiView() {
		return entityData.getUiView();
	}

}
