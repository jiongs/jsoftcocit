package com.jsoft.cocimpl.ui.view.jcocitfield;

import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import com.jsoft.cocimpl.ui.view.BaseFieldView;
import com.jsoft.cocimpl.util.StyleUtil;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;

public class UploadFieldView extends BaseFieldView {

	@Override
	public String getName() {
		return ViewNames.FIELD_VIEW_UPLOAD;
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

		CocFieldService fieldService = ((UIField) fieldModel).getFieldService();

		Properties attrs = fieldModel.getAttributes();
		
		/*
		 * 获取 CSS class 属性
		 */
		String cssClass = attrs.getProperty("class");
		if (cssClass != null) {
			attrs.remove("class");
		}

		/*
		 * 解析字段宽度
		 */
		Integer width = null;
		String styleText = attrs.getProperty("style");
		Map<String, String> styleMap = StyleUtil.parseMap(styleText);
		if (styleMap != null) {
			String strWidth = styleMap.get("width");
			if (strWidth != null) {
				width = StyleUtil.parseInt(strWidth);
			}
		}
		if (width == null)
			width = fieldModel.getWidth() + 2;

		write(out, "type=\"file\" class=\"jCocit-ui jCocit-upload\" data-options=\"value:'%s', text:'%s', fileTypeExts : '%s', fileTypeDesc : '%s', comboWidth : %s, comboHeight : %s, uploader: '%s' \"",//
				fieldModel.format(fieldValue),//
				fieldModel.format(fieldValue),//
				StringUtil.isBlank(fieldModel.getPattern()) ? "" : fieldModel.getPattern(),//
				"",//
				width,//
				24,//
				MVCUtil.makeUrl(UrlAPI.URL_UPLOAD, fieldService.getCocEntityKey(), fieldService.getKey()) + ";jsessionid=" + Cocit.me().getHttpContext().getRequest().getSession().getId()//
		);

//		renderAttrs(out, attrs);
	}
}
