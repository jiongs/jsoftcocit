package com.jsoft.cocimpl.ui.view.jcocitfield;

import java.io.Writer;
import java.util.Properties;

import com.jsoft.cocimpl.ui.view.BaseFieldView;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

public class SelectFieldView extends BaseFieldView {

	@Override
	public String getName() {
		return ViewNames.FIELD_VIEW_SELECT;
	}

	protected String getElementBegin() {
		return "<select";
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

		Option[] options = fieldModel.getDicOptions();

		Option selectedOption = fieldModel.getDicOption(fieldValue);
		String selectedValue = selectedOption == null ? "" : selectedOption.getValue();

		/*
		 * 获取 CSS class 属性
		 */
		Properties props = fieldModel.getAttributes();
		String cssClass = props.getProperty("class");
		if (cssClass != null) {
			props.remove("class");
		} else {
			cssClass = "select";
		}

		// Select
		write(out, "value=\"%s\" class=\"%s\"", //
		        StringUtil.escapeHtml(selectedValue),//
		        cssClass//
		);

		renderAttrs(out, props);

		write(out, ">");

		write(out, "<option value=\"\"></option>");

		// options
		for (Option option : options) {
			write(out, "<option %s value=\"%s\">%s</option>", //
			        ((option.getValue().equals(selectedValue)) ? "selected" : ""), //
			        StringUtil.escapeHtml(option.getValue()), //
			        StringUtil.escapeHtml(option.getText())//
			);
		}

	}

	protected String getElementEnd() {
		return "</select>";
	}

}
