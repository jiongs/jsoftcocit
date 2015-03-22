package com.jsoft.cocit.entity.impl.security;

import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.security.IGroup;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;
import com.jsoft.cocit.entityengine.annotation.Cui;
import com.jsoft.cocit.entityengine.annotation.CuiEntity;
import com.jsoft.cocit.entityengine.annotation.CuiGrid;

@Entity
@CocEntity(name = "群组管理", key = Const.TBL_SEC_GROUP, sn = 5, uniqueFields = Const.FLIST_TENANT_KEYS, indexFields = Const.FLIST_TENANT_KEYS,//
           actions = {
                   //
                   @CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c"),//
                   @CocAction(name = "编辑", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v"), //
                   @CocAction(name = "启用", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v1"), //
                   @CocAction(name = "停用", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v2"), //
                   @CocAction(name = "上移", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v3"), //
                   @CocAction(name = "下移", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v4"), //
                   @CocAction(name = "添加用户", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v5"), //
                   @CocAction(name = "移除用户", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v6") //
           },// end actions
           groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "名称", field = "name", mode = "c:M e:M"),//
                   @CocColumn(name = "编号", field = "key", mode = "c:M e:M"),//
                   @CocColumn(name = "上级", field = "parentKey", length = 64, fkTargetEntity = Const.TBL_SEC_GROUP),//
                   @CocColumn(name = "上级名称", field = "parentName", length = 128, fkTargetEntity = Const.TBL_SEC_GROUP, fkTargetField = "name", fkDependField = "parentKey", mode = "*:N v:P"), //
                   @CocColumn(name = "序号", field = "sn", mode = "*:N v:P"),//
           }) // end group
           }// end groups
)
@Cui({ @CuiEntity( //
                  grid = @CuiGrid(fields = "name|key")) //
})
public class Group extends Principal implements IGroup {

	protected String parentKey;
	protected String parentName;

	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public int getPrincipalType() {
		return Const.GROUP_NORMAL;
	}
}
