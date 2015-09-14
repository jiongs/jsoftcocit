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
	protected String name;

	protected boolean rownumbers;
	protected boolean checkbox;
	protected boolean singleSelect;
	protected boolean pageSize;
	protected boolean pageIndex;
	protected boolean pageOptions;
	protected boolean pagination;
	protected boolean selectOnCheck;
	protected boolean checkOnSelect;
	protected boolean showFooter;
	protected boolean showHeader;
	protected boolean sortExpr;

	protected String onBeforeLoad;
	protected String onSelect;
	protected String onUnselect;
	protected String onUnselectAll;
	protected String onCheck;
	protected String onUncheck;
	protected String onCheckAll;
	protected String onUncheckAll;
	protected String onHeaderContextMenu;
	protected String onDblClickRow;

	public int doStartTag() throws JspException {

		Cocit coc = Cocit.me();
		UIGrid model = null;

		/*
		 * 准备参数
		 */
		List<String> fieldList = null;
		if (fields != null)
			fieldList = StringUtil.toList(fields);
		List<String> actionList = null;
		if (rowActions != null)
			actionList = StringUtil.toList(rowActions);

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
		if (StringUtil.isBlank(modelName)) {
			modelName = ViewKeys.UI_MODEL_KEY;
		}
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
		OpContext opContext;
		if (StringUtil.hasContent(funcExpr)) {
			opContext = OpContext.make(funcExpr, null, null);
		} else {
			opContext = (OpContext) pageContext.getAttribute(OpContext.REQUEST_KEY_HTTPCOMMAND, PageContext.REQUEST_SCOPE);
		}
		if (opContext != null) {
			UIModelFactory uiFactory = coc.getUiModelFactory();
			if (fieldList != null || actionList != null) {
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
			if (StringUtil.hasContent(name)) {
				model.putAttribute("name", name);
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
			if(this.onBeforeLoad!=null){
				model.set("onBeforeLoad", onBeforeLoad);
			}
			if(this.onSelect!=null){
				model.set("onSelect", onSelect);
			}
			if(this.onUnselect!=null){
				model.set("onUnselect", onUnselect);
			}
			if(this.onUnselectAll!=null){
				model.set("onUnselectAll", onUnselectAll);
			}
			if(this.onCheck!=null){
				model.set("onCheck", onCheck);
			}
			if(this.onUncheck!=null){
				model.set("onUncheck", onUncheck);
			}
			if(this.onCheckAll!=null){
				model.set("onCheckAll", onCheckAll);
			}
			if(this.onDblClickRow!=null){
				model.set("onDblClickRow", onDblClickRow);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRownumbers() {
		return rownumbers;
	}

	public void setRownumbers(boolean rownumbers) {
		this.rownumbers = rownumbers;
	}

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public boolean isSingleSelect() {
		return singleSelect;
	}

	public void setSingleSelect(boolean singleSelect) {
		this.singleSelect = singleSelect;
	}

	public boolean isPageSize() {
		return pageSize;
	}

	public void setPageSize(boolean pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(boolean pageIndex) {
		this.pageIndex = pageIndex;
	}

	public boolean isPageOptions() {
		return pageOptions;
	}

	public void setPageOptions(boolean pageOptions) {
		this.pageOptions = pageOptions;
	}

	public boolean isPagination() {
		return pagination;
	}

	public void setPagination(boolean pagination) {
		this.pagination = pagination;
	}

	public boolean isSelectOnCheck() {
		return selectOnCheck;
	}

	public void setSelectOnCheck(boolean selectOnCheck) {
		this.selectOnCheck = selectOnCheck;
	}

	public boolean isCheckOnSelect() {
		return checkOnSelect;
	}

	public void setCheckOnSelect(boolean checkOnSelect) {
		this.checkOnSelect = checkOnSelect;
	}

	public boolean isShowFooter() {
		return showFooter;
	}

	public void setShowFooter(boolean showFooter) {
		this.showFooter = showFooter;
	}

	public boolean isShowHeader() {
		return showHeader;
	}

	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

	public boolean isSortExpr() {
		return sortExpr;
	}

	public void setSortExpr(boolean sortExpr) {
		this.sortExpr = sortExpr;
	}

	public String getOnBeforeLoad() {
		return onBeforeLoad;
	}

	public void setOnBeforeLoad(String onBeforeLoad) {
		this.onBeforeLoad = onBeforeLoad;
	}

	public String getOnSelect() {
		return onSelect;
	}

	public void setOnSelect(String onSelect) {
		this.onSelect = onSelect;
	}

	public String getOnUnselect() {
		return onUnselect;
	}

	public void setOnUnselect(String onUnselect) {
		this.onUnselect = onUnselect;
	}

	public String getOnUnselectAll() {
		return onUnselectAll;
	}

	public void setOnUnselectAll(String onUnselectAll) {
		this.onUnselectAll = onUnselectAll;
	}

	public String getOnCheck() {
		return onCheck;
	}

	public void setOnCheck(String onCheck) {
		this.onCheck = onCheck;
	}

	public String getOnUncheck() {
		return onUncheck;
	}

	public void setOnUncheck(String onUncheck) {
		this.onUncheck = onUncheck;
	}

	public String getOnCheckAll() {
		return onCheckAll;
	}

	public void setOnCheckAll(String onCheckAll) {
		this.onCheckAll = onCheckAll;
	}

	public String getOnUncheckAll() {
		return onUncheckAll;
	}

	public void setOnUncheckAll(String onUncheckAll) {
		this.onUncheckAll = onUncheckAll;
	}

	public String getOnHeaderContextMenu() {
		return onHeaderContextMenu;
	}

	public void setOnHeaderContextMenu(String onHeaderContextMenu) {
		this.onHeaderContextMenu = onHeaderContextMenu;
	}

	public String getOnDblClickRow() {
		return onDblClickRow;
	}

	public void setOnDblClickRow(String onDblClickRow) {
		this.onDblClickRow = onDblClickRow;
	}

}
