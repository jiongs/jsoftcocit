package com.jsoft.cocit.entity.impl.cui;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.DataEntity;
import com.jsoft.cocit.entity.cui.IExtCuiGrid;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

@Entity
@CocEntity(name = "定义GRID", key = Const.TBL_CUI_GRID, sn = 2, dbEncoding = true, uniqueFields = "cocEntityKey,cuiEntityKey", indexFields = "cocEntityKey,cuiEntityKey"//
           , actions = {
                   //
                   @CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c"),//
                   @CocAction(name = "编辑", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v") //
           }// end actions
           , groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "所属实体对象", field = "cocEntityKey", fkTargetEntity = Const.TBL_COC_ENTITY, asFilterNode = true), //
                   @CocColumn(name = "所属界面", field = "cuiEntityKey", fkTargetEntity = Const.TBL_CUI_ENTITY), //
                   @CocColumn(name = "GRID编码", field = "key"), //
                   @CocColumn(name = "锁定字段列表", field = "frozenFields"), //
                   @CocColumn(name = "字段列表", field = "fields", length = 1000), //
                   @CocColumn(name = "显示行号", field = "rownumbers"), //
                   @CocColumn(name = "支持多选", field = "multiSelect"), //
                   @CocColumn(name = "勾选时选中行", field = "selectOnCheck"), //
                   @CocColumn(name = "选中行时勾选", field = "checkOnSelect"), //
                   @CocColumn(name = "分页导航位置", field = "paginationPos"), //
                   @CocColumn(name = "分页导航嵌入操作", field = "paginationActions"), //
                   @CocColumn(name = "页索引", field = "pageIndex"), //
                   @CocColumn(name = "页大小", field = "pageSize"), //
                   @CocColumn(name = "页大小选项", field = "pageOptions"), //
                   @CocColumn(name = "默认排序", field = "sortExpr"), //
                   @CocColumn(name = "显示表头", field = "showHeader"), //
                   @CocColumn(name = "显示表脚", field = "showFooter"), //
                   @CocColumn(name = "行操作列表", field = "rowActions"), //
                   @CocColumn(name = "行操作UI", field = "rowActionsView", length = 20), //
                   @CocColumn(name = "行操作位置", field = "rowActionsPos"), //
                   @CocColumn(name = "TreeGrid字段", field = "treeField"), //
           }) // end group
           }// end groups
)
public class CuiGridImpl extends DataEntity implements IExtCuiGrid {

	private String cocEntityKey;
	private String cuiEntityKey;
	private String frozenFields;
	private String fields;
	private boolean rownumbers;
	private boolean multiSelect;
	private boolean selectOnCheck;
	private boolean checkOnSelect;
	private byte paginationPos;
	private String paginationActions;
	private int pageIndex;
	private int pageSize;
	private String pageOptions;
	private String sortExpr;
	private boolean showHeader;
	private boolean showFooter;
	@Column(length = 512)
	private String rowActions;
	private String rowActionsView;
	private byte rowActionsPos;
	@Column(length = 512)
	private String rowStyleRule;
	private String treeField;
	private String uiView;
	private boolean singleRowEdit;

	public void release() {
		super.release();

		this.cocEntityKey = null;
		this.cuiEntityKey = null;
		this.frozenFields = null;
		this.fields = null;
		this.rownumbers = false;
		this.multiSelect = false;
		this.selectOnCheck = false;
		this.checkOnSelect = false;
		this.paginationPos = 0;
		this.paginationActions = null;
		this.pageIndex = 1;
		this.pageSize = 20;
		this.pageOptions = null;
		this.sortExpr = null;
		this.showHeader = false;
		this.showFooter = false;
		this.rowActions = null;
		this.rowActionsPos = 0;
		this.treeField = null;
	}

	public String getCocEntityKey() {
		return cocEntityKey;
	}

	public void setCocEntityKey(String cocEntityKey) {
		this.cocEntityKey = cocEntityKey;
	}

	public String getCuiEntityKey() {
		return cuiEntityKey;
	}

	public void setCuiEntityKey(String cuiEntityKey) {
		this.cuiEntityKey = cuiEntityKey;
	}

	public String getFrozenFields() {
		return frozenFields;
	}

	public void setFrozenFields(String frozenFields) {
		this.frozenFields = frozenFields;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public boolean isRownumbers() {
		return rownumbers;
	}

	public void setRownumbers(boolean rownumbers) {
		this.rownumbers = rownumbers;
	}

	public boolean isMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(boolean singleSelect) {
		this.multiSelect = singleSelect;
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

	public byte getPaginationPos() {
		return paginationPos;
	}

	public void setPaginationPos(byte paginationPos) {
		this.paginationPos = paginationPos;
	}

	public String getPaginationActions() {
		return paginationActions;
	}

	public void setPaginationActions(String paginationActions) {
		this.paginationActions = paginationActions;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageOptions() {
		return pageOptions;
	}

	public void setPageOptions(String pageOptions) {
		this.pageOptions = pageOptions;
	}

	public String getSortExpr() {
		return sortExpr;
	}

	public void setSortExpr(String sortExpr) {
		this.sortExpr = sortExpr;
	}

	public boolean isShowHeader() {
		return showHeader;
	}

	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}

	public boolean isShowFooter() {
		return showFooter;
	}

	public void setShowFooter(boolean showFooter) {
		this.showFooter = showFooter;
	}

	public String getRowActions() {
		return rowActions;
	}

	public void setRowActions(String rowActions) {
		this.rowActions = rowActions;
	}

	public String getRowActionsView() {
		return rowActionsView;
	}

	public void setRowActionsView(String rowActionsView) {
		this.rowActionsView = rowActionsView;
	}

	public byte getRowActionsPos() {
		return rowActionsPos;
	}

	public void setRowActionsPos(byte rowActionsPos) {
		this.rowActionsPos = rowActionsPos;
	}

	public String getTreeField() {
		return treeField;
	}

	public void setTreeField(String treeField) {
		this.treeField = treeField;
	}

	public String getRowStyleRule() {
		return rowStyleRule;
	}

	public void setRowStyleRule(String rowStyleRule) {
		this.rowStyleRule = rowStyleRule;
	}

	public String getUiView() {
		return uiView;
	}

	public void setUiView(String uiView) {
		this.uiView = uiView;
	}

	public boolean isSingleRowEdit() {
		return singleRowEdit;
	}

	public void setSingleRowEdit(boolean singleRowEdit) {
		this.singleRowEdit = singleRowEdit;
	}
}
