package com.jsoft.cocimpl.ui.view.jcocitfield;

import java.io.Writer;

import com.jsoft.cocimpl.ui.view.BaseFieldView;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

public class RadioFieldView extends BaseFieldView {

	protected String getElementBegin() {
		return "";
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

		// options
		int count = 0;
		for (Option option : options) {
			write(out, "<input type=\"radio\" id=\"%s_%s\" name=\"%s\" value=\"%s\" %s /><label for=\"%s_%s\">%s</label>", //
			        fieldModel.getId(), count,//
			        idFieldName, //
			        StringUtil.escapeHtml(option.getValue()), //
			        ((option.getValue().equals(selectedValue)) ? "checked" : ""), //
			        fieldModel.getId(), count,//
			        StringUtil.escapeHtml(option.getText())//
			);
			count++;
		}

	}

	protected String getElementEnd() {
		return "";
	}

	@Override
	public String getName() {
		return ViewNames.FIELD_VIEW_RADIO;
	}

}
