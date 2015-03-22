package com.jsoft.cocit.entity.impl.cui;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.cui.IExtCuiFormField;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

@Entity
@CocEntity(name = "定义表单字段", key = Const.TBL_CUI_FORMFIELD, sn = 5, dbEncoding = true, uniqueFields = "cocEntityKey,cuiEntityKey,cuiFormKey,key", indexFields = "cocEntityKey,cuiEntityKey,cuiFormKey,key"//
           , actions = {
                   //
                   @CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c"),//
                   @CocAction(name = "编辑", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v") //
           }// end actions
           , groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "所属实体对象", field = "cocEntityKey", fkTargetEntity = Const.TBL_COC_ENTITY, asFilterNode = true), //
                   @CocColumn(name = "所属界面", field = "cuiEntityKey", fkTargetEntity = Const.TBL_CUI_ENTITY), //
                   @CocColumn(name = "所属表单", field = "cuiFormKey", fkTargetEntity = Const.TBL_CUI_FORM), //
                   @CocColumn(name = "字段名称", field = "name"), //
                   @CocColumn(name = "JAVA属性", field = "key"), //
                   @CocColumn(name = "显示模式", field = "mode"), //
                   @CocColumn(name = "字典选项", field = "dicOptions"), //
                   @CocColumn(name = "字段视图", field = "uiView"), //
                   @CocColumn(name = "字段链接地址", field = "uiViewLinkUrl", length = 200), //
                   @CocColumn(name = "字段链接目标", field = "uiViewLinkTarget"), //
                   @CocColumn(name = "标签位置", field = "labelPos", dicOptions = "0:自动,1:左边,2:右边,3:上面,4:下面,5:表头(批处理),127:不显示"), //
                   @CocColumn(name = "横跨列数", field = "colspan"), //
                   @CocColumn(name = "纵跨行数", field = "rowspan"), //
                   @CocColumn(name = "字段样式", field = "style", length = 255), //
                   @CocColumn(name = "字段引用样式", field = "styleClass"), //
                   @CocColumn(name = "OneToMany目标操作", field = "oneToManyTargetAction"), //
                   @CocColumn(name = "外键数据过滤条件", field = "fkComboWhere"), //
                   @CocColumn(name = "外键数据加载地址", field = "fkComboUrl"), //
           }) // end group
           }// end groups
)
public class CuiFormFieldImpl extends NamedEntity implements IExtCuiFormField {

	private String cocEntityKey;
	private String cuiEntityKey;
	private String cuiFormKey;
	private String mode;
	private String uiView;
	private String uiViewLinkUrl;
	private String uiViewLinkTarget;
	private byte labelPos;
	private String style;
	private String styleClass;
	private String oneToManyTargetAction;
	private String align;
	private String halign;
	private String dicOptions;
	private byte colspan;
	private byte rowspan;
	@Column(length = 255)
	private String fkComboWhere;
	@Column(length = 255)
	private String fkComboUrl;

	public void release() {
		super.release();

		this.cocEntityKey = null;
		this.cuiEntityKey = null;
		this.cuiFormKey = null;
		this.mode = null;
		this.uiView = null;
		this.uiViewLinkUrl = null;
		this.uiViewLinkTarget = null;
		this.labelPos = 0;
		this.oneToManyTargetAction = null;
		this.style = null;
		this.styleClass = null;
		this.align = null;
		this.dicOptions = null;
	}

	public String getCocEntityKey() {
		return cocEntityKey;
	}

	public void setCocEntityKey(String cocEntityKey) {
		this.cocEntityKey = cocEntityKey;
	}

	public String getCuiEntityKey() {
		return cuiEntityKey;
	}

	public void setCuiEntityKey(String cuiEntityKey) {
		this.cuiEntityKey = cuiEntityKey;
	}

	public String getCuiFormKey() {
		return cuiFormKey;
	}

	public void setCuiFormKey(String cuiFormKey) {
		this.cuiFormKey = cuiFormKey;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getUiView() {
		return uiView;
	}

	public void setUiView(String uiView) {
		this.uiView = uiView;
	}

	public byte getLabelPos() {
		return labelPos;
	}

	public void setLabelPos(byte labelPos) {
		this.labelPos = labelPos;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getOneToManyTargetAction() {
		return oneToManyTargetAction;
	}

	public void setOneToManyTargetAction(String actionForOneToMany) {
		this.oneToManyTargetAction = actionForOneToMany;
	}

	@Override
	public String getFieldName() {
		return getKey();
	}

	@Override
	public void setFieldName(String field) {
		setKey(field);
	}

	public String getUiViewLinkUrl() {
		return uiViewLinkUrl;
	}

	public void setUiViewLinkUrl(String uiViewAction) {
		this.uiViewLinkUrl = uiViewAction;
	}

	public String getUiViewLinkTarget() {
		return uiViewLinkTarget;
	}

	public void setUiViewLinkTarget(String uiViewLinkTarget) {
		this.uiViewLinkTarget = uiViewLinkTarget;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public byte getColspan() {
		return colspan;
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

	public String getDicOptions() {
		return dicOptions;
	}

	public void setDicOptions(String dicOptions) {
		this.dicOptions = dicOptions;
	}

	public String getHalign() {
		return halign;
	}

	public void setHalign(String halign) {
		this.halign = halign;
	}

	public String getFkComboWhere() {
		return fkComboWhere;
	}

	public void setFkComboWhere(String fkComboWhere) {
		this.fkComboWhere = fkComboWhere;
	}

	public String getFkComboUrl() {
		return fkComboUrl;
	}

	public void setFkComboUrl(String fkComboUrl) {
		this.fkComboUrl = fkComboUrl;
	}
}
