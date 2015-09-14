package com.jsoft.cocit.dmengine.command.impl;

import java.util.List;

import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.command.WebCommandInterceptor;
import com.jsoft.cocit.orm.PageResult;

public class QueryCommand extends WebCommandInterceptor {

	@Override
	public String getName() {
		return CommandNames.COC_QUERY;
	}

	@Override
	protected boolean execute(WebCommandContext cmdctx) {

		PageResult pageResult = new PageResult();

		List result = cmdctx.getDataManager().query(cmdctx.getSystemMenu(), cmdctx.getCocEntity(), cmdctx.getCocActionID(), cmdctx.getQueryExpr());
		pageResult.setResult(result);

		int count = cmdctx.getDataManager().count(cmdctx.getSystemMenu(), cmdctx.getCocEntity(), cmdctx.getCocActionID(), cmdctx.getQueryExpr());
		pageResult.setTotalRecord(count);

		cmdctx.setResult(pageResult);

		return false;
	}

}