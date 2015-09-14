package com.jsoft.cocimpl.dmengine.impl.info;

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
import com.jsoft.cocit.baseentity.INamedEntity;
import com.jsoft.cocit.baseentity.ITreeEntity;
import com.jsoft.cocit.baseentity.ITreeObjectEntity;
import com.jsoft.cocit.baseentity.coc.ICocActionEntity;
import com.jsoft.cocit.baseentity.coc.ICocEntity;
import com.jsoft.cocit.baseentity.coc.ICocFieldEntity;
import com.jsoft.cocit.baseentity.coc.ICocGroupEntity;
import com.jsoft.cocit.baseentity.cui.ICuiEntity;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.dmengine.IPatternAdapter;
import com.jsoft.cocit.dmengine.IPatternAdapters;
import com.jsoft.cocit.dmengine.field.IExtField;
import com.jsoft.cocit.dmengine.info.ICocActionInfo;
import com.jsoft.cocit.dmengine.info.ICocCatalogInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.dmengine.info.ICocGroupInfo;
import com.jsoft.cocit.dmengine.info.ICuiEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormFieldInfo;
import com.jsoft.cocit.dmengine.info.ICuiFormInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.log.Log;
import com.jsoft.cocit.log.Logs;
import com.jsoft.cocit.orm.IExtDao;
import com.jsoft.cocit.orm.IExtOrm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.orm.generator.EntityGenerators;
import com.jsoft.cocit.orm.generator.Generator;
import com.jsoft.cocit.orm.mapping.EnColumnMapping;
import com.jsoft.cocit.orm.mapping.EnMapping;
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
public class CocEntityInfo extends NamedEntityInfo<ICocEntity>implements ICocEntityInfo {
	private static Log log = Logs.getLog(CocEntityInfo.class);

	private List<ICocGroupInfo>					fieldGroups;
	private List<ICocFieldInfo>					fields;
	private List<ICocFieldInfo>					fkFieldsOfOtherModules;
	// 其他模块的外键字段：即其他模块通过哪个外键关联到此模块
	// <moduleCode,EntityFieldProxy>
	private Map<String, ICocFieldInfo>			fkFieldsOfSubModules;
	private Map<Serializable, ICocFieldInfo>	fieldsMap;
	private List<ICocActionInfo>				actions;
	private Map<Serializable, ICocActionInfo>	actionsMap;
	private List<ICocFieldInfo>					fieldsForFilter;
	private Properties							extProps;
	private Class								typeOfEntity;
	private Map<String, ICuiEntityInfo>			cuiEntityServices;

	CocEntityInfo(ICocEntity e) {
		super(e);

		fieldGroups = new ArrayList();
		fields = new ArrayList();
		actions = new ArrayList();
		extProps = new Properties();
		fieldsMap = new HashMap();
		actionsMap = new HashMap();

		List<ICocFieldEntity> entityFields = (List<ICocFieldEntity>) orm().query(EntityTypes.CocField, Expr.eq(Const.F_COC_ENTITY_CODE, entityData.getCode()).addAsc(Const.F_SN));
		List<ICocActionEntity> entityActions = (List<ICocActionEntity>) orm().query(EntityTypes.CocAction, Expr.eq(Const.F_COC_ENTITY_CODE, entityData.getCode()).addAsc(Const.F_SN));

		initEntityFields(entityFields);
		initEntityActions(entityActions);
	}

	@Override
	public void release() {
		super.release();

		this.typeOfEntity = null;
		if (fieldGroups != null) {
			for (ICocGroupInfo group : fieldGroups) {
				group.release();
			}
			fieldGroups.clear();
			fieldGroups = null;
		}
		if (fields != null) {
			for (ICocFieldInfo f : fields) {
				f.release();
			}
			fields.clear();
			fields = null;
		}
		if (fkFieldsOfOtherModules != null) {
			for (ICocFieldInfo f : fkFieldsOfOtherModules) {
				f.release();
			}
			fkFieldsOfOtherModules.clear();
			fkFieldsOfOtherModules = null;
		}
		if (fkFieldsOfSubModules != null) {
			for (ICocFieldInfo f : fkFieldsOfSubModules.values()) {
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
			for (ICocActionInfo obj : actions) {
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
			for (ICuiEntityInfo f : cuiEntityServices.values()) {
				f.release();
			}
			cuiEntityServices.clear();
			cuiEntityServices = null;
		}
	}

	private void initEntityFields(List<ICocFieldEntity> fieldObjs) {
		if (fieldObjs == null)
			return;

		Map<String, CocGroupInfo> groupsMap = new HashMap();

		for (ICocFieldEntity fieldObj : fieldObjs) {
			String groupCode = fieldObj.getCocGroupCode();

			CocGroupInfo group = (CocGroupInfo) groupsMap.get(groupCode);
			if (group == null) {
				ICocGroupEntity groupObj = (ICocGroupEntity) orm().get(EntityTypes.CocGroup, Expr.eq(Const.F_COC_ENTITY_CODE, entityData.getCode()).and(Expr.eq(Const.F_CODE, groupCode)));
				group = new CocGroupInfo(groupObj, this);
				groupsMap.put(groupCode, group);
				fieldGroups.add(group);
			}

			ICocFieldInfo field = new CocFieldInfo(fieldObj, this, group);
			this.fields.add(field);
			this.fieldsMap.put(field.getId(), field);
			this.fieldsMap.put(field.getFieldName(), field);

			group.addField(field);
		}

		SortUtil.sort(fieldGroups, Const.F_SN, true);
	}

	private void initEntityActions(List<ICocActionEntity> actionObjs) {
		if (actionObjs == null)
			return;

		for (ICocActionEntity action : actionObjs) {
			ICocActionInfo actionService = new CocActionInfo(action, this);
			this.actions.add(actionService);
			this.actionsMap.put(actionService.getId(), actionService);
			this.actionsMap.put(actionService.getCode(), actionService);
		}
	}

	private void initFkFieldsOfSubModules() {
		if (fkFieldsOfOtherModules == null) {
			fkFieldsOfOtherModules = new ArrayList();
			fkFieldsOfSubModules = new HashMap();

			List<ICocFieldEntity> list = (List<ICocFieldEntity>) orm().query(EntityTypes.CocField, Expr.fieldRexpr(Const.F_PROP_NAME + "|" + Const.F_COC_ENTITY_CODE).and(CndExpr.eq(Const.F_FK_TARGET_ENTITY, this.getCode())));
			for (ICocFieldEntity otherFK : list) {
				ICocEntityInfo otherModule = getEntityInfoFactory().getEntity(otherFK.getCocEntityCode());
				if (otherModule == null) {
					continue;
				}
				ICocFieldInfo otherModuleFkField = otherModule.getField(otherFK.getFieldName());
				fkFieldsOfOtherModules.add(otherModuleFkField);

				if (otherModuleFkField.isFkTargetAsParent())
					fkFieldsOfSubModules.put(otherFK.getCocEntityCode(), otherModuleFkField);
			}
		}
	}

	private void initFieldsForFilter() {

		this.fieldsForFilter = new ArrayList();

		for (ICocFieldInfo field : this.fields) {
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

	public String getCatalogCode() {
		return entityData.getCatalogCode();
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

	public List<ICocGroupInfo> getGroups() {
		return fieldGroups;
	}

	public List<ICocFieldInfo> getFields() {
		return fields;
	}

	public List<ICocActionInfo> getActions(List<String> actionIDList) {
		List<ICocActionInfo> ret = new ArrayList();
		if (actionIDList == null) {
			for (ICocActionInfo action : actions) {
				if (action.isDisabled())
					continue;

				ret.add(action);
			}
		} else {
			for (String actionID : actionIDList) {
				ICocActionInfo action = this.getAction(actionID);
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

	public ICocActionInfo getActionByOpCode(int opCode) {
		for (ICocActionInfo op : this.actions) {
			if (op.getOpCode() == opCode) {
				return op;
			}
		}
		return null;
	}

	public String getUniqueFields() {
		return entityData.getUniqueFields();
	}

	public String getIndexFields() {
		return entityData.getIndexFields();
	}

	public List<ICocFieldInfo> getFieldsOfFilter(boolean usedToSubModule) {
		if (fieldsForFilter == null || fieldsForFilter.size() == 0)
			initFieldsForFilter();

		List<ICocFieldInfo> ret = new ArrayList();
		if (usedToSubModule) {

			for (ICocFieldInfo field : this.fieldsForFilter) {

				if (!field.isFkTargetAsParent()) {
					ret.add(field);
				}

			}
			return ret;
		}

		return fieldsForFilter;
	}

	public List<ICocFieldInfo> getFieldsOfDataAuth() {
		List<String> fields = StringUtil.toList(this.getDataAuthFields());

		List<ICocFieldInfo> ret = new ArrayList();

		for (String field : fields) {
			ICocFieldInfo f = this.getField(field);
			if (f != null) {
				ret.add(f);
			}
		}

		return ret;
	}

	public ICocFieldInfo getFieldOfTree() {
		for (ICocFieldInfo field : this.fields) {

			if (field.isDisabled()) {// !field.isAsFilterNode() ||
				continue;
			}

			if (field.isFkField() && field.getFkTargetEntityCode().equals(this.entityData.getCode())) {
				return field;
			}

		}

		return null;
	}

	@Override
	public ICocFieldInfo getFieldOfGroup() {
		for (ICocFieldInfo field : this.fields) {

			if (field.isFkTargetAsGroup() && !field.isDisabled()) {
				return field;
			}

		}

		return null;
	}

	public List<ICocFieldInfo> getFieldsOfGrid(List<String> fieldNames) {
		List<ICocFieldInfo> ret = new LinkedList();

		if (fieldNames == null || fieldNames.size() == 0) {
			List<ICocFieldInfo> other = new LinkedList();
			for (ICocFieldInfo fld : this.fields) {
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
				ICocFieldInfo field = this.getField(fieldName);
				if (field == null || field.isDisabled())
					continue;

				ret.add(field);
			}
		}

		return ret;
	}

	public Map<String, ICocFieldInfo> getBizFieldsMapByPropName() {
		Map<String, ICocFieldInfo> map = new HashMap();
		for (ICocFieldInfo f : this.fields) {
			map.put(f.getFieldName(), f);
		}

		return map;
	}

	public Tree getFilterData(List<String> filterFields, boolean usedToSubModule) {

		List<ICocFieldInfo> filterFieldServices = this.getFieldsOfFilter(usedToSubModule);

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
				ICocFieldInfo fld = this.getField(fldName);
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

		List<ICocFieldInfo> filterFieldServices = this.getFieldsOfDataAuth();

		Tree tree = Tree.make();

		for (ICocFieldInfo fld : filterFieldServices) {
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

		ICocFieldInfo field = this.getFieldOfTree();
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

	private boolean makeSelfTreeNodes(Tree tree, Node node, ICocFieldInfo treeField, CndExpr expr) {
		boolean ret = true;
		String rootNodeID = node == null ? "" : node.getId();

		if (treeField == null) {

			/*
			 * 获取外键目标实体的分组字段
			 */
			ICocFieldInfo fkGroupField = null;
			for (ICocFieldInfo targetFkField : getFkFields()) {
				if (targetFkField.isFkTargetAsGroup()) {
					fkGroupField = targetFkField;
					break;
				}
			}

			// 创建外键节点的分组节点：即外键节点按什么字段进行分组？
			String groupField = null;
			if (fkGroupField != null) {
				groupField = fkGroupField.getFieldName();
				String fkGroupTargetField = fkGroupField.getFkTargetFieldCode();
				if (fkGroupField.isManyToOne()) {
					groupField = groupField + "." + fkGroupTargetField;
				}
				ret = makeSelfTreeNodes(tree, fkGroupField, null, rootNodeID, "", groupField, null);
				for (Node n : tree.getAll()) {
					n.set("unselectable", true);
					// n.set("uncheckable", true);
					n.set("unvalueable", true);
				}
			}

			// 创建外键节点
			ret = ret && makeSelfTreeNodes(tree, treeField, groupField, rootNodeID, groupField, "", expr);
		} else if (treeField.isFkField()) {

			/*
			 * 获取外键字段关联的目标模块
			 */
			ICocEntityInfo fkTargetModule = treeField.getFkTargetEntity();
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
			String groupField = null;
			if (fkGroupField != null) {
				groupField = fkGroupField.getFieldName();
				String fkGroupTargetField = fkGroupField.getFkTargetFieldCode();
				if (fkGroupField.isManyToOne()) {
					groupField = groupField + "." + fkGroupTargetField;
				}
				ret = makeSelfTreeNodes(tree, fkGroupField, null, rootNodeID, "", groupField, null);
				for (Node n : tree.getAll()) {
					n.set("unselectable", true);
					// n.set("uncheckable", true);
					n.set("unvalueable", true);
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

	private boolean makeSelfTreeNodes(Tree tree, ICocFieldInfo treeField, String groupField, String rootNodeID, String groupNodeIDPrefix, String nodeIDPrefix, CndExpr expr) {
		Class type = this.getClassOfEntity();
		if (treeField != null) {
			// 获取该字段引用的外键系统
			ICocEntityInfo fkModule = treeField.getFkTargetEntity();

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
				parentID = obj.getParentCode();
				if (StringUtil.isBlank(parentID)) {
					parentID = this.makeGroupNodeID(obj, groupNodeIDPrefix, groupField, rootNodeID);
				} else {
					// 分组字段关联的表是一颗自身树
					if (StringUtil.hasContent(nodeIDPrefix)) {
						parentID = nodeIDPrefix + ":" + parentID;
					}
				}
				String nodeID = obj.getCode();
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

			} else if (record instanceof ITreeObjectEntity) {
				ITreeObjectEntity obj = (ITreeObjectEntity) record;
				ITreeObjectEntity parent = (ITreeObjectEntity) obj.getParent();
				if (parent != null) {
					parentID = parent.getCode();
				} else {
					parentID = "";
				}
				if (StringUtil.isBlank(parentID)) {
					parentID = this.makeGroupNodeID(obj, groupNodeIDPrefix, groupField, rootNodeID);
				}

				String nodeID = obj.getCode();
				if (StringUtil.hasContent(nodeIDPrefix)) {
					nodeID = nodeIDPrefix + ":" + nodeID;
				}
				String nodeName = obj.getName();

				Node childNode = tree.addNode(parentID, nodeID);
				if (childNode != null) {
					childNode.setName(nodeName);
					childNode.setSn(obj.getSn());
				}

			} else if (record instanceof INamedEntity) {
				INamedEntity obj = (INamedEntity) record;
				String nodeID = obj.getCode();
				if (StringUtil.hasContent(nodeIDPrefix)) {
					nodeID = nodeIDPrefix + ":" + nodeID;
				}
				String nodeName = obj.getName();
				if (StringUtil.hasContent(groupField)) {
					parentID = this.makeGroupNodeID(obj, groupNodeIDPrefix, groupField, rootNodeID);
				}

				Node childNode = tree.addNode(parentID, nodeID);
				if (childNode != null) {
					childNode.setName(nodeName);
					childNode.setSn(obj.getSn());
				}
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
				if (ClassUtil.hasField(type, Const.F_CODE)) {
					nodeID = "" + ObjectUtil.getValue(record, Const.F_CODE);
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

		log.debug("makeFieldNodes：" + tree.getLog());

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

	private Map<String, Integer> getFieldsModeMap(String opMode, Object entityObject) {
		ICuiEntityInfo cuiEntity = this.getCuiEntity(this.getUiView());
		ICocActionInfo cocAction = this.getAction(opMode);
		String uiForm = cocAction.getUiForm();
		ICuiFormInfo cuiForm = null;
		if (cuiEntity != null && StringUtil.hasContent(uiForm)) {
			cuiForm = cuiEntity.getCuiForm(uiForm);
		}
		Map<String, Integer> formFieldModes = new HashMap();
		if (cuiForm != null) {
			formFieldModes = cuiForm.getFieldModes();
		}

		List<ICocFieldInfo> fields = getFields();
		Map<String, Integer> fieldMode = new HashMap();
		for (ICocFieldInfo f : fields) {
			CocFieldInfo field = (CocFieldInfo) f;

			Integer mode = formFieldModes.get(field.getFieldName());
			if (mode == null)
				mode = field.getMode(opMode, entityObject);

			fieldMode.put(field.getFieldName(), mode);
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
					ICocFieldInfo fieldService = this.getField(field);
					String fkTargetField = fieldService.getFkTargetFieldCode();
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
			Map<String, ICocFieldInfo> fields = getBizFieldsMapByPropName();
			for (String prop : requiredFields) {
				error.append(fields.get(prop).getName()).append(",");
			}
		}

		// Pattern 校验
		Cocit coc = Cocit.me();
		IPatternAdapters patterns = coc.getPatternAdapters();
		List<ICocFieldInfo> fields = this.getFields();
		Object fieldValue;
		IPatternAdapter adapter;
		String pattern;
		String propName;
		IMessageConfig msgs = coc.getMessages();
		for (ICocFieldInfo field : fields) {
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

	public Map<String, ICocFieldInfo> getFieldsMap() {
		Map<String, ICocFieldInfo> ret = new Hashtable();
		for (ICocFieldInfo f : this.fields) {
			ret.put(f.getFieldName(), f);
		}

		return ret;
	}

	@Override
	public <T> List<T> parseDataFromExcel(File excel) {
		try {
			SystemExcelUtil systemExcel = new SystemExcelUtil(this, excel);
			return systemExcel.getRows();
		} catch (Throwable e) {
			throw new CocException(excel.getAbsolutePath() + " " + e.getMessage());
		}

	}

	public List<ICocFieldInfo> getFkFieldsOfOtherEntities() {
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

	public List<ICocFieldInfo> getFkFieldsOfSubEntities() {
		List<ICocFieldInfo> ret = new LinkedList();

		List<ICocFieldInfo> otherModuleFkFields = getFkFieldsOfOtherEntities();
		for (ICocFieldInfo otherModuleFkField : otherModuleFkFields) {
			if (!otherModuleFkField.isFkTargetAsParent()) {
				continue;
			}
			ICocEntityInfo otherModule = otherModuleFkField.getEntity();
			if (!otherModuleFkField.isDisabled() && !otherModule.isDisabled()) {
				ret.add(otherModuleFkField);
			}
		}

		return ret;
	}

	public List<ICocEntityInfo> getSubEntities() {
		List<ICocEntityInfo> ret = new LinkedList();

		List<ICocFieldInfo> fields = getFkFieldsOfSubEntities();
		for (ICocFieldInfo f : fields) {
			ICocEntityInfo fkModule = f.getEntity();
			ret.add(fkModule);
		}

		return ret;
	}

	public ICocActionInfo getAction(Serializable actionMode) {
		return this.actionsMap.get(actionMode);
	}

	public Class getClassOfEntity() {
		if (typeOfEntity == null) {
			typeOfEntity = ee().getTypeOfEntity(this);
		}

		return typeOfEntity;
	}

	public List<ICocFieldInfo> getFieldsOfEnabled() {
		List<ICocFieldInfo> list = this.getFields();

		List<ICocFieldInfo> ret = new LinkedList();
		for (ICocFieldInfo f : list) {
			if (!f.isDisabled())
				ret.add(f);
		}

		return ret;
	}

	public ICocFieldInfo getField(String propName) {
		return this.fieldsMap.get(propName);
	}

	public ICocFieldInfo getFkFieldOfSubEntity(String subModuleCode) {
		if (fkFieldsOfSubModules == null)
			this.initFkFieldsOfSubModules();

		return this.fkFieldsOfSubModules.get(subModuleCode);
	}

	public List<ICocFieldInfo> getFkFields() {
		List<ICocFieldInfo> list = this.getFields();

		List<ICocFieldInfo> ret = new LinkedList();
		for (ICocFieldInfo f : list) {
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
	public List<ICocFieldInfo> getFkFieldsOfRedundant(String propName) {
		List<ICocFieldInfo> ret = new ArrayList();

		for (ICocFieldInfo fk : this.getFkFields()) {
			if (propName.equals(fk.getFkDependFieldCode())) {
				ret.add(fk);
			}
		}

		return ret;
	}

	@Override
	public ICocFieldInfo getFkNameField(String propName) {
		for (ICocFieldInfo fk : this.getFkFields()) {
			if (propName.equals(fk.getFkDependFieldCode())) {
				if (Const.F_NAME.equals(fk.getFkTargetFieldCode()))
					return fk;
			}
		}

		return null;
	}

	@Override
	public ISystemMenuInfo getSystemMenu() {
		ISystemInfo systemService = Cocit.me().getHttpContext().getLoginSystem();

		return systemService.getSystemMenuByModule(this.getCode());
	}

	private Object getFieldValue(Object obj, String path) {
		if (obj == null)
			return null;

		int dot = path.indexOf(".");
		if (dot > -1) {
			String currentPath = path.substring(0, dot);
			String subPath = path.substring(dot + 1);

			Object currentFieldValue = ObjectUtil.getValue(obj, currentPath);

			if (currentFieldValue == null) {
				return null;
			}

			ICocFieldInfo fldInfo = this.getField(currentPath);
			ICocEntityInfo fkTargetEntity = fldInfo.getFkTargetEntity();
			String fkTargetField = fldInfo.getFkTargetFieldCode();

			int dot2 = subPath.indexOf(".");
			String fld = subPath;
			if (dot2 > -1) {
				fld = subPath.substring(0, dot2);
			}
			String fldExpr = "id|code|name|" + fld;

			if (fkTargetEntity != null) {
				Object fkObj = orm().get(fkTargetEntity.getClassOfEntity(), Expr.eq(fkTargetField, currentFieldValue).setFieldRexpr(fldExpr, false));

				return this.getFieldValue(fkObj, subPath);
			} else {
				return null;
			}

		} else {
			return ObjectUtil.getValue(obj, path);
		}

	}

	@Override
	public void initDataObjectWithDefaultValues(String entityActionID, Object dataObject) {

		/*
		 * 从数据库加载外键字段值
		 */
		List<ICocFieldInfo> fields = this.getFkFields();
		for (ICocFieldInfo field : fields) {
			if (field.isManyToOne()) {
				Object fkFieldValue = ObjectUtil.getValue(dataObject, field.getFieldName());
				if (fkFieldValue != null && !ClassUtil.isAgent(fkFieldValue.getClass())) {
					String fkTargetField = field.getFkTargetFieldCode();
					Object fkTargetFieldValue = ObjectUtil.getValue(fkFieldValue, fkTargetField);
					if (fkTargetFieldValue != null) {
						fkFieldValue = orm().get(fkFieldValue.getClass(), Expr.eq(fkTargetField, fkTargetFieldValue));
						ObjectUtil.setValue(dataObject, field.getFieldName(), fkFieldValue);
					}
				}
			}
		}

		ICuiFormInfo cuiForm = null;
		ICuiEntityInfo cuiEntity = this.getCuiEntity(this.getUiView());
		ICocActionInfo cocAction = this.getAction(entityActionID);
		if (cuiEntity != null && cocAction != null) {
			String uiForm = cocAction.getUiForm();
			if (StringUtil.hasContent(uiForm)) {
				cuiForm = cuiEntity.getCuiForm(uiForm);
			}
		}

		/*
		 * 计算字段默认值
		 */
		fields = this.getFields();
		for (ICocFieldInfo field : fields) {
			String propName = field.getFieldName();
			if (ObjectUtil.getValue(dataObject, propName) != null)
				continue;

			String defaultValue = field.getDefaultValue();
			if (cuiForm != null) {
				ICuiFormFieldInfo cuiField = cuiForm.getFormField(propName);
				if (cuiField != null && StringUtil.hasContent(cuiField.getDefaultValue())) {
					defaultValue = cuiField.getDefaultValue();
				}
			}
			if (StringUtil.hasContent(defaultValue)) {
				if (defaultValue.startsWith("$F{")) {// 取其他字段值
					String path = defaultValue.substring(3, defaultValue.length() - 1);

					Object defaultFieldValue = this.getFieldValue(dataObject, path);

					if (defaultFieldValue != null) {
						ObjectUtil.setValue(dataObject, propName, defaultFieldValue);
					}
				} else {

					IExtOrm orm = (IExtOrm) Cocit.me().orm();
					IExtDao dao = (IExtDao) orm.getDao();
					EnMapping entityMapping = dao.getEnMapping(dataObject.getClass());
					EnColumnMapping columnMapping = entityMapping.getColumnMapping(propName);

					String expr = defaultValue;
					String gname, params;
					Object fieldValue = null;

					EntityGenerators generators = Cocit.me().getEntityGenerators();
					Generator generator;

					/*
					 * 表达式解析：$V{genName.param}
					 */
					while (StringUtil.hasContent(expr)) {
						gname = "";
						params = "";

						if (expr.startsWith("$V{")) {
							int end = expr.indexOf("}");
							if (end > 0) {
								String fullprop = expr.substring(3, end);
								int dot = fullprop.indexOf(".");
								if (dot > 0) {
									gname = fullprop.substring(0, dot);
									params = fullprop.substring(dot + 1);
								} else {
									gname = fullprop;
								}

								expr = expr.substring(end + 1);
							} else {
								gname = expr;
								expr = "";
							}
						} else {// 兼容：genName(params)
							int from = expr.indexOf("(");
							if (from > 0) {
								int to = expr.indexOf(")");
								if (to > from) {
									gname = expr.substring(0, from);
									params = expr.substring(from + 1, to);
								}
							} else {
								gname = expr;
							}
							expr = "";
						}

						try {
							generator = generators.getGenerator(gname);
							if (generator == null) {
								params = gname;
								generator = generators.getGenerator("code");// 默认编码生成器：解析内容支持：$D?,$N?,$C?
							}
							if (generator == null) {
								fieldValue = defaultValue;
							} else {
								if (fieldValue == null) {
									fieldValue = generator.generate(dao, entityMapping, columnMapping, dataObject, StringUtil.toArray(params));
								} else {
									fieldValue = fieldValue.toString() + generator.generate(dao, entityMapping, columnMapping, dataObject, StringUtil.toArray(params));
								}
							}

						} catch (Throwable e) {
							throw new CocException("计算默认值失败! %s", ExceptionUtil.msg(e));
						}
					}

					ObjectUtil.setValue(dataObject, propName, fieldValue);
				}
			}
		}
	}

	@Override
	public ICuiEntityInfo getCuiEntity(String cuiCode) {
		if (StringUtil.isBlank(cuiCode))
			cuiCode = this.getCode();

		if (cuiEntityServices == null) {
			cuiEntityServices = new HashMap();
		}

		ICuiEntityInfo ret = cuiEntityServices.get(cuiCode);

		if (ret == null) {
			ICuiEntity cui = orm().get(EntityTypes.CuiEntity, Expr.eq(Const.F_COC_ENTITY_CODE, this.getCode())//
			        .and(Expr.eq(Const.F_CODE, cuiCode))//
			);

			if (cui != null) {
				ret = new CuiEntityInfo(cui, this);
				cuiEntityServices.put(cuiCode, ret);
			}
		}

		return ret;
	}

	@Override
	public ICocGroupInfo getGroup(String groupCode) {
		if (groupCode == null)
			return null;

		for (ICocGroupInfo group : fieldGroups) {
			if (groupCode.trim().equals(group.getCode()))
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

	@Override
	public ICocCatalogInfo getCatalogInfo() {
		return this.getEntityInfoFactory().getCocCatalog(this.getCatalogCode());
	}

	@Override
	public String getPrevInterceptors() {
		return entityData.getPrevInterceptors();
	}

	@Override
	public String getPostInterceptors() {
		return entityData.getPostInterceptors();
	}
}
