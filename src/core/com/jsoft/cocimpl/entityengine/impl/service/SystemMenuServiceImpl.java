// $codepro.audit.disable unnecessaryCast
package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.entity.security.ISystemMenu;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CuiEntityService;
import com.jsoft.cocit.entityengine.service.CuiGridService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class SystemMenuServiceImpl extends NamedEntityServiceImpl<ISystemMenu> implements SystemMenuService {

	private static final long serialVersionUID = -8604484097447125662L;

	private SystemService system;

	SystemMenuServiceImpl(ISystemMenu obj, SystemService system) {
		super(obj);
		this.system = system;
	}

	@Override
	public void release() {
		super.release();

		system = null;
	}

	public String getSystemKey() {
		return entityData.getSystemKey();
	}

	public String getDataSourceKey() {
		return entityData.getDataSourceKey();
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

	public String getParentKey() {
		return entityData.getParentKey();
	}

	public String getParentName() {
		return entityData.getParentName();
	}

	public String getFields() {
		return entityData.getFields();
	}

	public CocEntityService getCocEntity() {
		return esf().getEntity(entityData.getRefEntity());
	}

	public SystemService getSystem() {
		return system;
	}

	public List<CocEntityService> getSubEntityModules() {
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
	public List<String> getActionKeysWithoutRow() {
		return StringUtil.toList(this.getActions(), "|,;， ；、.");
	}

	@Override
	public List<String> getActionKeys() {
		List ret = new ArrayList();
		ret.addAll(this.getActionKeysWithoutRow());

		CocEntityService cocEntity = this.getCocEntity();
		if (cocEntity != null) {
			CuiEntityService cuiEntity = cocEntity.getCuiEntity(this.getUiView());
			if (cuiEntity != null) {
				CuiGridService cuiGrid = cuiEntity.getCuiGrid();
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
			List<CocActionService> list = cocEntity.getActions(null);
			for (CocActionService a : list) {
				ret.add(a.getKey());
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
	public List<CocActionService> getCocActions() {
		List<CocActionService> allActions = new ArrayList();

		/*
		 * 获取实体操作
		 */
		CocEntityService cocEntity = getCocEntity();
		if (cocEntity != null) {
			CocActionService cocAction;

			List<String> actions = getActionKeys();
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
	public List<CocActionService> getCocActions(List<String> actionKeys) {
		if (actionKeys == null)
			return getCocActions();

		List<CocActionService> retActions = new ArrayList();

		/*
		 * 获取实体操作
		 */
		CocEntityService cocEntity = getCocEntity();
		if (cocEntity != null) {
			CocActionService cocAction;

			List<String> actions = getActionKeys();
			for (String a : actions) {
				if (!actionKeys.contains(a)) {
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
	public String getAuthrizedName() {
		return this.getKey();
	}

	@Override
	public boolean isHidden() {
		return entityData.isHidden();
	}
}