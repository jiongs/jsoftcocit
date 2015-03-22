package com.jsoft.cocimpl.ui.view.jcocitfield;

import java.io.Writer;
import java.util.Properties;

import com.jsoft.cocimpl.ui.view.BaseFieldView;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.util.StringUtil;

public class PasswordFieldView extends BaseFieldView {

	@Override
	public String getName() {
		return ViewNames.FIELD_VIEW_PASSWORD;
	}

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
		} else {
			cssClass = "input";
		}

		write(out, "type=\"password\" value=\"%s\" class=\"%s\"",//
		        StringUtil.escapeHtml(fieldModel.format(fieldValue)),//
		        cssClass//
		);

		renderAttrs(out, props);
	}

}
