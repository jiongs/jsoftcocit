package com.jsoft.cocimpl.ui.view.jcocitfield;

import java.io.Writer;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.ui.view.UIFieldView;

/**
 * 
 * 用于主从表数据维护：批量编辑子表数据
 * 
 * @author Ji Yongshan
 * 
 */
public class OneToManyFieldView implements UIFieldView {

	@Override
	public String getName() {
		return ViewNames.FIELD_VIEW_ONE2MANY;
	}

	@Override
	public void render(Writer out, UIFieldModel fieldModel, Object entityObject, String fieldName, Object fieldValue) throws Exception {
		UIField field = (UIField) fieldModel;
		UIForm uiForm = field.getOneToManyTargetForm();
		uiForm.render(out);
	}

}
