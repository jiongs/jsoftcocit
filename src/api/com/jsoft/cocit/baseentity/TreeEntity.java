package com.jsoft.cocit.baseentity;

import javax.persistence.Column;


/**
 * “树形结构的数据实体”基类：该类是【{@link NamedEntity}】的子类，该类的子类所映射的数据表记录包含递归树节点信息。
 * <p>
 * 其子类所映射的数据表除了拥有父类【{@link NamedEntity}】中所描述的公共字段外，还将拥有如下公共字段：
 * <UL>
 * <LI>parentCode：父节点，数据库端字段名【_parent_key】,字段类型【逻辑外键，关联到自身表的逻辑主键{@link ITreeEntity#getCode()}】
 * </UL>
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public abstract class TreeEntity extends NamedEntity implements ITreeEntityExt {

	@Column(length = 64, name = "parent_key_")
	protected String parentCode;

	@Column(length = 128, name = "parent_name_")
	protected String parentName;

	protected void toJson(StringBuffer sb) {
		super.toJson(sb);

		this.toJson(sb, "parentCode", parentCode);
		this.toJson(sb, "parentName", parentName);
	}

	public void release() {
		super.release();

		this.parentCode = null;
		this.parentName = null;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}
