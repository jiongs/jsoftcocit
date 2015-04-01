package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lilystudio.util.StringWriter;

import com.jsoft.cocimpl.ui.UIViews;
import com.jsoft.cocimpl.ui.view.BaseModelView;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.FieldTypes;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.log.Log;
import com.jsoft.cocit.log.Logs;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.control.UIActions;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.model.datamodel.UIGridData;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.SortUtil;
import com.jsoft.cocit.util.StringUtil;

public abstract class UIGridViews {
	private static final Log log = Logs.getLog(UIGridViews.class);

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
			int columnsWidth = gridWidth - 54 - rowActionsWidth;
			double fixColRate = 1.0;
			if (columnsWidth > colTotalWidth) {
				fixColRate = new Double(columnsWidth) / new Double(colTotalWidth);
			}

			int pageSize = model.getPageSize();
			pageSize = (height - 100) / 32 / 5 * 5;
			if (pageSize < 10) {
				pageSize = 10;
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
			String url = model.getDataLoadUrl();

			write(out, "<table id=\"%s\" class=\"jCocit-ui jCocit-%s entity-%s\" title=\"%s\" style=\"height: %spx; width:%spx;\" data-options=\"",//
			        model.getId(), //
			        "datagrid",//
			        model.getEntityKey(),//
			        title,//
			        height,//
			        gridWidth//
			);
			write(out, "token: '%s'", token);// 主表Grid通过该令牌获取子表Tabs对象，以便于选中主表记录后刷新Tabs中当前子表的Grid
			write(out, ",url: '%s'", url);
			boolean rowEditable = false;
			if (StringUtil.hasContent(model.getDataAddUrl())) {
				rowEditable = true;
				write(out, ",addUrl: '%s'", model.getDataAddUrl());
			}
			if (StringUtil.hasContent(model.getDataEditUrl())) {
				rowEditable = true;
				write(out, ",editUrl: '%s'", model.getDataEditUrl());
			}
			write(out, ",rownumbers: %s", (boolean) model.get("rownumbers", true));
			write(out, ",sortField: '%s'", sortField);
			write(out, ",sortOrder: '%s'", sortOrder);
			write(out, ",WYSIWYG: true");
			if (rowActions != null)
				write(out, ",fitColumns: true");
			else
				write(out, ",fitColumns: false");
			write(out, ",noSelect: %s", (boolean) model.get("noSelect", false));
			write(out, ",pagination: %s", pagination);
			write(out, ",singleSelect: %s", singleSelect);//
			write(out, ",selectOnCheck:  %s", (boolean) model.get("selectOnCheck", true));//
			write(out, ",checkOnSelect:  %s", (boolean) model.get("checkOnSelect", true));//
			// write(out, ",onClickRow: jCocit.entity.doClickGridRow");
			write(out, ",onSelect: jCocit.entity.doSelectGrid");
			// write(out, ",onUnselect: jCocit.entity.doUnselectGrid");
			// write(out, ",onUnselectAll: jCocit.entity.doUnselectGrid");
			// write(out, ",onCheck: jCocit.entity.doSelectGrid");
			// write(out, ",onUncheck: jCocit.entity.doSelectGrid");
			// write(out, ",onCheckAll: jCocit.entity.doSelectGrid");
			// write(out, ",onUncheckAll: jCocit.entity.doSelectGrid");
			write(out, ",onBeforeLoad: jCocit.entity.doBeforeLoadGrid");
			write(out, ",onHeaderContextMenu: jCocit.entity.doGridHeaderContextMenu");
			if (rowEditable) {
				write(out, ",onEdit: jCocit.entity.doEditGrid");
			}
			write(out, ",pageOptions : %s", StringUtil.toString(pageOptions));
			// write(out, ",checkedRows : [{id:68}, {id:177}]");
			write(out, ",pageSize: %s", pageSize);
			// write(out, ",pagination: false");
			write(out, ",idField: 'id'");// 用来保留GRID状态

			write(out, ", entityKey: '%s'", model.getEntityKey());
			write(out, ", resultUI: %s", StringUtil.toJSArray(model.getResultUI()));
			write(out, ", paramUI: %s", StringUtil.toJSArray(model.getParamUI()));
			write(out, ", fkFields: %s", StringUtil.toJSArray(model.getFkFields()));
			write(out, ", fkTargetFields: %s", StringUtil.toJSArray(model.getFkTargetFields()));

			String toolbarID = model.get("toolbarID", "");
			if (StringUtil.hasContent(toolbarID)) {
				write(out, ",toolbar: '#%s'", toolbarID);
			}

			if (rowStyles != null) {
				write(out, ",rowStyler: rowStyler_%s", token);
			}

			write(out, ",pageButtons:[]");
			// print(out, "{title: '系统设置', iconCls: 'icon-setting', token:'%s', onClick:jCocit.entity.doSetting}", token);

			write(out, "\">");
			write(out, "<thead>");
			write(out, "<tr>");

			write(out, "<th data-options=\"field: 'id', %s\">ID</th>", //
			        checkbox ? "checkbox: true" : "hidden: true"//
			);
			int colWidth;
			int runtimeColTotalWidth = 0;
			int count = 0;
			for (UIFieldModel col : columns) {
				colWidth = col.getWidth();
				UIField uiFld = (UIField) col;
				CocFieldService fldService = uiFld.getFieldService();

				if (fixColRate > 1)
					colWidth = new Double(colWidth * fixColRate).intValue();
				if (count == columns.size() - 1) {
					colWidth = columnsWidth - runtimeColTotalWidth - count;
				}
				runtimeColTotalWidth += colWidth;

				String styler = null;
				if (col.getCellStyles() != null) {
					styler = "cellStyler_" + token + "_" + col.getPropName();
				}

				write(out, "<th data-options=\"field: '%s', hidden: %s, showTips: %s, width: %s, sortable: true, align: '%s', halign: '%s'", //
				        col.getPropName(), //
				        col.isHidden(),//
				        col.isShowTips(),//
				        colWidth, //
				        col.getAlign(), //
				        col.getHalign() //
				);
				if (rowEditable) {
					String editorType = "text";
					StringBuffer options = new StringBuffer();
					String viewName = uiFld.getViewName();
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
							} else if (fldService.isFkField()) {
								if (ViewNames.FIELD_VIEW_COMBOTREE.equals(viewName)) {
									editorType = "combotree";
									options.append(String.format("dataURL: '%s'", uiFld.getFkComboTreeUrl()));
								} else {
									editorType = "combobox";
									options.append(String.format("url: '%s'", uiFld.getFkComboListUrl()));
								}
							}
					}

					write(out, ", editor: {type: '%s', options: {height: 26, %s}}", editorType, options);
				}
				if (styler != null) {
					write(out, ", styler: %s", styler);
				}

				write(out, "\">%s</th>", col.getTitle());

				count++;
			}
			if (rowActions != null || rowEditable) {
				write(out, "<th data-options=\"field: '_row_buttons_', rowbuttons: true, width: %s, align: 'center'", rowActionsWidth);
				if (rowEditable) {
					write(out, ", editor: {type: 'buttons'}");
				}
				write(out, "\">操作</th>");
			}
			write(out, "</tr>");
			write(out, "</thead>");
			write(out, "</table>");

		}
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
			String field, strFieldValue;
			Object fieldValue;
			boolean noFirstRow = false;

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
					CocFieldService fldService = uiFld.getFieldService();

					log.debugf("UIGridViews: uiField=%s, fldService=%s", uiFld.getPropName(), fldService);

					field = col.getPropName();
					fieldValue = ObjectUtil.getValue(row, field);

					// 通过VIEW生成单元格内容
					view = views.getCellView(col.getViewName());
					if (view != null) {
						StringWriter cellOut = new StringWriter();
						view.render(cellOut, col, row, col.getPropName(), fieldValue);
						strFieldValue = cellOut.toString();

						sb.append(String.format(",\"%s\": %s", field, JsonUtil.toJson(strFieldValue)));
					} else {
						if (fldService != null && fldService.isFkField()) {
							String value = StringUtil.escapeHtml(fieldValue == null ? "" : fieldValue.toString());
							String text = StringUtil.escapeHtml(col.format(fieldValue));
							sb.append(String.format(",\"%s\": {\"value\": %s, \"text\": %s}", field, JsonUtil.toJson(value), JsonUtil.toJson(text)));
						} else {

							if (col.isSupportHtml()) {
								sb.append(String.format(",\"%s\": %s", field, JsonUtil.toJson(col.format(fieldValue))));
							} else {
								sb.append(String.format(",\"%s\": %s", field, JsonUtil.toJson(StringUtil.escapeHtml(col.format(fieldValue)))));
							}
						}
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
