package com.jsoft.cocit.dmengine.command.impl;

import java.util.List;

import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.command.WebCommandInterceptor;

public class ComboListCommand extends WebCommandInterceptor {

	@Override
	public String getName() {
		return CommandNames.COC_COMBOLIST;
	}

	@Override
	protected boolean execute(WebCommandContext cmdctx) {

		List data = cmdctx.getDataManager().query(cmdctx.getSystemMenu(), cmdctx.getCocEntity(), null, cmdctx.getQueryExpr());

		cmdctx.setResult(data);

		return false;
	}

}