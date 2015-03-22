package com.jsoft.cocit.ui.model;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.util.MVCUtil;

/**
 * Smarty模版模型： 环境context中可以包含
 * <UL>
 * <LI>actionService: {@link OpContext}对象
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class SmartyModel extends BaseUIModel {

	private String templatePath;

	/**
	 * 
	 * 软件环境路径 = WEB环境路径 + 软件JSP根路径。
	 * <p>
	 * 软件JSP根路径是软件编号替换“.”为“_”后的路径。如软件编号 www.yunnanbaiyao.com.cn 替换后变成 www_yunnanbaiyao_com_cn
	 * <p>
	 * JSP相对路径是相对于软件JSP根路径而言。
	 * 
	 * @param contextPath
	 *            软件环境路径
	 * @param path
	 *            JSP相对路径
	 * @return
	 */
	public static SmartyModel make(HttpServletRequest req, HttpServletResponse resp, String contextPath, String path, Map ctx) {
		SmartyModel ret = new SmartyModel(req, resp, contextPath, path, ctx);

		return ret;
	}

	protected SmartyModel(HttpServletRequest req, HttpServletResponse resp, String contextPath, String path, Map ctx) {
		super();

		this.contextPath = contextPath;
		if (contextPath != null)
			this.templatePath = contextPath + path;
		else
			this.templatePath = path;

		if (!this.templatePath.endsWith(".st")) {
			templatePath += ".st";
		}

		if (ctx != null)
			this.context.putAll(ctx);

		context.put("request", req);
		context.put("response", resp);
		context.put("session", req.getSession());
		context.putAll(MVCUtil.GLOBAL_PARAMS);
	}

	public void release() {
		super.release();
		this.templatePath = null;
	}

	public String getViewName() {
		return ViewNames.VIEW_SMARTY;
	}

	public String getTemplatePath() {
		return templatePath;
	}
}
