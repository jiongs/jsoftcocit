package com.jsoft.cocimpl.ui.view.jcocitfield;

import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jsoft.cocimpl.util.StyleUtil;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.view.BaseFieldView;
import com.jsoft.cocit.util.StringUtil;

public class ComboGridFieldView extends BaseFieldView {

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
		ICocFieldInfo fieldService = ((UIField) fieldModel).getFieldService();
		ICocEntityInfo fkModule = fieldService.getFkTargetEntity();

		UIGrid gridModel = null;
		List<UIFieldModel> columns = null;
		String dataUrl = null;
		if (fkModule != null) {
			gridModel = (UIGrid) Cocit.me().getUiModelFactory().getComboGrid(null, fkModule, fieldService);
			columns = gridModel.getColumns();
			dataUrl = gridModel.getDataLoadUrl();
		}

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
		boolean multiSelect = fieldModel.isMultiple();
		int columnWidth = 187;

		/*
		 * 输出字段 HTML
		 */
		write(out, "textFieldName=\"%s\" class=\"jCocit-ui jCocit-combogrid %s\" data-options=\"value: '%s', text: '%s', width: %s, ", //
		        textFieldName, //
		        cssClass == null ? "" : cssClass,//
		        idValue,//
		        textValue,//
		        width);
		write(out, "panelWidth: 380,");
		if (multiSelect) {
			write(out, "multiple: true,");
			write(out, "singleSelect: false,");
			columnWidth = 173;
		} else {
			write(out, "singleSelect: true,");
		}
		write(out, "rownumbers: false,");
		write(out, "panelHeight: 390,");
		write(out, "pagination: true,");
		write(out, "fitColumns: false,");
		write(out, "showRefresh: false,");
		write(out, "showPageList: false,");
		write(out, "idField: '%s',", targetIdField);
		write(out, "textField: '%s',", targetTextField);
		if (!StringUtil.isBlank(dataUrl)) {
			write(out, "url: '%s',", dataUrl);
		}
		write(out, "mode: 'remote',");
		write(out, "pageSize: %s,", 10);
		write(out, "columns: [[");

		write(out, "{field:'id', title:'ID', width:80, align:'right', %s},", multiSelect ? "checkbox: true" : "hidden: true");

		if (columns != null) {
			for (UIFieldModel col : columns) {
				write(out, "{field:'%s',title:'%s',width:%s,sortable:true,align:'center',halign:'center'},", //
				        col.getPropName(), //
				        col.getTitle(),//
				        columnWidth //
				);
			}
		}

		write(out, "]],");
		write(out, "\"");

		renderAttrs(out, attrs);

	}

	@Override
	public String getName() {
		return ViewNames.FIELD_VIEW_COMBOGRID;
	}

}
