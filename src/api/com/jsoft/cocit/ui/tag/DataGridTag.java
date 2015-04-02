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
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.ui.UIModelFactory;
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
	protected String modelName;

	public int doStartTag() throws JspException {

		Cocit coc = Cocit.me();
		UIGrid model = null;

		/*
		 * 准备参数
		 */
		if (StringUtil.isBlank(modelName)) {
			modelName = ViewKeys.UI_MODEL_KEY;
		}
		List<String> fieldList = StringUtil.toList(fields);
		List<String> actionList = StringUtil.toList(rowActions);

		/*
		 * 准备 HttpContext
		 */
		HttpContext httpContext = coc.getHttpContext();
		if (httpContext == null) {
			coc.makeHttpContext((HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
		}

		/*
		 * 获取 UIGrid
		 */
		UIModel uiModel = (UIModel) pageContext.getAttribute(modelName, PageContext.REQUEST_SCOPE);
		if (uiModel != null) {
			if (uiModel instanceof UIEntity) {
				model = ((UIEntity) uiModel).getGrid();
			} else if (uiModel instanceof UIGrid) {
				model = (UIGrid) uiModel;
			}
		}

		/*
		 * 准备 OpContext
		 */
		OpContext opContext = (OpContext) pageContext.getAttribute(OpContext.REQUEST_KEY_OPCONTEXT, PageContext.REQUEST_SCOPE);
		if (opContext == null && StringUtil.hasContent(funcExpr)) {
			opContext = OpContext.make(funcExpr, null, null);
		}
		if (opContext != null) {
			UIModelFactory uiFactory = coc.getUiModelFactory();
			if ((fieldList != null && fieldList.size() > 0) || (actionList != null && actionList.size() > 0)) {
				model = uiFactory.getGrid(opContext.getSystemMenu(), opContext.getCocEntity(), fieldList, actionList);
			}

			if (model == null) {
				model = uiFactory.getGrid(opContext.getSystemMenu(), opContext.getCocEntity());
			}
		}

		/*
		 * 
		 */
		Writer out = null;
		try {
			if (model == null) {
				throw new CocException("标记库(coc:datagrid)用法错误！请参见相关文档。");
			}

			out = pageContext.getOut();

			/*
			 * 准备UIGrid参数
			 */
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

			model.render(out);

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

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}
