// $codepro.audit.disable unnecessaryImport
package com.jsoft.cocimpl.ui.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.constant.StatusCodes;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.entityengine.service.CocGroupService;
import com.jsoft.cocit.entityengine.service.CuiEntityService;
import com.jsoft.cocit.entityengine.service.CuiFormActionService;
import com.jsoft.cocit.entityengine.service.CuiFormFieldService;
import com.jsoft.cocit.entityengine.service.CuiFormService;
import com.jsoft.cocit.entityengine.service.CuiGridFieldService;
import com.jsoft.cocit.entityengine.service.CuiGridService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.ui.UIModelFactory;
import com.jsoft.cocit.ui.model.control.UIActions;
import com.jsoft.cocit.ui.model.control.UIEntities;
import com.jsoft.cocit.ui.model.control.UIEntity;
import com.jsoft.cocit.ui.model.control.UIEntityContainer;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UIFieldGroup;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.model.control.UIList;
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
	public UIEntities getMains(SystemMenuService menuService) {
		if (menuService == null)
			throw new CocException("未知系统菜单！");

		/*
		 * 创建模块 Grid
		 */
		CocEntityService entityService = menuService.getCocEntity();

		/*
		 * 获取需要引用的主界面
		 */
		String cuiKey = menuService.getUiView();
		if (StringUtil.isBlank(cuiKey)) {
			cuiKey = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		List<String> cols = null;
		List<String> rows = null;
		CuiEntityService cui = entityService.getCuiEntity(cuiKey);
		if (cui != null) {
			cols = cui.getColsList();
			rows = cui.getRowsList();
		}

		/*
		 * 计算实体UI
		 */
		UIEntity uiEntity = getMain(menuService, entityService, false);

		/*
		 * 创建模块 Panels
		 */
		UIEntities panels = new UIEntities()//
		        .setId(makeHtmlID(UIEntities.class, menuService.getId()));
		panels.setRows(rows);
		panels.setCols(cols);

		/*
		 * 创建主模块 Panel
		 */
		UIEntityContainer panel = UIEntityContainer.make(uiEntity)//
		        .setPanelUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_GET_MAIN_UI, menuService, entityService) + "/1/1")//
		        .setAjax(true)//
		        .setId(makeHtmlID(UIEntityContainer.class, menuService.getId()))//
		        .setTitle(menuService.getName())//
		;

		/*
		 * 添加主模块 Panel 到 Panels
		 */
		panels.addPanel(panel);

		/*
		 * 创建子模块 Panel
		 */
		List<CocEntityService> subModuleServices = entityService.getSubEntities();
		if (subModuleServices != null) {
			for (CocEntityService subModule : subModuleServices) {

				CocFieldService subModuleFkField = entityService.getFkFieldOfSubEntity(subModule.getKey());

				/*
				 * 创建子模块 Panel
				 */
				panel = UIEntityContainer.make(null)//
				        .setPanelUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_GET_MAIN_UI, menuService, subModule) + "/1/1")//
				        .set("fkField", subModuleFkField.getFieldName())//
				        .set("fkTargetField", subModuleFkField.getFkTargetFieldKey())//
				        .setId(makeHtmlID(UIEntityContainer.class, subModule.getId()))//
				        .setTitle(subModule.getName())//
				;

				/*
				 * 添加子模块 Panel 到 Panels
				 */
				panels.addPanel(panel);
			}
		}

		/*
		 * 返回
		 */
		return panels;
	}

	@Override
	public UIEntity getMain(SystemMenuService menuService, CocEntityService entityService, boolean usedToSubEntity) {

		/*
		 * 获取需要引用的主界面
		 */
		String cuiKey = menuService.getUiView();
		if (StringUtil.isBlank(cuiKey)) {
			cuiKey = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		String title = null;
		byte actionsPos = 0;
		byte searchBoxPos = 0;
		String uiView = null;
		CuiEntityService cui = entityService.getCuiEntity(cuiKey);
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
		model.setSearchBoxPos(searchBoxPos);

		/*
		 * 检查当前GRID是否支持行编辑
		 */
		Node insertNode = actions.findAction("opCode", "" + OpCodes.OP_INSERT_GRID_ROW);
		Node updateNode = actions.findAction("opCode", "" + OpCodes.OP_UPDATE_GRID_ROW);
		if (insertNode != null) {
			grid.setDataAddUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_SAVE, menuService, entityService, insertNode.getId()));
		}
		if (updateNode != null) {
			grid.setDataEditUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_SAVE, menuService, entityService, updateNode.getId()));
			// updateNode.setStatusCode("" + StatusCodes.STATUS_CODE_DISABLED);
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
	public UISearchBox getSearchBox(SystemMenuService menuService, CocEntityService entityService) {

		/*
		 * 获取需要引用的主界面
		 */
		String cuiKey = menuService.getUiView();
		if (StringUtil.isBlank(cuiKey)) {
			cuiKey = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		List<String> queryFields = null;
		CuiEntityService cui = entityService.getCuiEntity(cuiKey);
		if (cui != null) {
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
			List<CocFieldService> fields = entityService.getFieldsOfGrid(queryFields);
			for (CocFieldService fieldService : fields) {
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
	public UIGrid getGrid(SystemMenuService menuService, CocEntityService entityService) {

		/*
		 * 获取需要引用的主界面
		 */
		String cuiKey = menuService.getUiView();
		if (StringUtil.isBlank(cuiKey)) {
			cuiKey = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		CuiGridService cuiGrid = null;
		List<String> allFieldNames = null;
		List<String> frozenFieldNames = null;
		List<String> fieldNames = null;
		CuiEntityService cui = entityService.getCuiEntity(cuiKey);
		if (cui != null) {
			cuiGrid = cui.getCuiGrid();
			if (cuiGrid != null) {
				allFieldNames = new ArrayList();
				frozenFieldNames = cuiGrid.getFrozenFieldsList();
				allFieldNames.addAll(frozenFieldNames);
				fieldNames = cuiGrid.getFieldsList();
				allFieldNames.addAll(fieldNames);
			}
		}

		/*
		 * 创建 Grid
		 */
		UIGrid model = new UIGrid()//
		        .setDataLoadUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_GET_GRID_DATA, menuService, entityService))//
		        .setId(makeHtmlID(UIGrid.class, entityService.getId()))//
		        .setTitle(entityService.getName())//
		;
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

			String rowActions = cuiGrid.getRowActions();
			if (StringUtil.hasContent(rowActions)) {
				List<String> actionIDList = StringUtil.toList(rowActions, "|,， ");
				Tree data = getActionsData(menuService, entityService, null, null, actionIDList);
				UIActions rowUIActions = new UIActions().setData(data).setId(makeHtmlID(UIActions.class, entityService.getId()));

				String rowActionsView = cuiGrid.getRowActionsView();
				if (StringUtil.hasContent(rowActionsView))
					rowUIActions.setViewName(rowActionsView);
				// else
				// rowUIActions.setViewName(ViewNames.CELL_VIEW_ROW_ACTIONS);

				rowUIActions.addResultUI(model.getId());

				model.setRowActions(rowUIActions);
				model.setRowActionsPos(cuiGrid.getRowActionsPos());
			}

			model.setRowStyles(cuiGrid.getRowStyles());
			model.setSortExpr(cuiGrid.getSortExpr());
			model.setTreeField(cuiGrid.getTreeField());
			model.setViewName(cuiGrid.getUiView());
		}

		/*
		 * 创建 Grid 列
		 */
		List<CocFieldService> fields = entityService.getFieldsOfGrid(allFieldNames);
		CuiGridFieldService cuiField = null;
		int columnsTotalWidth = 0;
		int width = -1;
		boolean hidden = false, showCellTips = false;
		String title = null, cellView = null;
		String linkUrl = null;
		String linkTarget = null;
		String align = null, halign = null;
		StyleRule[] styleRules = null;
		for (CocFieldService fld : fields) {
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
				cuiField = cuiGrid.getField(fld.getKey());
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
						default:
							width = 150;
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

			columnsTotalWidth += width;

			model.addColumn(col);
		}

		model.setColumnsTotalWidth(columnsTotalWidth);
		model.setEntityKey(entityService.getKey());

		/*
		 * 返回
		 */
		return model;
	}

	@Override
	public UIGrid getComboGrid(SystemMenuService targetMenuService, CocEntityService targetEntityService, CocFieldService fkFieldService) {

		UIGrid model = new UIGrid();
		model.setId(makeHtmlID(UIGrid.class, targetEntityService.getId()));
		model.setName(targetEntityService.getName());
		String url = fkFieldService.getFkComboUrl();
		if (StringUtil.isBlank(url)) {
			url = MVCUtil.makeUrl(UrlAPI.ENTITY_GET_COMBOGRID_DATA, targetMenuService, targetEntityService);
			url += "/" + fkFieldService.getCocEntityKey() + ":" + fkFieldService.getFieldName();
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
		CocFieldService nameField = targetEntityService.getField(Const.F_NAME);
		if (nameField == null) {
			throw new CocException("实体模块字段不存在！%s.%s", targetEntityService.getClassOfEntity().getName(), Const.F_NAME);
		}

		UIField col = new UIField().setFieldService(nameField).setWidth(150);
		model.addColumn(col);

		/*
		 * KEY字段
		 */
		CocFieldService keyField = targetEntityService.getField(Const.F_KEY);
		if (keyField == null) {
			throw new CocException("实体模块字段不存在！%s.%s", targetEntityService.getClassOfEntity().getName(), Const.F_KEY);
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
	public UIList getComboList(SystemMenuService menuService, CocEntityService entityService, CocFieldService fkFieldService) {
		String url = fkFieldService.getFkComboUrl();
		if (StringUtil.isBlank(url)) {
			url = MVCUtil.makeUrl(UrlAPI.ENTITY_GET_COMBOLIST_DATA, menuService, entityService);
			url += "/" + fkFieldService.getCocEntityKey() + ":" + fkFieldService.getFieldName();
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
	public UITree getComboTree(SystemMenuService menuService, CocEntityService entityService, CocFieldService fkFieldService) {
		String url = fkFieldService.getFkComboUrl();
		if (StringUtil.isBlank(url)) {
			url = MVCUtil.makeUrl(UrlAPI.ENTITY_GET_COMBOTREE_DATA, menuService, entityService);
			url += "/" + fkFieldService.getCocEntityKey() + ":" + fkFieldService.getFieldName();
		} else {
			url = MVCUtil.makeUrl(url);
		}
		UITree model = new UITree();
		model.setDataLoadUrl(url);
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
	public UITreeData getComboTreeData(SystemMenuService menuService, CocEntityService entityService, CndExpr expr) {
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
	public UIActions getActions(SystemMenuService menuService, CocEntityService entityService) {

		List<String> actionIDList = menuService.getActionKeysWithoutRow();

		/*
		 * 获取需要引用的主界面
		 */
		String cuiKey = menuService.getUiView();
		if (StringUtil.isBlank(cuiKey)) {
			cuiKey = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		String actionsView = null;
		CuiEntityService cui = entityService.getCuiEntity(cuiKey);
		if (cui != null) {
			if (actionIDList == null || actionIDList.size() == 0) {
				actionIDList = cui.getActionsList();
			}
			actionsView = cui.getActionsView();
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
			model.setViewName(ViewNames.VIEW_BUTTONS);

		/*
		 * 返回
		 */
		return model;
	}

	private Tree getActionsData(SystemMenuService menuService, CocEntityService entityService, CocActionService entityAction, Object dataObject, List<String> actionIDList) {

		String theCurrentAction = "";
		String dataID = "";
		if (entityAction != null) {
			theCurrentAction = entityAction.getKey();
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

		/*
		 * 创建操作菜单
		 */
		Tree data = Tree.make();
		List<CocActionService> actionServices = entityService.getActions(actionIDList);
		for (CocActionService action : actionServices) {
			String actionKey = action.getKey();

			/*
			 * 创建节点ID
			 */
			String parentNodeID = action.getParentKey();
			if (parentNodeID == "") {
				parentNodeID = null;
			}
			String nodeID = action.getKey();

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
			switch (action.getOpCode()) {
			/*
			 * 打开操作表单
			 */
				case OpCodes.OP_INSERT_FORM_DATA:
					if (theCurrentAction.equals(actionKey)) {
						urlAPI = UrlAPI.ENTITY_SAVE;
					} else {
						urlAPI = UrlAPI.ENTITY_GET_FORM_TO_SAVE;
					}
					break;
				case OpCodes.OP_UPDATE_FORM_DATA:
				case OpCodes.OP_UPDATE_FORM_DATAS:
					if (theCurrentAction.equals(actionKey)) {
						urlAPI = UrlAPI.ENTITY_SAVE;
					} else {
						urlAPI = UrlAPI.ENTITY_GET_FORM_TO_SAVE;
					}
					break;
				case OpCodes.OP_REMOVE_FORM_DATA:
				case OpCodes.OP_REMOVE_FORM_DATAS:
					if (theCurrentAction.equals(actionKey)) {
						urlAPI = UrlAPI.ENTITY_REMOVE;
					} else {
						urlAPI = UrlAPI.ENTITY_GET_FORM_TO_REMOVE;
					}
					break;
				case OpCodes.OP_DELETE_FORM_DATA:
				case OpCodes.OP_DELETE_FORM_DATAS:
					if (theCurrentAction.equals(actionKey)) {
						urlAPI = UrlAPI.ENTITY_DELETE;
					} else {
						urlAPI = UrlAPI.ENTITY_GET_FORM_TO_DELETE;
					}
					break;
				case OpCodes.OP_RUN_FORM_DATA:
				case OpCodes.OP_RUN_FORM_DATAS:
				case OpCodes.OP_RUN_FORM:
					if (theCurrentAction.equals(actionKey)) {
						urlAPI = UrlAPI.ENTITY_RUN;
					} else {
						urlAPI = UrlAPI.ENTITY_GET_FORM_TO_RUN;
					}
					break;
				case OpCodes.OP_EXPORT_XLS:
					urlAPI = UrlAPI.ENTITY_GET_FORM_TO_EXPORT_XLS;
					break;
				case OpCodes.OP_IMPORT_XLS:
					urlAPI = UrlAPI.ENTITY_GET_FORM_TO_IMPORT_XLS;
					break;
				/*
				 * 以下操作不弹出表单
				 */
				case OpCodes.OP_UPDATE_ROW:
				case OpCodes.OP_UPDATE_ROWS:
					urlAPI = UrlAPI.ENTITY_SAVE;
					break;
				case OpCodes.OP_REMOVE_ROW:
				case OpCodes.OP_REMOVE_ROWS:
					urlAPI = UrlAPI.ENTITY_REMOVE;
					break;
				case OpCodes.OP_DELETE_ROW:
				case OpCodes.OP_DELETE_ROWS:
					urlAPI = UrlAPI.ENTITY_DELETE;
					break;
				case OpCodes.OP_RUN_ROW:
				case OpCodes.OP_RUN_ROWS:
				case OpCodes.OP_RUN:
					urlAPI = UrlAPI.ENTITY_RUN;
					break;
				case OpCodes.OP_CLEAR:
					urlAPI = UrlAPI.ENTITY_CLEAR;
					break;
				/*
				 * 排序
				 */
				case OpCodes.OP_SORT_TOP:
					urlAPI = UrlAPI.ENTITY_SORT_MOVE_TOP;
					break;
				case OpCodes.OP_SORT_UP:
					urlAPI = UrlAPI.ENTITY_SORT_MOVE_UP;
					break;
				case OpCodes.OP_SORT_DOWN:
					urlAPI = UrlAPI.ENTITY_SORT_MOVE_DOWN;
					break;
				case OpCodes.OP_SORT_BOTTOM:
					urlAPI = UrlAPI.ENTITY_SORT_MOVE_BOTTOM;
					break;
				case OpCodes.OP_SORT_REVERSE:
					urlAPI = UrlAPI.ENTITY_SORT_REVERSE;
					break;
				case OpCodes.OP_SORT_CANCEL:
					urlAPI = UrlAPI.ENTITY_SORT_CANCEL;
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
			child.set("opMode", action.getKey());
			child.set("title", action.getTitle());
			if (action.getUiWindowHeight() > 0)
				child.set("windowHeight", action.getUiWindowHeight());
			if (action.getUiWindowWidth() > 0)
				child.set("windowWidth", action.getUiWindowWidth());
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
	public UITree getFilter(SystemMenuService menuService, CocEntityService entityService, boolean usedToSubEntity) {
		/*
		 * 获取需要引用的主界面
		 */
		String cuiKey = menuService.getUiView();
		if (StringUtil.isBlank(cuiKey)) {
			cuiKey = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		List<String> filterFields = null;
		String fielterFieldsView = null;
		CuiEntityService cui = entityService.getCuiEntity(cuiKey);
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
		if (filterFields != null && filterFields.size() > 0) {
			model.setTitle(entityService.getField(filterFields.get(0)).getName());//
		} else {
			model.setTitle("快速过滤");//
		}

		/*
		 * 设置异步加载数据的 URL 地址
		 */
		String url = MVCUtil.makeUrl(UrlAPI.ENTITY_GET_FILTER_DATA, menuService, entityService);
		model.setDataLoadUrl(url + "/" + usedToSubEntity);

		/*
		 * 返回
		 */
		return model;
	}

	@Override
	public UITreeData getFilterData(SystemMenuService menuService, CocEntityService entityService, boolean usedToSubEntity) {
		/*
		 * 获取需要引用的主界面
		 */
		String cuiKey = menuService.getUiView();
		if (StringUtil.isBlank(cuiKey)) {
			cuiKey = entityService.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		List<String> filterFields = null;
		CuiEntityService cui = entityService.getCuiEntity(cuiKey);
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
	public UITreeData getRowsAuthData(SystemMenuService menuService, CocEntityService entityService) {
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
	public UITreeData getActionsData(SystemMenuService menuService, CocEntityService entityService) {

		/*
		 * 创建操作菜单
		 */
		Tree tree = Tree.make();
		String rootNode = menuService.getKey() + "_actions";
		Node node = tree.addNode(null, rootNode).setName("全部");
		node.set("open", "true");
		node.set("type", "folder");

		List<CocActionService> actionServices = menuService.getCocActions();
		for (CocActionService action : actionServices) {
			/*
			 * 创建节点ID
			 */
			String parentNodeID = action.getParentKey();
			if (parentNodeID == null || parentNodeID.trim().length() == 0) {
				parentNodeID = rootNode;
			}
			String nodeID = action.getKey();

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
	public UITree getTree(SystemMenuService menuService, CocEntityService entityService) {
		// if (entityService.getFieldOfTree() == null){
		// entityService.getFieldOfGroup();
		// return null;
		// }

		/*
		 * 创建树模型
		 */
		UITree model = new UITree();
		model.setDataLoadUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_GET_TREE_DATA, menuService, entityService));
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
	public UITreeData getTreeData(SystemMenuService menuService, CocEntityService entityService) {
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
		Tree data = entityService.getTreeData(null);

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
	public UIForm getForm(SystemMenuService menuService, CocEntityService entityService, CocActionService entityAction, Object dataObject) {
		UIForm form = new UIForm();

		if (entityAction == null) {
			throw new CocException("操作不存在！");
		}

		form.setActionID(entityAction.getKey());

		String cuiFormKey = entityAction.getUiForm();

		/*
		 * 获取需要引用的主界面
		 */
		String cuiKey = menuService.getUiView();
		if (StringUtil.isBlank(cuiKey)) {
			cuiKey = entityService.getUiView();
		}

		/*
		 * 获取表单UI服务
		 */
		CuiFormService cuiFormService = null;
		CuiEntityService cuiEntityService = entityService.getCuiEntity(cuiKey);
		if (cuiEntityService != null) {
			cuiFormService = cuiEntityService.getCuiForm(cuiFormKey);
		}

		/*
		 * 计算表单字段
		 */
		if (cuiFormService == null) {
			if (!StringUtil.isBlank(cuiFormKey)) {
				form.setViewName(cuiFormKey);
			}

			evalUIFormField(form, entityService, entityAction.getKey(), dataObject);
		} else {
			evalUIFormField(form, menuService, entityService, entityAction.getKey(), dataObject, cuiFormService);

			List<String> batchFields = cuiFormService.getBatchFieldsList();
			form.setBatchFields(batchFields);
			this.evalUIFormField(form, menuService, entityService, entityAction.getKey(), dataObject, cuiFormService, batchFields);

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
					String actionKey = node.getId();
					CuiFormActionService action = cuiFormService.getFormAction(actionKey);
					if (action != null && StringUtil.hasContent(action.getName())) {
						node.setName(action.getName());
					}
				}

				UIActions uiActions = new UIActions().setData(data).setId(makeHtmlID(UIActions.class, entityService.getId()));

				form.setActions(uiActions);
			}
		}

		return form;
	}

	private void evalUIFormField(UIForm form, SystemMenuService menuService, CocEntityService entityService, String opID, Object dataObject, CuiFormService cuiFormService) {
		List<List<String>> fields = cuiFormService.getFieldsList();
		form.setFields(fields);

		int fieldRowSize = 0;
		for (List<String> fieldRow : fields) {
			if (fieldRow.size() > fieldRowSize) {
				fieldRowSize = fieldRow.size();
			}

			this.evalUIFormField(form, menuService, entityService, opID, dataObject, cuiFormService, fieldRow);
		}

		form.setRowFieldsSize(fieldRowSize);

	}

	private void evalUIFormField(UIForm form, SystemMenuService menuService, CocEntityService entityService, String opID, Object dataObject, CuiFormService cuiFormService, List<String> fields) {
		if (fields == null || fields.size() == 0)
			return;

		UIFieldGroup uiFieldGroup = null;
		UIField uiField;
		CocGroupService cocGroupService;
		CocFieldService cocFieldService;
		CuiFormFieldService cuiFieldService;
		String fieldName, groupName, title, uiView, linkUrl, linkTarget, align, halign;
		UIForm oneToManyTargetForm;
		int mode = 0;
		byte labelPos = 0, colspan, rowspan;
		Properties fieldAttributes;
		Option[] dicOptions;

		for (String field : fields) {
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

			int idx = field.indexOf(":");
			if (idx > -1) {
				groupName = field.substring(0, idx);
				fieldName = field.substring(idx + 1);

				cocGroupService = entityService.getGroup(groupName);
				uiFieldGroup = new UIFieldGroup().setId(groupName).setTitle(cocGroupService.getName());
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
				mode = cuiFieldService.getModeValue();
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
					CocEntityService manyTargetEntity = cocFieldService.getOneToManyTargetEntity();
					String manyProp = cuiFieldService.getFieldName();
					Object manyFieldValue = ObjectUtil.getValue(dataObject, manyProp);
					oneToManyTargetForm = this.getForm(menuService, manyTargetEntity, manyTargetEntity.getAction(oneToManyTargetAction), manyFieldValue);
					String beanName = form.getBeanName() + "." + manyProp;
					oneToManyTargetForm.setBeanName(beanName);
					oneToManyTargetForm.setViewName(ViewNames.VIEW_SUBFORM);
				}
			}
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

	private UIField makeField(SystemMenuService menuService, CocFieldService cocFieldService, CuiFormFieldService cuiFieldService) {
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
			mode = cuiFieldService.getModeValue();
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

	private void evalUIFormField(UIForm form, CocEntityService entityService, String opID, Object dataObject) {

		List<CocGroupService> groups = entityService.getGroups();
		UIFieldGroup fieldGroup;
		UIField field;

		for (CocGroupService group : groups) {
			fieldGroup = new UIFieldGroup().setTitle(group.getName()).setId(group.getKey());

			List<CocFieldService> fields = group.getFields();
			if (fields == null) {
				continue;
			}

			for (CocFieldService fld : fields) {
				if (fld.isDisabled())
					continue;

				int mode = fld.getMode(opID, dataObject);
				String propName = fld.getFieldName();

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

	private String getDefaultFormFieldView(CocFieldService fieldService) {

		if (fieldService.isDic())
			return ViewNames.FIELD_VIEW_DIC;

		switch (fieldService.getFieldType()) {
			case Const.FIELD_TYPE_BOOLEAN:
				return ViewNames.FIELD_VIEW_SELECT;
			case Const.FIELD_TYPE_BYTE:
				return ViewNames.FIELD_VIEW_NUMBERBOX;
			case Const.FIELD_TYPE_DATE:
				return ViewNames.FIELD_VIEW_COMBODATE;
			case Const.FIELD_TYPE_DECIMAL:
				return ViewNames.FIELD_VIEW_NUMBERBOX;
			case Const.FIELD_TYPE_DOUBLE:
				return ViewNames.FIELD_VIEW_NUMBERBOX;
			case Const.FIELD_TYPE_FK:
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
