package com.jsoft.cocimpl.mvc.nutz;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.json.Json;
import org.nutz.mvc.adaptor.ParamExtractor;
import org.nutz.mvc.adaptor.ParamInjector;
import org.nutz.mvc.adaptor.Params;

import com.jsoft.cocit.mvc.CocEntityParam;
import com.jsoft.cocit.orm.expr.ExprRule;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 */
public class CocEntityParamInjector implements ParamInjector {

	private String prefix;

	public CocEntityParamInjector(String prefix) {
		if ("".equals(prefix))
			this.prefix = "entity";
		else
			this.prefix = prefix;
	}

	public Object get(ServletContext sc, HttpServletRequest req, HttpServletResponse resp, Object refer) {
		return get(sc, req, resp, refer, prefix);
	}

	public static CocEntityParam get(ServletContext sc, HttpServletRequest req, HttpServletResponse resp, Object refer, String prefix) {
		CocEntityParam node = new CocEntityParam();

		List<ExprRule> rules = getExprRules(req, "naviRules");
		for (ExprRule rule : rules) {
			if (rule.getData() != null)
				node.put(prefix + rule.getField(), StringUtil.toArray(rule.getData().toString(), ","));
		}

		ParamExtractor pe = Params.makeParamExtractor(req, refer);
		for (Object name : pe.keys()) {
			String na = (String) name;
			if (na.startsWith(prefix + ".") || na.startsWith(prefix + "[")) {
				node.put(na, pe.extractor(na));
			}
		}

		return node;
	}

	protected static List<ExprRule> getExprRules(HttpServletRequest req, String param) {
		String naviRuleString = req.getParameter(param);
		List<String> ruleList = (List) Json.fromJson(naviRuleString);

		List<ExprRule> rules = new LinkedList();
		if (ruleList != null && ruleList.size() > 0) {
			ExprRule rule = null;
			for (String str : ruleList) {
				if (StringUtil.isBlank(str)) {
					continue;
				}

				rule = new ExprRule(str);
				String op = rule.getOp();
				if ("eq".equals(op) || "=".equals(op)) {
					rules.add(rule);
				}

			}
		}

		return rules;
	}
}
