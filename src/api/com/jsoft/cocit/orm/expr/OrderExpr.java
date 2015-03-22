package com.jsoft.cocit.orm.expr;

/**
 * 排序表达式：用来描述SQL语句中的 order by 部分。
 * 
 * @author yongshan.ji
 * @preserve public
 */
public class OrderExpr extends Expr {

	private OrderType type;

	private String prop;

	public OrderExpr(String prop, OrderType type) {
		this.prop = prop;
		this.type = type;
	}

	/**
	 * 获取排序字段
	 * 
	 * @return
	 */
	public String getProp() {
		return prop;
	}

	public OrderType getType() {
		return type;
	}

	public String toString() {
		return "order by " + prop + " " + type;
	}

}
