package com.jsoft.cocit.entity.impl.config;

import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.TreeEntity;
import com.jsoft.cocit.entity.config.IDic;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
@Entity
@CocEntity(name = "字典数据维护", key = Const.TBL_CFG_DIC, sn = 2, actions = {
//
		@CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c")//
		, @CocAction(importFromFile = "EntityAction.data.js") //
}// end actions
, groups = {
//
@CocGroup(name = "基本信息", key = "basic", fields = {
//
		@CocColumn(name = "字典名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "字典编码", propName = "key", mode = "c:M e:M")//
		, @CocColumn(name = "上级字典", propName = "parentKey", mode = "c:E e:E", fkTargetEntity = Const.TBL_CFG_DIC)//
		, @CocColumn(name = "字典序号", propName = "sn", mode = "*:N v:P") //
		, @CocColumn(name = "创建时间", propName = "createdDate", mode = "*:N v:P", pattern = "datetime") //
		, @CocColumn(name = "创建帐号", propName = "createdUser", mode = "*:N v:P") //
		, @CocColumn(name = "修改时间", propName = "updatedDate", mode = "*:N v:P", pattern = "datetime") //
		, @CocColumn(name = "修改帐号", propName = "updatedUser", mode = "*:N v:P") //

}) // end group
}// end groups
)
public class DicImpl extends TreeEntity implements IDic {
}
