package com.jsoft.cocit.entity.impl.coc;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.coc.IExtCocGroup;
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
@CocEntity(name = "定义实体字段组", key = Const.TBL_COC_GROUP, sn = 4, dbEncoding = true, uniqueFields = Const.FLIST_COC_KEYS, indexFields = Const.FLIST_COC_KEYS//
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
                   @CocColumn(name = "实体对象", field = "cocEntityKey", fkTargetEntity = Const.TBL_COC_ENTITY, fkTargetAsParent = true, asFilterNode = true), //
                   @CocColumn(name = "分组名称", field = "name"),//
                   @CocColumn(name = "分组编码", field = "key"), //
                   @CocColumn(name = "序号", field = "sn") //
                     }) // end group
           }// end groups
)
public class CocGroupImpl extends NamedEntity implements IExtCocGroup {

	@Column(length = 64)
	protected String cocEntityKey;

	@Column(length = 255)
	protected String mode;

	protected void toJson(StringBuffer sb) {
		super.toJson(sb);

		this.toJson(sb, "cocEntityKey", cocEntityKey);
		this.toJson(sb, "mode", mode);
	}

	public void release() {
		super.release();

		this.cocEntityKey = null;
		this.mode = null;
	}

	public String getCocEntityKey() {
		return cocEntityKey;
	}

	public void setCocEntityKey(String moduleKey) {
		this.cocEntityKey = moduleKey;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

}
