package com.jsoft.cocit.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.CndType;
import com.jsoft.cocit.orm.expr.CombCndExpr;
import com.jsoft.cocit.orm.expr.CombType;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.orm.expr.SimpleCndExpr;

/**
 * 该工具类用来检查指定的数据对象是否与设定的条件匹配？
 * 
 * @author Ji Yongshan
 * 
 */
public abstract class ExprUtil {

	public static CndExpr systemIs(String systemKey) {
		ICocConfig config = Cocit.me().getConfig();

		/*
		 * 平台管理系统
		 */
		if (config.getCocitSystemCode().equals(systemKey)) {
			return Expr.eq(Const.F_SYSTEM_CODE, systemKey);
		}

		/*
		 * 支持多应用系统
		 */
		if (config.isMultiSystem()) {
			return Expr.eq(Const.F_SYSTEM_CODE, systemKey);
		}
		/*
		 * 不支持多应用系统
		 */
		else {
			return Expr.ne(Const.F_SYSTEM_CODE, config.getCocitSystemCode());
		}
	}

	public static CndExpr tenantIs(String tenantKey) {
		ICocConfig config = Cocit.me().getConfig();

		/*
		 * 平台用户
		 */
		if (config.getCocitTenantCode().equals(tenantKey)) {
			return null;
		}

		/*
		 * 支持多租
		 */
		if (config.isMultiTenant()) {
			return Expr.eq(Const.F_TENANT_CODE, tenantKey);
		}
		/*
		 * 不支持多租
		 */
		else {
			return null;
		}
	}

	/**
	 * 检查对象字段值是否与指定的字段值相匹配。
	 */
	public static boolean match(Object obj, CndExpr cndExpr) {
		if (cndExpr == null) {
			return true;
		}

		return match(cndExpr, obj);
	}

	public static boolean match(String fld, String val, CndExpr cndExpr) {
		if (cndExpr == null) {
			return true;
		}

		return match(cndExpr, fld, val);
	}

	private static boolean match(CndExpr cndExpr, Object obj) {
		if (cndExpr instanceof SimpleCndExpr) {
			return match((SimpleCndExpr) cndExpr, obj);
		} else if (cndExpr instanceof CombCndExpr) {
			return match((CombCndExpr) cndExpr, obj);
		}

		return false;
	}

	private static boolean match(CndExpr cndExpr, String fld, String val) {
		if (cndExpr instanceof SimpleCndExpr) {
			return match((SimpleCndExpr) cndExpr, fld, val);
		} else if (cndExpr instanceof CombCndExpr) {
			return match((CombCndExpr) cndExpr, fld, val);
		}

		return false;
	}

	private static boolean match(CombCndExpr cndExpr, Object obj) {
		CombCndExpr scnd = (CombCndExpr) cndExpr;
		CndExpr expr1 = scnd.getExpr();
		CndExpr expr2 = scnd.getExpr2();
		if (scnd.getType() == CombType.and) {
			return match(expr1, obj) && match(expr2, obj);
		} else if (scnd.getType() == CombType.or) {
			return match(expr1, obj) || match(expr2, obj);
		} else if (scnd.getType() == CombType.not) {
			return !match(expr1, obj);
		}

		return false;
	}

	private static boolean match(CombCndExpr cndExpr, String fld, String val) {
		CombCndExpr scnd = (CombCndExpr) cndExpr;
		CndExpr expr1 = scnd.getExpr();
		CndExpr expr2 = scnd.getExpr2();
		if (scnd.getType() == CombType.and) {
			return match(expr1, fld, val) && match(expr2, fld, val);
		} else if (scnd.getType() == CombType.or) {
			return match(expr1, fld, val) || match(expr2, fld, val);
		} else if (scnd.getType() == CombType.not) {
			return !match(expr1, fld, val);
		}

		return false;
	}

	private static boolean match(SimpleCndExpr cndExpr, Object obj) {
		SimpleCndExpr scnd = (SimpleCndExpr) cndExpr;
		String prop = scnd.getProp();
		Object fieldValue = ObjectUtil.getValue(obj, prop);
		Object cndValue = scnd.getValue();

		return match(fieldValue, scnd.getType(), cndValue);
	}

	private static boolean match(SimpleCndExpr cndExpr, String fld, String val) {
		SimpleCndExpr scnd = (SimpleCndExpr) cndExpr;
		String prop = scnd.getProp();
		if (!prop.equals(fld)) {
			return true;
		}

		Object fieldValue = val;
		Object cndValue = scnd.getValue();

		return match(fieldValue, scnd.getType(), cndValue);
	}

	private static boolean match(Object fieldValue, CndType op, Object cndValue) {

		String str1 = "";
		if (fieldValue != null) {
			str1 = fieldValue.toString().trim();
		}
		String str2 = "";
		if (cndValue != null) {
			str2 = cndValue.toString().trim();
		}

		if (op == CndType.eq) {
			return str1.equals(str2);
		} else if (op == CndType.ge) {
			return SortUtil.compare(str1, str2, false) >= 0;
		} else if (op == CndType.bt) {
		} else if (op == CndType.gt) {
			return SortUtil.compare(str1, str2, false) > 0;
		} else if (op == CndType.in) {
			if (cndValue instanceof List) {
				List strList = new ArrayList();
				for (Object obj : ((List) cndValue)) {
					strList.add(obj.toString());
				}
				return strList.contains(str1);
			} else {
				return str1.equals(str2);
			}
		} else if (op == CndType.le) {
			return SortUtil.compare(str1, str2, false) <= 0;
		} else if (op == CndType.lk) {
			return str1.contains(str2);
		} else if (op == CndType.cn) {
			return str1.contains(str2);
		} else if (op == CndType.lt) {
			return SortUtil.compare(str1, str2, false) < 0;
		} else if (op == CndType.ne) {
			return SortUtil.compare(str1, str2, false) != 0;
		} else if (op == CndType.ni) {
			boolean m = false;
			if (cndValue instanceof List) {
				List strList = new ArrayList();
				for (Object obj : ((List) cndValue)) {
					strList.add(obj.toString());
				}
				m = strList.contains(str1);
			} else {
				m = str1.equals(str2);
			}
			return !m;
		} else if (op == CndType.nl) {
			return !str1.contains(str2);
		} else if (op == CndType.bw) {
			return str1.startsWith(str2);
		} else if (op == CndType.ew) {
			return str1.endsWith(str2);
		} else if (op == CndType.nn) {
			return str1.length() != 0;
		} else if (op == CndType.nu) {
			return str1.length() == 0;
		}

		return false;
	}

	public static CndExpr parseToExpr(String whereRule) {
		if (StringUtil.isBlank(whereRule)) {
			return null;
		}

		String json = whereRule;
		if (!whereRule.startsWith("{")) {
			json = "{" + whereRule + "}";
		}
		try {
			Map map = JsonUtil.loadFromJson(Map.class, json);
			return ExprUtil.parseToExpr(map);
		} catch (Throwable e) {
			return Expr.rules(whereRule);
		}
	}

	public static CndExpr parseToExpr(Map map) {
		CndExpr retExpr = null;

		Iterator<String> exprs = map.keySet().iterator();
		while (exprs.hasNext()) {
			String prop = exprs.next();
			Object value = map.get(prop);

			int idx = prop.indexOf(".");
			if (idx > -1) {
				prop = prop.substring(0, idx);
			}
			if (value instanceof List) {
				List valueList = (List) value;

				if (retExpr == null) {
					retExpr = Expr.in(prop, valueList);
				} else {
					retExpr = retExpr.and(Expr.in(prop, valueList));
				}
			} else {
				String str = value.toString();

				if (retExpr == null) {
					retExpr = Expr.eq(prop, str);
				} else {
					retExpr = retExpr.and(Expr.eq(prop, str));
				}
			}
		}

		return retExpr;
	}
	

	public static CndExpr makeInExprFromJson(String jsonExpr, StringBuffer logExpr) {

		if (StringUtil.isBlank(jsonExpr)) {
			return null;
		}
		if (jsonExpr.charAt(0) != '{') {
			return Expr.rules(jsonExpr);
		}

		CndExpr retExpr = null;

		Map map = JsonUtil.loadFromJson(Map.class, jsonExpr);
		Iterator<String> exprs = map.keySet().iterator();
		while (exprs.hasNext()) {
			String prop = exprs.next().trim();

			String fld = prop;
			String op = "";
			Object value = map.get(prop);

			int idx = prop.indexOf(" ");
			if (idx > 0) {
				fld = prop.substring(0, idx);
				op = prop.substring(idx + 1);
			}

			idx = fld.indexOf(".");
			if (idx > -1) {
				fld = fld.substring(0, idx);
			}

			if (value instanceof List) {
				retExpr = makeExpr(retExpr, fld, op, (List) value);
			} else {
				retExpr = makeExpr(retExpr, fld, op, value.toString());
			}
		}

		return retExpr;
	}

	public static CndExpr makeExpr(CndExpr retExpr, String fld, String op, List valueList) {
		if (valueList == null || valueList.size() == 0) {
			return retExpr;
		}

		if (valueList.size() == 1) {
			return makeExpr(retExpr, fld, op, valueList.get(0).toString());
		}

		if (StringUtil.isBlank(op)) {
			op = "in";
		}

		if (retExpr == null) {
			retExpr = Expr.rule(fld, op, valueList);
		} else {
			retExpr = retExpr.and(Expr.rule(fld, op, valueList));
		}

		return retExpr;
	}

	public static CndExpr makeExpr(CndExpr retExpr, String fld, String op, String value) {
		if (value == null || value.trim().length() == 0) {
			return retExpr;
		}

		if ("-keywords-".equals(fld)) {
			if (retExpr == null) {
				retExpr = Expr.contains(Const.F_NAME, value).or(Expr.contains(Const.F_CODE, value));
			} else {
				retExpr = retExpr.and(Expr.contains(Const.F_NAME, value).or(Expr.contains(Const.F_CODE, value)));
			}
		} else {

			if (StringUtil.isBlank(op)) {
				op = "eq";
			}

			if (retExpr == null) {
				retExpr = Expr.rule(fld, op, value);
			} else {
				retExpr = retExpr.and(Expr.rule(fld, op, value));
			}
		}

		return retExpr;
	}
}
