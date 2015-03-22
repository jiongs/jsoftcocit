package com.jsoft.cocimpl.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.util.StringUtil;

public abstract class StyleUtil {
	public static Map<String, String> parseMap(String styleText) {
		if (styleText == null)
			return null;

		List<String> styleList = StringUtil.toList(styleText, ";");

		Map<String, String> ret = new HashMap();
		for (String str : styleList) {
			int idx = str.indexOf(':');
			if (idx > 0) {
				String attr = str.substring(0, idx).trim();
				String val = str.substring(idx + 1).trim();

				ret.put(attr.toLowerCase(), val);
			}
		}

		return ret;
	}

	/**
	 * 解析文本中开头部分的数字，如 50px，返回 50。
	 */
	public static Integer parseInt(String val) {
		if (val == null)
			return null;

		int len = val.length();
		StringBuffer sb = new StringBuffer(len);

		for (int i = 0; i < len; i++) {
			char c = val.charAt(i);
			if (c >= '0' && c <= '9') {
				sb.append(c);
			} else {
				break;
			}
		}

		if (sb.length() > 0)
			return Integer.parseInt(sb.toString());

		return null;
	}
}
