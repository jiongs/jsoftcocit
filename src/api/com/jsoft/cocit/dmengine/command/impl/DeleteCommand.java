package com.jsoft.cocit.dmengine.command.impl;

import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.command.WebCommandInterceptor;

public class DeleteCommand extends WebCommandInterceptor {

	@Override
	public String getName() {
		return CommandNames.COC_DELETE;
	}

	@Override
	protected boolean execute(WebCommandContext cmdctx) {

		Object result = cmdctx.getResult();
		cmdctx.getDataManager().delete(cmdctx.getSystemMenu(), cmdctx.getCocEntity(), result, cmdctx.getCocActionID());

		return false;
	}

}
