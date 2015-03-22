package com.jsoft.cocit.orm.expr;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public class NullCndExpr extends CndExpr {
	public NullCndExpr() {
	}

	public NullCndExpr setFieldRexpr(FieldRexpr exp) {
		this.fieldRexpr = exp;
		return this;
	}

	public NullCndExpr setPager(PagerExpr exp) {
		this.pagerExpr = exp;
		return this;
	}

	public NullCndExpr addGroup(GroupByExpr exp) {
		this.groupByExprs.add(exp);
		return this;
	}

	public NullCndExpr addOrder(OrderExpr exp) {
		this.orderExprs.add(exp);
		return this;
	}
}
