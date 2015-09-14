package com.jsoft.cocimpl.ui.view.jcocitfield;

import java.io.Writer;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.BaseFieldView;
import com.jsoft.cocit.util.StringUtil;

public class CKEditorFieldView extends BaseFieldView {

	@Override
	public String getName() {
		return ViewNames.FIELD_VIEW_CKEDITOR;
	}

	protected String getElementBegin() {
		return "<textarea";
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

		write(out, "class=\"jCocit-ui jCocit-ckeditor\">%s", StringUtil.escapeHtml(fieldModel.format(idValue)));
	}

	protected String getElementEnd() {
		return "</textarea>";
	}
}
