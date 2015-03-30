package com.jsoft.cocit.ui.model.control;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.entityengine.PatternAdapter;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.ui.model.UIControlModel;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

public class UIField extends UIControlModel implements UIFieldModel {

	private CocFieldService fieldService;
	private PatternAdapter adapter;
	private String propName;
	private String align;
	private String halign;
	private int width;
	private String patternName;
	private String pattern;
	private int fieldType;
	private int mode;
	private List<UIField> hiddenChildren;
	private List<UIField> visibleChildren;
	private byte labelPos;
	private String linkUrl;
	private String linkTarget;
	private byte colspan;
	private byte rowspan;
	private Option[] dicOptions;
	private UIForm oneToManyTargetForm;
	private StyleRule[] cellStyles;
	private boolean hidden;
	private boolean showTips;
	private boolean multiple;
	private boolean supportHtml;

	public UIField() {
		super();

		this.visibleChildren = new ArrayList();
		this.hiddenChildren = new ArrayList();
	}

	@Override
	public void release() {
		super.release();

		this.propName = null;
		this.align = null;
		this.patternName = null;
		this.pattern = null;

		for (UIField f : this.hiddenChildren) {
			f.release();
		}
		this.hiddenChildren.clear();
		this.hiddenChildren = null;

		for (UIField f : this.visibleChildren) {
			f.release();
		}
		this.visibleChildren.clear();
		this.visibleChildren = null;

		this.fieldService = null;
		this.adapter = null;
	}

	public String getId() {
		String id = super.getId();
		if (StringUtil.isBlank(id) && fieldService != null) {
			id = "fld_" + fieldService.getId();
		}

		if (StringUtil.hasContent(id)) {
			id = id + "_" + Cocit.me().getHttpContext().getClientUIToken();
		}

		return id;
	}

	public int getMode() {
		return mode;
	}

	public UIField setMode(int mode) {
		this.mode = mode;
		return this;
	}

	public int getFieldType() {
		return fieldType;
	}

	public UIField setFieldType(int type) {
		this.fieldType = type;
		return this;
	}

	public UIField addChild(UIField field) {

		// N：不显示
		if (FieldModes.isN(field.getMode()))
			return this;

		// H：隐藏字段
		if (FieldModes.isH(field.getMode()))
			this.hiddenChildren.add(field);
		else
			this.visibleChildren.add(field);

		return this;
	}

	// public UIField addVisibleChild(UIField field) {
	// this.visibleChildren.add(field);
	// return this;
	// }

	public List<UIField> getVisibleChildren() {
		return visibleChildren;
	}

	public String getPropName() {
		return propName;
	}

	public UIField setPropName(String field) {
		this.propName = field;
		return this;
	}

	public String getAlign() {
		return align;
	}

	public UIField setAlign(String align) {
		this.align = align;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public UIField setWidth(int width) {
		this.width = width;
		return this;
	}

	public String getPattern() {
		return pattern;
	}

	public UIField setPattern(String pattern) {
		this.pattern = pattern;
		return this;
	}

	public CocFieldService getFieldService() {
		return fieldService;
	}

	public UIField setFieldService(CocFieldService fieldService) {
		this.fieldService = fieldService;
		this.title = fieldService.getName();
		this.propName = fieldService.getFieldName();
		this.fieldType = fieldService.getFieldType();
		this.align = "left";
		if (fieldService.isDic()) {
			this.align = "center";
		} else if (fieldService.isNumber()) {
			this.align = "right";
		}
		this.halign = "center";
		this.dicOptions = fieldService.getDicOptionsArray();
		this.adapter = Cocit.me().getPatternAdapters().getAdapter(fieldService.getPattern());
		if (this.adapter != null) {
			this.patternName = adapter.getName();
			this.pattern = adapter.getPattern();
		}

		return this;
	}

	public String format(Object fieldValue) {
		if (fieldValue == null)
			return "";
		if (adapter == null) {
			if (fieldService != null)
				return fieldService.formatFieldValue(fieldValue);
			else
				return fieldValue.toString();
		}

		return adapter.format(fieldValue);
	}

	public void setAdapter(PatternAdapter adapter) {
		this.adapter = adapter;
	}

	public boolean isNewRow() {
		return fieldService.isText() || fieldService.isRichText() || fieldService.isOneToMany();
	}

	@Override
	public Option[] getDicOptions() {
		return dicOptions;
	}

	public void setDicOptions(Option[] dicOptions) {
		this.dicOptions = dicOptions;
	}

	@Override
	public Option getDicOption(Object fieldValue) {
		return fieldService.getDicOption(fieldValue);
	}

	@Override
	public int getScale() {
		return fieldService.getScale();
	}

	@Override
	public String getFkComboTreeUrl() {
		CocEntityService fkTargetModule = fieldService.getFkTargetEntity();
		if (fkTargetModule == null)
			return null;

		UITree fkDataModel = Cocit.me().getUiModelFactory().getComboTree(null, fkTargetModule, fieldService);

		return fkDataModel.getDataLoadUrl();
	}

	public String getFkComboGridUrl() {
		CocEntityService fkTargetModule = fieldService.getFkTargetEntity();
		if (fkTargetModule == null)
			return null;

		UIGrid grid = (UIGrid) Cocit.me().getUiModelFactory().getComboGrid(null, fkTargetModule, fieldService);

		return grid.getDataLoadUrl();
	}

	public String getFkComboListUrl() {
		CocEntityService fkTargetModule = fieldService.getFkTargetEntity();
		if (fkTargetModule == null)
			return null;

		UIList list = Cocit.me().getUiModelFactory().getComboList(null, fkTargetModule, fieldService);

		return list.getDataLoadUrl();
	}

	public PatternAdapter getAdapter() {
		return adapter;
	}

	public String getPatternName() {
		return patternName;
	}

	public void setPatternName(String patternName) {
		this.patternName = patternName;
	}

	public List<UIField> getHiddenChildren() {
		return hiddenChildren;
	}

	public void setLabelPos(byte labelPos) {
		this.labelPos = labelPos;
	}

	public byte getLabelPos() {
		return labelPos;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkTarget(String linkTarget) {
		this.linkTarget = linkTarget;
	}

	public String getLinkTarget() {
		return linkTarget;
	}

	public void setColspan(byte colspan) {
		this.colspan = colspan;
	}

	public byte getRowspan() {
		return rowspan;
	}

	public void setRowspan(byte rowspan) {
		this.rowspan = rowspan;
	}

	public byte getColspan() {
		return colspan;
	}

	public UIForm getOneToManyTargetForm() {
		return oneToManyTargetForm;
	}

	/**
	 * 设置“OneToMany字段”表单
	 */
	public void setOne2ManyTargetForm(UIForm one2ManyForm) {
		this.oneToManyTargetForm = one2ManyForm;
	}

	public void setCellStyles(StyleRule[] styleRules) {
		this.cellStyles = styleRules;
	}

	public StyleRule[] getCellStyles() {
		return cellStyles;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setShowTips(boolean showTips) {
		this.showTips = showTips;
	}

	public boolean isShowTips() {
		return showTips;
	}

	public String getHalign() {
		return halign;
	}

	public void setHalign(String halign) {
		this.halign = halign;
	}

	public void setMultiple(boolean b) {
		multiple = b;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public boolean isSupportHtml() {
		return supportHtml;
	}

	public void setSupportHtml(boolean supportHtml) {
		this.supportHtml = supportHtml;
	}

}
