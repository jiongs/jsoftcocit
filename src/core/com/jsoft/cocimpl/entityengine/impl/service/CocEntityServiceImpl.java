package com.jsoft.cocimpl.entityengine.impl.service;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.entity.INamedEntity;
import com.jsoft.cocit.entity.ITree2Entity;
import com.jsoft.cocit.entity.ITreeEntity;
import com.jsoft.cocit.entity.coc.ICocAction;
import com.jsoft.cocit.entity.coc.ICocEntity;
import com.jsoft.cocit.entity.coc.ICocField;
import com.jsoft.cocit.entity.coc.ICocGroup;
import com.jsoft.cocit.entity.cui.ICuiEntity;
import com.jsoft.cocit.entityengine.PatternAdapter;
import com.jsoft.cocit.entityengine.PatternAdapters;
import com.jsoft.cocit.entityengine.field.IExtField;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.entityengine.service.CocGroupService;
import com.jsoft.cocit.entityengine.service.CuiEntityService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.log.Log;
import com.jsoft.cocit.log.Logs;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.SortUtil;
import com.jsoft.cocit.util.StringUtil;
import com.jsoft.cocit.util.Tree;
import com.jsoft.cocit.util.Tree.Node;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class CocEntityServiceImpl extends NamedEntityServiceImpl<ICocEntity> implements CocEntityService {
	private static Log log = Logs.getLog(CocEntityServiceImpl.class);

	private List<CocGroupService> fieldGroups;
	private List<CocFieldService> fields;
	private List<CocFieldService> fkFieldsOfOtherModules;
	// 其他模块的外键字段：即其他模块通过哪个外键关联到此模块
	// <moduleKey,EntityFieldProxy>
	private Map<String, CocFieldService> fkFieldsOfSubModules;
	private Map<Serializable, CocFieldService> fieldsMap;
	private List<CocActionService> actions;
	private Map<Serializable, CocActionService> actionsMap;
	private List<CocFieldService> fieldsForFilter;
	private Properties extProps;
	private Class typeOfEntity;
	private Map<String, CuiEntityService> cuiEntityServices;

	CocEntityServiceImpl(ICocEntity e) {
		super(e);

		fieldGroups = new ArrayList();
		fields = new ArrayList();
		actions = new ArrayList();
		extProps = new Properties();
		fieldsMap = new HashMap();
		actionsMap = new HashMap();

		List<ICocField> entityFields = (List<ICocField>) orm().query(EntityTypes.CocField, Expr.eq(Const.F_COC_ENTITY_KEY, entityData.getKey()).addAsc(Const.F_SN));
		List<ICocAction> entityActions = (List<ICocAction>) orm().query(EntityTypes.CocAction, Expr.eq(Const.F_COC_ENTITY_KEY, entityData.getKey()).addAsc(Const.F_SN));

		initEntityFields(entityFields);
		initEntityActions(entityActions);
	}

	@Override
	public void release() {
		super.release();

		this.typeOfEntity = null;
		if (fieldGroups != null) {
			for (CocGroupService group : fieldGroups) {
				group.release();
			}
			fieldGroups.clear();
			fieldGroups = null;
		}
		if (fields != null) {
			for (CocFieldService f : fields) {
				f.release();
			}
			fields.clear();
			fields = null;
		}
		if (fkFieldsOfOtherModules != null) {
			for (CocFieldService f : fkFieldsOfOtherModules) {
				f.release();
			}
			fkFieldsOfOtherModules.clear();
			fkFieldsOfOtherModules = null;
		}
		if (fkFieldsOfSubModules != null) {
			for (CocFieldService f : fkFieldsOfSubModules.values()) {
				f.release();
			}
			fkFieldsOfSubModules.clear();
			fkFieldsOfSubModules = null;
		}
		if (fieldsMap != null) {
			fieldsMap.clear();
			fieldsMap = null;
		}
		if (actions != null) {
			for (CocActionService obj : actions) {
				obj.release();
			}
			actions.clear();
			actions = null;
		}
		if (actionsMap != null) {
			actionsMap.clear();
			actionsMap = null;
		}
		if (extProps != null) {
			extProps.clear();
			extProps = null;
		}
		if (fieldsForFilter != null) {
			fieldsForFilter.clear();
			fieldsForFilter = null;
		}
		if (cuiEntityServices != null) {
			for (CuiEntityService f : cuiEntityServices.values()) {
				f.release();
			}
			cuiEntityServices.clear();
			cuiEntityServices = null;
		}
	}

	private void initEntityFields(List<ICocField> fieldObjs) {
		if (fieldObjs == null)
			return;

		Map<String, CocGroupServiceImpl> groupsMap = new HashMap();

		for (ICocField fieldObj : fieldObjs) {
			String groupKey = fieldObj.getCocGroupKey();

			CocGroupServiceImpl group = (CocGroupServiceImpl) groupsMap.get(groupKey);
			if (group == null) {
				ICocGroup groupObj = (ICocGroup) orm().get(EntityTypes.CocGroup, Expr.eq(Const.F_COC_ENTITY_KEY, entityData.getKey()).and(Expr.eq(Const.F_KEY, groupKey)));
				group = new CocGroupServiceImpl(groupObj, this);
				groupsMap.put(groupKey, group);
				fieldGroups.add(group);
			}

			CocFieldService field = new CocFieldServiceImpl(fieldObj, this, group);
			this.fields.add(field);
			this.fieldsMap.put(field.getId(), field);
			this.fieldsMap.put(field.getFieldName(), field);

			group.addField(field);
		}

		SortUtil.sort(fieldGroups, Const.F_SN, true);
	}

	private void initEntityActions(List<ICocAction> actionObjs) {
		if (actionObjs == null)
			return;

		for (ICocAction action : actionObjs) {
			CocActionService actionService = new CocActionServiceImpl(action, this);
			this.actions.add(actionService);
			this.actionsMap.put(actionService.getId(), actionService);
			this.actionsMap.put(actionService.getKey(), actionService);
		}
	}

	private void initFkFieldsOfSubModules() {
		if (fkFieldsOfOtherModules == null) {
			fkFieldsOfOtherModules = new ArrayList();
			fkFieldsOfSubModules = new HashMap();

			List<ICocField> list = (List<ICocField>) orm().query(EntityTypes.CocField, Expr.fieldRexpr(Const.F_PROP_NAME + "|" + Const.F_COC_ENTITY_KEY).and(CndExpr.eq(Const.F_FK_TARGET_ENTITY, this.getKey())));
			for (ICocField otherFK : list) {
				CocEntityService otherModule = esf().getEntity(otherFK.getCocEntityKey());
				if (otherModule == null) {
					continue;
				}
				CocFieldService otherModuleFkField = otherModule.getField(otherFK.getFieldName());
				fkFieldsOfOtherModules.add(otherModuleFkField);

				if (otherModuleFkField.isFkTargetAsParent())
					fkFieldsOfSubModules.put(otherFK.getCocEntityKey(), otherModuleFkField);
			}
		}
	}

	private void initFieldsForFilter() {

		this.fieldsForFilter = new ArrayList();

		for (CocFieldService field : this.fields) {
			if (!field.isAsFilterNode() || field.isDisabled()) {
				continue;
			}

			if (field.isFkField() // && !field.isChildOfRefModule())//
			        || field.isBoolean() //
			        || !StringUtil.isBlank(field.getDicOptions()))
				fieldsForFilter.add(field);

		}

	}

	public String getTableName() {
		return entityData.getTableName();
	}

	public String getSortExpr() {
		return entityData.getSortExpr();
	}

	public String getCatalogKey() {
		return entityData.getCatalogKey();
	}

	public String getCatalogName() {
		return entityData.getCatalogName();
	}

	public String getClassName() {
		return entityData.getClassName();
	}

	// public byte getUiType() {
	// return entity.getUiType();
	// }

	public String getUiView() {
		return entityData.getUiView();
	}

	public String getPathPrefix() {
		return entityData.getPathPrefix();
	}

	public String getCompileVersion() {
		return entityData.getCompileVersion();
	}

	public String getExtendsClassName() {
		return entityData.getExtendsClassName();
	}

	public List<CocGroupService> getGroups() {
		return fieldGroups;
	}

	public List<CocFieldService> getFields() {
		return fields;
	}

	public List<CocActionService> getActions(List<String> actionIDList) {
		List<CocActionService> ret = new ArrayList();
		if (actionIDList == null || actionIDList.size() == 0) {
			for (CocActionService action : actions) {
				if (action.isDisabled())
					continue;

				ret.add(action);
			}
		} else {
			for (String actionID : actionIDList) {
				CocActionService action = this.getAction(actionID);
				if (action == null) {
					throw new CocException("引用的操作(%s)不存在！", actionID);
				}
				if (action.isDisabled())
					continue;

				ret.add(action);
			}
		}
		return ret;
	}

	public String getUniqueFields() {
		return entityData.getUniqueFields();
	}

	public String getIndexFields() {
		return entityData.getIndexFields();
	}

	public List<CocFieldService> getFieldsOfFilter(boolean usedToSubModule) {
		if (fieldsForFilter == null || fieldsForFilter.size() == 0)
			initFieldsForFilter();

		List<CocFieldService> ret = new ArrayList();
		if (usedToSubModule) {

			for (CocFieldService field : this.fieldsForFilter) {

				if (!field.isFkTargetAsParent()) {
					ret.add(field);
				}

			}
			return ret;
		}

		return fieldsForFilter;
	}

	public List<CocFieldService> getFieldsOfDataAuth() {
		List<String> fields = StringUtil.toList(this.getDataAuthFields());

		List<CocFieldService> ret = new ArrayList();

		for (String field : fields) {
			CocFieldService f = this.getField(field);
			if (f != null) {
				ret.add(f);
			}
		}

		return ret;
	}

	public CocFieldService getFieldOfTree() {
		for (CocFieldService field : this.fields) {

			if (field.isDisabled()) {// !field.isAsFilterNode() ||
				continue;
			}

			if (field.isFkField() && field.getFkTargetEntityKey().equals(this.entityData.getKey())) {
				return field;
			}

		}

		return null;
	}

	@Override
	public CocFieldService getFieldOfGroup() {
		for (CocFieldService field : this.fields) {

			if (field.isFkTargetAsGroup() && !field.isDisabled()) {
				return field;
			}

		}

		return null;
	}

	public List<CocFieldService> getFieldsOfGrid(List<String> fieldNames) {
		List<CocFieldService> ret = new LinkedList();

		if (fieldNames == null || fieldNames.size() == 0) {
			List<CocFieldService> other = new LinkedList();
			for (CocFieldService fld : this.fields) {
				if (fld.isAsGridColumn()) {
					ret.add(fld);
				} else if (fld.getGridColumnSn() != null && fld.getGridColumnSn() > 0) {
					ret.add(fld);
				} else {
					other.add(fld);
				}
			}

			int fixColumns = ret.size();
			if (fixColumns <= 0) {
				int maxColumns = 10;
				int len = other.size();
				len = Math.min(len, maxColumns);
				for (int i = 0; i < len; i++) {
					ret.add(other.get(i));
				}
			}

			SortUtil.sort(ret, Const.F_GRID_COLUMN_SN, true);
		} else {
			for (String fieldName : fieldNames) {
				CocFieldService field = this.getField(fieldName);
				if (field == null || field.isDisabled())
					continue;

				ret.add(field);
			}
		}

		return ret;
	}

	public Map<String, CocFieldService> getBizFieldsMapByPropName() {
		Map<String, CocFieldService> map = new HashMap();
		for (CocFieldService f : this.fields) {
			map.put(f.getFieldName(), f);
		}

		return map;
	}

	public Tree getFilterData(List<String> filterFields, boolean usedToSubModule) {

		List<CocFieldService> filterFieldServices = this.getFieldsOfFilter(usedToSubModule);

		Tree tree = Tree.make();

		if (filterFields == null || filterFields.size() == 0) {
			for (CocFieldService fld : filterFieldServices) {
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
				CocFieldService fld = this.getField(fldName);
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

	public Tree getRowsAuthData() {

		List<CocFieldService> filterFieldServices = this.getFieldsOfDataAuth();

		Tree tree = Tree.make();

		for (CocFieldService fld : filterFieldServices) {
			Node node = tree.addNode(null, fld.getFieldName()).setName("全部【" + fld.getName() + "】");
			node.set("open", "true");
			node.set("type", "folder");

			boolean success = this.makeFieldNodes(tree, node, fld);
			if (!success) {
				tree.removeNode(node);
			}
		}

		// 如果导航树节点总数没有超过边框则全部展开
		// root.optimizeStatus();

		tree.sort();

		return tree;
	}

	public Tree getTreeData(CndExpr expr) {

		Tree tree = Tree.make();

		CocFieldService field = this.getFieldOfTree();
		// if (field == null) {
		// field = this.getFieldOfGroup();
		// }
		// for (EntityFieldProxy field : this.getFieldsForDimTree()) {
		// Node node = tree.addNode(null, field.getFieldName()).setName(field.getName());
		// node.set("open", "true");

		// boolean success =
		Node node = null;
		if (field != null) {
			node = tree.addNode(null, field.getFieldName()).setName(field.getName());
		}
		this.makeSelfTreeNodes(tree, node, field, expr);
		// if (!success) {
		// tree.removeNode(node);
		// }
		// }

		// 如果导航树节点总数没有超过边框则全部展开
		tree.optimizeStatus();

		tree.sort();

		// System.out.println(tree.getLog());

		return tree;
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

	private boolean makeSelfTreeNodes(Tree tree, Node node, CocFieldService treeField, CndExpr expr) {
		boolean ret = true;
		String rootNodeID = node == null ? "" : node.getId();

		if (treeField == null) {

			/*
			 * 获取外键目标实体的分组字段
			 */
			CocFieldService fkGroupField = null;
			for (CocFieldService targetFkField : getFkFields()) {
				if (targetFkField.isFkTargetAsGroup()) {
					fkGroupField = targetFkField;
					break;
				}
			}

			// 创建外键节点的分组节点：即外键节点按什么字段进行分组？
			String groupField = null;
			if (fkGroupField != null) {
				groupField = fkGroupField.getFieldName();
				String fkGroupTargetField = fkGroupField.getFkTargetFieldKey();
				if (fkGroupField.isManyToOne()) {
					groupField = groupField + "." + fkGroupTargetField;
				}
				ret = makeSelfTreeNodes(tree, fkGroupField, null, rootNodeID, "", groupField, null);
				for (Node n : tree.getAll()) {
					n.set("unselectable", true);
				}
			}

			// 创建外键节点
			ret = ret && makeSelfTreeNodes(tree, treeField, groupField, rootNodeID, groupField, "", expr);
		} else if (treeField.isFkField()) {

			/*
			 * 获取外键字段关联的目标模块
			 */
			CocEntityService fkTargetModule = treeField.getFkTargetEntity();
			if (fkTargetModule == null) {
				return false;
			}

			/*
			 * 获取外键目标实体的分组字段
			 */
			CocFieldService fkGroupField = null;
			for (CocFieldService targetFkField : fkTargetModule.getFkFields()) {
				if (targetFkField.isFkTargetAsGroup()) {
					fkGroupField = targetFkField;
					break;
				}
			}

			// 创建外键节点的分组节点：即外键节点按什么字段进行分组？
			String groupField = null;
			if (fkGroupField != null) {
				groupField = fkGroupField.getFieldName();
				String fkGroupTargetField = fkGroupField.getFkTargetFieldKey();
				if (fkGroupField.isManyToOne()) {
					groupField = groupField + "." + fkGroupTargetField;
				}
				ret = makeSelfTreeNodes(tree, fkGroupField, null, rootNodeID, "", groupField, null);
				for (Node n : tree.getAll()) {
					n.set("unselectable", true);
				}
			}

			// 创建外键节点
			ret = ret && makeSelfTreeNodes(tree, treeField, groupField, rootNodeID, groupField, "", expr);

		} else {
			Option[] options = treeField.getDicOptionsArray();
			if (options == null || options.length == 0 || options.length > 200) {
				return false;
			}

			for (Option option : options) {
				tree.addNode(rootNodeID, option.getValue()).setName(option.getText());
			}
		}

		log.debug("makeSelfTreeNodes：" + tree.getLog());

		return ret;
	}

	private boolean makeSelfTreeNodes(Tree tree, CocFieldService treeField, String groupField, String rootNodeID, String groupNodeIDPrefix, String nodeIDPrefix, CndExpr expr) {
		Class type = this.getClassOfEntity();
		if (treeField != null) {
			// 获取该字段引用的外键系统
			CocEntityService fkModule = treeField.getFkTargetEntity();

			// 查询外键数据
			type = ee().getTypeOfEntity(fkModule);
			// if (orm().count(fkSystemType) > 50) {
			// return false;
			// }
			CndExpr sortExpr = makeSortExpr((ICocEntity) fkModule, "tree");

			if (expr == null) {
				expr = sortExpr;
			} else {
				if (sortExpr != null) {
					expr = expr.and(sortExpr);
				}
			}
		}

		List fkSystemRecords = orm().query(type, expr);

		// 数据自身树
		// String selfTreeProp = null;
		// ICocField selfTreeFld = ee().getFieldOfSelfTree(fkModule);
		// if (selfTreeFld != null) {
		// selfTreeProp = selfTreeFld.getFieldName();
		// }

		String parentID = "";
		for (Object record : fkSystemRecords) {
			if (record instanceof ITreeEntity) {
				ITreeEntity obj = (ITreeEntity) record;
				parentID = obj.getParentKey();
				if (StringUtil.isBlank(parentID)) {
					parentID = this.makeGroupNodeID(obj, groupNodeIDPrefix, groupField, rootNodeID);
				}
				String nodeID = obj.getKey();
				if (StringUtil.hasContent(nodeIDPrefix)) {
					nodeID = nodeIDPrefix + ":" + nodeID;
				}
				String nodeName = obj.getName();

				Node childNode = tree.addNode(parentID, nodeID);
				if (childNode == null) {
					log.warnf("tree.addNode result is null! {parentId : %s, nodeID : %s}", parentID, nodeID);
				} else {
					childNode.setName(nodeName);
					childNode.setSn(obj.getSn());
				}

			} else if (record instanceof ITree2Entity) {
				ITree2Entity obj = (ITree2Entity) record;
				ITree2Entity parent = (ITree2Entity) obj.getParent();
				if (parent != null) {
					parentID = parent.getKey();
				} else {
					parentID = "";
				}
				if (StringUtil.isBlank(parentID)) {
					parentID = this.makeGroupNodeID(obj, groupNodeIDPrefix, groupField, rootNodeID);
				}

				String nodeID = obj.getKey();
				if (StringUtil.hasContent(nodeIDPrefix)) {
					nodeID = nodeIDPrefix + ":" + nodeID;
				}
				String nodeName = obj.getName();

				Node childNode = tree.addNode(parentID, nodeID);
				childNode.setName(nodeName);
				childNode.setSn(obj.getSn());

			} else if (record instanceof INamedEntity) {
				INamedEntity obj = (INamedEntity) record;
				String nodeID = obj.getKey();
				if (StringUtil.hasContent(nodeIDPrefix)) {
					nodeID = nodeIDPrefix + ":" + nodeID;
				}
				String nodeName = obj.getName();
				parentID = this.makeGroupNodeID(obj, groupNodeIDPrefix, groupField, rootNodeID);

				Node childNode = tree.addNode(parentID, nodeID);
				childNode.setName(nodeName);
				childNode.setSn(obj.getSn());
			} else {
				// 计算上级节点ID
				// if (!StringUtil.isBlank(selfTreeProp)) {
				// Object parentObj = ObjectUtil.getValue(record, selfTreeProp);
				// if (parentObj != null)
				// parentID = "" + ObjectUtil.getValue(parentObj, Const.F_KEY);
				// else
				// parentID = "";
				// }

				// 计算节点ID
				String nodeID = null;
				if (ClassUtil.hasField(type, Const.F_KEY)) {
					nodeID = "" + ObjectUtil.getValue(record, Const.F_KEY);
				} else {
					nodeID = "" + record.hashCode();
				}
				if (StringUtil.hasContent(nodeIDPrefix)) {
					nodeID = nodeIDPrefix + ":" + nodeID;
				}

				// 计算节点名称
				String nodeName = record.toString();

				// 添加节点
				Node childNode = tree.addNode(parentID, nodeID).setName(nodeName);

				// 计算节点顺序
				if (ClassUtil.hasField(type, Const.F_SN)) {
					Integer seq = ObjectUtil.getValue(record, Const.F_SN);
					if (seq != null)
						childNode.setSn(seq);
				}
			}
		}
		return true;
	}

	private boolean makeFieldNodes(Tree tree, Node node, CocFieldService field) {

		boolean ret = true;

		int nodeMaxLength = 1000;

		String rootNodeID = node == null ? "" : node.getId();
		String nodeIDPrefix = field.getFieldName();

		if (field.isFkField()) {

			/*
			 * 获取外键字段关联的目标模块
			 */
			CocEntityService fkTargetModule = field.getFkTargetEntity();
			if (fkTargetModule == null) {
				return false;
			}

			/*
			 * 获取外键目标实体的分组字段
			 */
			CocFieldService fkGroupField = null;
			for (CocFieldService targetFkField : fkTargetModule.getFkFields()) {
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
				String fkGroupTargetField = fkGroupField.getFkTargetFieldKey();
				if (fkGroupField.isManyToOne()) {
					groupField = groupField + "." + fkGroupTargetField;
				}

				for (CocFieldService fkfld : field.getEntity().getFkFields()) {
					if (fkfld.getFkTargetEntity().equals(fkGroupField.getFkTargetEntity())) {
						groupNodeIDPrefix = fkfld.getFieldName();
						String tf = fkfld.getFkTargetFieldKey();
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
						n.set("uncheckable", true);
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

		log.debug("makeFieldNodes：" + tree.getLog());

		return ret;
	}

	private boolean makeFieldNodes(Tree tree, CocFieldService field, String groupField, String rootNodeID, String groupNodeIDPrefix, String nodeIDPrefix) {
		int nodeMaxLength = 1000;

		/*
		 * 获取外键字段关联的目标模块
		 */
		CocEntityService fkTargetModule = field.getFkTargetEntity();
		if (fkTargetModule == null) {
			return false;
		}

		/*
		 * 查询外键数据
		 */
		Class fkTargetClass = fkTargetModule.getClassOfEntity();
		if (orm().count(fkTargetClass) > nodeMaxLength) {
			return false;
		}
		List fkTargetDatas = orm().query(fkTargetClass, makeSortExpr(fkTargetModule, "tree"));

		/*
		 * 外键关联
		 */
		String fkTargetField = field.getFkTargetFieldKey();
		if (field.isManyToOne() && StringUtil.hasContent(nodeIDPrefix)) {
			nodeIDPrefix = nodeIDPrefix + "." + fkTargetField;
		}
		boolean fkTargetKEY = Const.F_KEY.equals(fkTargetField);
		boolean fkTargetID = Const.F_ID.equals(fkTargetField);

		/*
		 * 外键关联到自己
		 */
		String fkSelfField = null;
		CocFieldService selfTreeFieldService = fkTargetModule.getFieldOfTree();
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
		ITree2Entity tree2Obj;
		ITree2Entity tree2ParentObj;
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
					nodeID = nodeIDPrefix + ":" + treeObj.getKey();
				else
					nodeID = treeObj.getKey();

				/*
				 * 计算父节点ID
				 */
				if (!StringUtil.isBlank(treeObj.getParentKey())) {
					if (StringUtil.hasContent(nodeIDPrefix))
						parentNodeID = nodeIDPrefix + ":" + treeObj.getParentKey();
					else {
						parentNodeID = treeObj.getParentKey();
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

			} else if (record instanceof ITree2Entity) {

				/*
				 * 计算当前节点ID
				 */
				tree2Obj = (ITree2Entity) record;
				if (fkTargetKEY) {
					id = tree2Obj.getKey();
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
				tree2ParentObj = (ITree2Entity) tree2Obj.getParent();
				if (tree2ParentObj != null) {
					if (fkTargetKEY) {
						id = tree2ParentObj.getKey();
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

	private Map<String, Integer> getFieldsModeMap(String opMode, Object entityObject) {
		List<CocFieldService> fields = getFields();
		Map<String, Integer> fieldMode = new HashMap();
		for (CocFieldService f : fields) {
			CocFieldServiceImpl field = (CocFieldServiceImpl) f;

			fieldMode.put(field.getFieldName(), field.getMode(opMode, entityObject));
		}

		return fieldMode;
	}

	public void validateDataObject(String opMode, Object entityObject) throws CocException {
		if (entityObject instanceof List) {
			for (Object one : (List) entityObject) {
				validateOne(opMode, one);
			}
		} else {
			validateOne(opMode, entityObject);
		}
	}

	private void validateOne(String opMode, Object entityObject) throws CocException {
		Map<String, Integer> fieldsMode = this.getFieldsModeMap(opMode, entityObject);

		Iterator<String> keys = fieldsMode.keySet().iterator();
		List<String> requiredFields = new LinkedList();
		while (keys.hasNext()) {
			String field = keys.next();
			int mode = fieldsMode.get(field);
			if (FieldModes.isM(mode)) {
				Object v = ObjectUtil.getValue(entityObject, field);
				if (v == null) {
					requiredFields.add(field);
				} else if (v instanceof String) {
					if (StringUtil.isBlank((String) v))
						requiredFields.add(field);
				} else if (ObjectUtil.isEntity(v)) {
					CocFieldService fieldService = this.getField(field);
					String fkTargetField = fieldService.getFkTargetFieldKey();
					Object fkValue = ObjectUtil.getValue(v, fkTargetField);
					if (fkValue == null || fkValue.toString().trim().length() == 0)
						requiredFields.add(field);
				} else if (v instanceof IExtField) {
					if (StringUtil.isBlank(((IExtField) v).toString())) {
						requiredFields.add(field);
					}
				}
			}
		}

		StringBuffer error = new StringBuffer();
		if (requiredFields.size() > 0) {
			error.append("必填字段：");
			Map<String, CocFieldService> fields = getBizFieldsMapByPropName();
			for (String prop : requiredFields) {
				error.append(fields.get(prop).getName()).append(",");
			}
		}

		// Pattern 校验
		Cocit coc = Cocit.me();
		PatternAdapters patterns = coc.getPatternAdapters();
		List<CocFieldService> fields = this.getFields();
		Object fieldValue;
		PatternAdapter adapter;
		String pattern;
		String propName;
		IMessageConfig msgs = coc.getMessages();
		for (CocFieldService field : fields) {
			pattern = field.getPattern();
			propName = field.getFieldName();
			if (StringUtil.hasContent(pattern)) {
				fieldValue = ObjectUtil.getValue(entityObject, propName);
				adapter = patterns.getAdapter(pattern);
				if (adapter != null) {
					try {
						if (!adapter.validate(fieldValue)) {
							error.append(msgs.getMsg("10003." + adapter.getName(), fieldValue));
						}
					} catch (Throwable e) {
						error.append(String.format("\n%s：%s", field.getName(), ExceptionUtil.msg(e)));
					}
				}
			}
		}

		if (error.length() > 0)
			throw new CocException(error.toString().trim());
	}

	public void set(String key, String value) {
		this.extProps.put(key, value);
	}

	public Map<String, CocFieldService> getFieldsMap() {
		Map<String, CocFieldService> ret = new Hashtable();
		for (CocFieldService f : this.fields) {
			ret.put(f.getFieldName(), f);
		}

		return ret;
	}

	@Override
	public <T> List<T> parseDataFromExcel(File excel) {
		try {
			SystemExcel systemExcel = new SystemExcel(this, excel);
			return systemExcel.getRows();
		} catch (Throwable e) {
			throw new CocException(e.getMessage());
		}

	}

	public List<CocFieldService> getFkFieldsOfOtherEntities() {
		if (fkFieldsOfOtherModules == null) {
			this.initFkFieldsOfSubModules();
		}

		return fkFieldsOfOtherModules;
	}

	//
	// public List<EntityFieldProxy> getFieldsByReferenced() {
	// List<EntityFieldProxy> ret = new ArrayList();
	//
	// // 哪些字段引用了该模块？
	// List<? extends EntityFieldProxy> fields = getFieldsForFkToMe();
	// if (fields != null) {
	// for (EntityFieldProxy field : fields) {
	// if (!ret.contains(field)) {
	// ret.add(field);
	// }
	// }
	// }
	//
	// return ret;
	// }

	public List<CocFieldService> getFkFieldsOfSubEntities() {
		List<CocFieldService> ret = new LinkedList();

		List<CocFieldService> otherModuleFkFields = getFkFieldsOfOtherEntities();
		for (CocFieldService otherModuleFkField : otherModuleFkFields) {
			if (!otherModuleFkField.isFkTargetAsParent()) {
				continue;
			}
			CocEntityService otherModule = otherModuleFkField.getEntity();
			if (!otherModuleFkField.isDisabled() && !otherModule.isDisabled()) {
				ret.add(otherModuleFkField);
			}
		}

		return ret;
	}

	public List<CocEntityService> getSubEntities() {
		List<CocEntityService> ret = new LinkedList();

		List<CocFieldService> fields = getFkFieldsOfSubEntities();
		for (CocFieldService f : fields) {
			CocEntityService fkModule = f.getEntity();
			ret.add(fkModule);
		}

		return ret;
	}

	public CocActionService getAction(Serializable actionMode) {
		return this.actionsMap.get(actionMode);
	}

	public Class getClassOfEntity() {
		if (typeOfEntity == null) {
			typeOfEntity = ee().getTypeOfEntity(this);
		}

		return typeOfEntity;
	}

	public List<CocFieldService> getFieldsOfEnabled() {
		List<CocFieldService> list = this.getFields();

		List<CocFieldService> ret = new LinkedList();
		for (CocFieldService f : list) {
			if (!f.isDisabled())
				ret.add(f);
		}

		return ret;
	}

	public CocFieldService getField(String propName) {
		return this.fieldsMap.get(propName);
	}

	public CocFieldService getFkFieldOfSubEntity(String subModuleKey) {
		if (fkFieldsOfSubModules == null)
			this.initFkFieldsOfSubModules();

		return this.fkFieldsOfSubModules.get(subModuleKey);
	}

	public List<CocFieldService> getFkFields() {
		List<CocFieldService> list = this.getFields();

		List<CocFieldService> ret = new LinkedList();
		for (CocFieldService f : list) {
			if (!f.isDisabled() && f.isFkField())
				ret.add(f);
		}

		return ret;
	}

	public String getPackageName() {
		String clsName = this.getClassName();
		int idx = clsName.lastIndexOf(".");
		if (idx > -1) {
			return clsName.substring(0, idx);
		}

		return "";
	}

	public String getSimpleClassName() {
		String clsName = this.getClassName();
		int idx = clsName.lastIndexOf(".");
		if (idx > -1) {
			return clsName.substring(idx + 1);
		}

		return clsName;
	}

	@Override
	public List<CocFieldService> getFkFieldsOfRedundant(String propName) {
		List<CocFieldService> ret = new ArrayList();

		for (CocFieldService fk : this.getFkFields()) {
			if (propName.equals(fk.getFkDependFieldKey())) {
				ret.add(fk);
			}
		}

		return ret;
	}

	@Override
	public CocFieldService getFkNameField(String propName) {
		for (CocFieldService fk : this.getFkFields()) {
			if (propName.equals(fk.getFkDependFieldKey())) {
				if (Const.F_NAME.equals(fk.getFkTargetFieldKey()))
					return fk;
			}
		}

		return null;
	}

	@Override
	public SystemMenuService getSystemMenu() {
		SystemService systemService = Cocit.me().getHttpContext().getLoginSystem();

		return systemService.getSystemMenuByModule(this.getKey());
	}

	@Override
	public void initDataObjectWithDefaultValues(String entityActionID, Object dataObject) {

		/*
		 * 从数据库加载外键字段值
		 */
		List<CocFieldService> fields = this.getFkFields();
		for (CocFieldService field : fields) {
			if (field.isManyToOne()) {
				Object fkFieldValue = ObjectUtil.getValue(dataObject, field.getFieldName());
				if (fkFieldValue != null && !ClassUtil.isAgent(fkFieldValue.getClass())) {
					String fkTargetField = field.getFkTargetFieldKey();
					Object fkTargetFieldValue = ObjectUtil.getValue(fkFieldValue, fkTargetField);
					if (fkTargetFieldValue != null) {
						fkFieldValue = orm().get(fkFieldValue.getClass(), Expr.eq(fkTargetField, fkTargetFieldValue));
						ObjectUtil.setValue(dataObject, field.getFieldName(), fkFieldValue);
					}
				}
			}
		}

		/*
		 * 计算字段默认值
		 */
		fields = this.getFields();
		for (CocFieldService field : fields) {
			String propName = field.getFieldName();
			if (ObjectUtil.getValue(dataObject, propName) != null)
				continue;

			String defaultValue = field.getDefaultValue();
			if (StringUtil.hasContent(defaultValue)) {
				if (defaultValue.startsWith(Const.VAR_PREFIX)) {
					String path = defaultValue.substring(Const.VAR_PREFIX.length(), defaultValue.length() - 1);
					Object defaultFieldValue = ObjectUtil.getValue(dataObject, path);
					if (defaultFieldValue != null) {
						ObjectUtil.setValue(dataObject, propName, defaultFieldValue);
					}
				} else {
					ObjectUtil.setValue(dataObject, propName, defaultValue);
				}
			}
		}
	}

	@Override
	public CuiEntityService getCuiEntity(String cuiKey) {
		if (StringUtil.isBlank(cuiKey))
			cuiKey = this.getKey();

		if (cuiEntityServices == null) {
			cuiEntityServices = new HashMap();
		}

		CuiEntityService ret = cuiEntityServices.get(cuiKey);

		if (ret == null) {
			ICuiEntity cui = orm().get(EntityTypes.CuiEntity, Expr.eq(Const.F_COC_ENTITY_KEY, this.getKey())//
			        .and(Expr.eq(Const.F_KEY, cuiKey))//
			        );

			if (cui != null) {
				ret = new CuiEntityServiceImpl(cui, this);
				cuiEntityServices.put(cuiKey, ret);
			}
		}

		return ret;
	}

	@Override
	public CocGroupService getGroup(String groupKey) {
		if (groupKey == null)
			return null;

		for (CocGroupService group : fieldGroups) {
			if (groupKey.trim().equals(group.getKey()))
				return group;
		}

		return null;
	}

	@Override
	public String getDataAuthFields() {
		return this.entityData.getDataAuthFields();
	}

	@Override
	public boolean isWorkflow() {
		return entityData.isWorkflow();
	}
}
