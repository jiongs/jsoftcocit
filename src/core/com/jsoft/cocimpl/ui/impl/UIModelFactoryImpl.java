// $codepro.audit.disable unnecessaryImport
package com.jsoft.cocimpl.ui.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.baseentity.ITreeEntity;
import com.jsoft.cocit.baseentity.ITreeObjectEntity;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.constant.CocUrl;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.constant.StatusCodes;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.dmengine.info.ICocActionInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.dmengine.info.ICocGroupInfo;
import com.jsoft.cocit.dmengine.info.ICuiEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormActionInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormFieldInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormInfo;
import com.jsoft.cocit.dmengine.info.ICuiGridFieldInfo;
import com.jsoft.cocit.dmengine.info.ICuiGridInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.ui.UIModelFactory;
import com.jsoft.cocit.ui.model.control.UIActions;
import com.jsoft.cocit.ui.model.control.UIEntities;
import com.jsoft.cocit.ui.model.control.UIEntity;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UIFieldGroup;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.model.control.UIList;
import com.jsoft.cocit.ui.model.control.UIPanel;
import com.jsoft.cocit.ui.model.control.UISearchBox;
import com.jsoft.cocit.ui.model.control.UITree;
import com.jsoft.cocit.ui.model.datamodel.UIGridData;
import com.jsoft.cocit.ui.model.datamodel.UITreeData;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

/**
 * 
 * @preserve
 * @author Ji Yongshan
 * 
 */
public class UIModelFactoryImpl implements UIModelFactory {

	private String makeHtmlID(Class type, Long dataID) {
		return type.getSimpleName().toLowerCase() + "_" + dataID + "_" + Cocit.me().getHttpContext().getClientUIToken();
	}

	@Override
	public UIEntities getMains(ISystemMenuInfo menuService) {
		if (menuService == null)
			throw new CocException("未知系统菜单！");

		/*
		 * 创建模块 Grid
		 */
		ICocEntityInfo mainEntityInfo = menuService.getCocEntity();

		/*
		 * 获取需要引用的主界面
		 */
		String cuiCode = menuService.getUiView();
		if (StringUtil.isBlank(cuiCode)) {
			cuiCode = mainEntityInfo.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		List<String> cols = null;
		List<String> rows = null;
		ICuiEntityInfo cui = mainEntityInfo.getCuiEntity(cuiCode);
		if (cui != null) {
			cols = cui.getColsList();
			rows = cui.getRowsList();
		}

		/*
		 * 计算实体UI
		 */
		UIEntity mainUiEntity = getMain(menuService, mainEntityInfo, false);
		UIGrid mainGrid = mainUiEntity.getGrid();

		/*
		 * 创建模块 Panels
		 */
		UIEntities subPanels = new UIEntities()//
		        .setId(makeHtmlID(UIEntities.class, menuService.getId()));
		subPanels.setRows(rows);
		subPanels.setCols(cols);

		/*
		 * 创建主模块 Panel：通过URL异步加载主表界面
		 */
		// UIPanel panel = UIPanel.make(mainUiEntity)//
		// .setPanelUrl(MVCUtil.makeUrl(CocUrl.ENTITY_GET_MAIN_UI, menuService, mainEntityInfo) + "/1/1")//
		// .setAjax(true)//
		// .setId(makeHtmlID(UIPanel.class, menuService.getId()))//
		// .setTitle(menuService.getName())//
		// ;
		UIPanel panel = UIPanel.make(mainUiEntity)//
		        // .setPanelUrl(MVCUtil.makeUrl(CocUrl.ENTITY_GET_MAIN_UI, menuService, mainEntityInfo) + "/1/1")//
		        // .setAjax(true)//
		        .setId(makeHtmlID(UIPanel.class, menuService.getId()))//
		        .setTitle(menuService.getName())//
		        ;

		/*
		 * 添加主模块 Panel 到 Panels
		 */
		subPanels.addPanel(panel);

		/*
		 * 创建子模块 Panel
		 */
		List<ICocEntityInfo> subEntityInfos = mainEntityInfo.getSubEntities();
		if (subEntityInfos != null) {
			for (ICocEntityInfo subEntityInfo : subEntityInfos) {

				ICocFieldInfo subModuleFkField = mainEntityInfo.getFkFieldOfSubEntity(subEntityInfo.getCode());

				/*
				 * 创建子模块 Panel
				 */
				panel = UIPanel.make(MVCUtil.makeUrl(CocUrl.ENTITY_GET_MAIN_UI, menuService, subEntityInfo) + "/1/1")//
				        .set("fkField", subModuleFkField.getFieldName())//
				        .set("fkTargetField", subModuleFkField.getFkTargetFieldCode())//
				        .setId(makeHtmlID(UIPanel.class, subEntityInfo.getId()))//
				        .setTitle(subEntityInfo.getName())//
				        ;

				/*
				 * 设置主从表之间的关系：即 paramUI 和 resultUI
				 */
				mainGrid.addResultUI(makeHtmlID(UIGrid.class, subEntityInfo.getId()));
				panel.addParamUI(makeHtmlID(UIGrid.class, mainEntityInfo.getId()));

				/*
				 * 添加子模块 Panel 到 Panels
				 */
				subPanels.addPanel(panel);
			}
		}

		/*
		 * 返回
		 */
		return subPanels;
	}

	@Override
	public UIEntity getMain(ISystemMenuInfo menuService, ICocEntityInfo entityService, boolean usedToSubEntity) {

		/*
		 * 获取需要引用的主界面
		 */
		String cuiCode = menuService.getUiView();
		if (StringUtil.isBlank(cuiCode)) {
			cuiCode = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		String title = null;
		byte actionsPos = 0;
		byte searchBoxPos = 0;
		String uiView = null;
		ICuiEntityInfo cui = entityService.getCuiEntity(cuiCode);
		if (cui != null) {
			title = cui.getName();
			actionsPos = cui.getActionsPos();
			searchBoxPos = cui.getQueryFieldsPos();
			uiView = cui.getUiView();
		}
		if (actionsPos == 0)
			actionsPos = 1;
		if (searchBoxPos == 0)
			actionsPos = 1;

		/*
		 * 创建过滤器
		 */
		UITree filter = this.getFilter(menuService, entityService, usedToSubEntity);

		/*
		 * 创建检索框
		 */
		UISearchBox searchBox = this.getSearchBox(menuService, entityService);

		/*
		 * 创建操作按钮
		 */
		UIActions actions = this.getActions(menuService, entityService);

		/*
		 * 创建数据网格
		 */
		UIGrid grid = this.getGrid(menuService, entityService);

		/*
		 * 创建实体UI
		 */
		UIEntity model = new UIEntity()//
		        .setFilter(filter)//
		        .setActions(actions)//
		        .setGrid(grid)//
		        .setSearchBox(searchBox)//
		        .setId(makeHtmlID(UIEntity.class, entityService.getId()))//
		        .setTitle(title == null ? entityService.getName() : title)//
		        ;
		model.setViewName(uiView);
		model.setActionsPos(actionsPos);
		if (searchBoxPos == 0) {
			model.setSearchBoxPos((byte) 4);
		} else {
			model.setSearchBoxPos(searchBoxPos);
		}

		/*
		 * 检查当前GRID是否支持行编辑
		 */
		if (grid.isSingleRowEdit()) {
			Node opAdd = actions.findNode("opCode", "" + OpCodes.OP_INSERT_GRID_ROW);
			Node opEdit = actions.findNode("opCode", "" + OpCodes.OP_UPDATE_GRID_ROW);
			if (opEdit == null) {
				UIActions rowActions = grid.getRowActions();
				if (rowActions != null) {
					opEdit = rowActions.findNode("opCode", "" + OpCodes.OP_UPDATE_GRID_ROW);
				}
			}
			if (opEdit != null) {
				grid.setDataEditUrl(MVCUtil.makeUrl(CocUrl.ENTITY_SAVE, menuService, entityService, opEdit.getId()));
			}
			if (opAdd != null) {
				grid.setDataAddUrl(MVCUtil.makeUrl(CocUrl.ENTITY_SAVE, menuService, entityService, opAdd.getId()));
			}
		}

		/*
		 * 如果GRID支持行编辑，且支持多行编辑，则主界面即为表单
		 */
		Node opSave = actions.findNode("opCode", "" + OpCodes.OP_SAVE_GRID_ROWS);
		if (opSave != null) {
			model.setForm(true);
			grid.putAttribute("name", "entity");
			if (filter != null) {
				filter.putAttribute("name", "entity");
			}
		}

		/*
		 * 设置UI关联
		 */
		if (searchBox != null) {
			grid.addParamUI(searchBox.getId());
			searchBox.addResultUI(grid.getId());
		}
		if (filter != null) {
			grid.addParamUI(filter.getId());
			filter.addResultUI(grid.getId());
		}
		if (actions != null) {
			actions.addResultUI(grid.getId());
		}

		/*
		 * 返回
		 */
		return model;
	}

	@Override
	public UISearchBox getSearchBox(ISystemMenuInfo menuService, ICocEntityInfo entityService) {
		return getSearchBox(menuService, entityService, null);
	}

	@Override
	public UISearchBox getSearchBox(ISystemMenuInfo menuService, ICocEntityInfo entityService, List<String> queryFields) {

		/*
		 * 获取需要引用的主界面
		 */
		String cuiCode = menuService.getUiView();
		if (StringUtil.isBlank(cuiCode)) {
			cuiCode = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		ICuiEntityInfo cui = entityService.getCuiEntity(cuiCode);
		if (queryFields == null && cui != null) {
			queryFields = cui.getQueryFieldsList();
		}

		if (queryFields != null && queryFields.size() > 0) {
			/*
			 * 添加分类选项到检索框中
			 */
			UISearchBox model = new UISearchBox()//
			        // .setData(options)//
			        .setId(makeHtmlID(UISearchBox.class, entityService.getId()))//
			        ;

			/*
			 * 创建检索框模型
			 */
			// List<Option> options = new ArrayList();
			List<ICocFieldInfo> fields = entityService.getFieldsOfGrid(queryFields);
			for (ICocFieldInfo fieldService : fields) {
				model.addField(this.makeField(menuService, fieldService, null));
			}

			/*
			 * 返回
			 */
			return model;
		}

		return null;
	}

	@Override
	public UIGrid getGrid(ISystemMenuInfo menuService, ICocEntityInfo entityService) {
		return getGrid(menuService, entityService, null, null);
	}

	@Override
	public UIGrid getGrid(ISystemMenuInfo menuService, ICocEntityInfo entityService, List<String> specifiedFieldList, List<String> specifiedRowActionList) {

		/*
		 * 获取需要引用的主界面
		 */
		String cuiCode = menuService.getUiView();
		if (StringUtil.isBlank(cuiCode)) {
			cuiCode = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		ICuiEntityInfo cui = entityService.getCuiEntity(cuiCode);

		ICuiGridInfo cuiGrid = null;
		List<String> frozenFieldNames = null;
		List<String> fieldNames = null;
		List<String> fieldList = null;
		List<String> rowActionList = null;
		if (cui != null) {
			cuiGrid = cui.getCuiGrid();
			if ((specifiedFieldList == null)) {
				if (cuiGrid != null) {
					fieldList = new ArrayList();
					frozenFieldNames = cuiGrid.getFrozenFieldsList();
					fieldList.addAll(frozenFieldNames);
					fieldNames = cuiGrid.getFieldsList();
					fieldList.addAll(fieldNames);
				}
			} else {
				fieldList = specifiedFieldList;
			}
		}

		/*
		 * 创建 Grid
		 */
		UIGrid model = new UIGrid()//
		        .setId(makeHtmlID(UIGrid.class, entityService.getId()))//
		        .setTitle(entityService.getName())//
		        ;

		/*
		 * 计算 Grid 属性
		 */
		if (cuiGrid != null) {
			model.set("", cuiGrid.isCheckOnSelect());
			model.set("singleSelect", !cuiGrid.isMultiSelect());
			model.set("pageIndex", cuiGrid.getPageIndex());
			model.set("pageOptions", cuiGrid.getPageOptions());
			model.set("pageSize", cuiGrid.getPageSize());
			switch (cuiGrid.getPaginationPos()) {
				case 0:// auto
					model.set("paginationPos", "top");
					break;
				case 1:// bottom
					model.set("paginationPos", "bottom");
					break;
				case 2:// top
					model.set("paginationPos", "top");
					break;
				case 3:// bottom&top
					model.set("paginationPos", "");
					break;
				case 9:// none
					model.set("pagination", false);
					break;
			}
			model.set("rownumbers", cuiGrid.isRownumbers());
			model.set("selectOnCheck", cuiGrid.isSelectOnCheck());
			model.set("checkOnSelect", cuiGrid.isCheckOnSelect());
			model.set("showFooter", cuiGrid.isShowFooter());
			model.set("showHeader", cuiGrid.isShowHeader());
			model.set("sortExpr", cuiGrid.getSortExpr());

			model.setRowStyles(cuiGrid.getRowStyles());
			model.setSortExpr(cuiGrid.getSortExpr());
			model.setTreeField(cuiGrid.getTreeField());
			model.setViewName(cuiGrid.getUiView());
			model.setSingleRowEdit(cuiGrid.isSingleRowEdit());

			if (specifiedRowActionList == null) {
				String rowActions = cuiGrid.getRowActions();
				rowActionList = StringUtil.toList(rowActions, "|,， ");
			} else {
				rowActionList = specifiedRowActionList;
			}
		}

		/*
		 * 计算GRID行“添加、编辑”操作
		 */
		ICocActionInfo opAdd = entityService.getActionByOpCode(OpCodes.OP_INSERT_GRID_ROW);
		ICocActionInfo opEdit = entityService.getActionByOpCode(OpCodes.OP_UPDATE_GRID_ROW);
		String opAddGridRow = "";
		String opEditGridRow = "";
		if (opAdd != null) {
			opAddGridRow = opAdd.getCode();
			model.setDefaultValuesForAddRow(opAdd.getDefaultValues());
		}
		if (opEdit != null) {
			opEditGridRow = opEdit.getCode();
		}

		/*
		 * 计算行操作
		 */
		if (rowActionList != null) {
			Tree data = getActionsData(menuService, entityService, null, null, rowActionList);
			UIActions rowUIActions = new UIActions().setData(data).setId(makeHtmlID(UIActions.class, entityService.getId()));

			model.setRowActions(rowUIActions);
			rowUIActions.addResultUI(model.getId());

			if (cuiGrid != null) {
				model.setRowActionsPos(cuiGrid.getRowActionsPos());
			}

		}

		/*
		 * 创建 Grid 列
		 */
		List<ICocFieldInfo> fields = entityService.getFieldsOfGrid(fieldList);
		ICuiGridFieldInfo cuiField = null;
		int columnsTotalWidth = 0;
		int width = -1;
		boolean hidden = false, showCellTips = false;
		String title = null, cellView = null;
		String linkUrl = null;
		String linkTarget = null;
		String align = null, halign = null;
		StyleRule[] styleRules = null;
		for (ICocFieldInfo fld : fields) {
			width = -1;
			title = null;
			cellView = null;
			linkUrl = null;
			linkTarget = null;
			align = null;
			halign = null;
			styleRules = null;
			hidden = false;
			showCellTips = false;

			if (cuiGrid != null) {
				cuiField = cuiGrid.getField(fld.getCode());
				if (cuiField != null) {
					width = cuiField.getWidth();
					title = cuiField.getName();
					cellView = cuiField.getCellView();
					linkUrl = cuiField.getCellViewLinkUrl();
					linkTarget = cuiField.getCellViewLinkTarget();
					align = cuiField.getAlign();
					halign = cuiField.getHalign();
					styleRules = cuiField.getCellStyles();
					hidden = cuiField.isHidden();
					showCellTips = cuiField.isShowCellTips();
				}
			}
			if (StringUtil.isBlank(cellView)) {
				cellView = fld.getUiView();
			}
			if (width == -1) {
				if (fld.isDic()) {
					width = 100;
				} else {
					switch (fld.getFieldType()) {
						case Const.FIELD_TYPE_BYTE:
						case Const.FIELD_TYPE_SHORT:
						case Const.FIELD_TYPE_INTEGER:
						case Const.FIELD_TYPE_LONG:
							width = 60;
							break;
						case Const.FIELD_TYPE_NUMBER:
						case Const.FIELD_TYPE_FLOAT:
						case Const.FIELD_TYPE_DOUBLE:
						case Const.FIELD_TYPE_DECIMAL:
							width = 100;
							break;
						case Const.FIELD_TYPE_BOOLEAN:
							width = 60;
							break;
						case Const.FIELD_TYPE_DATE:
							width = 150;
							break;
						case Const.FIELD_TYPE_UPLOAD:
							width = 120;
							break;
						case Const.FIELD_TYPE_TEXT:
						case Const.FIELD_TYPE_RICHTEXT:
							width = 200;
							break;
						case Const.FIELD_TYPE_FK:
							width = 120;
							break;
						default:
							Integer len = fld.getLength();
							if (len != null && len > 0) {
								width = len * 8;
							} else {
								width = 150;
							}
							if (width > 200) {
								width = 200;
							}
					}
				}
			}

			UIField col = new UIField().setFieldService(fld).setWidth(width);
			if (StringUtil.hasContent(title)) {
				col.setTitle(title);
			}
			col.setViewName(cellView);
			col.setLinkUrl(linkUrl);
			col.setLinkTarget(linkTarget);
			if (StringUtil.hasContent(align)) {
				col.setAlign(align);
			}
			if (StringUtil.hasContent(halign)) {
				col.setHalign(halign);
			}
			col.setCellStyles(styleRules);
			col.setHidden(hidden);
			col.setShowTips(showCellTips);
			col.addResultUI(model.getId());
			if (StringUtil.hasContent(opAddGridRow)) {
				col.setModeToAddGridRow(fld.getMode(opAddGridRow, null));
			}
			if (StringUtil.hasContent(opEditGridRow)) {
				col.setModeToEditGridRow(fld.getMode(opEditGridRow, null));
			}

			columnsTotalWidth += width;

			model.addColumn(col);
		}

		model.setColumnsTotalWidth(columnsTotalWidth);
		model.setEntityCode(entityService.getCode());
		String url = MVCUtil.makeUrl(CocUrl.ENTITY_GET_GRID_DATA, menuService, entityService);
		if (specifiedFieldList != null) {
			url += "/" + StringUtil.join(specifiedFieldList, null, "|");
			if (specifiedRowActionList != null && specifiedRowActionList.size() > 0) {
				url += "/" + StringUtil.join(specifiedRowActionList, null, "|");
			}
		} else if (specifiedRowActionList != null) {
			url += "/" + CocUrl.PATH_PARAM_EMPTY + "/" + StringUtil.join(specifiedRowActionList, null, "|");
		}
		model.setDataLoadUrl(url);

		/*
		 * 
		 */
		HttpContext httpCtx = Cocit.me().getHttpContext();
		model.addResultUI(httpCtx.getClientResultUIList());
		model.addParamUI(httpCtx.getClientParamUIList());
		String params = httpCtx.getParameterValue("_fkField");
		model.addFkField(StringUtil.toList(params));
		params = httpCtx.getParameterValue("_fkTargetField");
		model.addFkTargetField(StringUtil.toList(params));

		/*
		 * 返回
		 */
		return model;
	}

	@Override
	public UIGrid getComboGrid(ISystemMenuInfo targetMenuService, ICocEntityInfo targetEntityService, ICocFieldInfo fkFieldService) {

		UIGrid model = new UIGrid();
		model.setId(makeHtmlID(UIGrid.class, targetEntityService.getId()));
		model.setName(targetEntityService.getName());
		String url = fkFieldService.getFkComboUrl();
		if (targetMenuService == null)
			targetMenuService = targetEntityService.getSystemMenu();
		if (StringUtil.isBlank(url)) {
			url = MVCUtil.makeUrl(CocUrl.ENTITY_GET_COMBOGRID_DATA, targetMenuService, targetEntityService);
			url += "/" + fkFieldService.getCocEntityCode() + ":" + fkFieldService.getFieldName();
		} else {
			url = MVCUtil.makeUrl(url);
		}
		model.setDataLoadUrl(url);
		model.set("rownumbers", false);
		model.set("singleSelect", true);
		model.set("checkbox", false);

		/*
		 * Name字段
		 */
		ICocFieldInfo nameField = targetEntityService.getField(Const.F_NAME);
		if (nameField == null) {
			throw new CocException("实体模块字段不存在！%s.%s", targetEntityService.getClassOfEntity().getName(), Const.F_NAME);
		}

		UIField col = new UIField().setFieldService(nameField).setWidth(150);
		model.addColumn(col);

		/*
		 * KEY字段
		 */
		ICocFieldInfo keyField = targetEntityService.getField(Const.F_CODE);
		if (keyField == null) {
			throw new CocException("实体模块字段不存在！%s.%s", targetEntityService.getClassOfEntity().getName(), Const.F_CODE);
		}
		col = new UIField().setFieldService(keyField).setWidth(100);
		col.setHidden(true);
		model.addColumn(col);

		model.setColumnsTotalWidth(150);

		/*
		 * 返回
		 */
		return model;
	}

	@Override
	public UIList getComboList(ISystemMenuInfo menuService, ICocEntityInfo entityService, ICocFieldInfo fkFieldService) {
		String url = fkFieldService.getFkComboUrl();
		if (StringUtil.isBlank(url)) {
			url = MVCUtil.makeUrl(CocUrl.ENTITY_GET_COMBOLIST_DATA, menuService, entityService);
			url += "/" + fkFieldService.getCocEntityCode() + ":" + fkFieldService.getFieldName();
		} else {
			url = MVCUtil.makeUrl(url);
		}
		UIList model = new UIList()//
		        .setDataLoadUrl(url)//
		        .setId(makeHtmlID(UIList.class, entityService.getId()))//
		        .setTitle(entityService.getName())//
		        ;

		/*
		 * 返回
		 */
		return model;
	}

	@Override
	public UITree getComboTree(ISystemMenuInfo menuService, ICocEntityInfo entityService, ICocFieldInfo fkFieldService) {
		String url = fkFieldService.getFkComboUrl();
		if (StringUtil.isBlank(url)) {
			url = MVCUtil.makeUrl(CocUrl.ENTITY_GET_COMBOTREE_DATA, menuService, entityService);
			url += "/" + fkFieldService.getCocEntityCode() + ":" + fkFieldService.getFieldName();
		} else {
			url = MVCUtil.makeUrl(url);
		}
		UITree model = new UITree();
		model.setDataLoadUrl(url);
		model.setId(makeHtmlID(UITree.class, entityService.getId()));
		// if(fkFieldService.isMultiSelect()){
		// model.set("checkbox", true);
		// }
		// model.set("onlyLeafCheck", false);
		// model.set("onlyLeafValue", false);
		// model.set("cascadeCheck", false);

		/*
		 * 返回
		 */
		return model;
	}

	@Override
	public UITreeData getComboTreeData(ISystemMenuInfo menuService, ICocEntityInfo entityService, CndExpr expr) {
		// if (entityService.getFieldOfTree() == null)
		// return null;

		/*
		 * 创建模型
		 */
		UITree model = new UITree();
		model.setId(makeHtmlID(UITree.class, entityService.getId()));

		/*
		 * 查询数据
		 */
		Tree data = entityService.getTreeData(expr);

		/*
		 * 设置模型属性
		 */
		UITreeData ret = new UITreeData();
		ret.setModel(model);
		ret.setData(data);

		/*
		 * 返回
		 */
		return ret;
	}

	@Override
	public UIActions getActions(ISystemMenuInfo menuService, ICocEntityInfo entityService) {
		return getActions(menuService, entityService, null);
	}

	@Override
	public UIActions getActions(ISystemMenuInfo menuService, ICocEntityInfo entityService, List<String> actionIDList) {
		ICocConfig config = Cocit.me().getConfig();

		if (actionIDList == null || actionIDList.size() == 0)
			actionIDList = menuService.getActionCodesWithoutRow();

		/*
		 * 获取需要引用的主界面
		 */
		String cuiCode = menuService.getUiView();
		if (StringUtil.isBlank(cuiCode)) {
			cuiCode = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		String actionsView = null;
		ICuiEntityInfo cui = entityService.getCuiEntity(cuiCode);
		if (cui != null) {
			if (actionIDList == null || actionIDList.size() == 0) {
				actionIDList = cui.getActionsList();
			}
			actionsView = cui.getActionsView();
		}

		if (actionIDList != null && actionIDList.size() == 0) {
			actionIDList = null;
		}

		Tree data = getActionsData(menuService, entityService, null, null, actionIDList);

		/*
		 * 创建操作按钮
		 */
		UIActions model = new UIActions()//
		        .setData(data)//
		        .setId(makeHtmlID(UIActions.class, entityService.getId()))//
		        ;
		if (StringUtil.hasContent(actionsView))
			model.setViewName(actionsView);
		else
			model.setViewName(config.getViewConfig().getViewForGridActions());

		/*
		 * 返回
		 */
		return model;
	}

	private Tree getActionsData(ISystemMenuInfo menuService, ICocEntityInfo entityService, ICocActionInfo entityAction, Object dataObject, List<String> actionIDList) {

		String theCurrentAction = "";
		String dataID = "";
		if (entityAction != null) {
			theCurrentAction = entityAction.getCode();
		}
		if (dataObject != null) {
			if (dataObject instanceof List) {
				for (Object obj : (List) dataObject) {
					Object id = ObjectUtil.getId(obj);
					if (id != null)
						dataID += "," + id.toString();
				}
				if (dataID.length() > 0) {
					dataID = dataID.substring(1);
				}
			} else {
				Object id = ObjectUtil.getId(dataObject);
				if (id != null)
					dataID = id.toString();
			}
		}
		// Class classOfEntity = entityService.getClassOfEntity();

		/*
		 * 创建操作菜单
		 */
		Tree data = Tree.make();
		List<ICocActionInfo> actionServices = entityService.getActions(actionIDList);
		for (ICocActionInfo action : actionServices) {
			String actionCode = action.getCode();

			/*
			 * 创建节点ID
			 */
			String parentNodeID = action.getParentCode();
			if (parentNodeID == "") {
				parentNodeID = null;
			}
			String nodeID = action.getCode();

			/*
			 * 添加操作节点
			 */
			Node child = data.addNode(parentNodeID, nodeID);
			if (child == null)
				continue;

			/*
			 * 设置节点属性
			 */
			child.setName(action.getName());
			child.setSn(action.getSn());

			/*
			 * 设置节点动态属性
			 */
			String opUrl = null;
			String urlAPI = null;
			boolean isWorkflow = false;
			boolean noHeader = !action.isUiWindowHeader();

			switch (action.getOpCode()) {
				/*
				 * 打开操作表单
				 */
				case OpCodes.OP_INSERT_FORM_DATA:
					// if (IProcessInstanceEntity.class.isAssignableFrom(classOfEntity)) {
					// if (theCurrentAction.equals(actionCode)) {
					// urlAPI = WfUrl.WF_START_URL;
					// } else {
					// urlAPI = WfUrl.WF_FORM_TO_START_URL;
					// }
					// isWorkflow = true;
					// } else {
					if (theCurrentAction.equals(actionCode)) {
						urlAPI = CocUrl.ENTITY_SAVE;
					} else {
						urlAPI = CocUrl.ENTITY_GET_FORM_TO_SAVE;
					}
					// }
					break;
				case OpCodes.OP_UPDATE_FORM_DATA:
				case OpCodes.OP_UPDATE_FORM_DATAS:
					// if (IProcessInstanceEntity.class.isAssignableFrom(classOfEntity)) {
					// if (theCurrentAction.equals(actionCode)) {
					// urlAPI = WfUrl.WF_START_URL;
					// } else {
					// urlAPI = WfUrl.WF_FORM_TO_START_URL;
					// }
					// isWorkflow = true;
					// } else {
					if (theCurrentAction.equals(actionCode)) {
						urlAPI = CocUrl.ENTITY_SAVE;
					} else {
						urlAPI = CocUrl.ENTITY_GET_FORM_TO_SAVE;
					}
					// }
					break;
				case OpCodes.OP_REMOVE_FORM_DATA:
				case OpCodes.OP_REMOVE_FORM_DATAS:
					if (theCurrentAction.equals(actionCode)) {
						urlAPI = CocUrl.ENTITY_REMOVE;
					} else {
						urlAPI = CocUrl.ENTITY_GET_FORM_TO_REMOVE;
					}

					break;
				case OpCodes.OP_DELETE_FORM_DATA:
				case OpCodes.OP_DELETE_FORM_DATAS:
					// if (IProcessInstanceEntity.class.isAssignableFrom(classOfEntity)) {
					// urlAPI = WfUrl.WF_DELETE_URL;
					// isWorkflow = true;
					// } else {
					if (theCurrentAction.equals(actionCode)) {
						urlAPI = CocUrl.ENTITY_DELETE;
					} else {
						urlAPI = CocUrl.ENTITY_GET_FORM_TO_DELETE;
					}
					// }
					break;
				case OpCodes.OP_RUN_FORM_DATA:
				case OpCodes.OP_RUN_FORM_DATAS:
				case OpCodes.OP_RUN_FORM:
					if (theCurrentAction.equals(actionCode)) {
						urlAPI = CocUrl.ENTITY_RUN;
					} else {
						urlAPI = CocUrl.ENTITY_GET_FORM_TO_RUN;
					}
					break;
				case OpCodes.OP_EXPORT_XLS:
					urlAPI = CocUrl.ENTITY_GET_FORM_TO_EXPORT_XLS;
					break;
				case OpCodes.OP_IMPORT_XLS:
					urlAPI = CocUrl.ENTITY_GET_FORM_TO_IMPORT_XLS;
					break;
				/*
				 * 以下操作不弹出表单
				 */
				case OpCodes.OP_UPDATE_ROW:
				case OpCodes.OP_UPDATE_ROWS:
				case OpCodes.OP_SAVE_GRID_ROWS:
					urlAPI = CocUrl.ENTITY_SAVE;
					break;
				case OpCodes.OP_REMOVE_ROW:
				case OpCodes.OP_REMOVE_ROWS:
					urlAPI = CocUrl.ENTITY_REMOVE;
					break;
				case OpCodes.OP_DELETE_ROW:
				case OpCodes.OP_DELETE_ROWS:
					// if (IProcessInstanceEntity.class.isAssignableFrom(classOfEntity)) {
					// urlAPI = WfUrl.WF_DELETE_URL;
					// isWorkflow = true;
					// } else {
					urlAPI = CocUrl.ENTITY_DELETE;
					// }
					break;
				case OpCodes.OP_RUN_ROW:
				case OpCodes.OP_RUN_ROWS:
				case OpCodes.OP_RUN:
					urlAPI = CocUrl.ENTITY_RUN;
					break;
				case OpCodes.OP_CLEAR:
					urlAPI = CocUrl.ENTITY_CLEAR;
					break;
				/*
				 * 排序
				 */
				case OpCodes.OP_SORT_TOP:
					urlAPI = CocUrl.ENTITY_SORT_MOVE_TOP;
					break;
				case OpCodes.OP_SORT_UP:
					urlAPI = CocUrl.ENTITY_SORT_MOVE_UP;
					break;
				case OpCodes.OP_SORT_DOWN:
					urlAPI = CocUrl.ENTITY_SORT_MOVE_DOWN;
					break;
				case OpCodes.OP_SORT_BOTTOM:
					urlAPI = CocUrl.ENTITY_SORT_MOVE_BOTTOM;
					break;
				case OpCodes.OP_SORT_REVERSE:
					urlAPI = CocUrl.ENTITY_SORT_REVERSE;
					break;
				case OpCodes.OP_SORT_CANCEL:
					urlAPI = CocUrl.ENTITY_SORT_CANCEL;
					break;
			}
			if (StringUtil.hasContent(urlAPI)) {
				opUrl = MVCUtil.makeUrl(urlAPI, menuService, entityService, action);
			}
			if (StringUtil.hasContent(opUrl) && StringUtil.hasContent(dataID)) {
				opUrl += "/" + dataID;
			}

			String urlExpr = action.getUiFormUrl();
			if (StringUtil.hasContent(urlExpr)) {
				if (urlExpr.startsWith(Const.VAR_PREFIX)) {
					urlExpr = Cocit.me().getContextPath() + urlExpr;
				} else {
					urlExpr = MVCUtil.makeUrl(urlExpr);
				}
			}
			child.set("successMessage", action.getSuccessMessage());
			child.set("errorMessage", action.getErrorMessage());
			child.set("warnMessage", action.getWarnMessage());
			child.set("opCode", "" + action.getOpCode());
			child.set("opMode", action.getCode());
			if (noHeader) {
				child.set("noHeader", true);
			}
			child.set("title", action.getTitle());

			if (isWorkflow) {
				child.set("dialogStyle", "window-simple");
				child.set("noHeader", true);
			}

			// if (!isWorkflow) {
			if (action.getUiWindowHeight() > 0)
				child.set("windowHeight", action.getUiWindowHeight());
			if (action.getUiWindowWidth() > 0)
				child.set("windowWidth", action.getUiWindowWidth());
			// }

			if (opUrl != null) {// 自动生成的操作按钮访问路径
				child.set("opUrl", opUrl);
			}
			if (StringUtil.hasContent(urlExpr)) {// 操作按钮表达式地址，通常是含有变量(${var})的地址
				child.set("urlExpr", urlExpr);
			}
			if (StringUtil.hasContent(action.getUiFormTarget())) {
				child.set("opUrlTarget", action.getUiFormTarget());
			}
			child.setReferObj(action);

		}

		return data;
	}

	@Override
	public UITree getFilter(ISystemMenuInfo menuService, ICocEntityInfo entityService, boolean usedToSubEntity) {
		/*
		 * 获取需要引用的主界面
		 */
		String cuiCode = menuService.getUiView();
		if (StringUtil.isBlank(cuiCode)) {
			cuiCode = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		List<String> filterFields = null;
		String fielterFieldsView = null;
		ICuiEntityInfo cui = entityService.getCuiEntity(cuiCode);
		byte filterPosition = 0;
		if (cui != null) {
			filterFields = cui.getFilterFieldsList();
			fielterFieldsView = cui.getFilterFieldsView();
			filterPosition = cui.getFilterFieldsPos();
		}

		/*
		 * 没有找到过滤器字段
		 */
		if ((filterFields == null || filterFields.size() == 0)//
		        && ObjectUtil.isNil(entityService.getFieldsOfFilter(usedToSubEntity))//
		) {
			return null;
		}

		/*
		 * 创建树模型
		 */
		UITree model = new UITree();
		model.setId(makeHtmlID(UITree.class, entityService.getId()));
		model.set("onlyLeafCheck", false);
		model.set("onlyLeafValue", false);
		model.set("checkOnSelect", true);
		model.set("selectOnCheck", true);
		model.set("cascadeCheck", true);
		// model.set("onSelect", "jCocit.entity.doSelectFilter");
		model.set("onCheck", "jCocit.entity.doSelectFilter");
		model.set("checkbox", 1);
		model.setViewName(fielterFieldsView);
		model.set("filterPosition", filterPosition);
		// if (filterFields != null && filterFields.size() > 0) {
		// model.setTitle(entityService.getField(filterFields.get(0)).getName());//
		// } else {
		model.setTitle("快速过滤");//
		// }

		/*
		 * 设置异步加载数据的 URL 地址
		 */
		String url = MVCUtil.makeUrl(CocUrl.ENTITY_GET_FILTER_DATA, menuService, entityService);
		model.setDataLoadUrl(url + "/" + usedToSubEntity);

		/*
		 * 返回
		 */
		return model;
	}

	@Override
	public UITreeData getFilterData(ISystemMenuInfo menuService, ICocEntityInfo entityService, boolean usedToSubEntity) {
		/*
		 * 获取需要引用的主界面
		 */
		String cuiCode = menuService.getUiView();
		if (StringUtil.isBlank(cuiCode)) {
			cuiCode = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		List<String> filterFields = null;
		ICuiEntityInfo cui = entityService.getCuiEntity(cuiCode);
		byte filterPosition = 0;
		if (cui != null) {
			filterFields = cui.getFilterFieldsList();
			filterPosition = cui.getFilterFieldsPos();
		}

		/*
		 * 没有找到过滤器字段
		 */
		if ((filterFields == null || filterFields.size() == 0)//
		        && ObjectUtil.isNil(entityService.getFieldsOfFilter(usedToSubEntity))//
		) {
			return null;
		}

		/*
		 * 查询数据
		 */
		Tree data = entityService.getFilterData(filterFields, usedToSubEntity);
		data.optimizeStatus();

		CndExpr menuExpr = menuService.getWhere();
		Map<String, Node> map = data.getAllMap();
		Iterator<Node> nodes = map.values().iterator();
		while (nodes.hasNext()) {
			Node node = nodes.next();
			String nodeID = node.getId();
			int idx = nodeID.indexOf(":");
			if (idx <= 0) {
				continue;
			}
			String fld = nodeID.substring(0, idx);
			String val = nodeID.substring(idx + 1);
			if (!ExprUtil.match(fld, val, menuExpr)) {
				node.setStatusCode("" + StatusCodes.STATUS_CODE_DISABLED);
			}
		}

		/*
		 * 创建模型
		 */
		UITree model = new UITree()//
		        .setId(makeHtmlID(UITree.class, entityService.getId()));
		model.set("filterPosition", filterPosition);

		/*
		 * 创建模型
		 */
		UITreeData ret = new UITreeData();
		ret.setData(data);
		ret.setModel(model);

		/*
		 * 返回
		 */
		return ret;
	}

	@Override
	public UITreeData getRowsAuthData(ISystemMenuInfo menuService, ICocEntityInfo entityService) {
		/*
		 * 查询数据
		 */
		Tree data = entityService.getRowsAuthData();
		data.optimizeStatus();

		CndExpr menuExpr = menuService.getWhere();
		Map<String, Node> map = data.getAllMap();
		Iterator<Node> nodes = map.values().iterator();
		while (nodes.hasNext()) {
			Node node = nodes.next();
			String nodeID = node.getId();
			int idx = nodeID.indexOf(":");
			if (idx <= 0) {
				continue;
			}
			String fld = nodeID.substring(0, idx);
			int dot = fld.indexOf(".");
			if (dot > -1) {
				fld = fld.substring(0, dot);
			}
			String val = nodeID.substring(idx + 1);
			if (!ExprUtil.match(fld, val, menuExpr) && !"folder".equals(node.getType())) {
				node.setStatusCode("" + StatusCodes.STATUS_CODE_DISABLED);
			}
		}

		/*
		 * 创建模型
		 */
		UITree model = new UITree()//
		        .setId(makeHtmlID(UITree.class, entityService.getId()));

		/*
		 * 创建模型
		 */
		UITreeData ret = new UITreeData();
		ret.setData(data);
		ret.setModel(model);

		/*
		 * 返回
		 */
		return ret;
	}

	@Override
	public UITreeData getActionsData(ISystemMenuInfo menuService, ICocEntityInfo entityService) {

		/*
		 * 创建操作菜单
		 */
		Tree tree = Tree.make();
		String rootNode = menuService.getCode() + "_actions";
		Node node = tree.addNode(null, rootNode).setName("全部");
		node.set("open", "true");
		node.set("type", "folder");

		List<ICocActionInfo> actionServices = menuService.getCocActions();
		for (ICocActionInfo action : actionServices) {
			/*
			 * 创建节点ID
			 */
			String parentNodeID = action.getParentCode();
			if (parentNodeID == null || parentNodeID.trim().length() == 0) {
				parentNodeID = rootNode;
			}
			String nodeID = action.getCode();

			/*
			 * 添加操作节点
			 */
			Node child = tree.addNode(parentNodeID, nodeID);
			if (child == null)
				continue;

			/*
			 * 设置节点属性
			 */
			child.setName(action.getName());
			child.setSn(action.getSn());
		}

		/*
		 * 创建模型
		 */
		UITree model = new UITree()//
		        .setId(makeHtmlID(UITree.class, entityService.getId()));

		/*
		 * 创建模型
		 */
		UITreeData ret = new UITreeData();
		ret.setData(tree);
		ret.setModel(model);

		/*
		 * 返回
		 */
		return ret;
	}

	@Override
	public UITree getTree(ISystemMenuInfo menuService, ICocEntityInfo entityService) {
		// if (entityService.getFieldOfTree() == null){
		// entityService.getFieldOfGroup();
		// return null;
		// }

		/*
		 * 创建树模型
		 */
		UITree model = new UITree();
		model.setDataLoadUrl(MVCUtil.makeUrl(CocUrl.ENTITY_GET_TREE_DATA, menuService, entityService));
		model.setId(makeHtmlID(UITree.class, entityService.getId()));
		// model.set("checkbox", false);
		// model.set("onlyLeafCheck", false);
		// model.set("onlyLeafValue", false);
		// model.set("cascadeCheck", false);

		/*
		 * 返回
		 */
		return model;
	}

	@Override
	public UITreeData getTreeData(ISystemMenuInfo menuService, ICocEntityInfo entityService, CndExpr expr) {
		// if (entityService.getFieldOfTree() == null)
		// return null;

		/*
		 * 创建模型
		 */
		UITree model = new UITree();
		model.setId(makeHtmlID(UITree.class, entityService.getId()));

		/*
		 * 查询数据
		 */
		Tree data = entityService.getTreeData(expr);

		/*
		 * 设置模型属性
		 */
		UITreeData ret = new UITreeData();
		ret.setModel(model);
		ret.setData(data);

		/*
		 * 返回
		 */
		return ret;
	}

	@Override
	public UIForm getForm(ISystemMenuInfo menuService, ICocEntityInfo entityService, ICocActionInfo entityAction, Object dataObject) {
		return this.getForm(menuService, entityService, entityAction, dataObject, null);
	}

	@Override
	public UIForm getForm(ISystemMenuInfo menuService, ICocEntityInfo entityService, ICocActionInfo entityAction, Object dataObject, List<String> fieldList) {
		UIForm form = new UIForm();

		if (fieldList != null && fieldList.size() == 0) {
			fieldList = null;
		}

		if (entityAction == null) {
			throw new CocException("操作不存在！");
		}

		form.setActionID(entityAction.getCode());

		/*
		 * 获取需要引用的主界面
		 */
		String cuiCode = menuService.getUiView();
		if (StringUtil.isBlank(cuiCode)) {
			cuiCode = entityService.getUiView();
		}

		/*
		 * 获取表单UI服务
		 */
		ICuiEntityInfo cuiEntityService = entityService.getCuiEntity(cuiCode);

		ICuiFormInfo cuiFormService = null;
		String cuiFormCode = entityAction.getUiForm();
		if (cuiEntityService != null && StringUtil.hasContent(cuiFormCode)) {
			cuiFormService = cuiEntityService.getCuiForm(cuiFormCode);
		}

		/*
		 * 计算表单字段
		 */
		if (cuiFormService == null) {
			if (!StringUtil.isBlank(cuiFormCode)) {
				form.setViewName(cuiFormCode);
			}

			evalUIFormFields(form, entityService, entityAction.getCode(), dataObject, fieldList);
		} else {
			form.setViewName(cuiFormService.getUiView());

			evalUIFormFields(form, menuService, entityService, entityAction.getCode(), dataObject, cuiFormService, fieldList);

			List<String> batchFields = cuiFormService.getBatchFieldsList();
			form.setBatchFields(batchFields);
			this.evalUIFormFields(form, null, menuService, entityService, entityAction.getCode(), dataObject, cuiFormService, batchFields, fieldList);

			/*
			 * 计算表单UI属性
			 */
			Properties attrs = new Properties();
			if (StringUtil.hasContent(cuiFormService.getStyle())) {
				attrs.setProperty("style", cuiFormService.getStyle());
			}
			if (StringUtil.hasContent(cuiFormService.getStyleClass())) {
				attrs.setProperty("styleClass", cuiFormService.getStyleClass());
			}
			form.setFieldLabelPos(cuiFormService.getFieldLabelPos());
			form.setAttributes(attrs);
			if (StringUtil.hasContent(cuiFormService.getName())) {
				form.setTitle(cuiFormService.getName());
			}

			List<String> actionsList = cuiFormService.getActionsList();
			if (actionsList != null && actionsList.size() > 0) {
				Tree data = getActionsData(menuService, entityService, entityAction, dataObject, actionsList);
				for (Node node : data.getChildren()) {
					String actionCode = node.getId();
					ICuiFormActionInfo action = cuiFormService.getFormAction(actionCode);
					if (action != null && StringUtil.hasContent(action.getName())) {
						node.setName(action.getName());
					}
				}

				UIActions uiActions = new UIActions().setData(data).setId(makeHtmlID(UIActions.class, entityService.getId()));

				form.setActions(uiActions);
			}
		}

		form.setDataObject(dataObject);

		return form;
	}

	private void evalUIFormFields(UIForm form, ISystemMenuInfo menuService, ICocEntityInfo entityService, String opID, Object dataObject, ICuiFormInfo cuiFormService, List<String> fieldList) {
		List<List<String>> fields = cuiFormService.getFieldsList();
		form.setFields(fields);

		int fieldRowSize = 0;
		for (List<String> fieldRow : fields) {
			this.evalUIFormFields(form, null, menuService, entityService, opID, dataObject, cuiFormService, fieldRow, fieldList);
		}

		/*
		 * 分组字段
		 */
		UIFieldGroup uiFieldGroup;

		fields = cuiFormService.getGroup1FieldsList();
		if (fields != null && fields.size() > 0) {

			uiFieldGroup = new UIFieldGroup();
			uiFieldGroup.setId(cuiFormService.getGroup1Name());
			uiFieldGroup.setTitle(cuiFormService.getGroup1Name());
			uiFieldGroup.setFields(fields);

			form.addFieldGroup(uiFieldGroup);

			for (List<String> fieldRow : fields) {
				this.evalUIFormFields(form, uiFieldGroup, menuService, entityService, opID, dataObject, cuiFormService, fieldRow, fieldList);
			}
		}

		fields = cuiFormService.getGroup2FieldsList();
		if (fields != null && fields.size() > 0) {

			uiFieldGroup = new UIFieldGroup();
			uiFieldGroup.setId(cuiFormService.getGroup2Name());
			uiFieldGroup.setTitle(cuiFormService.getGroup2Name());
			uiFieldGroup.setFields(fields);

			form.addFieldGroup(uiFieldGroup);

			for (List<String> fieldRow : fields) {
				this.evalUIFormFields(form, uiFieldGroup, menuService, entityService, opID, dataObject, cuiFormService, fieldRow, fieldList);
			}
		}

		fields = cuiFormService.getGroup3FieldsList();
		if (fields != null && fields.size() > 0) {

			uiFieldGroup = new UIFieldGroup();
			uiFieldGroup.setId(cuiFormService.getGroup3Name());
			uiFieldGroup.setTitle(cuiFormService.getGroup3Name());
			uiFieldGroup.setFields(fields);

			form.addFieldGroup(uiFieldGroup);

			for (List<String> fieldRow : fields) {
				this.evalUIFormFields(form, uiFieldGroup, menuService, entityService, opID, dataObject, cuiFormService, fieldRow, fieldList);
			}
		}

		fields = cuiFormService.getGroup4FieldsList();
		if (fields != null && fields.size() > 0) {

			uiFieldGroup = new UIFieldGroup();
			uiFieldGroup.setId(cuiFormService.getGroup4Name());
			uiFieldGroup.setTitle(cuiFormService.getGroup4Name());
			uiFieldGroup.setFields(fields);

			form.addFieldGroup(uiFieldGroup);

			for (List<String> fieldRow : fields) {
				this.evalUIFormFields(form, uiFieldGroup, menuService, entityService, opID, dataObject, cuiFormService, fieldRow, fieldList);
			}
		}

		fields = cuiFormService.getGroup5FieldsList();
		if (fields != null && fields.size() > 0) {

			uiFieldGroup = new UIFieldGroup();
			uiFieldGroup.setId(cuiFormService.getGroup5Name());
			uiFieldGroup.setTitle(cuiFormService.getGroup5Name());
			uiFieldGroup.setFields(fields);

			form.addFieldGroup(uiFieldGroup);

			for (List<String> fieldRow : fields) {
				this.evalUIFormFields(form, uiFieldGroup, menuService, entityService, opID, dataObject, cuiFormService, fieldRow, fieldList);
			}
		}

		form.setRowFieldsSize(fieldRowSize);

	}

	private void evalUIFormFields(UIForm form, UIFieldGroup uiFieldGroup, ISystemMenuInfo menuService, ICocEntityInfo entityService, String opID, Object dataObject, ICuiFormInfo cuiFormService, List<String> fields, List<String> allowFields) {
		if (fields == null || fields.size() == 0)
			return;

		UIField uiField;
		ICocFieldInfo cocFieldService;
		ICuiFormFieldInfo cuiFieldService;
		String fieldName, title, uiView, linkUrl, linkTarget, align, halign, modeStr;
		UIForm oneToManyTargetForm;
		int mode = 0;
		byte labelPos = 0, colspan, rowspan;
		Properties fieldAttributes;
		Option[] dicOptions;

		for (String field : fields) {

			if (allowFields != null && !allowFields.contains(field)) {
				continue;
			}

			fieldAttributes = new Properties();
			title = null;
			uiView = null;
			linkUrl = null;
			linkTarget = null;
			align = null;
			halign = null;
			modeStr = null;
			mode = 0;
			labelPos = 0;
			colspan = 0;
			rowspan = 0;
			dicOptions = null;
			oneToManyTargetForm = null;

			int idx = field.indexOf(":");
			if (idx > -1) {
				fieldName = field.substring(0, idx);
				modeStr = field.substring(idx + 1);
			} else {
				fieldName = field;
			}
			cocFieldService = entityService.getField(fieldName);
			cuiFieldService = cuiFormService.getFormField(fieldName);

			/*
			 * 字段已被禁用
			 */
			if (cocFieldService == null || cocFieldService.isDisabled())
				continue;

			/*
			 * 计算字段UI属性
			 */
			if (cuiFieldService != null) {
				// mode = cuiFieldService.getModeValue();
				title = cuiFieldService.getName();
				uiView = cuiFieldService.getUiView();
				align = cuiFieldService.getAlign();
				halign = cuiFieldService.getHalign();
				if (StringUtil.hasContent(cuiFieldService.getStyle())) {
					fieldAttributes.setProperty("style", cuiFieldService.getStyle());
				}
				if (StringUtil.hasContent(cuiFieldService.getStyleClass())) {
					fieldAttributes.setProperty("styleClass", cuiFieldService.getStyleClass());
				}
				labelPos = cuiFieldService.getLabelPos();
				linkUrl = cuiFieldService.getUiViewLinkUrl();
				linkTarget = cuiFieldService.getUiViewLinkTarget();
				colspan = cuiFieldService.getColspan();
				rowspan = cuiFieldService.getRowspan();
				dicOptions = cuiFieldService.getDicOptionsArray();
				String oneToManyTargetAction = cuiFieldService.getOneToManyTargetAction();
				if (StringUtil.hasContent(oneToManyTargetAction)) {
					ICocEntityInfo manyTargetEntity = cocFieldService.getOneToManyTargetEntity();
					String manyProp = cuiFieldService.getFieldName();
					Object manyFieldValue = ObjectUtil.getValue(dataObject, manyProp);
					oneToManyTargetForm = this.getForm(menuService, manyTargetEntity, manyTargetEntity.getAction(oneToManyTargetAction), manyFieldValue);
					String beanName = form.getBeanName() + "." + manyProp;
					oneToManyTargetForm.setBeanName(beanName);
					oneToManyTargetForm.setViewName(ViewNames.VIEW_SUBFORM);
				}
			}
			mode = FieldModes.parseMode(modeStr);
			if (mode == 0) {
				mode = cocFieldService.getMode(opID, dataObject);
			}
			if (StringUtil.isBlank(uiView)) {
				uiView = cocFieldService.getUiView();
			}

			if (mode == 0) {
				if (opID.startsWith("v")) {
					mode = FieldModes.S;
				} else {
					if (//
					fieldName.equals(Const.F_STATUS_CODE) || //
					        fieldName.equals(Const.F_CREATED_DATE) || //
					        fieldName.equals(Const.F_CREATED_USER) || //
					        fieldName.equals(Const.F_UPDATED_DATE) || //
					        fieldName.equals(Const.F_UPDATED_USER) || //
					        fieldName.equals(Const.F_UPDATED_OP_LOG) || //
					        fieldName.equals(Const.F_UPDATED_OP_LOG) || //
					        fieldName.equals(Const.F_VERSION)//
					) {
						mode = FieldModes.N;
					} else {
						mode = FieldModes.E;
					}
				}
			}

			uiField = new UIField()//
			        .setFieldService(cocFieldService)//
			        .setMode(mode)//
			        .setWidth(200)//
			        ;

			if (StringUtil.hasContent(align)) {
				uiField.setAlign(align);
			}
			if (StringUtil.hasContent(halign)) {
				uiField.setHalign(halign);
			}
			if (StringUtil.hasContent(title)) {
				uiField.setTitle(title);
			}
			if (StringUtil.hasContent(uiView)) {
				uiField.setViewName(uiView);
			} else {
				uiField.setViewName(this.getDefaultFormFieldView(cocFieldService));
			}
			uiField.setAttributes(fieldAttributes);
			uiField.setLabelPos(labelPos);
			uiField.setLinkUrl(linkUrl);
			uiField.setLinkTarget(linkTarget);
			uiField.setColspan(colspan);
			uiField.setRowspan(rowspan);
			if (dicOptions != null && dicOptions.length > 0) {
				uiField.setDicOptions(dicOptions);
			}
			uiField.setOne2ManyTargetForm(oneToManyTargetForm);
			if (StringUtil.hasContent(title))
				uiField.setTitle(title);

			if (uiFieldGroup != null) {
				uiFieldGroup.addField(uiField);
			}

			form.addField(uiField);
		}

	}

	private UIField makeField(ISystemMenuInfo menuService, ICocFieldInfo cocFieldService, ICuiFormFieldInfo cuiFieldService) {
		String title, uiView, linkUrl, linkTarget, align, halign;
		UIForm oneToManyTargetForm;
		int mode = 0;
		byte labelPos = 0, colspan, rowspan;
		Properties fieldAttributes;
		Option[] dicOptions;
		/*
		 * 初始化值
		 */
		fieldAttributes = new Properties();
		title = null;
		uiView = null;
		linkUrl = null;
		linkTarget = null;
		align = null;
		halign = null;
		mode = 0;
		labelPos = 0;
		colspan = 0;
		rowspan = 0;
		dicOptions = null;
		oneToManyTargetForm = null;

		/*
		 * 计算字段UI属性
		 */
		if (cuiFieldService != null) {
			// mode = cuiFieldService.getModeValue();
			title = cuiFieldService.getName();
			uiView = cuiFieldService.getUiView();
			align = cuiFieldService.getAlign();
			halign = cuiFieldService.getHalign();
			if (StringUtil.hasContent(cuiFieldService.getStyle())) {
				fieldAttributes.setProperty("style", cuiFieldService.getStyle());
			}
			if (StringUtil.hasContent(cuiFieldService.getStyleClass())) {
				fieldAttributes.setProperty("styleClass", cuiFieldService.getStyleClass());
			}
			labelPos = cuiFieldService.getLabelPos();
			linkUrl = cuiFieldService.getUiViewLinkUrl();
			linkTarget = cuiFieldService.getUiViewLinkTarget();
			colspan = cuiFieldService.getColspan();
			rowspan = cuiFieldService.getRowspan();
			dicOptions = cuiFieldService.getDicOptionsArray();
			// String oneToManyTargetAction = cuiFieldService.getOneToManyTargetAction();
			// if (StringUtil.hasContent(oneToManyTargetAction)) {
			// CocEntityService manyTargetEntity = cocFieldService.getOneToManyTargetEntity();
			// String manyProp = cuiFieldService.getFieldName();
			// oneToManyTargetForm = this.getUIForm(menuService, manyTargetEntity, manyTargetEntity.getAction(oneToManyTargetAction), manyFieldValue);
			// String beanName = form.getBeanName() + "." + manyProp;
			// oneToManyTargetForm.setBeanName(beanName);
			// oneToManyTargetForm.setViewName(ViewNames.VIEW_SUBFORM);
			// }
		} else {
			CndExpr cndExpr = menuService.getWhere();
			List<Option> options = new ArrayList();
			for (Option opt : cocFieldService.getDicOptionsArray()) {
				if (ExprUtil.match(cocFieldService.getFieldName(), opt.getValue(), cndExpr)) {
					options.add(opt);
				}
			}
			dicOptions = new Option[options.size()];
			for (int i = options.size() - 1; i >= 0; i--) {
				dicOptions[i] = options.get(i);
			}
		}
		// if (mode == 0) {
		// mode = cocFieldService.getMode(opID, dataObject);
		// }
		if (StringUtil.isBlank(uiView)) {
			uiView = cocFieldService.getUiView();
		}

		UIField uiField = new UIField()//
		        .setFieldService(cocFieldService)//
		        .setMode(mode)//
		        .setWidth(200)//
		        ;

		if (StringUtil.hasContent(align)) {
			uiField.setAlign(align);
		}
		if (StringUtil.hasContent(halign)) {
			uiField.setHalign(halign);
		}
		if (StringUtil.hasContent(title)) {
			uiField.setTitle(title);
		}
		if (StringUtil.hasContent(uiView)) {
			uiField.setViewName(uiView);
		} else {
			uiField.setViewName(this.getDefaultFormFieldView(cocFieldService));
		}
		uiField.setAttributes(fieldAttributes);
		uiField.setLabelPos(labelPos);
		uiField.setLinkUrl(linkUrl);
		uiField.setLinkTarget(linkTarget);
		uiField.setColspan(colspan);
		uiField.setRowspan(rowspan);
		if (dicOptions != null && dicOptions.length > 0) {
			uiField.setDicOptions(dicOptions);
		}
		uiField.setOne2ManyTargetForm(oneToManyTargetForm);
		if (StringUtil.hasContent(title))
			uiField.setTitle(title);

		return uiField;
	}

	private void evalUIFormFields(UIForm form, ICocEntityInfo entityService, String opID, Object dataObject, List<String> fieldList) {

		List<ICocGroupInfo> groups = entityService.getGroups();
		UIFieldGroup fieldGroup;
		UIField field;

		for (ICocGroupInfo group : groups) {
			fieldGroup = new UIFieldGroup().setTitle(group.getName()).setId(group.getCode());

			List<ICocFieldInfo> fields = group.getFields();
			if (fields == null) {
				continue;
			}

			List<List<String>> rowFieldsList = new ArrayList();
			List<String> rowFields = null;

			for (ICocFieldInfo fld : fields) {
				if (fld.isDisabled()) {
					continue;
				}

				String propName = fld.getFieldName();
				if (fieldList != null && !fieldList.contains(propName)) {
					continue;
				}

				int mode = fld.getMode(opID, dataObject);

				/*
				 * 计算字段模式
				 */
				if (mode == 0) {
					if (opID.startsWith("e") || opID.startsWith("c")) {
						if (//
						propName.equals(Const.F_STATUS_CODE) || //
						        propName.equals(Const.F_CREATED_DATE) || //
						        propName.equals(Const.F_CREATED_USER) || //
						        propName.equals(Const.F_UPDATED_DATE) || //
						        propName.equals(Const.F_UPDATED_USER) || //
						        propName.equals(Const.F_UPDATED_OP_LOG) || //
						        propName.equals(Const.F_UPDATED_OP_LOG) || //
						        propName.equals(Const.F_VERSION)//
						) {
							mode = FieldModes.N;
						} else {
							mode = FieldModes.E;
						}
					} else if (opID.startsWith("v")) {
						mode = FieldModes.S;
					}
				}
				if (StringUtil.hasContent(fld.getFkDependFieldCode())) {
					mode = FieldModes.N;
				}

				/*
				 * 创建字段
				 */
				field = new UIField()//
				        .setFieldService(fld)//
				        .setMode(mode)//
				        .setWidth(200)//
				        ;

				if (StringUtil.hasContent(fld.getUiView())) {
					field.setViewName(fld.getUiView());
				} else {
					field.setViewName(this.getDefaultFormFieldView(fld));
				}

				fieldGroup.addField(field);
				form.addField(field);

			}

			form.addFieldGroup(fieldGroup);

		}
	}

	private String getDefaultFormFieldView(ICocFieldInfo fieldService) {
		int fieldType = fieldService.getFieldType();

		if (fieldService.isDic() && fieldType != Const.FIELD_TYPE_BOOLEAN) {
			return ViewNames.FIELD_VIEW_DIC;
		}

		switch (fieldType) {
			case Const.FIELD_TYPE_BOOLEAN:
				return ViewNames.FIELD_VIEW_RADIO;
			case Const.FIELD_TYPE_BYTE:
				return ViewNames.FIELD_VIEW_NUMBERBOX;
			case Const.FIELD_TYPE_DATE:
				return ViewNames.FIELD_VIEW_COMBODATE;
			case Const.FIELD_TYPE_DECIMAL:
				return ViewNames.FIELD_VIEW_NUMBERBOX;
			case Const.FIELD_TYPE_DOUBLE:
				return ViewNames.FIELD_VIEW_NUMBERBOX;
			case Const.FIELD_TYPE_FK:
				Class fldClass = fieldService.getFkTargetEntity().getClassOfEntity();
				if (ITreeEntity.class.isAssignableFrom(fldClass) || ITreeObjectEntity.class.isAssignableFrom(fldClass)) {
					return ViewNames.FIELD_VIEW_COMBOTREE;
				}

				return ViewNames.FIELD_VIEW_COMBOGRID;
			case Const.FIELD_TYPE_FLOAT:
				return ViewNames.FIELD_VIEW_NUMBERBOX;
			case Const.FIELD_TYPE_INTEGER:
				return ViewNames.FIELD_VIEW_NUMBERBOX;
			case Const.FIELD_TYPE_LONG:
				return ViewNames.FIELD_VIEW_NUMBERBOX;
			case Const.FIELD_TYPE_RICHTEXT:
				return ViewNames.FIELD_VIEW_CKEDITOR;
			case Const.FIELD_TYPE_SHORT:
				return ViewNames.FIELD_VIEW_NUMBERBOX;
			case Const.FIELD_TYPE_TEXT:
				return ViewNames.FIELD_VIEW_TEXTAREA;
			case Const.FIELD_TYPE_UPLOAD:
				return ViewNames.FIELD_VIEW_UPLOAD;
			case Const.FIELD_TYPE_ONE2MANY:
				return ViewNames.FIELD_VIEW_ONE2MANY;
			case Const.FIELD_TYPE_STRING:
				if (fieldService.isText()) {
					return ViewNames.FIELD_VIEW_TEXTAREA;
				}
		}

		return ViewNames.FIELD_VIEW_DEFAULT;
	}

	@Override
	public UIGridData makeGridData() {
		UIGridData ret = new UIGridData();
		ret.setModel((UIGrid) makeGrid());

		return ret;
	}

	@Override
	public UIGrid makeGrid() {
		return new UIGrid();
	}

	@Override
	public UIField makeField() {
		return new UIField();
	}
}
