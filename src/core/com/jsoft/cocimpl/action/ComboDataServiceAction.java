package com.jsoft.cocimpl.action;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.jsoft.cocit.action.OpContext;
import com.jsoft.cocit.constant.CocUrl;
import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.mvc.CocEntityParam;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.securityengine.SecurityVoter4Runtime;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.ui.model.datamodel.AlertModel;
import com.jsoft.cocit.ui.model.datamodel.UIGridData;
import com.jsoft.cocit.ui.model.datamodel.UITreeData;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class ComboDataServiceAction extends EntityDataAction {

	@At(CocUrl.COMBO_GET_GRID_DATA)
	public UIGridData getGridData(String funcExpr, String fields, String rowActions) {
		SecurityVoter4Runtime.allow();

		return super.getGridData(funcExpr, fields, rowActions);
	}

	@At(CocUrl.COMBO_GET_FILTER_DATA)
	public UITreeData getFilterData(String funcExpr, boolean usedToSubModule) {
		SecurityVoter4Runtime.allow();

		return super.getFilterData(funcExpr, usedToSubModule);
	}

	@At(CocUrl.COMBO_GET_COMBOGRID_DATA)
	public UIGridData getComboGridData(String funcExpr, String fkFieldExpr) {
		SecurityVoter4Runtime.allow();

		return super.getComboGridData(funcExpr, fkFieldExpr);
	}

	@At(CocUrl.COMBO_GET_COMBOTREE_DATA)
	public UITreeData getComboTreeData(String funcExpr, String fkFieldExpr) {
		SecurityVoter4Runtime.allow();

		return super.getComboTreeData(funcExpr, fkFieldExpr);
	}

	@At(CocUrl.COMBO_GET_TREE_DATA)
	public UITreeData getTreeData(String funcExpr) {
		SecurityVoter4Runtime.allow();

		return super.getTreeData(funcExpr);
	}

	@At(CocUrl.COMBO_GET_FORM_TO_SAVE)
	public UIModel getFormToSave(String funcExpr, String rowID, boolean isAjax, @Param("::entity") CocEntityParam params) {
		SecurityVoter4Runtime.allow();

		OpContext opContext = OpContext.make(funcExpr, rowID, params, CommandNames.INTERCEPTOR_TYPE_FORM);

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

			uiModel.setSubmitUrl(MVCUtil.makeUrl(CocUrl.ENTITY_SAVE, opContext.getRuntimeFuncExpr(), rowID));
			uiModel.setDataObject(opContext.getDataObject());

		}

		LogUtil.debug("EntityAction.getDataObjectForm: uiModel = %s", uiModel);

		/*
		 * 及时清理内存
		 */
		opContext.release();

		return uiModel;
	}
}
