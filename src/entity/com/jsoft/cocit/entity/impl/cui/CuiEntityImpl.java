package com.jsoft.cocit.entity.impl.cui;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.cui.IExtCuiEntity;
import com.jsoft.cocit.entity.impl.coc.CocEntityImpl;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

@Entity
@CocEntity(name = "定义实体界面", key = Const.TBL_CUI_ENTITY, sn = 1, dbEncoding = true, uniqueFields = Const.FLIST_COC_KEYS, indexFields = Const.FLIST_COC_KEYS//
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
                   @CocColumn(name = "界面名称", field = "name"), //
                   @CocColumn(name = "界面编码", field = "key"), //
                   @CocColumn(name = "过滤器字段", field = "filterFields", length = 255), //
                   @CocColumn(name = "过滤器UI", field = "filterFieldsView", dicOptions = "tree:树形"), //
                   @CocColumn(name = "查询编辑器字段", field = "queryFields", length = 255), //
                   @CocColumn(name = "查询编辑器栏位置", field = "queryFieldsPos", dicOptions = "0:自动, 1:顶部, 2:底部, 3:左边, 4:右边, 5:嵌入GRID顶部"), //
                   @CocColumn(name = "查询编辑器UI", field = "queryFieldsView"), //
                   @CocColumn(name = "操作列表", field = "actions", length = 255), //
                   @CocColumn(name = "操作栏位置", field = "actionsPos", dicOptions = "0:自动, 1:顶部, 2:底部, 3:左边, 4:右边, 5:嵌入GRID顶部"), //
                   @CocColumn(name = "操作栏UI", field = "actionsView", dicOptions = "buttons:按钮,menu:菜单"), //
           }) // end group
           }// end groups
)
public class CuiEntityImpl extends NamedEntity implements IExtCuiEntity {

	/*
	 * --------基本信息
	 */
	/**
	 * 逻辑外键：关联到{@link CocEntityImpl#getKey()}
	 */
	private String cocEntityKey;

	/*
	 * --------主界面布局
	 */
	private String cols;
	private String rows;

	/*
	 * --------数据过滤器
	 */
	@Column(length = 255)
	private String filterFields;
	private byte filterFieldsPos;
	private String filterFieldsView;

	/*
	 * -------- 查询编辑器
	 */
	@Column(length = 255)
	private String queryFields;
	private byte queryFieldsPos;
	private String queryFieldsView;

	/*
	 * -------- 操作按钮
	 */
	@Column(length = 255)
	private String actions;
	private String actionsView;
	private byte actionsPos;

	@Column(length = 128)
	private String uiView;

	public void release() {
		super.release();

		this.cocEntityKey = null;
		this.cols = null;
		this.rows = null;
		this.filterFields = null;
		this.filterFieldsView = null;
		this.queryFields = null;
		this.queryFieldsView = null;
		this.actions = null;
		this.actionsView = null;
		this.actionsPos = 0;
	}

	public String getCocEntityKey() {
		return cocEntityKey;
	}

	public void setCocEntityKey(String cocEntityKey) {
		this.cocEntityKey = cocEntityKey;
	}

	public String getCols() {
		return cols;
	}

	public void setCols(String cols) {
		this.cols = cols;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getFilterFields() {
		return filterFields;
	}

	public void setFilterFields(String filterFields) {
		this.filterFields = filterFields;
	}

	public String getFilterFieldsView() {
		return filterFieldsView;
	}

	public void setFilterFieldsView(String filterFieldsView) {
		this.filterFieldsView = filterFieldsView;
	}

	public String getQueryFields() {
		return queryFields;
	}

	public void setQueryFields(String queryFields) {
		this.queryFields = queryFields;
	}

	public String getQueryFieldsView() {
		return queryFieldsView;
	}

	public void setQueryFieldsView(String queryFieldsView) {
		this.queryFieldsView = queryFieldsView;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getActionsView() {
		return actionsView;
	}

	public void setActionsView(String actionsView) {
		this.actionsView = actionsView;
	}

	public byte getActionsPos() {
		return actionsPos;
	}

	public void setActionsPos(byte actionsPos) {
		this.actionsPos = actionsPos;
	}

	public byte getQueryFieldsPos() {
		return queryFieldsPos;
	}

	public void setQueryFieldsPos(byte queryFieldsPos) {
		this.queryFieldsPos = queryFieldsPos;
	}

	public String getUiView() {
		return uiView;
	}

	public void setUiView(String uiView) {
		this.uiView = uiView;
	}

	public byte getFilterFieldsPos() {
		return filterFieldsPos;
	}

	public void setFilterFieldsPos(byte filterFieldsPos) {
		this.filterFieldsPos = filterFieldsPos;
	}
}
