package com.jsoft.cocit.entity.impl.cui;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.cui.IExtCuiForm;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

@Entity
@CocEntity(name = "定义表单", key = Const.TBL_CUI_FORM, sn = 4, dbEncoding = true, uniqueFields = "cocEntityKey,cuiEntityKey,key", indexFields = "cocEntityKey,cuiEntityKey,key"//
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
                   @CocColumn(name = "表单名称", field = "name"), //
                   @CocColumn(name = "表单编码", field = "key"), //
                   @CocColumn(name = "字段列表", field = "fields", length = 1000), //
                   @CocColumn(name = "批处理字段", field = "batchFields", length = 1000), //
                   @CocColumn(name = "字段标签位置", field = "fieldLabelPos", dicOptions = "0:自动,1:左边,2:右边,3:上面,4:下面,5:表头(批处理),127:不显示"), //
                   @CocColumn(name = "表单样式", field = "style", length = 255), //
                   @CocColumn(name = "表单引用样式", field = "styleClass"), //
                   @CocColumn(name = "表单操作", field = "actions", length = 255), //
           }) // end group
           }// end groups
)
public class CuiFormImpl extends NamedEntity implements IExtCuiForm {

	private String cocEntityKey;
	private String cuiEntityKey;
	private String fields;
	private String batchFields;
	private byte fieldLabelPos;
	private String style;
	private String styleClass;
	private String actions;
	@Column(length = 16)
	private String actionsPos;

	public void release() {
		super.release();

		this.cocEntityKey = null;
		this.cuiEntityKey = null;
		this.fields = null;
		this.batchFields = null;
		this.fieldLabelPos = 0;
		this.style = null;
		this.styleClass = null;
		this.actions = null;
		this.actionsPos = null;
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

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public byte getFieldLabelPos() {
		return fieldLabelPos;
	}

	public void setFieldLabelPos(byte fieldLabelPos) {
		this.fieldLabelPos = fieldLabelPos;
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

	public String getActions() {
		return actions;
	}

	public void setActions(String steps) {
		this.actions = steps;
	}

	public String getBatchFields() {
		return batchFields;
	}

	public void setBatchFields(String batchFields) {
		this.batchFields = batchFields;
	}

	public String getActionsPos() {
		return actionsPos;
	}

	public void setActionsPos(String actionsPos) {
		this.actionsPos = actionsPos;
	}
}
