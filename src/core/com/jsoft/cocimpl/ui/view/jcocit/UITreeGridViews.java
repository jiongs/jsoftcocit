package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lilystudio.util.StringWriter;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.ui.UIViews;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.control.UIActions;
import com.jsoft.cocit.ui.model.control.UIGrid;
import com.jsoft.cocit.ui.model.datamodel.UIGridData;
import com.jsoft.cocit.ui.view.BaseModelView;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.ui.view.UICellView;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

public abstract class UITreeGridViews {

	public static class UIGridView extends BaseModelView<UIGrid> {
		public String getName() {
			return ViewNames.VIEW_TREEGRID;
		}

		public void render(Writer out, UIGrid model) throws Exception {
			String title = "";// model.getName()
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
			int columnsWidth = gridWidth - 78 - rowActionsWidth;
			double fixColRate = 1.0;
			if (columnsWidth > colTotalWidth) {
				fixColRate = new Double(columnsWidth) / new Double(colTotalWidth);
			}

			int pageSize = model.getPageSize();
			boolean singleSelect = (boolean) model.get("singleSelect", false);
			// boolean checkbox = (boolean) model.get("checkbox", true);
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
			String treeField = model.getTreeField();
			boolean pagination = (boolean) model.get("pagination", true);
			/*
			 * TODO: 树形GRID 暂时不支持分页
			 */
			// if (StringUtil.hasContent(treeField)) {
			// pagination = false;
			// }
			String url = String.format("%s?query.treeField=%s&_uiToken=%s", model.getDataLoadUrl(), treeField, token);

			write(out, "<table id=\"%s\" class=\"jCocit-ui jCocit-%s entity-%s\" title=\"%s\" style=\"height: %spx; width:%spx;\" data-options=\"",//
			        model.getId(), //
			        (StringUtil.hasContent(treeField) ? "treegrid" : "datagrid"),//
			        model.getEntityCode(),//
			        title,//
			        height,//
			        gridWidth//
			);
			write(out, "token: '%s'", token);// 主表Grid通过该令牌获取子表Tabs对象，以便于选中主表记录后刷新Tabs中当前子表的Grid
			write(out, ",url: '%s'", url);
			// write(out, ",rownumbers: %s", (boolean) model.get("rownumbers", true));
			write(out, ",sortField: '%s'", sortField);
			write(out, ",sortOrder: '%s'", sortOrder);
			write(out, ",fitColumns: true");
			write(out, ",WYSIWYG: true");
			write(out, ",noSelect: %s", (boolean) model.get("noSelect", false));
			write(out, ",pagination: %s", pagination);
			write(out, ",singleSelect: %s", singleSelect);//
			write(out, ",selectOnCheck:  %s", (boolean) model.get("selectOnCheck", true));//
			write(out, ",checkOnSelect:  %s", (boolean) model.get("checkOnSelect", true));//
			write(out, ",onSelect: jCocit.entity.doSelectGrid");
			// write(out, ",onCheck: jCocit.entity.doSelectGrid");
			// write(out, ",onUncheck: jCocit.entity.doSelectGrid");
			// write(out, ",onCheckAll: jCocit.entity.doSelectGrid");
			// write(out, ",onUncheckAll: jCocit.entity.doSelectGrid");
			write(out, ",onBeforeLoad: jCocit.entity.doBeforeLoadGrid");
			write(out, ",onHeaderContextMenu: jCocit.entity.doGridHeaderContextMenu");
			write(out, ",pageOptions : [10, 20, 50, 100]");
			// write(out, ",checkedRows : [{id:68}, {id:177}]");
			write(out, ",pageSize: %s", pageSize);
			write(out, ",idField: 'id'");// 用来保留GRID状态
			if (StringUtil.hasContent(treeField)) {
				write(out, ",treeField: 'name'");
			}

			write(out, ", entityCode: '%s'", model.getEntityCode());
			write(out, ", resultUI: %s", StringUtil.toJSArray(model.getResultUI()));
			write(out, ", paramUI: %s", StringUtil.toJSArray(model.getParamUI()));
			write(out, ", refFields: %s", StringUtil.toJSArray(model.getFkFields()));
			write(out, ", refTargetFields: %s", StringUtil.toJSArray(model.getFkTargetFields()));

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

			write(out, "<th data-options=\"field: 'id', checkbox: false, hidden: true\">ID</th>");
			int colWidth;
			int runtimeColTotalWidth = 0;
			int count = 0;
			for (UIFieldModel col : columns) {
				colWidth = col.getWidth();
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

				write(out, "<th data-options=\"field: '%s', hidden: %s, showTips: %s, width: %s, sortable: true, align: '%s', halign: '%s' %s\">%s</th>", //
				        col.getPropName(), //
				        col.isHidden(),//
				        col.isShowTips(),//
				        colWidth, //
				        col.getAlign(), //
				        col.getHalign(), //
				        (styler == null ? "" : (", styler: " + styler)),//
				        col.getTitle());

				count++;
			}
			if (rowActions != null) {
				write(out, "<th data-options=\"field: '_row_buttons_', rowbuttons: true, width: %s, align: 'center'\">操作</th>", rowActionsWidth);
			}
			write(out, "</tr>");
			write(out, "</thead>");
			write(out, "</table>");

		}
	}

	public static class UIGridDataView extends BaseModelView<UIGridData> {
		public String getName() {
			return ViewNames.VIEW_TREEGRIDDATA;
		}

		public void render(Writer out, UIGridData model) throws Exception {
			UIViews views = Cocit.me().getViews();

			UIGrid gridModel = model.getModel();
			List<UIFieldModel> columns = gridModel.getColumns();
			Object data = model.getData();
			String treeField = model.getModel().getTreeField();
			UIActions rowActions = model.getModel().getRowActions();
			// String token = Cocit.me().getHttpContext().getClientUIToken();
			// String treeChildrenUrl = String.format("%s?query.treeField=%s&_uiToken=%s", gridModel.getDataLoadUrl(), treeField, token);

			write(out, "{\"total\":%s,\"rows\":[", model.getTotal());

			Tree tree;
			if (data instanceof Tree) {
				tree = (Tree) data;
			} else {
				tree = Tree.make();
				this.makeNodes(tree, (List) data, treeField, 0);
			}
			StringBuffer sb = renderRows(tree.getChildren(), views, columns, rowActions);

			// System.out.println(sb);

			write(out, sb.toString());

			write(out, "]}");
		}

		private void makeNodes(Tree tree, List rows, String treeField, int depth) {
			if (depth > 5) {
				return;
			}

			for (Object obj : rows) {
				String key = ObjectUtil.getValue(obj, "code");
				Object parent = ObjectUtil.getValue(obj, treeField);
				String parentCode;
				if (parent == null) {
					parentCode = "";
				} else {
					parentCode = parent.toString();
				}
				tree.addNode(parentCode, key).setName(obj.toString()).setReferObj(obj);
			}

			this.queryParents(tree, treeField, depth);
		}

		private void queryParents(Tree tree, String treeField, int depth) {
			if (depth > 5) {
				return;
			}

			List empty = new ArrayList();
			Map<String, Node> nodes = tree.getAllMap();
			for (Node node : nodes.values()) {
				Node parentNode = node.getParent();
				if (parentNode != null) {
					Object obj = node.getReferObj();
					String parentCode = (String) ObjectUtil.getValue(obj, treeField);
					if (parentNode.getReferObj() == null && StringUtil.hasContent(parentCode)) {
						empty.add(obj);
					}
				}
			}

			if (empty.size() > 0) {
				List<String> parentCodes = new ArrayList();
				for (Object obj : empty) {
					String parentCode = ObjectUtil.getValue(obj, treeField);
					if (!parentCodes.contains(parentCode))
						parentCodes.add(parentCode);
				}
				List parentRows = Cocit.me().orm().query(empty.get(0).getClass(), Expr.in("code", parentCodes));
				this.makeNodes(tree, parentRows, treeField, ++depth);
			}
		}

		private StringBuffer renderRows(List<Node> rows, UIViews views, List<UIFieldModel> columns, UIActions rowActions) throws Exception {
			StringBuffer sb = new StringBuffer();

			if (rows == null) {
				return sb;
			}

			UICellView view;
			String field, strFieldValue;
			Object fieldValue;
			boolean noFirstRow = false;

			sb = new StringBuffer();

			for (Node node : rows) {

				Object row = node.getReferObj();

				if (noFirstRow) {
					sb.append(",");
				} else {
					noFirstRow = true;
				}

				/*
				 * Render Row
				 */
				sb.append("{");
				sb.append(String.format("\"id\":%s", JsonUtil.toJson(ObjectUtil.getValue(row, "id"))));

				// checked
				if (node.get("checked") != null) {
					sb.append(",\"checked\":true");
				}

				for (UIFieldModel col : columns) {
					field = col.getPropName();
					fieldValue = ObjectUtil.getValue(row, field);

					// 通过VIEW生成单元格内容
					view = views.getCellView(col.getViewName());
					if (view != null) {
						StringWriter cellOut = new StringWriter();
						view.render(cellOut, col, row, col.getPropName(), fieldValue);
						strFieldValue = cellOut.toString();
					} else {
						strFieldValue = StringUtil.escapeHtml(col.format(fieldValue));
					}

					sb.append(String.format(",\"%s\":%s", field, JsonUtil.toJson(strFieldValue)));
				}

				List<Node> children = node.getChildren();
				if (children != null && children.size() > 0) {
					StringBuffer sbChildren = renderRows(children, views, columns, rowActions);
					if (sbChildren.length() > 0) {
						sb.append(String.format(",\"children\": [%s]", sbChildren));
					}
				}

				// Render Row Actions
				if (rowActions != null) {
					StringWriter actionsOut = new StringWriter();
					view = views.getCellView(rowActions.getViewName());
					view.render(actionsOut, null, row, null, rowActions);
					sb.append(String.format(",\"%s\":%s", "_row_buttons_", JsonUtil.toJson(actionsOut.toString())));
				}

				sb.append('}');

			}

			return sb;
		}
	}
}
