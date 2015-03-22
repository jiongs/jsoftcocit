package com.jsoft.cocimpl.action;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.mvc.CocEntityParam;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.ui.model.JSPModel;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class WebAction {

	/**
	 * 
	 * @param jspArgs
	 *            JSP路径参数：子目录用冒号分隔，如：visit:index 表示要访问/visit/index.jsp页面。
	 * @param opArgs
	 *            操作参数：表示需要通过JSP页面动态访问指定的模块操作，参数格式为：“moduleID:tableID:operationID”。
	 * @param entityID
	 *            实体ID：表示JSP页面支持动态数据。
	 * @param cocEntityParamNode
	 *            实体参数节点：用来接收HTTP中以entity.开头的参数，这些参数将被注入到实体对象中，继续传递到指定的页面。
	 * @return JSPModel 对象
	 */
	@At(UrlAPI.GET_JSP_PAGE)
	public JSPModel getJspModel(String jspArgs, String opArgs, String entityID, @Param("::entity") CocEntityParam cocEntityParamNode) {
		LogUtil.debug("WebAction.getJspModel... jspArgs=%s, opArgs=%s, entityID=%s", jspArgs, opArgs, entityID);

		HttpContext httpCtx = Cocit.me().getHttpContext();
		OpContext actionContext = OpContext.make(opArgs, entityID, cocEntityParamNode);

		String softContextPath = Cocit.me().getContextPath() + "/" + actionContext.getTenant().getKey().replace('.', '_');
		String jspPath = softContextPath + "" + MVCUtil.makeJspPath(jspArgs);

		JSPModel model = JSPModel.make(httpCtx.getRequest(), httpCtx.getResponse(), jspPath);

		model.put("actionHelper", actionContext);

		LogUtil.debug("WebAction.getJspModel... model=%s", model);

		return model;
	}
}
