package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.StatusCodes;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.control.UITree;
import com.jsoft.cocit.ui.model.datamodel.UITreeData;
import com.jsoft.cocit.ui.view.BaseModelView;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

public abstract class UITreeViews {

	public static class UITreeView extends BaseModelView<UITree> {
		public String getName() {
			return ViewNames.VIEW_TREE;
		}

		public void render(Writer out, UITree model) throws Exception {
			String token = Cocit.me().getHttpContext().getClientUIToken();

			if (model.getData() != null) {
				write(out, "<script type=\"text/javascript\">var treedata_%s=", token);

				StringBuffer dataSB = new StringBuffer();
				new UITreeDataView().toJson(dataSB, model.getData().getChildren());
				write(out, dataSB.toString());

				LogUtil.trace("ModelRender.render: treeData=%s", dataSB);

				write(out, "</script>");
			}

			HttpContext ctx = Cocit.me().getHttpContext();
			Integer width = model.get("width", ctx.getClientUIWidth());
			Integer height = model.get("height", ctx.getClientUIHeight());

			Map<String, Object> props = model.getContext();
			String attrName = model.getAttributes().getProperty("name");
			
			// Tree容器：DIV
			write(out, "<div id=\"filterTree_%s\" style=\"height: %spx; width: %spx; overflow: hidden;\" class=\"tree_container\">", token, height - 2, width - 2);

			// Tree：id = "filtertree_" + token
			/*
			 * 生成树属性
			 */
			StringBuffer treeOptions = new StringBuffer();

			append(treeOptions, "token: '%s'", token);// 导航树Tree通过该令牌查找DataGrid对象
			append(treeOptions, ", resultUI: %s", StringUtil.toJSArray(model.getResultUI()));

			/*
			 * 树属性
			 */
			Iterator keys = props.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				Object value = props.get(key);
				if (ClassUtil.isPrimitive(value)) {
					append(treeOptions, ",%s: %s", key, value == null ? "" : value.toString());
				} else {
					append(treeOptions, ",%s: '%s'", key, value == null ? "" : value.toString());
				}
			}

			/*
			 * 数据来源
			 */
			if (model.getData() == null) {
				append(treeOptions, ",dataURL: '%s'", model.getDataLoadUrl() + "?_uiToken=" + token);
			} else {
				append(treeOptions, ",data: treedata_%s", token);
			}

			/*
			 * 选中和复选事件
			 */
			String onSelect = model.get("onSelect", "");
			if (!StringUtil.isBlank(onSelect))
				append(treeOptions, ",onSelect: %s", onSelect);
			String onCheck = model.get("onCheck", "");
			if (!StringUtil.isBlank(onCheck))
				append(treeOptions, ",onCheck: %s", onCheck);

			LogUtil.trace("ModelRender.render: treeOptions=%s", treeOptions);

			/*
			 * 输出树型UI
			 */
			write(out, "<ul id=\"%s\" name=\"%s\" class=\"jCocit-ui jCocit-tree\" style=\"position: absolute; left: 0; top: 0; width: %spx; \" data-options=\"%s\"></ul>", //
			        model.getId(),//
			        attrName == null ? "" : attrName,//
			        width,//
			        treeOptions//
			);

			write(out, "</div>");
		}
	}

	public static class UITreeDataView extends BaseModelView<UITreeData> {
		public String getName() {
			return ViewNames.VIEW_TREEDATA;
		}

		public void render(Writer out, UITreeData model) throws Exception {

			Tree tree = (Tree) model.getData();
			if (tree == null) {
				return;
			}
			List<Node> nodes = tree.getChildren();
			if (!ObjectUtil.isNil(nodes)) {
				StringBuffer sb = new StringBuffer();

				toJson(sb, nodes);

				write(out, sb.toString());
			} else {
				write(out, "[]");
			}

		}

		protected void toJson(StringBuffer sb, List<Node> nodes) throws IOException {

			sb.append("[");

			boolean noFirst = false;
			for (Node node : nodes) {
				String statusCode = node.getStatusCode();
				if (("" + StatusCodes.STATUS_CODE_DISABLED).equals(statusCode)) {
					continue;
				}

				if (noFirst) {
					sb.append(",");
				} else {
					noFirst = true;
				}

				append(sb, "{\"id\" : %s", JsonUtil.toJson(node.getId()));

				toJson(sb, "text", node.getName());

				Properties props = node.getProperties();
				Enumeration keys = props.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					Object value = props.get(key);
					toJson(sb, key, value);
				}

				List<Node> children = node.getChildren();
				if (!ObjectUtil.isNil(children)) {
					append(sb, ",\"children\" :");

					toJson(sb, children);
				} else if (!StringUtil.isBlank(node.getChildrenURL())) {
					append(sb, ",\"children\" : %s", JsonUtil.toJson(node.getChildrenURL()));

				}

				append(sb, "}");
			}

			sb.append("]");
		}
	}
}
