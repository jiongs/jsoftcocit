package com.jsoft.cocimpl.entityengine.impl.service;

import com.jsoft.cocit.entity.cui.ICuiGridField;
import com.jsoft.cocit.entityengine.service.CuiGridFieldService;
import com.jsoft.cocit.entityengine.service.CuiGridService;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.StringUtil;

public class CuiGridFieldServiceImpl extends NamedEntityServiceImpl<ICuiGridField> implements CuiGridFieldService {

	private CuiGridServiceImpl cuiGrid;

	CuiGridFieldServiceImpl(ICuiGridField field, CuiGridServiceImpl cuiGrid) {
		super(field);
		this.cuiGrid = cuiGrid;
	}

	@Override
	public CuiGridService getCuiGrid() {
		return this.cuiGrid;
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
	public String getCuiGridKey() {
		return entityData.getCuiGridKey();
	}

	@Override
	public String getFieldName() {
		return entityData.getFieldName();
	}

	@Override
	public int getWidth() {
		return entityData.getWidth();
	}

	@Override
	public String getAlign() {
		return entityData.getAlign();
	}

	@Override
	public String getHalign() {
		return entityData.getHalign();
	}

	@Override
	public String getCellView() {
		return entityData.getCellView();
	}

	@Override
	public String getCellViewLinkTarget() {
		return entityData.getCellViewLinkTarget();
	}

	@Override
	public String getCellViewLinkUrl() {
		return entityData.getCellViewLinkUrl();
	}

	@Override
	public String getCellStyleRule() {
		return entityData.getCellStyleRule();
	}

	@Override
	public StyleRule[] getCellStyles() {
		String strRule = this.getCellStyleRule();

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
	public boolean isHidden() {
		return entityData.isHidden();
	}

	@Override
	public boolean isShowCellTips() {
		return entityData.isShowCellTips();
	}
}
