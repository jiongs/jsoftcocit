package com.jsoft.cocit.baseentity;

import javax.persistence.Column;

/**
 * 
 * “可命名实体”基类：该类是【{@link DataEntity}】的子类，该类的子类所映射的数据表记录包含名称、序号等字段信息。
 * <p>
 * 其子类所映射的数据表除了拥有父类【{@link DataEntity}】中所描述的公共字段外，还将拥有如下公共字段：
 * <UL>
 * <LI>name：数据名称，数据库端字段名【_name】，字段类型【String】，字段长度【255】
 * <LI>sn：数据序号，数据库端字段名【_sn】，字段类型【Integer】
 * </UL>
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public abstract class NamedEntity extends DataEntity implements INamedEntityExt {

	@Column(length = 64, name = "name_")
	protected String name;

	@Column(length = 64, name = "name_py")
	protected String namePinyin;

	@Column(length = 64, name = "name_abbr")
	protected String nameAbbr;

	@Column(length = 64, name = "name_abbr_py")
	protected String nameAbbrPinyin;

	@Column(name = "sn_")
	protected Integer sn;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String toString() {
		if (name != null && name.trim().length() > 0) {
			return name + "(" + code + ")";
		}

		return super.toString();
	}

	protected void toJson(StringBuffer sb) {
		this.toJson(sb, "name", name);

		super.toJson(sb);

		this.toJson(sb, "sn", sn);
	}

	public void release() {
		super.release();

		this.name = null;
		this.sn = null;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public String getNameAbbr() {
		return nameAbbr;
	}

	public void setNameAbbr(String nameAbbr) {
		this.nameAbbr = nameAbbr;
	}

	public String getNameAbbrPinyin() {
		return nameAbbrPinyin;
	}

	public void setNameAbbrPinyin(String nameAbbrPinyin) {
		this.nameAbbrPinyin = nameAbbrPinyin;
	}
}
