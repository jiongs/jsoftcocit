package com.jsoft.cocit.ui;

import java.util.List;

import com.jsoft.cocit.dmengine.info.ICocActionInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
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
	UIEntities getMains(ISystemMenuInfo menuService);

	UIEntity getMain(ISystemMenuInfo menuService, ICocEntityInfo entityService, boolean usedToSubEntity);

	// /**
	// * @deprecated 用{@link #getUIEntity(SystemMenuService, CocEntityService, boolean)}代替
	// */
	// UIEntity getUIEntityWithMenu(SystemMenuService menuService, CocEntityService entityService, boolean usedToSubEntity);
	//
	// /**
	// * @deprecated 用{@link #getUIEntity(SystemMenuService, CocEntityService, boolean)}代替
	// */
	// UIEntity getUIEntityWithButtons(SystemMenuService menuService, CocEntityService entityService, boolean usedToSubEntity);

	UIForm getForm(ISystemMenuInfo menuService, ICocEntityInfo entityService, ICocActionInfo entityAction, Object dataObject);

	UIForm getForm(ISystemMenuInfo menuService, ICocEntityInfo entityService, ICocActionInfo entityAction, Object dataObject, List<String> fieldList);

	UIGrid getGrid(ISystemMenuInfo menuService, ICocEntityInfo entityService);

	UIGrid getGrid(ISystemMenuInfo menuService, ICocEntityInfo entityService, List<String> fieldList, List<String> rowActionList);

	UIGrid getComboGrid(ISystemMenuInfo targetMenuService, ICocEntityInfo targetEntityService, ICocFieldInfo fkFieldService);

	UIList getComboList(ISystemMenuInfo targetMenuService, ICocEntityInfo targetEntityService, ICocFieldInfo fkFieldService);

	UITree getComboTree(ISystemMenuInfo targetMenuService, ICocEntityInfo targetEntityService, ICocFieldInfo fkFieldService);

	UITreeData getComboTreeData(ISystemMenuInfo systemMenu, ICocEntityInfo cocEntity, CndExpr expr);

	UITree getTree(ISystemMenuInfo menuService, ICocEntityInfo entityService);

	UITreeData getTreeData(ISystemMenuInfo menuService, ICocEntityInfo entityService, CndExpr expr);

	UISearchBox getSearchBox(ISystemMenuInfo menuService, ICocEntityInfo entityService);

	UISearchBox getSearchBox(ISystemMenuInfo menuService, ICocEntityInfo entityService, List<String> fields);

	UIActions getActions(ISystemMenuInfo menuService, ICocEntityInfo entityService);

	UIActions getActions(ISystemMenuInfo menuService, ICocEntityInfo entityService, List<String> actionKeys);

	// /**
	// * @deprecated 用{@link #getUIActions(SystemMenuService, CocEntityService)}代替
	// */
	// UIActions getUIActionsMenu(SystemMenuService menuService, CocEntityService entityService);
	//
	// /**
	// * @deprecated 用{@link #getUIActions(SystemMenuService, CocEntityService)}代替
	// */
	// UIActions getUIActionsButtons(SystemMenuService menuService, CocEntityService entityService);

	UITree getFilter(ISystemMenuInfo menuService, ICocEntityInfo entityService, boolean usedToSubEntity);

	UITreeData getFilterData(ISystemMenuInfo menuService, ICocEntityInfo entityService, boolean usedToSubEntity);

	UITreeData getRowsAuthData(ISystemMenuInfo menuService, ICocEntityInfo entityService);

	UITreeData getActionsData(ISystemMenuInfo menuService, ICocEntityInfo entityService);

	UIGridData makeGridData();

	UIGrid makeGrid();

	UIField makeField();

}
