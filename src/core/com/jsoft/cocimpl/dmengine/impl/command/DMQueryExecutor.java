package com.jsoft.cocimpl.dmengine.impl.command;

import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.command.WebCommandInterceptor;

/**
 * DataManager Query Receiver: 数据管理器“查询命令”接收器
 * 
 * @author Ji Yongshan
 * 
 */
public class DMQueryExecutor extends WebCommandInterceptor {

	@Override
	public String getName() {
		return CommandNames.COC_SAVE;
	}

	@Override
	protected boolean execute(WebCommandContext command) {
		command.getDataManager().query(command.getSystemMenu(), command.getCocEntity(), null, command.getQueryExpr());
		return true;
	}

}
