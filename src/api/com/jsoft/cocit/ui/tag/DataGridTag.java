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
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.StringUtil;

public class DataGridTag extends BodyTagSupport {

	private static final long serialVersionUID = 3902335085986034089L;

	protected int width;
	protected int height;
	protected String rowActions = null;
	protected String fields = null;
	protected String funcExpr = null;
	protected String resultUI = null;
	protected String paramUI = null;
	protected String dataUrl = null;

	public int doStartTag() throws JspException {

		UIGrid model = null;
		UIEntity mainModel = null;

		UIModel uiModel = (UIModel) pageContext.getAttribute(ViewKeys.UI_MODEL_KEY, PageContext.REQUEST_SCOPE);
		if (uiModel != null && uiModel instanceof UIEntity) {
			mainModel = (UIEntity) uiModel;
		}

		List<String> fieldList = StringUtil.toList(fields);
		List<String> actionList = StringUtil.toList(rowActions);

		Writer out = null;
		try {
			out = pageContext.getOut();

			if (mainModel == null && StringUtil.hasContent(funcExpr)) {

				HttpContext httpContext = Cocit.me().getHttpContext();
				if (httpContext == null) {
					Cocit.me().makeHttpContext((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
				}

				OpContext opContext = OpContext.make(funcExpr, null, null);
				if (opContext.getException() != null) {
					throw new JspException(opContext.getException());
				}

				if ((fieldList != null && fieldList.size() > 0) || (actionList != null && actionList.size() > 0)) {
					model = opContext.getUiModelFactory().getGrid(opContext.getSystemMenu(), opContext.getCocEntity(), fieldList, actionList);
				} else {
					model = opContext.getUiModelFactory().getGrid(opContext.getSystemMenu(), opContext.getCocEntity());
				}

			} else {

				if ((fieldList != null && fieldList.size() > 0) || (actionList != null && actionList.size() > 0)) {

					OpContext opContext = (OpContext) pageContext.getAttribute(OpContext.OPCONTEXT_REQUEST_KEY, PageContext.REQUEST_SCOPE);
					model = Cocit.me().getUiModelFactory().getGrid(opContext.getSystemMenu(), opContext.getCocEntity(), fieldList, actionList);

				} else {
					model = mainModel.getGrid();
				}

			}

			if (model != null) {
				if (width != 0)
					model.set("width", width);
				if (height != 0)
					model.set("height", height);
				if (StringUtil.hasContent(id)) {
					model.setId(id);
				}
				if (StringUtil.hasContent(dataUrl)) {
					model.setDataLoadUrl(dataUrl);
				}

				/*
				 * 处理 resultUI
				 */
				if (StringUtil.hasContent(resultUI)) {
					model.getResultUI().clear();

					List<String> list = StringUtil.toList(resultUI);
					for (String str : list) {
						model.addResultUI(str);
					}
				}

				/*
				 * 计算 paramUI
				 */
				if (StringUtil.hasContent(paramUI)) {
					model.getParamUI().clear();

					List<String> list = StringUtil.toList(paramUI);
					for (String str : list) {
						model.addParamUI(str);
					}
				}

				try {
					model.render(out);
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getParamUI() {
		return paramUI;
	}

	public void setParamUI(String paramUI) {
		this.paramUI = paramUI;
	}

	public String getRowActions() {
		return rowActions;
	}

	public void setRowActions(String rowActions) {
		this.rowActions = rowActions;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

}
