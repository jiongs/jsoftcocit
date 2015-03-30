package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocimpl.ui.UIViews;
import com.jsoft.cocimpl.ui.view.BaseModelView;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.ui.model.control.UIActions;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UIFieldGroup;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.ui.model.datamodel.UIFormData;
import com.jsoft.cocit.ui.view.UIFieldView;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree.Node;

public abstract class UIFormViews {
	public static class UIFormView extends BaseModelView<UIForm> {
		public String getName() {
			return ViewNames.VIEW_FORM;
		}

		public void render(Writer out, UIForm model) throws Exception {
			String resultUI = Cocit.me().getHttpContext().getClientResultUI();
			String uiToken = Cocit.me().getHttpContext().getClientUIToken();

			/*
			 * 创建 FORM 标签
			 */
			write(out, "<form class=\"entityForm\" action=\"%s\" onsubmit=\"return false;\">", model.getSubmitUrl());

			/*
			 * ID字段
			 */
			// write(out, "<input name=\"%s.id\" type=\"hidden\" value=\"%s\">", model.getBeanName(), ObjectUtil.idOrtoString(model.getDataObject()));

			List<List<String>> fields = model.getFields();
			List<String> batchFields = model.getBatchFields();
			if ((fields == null || fields.size() == 0) && (batchFields == null || batchFields.size() == 0)) {
				renderAutoLayoutFields(out, model);
			} else {
				renderFieldsWithLeftLabel(out, model, fields);
				renderFieldsWithTopLabel(out, model, model.getBeanName(), batchFields);
			}

			// if (!model.isAjax()) {
			String formButtonsCSS = Cocit.me().getConfig().getViewConfig().getFormButtonsCSS();
			write(out, "<div class=\"%s\"><div>", formButtonsCSS);
			// write(out, "<div class=\"entityButtons\">");
			UIActions actions = model.getActions();
			String buttonStyle = Cocit.me().getConfig().getViewConfig().getButtonStyle();
			if (actions != null && actions.getData() != null && actions.getData().getChildren().size() > 0) {
				List<Node> nodes = actions.getData().getChildren();
				for (Node node : nodes) {

					CocActionService action = (CocActionService) node.getReferObj();
					if (!ExprUtil.match(model.getDataObject(), action.getWhere())) {
						continue;
					}

					StringBuffer options = new StringBuffer();
					append(options, "name:'%s'", node.getName());
					append(options, ", token:'%s'", uiToken);

					if (resultUI.length() > 0) {
						append(options, ", resultUI: %s", resultUI);// 菜单通过该令牌获取DataGrid对象
					}

					String urlExpr = node.get("urlExpr", "");
					if (!StringUtil.isBlank(urlExpr)) {
						append(options, ", urlExpr: '%s'", urlExpr);
					} else {
						String str = node.get("opUrl", "");
						if (!StringUtil.isBlank(str))
							append(options, ", opUrl: '%s'", str);
					}

					String str = node.get("opUrlTarget", "");
					if (!StringUtil.isBlank(str))
						append(options, ", opUrlTarget: '%s'", str);

					String opMode = node.get("opMode", "");
					if (!StringUtil.isBlank(opMode))
						append(options, ", opMode: '%s'", opMode);

					str = node.get("opCode", "");
					if (!StringUtil.isBlank(str)) {
						append(options, ", opCode: %s", str);
					}

					String msg = node.get("successMessage", "");
					if (!StringUtil.isBlank(msg))
						append(options, ", successMessage: '%s'", msg.replace("'", ""));
					msg = node.get("errorMessage", "");
					if (!StringUtil.isBlank(msg))
						append(options, ", errorMessage: '%s'", msg.replace("'", ""));
					msg = node.get("warnMessage", "");
					if (!StringUtil.isBlank(msg))
						append(options, ", warnMessage: '%s'", msg.replace("'", ""));

					if ("button".equals(buttonStyle)) {
						write(out, "<input type=\"submit\" data-options=\"%s\" onclick=\"jCocit.entity.doSubmitForm(this); return false;\" value=\"%s\" />", options, node.getName());
					} else {
						write(out, "<a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-button\"  data-options=\"%s, onClick:jCocit.entity.doSubmitForm\">%s</a>", options, node.getName());
					}
				}
			} else {
				if ("button".equals(buttonStyle)) {
					if (model.getActionID().startsWith("v")) {
						write(out, "<input type=\"submit\" onclick=\"jCocit.util.closeWindow(this, %s); return false;\" value=\"关闭\" />", resultUI);
					} else {
						write(out, "<input type=\"submit\" onclick=\"jCocit.util.submitForm(this, %s);return false;\" value=\"提交\" />", resultUI);
						write(out, "<input type=\"submit\" onclick=\"jCocit.util.closeWindow(this, %s); return false;\" value=\"取消\" />", resultUI);
					}
				} else {
					if (model.getActionID().startsWith("v")) {
						write(out, "<a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-button\"  data-options=\"resultUI: %s, onClick: jCocit.util.closeWindow\">关闭</a>", resultUI);
					} else {
						write(out, "<a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-button\"  data-options=\"resultUI: %s, onClick: jCocit.util.submitForm\">提交</a>", resultUI);
						write(out, "<a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-button\"  data-options=\"resultUI: %s, onClick: jCocit.util.closeWindow\">取消</a>", resultUI);
					}
				}
			}

			write(out, "</div></div>");
			// }

			write(out, "</form>");
		}
	}

	public static class UISubFormView extends BaseModelView<UIForm> {
		public String getName() {
			return ViewNames.VIEW_SUBFORM;
		}

		public void render(Writer out, UIForm model) throws Exception {

			List<List<String>> fields = model.getFields();
			List<String> batchFields = model.getBatchFields();
			if ((fields == null || fields.size() == 0) && (batchFields == null || batchFields.size() == 0)) {
				renderAutoLayoutFields(out, model);
			} else {
				renderFieldsWithLeftLabel(out, model, fields);
				renderFieldsWithTopLabel(out, model, model.getBeanName(), batchFields);
			}

		}

	}

	private static void write(Writer out, String format, Object... args) throws IOException {
		out.write("\n");
		if (args.length == 0)
			out.write(format);
		else
			out.write(String.format(format, args));
	}

	private static void renderFieldsWithTopLabel(Writer out, UIForm model, String listName, List<String> batchFields) throws Exception {
		if (batchFields == null || batchFields.size() == 0)
			return;

		UIViews views = Cocit.me().getViews();
		Object dataObject = model.getDataObject();

		/*
		 * 定义临时变量
		 */
		UIField uiField;
		UIFieldView view;
		Object fieldValue;
		String fieldName;

		write(out, "<table class=\"entityTableTop\" valign=\"top\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");

		StringWriter header = new StringWriter();
		StringWriter row = new StringWriter();
		StringWriter rowTemplate = new StringWriter();
		write(header, "<tr>");
		write(row, "<tr>");
		write(rowTemplate, "<tr>");

		int count = 0;
		String fieldNamePrefix = listName + "[" + count + "].";
		for (String propName : batchFields) {

			uiField = model.getField(propName);
			fieldName = fieldNamePrefix + propName;
			fieldValue = ObjectUtil.getValue(dataObject, propName);

			/*
			 * 字段输入框TD
			 */
			write(header, "<th class=\"entityFieldHeader\">%s</th>", uiField.getTitle());

			/*
			 * 空行
			 */
			write(row, "<td class=\"entityFieldBox\">");
			view = views.getFieldView(uiField.getViewName());
			view.render(row, uiField, dataObject, fieldName, fieldValue);
			write(row, "</td>");

			/*
			 * 模版
			 */
			write(rowTemplate, "<td class=\"entityFieldBox\">");
			view = views.getFieldView(uiField.getViewName());
			view.render(rowTemplate, uiField, dataObject, propName, fieldValue);
			write(rowTemplate, "</td>");
		}

		write(header, "<th class=\"entityFieldHeader\"><input class=\"entityRowOp\" onclick=\"try{jCocit.entity.doAddRow(this, '.batchFieldsTemplate', '%s');}catch(e){}return false;\" type=\"button\" value=\"+\"/></th>", listName);
		write(header, "</tr>");
		write(row, "<td class=\"entityFieldBox\"><input class=\"entityRowOp\" onclick=\"try{jCocit.entity.doDelRow(this);}catch(e){}return false;\" type=\"button\" value=\"-\"/></td>");
		write(row, "</tr>");
		write(rowTemplate, "<td class=\"entityFieldBox\"><input class=\"entityRowOp\" onclick=\"try{jCocit.entity.doDelRow(this);}catch(e){}return false;\" type=\"button\" value=\"-\"/></td>");
		write(rowTemplate, "</tr>");

		write(out, header.toString());
		write(out, row.toString());
		write(out, "</table>");
		write(out, "<textarea class=\"batchFieldsTemplate\" count=\"%s\" style=\"display:none;\">", count);
		write(out, StringUtil.escapeHtml(rowTemplate.toString()));
		write(out, "</textarea>");
	}

	// private void renderFieldsWithUpLabel(Writer out, UIForm model, List<List<String>> fieldRows) throws Exception {
	// UIViews views = Cocit.me().getViews();
	// Object dataObject = model.getDataObject();
	//
	// /*
	// * 定义临时变量
	// */
	// UIField uiField;
	// UIFieldView view;
	// Object fieldValue;
	// String fieldName;
	// int fieldCount, colspan;
	// int fieldRowMaxSize = model.getRowFieldsSize();
	//
	// write(out, "<table class=\"entityTableUp\" valign=\"top\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
	//
	// for (List<String> fieldRow : fieldRows) {
	// fieldCount = 0;
	// colspan = 0;
	//
	// /*
	// * 字段分组
	// */
	// String groupNameWithProp = fieldRow.get(0);
	// int idx = groupNameWithProp.indexOf(":");
	// if (idx > -1) {
	// String groupName = groupNameWithProp.substring(0, idx);
	// groupNameWithProp = groupNameWithProp.substring(idx + 1);
	//
	// UIFieldGroup group = model.getFieldGroup(groupName);
	// if (group != null) {
	// write(out, "</tr><td class=\"entityGroup\" colspan=\"%s\"><div class=\"entityGroupHeader\">%s</div></td><tr>", //
	// group.getTitle(),//
	// fieldRowMaxSize//
	// );
	// }
	// }
	//
	// /*
	// * 字段行
	// */
	// StringWriter header = new StringWriter();
	// StringWriter box = new StringWriter();
	// for (String propName : fieldRow) {
	// fieldCount++;
	//
	// if (fieldCount == 1) {
	// idx = propName.indexOf(":");
	// if (idx > -1) {
	// propName = propName.substring(idx + 1);
	// }
	// }
	//
	// uiField = model.getField(propName);
	// fieldName = model.getBeanName() + "." + propName;
	// fieldValue = ObjectUtil.getValue(dataObject, propName);
	// colspan = uiField.getColspan();
	// if (colspan <= 1 && fieldCount == fieldRow.size()) {
	// colspan = (fieldRowMaxSize - fieldCount) + 1;
	// }
	//
	// /*
	// * 字段输入框TD
	// */
	// if (colspan > 1) {
	// write(header, "<th class=\"entityFieldHeader\" colspan=\"%s\">%s</th>", colspan, uiField.getTitle());
	// write(box, "<td class=\"entityFieldBox\" colspan=\"%s\">", colspan);
	// } else {
	// write(header, "<th class=\"entityFieldHeader\">%s</th>", uiField.getTitle());
	// write(box, "<td class=\"entityFieldBox\">");
	// }
	// view = views.getFieldView(uiField.getViewName());
	// view.render(box, uiField, dataObject, fieldName, fieldValue);
	// write(box, "</td>");
	// }
	//
	// write(out, "<tr>");
	// write(out, header.toString());
	// write(out, "</tr>");
	// write(out, "<tr>");
	// write(out, box.toString());
	// write(out, "</tr>");
	// }
	// write(out, "</table>");
	// }

	private static void renderFieldsWithLeftLabel(Writer out, UIForm model, List<List<String>> fields) throws Exception {
		if (fields == null || fields.size() == 0)
			return;

		UIViews views = Cocit.me().getViews();
		Object dataObject = model.getDataObject();

		/*
		 * 定义临时变量
		 */
		UIField uiField;
		UIFieldView view;
		Object fieldValue;
		String fieldName;
		int fieldCount, colspan, rowspan, mode;
		int fieldRowMaxSize = model.getRowFieldsSize();

		write(out, "<table valign=\"top\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");

		for (List<String> rowFields : fields) {
			fieldCount = 0;
			colspan = 0;
			rowspan = 0;

			/*
			 * 字段分组
			 */
			String groupNameWithProp = rowFields.get(0);
			int idx = groupNameWithProp.indexOf(":");
			if (idx > -1) {
				String groupName = groupNameWithProp.substring(0, idx);
				groupNameWithProp = groupNameWithProp.substring(idx + 1);

				UIFieldGroup group = model.getFieldGroup(groupName);
				if (group != null) {
					write(out, "</tr><td class=\"entityGroup\" colspan=\"%s\"><div class=\"entityGroupHeader\">%s</div></td><tr>", //
					        group.getTitle(),//
					        fieldRowMaxSize * 2//
					);
				}
			}

			/*
			 * 字段行
			 */
			write(out, "<tr>");
			for (String propName : rowFields) {
				fieldCount++;

				if (fieldCount == 1) {
					idx = propName.indexOf(":");
					if (idx > -1) {
						propName = propName.substring(idx + 1);
					}
				}

				uiField = model.getField(propName);
				if (uiField == null) {
					throw new CocException("字段“%s”不存在！", propName);
				}
				fieldName = model.getBeanName() + "." + propName;
				fieldValue = ObjectUtil.getValue(dataObject, propName);
				mode = uiField.getMode();
				rowspan = uiField.getRowspan();
				colspan = uiField.getColspan();
				if (colspan <= 1 && fieldCount == rowFields.size()) {
					colspan = (fieldRowMaxSize - fieldCount) * 2 + 1;
				}

				/*
				 * 字段标签
				 */
				if (rowspan > 1) {
					write(out, "<th class=\"entityFieldHeader\" rowspan=\"%s\">", rowspan);
					if (FieldModes.isM(mode)) {
						write(out, "<span class=\"icon-mode-M\"></span>");
					}
					write(out, "%s</th>", uiField.getTitle());
				} else {
					write(out, "<th class=\"entityFieldHeader\">");
					if (FieldModes.isM(mode)) {
						write(out, "<span class=\"icon-mode-M\"></span>");
					}
					write(out, "%s</th>", uiField.getTitle());
				}

				/*
				 * 字段输入框
				 */
				if (rowspan > 1) {
					if (colspan > 1) {
						write(out, "<td class=\"entityFieldBox\" rowspan=\"%s\" colspan=\"%s\">", rowspan, colspan);
					} else {
						write(out, "<td class=\"entityFieldBox\" rowspan=\"%s\">", rowspan);
					}
				} else {
					if (colspan > 1) {
						write(out, "<td class=\"entityFieldBox\" colspan=\"%s\">", colspan);
					} else {
						write(out, "<td class=\"entityFieldBox\">");
					}
				}
				view = views.getFieldView(uiField.getViewName());
				view.render(out, uiField, dataObject, fieldName, fieldValue);
				write(out, "</td>");

			}

			write(out, "</tr>");
		}
		write(out, "</table>");
	}

	/* 自动布局字段位置 */
	private static void renderAutoLayoutFields(Writer out, UIForm model) throws Exception {
		HttpContext ctx = Cocit.me().getHttpContext();
		UIViews views = Cocit.me().getViews();
		Object dataObject = model.getDataObject();
		int width = model.get("width", ctx.getClientUIWidth());

		/*
		 * 计算表单列数
		 */
		int columnSize = width / 400;
		if (columnSize > 2) {
			columnSize = 2;
		}

		/*
		 * 定义临时变量
		 */
		UIFieldView view;
		CocFieldService fieldService;
		Object fieldValue;
		String fieldName, propName;
		int columnCount = 0, colspan, mode;
		boolean isFirst = true;

		/*
		 * Render 隐藏字段
		 */
		for (String groupKey : model.getFieldGroups()) {
			UIFieldGroup group = model.getFieldGroup(groupKey);
			for (UIField field : group.getHiddenFields()) {
				fieldService = field.getFieldService();
				propName = fieldService.getFieldName();
				fieldName = model.getBeanName() + "." + propName;
				fieldValue = ObjectUtil.getValue(dataObject, propName);

				view = views.getFieldView(field.getViewName());
				view.render(out, field, dataObject, fieldName, fieldValue);

			}

		}

		/*
		 * 创建 FieldGroup
		 */
		write(out, "<div class=\"entityGroups\">");
		for (String groupKey : model.getFieldGroups()) {
			UIFieldGroup group = model.getFieldGroup(groupKey);
			List<UIField> visibleFields = group.getVisibleFields();

			// 该分组中没有需要显示的字段
			if (visibleFields.size() == 0)
				continue;

			/*
			 * FieldGroup Title；Table: for fields
			 */
			write(out, "<div class=\"entityGroup\"><div class=\"entityGroupHeader\">%s</div>", group.getTitle());
			write(out, "<table valign=\"top\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr>");

			for (UIField field : visibleFields) {
				fieldService = field.getFieldService();
				if (StringUtil.hasContent(fieldService.getFkDependFieldKey())) {
					continue;
				}

				// 字段跨越多少列？
				colspan = 1;

				// 列计数器
				columnCount++;
				if (columnCount > columnSize) {
					columnCount = 1;
				}

				propName = fieldService.getFieldName();
				fieldName = model.getBeanName() + "." + propName;
				fieldValue = ObjectUtil.getValue(dataObject, propName);
				mode = field.getMode();

				/*
				 * 创建一个新行
				 */
				if (field.isNewRow()) {
					if (columnCount > 1) {

						/*
						 * 填充上一行后面的空白列
						 */
						for (int i = columnCount; i <= columnSize; i++) {
							write(out, "<th class=\"entityFieldHeader\">&nbsp;</th><td class=\"entityFieldBox\">&nbsp;</td>");
						}

						write(out, "</tr><tr>");
					} else if (!isFirst) {
						write(out, "</tr><tr>");
					}

					columnCount = columnSize;
					colspan = columnSize * 2 - 1;
				}
				if (columnCount == 1 && !isFirst) {
					write(out, "</tr><tr>");
				}

				/*
				 * 字段标题
				 */
				write(out, "<th class=\"entityFieldHeader\">");
				if (FieldModes.isM(mode)) {
					write(out, "<span class=\"icon-mode-M\"></span>");
				}
				write(out, "%s</th>", fieldService.getName());

				/*
				 * 字段输入框
				 */
				write(out, "<td class=\"entityFieldBox\" colspan=\"%s\">", colspan);

				view = views.getFieldView(field.getViewName());
				view.render(out, field, dataObject, fieldName, fieldValue);

				write(out, "</td>");

				//
				isFirst = false;
			}

			/*
			 * 填充最后一行的空白列
			 */
			for (int i = columnCount + 1; i <= columnSize; i++) {
				write(out, "<th class=\"entityFieldHeader\">&nbsp;</th><td class=\"entityFieldBox\">&nbsp;</td>");
			}

			write(out, "</tr></table></div>");// end entityGroup
		}
		write(out, "</div>");

	}

	public static class UIFormDataView extends BaseModelView<UIFormData> {
		public String getName() {
			return ViewNames.VIEW_FORMDATA;
		}

		public void render(Writer writer, UIFormData model) throws Exception {

			String message = "操作成功！";
			int statusCode = 200;

			Throwable ex = model.getException();
			if (ex != null) {
				statusCode = 300;
				message = ExceptionUtil.msg(ex);
			}

			StringBuffer sb = new StringBuffer();
			sb.append('{');
			sb.append("\"success\" : " + (statusCode == 200));
			sb.append(", \"statusCode\" : " + statusCode);
			sb.append(", \"message\" : " + JsonUtil.toJson(message));

			/*
			 * JSON DATA
			 */
			StringBuffer data = new StringBuffer();
			data.append('{');
			CocFieldService entityField;
			String propName;
			Object fieldValue;
			Object formData = model.getData();
			String strFieldValue;
			data.append("\"id\": ").append(ObjectUtil.getValue(formData, Const.F_ID));
			for (String groupKey : model.getModel().getFieldGroups()) {
				UIFieldGroup group = model.getModel().getFieldGroup(groupKey);
				List<UIField> fields = new ArrayList();
				fields.addAll(group.getHiddenFields());
				fields.addAll(group.getVisibleFields());
				for (UIField field : fields) {

					entityField = field.getFieldService();

					propName = entityField.getFieldName();

					fieldValue = ObjectUtil.getValue(formData, propName);
					strFieldValue = field.format(fieldValue);

					// N：不显示
					if (FieldModes.isN(field.getMode()))
						continue;

					data.append(",\"" + propName + "\": ").append(JsonUtil.toJson(strFieldValue));
				}
			}
			data.append('}');

			sb.append(", \"data\": ").append(data);

			sb.append('}');

			write(writer, sb.toString());
		}
	}

}
