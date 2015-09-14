package com.jsoft.cocit.ui.tag;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.ui.model.control.UIEntity;
import com.jsoft.cocit.ui.model.control.UISearchBox;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.StringUtil;

public class QueryFormTag extends BodyTagSupport {

	private static final long serialVersionUID = 3902335085986034089L;

	protected String fields = null;
	protected String funcExpr = null;
	protected String resultUI = null;

	public int doStartTag() throws JspException {

		UISearchBox model = null;
		UIEntity mainModel = null;

		UIModel uiModel = (UIModel) pageContext.getAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);
		if (uiModel != null && uiModel instanceof UIEntity) {
			mainModel = (UIEntity) uiModel;
		}

		List<String> fieldList = StringUtil.toList(fields);

		Writer out = null;
		try {
			out = pageContext.getOut();

			if (StringUtil.hasContent(funcExpr)) {

				HttpContext httpContext = Cocit.me().getHttpContext();
				if (httpContext == null) {
					Cocit.me().makeHttpContext((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
				}

				OpContext opContext = OpContext.make(funcExpr, null, null);
				if (opContext.getException() != null) {
					throw new JspException(opContext.getException());
				}

				if (fieldList != null && fieldList.size() > 0) {
					model = opContext.getUiModelFactory().getSearchBox(opContext.getSystemMenu(), opContext.getCocEntity(), fieldList);
				} else {
					model = opContext.getUiModelFactory().getSearchBox(opContext.getSystemMenu(), opContext.getCocEntity());
				}

			} else {

				if (fieldList != null && fieldList.size() > 0) {

					OpContext opContext = (OpContext) pageContext.getAttribute(OpContext.REQUEST_KEY_HTTPCOMMAND, PageContext.REQUEST_SCOPE);
					model = Cocit.me().getUiModelFactory().getSearchBox(opContext.getSystemMenu(), opContext.getCocEntity(), fieldList);

				} else if (mainModel != null) {
					model = mainModel.getSearchBox();
				}

			}

			if (model != null) {
				if (StringUtil.hasContent(id)) {
					model.setId(id);
				}

				/*
				 * 处理RESULT UI
				 */
				if (StringUtil.hasContent(resultUI)) {
					model.getResultUI().clear();

					List<String> list = StringUtil.toList(resultUI);
					for (String str : list) {
						model.addResultUI(str);
					}
				} else if (model.getResultUI().size() == 0 && mainModel != null) {
					model.addResultUI(mainModel.getGrid().getId());
				}

				try {
					model.render(pageContext.getOut());
				} catch (Exception e) {
					throw new JspException(e);
				}
			}

			return EVAL_BODY_INCLUDE;

		} catch (Throwable e) {
			try {
				out.write(ExceptionUtil.msg(e));
			} catch (Exception ex) {
				throw new JspException(e);
			}

			return SKIP_BODY;
		}
	}

	public void release() {
		super.release();
		funcExpr = null;
		resultUI = null;
	}

	public String getFuncExpr() {
		return funcExpr;
	}

	public void setFuncExpr(String funcExpr) {
		this.funcExpr = funcExpr;
	}

	public String getResultUI() {
		return resultUI;
	}

	public void setResultUI(String resultUI) {
		this.resultUI = resultUI;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String actions) {
		this.fields = actions;
	}
}
