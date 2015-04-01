package com.jsoft.cocit.ui.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.util.HttpUtil;

/**
 * COC HTML标签：用来输出HTML头信息、COC平台公共的CSS、JS等
 * 
 * @author Ji Yongshan
 * 
 */
public class HtmlTag extends BodyTagSupport {

	private static final long serialVersionUID = 4556876946756571944L;

	protected String funcExpr = null;

	public int doStartTag() throws JspException {

		UIModel uiModel = (UIModel) pageContext.getAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);

		String title = "";
		boolean isAjax = false;

		if (uiModel == null) {

			HttpContext httpContext = Cocit.me().getHttpContext();
			if (httpContext == null) {
				Cocit.me().makeHttpContext((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
			}

		}

		if (uiModel != null) {
			isAjax = uiModel.isAjax();
			title = uiModel.getTitle();
		}

		if (!isAjax) {
			try {
				HttpUtil.renderHTMLHeader(pageContext.getOut(), Cocit.me().getContextPath(), title);
			} catch (IOException e) {
				throw new JspException(e);
			}
		}

		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		UIModel uiModel = (UIModel) pageContext.getAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);
		if (uiModel != null && !uiModel.isAjax()) {
			try {
				HttpUtil.renderHTMLFooter(pageContext.getOut());
			} catch (IOException e) {
				throw new JspException(e);
			}
		}

		return EVAL_PAGE;
	}

	public void release() {
		super.release();
		funcExpr = null;
	}

	public String getFuncExpr() {
		return funcExpr;
	}

	public void setFuncExpr(String funcExpr) {
		this.funcExpr = funcExpr;
	}
}
