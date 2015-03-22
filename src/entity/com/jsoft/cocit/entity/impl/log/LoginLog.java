package com.jsoft.cocit.entity.impl.log;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.log.ILoginLog;
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
@CocEntity(name = "系统登录日志", key = Const.TBL_LOG_LOGIN, sn = 1, actions = {
//
        @CocAction(name = "清空", opCode = OpCodes.OP_CLEAR, key = "clr") //
        , @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v") //
           }//
           , groups = { @CocGroup(name = "基本信息", key = "basic"//
                                  , fields = {
                                          //
                                          @CocColumn(name = "登录IP", field = "loginIP", mode = "c:M e:M") //
                                          , @CocColumn(name = "浏览器信息", field = "browserInfo") //
                                          , @CocColumn(name = "登录帐号", field = "loginUser")//
                                          , @CocColumn(name = "登录名称", field = "loginName")//
                                          , @CocColumn(name = "登录时间", field = "loginDate")//
                                          , @CocColumn(name = "离开时间", field = "leaveDate")//
                                          , @CocColumn(name = "在线时长", field = "onlineTimes") //
                                  }) }// end groups
)
public class LoginLog implements ILoginLog {

	@Id
	@Column(name = "id_")
	protected Long id;

	@Column(name = "key_", length = 64)
	protected String key;

	protected String loginIP;

	protected String browserInfo;
	protected String loginUser;
	protected String loginName;
	protected String loginDate;
	protected String leaveDate;
	protected String onlineTimes;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
