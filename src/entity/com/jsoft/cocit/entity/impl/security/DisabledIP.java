package com.jsoft.cocit.entity.impl.security;

import java.util.Date;

import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.security.IDisabledIP;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;
import com.jsoft.cocit.entityengine.annotation.Cui;
import com.jsoft.cocit.entityengine.annotation.CuiEntity;
import com.jsoft.cocit.entityengine.annotation.CuiGrid;

@Entity
@CocEntity(name = "IP屏蔽管理", key = Const.TBL_SEC_DISABLEDIP, sn = 15, uniqueFields = Const.FLIST_TENANT_KEYS, indexFields = Const.FLIST_TENANT_KEYS,//
           actions = {
                   //
                   @CocAction(name = "编辑", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v"), //
                   @CocAction(name = "取消屏蔽", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v1"), //
           },// end actions
           groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "名称", field = "name"),//
                   @CocColumn(name = "编号", field = "key"),//
                   @CocColumn(name = "IP地址", field = "ipaddress", length = 64),//
                   @CocColumn(name = "屏蔽时间", field = "disabledDate"),//
                   @CocColumn(name = "屏蔽原因", field = "disabledReason"),//
           }) // end group
           }// end groups
)
@Cui({ @CuiEntity( //
                  grid = @CuiGrid(fields = "ipaddress|disabledDate|disabledReason")) //
})
public class DisabledIP extends NamedEntity implements IDisabledIP {
	protected String ipaddress;
	protected Date disabledDate;
	protected String disabledReason;

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Date getDisabledDate() {
		return disabledDate;
	}

	public void setDisabledDate(Date disabledDate) {
		this.disabledDate = disabledDate;
	}

	public String getDisabledReason() {
		return disabledReason;
	}

	public void setDisabledReason(String disabledReason) {
		this.disabledReason = disabledReason;
	}
}
