package com.jsoft.cocimpl.action;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.entity.IDataEntity;
import com.jsoft.cocit.entity.IExtDataEntity;
import com.jsoft.cocit.entity.IExtNamedEntity;
import com.jsoft.cocit.entity.INamedEntity;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.mvc.CocEntityParam;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.ui.model.control.UIActions;
import com.jsoft.cocit.ui.model.control.UIEntities;
import com.jsoft.cocit.ui.model.control.UIEntity;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.model.control.UITree;
import com.jsoft.cocit.ui.model.datamodel.AlertModel;
import com.jsoft.cocit.ui.model.datamodel.UIFormData;
import com.jsoft.cocit.util.ExcelUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 实体Action：即用来管理实体数据的Action，负责接收管理实体数据的请求并处理这些请求，包括“增加、删除、查询、修改、导入、导出”等操作。
 * 
 * @author jiongs753
 * 
 */
@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class EntityFuncAction {

	/**
	 * 获取“数据模块”界面模型，用于输出数据模块的界面。
	 * 
	 * @param funcExpr
	 *            Hex加密后的调用参数，参数组成“moduleID”
	 * @return
	 */
	@At(UrlAPI.ENTITY_GET_MAINS_UI)
	public UIEntities mains(String funcExpr, boolean isAjax) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UIEntities uiModel;
		if (opContext.getException() != null) {
			uiModel = new UIEntities().setException(opContext.getException());
		} else {
			uiModel = opContext.getUiModelFactory().getMains(opContext.getSystemMenu());
			uiModel.setAjax(isAjax);
		}

		LogUtil.debug("EntityAction.getDataObjectJson(String, String, CocEntityParamNode): uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		// 返回
		return uiModel;
	}

	@At(UrlAPI.ENTITY_GET_MAIN_UI)
	public UIEntity main(String funcExpr, boolean isAjax, boolean usedToSubModule) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UIEntity uiModel;
		if (opContext.getException() != null) {
			uiModel = new UIEntity().setException(opContext.getException());
		} else {
			uiModel = opContext.getUiModelFactory().getMain(opContext.getSystemMenu(), opContext.getCocEntity(), usedToSubModule);
			uiModel.setAjax(isAjax);
			// uiModel.getGrid().setTreeField(opContext.getTreeField());
		}

		LogUtil.debug("EntityAction.getUI: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		// 返回
		return uiModel;
	}

	@At(UrlAPI.ENTITY_GET_GRID)
	public UIGrid getGrid(String funcExpr, boolean isAjax) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UIGrid uiModel;
		if (opContext.getException() != null) {
			uiModel = new UIGrid().setException(opContext.getException());
		} else {
			uiModel = (UIGrid) opContext.getUiModelFactory().getGrid(opContext.getSystemMenu(), opContext.getCocEntity());
			uiModel.setAjax(isAjax);
		}

		LogUtil.debug("EntityAction.getGridUI: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_GET_ACTIONS)
	public UIActions getActions(String funcExpr, boolean isAjax) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UIActions uiModel;
		if (opContext.getException() != null) {
			uiModel = new UIActions().setException(opContext.getException());
		} else {
			uiModel = opContext.getUiModelFactory().getActions(opContext.getSystemMenu(), opContext.getCocEntity());
			uiModel.setAjax(isAjax);
		}

		LogUtil.debug("EntityAction.getActionsUI: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		// 返回
		return uiModel;
	}

	@At(UrlAPI.ENTITY_GET_TREE)
	public UITree getTree(String funcExpr, boolean isAjax) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UITree uiModel;
		if (opContext.getException() != null) {
			uiModel = new UITree().setException(opContext.getException());
		} else {
			uiModel = opContext.getUiModelFactory().getTree(opContext.getSystemMenu(), opContext.getCocEntity());
			uiModel.setAjax(isAjax);
		}

		LogUtil.debug("EntityAction.getEntityTreeUI: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_GET_FILTER)
	public UITree getFilter(String funcExpr, boolean isAjax) {
		OpContext opContext = OpContext.make(funcExpr, null, null);

		UITree uiModel;
		if (opContext.getException() != null) {
			uiModel = new UITree().setException(opContext.getException());
		} else {
			uiModel = opContext.getUiModelFactory().getFilter(opContext.getSystemMenu(), opContext.getCocEntity(), false);
			uiModel.setAjax(isAjax);
		}

		LogUtil.debug("EntityAction.getFilterUI: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	/**
	 * 
	 * 获取业务数据表单模型
	 * 
	 * @param opArgs
	 *            调用参数，参数组成“systemMenuID:entityModuleID:entityActionID”
	 * @param rowID
	 *            实体数据ID
	 * @param params
	 *            实体数据行参数节点
	 * @return
	 */
	@At(UrlAPI.ENTITY_GET_FORM_TO_SAVE)
	public UIModel getFormToSave(String funcExpr, String rowID, boolean isAjax, @Param("::entity") CocEntityParam params) {
		OpContext opContext = OpContext.make(funcExpr, rowID, params, true);

		UIForm uiModel;
		if (opContext.getException() != null) {
			if (isAjax) {
				return AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));
			}

			uiModel = new UIForm().setException(opContext.getException());
		} else {
			uiModel = (UIForm) opContext.getUiModelFactory().getForm(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getCocAction(), opContext.getDataObject());
			uiModel.setAjax(isAjax);

			if (opContext.getCocAction() != null)
				uiModel.setTitle(opContext.getCocAction().getName());

			uiModel.setSubmitUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_SAVE, opContext.getRuntimeFuncExpr(), rowID));
			uiModel.setDataObject(opContext.getDataObject());

		}

		LogUtil.debug("EntityAction.getDataObjectForm: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	/**
	 * 
	 * 保存业务数据
	 * 
	 * @param opArgs
	 *            调用参数，参数组成“systemMenuID:entityModuleID:entityActionID”
	 * @param rowID
	 *            实体数据ID
	 * @param params
	 *            实体数据行参数节点
	 * @return
	 */
	@At(UrlAPI.ENTITY_SAVE)
	public UIFormData save(String funcExpr, String rowID, @Param("::entity") CocEntityParam params) {
		OpContext opContext = OpContext.make(funcExpr, rowID, params);

		UIFormData uiModel = new UIFormData();

		if (opContext.getException() != null) {
			uiModel.setException(opContext.getException());
		} else {
			try {
				UIForm form = (UIForm) opContext.getUiModelFactory().getForm(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getCocAction(), opContext.getDataObject());

				uiModel.setModel(form);
				uiModel.setData(opContext.getDataObject());

				// ObjectUtil.setValue(opContext.getDataObject(), Consts.TENANT_KEY, opContext.getContext().getLoginTenantKey());
				opContext.getDataManager().save(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getDataObject(), opContext.getCocAction().getKey());

			} catch (Throwable e) {
				LogUtil.info("EntityAction.saveDataObject： ERROR！ %s", ExceptionUtil.msg(e));

				uiModel.setException(e);
			}
		}

		LogUtil.debug("EntityAction.saveDataObject: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_GET_FORM_TO_RUN)
	public UIModel getFormToRun(String funcExpr, String rowID, boolean isAjax, @Param("::entity") CocEntityParam params) {
		OpContext opContext = OpContext.make(funcExpr, rowID, params, true);

		UIForm uiModel;
		if (opContext.getException() != null) {
			if (isAjax) {
				return AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));
			}

			uiModel = new UIForm().setException(opContext.getException());
		} else {
			uiModel = (UIForm) opContext.getUiModelFactory().getForm(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getCocAction(), opContext.getDataObject());
			uiModel.setAjax(isAjax);

			if (opContext.getCocAction() != null)
				uiModel.setTitle(opContext.getCocAction().getName());

			uiModel.setSubmitUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_RUN, opContext.getRuntimeFuncExpr(), rowID));
			uiModel.setDataObject(opContext.getDataObject());

		}

		LogUtil.debug("EntityAction.getDataObjectForm: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_RUN)
	public AlertModel run(String args, String dataID) {
		OpContext opContext = OpContext.make(args, dataID, null);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			try {
				opContext.getDataManager().run(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getDataObject(), opContext.getCocActionID());

				String msg = opContext.getCocAction().getSuccessMessage();
				uiModel = AlertModel.makeSuccess(msg == null ? "操作成功！" : msg);
			} catch (Throwable e) {
				String msg = opContext.getCocAction().getErrorMessage();
				LogUtil.error(msg, e);
				uiModel = AlertModel.makeError(StringUtil.isBlank(msg) ? ExceptionUtil.msg(e) : msg);
			}
		}

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	/**
	 * 对应JSP: {@value Const#JSP_DIR}/getExportXlsForm.jsp
	 * 
	 * @return
	 */
	@At(UrlAPI.ENTITY_GET_FORM_TO_EXPORT_XLS)
	public UIModel getFormToExportXls(String funcExpr, String dataID, boolean isAjax) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		UIForm uiModel;
		if (opContext.getException() != null) {
			if (isAjax) {
				return AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));
			}

			uiModel = new UIForm().setException(opContext.getException());
		} else {
			uiModel = (UIForm) opContext.getUiModelFactory().getForm(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getCocAction(), opContext.getDataObject());
			uiModel.setAjax(isAjax);

			// if (StringUtil.isNil(formModel.getJsp()))
			uiModel.setViewName(Const.JSP_DIR + "/getExportXlsForm.jsp");

			uiModel.set("opContext", opContext);
			uiModel.set("filterExpr", StringUtil.escapeHtml(opContext.getHttpContext().getParameterValue("query.filterExpr", "")));
			uiModel.set("parentExpr", StringUtil.escapeHtml(opContext.getHttpContext().getParameterValue("query.parentExpr", "")));
			uiModel.set("keywords", StringUtil.escapeHtml(opContext.getHttpContext().getParameterValue("query.keywords", "")));
			uiModel.set("sortField", StringUtil.escapeHtml(opContext.getHttpContext().getParameterValue("sortField", "")));
			uiModel.set("sortOrder", StringUtil.escapeHtml(opContext.getHttpContext().getParameterValue("sortOrder", "")));

			uiModel.setSubmitUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_EXPORT_XLS, opContext.getRuntimeFuncExpr()));
		}

		/*
		 * 及时清理内存
		 */
		// opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_EXPORT_XLS)
	public void exportXls(String funcExpr, String dataID) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		OutputStream outStream = null;
		try {
			List<String[]> excelRows = new ArrayList();

			// 生成Excel表头
			String[] columns = opContext.getHttpContext().getParameterValues("columns");
			String[] header = new String[columns.length];
			Map<String, CocFieldService> fields = opContext.getCocEntity().getFieldsMap();
			for (int i = 0; i < columns.length; i++) {
				String col = columns[i];
				CocFieldService fld = fields.get(col);
				header[i] = fld.getName();
			}
			excelRows.add(header);

			// 查询数据
			CndExpr expr = opContext.makeExpr();
			List list = opContext.getDataManager().query(opContext.getSystemMenu(), opContext.getCocEntity(), null, expr);

			// 生成Excel行
			String[] row;
			for (Object obj : list) {
				row = new String[columns.length];
				for (int i = 0; i < columns.length; i++) {
					String col = columns[i];

					Object value = ObjectUtil.getValue(obj, col);
					CocFieldService fld = fields.get(col);
					String strValue = null;
					if (value instanceof IDataEntity) {
						strValue = ((IDataEntity) value).getKey();
					}
					if (StringUtil.isBlank(strValue) && value instanceof INamedEntity) {
						strValue = ((INamedEntity) value).getName();
					}
					if (StringUtil.isBlank(strValue)) {
						strValue = fld.formatFieldValue(value);
					}

					row[i] = strValue;
				}
				excelRows.add(row);
			}

			// 发送Excel文件
			HttpServletResponse response = opContext.getHttpContext().getResponse();
			String fileName = opContext.getCocEntity().getName();
			fileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachement; filename=" + fileName + ".xls");
			response.setContentType("application/octet-stream");
			outStream = response.getOutputStream();
			ExcelUtil.makeExcel(outStream, excelRows);

			LogUtil.debug("EntityAction.exportXls: total = %s", list == null ? 0 : list.size());
		} catch (Throwable e) {
			LogUtil.error("EntityAction.exportXls: error! ", e);
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Throwable ex) {
				}
			}
		}
	}

	/**
	 * 对应JSP: {@value Const#JSP_DIR}/getImportXlsForm.jsp
	 * 
	 * @return
	 */
	@At(UrlAPI.ENTITY_GET_FORM_TO_IMPORT_XLS)
	public UIModel getFormToImportXls(String funcExpr, String dataID, boolean isAjax) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		UIForm uiModel;
		if (opContext.getException() != null) {
			if (isAjax) {
				return AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));
			}

			uiModel = new UIForm().setException(opContext.getException());
		} else {
			uiModel = (UIForm) opContext.getUiModelFactory().getForm(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getCocAction(), opContext.getDataObject());
			uiModel.setAjax(isAjax);

			// if (StringUtil.isNil(formModel.getJsp()))
			uiModel.setViewName(Const.JSP_DIR + "/getImportXlsForm.jsp");

			uiModel.set("opContext", opContext);
			uiModel.set("filterExpr", StringUtil.escapeHtml(opContext.getHttpContext().getParameterValue("query.filterExpr", "")));
			uiModel.set("parentExpr", StringUtil.escapeHtml(opContext.getHttpContext().getParameterValue("query.parentExpr", "")));
			uiModel.set("keywords", StringUtil.escapeHtml(opContext.getHttpContext().getParameterValue("query.keywords", "")));
			uiModel.set("sortField", StringUtil.escapeHtml(opContext.getHttpContext().getParameterValue("sortField", "")));
			uiModel.set("sortOrder", StringUtil.escapeHtml(opContext.getHttpContext().getParameterValue("sortOrder", "")));

			uiModel.setSubmitUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_IMPORT_XLS, opContext.getRuntimeFuncExpr()));
		}

		/*
		 * 及时清理内存
		 */
		// opContext.release();

		return uiModel;
	}

	/**
	 * 该方法在执行{@value Const#JSP_DIR}/getImportXlsForm.jsp中的form.submit()时调用。
	 * 
	 * @param funcExpr
	 * @param dataID
	 */
	@At(UrlAPI.ENTITY_IMPORT_XLS)
	public AlertModel importXls(String funcExpr, String dataID) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			OutputStream outStream = null;
			try {
				String excelFilePath = opContext.getHttpContext().getParameterValue("excelFilePath", "");
				if (StringUtil.isBlank(excelFilePath)) {
					throw new CocException("Excel文件不存在！");
				}
				File excelFile = new File(Cocit.me().getContextDir() + excelFilePath);
				List dataRows = opContext.getCocEntity().parseDataFromExcel(excelFile);

				// String tenantKey = opContext.getContext().getLoginTenantKey();
				// for (Object row : dataRows) {
				// ObjectUtil.setValue(row, Consts.TENANT_KEY, tenantKey);
				// }

				opContext.getDataManager().save(opContext.getSystemMenu(), opContext.getCocEntity(), dataRows, opContext.getCocAction().getKey());

				LogUtil.debug("EntityAction.importXls: total = %s", dataRows == null ? 0 : dataRows.size());

				uiModel = AlertModel.makeSuccess("共导入了 " + (dataRows == null ? 0 : dataRows.size()) + " 条数据！");

			} catch (Throwable e) {

				LogUtil.error("EntityAction.importXls: error! ", e);

				uiModel = AlertModel.makeError("导入数据出错: " + ExceptionUtil.msg(e));

			} finally {
				if (outStream != null) {
					try {
						outStream.close();
					} catch (Throwable ex) {
					}
				}
			}
		}

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_SORT_MOVE_TOP)
	public AlertModel sortMoveTop(String funcExpr, String dataID) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			String[] array = StringUtil.toArray(dataID);
			List<Long> idList = new ArrayList();
			for (String s : array) {
				idList.add(StringUtil.castTo(s, 0L));
			}
			List listCurrent = null;
			if (idList.size() > 0) {
				CndExpr expr = Expr.in(Const.F_ID, idList).setFieldRexpr(Expr.fieldRexpr("sn|id"));
				listCurrent = opContext.getDataManager().getDataEngine().query(opContext.getCocEntity().getClassOfEntity(), expr);
			}

			if (listCurrent != null && listCurrent.size() != 0) {
				List listAll = opContext.query();
				sort(listAll, listCurrent, false, true);
				opContext.getDataManager().getDataEngine().save(listAll, Expr.fieldRexpr(Const.F_SN));
			}

			uiModel = AlertModel.makeSuccess("操作成功！");
		}

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_SORT_MOVE_UP)
	public AlertModel sortMoveUp(String funcExpr, String dataID) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			List listCurrent = getListCurrent(opContext, dataID);

			if (listCurrent != null && listCurrent.size() != 0) {
				List listAll = getListAll(opContext, dataID);

				sort(listAll, listCurrent, false, false);

				opContext.getOrm().save(listAll, Expr.fieldRexpr("sn|id"));
			}

			uiModel = AlertModel.makeSuccess("操作成功！");
		}

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_SORT_MOVE_DOWN)
	public AlertModel sortMoveDown(String funcExpr, String dataID) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			List listCurrent = getListCurrent(opContext, dataID);

			if (listCurrent != null && listCurrent.size() != 0) {
				List listAll = getListAll(opContext, dataID);

				sort(listAll, listCurrent, true, false);

				opContext.getOrm().save(listAll, Expr.fieldRexpr("sn|id"));
			}

			uiModel = AlertModel.makeSuccess("操作成功！");
		}

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_SORT_MOVE_BOTTOM)
	public AlertModel sortMoveBottom(String funcExpr, String dataID) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			List listCurrent = getListCurrent(opContext, dataID);

			if (listCurrent != null && listCurrent.size() != 0) {
				List listAll = getListAll(opContext, dataID);

				sort(listAll, listCurrent, true, true);

				opContext.getOrm().save(listAll, Expr.fieldRexpr("sn|id"));
			}

			uiModel = AlertModel.makeSuccess("操作成功！");
		}

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_SORT_REVERSE)
	public AlertModel sortReverse(String funcExpr, String dataID) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			List listCurrent = getListCurrent(opContext, dataID);

			if (listCurrent != null && listCurrent.size() != 0) {
				List<Integer> orders = new ArrayList<Integer>();
				for (Object obj : listCurrent) {
					Integer order = getSN(obj);
					if (order == null) {
						orders.add(0);
					} else {
						orders.add(order);
					}
				}
				int len = listCurrent.size() - 1;
				for (int i = len; i >= 0; i--) {
					Object data = listCurrent.get(i);
					setSN(data, orders.get(new Integer(len - i)));
				}

				opContext.getOrm().save(listCurrent, Expr.fieldRexpr("sn|id"));
			}

			uiModel = AlertModel.makeSuccess("操作成功！");
		}

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_SORT_CANCEL)
	public AlertModel sortCancel(String funcExpr, String dataID) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			List listCurrent = getListCurrent(opContext, dataID);

			if (listCurrent == null || listCurrent.size() == 0) {
				listCurrent = getListAll(opContext, dataID);
			}

			if (listCurrent != null && listCurrent.size() > 0) {
				for (Object one : listCurrent) {
					if (one instanceof IExtNamedEntity) {
						((IExtNamedEntity) one).setSn(null);
					} else {
						ObjectUtil.setValue(one, Const.F_SN, null);
					}
				}

				opContext.getOrm().save(listCurrent, Expr.fieldRexpr("sn|id"));
			}

			uiModel = AlertModel.makeSuccess("操作成功！");
		}

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	private List getListAll(OpContext opContext, String dataID) {
		Orm orm = opContext.getDataManager().getDataEngine().orm();
		Class classOfEntity = opContext.getCocEntity().getClassOfEntity();

		CndExpr expr = opContext.makeExpr();
		expr = expr.addAsc("sn").addDesc("id").setFieldRexpr(Expr.fieldRexpr("sn|id"));

		return orm.query(classOfEntity, expr);
	}

	private List getListCurrent(OpContext opContext, String dataID) {
		Orm orm = opContext.getDataManager().getDataEngine().orm();
		Class classOfEntity = opContext.getCocEntity().getClassOfEntity();

		String[] array = StringUtil.toArray(dataID);
		List<Long> idList = new ArrayList();
		for (String s : array) {
			idList.add(StringUtil.castTo(s, 0L));
		}
		List listCurrent = null;
		if (idList.size() > 0) {
			CndExpr expr = Expr.in(Const.F_ID, idList).addAsc("sn").addDesc("id").setFieldRexpr(Expr.fieldRexpr("sn|id"));
			listCurrent = orm.query(classOfEntity, expr);
		}

		return listCurrent;
	}

	private Integer getSN(Object obj) {
		if (obj instanceof IExtNamedEntity) {
			return ((IExtNamedEntity) obj).getSn();
		} else {
			return ObjectUtil.getValue(obj, Const.F_SN);
		}
	}

	private void setSN(Object obj, int orderby) {
		if (obj instanceof IExtNamedEntity) {
			((IExtNamedEntity) obj).setSn(orderby);
		} else {
			ObjectUtil.setValue(obj, Const.F_SN, orderby);
		}
	}

	private void sort(List listAll, List listCurrent, boolean isDown, boolean toEnd) {
		int from = listAll.indexOf(listCurrent.get(0));
		List otherDatas = new ArrayList();
		int len = listAll.size();
		int index = from - 1;
		int orderby = 1;
		if (isDown) {
			index = from;
			// 往下移动排序
			// 排序选中前的数据
			for (int i = 0; i < len; i++) {
				Object oneData = listAll.get(i);
				if (i < index) {// 排序选中前的数据
					setSN(oneData, orderby++);
				} else {
					// 忽略待排序的数据
					if (!listCurrent.contains(oneData)) {
						otherDatas.add(oneData);
						continue;
					}
				}
			}
			// 排序选中的数据和其他数据
			int otherLen = otherDatas.size();
			if (otherLen > 0) {// 排序其他数据中的第一条数据
				Object oneData = otherDatas.get(0);
				setSN(oneData, orderby++);
			}
			// 排序选中的数据

			for (Object obj : listAll) {
				if (listCurrent.contains(obj)) {
					setSN(obj, orderby++);
				}
			}
			// 排序除第一条外的其他数据
			for (int i = 1; i < otherLen; i++) {
				Object oneData = otherDatas.get(i);
				setSN(oneData, orderby++);
			}
		} else {
			if (toEnd) {
				index = 0;
			}
			// 往上移动排序
			// 排序选中前的数据和选中的数据
			for (int i = 0; i < len; i++) {
				Object oneData = listAll.get(i);
				if (i < index) {// 排序选中前的数据
					setSN(oneData, orderby++);
				} else {
					// 排序选中的数据
					if (listCurrent.contains(oneData)) {
						setSN(oneData, orderby++);
					} else {
						// 增加未排序的数据并继续
						otherDatas.add(oneData);
						continue;
					}
				}
			}
			// 排序其他数据
			for (Object oneData : otherDatas) {
				setSN(oneData, orderby++);
			}
		}
	}

	@At(UrlAPI.ENTITY_GET_FORM_TO_REMOVE)
	public UIModel getFormToRemove(String funcExpr, String rowID, boolean isAjax, @Param("::entity") CocEntityParam params) {
		OpContext opContext = OpContext.make(funcExpr, rowID, params, true);

		UIForm uiModel;
		if (opContext.getException() != null) {
			if (isAjax) {
				return AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));
			}

			uiModel = new UIForm().setException(opContext.getException());
		} else {
			uiModel = (UIForm) opContext.getUiModelFactory().getForm(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getCocAction(), opContext.getDataObject());
			uiModel.setAjax(isAjax);

			if (opContext.getCocAction() != null)
				uiModel.setTitle(opContext.getCocAction().getName());

			uiModel.setSubmitUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_REMOVE, opContext.getRuntimeFuncExpr(), rowID));
			uiModel.setDataObject(opContext.getDataObject());

		}

		LogUtil.debug("EntityAction.getDataObjectForm: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_REMOVE)
	public AlertModel remove(String funcExpr, String dataID, @Param("::entity") CocEntityParam params) {
		OpContext opContext = OpContext.make(funcExpr, dataID, params);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			try {
				List list = (List) opContext.getDataObject();
				if (list != null && list.size() != 0) {
					for (Object obj : list) {
						if (obj instanceof IExtDataEntity) {
							IExtDataEntity data = ((IExtDataEntity) obj);
							if (data.isArchived()) {
								throw new CocException("数据已归档，不允许移除！");
							}
							if (data.isBuildin()) {
								throw new CocException("预置数据，不允许移除！");
							}
							data.setStatusCode(Const.STATUS_CODE_REMOVED);
						}
					}
				}

				opContext.save();

				String msg = opContext.getCocAction().getSuccessMessage();
				uiModel = AlertModel.makeSuccess(msg == null ? "删除数据成功！" : msg);
			} catch (Throwable e) {
				uiModel = AlertModel.makeError(ExceptionUtil.msg(e));
			}
		}

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_GET_FORM_TO_DELETE)
	public UIModel getFormToDelete(String funcExpr, String rowID, boolean isAjax, @Param("::entity") CocEntityParam params) {
		OpContext opContext = OpContext.make(funcExpr, rowID, params, true);

		UIForm uiModel;
		if (opContext.getException() != null) {
			if (isAjax) {
				return AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));
			}

			uiModel = new UIForm().setException(opContext.getException());
		} else {
			uiModel = (UIForm) opContext.getUiModelFactory().getForm(opContext.getSystemMenu(), opContext.getCocEntity(), opContext.getCocAction(), opContext.getDataObject());
			uiModel.setAjax(isAjax);

			if (opContext.getCocAction() != null)
				uiModel.setTitle(opContext.getCocAction().getName());

			uiModel.setSubmitUrl(MVCUtil.makeUrl(UrlAPI.ENTITY_DELETE, opContext.getRuntimeFuncExpr(), rowID));
			uiModel.setDataObject(opContext.getDataObject());
		}

		LogUtil.debug("EntityAction.getDataObjectForm: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_DELETE)
	public AlertModel delete(String funcExpr, String dataID, @Param("::entity") CocEntityParam params) {
		OpContext opContext = OpContext.make(funcExpr, dataID, params);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			try {
				// List list = (List) opContext.getDataObject();
				// if (list != null && list.size() != 0) {
				// for (Object obj : list) {
				// if (obj instanceof IExtDataEntity) {
				// IExtDataEntity data = ((IExtDataEntity) obj);
				// if (!data.isRemoved()) {
				// throw new CocException("永久删除失败！");
				// }
				// }
				// }
				// }

				opContext.delete();

				String msg = opContext.getCocAction().getSuccessMessage();
				uiModel = AlertModel.makeSuccess(msg == null ? "永久删除成功！" : msg);
			} catch (Throwable e) {
				String msg = opContext.getCocAction().getErrorMessage();
				if (StringUtil.isBlank(msg)) {
					msg = ExceptionUtil.msg(e);
				} else {
					msg += ExceptionUtil.msg(e);
				}
				uiModel = AlertModel.makeError(msg);
			}
		}

		LogUtil.debug("EntityAction.delete: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}

	@At(UrlAPI.ENTITY_CLEAR)
	public AlertModel clear(String funcExpr, String dataID) {
		OpContext opContext = OpContext.make(funcExpr, dataID, null);

		AlertModel uiModel;
		if (opContext.getException() != null) {

			uiModel = AlertModel.makeError(ExceptionUtil.msg(opContext.getException()));

		} else {
			Orm orm = opContext.getDataManager().getDataEngine().orm();
			Class classOfEntity = opContext.getCocEntity().getClassOfEntity();

			orm.clear(classOfEntity, null);

			String msg = opContext.getCocAction().getSuccessMessage();
			uiModel = AlertModel.makeSuccess(msg == null ? "操作成功！" : msg);
		}

		LogUtil.debug("EntityAction.delDataObjects: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}
}
