package com.jsoft.cocit.entity.impl.config;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.config.IDicItem;
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
@CocEntity(name = "字典条目", key = Const.TBL_CFG_DICITEM, sn = 3, actions = {
//
		@CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c")//
		, @CocAction(importFromFile = "EntityAction.data.js") //
}// end actions
, groups = {
//
@CocGroup(name = "基本信息", key = "basic", fields = {
//
		@CocColumn(name = "字典编码", propName = "dicKey", mode = "c:M e:M", fkTargetEntity = Const.TBL_CFG_DIC, fkTargetAsParent = true)//
		, @CocColumn(name = "条目名称", propName = "name", mode = "c:M e:M")//
		, @CocColumn(name = "条目编码", propName = "key", mode = "c:M e:M")//
		, @CocColumn(name = "条目说明", propName = "description", mode = "c:E e:E")//
		, @CocColumn(name = "条目序号", propName = "sn", mode = "*:N v:P") //
		, @CocColumn(name = "创建时间", propName = "createdDate", mode = "*:N v:P", pattern = "datetime") //
		, @CocColumn(name = "创建帐号", propName = "createdUser", mode = "*:N v:P") //
		, @CocColumn(name = "修改时间", propName = "updatedDate", mode = "*:N v:P", pattern = "datetime") //
		, @CocColumn(name = "修改帐号", propName = "updatedUser", mode = "*:N v:P") //

}) // end group
}// end groups
)
public class DicItemImpl extends PreferenceImpl implements IDicItem {
	@Column(length = 64)
	protected String dicKey;

	@Column(length = 128)
	protected String dicName;

	public String getDicKey() {
		return dicKey;
	}

	public void setDicKey(String dicKey) {
		this.dicKey = dicKey;
	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String catalogName) {
		this.dicName = catalogName;
	}
}
