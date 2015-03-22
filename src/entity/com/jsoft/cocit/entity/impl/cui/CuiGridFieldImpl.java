package com.jsoft.cocit.entity.impl.cui;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.cui.IExtCuiGridField;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

@Entity
@CocEntity(name = "定义GRID字段", key = Const.TBL_CUI_GRIDFIELD, sn = 3, dbEncoding = true, uniqueFields = "cocEntityKey,cuiEntityKey,cuiGridKey,key", indexFields = "cocEntityKey,cuiEntityKey,cuiGridKey,key"//
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
                   @CocColumn(name = "所属GRID", field = "cuiGridKey", fkTargetEntity = Const.TBL_CUI_FORM), //
                   @CocColumn(name = "字段名称", field = "name"), //
                   @CocColumn(name = "JAVA属性", field = "key"), //
                   @CocColumn(name = "列宽", field = "width"), //
                   @CocColumn(name = "对齐方式", field = "align", length = 10), //
                   @CocColumn(name = "字段视图", field = "cellView"), //
                   @CocColumn(name = "字段链接地址", field = "cellViewLinkUrl", length = 200), //
                   @CocColumn(name = "字段链接目标", field = "cellViewLinkTarget"), //
           }) // end group
           }// end groups
)
public class CuiGridFieldImpl extends NamedEntity implements IExtCuiGridField {

	private String cocEntityKey;
	private String cuiEntityKey;
	private String cuiGridKey;
	private int width;
	private String align;
	private String halign;
	private String cellView;
	private String cellViewLinkUrl;
	private String cellViewLinkTarget;
	@Column(length = 512)
	private String cellStyleRule;
	private boolean hidden;
	private boolean showCellTips;

	public void release() {
		super.release();

		this.cocEntityKey = null;
		this.cuiEntityKey = null;
		this.cuiGridKey = null;
		this.width = 0;
		this.align = null;
		this.cellView = null;
		this.cellViewLinkUrl = null;
		this.cellViewLinkTarget = null;
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

	public String getCuiGridKey() {
		return cuiGridKey;
	}

	public void setCuiGridKey(String cuiGridKey) {
		this.cuiGridKey = cuiGridKey;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getCellView() {
		return cellView;
	}

	public void setCellView(String cellView) {
		this.cellView = cellView;
	}

	public String getCellViewLinkTarget() {
		return cellViewLinkTarget;
	}

	public void setCellViewLinkTarget(String cellViewLinkTarget) {
		this.cellViewLinkTarget = cellViewLinkTarget;
	}

	@Override
	public String getFieldName() {
		return getKey();
	}

	@Override
	public void setFieldName(String fieldName) {
		setKey(fieldName);
	}

	public String getCellViewLinkUrl() {
		return cellViewLinkUrl;
	}

	public void setCellViewLinkUrl(String cellViewFkAction) {
		this.cellViewLinkUrl = cellViewFkAction;
	}

	public String getCellStyleRule() {
		return cellStyleRule;
	}

	public void setCellStyleRule(String cellStyleRule) {
		this.cellStyleRule = cellStyleRule;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isShowCellTips() {
		return showCellTips;
	}

	public void setShowCellTips(boolean showCellTips) {
		this.showCellTips = showCellTips;
	}

	public String getHalign() {
		return halign;
	}

	public void setHalign(String halign) {
		this.halign = halign;
	}

}
