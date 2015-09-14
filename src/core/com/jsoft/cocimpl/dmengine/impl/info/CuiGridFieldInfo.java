package com.jsoft.cocimpl.dmengine.impl.info;

import com.jsoft.cocit.baseentity.cui.ICuiGridFieldEntity;
import com.jsoft.cocit.dmengine.info.ICuiGridFieldInfo;
import com.jsoft.cocit.dmengine.info.ICuiGridInfo;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.StringUtil;

public class CuiGridFieldInfo extends NamedEntityInfo<ICuiGridFieldEntity> implements ICuiGridFieldInfo {

	private CuiGridInfo cuiGrid;

	CuiGridFieldInfo(ICuiGridFieldEntity field, CuiGridInfo cuiGrid) {
		super(field);
		this.cuiGrid = cuiGrid;
	}

	@Override
	public ICuiGridInfo getCuiGrid() {
		return this.cuiGrid;
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
	public String getCuiGridCode() {
		return entityData.getCuiGridCode();
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
