package com.jsoft.cocit.ui.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.Const;

/**
 * JSP模型： 环境context中可以包含
 * <UL>
 * <LI>actionService: {@link OpContext}对象
 * </UL>
 * 
 * @author yongshan.ji
 * 
 */
public class JSPModel extends BaseUIModel {

	private HttpServletRequest req;

	private HttpServletResponse resp;

	public static JSPModel make(HttpServletRequest req, HttpServletResponse resp, String jspPath) {
		JSPModel ret = new JSPModel(req, resp, jspPath);

		return ret;
	}

	public void release() {
		super.release();
		this.req = null;
		this.resp = null;
	}

	protected JSPModel(HttpServletRequest req, HttpServletResponse resp, String jspPath) {
		super();

		this.req = req;
		this.resp = resp;
		this.viewName = jspPath;
	}

	//
	// public String getViewName() {
	// return ViewNames.VIEW_JSP;
	// }

	public String getContentType() {
		return Const.CONTENT_TYPE_HTML;
	}

	public boolean isCachable() {
		return false;
	}

	public String getJsp() {
		return viewName;
	}

	public <T> T get(String key) {
		return (T) context.get(key);
	}

	public HttpServletRequest getRequest() {
		return req;
	}

	public HttpServletResponse getResponse() {
		return resp;
	}
}
