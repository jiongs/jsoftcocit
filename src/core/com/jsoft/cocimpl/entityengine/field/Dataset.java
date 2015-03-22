package com.jsoft.cocimpl.entityengine.field;

import java.util.List;

import javax.persistence.Column;

import com.jsoft.cocimpl.ExtHttpContext;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 用于字段类型为数据集的情况
 * 
 * @author Administrator
 * 
 */
@CocColumn(precision = 2000, uiView = "ui.widget.field.Composite")
public class Dataset extends JsonField<Dataset> {
	public Dataset() {
		this("");
	}

	public Dataset(String str) {
		super(str);
	}

	@CocColumn(name = "继承选项", sn = 1, dicOptions = "0:不继承,1:继承数据", notes = "是否继承上级板块数据源查询结果集")
	private boolean inherit;

	// 实体表管理模块 字典数据来自模块实体表
	@CocColumn(name = "数据模块", fkTargetEntity = "", sn = 2, dicOptions = "['type eq 2']")
	private String moduleKey;

	// @CocColumn(name = "业务模块", refrenceSystem = BIZSYS_SOFT_MODULE, sn = 1,
	// dicOptions = "['type eq 2']")
	// private String moduleKey;// 功能模块

	@CocColumn(name = "数据分类", sn = 3, uiView = "ui.widget.field.ComboboxSys", uiCascading = "moduleKey:*:E")
	private String rules;// 查询规则

	@CocColumn(name = "动态分类", sn = 4, dicOptions = "0:不支持,1:路径参数", notes = "是否支持URL路径参数作为动态数据分类条件？只用于数据分类中指定了外键字段的情况。")
	private boolean dynamic;

	private String rules2;// rules label

	/**
	 * 逗号分隔的条件子句。
	 * <p>
	 * 语法示例：keywords like 吃
	 * <p>
	 * op 可以是：
	 * <UL>
	 * <LI>eq: equal, =
	 * <LI>ne: not equal, <>
	 * <LI>lt: less, <
	 * <LI>le: less or equal,<=
	 * <LI>gt: greater, >
	 * <LI>ge: greater or equal, >=
	 * <LI>bw: begins with, LIKE
	 * <LI>bn: does not begin with, NOT LIKE
	 * <LI>in: in, IN
	 * <LI>ni: not in, NOT IN
	 * <LI>ew: ends with, LIKE
	 * <LI>en: does not end with, NOT LIKE
	 * <LI>cn: contains, LIKE
	 * <LI>nc: does not contain, NOT LIKE
	 * <LI>nu: is null, IS NULL
	 * <LI>nn: is not null, IS NOT NULL
	 * <LI>lk: LIKE
	 * <LI>nl: NOT LIKE
	 * <LI>gl: between, greater and less
	 * </UL>
	 */
	@CocColumn(name = "查询条件", sn = 5)
	private String cnds;

	/**
	 * 逗号分隔的动态查询条件：即请求参数中的关键字(...&_k=XXX&...)将替换动态条件中的%s部分，如果动态条件是逗号分隔的多条件子句， 则请求参数中的参数对也将是多个，如：...?_k=v&_k1=v1&_k2=v2&...
	 * <p>
	 * 语法：keywords like %s, field1 eq %s, ...
	 */
	@CocColumn(name = "动态条件", sn = 6)
	private String dcnds;

	@CocColumn(name = "分页大小", sn = 7, uiView = "ui.widget.field.Spinner")
	private int pageSize;

	// 排序语法，如： sn ASC, id DESC
	@CocColumn(name = "排序字段", sn = 8)
	private String snBy;

	/**
	 * 逗号分隔的字段列表，只查询指定的字段，以便提高查询效率，不指定则查询所有字段。
	 */
	@Column(length = 128)
	@CocColumn(name = "查询字段", sn = 9, notes = "只查询指定的字段，以便提高查询效率")
	private String fieldRule;

	@CocColumn(name = "分组字段", sn = 10)
	private String groupBy;// 分组字段

	@Override
	protected void init(Dataset obj) {
		if (obj != null) {
			this.fieldRule = obj.fieldRule;
			this.groupBy = obj.groupBy;
			this.moduleKey = obj.moduleKey;
			this.rules = obj.rules;
			this.rules2 = obj.rules2;
			this.cnds = obj.cnds;
			this.pageSize = obj.pageSize;
			this.snBy = obj.snBy;
			this.dynamic = obj.dynamic;
			this.inherit = obj.inherit;
			this.dcnds = obj.dcnds;
		}
	}

	public String getRulesUrl() {
		if (StringUtil.isBlank(moduleKey))
			return "";

		return MVCUtil.makeUrl(UrlAPI.ENTITY_GET_COMBO_GRID, moduleKey + ":") + "?gridColumns=3&idField=key";
	}

	public List<String> getExprs() {
		List<String> ret = StringUtil.toList(rules, ",;");
		ret.addAll(StringUtil.toList(cnds, ",;"));

		// 解析动态条件
		List<String> dlist = StringUtil.toList(dcnds, ",;");
		int len = dlist.size();
		if (len > 0) {
			ExtHttpContext ctx = (ExtHttpContext) Cocit.me().getHttpContext();
			if (ctx != null) {
				String k = ctx.getParameterValue("_k", String.class, "");
				if (!StringUtil.isBlank(k))
					ret.add(String.format(dlist.get(0), k));
				for (int i = 1; i < len; i++) {
					k = ctx.getParameterValue("_k" + i, String.class, "");
					if (!StringUtil.isBlank(k))
						ret.add(String.format(dlist.get(i), k));
				}
			}
		}

		return ret;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public String getOrderBy() {
		return snBy;
	}

	public void setOrderBy(String snBy) {
		this.snBy = snBy;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}

	public String getRules2() {
		return rules2;
	}

	public void setRules2(String rules2) {
		this.rules2 = rules2;
	}

	public String getFieldRule() {
		return fieldRule;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setFieldRule(String fieldRule) {
		this.fieldRule = fieldRule;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getModuleKey() {
		return moduleKey;
	}

	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}

	public boolean isInherit() {
		return inherit;
	}

	public void setInherit(boolean inherit) {
		this.inherit = inherit;
	}

	public String getCnds() {
		return cnds;
	}

	public void setCnds(String customRules) {
		this.cnds = customRules;
	}

	public String getDcnds() {
		return dcnds;
	}

	public void setDcnds(String keyfield) {
		this.dcnds = keyfield;
	}

}
