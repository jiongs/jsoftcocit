package com.jsoft.cocit.orm.expr;

import java.util.List;

import com.jsoft.cocit.util.StringUtil;

/**
 * 字段正则表达式：用来描述SQL语句中要查询、修改、添加的字段集合。
 * 
 * @author yongshan.ji
 * @preserve public
 */
public class FieldRexpr extends Expr {

	private String regExpr;

	private boolean igloreNull;

	public FieldRexpr(String regExpr, boolean igloreNull) {
		List<String> list = StringUtil.toList(regExpr, ",;| ");
		StringBuffer sb = new StringBuffer();
		for (String str : list) {
			sb.append("|").append(str).append("$");
		}

		if (sb.length() > 0)
			this.regExpr = sb.substring(1);
	}

	/**
	 * 获取正则表达式
	 * 
	 * @return
	 */
	public String getRegExpr() {
		return regExpr;
	}

	public boolean isIgloreNull() {
		return igloreNull;
	}

	public String toString() {
		return this.regExpr;
	}

}
