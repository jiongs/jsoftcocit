package com.jsoft.cocit.entity;

import javax.persistence.Column;


/**
 * “树形结构的数据实体”基类：该类是【{@link NamedEntity}】的子类，该类的子类所映射的数据表记录包含递归树节点信息。
 * <p>
 * 其子类所映射的数据表除了拥有父类【{@link NamedEntity}】中所描述的公共字段外，还将拥有如下公共字段：
 * <UL>
 * <LI>parentKey：父节点，数据库端字段名【_parent_key】,字段类型【逻辑外键，关联到自身表的逻辑主键{@link ITreeEntity#getKey()}】
 * </UL>
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public class TreeEntity extends NamedEntity implements IExtTreeEntity {

	@Column(length = 64, name = "parent_key_")
	protected String parentKey;

	@Column(length = 128, name = "parent_name_")
	protected String parentName;

	protected void toJson(StringBuffer sb) {
		super.toJson(sb);

		this.toJson(sb, "parentKey", parentKey);
		this.toJson(sb, "parentName", parentName);
	}

	public void release() {
		super.release();

		this.parentKey = null;
		this.parentName = null;
	}

	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}
