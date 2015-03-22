package com.jsoft.cocit.ui;

import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.ui.model.control.UIActions;
import com.jsoft.cocit.ui.model.control.UIEntities;
import com.jsoft.cocit.ui.model.control.UIEntity;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.model.control.UIList;
import com.jsoft.cocit.ui.model.control.UISearchBox;
import com.jsoft.cocit.ui.model.control.UITree;
import com.jsoft.cocit.ui.model.datamodel.UIGridData;
import com.jsoft.cocit.ui.model.datamodel.UITreeData;

/**
 * UI模型工厂：负责创建或管理用户窗体的逻辑模型
 * 
 * @author jiongs753
 * 
 */
public interface UIModelFactory {
	UIEntities getMains(SystemMenuService menuService);

	UIEntity getMain(SystemMenuService menuService, CocEntityService entityService, boolean usedToSubEntity);

	// /**
	// * @deprecated 用{@link #getUIEntity(SystemMenuService, CocEntityService, boolean)}代替
	// */
	// UIEntity getUIEntityWithMenu(SystemMenuService menuService, CocEntityService entityService, boolean usedToSubEntity);
	//
	// /**
	// * @deprecated 用{@link #getUIEntity(SystemMenuService, CocEntityService, boolean)}代替
	// */
	// UIEntity getUIEntityWithButtons(SystemMenuService menuService, CocEntityService entityService, boolean usedToSubEntity);

	UIForm getForm(SystemMenuService menuService, CocEntityService entityService, CocActionService entityAction, Object dataObject);

	UIGrid getGrid(SystemMenuService menuService, CocEntityService entityService);

	UIGrid getComboGrid(SystemMenuService targetMenuService, CocEntityService targetEntityService, CocFieldService fkFieldService);

	UIList getComboList(SystemMenuService targetMenuService, CocEntityService targetEntityService, CocFieldService fkFieldService);

	UITree getComboTree(SystemMenuService targetMenuService, CocEntityService targetEntityService, CocFieldService fkFieldService);

	UITreeData getComboTreeData(SystemMenuService systemMenu, CocEntityService cocEntity, CndExpr expr);

	UITree getTree(SystemMenuService menuService, CocEntityService entityService);

	UITreeData getTreeData(SystemMenuService menuService, CocEntityService entityService);

	UISearchBox getSearchBox(SystemMenuService menuService, CocEntityService entityService);

	UIActions getActions(SystemMenuService menuService, CocEntityService entityService);

	// /**
	// * @deprecated 用{@link #getUIActions(SystemMenuService, CocEntityService)}代替
	// */
	// UIActions getUIActionsMenu(SystemMenuService menuService, CocEntityService entityService);
	//
	// /**
	// * @deprecated 用{@link #getUIActions(SystemMenuService, CocEntityService)}代替
	// */
	// UIActions getUIActionsButtons(SystemMenuService menuService, CocEntityService entityService);

	UITree getFilter(SystemMenuService menuService, CocEntityService entityService, boolean usedToSubEntity);

	UITreeData getFilterData(SystemMenuService menuService, CocEntityService entityService, boolean usedToSubEntity);

	UITreeData getRowsAuthData(SystemMenuService menuService, CocEntityService entityService);

	UITreeData getActionsData(SystemMenuService menuService, CocEntityService entityService);

	UIGridData makeGridData();

	UIGrid makeGrid();

	UIField makeField();

}
