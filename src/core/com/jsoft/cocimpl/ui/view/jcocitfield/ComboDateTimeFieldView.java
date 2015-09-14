package com.jsoft.cocimpl.ui.view.jcocitfield;

import java.io.Writer;
import java.util.Properties;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.BaseFieldView;
import com.jsoft.cocit.util.StringUtil;

public class ComboDateTimeFieldView extends BaseFieldView {

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

		write(out, "value=\"%s\" class=\"jCocit-ui jCocit-combodatetime %s\" height=\"%spx\" data-options=\"width:%s\"",//
		        StringUtil.escapeHtml(fieldModel.format(fieldValue)),//
		        cssClass == null ? "" : cssClass,//
		        24,//
		        fieldModel.getWidth()//
		);

		renderAttrs(out, props);

	}

	@Override
	public String getName() {
		return ViewNames.FIELD_VIEW_COMBODATETIME;
	}

}
