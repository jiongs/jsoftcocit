package com.jsoft.cocit.dmengine.command.impl;

import java.util.List;

import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.command.WebCommandInterceptor;
import com.jsoft.cocit.dmengine.info.ICocActionInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.util.ExprUtil;

/**
 * 计算代理操作
 * 
 * @author Ji Yongshan
 * 
 */
public class EvalProxyActionCommand extends WebCommandInterceptor {

	@Override
	public String getName() {
		return CommandNames.COC_SET_PROXY_ACTION;
	}

	@Override
	protected boolean execute(WebCommandContext cmdctx) {
		ICocActionInfo cocAction = cmdctx.getCocAction();
		ICocEntityInfo cocEntity = cmdctx.getCocEntity();
		Object result = cmdctx.getResult();

		if (cocAction == null)
			return false;

		List<String> proxyActions = cocAction.getProxyActionsList();
		if (proxyActions == null || proxyActions.size() == 0) {
			return false;
		}

		for (String actionKey : proxyActions) {
			ICocActionInfo action = cocEntity.getAction(actionKey);
			if (action == null) {
				cmdctx.setException(new CocException("操作不存在！%s", actionKey));
			}
			CndExpr expr = action.getWhere();
			boolean match = false;
			if (result instanceof List) {
				List list = (List) result;
				for (Object obj : list) {
					if (!ExprUtil.match(obj, expr)) {
						match = false;
						break;
					}
				}
			} else {
				match = ExprUtil.match(result, expr);
			}

			if (match) {
				cmdctx.setCocAction(action);
				break;
			}
		}

		return false;
	}

}