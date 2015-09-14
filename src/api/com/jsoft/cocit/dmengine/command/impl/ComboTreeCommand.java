package com.jsoft.cocit.dmengine.command.impl;

import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.command.WebCommandInterceptor;
import com.jsoft.cocit.util.Tree;

public class ComboTreeCommand extends WebCommandInterceptor {

	@Override
	public String getName() {
		return CommandNames.COC_COMBOTREE;
	}

	@Override
	protected boolean execute(WebCommandContext cmdctx) {

		Tree data = cmdctx.getCocEntity().getTreeData(cmdctx.getQueryExpr());
		cmdctx.setResult(data);

		return false;
	}

}