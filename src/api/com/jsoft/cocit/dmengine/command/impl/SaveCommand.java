package com.jsoft.cocit.dmengine.command.impl;

import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.command.WebCommandInterceptor;

public class SaveCommand extends WebCommandInterceptor {

	@Override
	public String getName() {
		return CommandNames.COC_SAVE;
	}

	@Override
	protected boolean execute(WebCommandContext cmdctx) {

		Object obj = cmdctx.getDataManager().save(cmdctx.getSystemMenu(), cmdctx.getCocEntity(), cmdctx.getDataObject(), cmdctx.getCocActionID());
		cmdctx.setResult(obj);

		return false;
	}

}