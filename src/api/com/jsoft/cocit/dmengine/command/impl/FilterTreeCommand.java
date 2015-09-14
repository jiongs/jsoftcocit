package com.jsoft.cocit.dmengine.command.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.baseentity.ITreeEntity;
import com.jsoft.cocit.baseentity.ITreeObjectEntity;
import com.jsoft.cocit.baseentity.coc.ICocEntity;
import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.StatusCodes;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.command.WebCommandInterceptor;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.dmengine.info.ICuiEntityInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

public class FilterTreeCommand extends WebCommandInterceptor {

	@Override
	public String getName() {
		return CommandNames.COC_FILTERTREE;
	}

	@Override
	protected boolean execute(WebCommandContext cmdctx) {

		Boolean usedToSubModule = (Boolean) cmdctx.get("usedToSubModule");
		if (usedToSubModule == null) {
			usedToSubModule = false;
		}

		ISystemMenuInfo menuInfo = cmdctx.getSystemMenu();
		ICocEntityInfo entityInfo = cmdctx.getCocEntity();

		/*
		 * 获取需要引用的主界面
		 */
		String cuiCode = menuInfo.getUiView();
		if (StringUtil.isBlank(cuiCode)) {
			cuiCode = entityInfo.getUiView();
		}

		/*
		 * 计算UI属性
		 */
		List<String> filterFields = null;
		ICuiEntityInfo cui = entityInfo.getCuiEntity(cuiCode);
		if (cui != null) {
			filterFields = cui.getFilterFieldsList();
		}

		/*
		 * 没有找到过滤器字段
		 */
		if ((filterFields == null || filterFields.size() == 0)//
		        && ObjectUtil.isNil(entityInfo.getFieldsOfFilter(usedToSubModule))//
		) {
			return false;
		}

		/*
		 * 查询数据
		 */
		Tree data = getFilterData(entityInfo, filterFields, usedToSubModule);
		data.optimizeStatus();

		CndExpr menuExpr = menuInfo.getWhere();
		Map<String, Node> map = data.getAllMap();
		Iterator<Node> nodes = map.values().iterator();
		while (nodes.hasNext()) {
			Node node = nodes.next();
			String nodeID = node.getId();
			int idx = nodeID.indexOf(":");
			if (idx <= 0) {
				continue;
			}
			String fld = nodeID.substring(0, idx);
			String val = nodeID.substring(idx + 1);
			if (!ExprUtil.match(fld, val, menuExpr)) {
				node.setStatusCode("" + StatusCodes.STATUS_CODE_DISABLED);
			}
		}

		cmdctx.setResult(data);

		return false;
	}

	public Tree getFilterData(ICocEntityInfo entityInfo, List<String> filterFields, boolean usedToSubModule) {

		List<ICocFieldInfo> filterFieldServices = entityInfo.getFieldsOfFilter(usedToSubModule);

		Tree tree = Tree.make();

		if (filterFields == null || filterFields.size() == 0) {
			for (ICocFieldInfo fld : filterFieldServices) {
				Node node = tree.addNode(null, fld.getFieldName()).setName("按【" + fld.getName() + "】过滤");
				node.set("open", "true");
				node.set("type", "folder");

				boolean success = this.makeFieldNodes(tree, node, fld);
				if (!success) {
					tree.removeNode(node);
				}
			}
		} else {
			for (String fldName : filterFields) {
				ICocFieldInfo fld = entityInfo.getField(fldName);
				Node node = tree.addNode(null, fld.getFieldName()).setName("按【" + fld.getName() + "】过滤");
				node.set("open", "true");
				node.set("type", "folder");

				boolean success = this.makeFieldNodes(tree, node, fld);
				if (!success) {
					tree.removeNode(node);
				}
			}
		}

		// 如果导航树节点总数没有超过边框则全部展开
		// root.optimizeStatus();

		tree.sort();

		return tree;
	}

	private boolean makeFieldNodes(Tree tree, Node node, ICocFieldInfo field) {

		boolean ret = true;

		int nodeMaxLength = 1000;

		String rootNodeID = node == null ? "" : node.getId();
		String nodeIDPrefix = field.getFieldName();

		if (field.isFkField()) {

			/*
			 * 获取外键字段关联的目标模块
			 */
			ICocEntityInfo fkTargetModule = field.getFkTargetEntity();
			if (fkTargetModule == null) {
				return false;
			}

			/*
			 * 获取外键目标实体的分组字段
			 */
			ICocFieldInfo fkGroupField = null;
			for (ICocFieldInfo targetFkField : fkTargetModule.getFkFields()) {
				if (targetFkField.isFkTargetAsGroup()) {
					fkGroupField = targetFkField;
					break;
				}
			}

			// 创建外键节点的分组节点：即外键节点按什么字段进行分组？
			String groupNodeIDPrefix = "";
			String groupNodeIDPrefix2 = "";
			String groupField = null;
			if (fkGroupField != null) {
				groupField = fkGroupField.getFieldName();
				String fkGroupTargetField = fkGroupField.getFkTargetFieldCode();
				if (fkGroupField.isManyToOne()) {
					groupField = groupField + "." + fkGroupTargetField;
				}

				for (ICocFieldInfo fkfld : field.getEntity().getFkFields()) {
					if (fkfld.getFkTargetEntity().equals(fkGroupField.getFkTargetEntity())) {
						groupNodeIDPrefix = fkfld.getFieldName();
						String tf = fkfld.getFkTargetFieldCode();
						if (fkfld.isManyToOne()) {
							groupNodeIDPrefix2 = groupNodeIDPrefix + "." + tf;
						}
						break;
					}
				}

				ret = makeFieldNodes(tree, fkGroupField, null, rootNodeID, "", groupNodeIDPrefix);

				if (StringUtil.isBlank(groupNodeIDPrefix)) {
					for (Node n : tree.getAll()) {
						n.set("unselectable", true);
						// n.set("uncheckable", true);
						n.set("unvalueable", true);
					}
				}
			}

			// 创建外键节点
			ret = ret && makeFieldNodes(tree, field, groupField, rootNodeID, groupNodeIDPrefix2, nodeIDPrefix);

		} else {

			Option[] options = field.getDicOptionsArray();
			if (options == null || options.length == 0 || options.length > nodeMaxLength) {
				return false;
			}

			for (Option option : options) {
				tree.addNode(rootNodeID, nodeIDPrefix + ":" + option.getValue())//
				        .setName(option.getText());
			}

		}

		return ret;
	}

	private boolean makeFieldNodes(Tree tree, ICocFieldInfo field, String groupField, String rootNodeID, String groupNodeIDPrefix, String nodeIDPrefix) {
		int nodeMaxLength = 1000;

		/*
		 * 获取外键字段关联的目标模块
		 */
		ICocEntityInfo fkTargetModule = field.getFkTargetEntity();
		if (fkTargetModule == null) {
			return false;
		}

		IOrm orm = Cocit.me().orm();

		/*
		 * 查询外键数据
		 */
		Class fkTargetClass = fkTargetModule.getClassOfEntity();
		if (orm.count(fkTargetClass) > nodeMaxLength) {
			return false;
		}
		List fkTargetDatas = orm.query(fkTargetClass, makeSortExpr(fkTargetModule, "tree"));

		/*
		 * 外键关联
		 */
		String fkTargetField = field.getFkTargetFieldCode();
		if (field.isManyToOne() && StringUtil.hasContent(nodeIDPrefix)) {
			nodeIDPrefix = nodeIDPrefix + "." + fkTargetField;
		}
		boolean fkTargetKEY = Const.F_CODE.equals(fkTargetField);
		boolean fkTargetID = Const.F_ID.equals(fkTargetField);

		/*
		 * 外键关联到自己
		 */
		String fkSelfField = null;
		ICocFieldInfo selfTreeFieldService = fkTargetModule.getFieldOfTree();
		if (selfTreeFieldService != null) {
			fkSelfField = selfTreeFieldService.getFieldName();
		}

		/*
		 * 定义for循环中用到的临时变量
		 */
		String id;
		String nodeID;
		String parentNodeID;
		Object parentObj;
		ITreeEntity treeObj;
		ITreeObjectEntity tree2Obj;
		ITreeObjectEntity tree2ParentObj;
		Node childNode;
		Integer sn;

		/*
		 * 迭代数据，生成树节点
		 */
		for (Object record : fkTargetDatas) {

			if (record instanceof ITreeEntity) {

				/*
				 * 计算当前节点ID
				 */
				treeObj = (ITreeEntity) record;
				if (StringUtil.hasContent(nodeIDPrefix))
					nodeID = nodeIDPrefix + ":" + treeObj.getCode();
				else
					nodeID = treeObj.getCode();

				/*
				 * 计算父节点ID
				 */
				if (!StringUtil.isBlank(treeObj.getParentCode())) {
					if (StringUtil.hasContent(nodeIDPrefix))
						parentNodeID = nodeIDPrefix + ":" + treeObj.getParentCode();
					else {
						parentNodeID = treeObj.getParentCode();
					}
				} else {
					parentNodeID = this.makeGroupNodeID(record, groupNodeIDPrefix, groupField, rootNodeID);
				}

				/*
				 * 创建节点并添加到树中
				 */
				childNode = tree.addNode(parentNodeID, nodeID);
				childNode.setName(treeObj.getName());
				childNode.setSn(treeObj.getSn());

			} else if (record instanceof ITreeObjectEntity) {

				/*
				 * 计算当前节点ID
				 */
				tree2Obj = (ITreeObjectEntity) record;
				if (fkTargetKEY) {
					id = tree2Obj.getCode();
				} else if (fkTargetID) {
					id = tree2Obj.getId().toString();
				} else {
					id = ObjectUtil.getStringValue(tree2Obj, fkTargetField);
				}
				if (StringUtil.hasContent(nodeIDPrefix))
					nodeID = nodeIDPrefix + ":" + id;
				else
					nodeID = id;

				/*
				 * 计算父节点ID
				 */
				tree2ParentObj = (ITreeObjectEntity) tree2Obj.getParent();
				if (tree2ParentObj != null) {
					if (fkTargetKEY) {
						id = tree2ParentObj.getCode();
					} else if (fkTargetID) {
						id = tree2ParentObj.getId().toString();
					} else {
						id = ObjectUtil.getStringValue(tree2ParentObj, fkTargetField);
					}

					if (StringUtil.hasContent(nodeIDPrefix))
						parentNodeID = nodeIDPrefix + ":" + id;
					else
						parentNodeID = id;
				} else {
					parentNodeID = this.makeGroupNodeID(record, groupNodeIDPrefix, groupField, rootNodeID);
				}

				/*
				 * 创建节点并添加到树中
				 */
				childNode = tree.addNode(parentNodeID, nodeID);
				childNode.setName(tree2Obj.getName());
				childNode.setSn(tree2Obj.getSn());

			} else {

				/*
				 * 计算节点ID
				 */
				id = ObjectUtil.getStringValue(record, fkTargetField);
				if (StringUtil.hasContent(nodeIDPrefix))
					nodeID = nodeIDPrefix + ":" + id;
				else
					nodeID = id;

				/*
				 * 计算上级节点ID
				 */
				if (!StringUtil.isBlank(fkSelfField)) {
					parentObj = ObjectUtil.getValue(record, fkSelfField);
					if (parentObj != null) {
						id = ObjectUtil.getStringValue(parentObj, fkTargetField);

						if (StringUtil.hasContent(nodeIDPrefix))
							parentNodeID = nodeIDPrefix + ":" + id;
						else
							parentNodeID = id;
					} else {
						parentNodeID = this.makeGroupNodeID(record, groupNodeIDPrefix, groupField, rootNodeID);
					}
				} else {
					parentNodeID = this.makeGroupNodeID(record, groupNodeIDPrefix, groupField, rootNodeID);
				}

				/*
				 * 创建节点并添加到树中
				 */
				childNode = tree.addNode(parentNodeID, nodeID);
				childNode.setName(record.toString());
				if (ClassUtil.hasField(fkTargetClass, Const.F_SN)) {
					sn = ObjectUtil.getValue(record, Const.F_SN);
					if (sn != null)
						childNode.setSn(sn);
				}
			}

		}

		return true;
	}

	private String makeGroupNodeID(Object record, String groupNodeIDPrefix, String groupField, String defaultValue) {

		if (StringUtil.hasContent(groupField)) {
			String group = ObjectUtil.getStringValue(record, groupField);
			if (StringUtil.hasContent(group)) {
				if (StringUtil.hasContent(groupNodeIDPrefix)) {
					return groupNodeIDPrefix + ":" + group;
				} else {
					return group;
				}
			} else {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}

	}

	private CndExpr makeSortExpr(ICocEntity table, String type) {
		CndExpr ret = null;

		String sortExpr = table.getSortExpr();
		String[] exprs = StringUtil.toArray(sortExpr, ";；");

		for (String expr : exprs) {
			String[] arr = StringUtil.toArray(expr, ":");
			if (arr.length > 1) {
				if (!type.equalsIgnoreCase(arr[0].trim()))
					continue;

				String treeExpr = arr[1];
				int idx = treeExpr.indexOf(" ");
				if (idx > -1) {
					String sortFld = treeExpr.substring(0, idx);
					String sortType = treeExpr.substring(idx + 1);
					if ("DESC".equalsIgnoreCase(sortType.trim()))
						ret = CndExpr.desc(sortFld.trim());
					else
						ret = CndExpr.asc(sortFld.trim());
				} else {
					ret = CndExpr.asc(treeExpr.trim());
				}
			}
		}

		return ret;
	}
}