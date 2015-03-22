package com.jsoft.cocit.entity.impl.log;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.log.IOperationLog;
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
@CocEntity(name = "系统操作日志", key = Const.TBL_LOG_OPERATION, sn = 2, actions = {
//
        @CocAction(name = "清空", opCode = OpCodes.OP_CLEAR, key = "clr") //
        , @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v") //
        , @CocAction(name = "数据恢复", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e1") //
           }//
           , groups = { @CocGroup(name = "基本信息", key = "basic"//
                                  , fields = {
                                          //
                                          @CocColumn(name = "操作IP", field = "opIP", mode = "c:M e:M") //
                                          , @CocColumn(name = "操作帐号", field = "opUser") //
                                          , @CocColumn(name = "用户名称", field = "opUserName")//
                                          , @CocColumn(name = "操作时间", field = "opDate")//
                                          , @CocColumn(name = "操作菜单", field = "opMenu")//
                                          , @CocColumn(name = "操作编号", field = "opCode")//
                                          , @CocColumn(name = "操作名称", field = "opName")//
                                          , @CocColumn(name = "操作数据", field = "opData")//
                                          , @CocColumn(name = "操作前数据", field = "oldData") //
                                  }) }// end groups
)
public class OperationLog implements IOperationLog {

	@Id
	@Column(name = "id_")
	protected Long id;

	@Column(name = "key_", length = 64)
	protected String opIP;
	protected String opUser;
	protected String opUserName;
	protected String opDate;
	protected String opMenu;
	protected String opCode;
	protected String opName;
	protected String opData;
	protected String oldData;
}
