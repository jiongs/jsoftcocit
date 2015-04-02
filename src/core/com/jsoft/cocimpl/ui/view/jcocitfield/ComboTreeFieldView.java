package com.jsoft.cocimpl.ui.view.jcocitfield;

import java.io.Writer;
import java.util.Properties;

import com.jsoft.cocimpl.ui.view.BaseFieldView;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.util.StringUtil;

public class ComboTreeFieldView extends BaseFieldView {

	@Override
	protected void renderElementAttr(//
	        Writer out, //
	        UIFieldModel fieldModel,//
	        Object entityObject,//
	        Object fieldValue,//
	        String idFieldName,//
	        String textFieldName,//
	        String idValue,//
	        String textValue,//
	        String targetIdField,//
	        String targetTextField //
	) throws Exception {

		/*
		 * 获取 CSS class 属性
		 */
		Properties props = fieldModel.getAttributes();
		String cssClass = props.getProperty("class");
		if (cssClass != null) {
			props.remove("class");
		}

		Boolean checkOnSelect = fieldModel.get("checkOnSelect");
		if (checkOnSelect == null) {
			checkOnSelect = false;
		}
		Boolean selectOnCheck = fieldModel.get("selectOnCheck");
		if (selectOnCheck == null) {
			selectOnCheck = false;
		}
		String onCheck = fieldModel.get("onCheck");
		if (StringUtil.hasContent(onCheck)) {
			cssClass += " jCocit-dosearch";
		}

		boolean multiSelect = fieldModel.isMultiple();
		if (multiSelect) {
			idValue = StringUtil.toJSArray(StringUtil.toList(idValue));
			textValue = StringUtil.toJSArray(StringUtil.toList(textValue));
		} else {
			idValue = "'" + idValue + "'";
			textValue = "'" + textValue + "'";
		}

		write(out, "textFieldName=\"%s\" class=\"jCocit-ui jCocit-combotree %s\" data-options=\"value:%s, text:%s, dataURL: '%s' %s %s %s %s\" style=\"width:%spx;\"", //
		        textFieldName, //
		        cssClass == null ? "" : cssClass,//
		        idValue,//
		        textValue,//
		        fieldModel.getFkComboTreeUrl(),//
		        (multiSelect ? ", multiple: true" : ""),//
		        (checkOnSelect ? ", checkOnSelect: true" : ""),//
		        (selectOnCheck ? ", selectOnCheck: true" : ""),//
		        (StringUtil.hasContent(onCheck) ? (", onCheck: " + onCheck) : ""),//

		        fieldModel.getWidth()//
		);

		renderAttrs(out, props);
	}

	@Override
	public String getName() {
		return ViewNames.FIELD_VIEW_COMBOTREE;
	}

}
