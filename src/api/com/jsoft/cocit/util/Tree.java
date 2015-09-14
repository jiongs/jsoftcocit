// $codepro.audit.disable com.instantiations.assist.eclipse.analysis.audit.rule.effectivejava.obeyEqualsContract.obeyGeneralContractOfEquals, unnecessaryCast
package com.jsoft.cocit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jsoft.cocit.config.IDynaBean;

public class Tree {

	private Map<String, Node> nodeMap;

	private Properties extProps;

	private List<Node> children;

	private StringBuffer log;

	private Tree() {
		nodeMap = new HashMap();
		children = new ArrayList();
		extProps = new Properties();
		log = new StringBuffer();
	}

	public Node findNode(String key, String val) {
		if (children == null)
			return null;

		if ("id".equals(key)) {
			return nodeMap.get(val);
		} else {
			for (Node node : children) {
				String str = (String) node.get(key);
				if (StringUtil.hasContent(str) && str.equals(val)) {
					return node;
				}
			}
		}

		return null;
	}

	public void release() {
		Iterator keys = this.nodeMap.keySet().iterator();
		while (keys.hasNext()) {
			Object key = keys.next();
			Node node = nodeMap.get(key);
			node.release();
		}
		this.nodeMap.clear();
		this.nodeMap = null;
		this.extProps.clear();
		this.extProps = null;
		this.children.clear();
		this.children = null;
	}

	public void optimizeStatus() {
		List<Node> list = getChildren();
		if (list.size() == 1) {
			Node node = list.get(0);
			List<Node> children = node.getChildren();
			this.children.remove(0);
			for (Node child : children) {
				this.children.add(child);
				child.setParent(null);
			}
		}
		for (Node child : children) {
			child.set("open", "true");
		}
	}

	public void optimize() {
		List<Node> children = getChildren();
		// for (int i = list.size() - 1; i >= 0; i--) {
		// removeEmptyFolder(list, i);
		// }

		for (int i = children.size() - 1; i >= 0; i--) {
			Node child = children.get(i);
			move1ChildFolder(child);
		}
	}

	// 处理只有一个儿子的文件夹
	void move1ChildFolder(Node node) {
		if ("folder".equals(node.get("type", ""))) {
			if (node.size() == 1) {
				Node child = node.getChildren().get(0);

				Node parent = node.getParent();
				if (parent != null) {
					// 将单个儿子节点往上移动
					parent.getChildren().add(child);
					child.setParent(parent);

					// 并从当前节点中移除
					node.getChildren().remove(0);
					parent.getChildren().remove(node);

					// 继续往上移动单个儿子节点
					// move1ChildFolder(child);
				} else {
					this.children.add(child);
					child.setParent(null);

					// 并从当前节点中移除
					this.getChildren().remove(0);
					this.children.remove(node);
				}
			}
		}
	}

	// 移除空文件夹
	void removeEmptyFolder(List<Node> children, int index) {
		Node node = children.get(index);
		if (node.size() != 0) {
			List<Node> list2 = node.getChildren();
			for (int i2 = list2.size() - 1; i2 >= 0; i2--) {
				removeEmptyFolder(list2, i2);
			}
		}
		if (node.size() == 0) {
			children.remove(index);
		}
	}

	public <T> T get(String propName, T defaultReturn) {
		String value = extProps.getProperty(propName);

		if (value == null)
			return defaultReturn;
		if (defaultReturn == null)
			return (T) value;

		Class valueType = defaultReturn.getClass();

		try {
			return (T) StringUtil.castTo(value, valueType);
		} catch (Throwable e) {
			LogUtil.warn("", e);
		}

		return defaultReturn;
	}

	public Tree set(String propName, Object value) {
		if (value != null)
			extProps.put(propName, value);

		return this;

	}

	//
	// public Tree set(String propName, String value) {
	// extProps.put(propName, value);
	//
	// return this;
	// }

	public static Tree make() {
		return new Tree();
	}

	public int count() {
		int total = this.size();
		for (Node node : children) {
			total += node.count();
		}

		return total;
	}

	public boolean existNode(String nodeID) {
		return nodeMap.get(nodeID) != null;
	}

	private Node getNode(String nodeID) {
		if (StringUtil.isBlank(nodeID)) {
			return null;
		}

		Node node = nodeMap.get(nodeID);

		if (node == null) {
			node = new Node(nodeID);
			nodeMap.put(nodeID, node);
		}

		return node;
	}

	public Node addNode(String parentID, String nodeID) {
		log.append(String.format("(%s, %s)\n", parentID, nodeID));

		return this.addNode(parentID, nodeID, -1);
	}

	public Node addNode(String parentID, String nodeID, int index) {
		Node p = getNode(parentID);
		Node n = getNode(nodeID);

		if (n != null) {
			if (p != null) {
				p.setType("folder");
				if (!p.getChildren().contains(n))
					p.addChild(n);
			} else if (!children.contains(n)) {
				if (index >= 0)
					children.add(index, n);
				else
					children.add(n);
			}
		}

		return n;
	}

	public Tree addChild(Node child) {
		child.parent = null;
		children.add(child);

		return this;
	}

	public int size() {
		if (children == null) {
			return 0;
		}
		return children.size();
	}

	public List<Node> getChildren() {
		return children;
	}

	public List<Node> getAll() {
		List<Node> ret = new ArrayList();

		this.addTo(ret, children);

		return ret;
	}

	private void addTo(List<Node> ret, List<Node> nodes) {
		for (Node node : nodes) {
			if (!ret.contains(node))
				ret.add(node);

			if (node.size() > 0) {
				addTo(ret, node.getChildren());
			}
		}
	}

	/**
	 * <B>扩展属性：</B>
	 * <UL>
	 * <LI>open: BOOL值；
	 * <LI>checked: BOOL值；
	 * <LI>type: 字符串，可选值 folder | leaf；
	 * <LI>icon: 字符串，图片环境路径，如：/images/tree/folder.gif；
	 * </UL>
	 * 
	 * @preserve all
	 */
	public static final class Node implements IDynaBean {
		private List<Node> children;

		private Node parent;

		private String id;// 节点ID

		private String key;

		private String name;// 节点名称

		private Integer sn;// 节点顺序

		private String statusCode;

		private String type;

		private String childrenURL;

		private Properties extProps;

		private Object referObj;

		private Node(String id) {
			this.id = id;
			type = "leaf";
			this.extProps = new Properties();
			this.children = new ArrayList();
		}

		private void release() {
			this.children.clear();
			this.children = null;
			this.parent = null;
			this.id = null;
			this.name = null;
			this.sn = null;
			this.key = null;
			this.statusCode = null;
			this.type = null;
			this.childrenURL = null;
			this.extProps.clear();
			this.extProps = null;
			this.referObj = null;
		}

		public void removeAllLeaf() {
			if (children == null || children.size() == 0)
				return;

			removeAllLeaf(children);
		}

		private static void removeAllLeaf(List<Node> children) {
			for (int i = children.size() - 1; i >= 0; i--) {
				Node child = children.get(i);
				if (child.children == null || child.children.size() == 0) {
					children.remove(i);
				} else {
					removeAllLeaf(child.children);
				}
			}
		}

		/**
		 * 获取节点扩展属性
		 * 
		 * @param propName
		 * @return
		 */
		public <T> T get(String propName, T defaultReturn) {
			String value = extProps.getProperty(propName);

			if (value == null)
				return defaultReturn;
			if (defaultReturn == null)
				return (T) value;

			Class valueType = defaultReturn.getClass();

			try {
				return (T) StringUtil.castTo(value, valueType);
			} catch (Throwable e) {
				LogUtil.warn("", e);
			}

			return defaultReturn;
		}

		/**
		 * 设置节点扩展属性
		 * 
		 * @param propName
		 * @param value
		 * @return
		 */
		public Node set(String propName, Object value) {
			if (value != null)
				extProps.put(propName, value);

			return this;
		}

		public int count() {
			int total = this.size();
			for (Node node : children) {
				total += node.count();
			}

			return total;
		}

		public Node getParent() {
			return parent;
		}

		public List<Node> getChildren() {
			return children;
		}

		public Node addChild(Node child) {
			child.parent = this;
			children.add(child);

			return this;
		}

		public void removeChild(Node c) {
			children.remove(c);
			if (c.getParent() == this) {
				c.parent = null;
			}
		}

		public int size() {
			if (children == null) {
				return 0;
			}
			return children.size();
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public Node setName(String name) {
			this.name = name;

			return this;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public String toString() {
			return this.name == null ? id : (this.name + "(" + id + ")");
		}

		public int hashCode() {
			if (id == null) {
				return super.hashCode();
			}
			return 37 * 17 + id.hashCode();
		}

		public boolean equals(Object that) {

			if (that == null)
				return false;

			if (!that.getClass().equals(this.getClass()))
				return false;

			Node thatEntity = (Node) that;
			if (id == null || thatEntity.id == null) {
				return super.equals(that);
			}

			return thatEntity.id.equals(id);
		}

		public Integer getSn() {
			return sn;
		}

		public void setSn(Integer seq) {
			this.sn = seq;
		}

		public String getChildrenURL() {
			return childrenURL;
		}

		public void setChildrenURL(String childrenURL) {
			this.childrenURL = childrenURL;
		}

		public Properties getProperties() {
			return extProps;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public Object getReferObj() {
			return referObj;
		}

		public void setReferObj(Object referenceObj) {
			this.referObj = referenceObj;
		}

		public String getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(String status) {
			this.statusCode = status;
		}

		public void setStatusCode(int status) {
			this.statusCode = "" + status;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Object get(String extField) {
			return this.extProps.get(extField);
		}

		public int getSize() {
			return this.size();
		}

		public int getLast() {
			if (isLast()) {
				return 1;
			}

			return 0;
		}

		public int getFirst() {
			if (isFirst()) {
				return 1;
			}

			return 0;
		}

		public boolean isLast() {
			return this.parent.getChildren().get(this.getParent().getSize() - 1) == this;
		}

		public boolean isFirst() {
			return this.parent.getChildren().get(0) == this;
		}
	}

	public void sort() {
		sort(children);
	}

	private void sort(List<Node> list) {
		if (list == null) {
			return;
		}
		SortUtil.sort(list, "sn", true);
		for (Node node : list) {
			sort(node.getChildren());
		}
	}

	public void removeNode(Node node) {
		this.children.remove(node);
		this.nodeMap.remove(node.getId());
		Node parent = node.getParent();
		if (parent != null && parent.getChildren() != null) {
			parent.children.remove(node);
		}

	}

	public Map<String, Node> getAllMap() {
		return this.nodeMap;
	}

	public int sizeAll() {
		return nodeMap.size();
	}

	public int getSize() {
		return this.size();
	}

	public StringBuffer getLog() {
		return log;
	}
}
