package com.jsoft.cocimpl.action;

import java.util.List;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.mvc.CocEntityParam;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.model.control.UIList;
import com.jsoft.cocit.ui.model.datamodel.UIFormData;
import com.jsoft.cocit.ui.model.datamodel.UIGridData;
import com.jsoft.cocit.ui.model.datamodel.UIListData;
import com.jsoft.cocit.ui.model.datamodel.UITreeData;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 实体Action：即用来管理实体数据的Action，负责接收管理实体数据的请求并处理这些请求，包括“增加、删除、查询、修改、导入、导出”等操作。
 * 
 * @author jiongs753
 * 
 */
@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class EntityDataAction {

	/**
	 * 获取“数据表GRID”数据模型，用于输出数据表GRID所需要的JSON数据。
	 * 
	 * @param opArgs
	 *            加密后的调用参数，参数组成“systemMenuID:entityModuleID”
	 * @return
	 */
	@At(UrlAPI.ENTITY_GET_GRID_DATA)
	public UIGridData getGridData(String funcExpr) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UIGridData dataModel = new UIGridData();
		if (opContext.getException() != null) {

			dataModel.setException(opContext.getException());

		} else {

			// 构造Grid数据模型
			UIGrid grid = (UIGrid) opContext.getUiModelFactory().getGrid(opContext.getSystemMenu(), opContext.getCocEntity());
			// grid.setTreeField(opContext.getTreeField());
			String view = grid.getViewName();
			if (StringUtil.hasContent(view)) {
				dataModel.setViewName(view + "data");
			}

			dataModel.setModel(grid);
			// 构造查询条件
			CndExpr expr = opContext.makeExpr();
			try {
				List data = opContext.getDataManager().query(opContext.getSystemMenu(), opContext.getCocEntity(), null, expr);
				int total = opContext.getDataManager().count(opContext.getSystemMenu(), opContext.getCocEntity(), null, expr);

				dataModel.setData(data);
				dataModel.setTotal(total);

				LogUtil.debug("EntityAction.getEntityGridData: total = %s", total);
			} catch (Throwable e) {
				LogUtil.error("获取网格JSON数据失败！", e);

				dataModel.setException(e);
			}
		}

		LogUtil.debug("EntityAction.getEntityGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return dataModel;
	}

	@At(UrlAPI.ENTITY_GET_COMBOGRID_DATA)
	public UIGridData getComboGridData(String funcExpr, String fkFieldExpr) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		String[] array = MVCUtil.decodeArgs(fkFieldExpr);
		CocEntityService cocEntity = Cocit.me().getEntityServiceFactory().getEntity(array[0]);
		CocFieldService fkField = cocEntity.getField(array[1]);

		UIGridData dataModel = new UIGridData();
		if (opContext.getException() != null) {

			dataModel.setException(opContext.getException());

		} else {

			// 构造Grid数据模型
			UIGrid grid = (UIGrid) opContext.getUiModelFactory().getComboGrid(opContext.getSystemMenu(), opContext.getCocEntity(), fkField);

			dataModel.setModel(grid);

			// 构造查询条件
			CndExpr expr = opContext.makeExpr();
			CndExpr fkComboExpr = ExprUtil.parseToExpr(fkField.getFkComboWhere());
			if (fkComboExpr != null) {
				expr = expr.and(fkComboExpr);
			}

			try {

				List data = opContext.getDataManager().query(opContext.getSystemMenu(), opContext.getCocEntity(), null, expr);
				int total = opContext.getDataManager().count(opContext.getSystemMenu(), opContext.getCocEntity(), null, expr);

				dataModel.setData(data);
				dataModel.setTotal(total);

				LogUtil.debug("EntityAction.getEntityGridData: total = %s", total);

			} catch (Throwable e) {
				LogUtil.error("获取网格JSON数据失败！", e);

				dataModel.setException(e);

			}
		}

		LogUtil.debug("EntityAction.getEntityComboGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return dataModel;
	}

	@At(UrlAPI.ENTITY_GET_COMBOLIST_DATA)
	public UIListData getComboListData(String funcExpr, String fkFieldExpr) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		String[] array = MVCUtil.decodeArgs(fkFieldExpr);
		CocEntityService cocEntity = Cocit.me().getEntityServiceFactory().getEntity(array[0]);
		CocFieldService fkField = cocEntity.getField(array[1]);

		UIListData dataModel = new UIListData();
		if (opContext.getException() != null) {

			dataModel.setException(opContext.getException());

		} else {

			// 构造数据模型
			UIList model = opContext.getUiModelFactory().getComboList(opContext.getSystemMenu(), opContext.getCocEntity(), fkField);

			dataModel.setModel(model);

			// 构造查询条件
			CndExpr expr = opContext.makeExpr();
			CndExpr fkComboExpr = ExprUtil.parseToExpr(fkField.getFkComboWhere());
			if (fkComboExpr != null) {
				expr = expr.and(fkComboExpr);
			}

			try {

				List data = opContext.getDataManager().query(opContext.getSystemMenu(), opContext.getCocEntity(), null, expr);
				// int total = helper.entityManager.count(expr, null);

				dataModel.setData(data);

			} catch (Throwable e) {
				LogUtil.error("获取列表JSON数据失败！", e);

				dataModel.setException(e);
			}
		}

		LogUtil.debug("EntityAction.getEntityComboGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return dataModel;
	}

	@At(UrlAPI.ENTITY_GET_COMBOTREE_DATA)
	public UITreeData getComboTreeData(String funcExpr, String fkFieldExpr) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		String[] array = MVCUtil.decodeArgs(fkFieldExpr);
		CocEntityService cocEntity = Cocit.me().getEntityServiceFactory().getEntity(array[0]);
		CocFieldService fkField = cocEntity.getField(array[1]);

		UITreeData dataModel;
		if (opContext.getException() != null) {
			dataModel = (UITreeData) new UITreeData();
			dataModel.setException(opContext.getException());
		} else {

			// 构造查询条件
			CndExpr expr = opContext.makeExpr();
			CndExpr fkComboExpr = ExprUtil.parseToExpr(fkField.getFkComboWhere());
			if (fkComboExpr != null) {
				expr = expr.and(fkComboExpr);
			}

			try {
				dataModel = opContext.getUiModelFactory().getComboTreeData(opContext.getSystemMenu(), opContext.getCocEntity(), expr);
			} catch (Throwable e) {
				LogUtil.error("获取树形JSON数据失败！", e);

				dataModel = (UITreeData) new UITreeData();
				dataModel.setException(opContext.getException());
			}
		}

		LogUtil.debug("EntityAction.getEntityComboGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return dataModel;
	}

	/**
	 * 获取“获取数据表导航树”数据模型，用于输出树所需要的JSON数据。
	 * 
	 * @param opArgs
	 *            加密后的调用参数，参数组成“systemMenuID:entityModuleID”
	 * @return
	 */
	@At(UrlAPI.ENTITY_GET_FILTER_DATA)
	public UITreeData getFilterData(String funcExpr, boolean usedToSubModule) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UITreeData dataModel;
		if (opContext.getException() != null) {
			dataModel = (UITreeData) new UITreeData();
			dataModel.setException(opContext.getException());
		} else {
			try {
				dataModel = opContext.getUiModelFactory().getFilterData(opContext.getSystemMenu(), opContext.getCocEntity(), usedToSubModule);

			} catch (Throwable e) {
				LogUtil.error("获取过滤器JSON数据失败！", e);

				dataModel = (UITreeData) new UITreeData();
				dataModel.setException(opContext.getException());
			}
		}

		LogUtil.debug("EntityAction.getEntityComboGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return dataModel;
	}

	@At(UrlAPI.ENTITY_GET_ROWS_AUTH_DATA)
	public UITreeData getRowsAuthData(String funcExpr) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UITreeData dataModel;
		if (opContext.getException() != null) {
			dataModel = (UITreeData) new UITreeData();
			dataModel.setException(opContext.getException());
		} else {
			try {
				dataModel = opContext.getUiModelFactory().getRowsAuthData(opContext.getSystemMenu(), opContext.getCocEntity());

			} catch (Throwable e) {
				LogUtil.error("获取过滤器JSON数据失败！", e);

				dataModel = (UITreeData) new UITreeData();
				dataModel.setException(opContext.getException());
			}
		}

		LogUtil.debug("EntityAction.getEntityComboGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return dataModel;
	}

	@At(UrlAPI.ENTITY_GET_ACTIONS_DATA)
	public UITreeData getActionsData(String funcExpr) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UITreeData dataModel;
		if (opContext.getException() != null) {
			dataModel = (UITreeData) new UITreeData();
			dataModel.setException(opContext.getException());
		} else {
			try {
				dataModel = opContext.getUiModelFactory().getActionsData(opContext.getSystemMenu(), opContext.getCocEntity());

			} catch (Throwable e) {
				LogUtil.error("获取过滤器JSON数据失败！", e);

				dataModel = (UITreeData) new UITreeData();
				dataModel.setException(opContext.getException());
			}
		}

		LogUtil.debug("EntityAction.getEntityComboGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return dataModel;
	}

	@At(UrlAPI.ENTITY_GET_TREE_DATA)
	public UITreeData getTreeData(String funcExpr) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UITreeData dataModel;
		if (opContext.getException() != null) {
			dataModel = (UITreeData) new UITreeData();
			dataModel.setException(opContext.getException());
		} else {
			try {
				dataModel = opContext.getUiModelFactory().getTreeData(opContext.getSystemMenu(), opContext.getCocEntity());
			} catch (Throwable e) {
				LogUtil.error("获取树形JSON数据失败！", e);

				dataModel = (UITreeData) new UITreeData();
				dataModel.setException(opContext.getException());
			}
		}

		LogUtil.debug("EntityAction.getEntityComboGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return dataModel;
	}

	@At(UrlAPI.ENTITY_GET_DATAOBJECT)
	public UIFormData getDataObject(String funcExpr, String rowID, @Param("::entity") CocEntityParam rowNode) {
		OpContext opContext = OpContext.make(funcExpr, rowID, rowNode);

		UIFormData dataModel = new UIFormData();
		if (opContext.getException() != null) {
			dataModel.setException(opContext.getException());
		} else {

			try {
				UIForm form = (UIForm) opContext.getUiModelFactory().getForm(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getCocAction(), opContext.getDataObject());

				if (opContext.getCocAction() != null)
					form.setTitle(opContext.getCocAction().getName());

				dataModel.setModel(form);
				dataModel.setData(opContext.getDataObject());
			} catch (Throwable e) {
				LogUtil.error("获取表单JSON数据失败！", e);

				dataModel.setException(opContext.getException());
			}
		}

		LogUtil.debug("EntityAction.getEntityComboGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return dataModel;
	}
}
