package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lilystudio.util.StringWriter;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.constant.FieldTypes;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.ui.UIViews;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.control.UIActions;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.model.datamodel.UIGridData;
import com.jsoft.cocit.ui.view.BaseModelView;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.SortUtil;
import com.jsoft.cocit.util.StringUtil;

public abstract class UIGridViews {

	public static class UIGridView extends BaseModelView<UIGrid> {
		public String getName() {
			return ViewNames.VIEW_GRID;
		}

		public void render(Writer out, UIGrid model) throws Exception {
			String title = "";// model.getTitle();
			int height = model.get("height", Cocit.me().getHttpContext().getClientUIHeight());
			int colTotalWidth = model.getColumnsTotalWidth();
			if (colTotalWidth == 0) {
				for (UIFieldModel c : model.getColumns()) {
					colTotalWidth += c.getWidth();
				}
				if (colTotalWidth == 0) {
					colTotalWidth = 800;
				}
			}

			String token = Cocit.me().getHttpContext().getClientUIToken();
			List<UIFieldModel> columns = model.getColumns();

			/*
			 * 生成Styler JS代码
			 */
			write(out, "<script type=\"text/javascript\">");
			StyleRule[] rowStyles = model.getRowStyles();
			if (rowStyles != null) {
				StringBuffer styler = new StringBuffer();

				styler.append("\nfunction rowStyler_" + token + "(index, row){ ");
				for (StyleRule rule : rowStyles) {
					Map where = rule.getWhere();
					String style = rule.getStyle();

					if (where == null || style == null) {
						throw new CocException("解析行条件样式出错：where=%s, style=%s", where, style);
					}

					Iterator<String> keys = where.keySet().iterator();
					while (keys.hasNext()) {
						String field = keys.next();
						Object value = where.get(field);
						List list = new ArrayList();
						if (value instanceof List) {
							list = (List) value;
						} else {
							list.add(value);
						}

						UIFieldModel uiField = model.getColumn(field);

						styler.append("\n\tswitch(row." + field + "){");
						for (Object o : list) {
							styler.append("\n\t\tcase '").append(StringUtil.escapeHtml(uiField.format(o))).append("':");
						}
						styler.append("\n\t\t\treturn '").append(style.replace("'", "").replace("\"", "")).append("';");
						styler.append("\n\t}");

					}
				}
				styler.append("\n }");

				write(out, styler.toString());
			}
			for (UIFieldModel col : columns) {

				StyleRule[] rules = col.getCellStyles();
				if (rules != null) {
					StringBuffer styler = new StringBuffer();

					styler.append("\nfunction cellStyler_" + token + "_" + col.getPropName() + "(value, row, index){ ");
					for (StyleRule rule : rules) {
						Map where = rule.getWhere();
						String style = rule.getStyle();

						if (where == null || style == null) {
							throw new CocException("解析单元格条件样式出错：where=%s, style=%s", where, style);
						}

						Iterator<String> keys = where.keySet().iterator();
						while (keys.hasNext()) {
							String field = keys.next();
							Object value = where.get(field);
							List list = new ArrayList();
							if (value instanceof List) {
								list = (List) value;
							} else {
								list.add(value);
							}

							UIFieldModel uiField = model.getColumn(field);

							styler.append("\n\tswitch(row." + field + "){");
							for (Object o : list) {
								styler.append("\n\t\tcase '").append(StringUtil.escapeHtml(uiField.format(o))).append("':");
							}
							styler.append("\n\t\t\treturn '").append(style.replace("'", "").replace("\"", "")).append("';");
							styler.append("\n\t}");
						}
					}
					styler.append("\n }");

					write(out, styler.toString());
				}
			}
			write(out, "\n</script>");

			UIActions rowActions = model.getRowActions();
			int rowActionsWidth = 0;
			int gridWidth = model.get("width", Cocit.me().getHttpContext().getClientUIWidth());
			int columnsWidth = gridWidth - 65 - rowActionsWidth;// 56:Grid checkbox rownumbers 宽度和 + 滚动条

			int pageSize = model.getPageSize();
			pageSize = (height - 100) / 32 / 5 * 5;
			if (pageSize < 10) {
				pageSize = 10;
				columnsWidth -= 25;
			}
			List<Integer> pageOptions = new ArrayList();
			pageOptions.add(10);
			pageOptions.add(20);
			pageOptions.add(50);
			pageOptions.add(100);
			if (!pageOptions.contains(pageSize)) {
				pageOptions.add(pageSize);
			}
			SortUtil.sort(pageOptions, null, false);

			double fixColRate = new Double(columnsWidth) / new Double(colTotalWidth);

			boolean singleSelect = (boolean) model.get("singleSelect", false);
			boolean checkbox = (boolean) model.get("checkbox", true);
			String sortField = "id";
			String sortOrder = "desc";
			List<String> sortExpr = StringUtil.toList(model.getSortExpr());
			if (sortExpr != null) {
				if (sortExpr.size() > 0) {
					sortField = sortExpr.get(0);
					sortOrder = "asc";
				}
				if (sortExpr.size() > 1) {
					sortOrder = sortExpr.get(1);
				}
			}
			boolean pagination = (boolean) model.get("pagination", true);
			boolean noSelect = (boolean) model.get("noSelect", false);

			String url = model.getDataLoadUrl();

			String attrName = model.getAttributes().getProperty("name");

			write(out, "<table id=\"%s\" name=\"%s\" class=\"jCocit-ui jCocit-%s entity-%s\" title=\"%s\" style=\"height: %spx; width:auto;\" data-options=\"", //
			        model.getId(), //
			        attrName == null ? "" : attrName, //
			        "datagrid", //
			        model.getEntityCode(), //
			        title, //
			        height, //
			        gridWidth//
			);
			write(out, "token: '%s'", token);// 主表Grid通过该令牌获取子表Tabs对象，以便于选中主表记录后刷新Tabs中当前子表的Grid

			/*
			 * GRID数据查询相关属性
			 */
			write(out, ",url: '%s'", url);
			write(out, ",queryParams: {}");

			/*
			 * 行操作相关的属性：如添加行、编辑行
			 */
			boolean isSingleRowEdit = model.isSingleRowEdit();
			if (isSingleRowEdit) {
				write(out, ",onEdit: jCocit.entity.doEditGrid");
				if (StringUtil.hasContent(model.getDataAddUrl()))
					write(out, ",addUrl: '%s'", model.getDataAddUrl());
				if (StringUtil.hasContent(model.getDataEditUrl()))
					write(out, ",editUrl: '%s'", model.getDataEditUrl());
			}
			// “插入行”时候的默认值
			StringBuffer sb = new StringBuffer();
			Map defaultValues = model.getDefaultValuesToAddRow();
			if (defaultValues != null && defaultValues.size() > 0) {
				sb.append("{");
				Iterator<String> fields = defaultValues.keySet().iterator();
				int count = 0;
				while (fields.hasNext()) {
					if (count > 0) {
						sb.append(", ");
					}
					String field = fields.next();
					Object value = defaultValues.get(field);
					UIField uiField = (UIField) model.getColumn(field);
					Option option = makeFieldOption(uiField, value);
					if (option != null) {
						sb.append(field + ": {value: '" + option.getValue().replace("\"", "").replace("'", "") + "', text: '" + option.getText().replace("\"", "").replace("'", "") + "'}");
					} else {
						sb.append(field + ": '" + value + "'");
					}
					count++;
				}
				sb.append("}");
				write(out, ",defaultValues: %s", sb);
			}

			/*
			 * 是否自动调整列宽？如果有“行操作、行编辑、行添加”等功能；则需自动调整。
			 */
			if (rowActions != null || isSingleRowEdit)
				write(out, ",fitColumns: true");
			else
				write(out, ",fitColumns: true");

			/*
			 * GRID分页、排序属性
			 */
			write(out, ",sortField: '%s'", sortField);
			write(out, ",sortOrder: '%s'", sortOrder);
			write(out, ",pagination: %s", pagination);
			write(out, ",pageOptions : %s", StringUtil.toString(pageOptions));
			write(out, ",pageSize: %s", pageSize);
			write(out, ",pageButtons:[]");

			// write(out, ",checkedRows : [{id:68}, {id:177}]");

			/*
			 * GRID行选中相关属性
			 */
			if (noSelect)
				write(out, ",noSelect: %s", noSelect);
			write(out, ",singleSelect: %s", singleSelect);//
			write(out, ",selectOnCheck:  %s", (boolean) model.get("selectOnCheck", true));//
			write(out, ",checkOnSelect:  %s", (boolean) model.get("checkOnSelect", true));//
			write(out, ",idField: 'id'");// 用来保留GRID状态
			write(out, ",WYSIWYG: true");
			write(out, ",rownumbers: %s", (boolean) model.get("rownumbers", true));

			write(out, ", entityCode: '%s'", model.getEntityCode());

			/*
			 * GRID与其他UI组件之间的交互属性
			 */
			write(out, ", resultUI: %s", StringUtil.toJSArray(model.getResultUI()));
			write(out, ", paramUI: %s", StringUtil.toJSArray(model.getParamUI()));
			write(out, ", fkFields: %s", StringUtil.toJSArray(model.getFkFields()));
			write(out, ", fkTargetFields: %s", StringUtil.toJSArray(model.getFkTargetFields()));

			/*
			 * GRID工具栏
			 */
			String toolbarID = model.get("toolbarID", "");
			if (StringUtil.hasContent(toolbarID)) {
				write(out, ",toolbar: '#%s'", toolbarID);
			}

			/*
			 * GRID行样式
			 */
			if (rowStyles != null) {
				write(out, ",rowStyler: rowStyler_%s", token);
			}

			/*
			 * GRID事件：回调函数
			 */
			// write(out, ",onClickRow: jCocit.entity.doClickGridRow");
			String onSelect = model.get("onSelect");
			if (onSelect == null) {
				onSelect = "jCocit.entity.doSelectGrid";
			}
			write(out, ",onSelect: %s", onSelect);
			String onEvent = model.get("onUnselect");
			if (onEvent != null) {
				write(out, ",onUnselect: %s", onEvent);
			}
			onEvent = model.get("onUnselectAll");
			if (onEvent != null) {
				write(out, ",onUnselectAll: %s", onEvent);
			}
			onEvent = model.get("onCheck");
			if (onEvent != null) {
				write(out, ",onCheck: %s", onEvent);
			}
			onEvent = model.get("onUncheck");
			if (onEvent != null) {
				write(out, ",onUncheck: %s", onEvent);
			}
			onEvent = model.get("onCheckAll");
			if (onEvent != null) {
				write(out, ",onCheckAll: %s", onEvent);
			}
			onEvent = model.get("onUncheckAll");
			if (onEvent != null) {
				write(out, ",onUncheckAll: %s", onEvent);
			}

			String onBeforeLoad = model.get("onBeforeLoad");
			if (onBeforeLoad == null) {
				onBeforeLoad = "jCocit.entity.doBeforeLoadGrid";
			}
			write(out, ",onBeforeLoad: %s", onBeforeLoad);

			String onHeaderContextMenu = model.get("onHeaderContextMenu");
			if (onHeaderContextMenu == null) {
				onHeaderContextMenu = "jCocit.entity.doGridHeaderContextMenu";
			}
			write(out, ",onHeaderContextMenu: %s", onHeaderContextMenu);
			onEvent = model.get("onDblClickRow");
			if (onEvent != null) {
				write(out, ",onDblClickRow: %s", onEvent);
			}

			write(out, "\">");
			write(out, "<thead>");
			write(out, "<tr>");

			UIFieldModel column = model.getColumn("id");
			if (column != null) {
				checkbox = true;
			}
			write(out, "<th data-options=\"field: 'id', %s\">ID</th>", //
			        checkbox ? "checkbox: true" : "hidden: true"//
			);
			int colWidth, editMode, addMode;
			int runtimeColTotalWidth = 0;
			int count = 0;
			int colSize = columns.size() - 1;

			for (UIFieldModel col : columns) {
				colWidth = col.getWidth();
				UIField uiFld = (UIField) col;

				if ("id".equals(uiFld.getPropName())) {
					continue;
				}

				ICocFieldInfo fldService = uiFld.getFieldService();

				colWidth = new Double(colWidth * fixColRate).intValue() - 2;
				if (count == colSize) {
					colWidth = columnsWidth - runtimeColTotalWidth - 2;
				}
				runtimeColTotalWidth += colWidth;

				String styler = null;
				if (col.getCellStyles() != null) {
					styler = "cellStyler_" + token + "_" + col.getPropName();
				}

				write(out, "<th data-options=\"field: '%s', hidden: %s, showTips: %s, width: %s, sortable: true, align: '%s', halign: '%s'", //
				        col.getPropName(), //
				        col.isHidden(), //
				        col.isShowTips(), //
				        colWidth, //
				        col.getAlign(), //
				        col.getHalign() //
				);
				// if (rowEditable) {
				String editorType = "text";
				StringBuffer options = new StringBuffer();
				String viewName = uiFld.getViewName();

				if (col.getDicOptions().length > 0) {
					editorType = "combobox";
					options.append("data:[");
					int optCount = 0;
					for (Option opt : col.getDicOptions()) {
						if (optCount > 0) {
							options.append(",");
						}
						options.append(String.format("{value: '%s', text: '%s'}", opt.getValue(), opt.getText()));

						optCount++;
					}
					options.append("]");
				} else {
					switch (col.getFieldType()) {
						case FieldTypes.FIELD_TYPE_BYTE:
						case FieldTypes.FIELD_TYPE_SHORT:
						case FieldTypes.FIELD_TYPE_INTEGER:
						case FieldTypes.FIELD_TYPE_LONG:
						case FieldTypes.FIELD_TYPE_NUMBER:
						case FieldTypes.FIELD_TYPE_FLOAT:
						case FieldTypes.FIELD_TYPE_DOUBLE:
						case FieldTypes.FIELD_TYPE_DECIMAL:
							editorType = "numberbox";
							break;
						case FieldTypes.FIELD_TYPE_DATE:
							editorType = "combodate";
							if (ViewNames.FIELD_VIEW_COMBODATETIME.equals(uiFld.getViewName())) {
								editorType = "combodatetime";
							}
							break;
						default:
							if (fldService.isFkField()) {
								if (ViewNames.FIELD_VIEW_COMBOTREE.equals(viewName)) {
									editorType = "combotree";
									options.append(String.format("dataURL: '%s'", uiFld.getFkComboTreeUrl()));
								} else {
									editorType = "combobox";
									options.append(String.format("url: '%s'", uiFld.getFkComboListUrl()));
								}
							}
					}
				}

				addMode = FieldModes.parseUiMode(col.getModeToAddGridRow());
				editMode = FieldModes.parseUiMode(col.getModeToEditGridRow());
				write(out, ", editor: {type: '%s', insertable: %s, updatable: %s, options: {height: 26, %s}}", //
				        editorType, //
				        addMode == FieldModes.E || addMode == FieldModes.M, //
				        editMode == FieldModes.E || editMode == FieldModes.M, //
				        options//
				);
				// }
				if (styler != null) {
					write(out, ", styler: %s", styler);
				}

				write(out, "\">%s</th>", col.getTitle());

				count++;
			}
			if ((rowActions != null && rowActions.getData().getSize() > 0) || isSingleRowEdit) {
				write(out, "<th data-options=\"field: '_row_buttons_', rowbuttons: true, width: %s, align: 'center'", rowActionsWidth);
				write(out, ", editor: {type: 'buttons'}");
				write(out, "\">操作</th>");
			}
			write(out, "</tr>");
			write(out, "</thead>");
			write(out, "</table>");

		}
	}

	static Option makeFieldOption(UIField uiField, Object fieldValue) {
		ICocFieldInfo fldService = uiField.getFieldService();
		if (fldService == null) {
			return null;
		}

		boolean isDic = fldService.getDicOptions().length() > 0;
		boolean isObject = fldService.isFkField();//

		String strFieldValue = "";
		String strFieldText = "";
		if (isDic) {
			strFieldValue = StringUtil.escapeHtml(fieldValue == null ? "" : fieldValue.toString());
			strFieldText = StringUtil.escapeHtml(uiField.format(fieldValue));
		} else if (isObject) {
			strFieldValue = ObjectUtil.getStringValue(fieldValue, fldService.getFkTargetFieldCode());
			strFieldText = StringUtil.escapeHtml(uiField.format(fieldValue));
		} else {
			return null;
		}

		return Option.make(strFieldText, strFieldValue);
	}

	public static class UIGridDataView extends BaseModelView<UIGridData> {
		public String getName() {
			return ViewNames.VIEW_GRIDDATA;
		}

		public void render(Writer out, UIGridData model) throws Exception {
			UIViews views = Cocit.me().getViews();

			UIGrid gridModel = model.getModel();
			List<UIFieldModel> columns = gridModel.getColumns();
			List allRows = (List) model.getData();
			UIActions rowActions = model.getModel().getRowActions();

			write(out, "{\"total\":%s,\"rows\":[", model.getTotal());

			StringBuffer sb = renderRows(allRows, views, columns, rowActions);

			write(out, sb.toString());

			write(out, "]}");
		}

		private StringBuffer renderRows(List allRows, UIViews views, List<UIFieldModel> columns, UIActions rowActions) throws Exception {
			StringBuffer sb = new StringBuffer();

			if (allRows == null) {
				return sb;
			}

			UICellView view;
			String field, strFieldValue, strFieldText, viewName;
			Object fieldValue;
			boolean noFirstRow = false, supportHtml = false, isObject = false, isDic = false;

			UICellView rowActionsView = null;
			if (rowActions != null) {
				rowActionsView = views.getCellView(rowActions.getViewName());
				if (rowActionsView == null) {
					rowActionsView = views.getCellView(Cocit.me().getConfig().getViewConfig().getCellViewForRowActions());
				}
			}

			sb = new StringBuffer();

			for (Object row : allRows) {

				if (noFirstRow) {
					sb.append(",");
				} else {
					noFirstRow = true;
				}

				/*
				 * Render Row
				 */
				sb.append("{");
				Object id = ObjectUtil.getValue(row, "id");
				if (id == null || id.toString().trim().length() == 0) {
					id = "hash_" + Integer.toHexString(row.hashCode());
				}
				sb.append(String.format("\"id\":%s", JsonUtil.toJson(id)));
				for (UIFieldModel col : columns) {
					UIField uiFld = (UIField) col;
					ICocFieldInfo fldService = uiFld.getFieldService();

					supportHtml = col.isSupportHtml();
					field = col.getPropName();
					fieldValue = ObjectUtil.getValue(row, field);
					view = null;

					// 通过VIEW生成单元格内容
					viewName = col.getViewName();
					if (StringUtil.isBlank(viewName) && StringUtil.hasContent(col.getLinkUrl())) {
						if ("dialog".equals(col.getLinkTarget()))
							viewName = ViewNames.CELL_VIEW_LINK_DIALOG;
						else
							viewName = ViewNames.CELL_VIEW_LINK;
					}
					if (StringUtil.hasContent(viewName)) {
						view = views.getCellView(viewName);
					}
					if (view != null) {
						StringWriter cellOut = new StringWriter();
						view.render(cellOut, col, row, col.getPropName(), fieldValue);
						strFieldText = cellOut.toString();
						supportHtml = true;
					} else {
						strFieldText = null;
					}

					if (fldService != null) {
						isDic = fldService.getDicOptions().length() > 0;
						isObject = fldService.isFkField();//
					} else {
						isDic = false;
						isObject = false;
					}
					if (!supportHtml && isDic) {
						strFieldValue = StringUtil.escapeHtml(fieldValue == null ? "" : fieldValue.toString());
						if (strFieldText == null) {
							strFieldText = StringUtil.escapeHtml(col.format(fieldValue));
						}
						sb.append(String.format(",\"%s\": {\"value\": %s, \"text\": %s}", field, JsonUtil.toJson(strFieldValue), JsonUtil.toJson(strFieldText)));
					} else if (!supportHtml && isObject) {
						strFieldValue = ObjectUtil.getStringValue(fieldValue, fldService.getFkTargetFieldCode());
						if (strFieldText == null) {
							strFieldText = StringUtil.escapeHtml(col.format(fieldValue));
						}
						sb.append(String.format(",\"%s\": {\"value\": %s, \"text\": %s}", field, JsonUtil.toJson(strFieldValue), JsonUtil.toJson(strFieldText)));
					} else {
						if (strFieldText == null) {
							strFieldText = col.format(fieldValue);
						}
						if (!supportHtml) {
							strFieldText = StringUtil.escapeHtml(strFieldText);
						}
						sb.append(String.format(",\"%s\": %s", field, JsonUtil.toJson(strFieldText)));
					}

				}

				// Render Row Actions
				if (rowActions != null) {
					StringWriter actionsOut = new StringWriter();
					rowActionsView.render(actionsOut, null, row, null, rowActions);
					sb.append(String.format(",\"%s\":%s", "_row_buttons_", JsonUtil.toJson(actionsOut.toString())));
				}

				sb.append('}');

			}
			return sb;
		}
	}
}
