package com.jsoft.cocimpl.orm.nutz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.EntityField;
import org.nutz.dao.entity.Link;
import org.nutz.lang.Mirror;

import com.jsoft.cocimpl.orm.generator.EntityIdGenerator;
import com.jsoft.cocimpl.orm.generator.INamingStrategy;
import com.jsoft.cocit.orm.mapping.EnColumnMapping;
import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.util.SortUtil;
import com.jsoft.cocit.util.StringUtil;

public class EnMappingImpl<T> extends Entity<T> implements EnMapping {
	Map<String, EnColumnMappingImpl> fieldsMap;// 字段映射:<数据表字段名，字段>
	Map<String, EnColumnMappingImpl> propsMap;// 字段映射:<Java属性名，字段>
	private Map<String, Link> fkMap;// 外键映射：<外键名，字段>
	private List<EntityField> sortedFields;// 按字段名排序后的字段列表
	private Mirror<? extends T> agentMirror;// 代理类：
	private Mirror<? extends T> lazyAgentMirror;// 懒加载代理类
	// 实体间的继承关系
	private EnColumnMappingImpl enTypeCol;// 使用单表时用该字段来区分
	private EnMappingImpl<?> parent;
	private List<EnMappingImpl> children;
	private EntityIdGenerator idTableGenerator;
	private INamingStrategy naming;
	private boolean syncedTable = false;
	private boolean syncedRefTable = false;
	private boolean readonly;
	private List<String[]> uniqueFields;
	private List<String[]> indexFields;
	private List<EnColumnMapping> generatorColumns;

	public void release() {
		super.release();

		if (fieldsMap != null) {
			for (EnColumnMappingImpl o : fieldsMap.values()) {
				o.release();
			}
			fieldsMap.clear();
			fieldsMap = null;
		}

		if (propsMap != null) {
			for (EnColumnMappingImpl o : propsMap.values()) {
				o.release();
			}
			propsMap.clear();
			propsMap = null;
		}

		if (fkMap != null) {
			for (Link o : fkMap.values()) {
				o.release();
			}
			fkMap.clear();
			fkMap = null;
		}

		if (sortedFields != null) {
			for (EntityField o : sortedFields) {
				o.release();
			}
			sortedFields.clear();
			sortedFields = null;
		}

		agentMirror = null;
		lazyAgentMirror = null;

		if (enTypeCol != null) {
			enTypeCol.release();
			enTypeCol = null;
		}

		parent = null;

		idTableGenerator = null;
		naming = null;

		if (children != null) {
			for (EnMappingImpl o : children) {
				o.release();
			}
			children.clear();
			children = null;
		}

		if (uniqueFields != null) {
			uniqueFields.clear();
			uniqueFields = null;
		}

		if (indexFields != null) {
			indexFields.clear();
			indexFields = null;
		}
	}

	public EnMappingImpl(EnMappingImpl parent) {
		super();
		fieldsMap = new HashMap<String, EnColumnMappingImpl>();
		propsMap = new HashMap<String, EnColumnMappingImpl>();
		sortedFields = new ArrayList();
		uniqueFields = new ArrayList();
		indexFields = new ArrayList();
		generatorColumns = new ArrayList();
		fkMap = new HashMap<String, Link>();
		if (parent != null) {
			parent.addChild(this);
		}
	}

	// public void destroy(EnMappingHolder holder) {
	// if (children != null) {
	// List<EnMappingImpl> list = new LinkedList();
	// for (EnMappingImpl c : children) {
	// list.add(c);
	// }
	// for (EnMappingImpl c : list) {
	// holder.remove(c);
	// c.destroy(holder);
	// }
	// }
	//
	// if (fieldsMap != null) {
	// fieldsMap.clear();
	// fieldsMap = null;
	// }
	// if (fkMap != null) {
	// fkMap.clear();
	// fkMap = null;
	// }
	// if (sortedFields != null) {
	// sortedFields.clear();
	// sortedFields = null;
	// }
	// agentMirror = null;
	// lazyAgentMirror = null;
	// enTypeCol = null;
	//
	// if (parent != null && parent.children != null)
	// parent.children.remove(this);
	//
	// idTableGenerator = null;
	// naming = null;
	// }

	public boolean isAbstract() {
		return this.getChildren() != null;
	}

	private void addChild(EnMappingImpl child) {
		if (children == null) {
			children = new ArrayList();
		}
		child.parent = this;
		children.add(child);
	}

	public void addField(EntityField ef) {
		String fieldName = ef.getColumnName();
		if (fieldsMap.get(fieldName) == null) {
			fieldsMap.put(fieldName, (EnColumnMappingImpl) ef);

			// 对字段按数据表列名进行排序
			sortedFields.add(ef);
			SortUtil.sort(sortedFields, "columnName", false);

			super.addField(ef);
		}
		String propName = ef.getName();
		if (propsMap.get(propName) == null) {
			propsMap.put(propName, (EnColumnMappingImpl) ef);
		}
	}

	public void addForeignKey(String fkColumnName, Link link) {
		fkMap.put(fkColumnName, link);
	}

	public EnColumnMappingImpl getFieldByColumn(String name) {
		EnColumnMappingImpl ret = this.fieldsMap.get(name);
		if (ret == null)
			ret = this.fieldsMap.get(name.toUpperCase());
		if (ret == null)
			ret = this.fieldsMap.get(name.toLowerCase());

		return ret;
	}

	public Link getLink(String fkColumnName) {
		return fkMap.get(fkColumnName);
	}

	public Collection<EntityField> fields() {
		return sortedFields;
	}

	public Mirror<? extends T> getAgentMirror() {
		return agentMirror;
	}

	public void setAgentMirror(Mirror<? extends T> agentMirror) {
		this.agentMirror = agentMirror;
	}

	public Mirror<? extends T> getLazyAgentMirror() {
		return lazyAgentMirror;
	}

	public void setLazyAgentMirror(Mirror<? extends T> agentMirror) {
		this.lazyAgentMirror = agentMirror;
	}

	public EnMappingImpl getParent() {
		return parent;
	}

	public boolean isChild() {
		return getParent() != null;
	}

	public List<EnMappingImpl> getChildren() {
		return children;
	}

	public EnColumnMappingImpl getDtype() {
		return enTypeCol;
	}

	public void setDtype(EnColumnMappingImpl dtype) {
		this.enTypeCol = dtype;
	}

	public EntityIdGenerator getIdGenerator() {
		return idTableGenerator;
	}

	public void setIdGenerator(EntityIdGenerator idTableGenerator) {
		this.idTableGenerator = idTableGenerator;
	}

	public INamingStrategy getNaming() {
		return naming;
	}

	public void setNaming(INamingStrategy naming) {
		this.naming = naming;
	}

	public String getIdProperty() {
		return getIdentifiedField() == null ? null : getIdentifiedField().getName();
	}

	public List getRelations(String regex) {
		return this.getLinks(regex);
	}

	public List getManyMany(String regex) {
		List<Link> links = getLinks(regex);
		List<Link> ret = new ArrayList();
		for (Link link : links) {
			if (link.isManyMany() && StringUtil.isBlank((String) link.get("mappedBy"))) {
				ret.add(link);
			}
		}
		return ret;
	}

	public List getTargetManyMany(String regex) {
		List<Link> links = getLinks(regex);
		List<Link> ret = new ArrayList();
		for (Link link : links) {
			if (link.isManyMany() && !StringUtil.isBlank((String) link.get("mappedBy"))) {
				ret.add(link);
			}
		}
		return ret;
	}

	public boolean isSyncedTable() {
		return syncedTable;
	}

	public void setSyncedTable(boolean syncTable) {
		this.syncedTable = syncTable;
	}

	public boolean isSyncedRefTable() {
		return syncedRefTable;
	}

	public void setSyncedRefTable(boolean syncRefTable) {
		this.syncedRefTable = syncRefTable;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public List<String[]> getUniqueFields() {
		return uniqueFields;
	}

	public List<String[]> getIndexFields() {
		return indexFields;
	}

	public void addGeneratorColumn(EnColumnMapping column) {
		generatorColumns.add(column);
	}

	public Iterator<EnColumnMapping> getGeneratorColumns() {
		return this.generatorColumns.iterator();
	}

	public String toString() {
		return this.getType().getSimpleName() + "(" + this.getTableName() + ")";
	}
}
