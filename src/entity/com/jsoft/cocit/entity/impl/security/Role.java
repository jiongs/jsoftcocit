package com.jsoft.cocit.entity.impl.security;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.TenantOwnerNamedEntity;
import com.jsoft.cocit.entity.security.IRole;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;
import com.jsoft.cocit.entityengine.annotation.Cui;
import com.jsoft.cocit.entityengine.annotation.CuiEntity;
import com.jsoft.cocit.entityengine.annotation.CuiGrid;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
@Entity
@CocEntity(name = "角色管理", key = Const.TBL_SEC_ROLE, sn = 6, //
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
           }//
           , groups = {//
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "角色名称", propName = "name", mode = "c:M e:M"),//
                   @CocColumn(name = "角色编码", propName = "key", mode = "c:M e:M") //
                   , @CocColumn(name = "上级", field = "parentKey", fkTargetEntity = Const.TBL_SEC_ROLE, nullable = false) //
                   , @CocColumn(name = "上级名称", field = "parentName", fkTargetEntity = Const.TBL_SEC_ROLE, fkTargetField = "name", fkDependField = "parentKey", mode = "*:N v:P") //
                   , @CocColumn(name = "序号", field = "sn", mode = "*:N v:P") //
                     }) // end group
           }// end groups
)
@Cui({ @CuiEntity( //
                  grid = @CuiGrid(fields = "name|key")) //
})
public class Role extends TenantOwnerNamedEntity implements IRole {

	@Column(length = 64, nullable = false)
	protected String parentKey = "";

	@Column(length = 128)
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
}
