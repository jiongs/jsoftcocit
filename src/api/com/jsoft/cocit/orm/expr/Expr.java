package com.jsoft.cocit.orm.expr;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.exception.CocException;

/**
 * ORM表达式，用于描述数据查询、修改、删除、添加等操作的条件、字段限制、排序、分页等。
 * 
 * @author yongshan.ji
 * @preserve public
 * 
 */
public abstract class Expr {

	public static SimpleCndExpr rule(String field, String op, Object data) {
		return new ExprRule(field, op, data).toExpr();
	}

	public static SimpleCndExpr rule(String rule) {
		return new ExprRule(rule).toExpr();
	}

	private static int countLeftBracket(String rule) {
		rule = rule.trim();

		int ret = 0;
		if (rule.charAt(0) == '(') {
			ret = 1 + countLeftBracket(rule.substring(1));
		}

		return ret;
	}

	public static CndExpr rules(String rules) {
		rules = rules.trim();
		int len = rules.length();

		int tmpLeftCount = 0;
		int leftCount = countLeftBracket(rules);
		if (leftCount > 0) {
			String leftRule = "";
			String rightRule = "";
			int rightCount = 0;
			for (int i = 0; i < len; i++) {
				char c = rules.charAt(i);
				switch (c) {
					case '(':
						tmpLeftCount++;
						if (tmpLeftCount > leftCount) {
							leftCount = tmpLeftCount;
						}
						break;
					case ')':
						rightCount++;
						if (rightCount == leftCount) {
							leftRule = rules.substring(1, i);
							String str = rules.substring(i + 1).trim();
							if (str.toLowerCase().startsWith("and")) {
								rightRule = str.substring(3);
								CndExpr leftExpr = rules(leftRule);
								CndExpr rightExpr = rules(rightRule);
								return Expr.and(leftExpr, rightExpr);
							} else if (str.toLowerCase().startsWith("or")) {
								rightRule = str.substring(2);
								CndExpr leftExpr = rules(leftRule);
								CndExpr rightExpr = rules(rightRule);
								return Expr.or(leftExpr, rightExpr);
							} else {
								return rules(leftRule);
							}
						}
				}
			}

			throw new CocException("非法表达式！%s", rules);
		} else {
			int size = 4;
			int andIndex = rules.toLowerCase().indexOf(" or ");
			if (andIndex == -1) {
				andIndex = rules.toLowerCase().indexOf(" or(");
				size = 3;
			}
			if (andIndex > 0) {
				String leftRule = rules.substring(0, andIndex);
				String rightRule = rules.substring(andIndex + size);
				CndExpr leftExpr = rules(leftRule);
				CndExpr rightExpr = rules(rightRule);
				return Expr.or(leftExpr, rightExpr);
			} else {
				size = 5;
				int orIndex = rules.toLowerCase().indexOf(" and ");
				if (orIndex == -1) {
					orIndex = rules.toLowerCase().indexOf(" and(");
					size = 4;
				}
				if (orIndex > 0) {
					String leftRule = rules.substring(0, orIndex);
					String rightRule = rules.substring(orIndex + size);
					CndExpr leftExpr = rules(leftRule);
					CndExpr rightExpr = rules(rightRule);
					return Expr.and(leftExpr, rightExpr);
				} else {
					return rule(rules);
				}
			}
		}
	}

	public static SimpleCndExpr eq(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.eq, value);
	}

	public static SimpleCndExpr gt(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.gt, value);
	}

	public static SimpleCndExpr ge(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.ge, value);
	}

	public static SimpleCndExpr le(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.le, value);
	}

	public static SimpleCndExpr lt(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.lt, value);
	}

	public static SimpleCndExpr ne(String prop, Object value) {
		return new SimpleCndExpr(prop, CndType.ne, value);
	}

	public static SimpleCndExpr eqProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.eq, prop2);
	}

	public static SimpleCndExpr gtProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.gt, prop2);
	}

	public static SimpleCndExpr geProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.ge, prop2);
	}

	public static SimpleCndExpr leProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.le, prop2);
	}

	public static SimpleCndExpr ltProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.lt, prop2);
	}

	public static SimpleCndExpr neProp(String prop, String prop2) {
		return new SimpleCndExpr(prop, CndType.ne, prop2);
	}

	public static SimpleCndExpr in(String prop, Long... value) {
		List list = new ArrayList();
		for (Long v : value) {
			list.add(v);
		}
		return new SimpleCndExpr(prop, CndType.in, value);
	}

	public static SimpleCndExpr in(String prop, String... value) {
		List list = new ArrayList();
		for (String v : value) {
			list.add(v);
		}
		return new SimpleCndExpr(prop, CndType.in, value);
	}

	public static SimpleCndExpr in(String prop, List value) {
		return new SimpleCndExpr(prop, CndType.in, value);
	}

	public static SimpleCndExpr notIn(String prop, List value) {
		return new SimpleCndExpr(prop, CndType.ni, value);
	}

	public static SimpleCndExpr like(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.lk, value);
	}

	public static SimpleCndExpr contains(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.lk, "%" + value + "%");
	}

	public static SimpleCndExpr notContains(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.nl, "%" + value + "%");
	}

	public static SimpleCndExpr beginWith(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.lk, value + "%");
	}

	public static SimpleCndExpr notBeginWith(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.nl, value + "%");
	}

	public static SimpleCndExpr endWith(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.lk, "%" + value);
	}

	public static SimpleCndExpr notEndWith(String prop, String value) {
		return new SimpleCndExpr(prop, CndType.nl, "%" + value);
	}

	public static SimpleCndExpr between(String prop, Object lo, Object hi) {
		SimpleCndExpr w = new SimpleCndExpr(prop, CndType.bt, lo);
		w.setValue2(hi);
		return w;
	}

	public static SimpleCndExpr isNull(String prop) {
		return new SimpleCndExpr(prop, CndType.nu, null);
	}

	public static SimpleCndExpr notNull(String prop) {
		return new SimpleCndExpr(prop, CndType.nn, null);
	}

	public static CombCndExpr and(CndExpr exp1, CndExpr exp2) {
		return new CombCndExpr(exp1, CombType.and, exp2);
	}

	public static CombCndExpr or(CndExpr exp1, CndExpr exp2) {
		return new CombCndExpr(exp1, CombType.or, exp2);
	}

	public static CombCndExpr not(CndExpr exp) {
		return new CombCndExpr(exp, CombType.not, null);
	}

	public static NullCndExpr desc(String prop) {
		NullCndExpr nexp = new NullCndExpr();
		nexp.addOrder(new OrderExpr(prop, OrderType.desc));
		return nexp;
	}

	public static NullCndExpr asc(String prop) {
		NullCndExpr nexp = new NullCndExpr();
		nexp.addOrder(new OrderExpr(prop, OrderType.asc));
		return nexp;
	}

	public static NullCndExpr orderby(String prop) {
		NullCndExpr nexp = new NullCndExpr();
		boolean desc = prop.indexOf(" desc") > 0;
		if (desc)
			nexp.addOrder(new OrderExpr(prop.replace(" desc", ""), OrderType.desc));
		else
			nexp.addOrder(new OrderExpr(prop.replace(" asc", ""), OrderType.asc));

		return nexp;
	}

	public static NullCndExpr group(String prop) {
		NullCndExpr nexp = new NullCndExpr();
		nexp.addGroup(new GroupByExpr(prop));
		return nexp;
	}

	public static NullCndExpr page(int pageIndex, int pageSize) {
		NullCndExpr nexp = new NullCndExpr();
		nexp.setPager(new PagerExpr(pageIndex, pageSize));
		return nexp;
	}

	public static NullCndExpr fieldRexpr(String rexpr) {
		return fieldRexpr(rexpr, false);
	}

	public static NullCndExpr fieldRexpr(String rexpr, boolean igloreNull) {
		NullCndExpr nexp = new NullCndExpr();
		nexp.setFieldRexpr(new FieldRexpr(rexpr, igloreNull));
		return nexp;
	}

}
