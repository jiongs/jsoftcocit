package com.jsoft.cocimpl.action;

import java.util.List;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.CocUrl;
import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.mvc.CocEntityParam;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.orm.PageResult;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.model.control.UIList;
import com.jsoft.cocit.ui.model.control.UITree;
import com.jsoft.cocit.ui.model.datamodel.UIFormData;
import com.jsoft.cocit.ui.model.datamodel.UIGridData;
import com.jsoft.cocit.ui.model.datamodel.UIListData;
import com.jsoft.cocit.ui.model.datamodel.UITreeData;
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
	@At(CocUrl.ENTITY_GET_GRID_DATA)
	public UIGridData getGridData(String funcExpr, String fields, String rowActions) {
		WebCommandContext cmdctx = WebCommandContext.make(funcExpr, null, null, CommandNames.INTERCEPTOR_TYPE_GRID);

		cmdctx.execute(null, CommandNames.COC_QUERY, null);

		if (CocUrl.PATH_PARAM_EMPTY.equals(fields)) {
			fields = null;
		}

		UIGridData dataModel = new UIGridData();
		if (cmdctx.getException() != null) {

			dataModel.setException(cmdctx.getException());

		} else {

			// 构造Grid数据模型
			List<String> fieldsList = null;
			if (fields != null) {
				fieldsList = StringUtil.toList(fields);
			}
			List<String> actionsList = null;
			if (rowActions != null) {
				actionsList = StringUtil.toList(rowActions);
			}
			UIGrid grid = (UIGrid) cmdctx.getUiModelFactory().getGrid(cmdctx.getSystemMenu(), cmdctx.getCocEntity(), fieldsList, actionsList);
			// grid.setTreeField(opContext.getTreeField());
			String view = grid.getViewName();
			if (StringUtil.hasContent(view)) {
				dataModel.setViewName(view + "data");
			}

			dataModel.setModel(grid);
			try {

				PageResult pageResult = (PageResult) cmdctx.getResult();

				dataModel.setData(pageResult.getResult());
				dataModel.setTotal(pageResult.getTotalRecord());

				LogUtil.debug("EntityAction.getEntityGridData: total = %s", dataModel.getTotal());
			} catch (Throwable e) {
				LogUtil.error("获取网格JSON数据失败！", e);

				dataModel.setException(e);
			}
		}

		LogUtil.debug("EntityAction.getEntityGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		cmdctx.release();

		return dataModel;
	}

	@At(CocUrl.ENTITY_GET_COMBOGRID_DATA)
	public UIGridData getComboGridData(String funcExpr, String fkFieldExpr) {
		WebCommandContext cmdctx = WebCommandContext.make(funcExpr, null, null, CommandNames.INTERCEPTOR_TYPE_COLUMN);

		String[] array = MVCUtil.decodeArgs(fkFieldExpr);
		ICocEntityInfo cocEntity = Cocit.me().getEntityServiceFactory().getEntity(array[0]);
		ICocFieldInfo fkField = cocEntity.getField(array[1]);

		List<String> prevInterceptors = cmdctx.getCommonPrevInterceptors();
		prevInterceptors.addAll(StringUtil.toList(fkField.getPrevInterceptors()));
		List<String> postInterceptors = StringUtil.toList(fkField.getPostInterceptors());
		postInterceptors.addAll(cmdctx.getCommonPostInterceptors());
		cmdctx.execute(prevInterceptors, CommandNames.COC_COMBOGRID, postInterceptors);

		UIGridData dataModel = new UIGridData();
		if (cmdctx.getException() != null) {

			dataModel.setException(cmdctx.getException());

		} else {

			// 构造Grid数据模型
			UIGrid grid = (UIGrid) cmdctx.getUiModelFactory().getComboGrid(cmdctx.getSystemMenu(), cmdctx.getCocEntity(), fkField);

			dataModel.setModel(grid);

			PageResult pageResult = (PageResult) cmdctx.getResult();

			dataModel.setData(pageResult.getResult());
			dataModel.setTotal(pageResult.getTotalRecord());

		}

		LogUtil.debug("EntityAction.getEntityComboGridJson: uiModel = %s", dataModel);

		/*
		 * 及时清理内存
		 */
		cmdctx.release();

		return dataModel;
	}

	@At(CocUrl.ENTITY_GET_COMBOLIST_DATA)
	public UIListData getComboListData(String funcExpr, String fkFieldExpr) {
		WebCommandContext cmdctx = WebCommandContext.make(funcExpr, null, null, CommandNames.INTERCEPTOR_TYPE_COLUMN);

		String[] array = MVCUtil.decodeArgs(fkFieldExpr);
		ICocEntityInfo cocEntity = Cocit.me().getEntityServiceFactory().getEntity(array[0]);
		ICocFieldInfo fkField = cocEntity.getField(array[1]);

		cmdctx.execute(StringUtil.toList(fkField.getPrevInterceptors()), CommandNames.COC_COMBOLIST, StringUtil.toList(fkField.getPostInterceptors()));

		UIListData dataModel = new UIListData();
		if (cmdctx.getException() != null) {

			dataModel.setException(cmdctx.getException());

		} else {

			// 构造数据模型
			UIList model = cmdctx.getUiModelFactory().getComboList(cmdctx.getSystemMenu(), cmdctx.getCocEntity(), fkField);

			dataModel.setModel(model);

			dataModel.setData(cmdctx.getResult());

		}

		/*
		 * 及时清理内存
		 */
		cmdctx.release();

		return dataModel;
	}

	@At(CocUrl.ENTITY_GET_COMBOTREE_DATA)
	public UITreeData getComboTreeData(String funcExpr, String fkFieldExpr) {
		WebCommandContext cmdctx = WebCommandContext.make(funcExpr, null, null, CommandNames.INTERCEPTOR_TYPE_COLUMN);

		ICocFieldInfo fkField = null;
		if (StringUtil.hasContent(fkFieldExpr)) {
			String[] array = MVCUtil.decodeArgs(fkFieldExpr);
			ICocEntityInfo cocEntity = Cocit.me().getEntityServiceFactory().getEntity(array[0]);
			fkField = cocEntity.getField(array[1]);

			cmdctx.execute(StringUtil.toList(fkField.getPrevInterceptors()), CommandNames.COC_COMBOTREE, StringUtil.toList(fkField.getPostInterceptors()));
		} else {
			cmdctx.execute(null, CommandNames.COC_COMBOTREE, null);
		}

		UITreeData dataModel = new UITreeData();
		if (cmdctx.getException() != null) {
			dataModel.setException(cmdctx.getException());
		} else {

			dataModel.setModel(cmdctx.getUiModelFactory().getComboTree(cmdctx.getSystemMenu(), cmdctx.getCocEntity(), fkField));
			dataModel.setData(cmdctx.getResult());
		}

		/*
		 * 及时清理内存
		 */
		cmdctx.release();

		return dataModel;
	}

	/**
	 * 获取“获取数据表导航树”数据模型，用于输出树所需要的JSON数据。
	 * 
	 * @param opArgs
	 *            加密后的调用参数，参数组成“systemMenuID:entityModuleID”
	 * @return
	 */
	@At(CocUrl.ENTITY_GET_FILTER_DATA)
	public UITreeData getFilterData(String funcExpr, boolean usedToSubModule) {
		WebCommandContext cmdctx = WebCommandContext.make(funcExpr, null, null, CommandNames.INTERCEPTOR_TYPE_COLUMN);

		cmdctx.put("usedToSubModule", usedToSubModule);

		cmdctx.execute(null, CommandNames.COC_FILTERTREE, null);

		UITreeData dataModel = (UITreeData) new UITreeData();
		if (cmdctx.getException() != null) {
			dataModel.setException(cmdctx.getException());
		} else {
			UITree model = cmdctx.getUiModelFactory().getFilter(cmdctx.getSystemMenu(), cmdctx.getCocEntity(), usedToSubModule);

			dataModel.setModel(model);
			dataModel.setData(cmdctx.getResult());
		}

		/*
		 * 及时清理内存
		 */
		cmdctx.release();

		return dataModel;
	}

	@At(CocUrl.ENTITY_GET_ROWS_AUTH_DATA)
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

	@At(CocUrl.ENTITY_GET_ACTIONS_DATA)
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

	@At(CocUrl.ENTITY_GET_TREE_DATA)
	public UITreeData getTreeData(String funcExpr) {
		WebCommandContext opContext = WebCommandContext.make(funcExpr, null, null);

		UITreeData dataModel;
		try {
			dataModel = opContext.getUiModelFactory().getTreeData(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getQueryExpr());
		} catch (Throwable e) {
			dataModel = (UITreeData) new UITreeData();
			dataModel.setException(opContext.getException());
		}

		return dataModel;
	}

	@At(CocUrl.ENTITY_GET_DATAOBJECT)
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
