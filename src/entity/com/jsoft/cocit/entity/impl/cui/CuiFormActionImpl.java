package com.jsoft.cocit.entity.impl.cui;

import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.cui.IExtCuiFormAction;
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
                   @CocColumn(name = "操作名称", field = "name"), //
                   @CocColumn(name = "操作编号", field = "key"), //
                   @CocColumn(name = "操作标题", field = "title"), //
                   @CocColumn(name = "按钮位置", field = "position") //
                     }) // end group
           }// end groups
)
public class CuiFormActionImpl extends NamedEntity implements IExtCuiFormAction {

	private String cocEntityKey;
	private String cuiEntityKey;
	private String cuiFormKey;
	private String title;

	public void release() {
		super.release();

		this.cocEntityKey = null;
		this.cuiEntityKey = null;
		this.cuiFormKey = null;
		this.title = null;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
