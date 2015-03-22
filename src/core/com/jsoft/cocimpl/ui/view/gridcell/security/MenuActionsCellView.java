package com.jsoft.cocimpl.ui.view.gridcell.security;

import java.io.Writer;
import java.util.List;

import com.jsoft.cocimpl.entityengine.EntityServiceFactory;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.entity.security.ISystemMenu;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.util.WriteUtil;

public class MenuActionsCellView implements UICellView {

	@Override
	public String getName() {
		return "menuactions";
	}

	@Override
	public void render(Writer out, UIFieldModel fieldModel, Object dataObject, String fieldName, Object fieldValue) throws Exception {
		Cocit coc = Cocit.me();
		EntityServiceFactory serviceFactory = coc.getEntityServiceFactory();

		ISystemMenu menu = (ISystemMenu) dataObject;
		String menuKey = menu.getKey();

		SystemService systemService = serviceFactory.getSystem(menu.getSystemKey());
		SystemMenuService menuService = systemService.getSystemMenu(menuKey);

		if (menuService == null)
			return;

		List<CocActionService> cocActions = menuService.getCocActions();
		int count = 0;
		for (CocActionService a : cocActions) {
			if (count > 0) {
				WriteUtil.write(out, ", ");
			}
			WriteUtil.write(out, "%s", a.getName());

			count++;
		}

	}
}
