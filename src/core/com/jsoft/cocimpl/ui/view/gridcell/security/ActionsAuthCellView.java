package com.jsoft.cocimpl.ui.view.gridcell.security;

import java.io.Writer;
import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.action.beans.AuthItem;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.WriteUtil;

public class ActionsAuthCellView implements UICellView {

	@Override
	public String getName() {
		return "actionsauth";
	}

	@Override
	public void render(Writer out, UIFieldModel fieldModel, Object dataObject, String fieldName, Object fieldValue) throws Exception {

		AuthItem authItem = (AuthItem) dataObject;
		List<CocActionService> menuActions = authItem.getActionsAuth();
		List<String> allowActions = StringUtil.toList(authItem.getAllowActions());

		String token = Cocit.me().getHttpContext().getClientUIToken();

		String ckAll = "";
		if (allowActions.size() == menuActions.size()) {
			ckAll = "Ck1";
		} else if (allowActions.size() > 0) {
			ckAll = "Ck2";
		}

		if (menuActions != null && menuActions.size() > 0) {
			String id = authItem.getMenuKey() + "_allactions_" + token;
			// WriteUtil.write(out, "<fieldset><legend><span class=\"CkAll Ck %s\"></span><span class=\"CkT\">全部</span></legend><div class=\"CkList\">", ckAll, id, id);
			for (CocActionService a : menuActions) {
				String actionKey = a.getKey();
				String actionName = a.getName();

				id = authItem.getMenuKey() + "_" + actionKey + "_" + token;

				WriteUtil.write(out, "<span class=\"CkOne Ck %s\" val=\"%s\"></span><span class=\"CkT\">%s</span>", (allowActions.contains(actionKey) ? "Ck1" : ""), actionKey, actionName);
			}
			// WriteUtil.write(out, "</div></fieldset>");
		}

		// AuthItem authItem = (AuthItem) dataObject;
		// String actionsAuthUrl = (String) fieldValue;
		// String allowActions = authItem.getAllowActions();
		// String allowActionsName = authItem.getAllowActionsNames();
		// if (allowActionsName == null) {
		// allowActionsName = "";
		// }
		// String allowActionsJSArray = StringUtil.toJSArray(StringUtil.toList(allowActions));
		//
		// if (actionsAuthUrl != null && actionsAuthUrl.trim().length() > 0) {
		// WriteUtil
		// .write(out,
		// "<input name=\"menuActions\" textFieldName=\"menuActionsNames\" class=\"jCocit-ui jCocit-combotree\" data-options=\"value: %s, text:'%s', onlyLeafValue: true, editable: false, multiple: true, dataURL: '%s', widthDiff: 8, separator: '，'\" style=\"height: 26px; overflow: auto;\" >",
		// //
		// allowActionsJSArray,//
		// allowActionsName,//
		// actionsAuthUrl//
		// );
		// }
		//
	}
}
