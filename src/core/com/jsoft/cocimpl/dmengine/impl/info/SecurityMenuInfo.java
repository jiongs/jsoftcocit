// $codepro.audit.disable unnecessaryCast
package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.baseentity.security.ISystemMenuEntity;
import com.jsoft.cocit.dmengine.info.ICocActionInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiGridInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class SecurityMenuInfo extends NamedEntityInfo<ISystemMenuEntity> implements ISystemMenuInfo {

	private static final long serialVersionUID = -8604484097447125662L;

	private ISystemInfo system;

	SecurityMenuInfo(ISystemMenuEntity obj, ISystemInfo system) {
		super(obj);
		this.system = system;
	}

	@Override
	public void release() {
		super.release();

		system = null;
	}

	public String getSystemCode() {
		return entityData.getSystemCode();
	}

	public String getDataSourceCode() {
		return entityData.getDataSourceCode();
	}

	public String getLogo() {
		return entityData.getLogo();
	}

	public String getImage() {
		return entityData.getImage();
	}

	public String getPath() {
		return entityData.getPath();
	}

	public String getRefEntity() {
		return entityData.getRefEntity();
	}

	// public String getEntityName() {
	// return entityData.getEntityName();
	// }

	public String getActions() {
		return entityData.getActions();
	}

	public int getType() {
		return entityData.getType();
	}

	// public String getPathPrefix() {
	// return entityData.getPathPrefix();
	// }

	public String getUiView() {
		return entityData.getUiView();
	}

	public String getParentCode() {
		return entityData.getParentCode();
	}

//	public String getParentName() {
//		return entityData.getParentName();
//	}

	public String getFields() {
		return entityData.getFields();
	}

	public ICocEntityInfo getCocEntity() {
		return getEntityInfoFactory().getEntity(entityData.getRefEntity());
	}

	public ISystemInfo getSystem() {
		return system;
	}

	public List<ICocEntityInfo> getSubEntityModules() {
		return this.getCocEntity().getSubEntities();
	}

	@Override
	public Class getClassOfEntity() {
		return this.getCocEntity().getClassOfEntity();
	}

	@Override
	public String getWhereRule() {
		return entityData.getWhereRule();
	}

	@Override
	public List<String> getActionCodesWithoutRow() {
		return StringUtil.toList(this.getActions(), "|,;， ；、.");
	}

	@Override
	public List<String> getActionCodes() {
		List ret = new ArrayList();
		ret.addAll(this.getActionCodesWithoutRow());

		ICocEntityInfo cocEntity = this.getCocEntity();
		if (cocEntity != null) {
			ICuiEntityInfo cuiEntity = cocEntity.getCuiEntity(this.getUiView());
			if (cuiEntity != null) {
				ICuiGridInfo cuiGrid = cuiEntity.getCuiGrid();
				if (cuiGrid != null) {
					String rowActions = cuiGrid.getRowActions();
					List<String> rowActionList = StringUtil.toList(rowActions, "|,;， ；、.");
					for (String a : rowActionList) {
						if (!ret.contains(a)) {
							ret.add(a);
						}
					}
				}
			}
		}

		if (ret.size() == 0 && cocEntity != null) {
			List<ICocActionInfo> list = cocEntity.getActions(null);
			for (ICocActionInfo a : list) {
				ret.add(a.getCode());
			}
		}

		return ret;
	}

	@Override
	public CndExpr getWhere() {
		return ExprUtil.parseToExpr(getWhereRule());
	}

	@Override
	public Map getDefaultValues() {
		return StringUtil.parseJsonToMap(getDefaultValuesRule());
	}

	@Override
	public String getDefaultValuesRule() {
		return entityData.getDefaultValuesRule();
	}

	@Override
	public List<ICocActionInfo> getCocActions() {
		List<ICocActionInfo> allActions = new ArrayList();

		/*
		 * 获取实体操作
		 */
		ICocEntityInfo cocEntity = getCocEntity();
		if (cocEntity != null) {
			ICocActionInfo cocAction;

			List<String> actions = getActionCodes();
			for (String a : actions) {
				cocAction = cocEntity.getAction(a);
				if (!allActions.contains(cocAction)) {
					allActions.add(cocAction);
				}
			}

			if (allActions.size() == 0) {
				allActions = cocEntity.getActions(null);
			}

		}

		return allActions;
	}

	@Override
	public List<ICocActionInfo> getCocActions(List<String> actionCodes) {
		if (actionCodes == null)
			return getCocActions();

		List<ICocActionInfo> retActions = new ArrayList();

		/*
		 * 获取实体操作
		 */
		ICocEntityInfo cocEntity = getCocEntity();
		if (cocEntity != null) {
			ICocActionInfo cocAction;

			List<String> actions = getActionCodes();
			for (String a : actions) {
				if (!actionCodes.contains(a)) {
					continue;
				}

				cocAction = cocEntity.getAction(a);
				if (!retActions.contains(cocAction)) {
					retActions.add(cocAction);
				}
			}
		}

		return retActions;
	}

	@Override
	public String getAuthorizedName() {
		return this.getCode();
	}

	@Override
	public boolean isHidden() {
		return entityData.isHidden();
	}
}