package com.jsoft.cocimpl.ui.view.gridcell.security;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.action.beans.AuthItem;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.WriteUtil;

public class RowsAuthCellView implements UICellView {

	@Override
	public String getName() {
		return "rowsauth";
	}

	@Override
	public void render(Writer out, UIFieldModel fieldModel, Object dataObject, String fieldName, Object fieldValue) throws Exception {
		AuthItem authItem = (AuthItem) dataObject;
		String rowsAuthUrl = (String) fieldValue;
		String allowRows = authItem.getAllowRows();
		String allowRowsNames = authItem.getAllowRowsNames();
		if (allowRowsNames == null) {
			allowRowsNames = "";
		}

		List<String> valueList = new ArrayList();
		try {
			Map<String, List<String>> json = JsonUtil.loadFromJson(Map.class, allowRows);
			if (json != null) {
				Iterator<String> fields = json.keySet().iterator();
				while (fields.hasNext()) {
					String fld = fields.next();
					List<String> fldvalues = json.get(fld);
					for (String v : fldvalues) {
						valueList.add(fld + ":" + v);
					}
				}
			}
		} catch (Throwable e) {
			allowRowsNames = ExceptionUtil.msg(e);
		}

		if (rowsAuthUrl != null && rowsAuthUrl.trim().length() > 0) {
			WriteUtil
			        .write(out,
			                "<input name=\"dataRows\" textFieldName=\"dataRowsNames\" class=\"jCocit-ui jCocit-combotree\" data-options=\"value: %s, text:'%s', onlyLeafValue: true, editable: false, multiple: true, dataURL: '%s', widthDiff: 8, separator: '，'\" style=\"height: 26px; overflow: auto;\" >", //
			                StringUtil.toJSArray(valueList),//
			                allowRowsNames,//
			                rowsAuthUrl//
			        );
		}
	}
}
