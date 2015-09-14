package com.jsoft.cocimpl.dmengine.impl.pattern;

import com.jsoft.cocit.constant.PatternNames;
import com.jsoft.cocit.dmengine.IPatternAdapter;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.StringUtil;

/*
 * 用户帐号规则：登录帐号必须以字母开头！且只能由数字、字母、下划线组成！
 */
public class CodePatternAdapter implements IPatternAdapter<String> {

	@Override
	public String getName() {
		return PatternNames.PATTERN_CODE;
	}

	@Override
	public String getPattern() {
		return "";
	}

	@Override
	public boolean validate(String code) {
		if (!StringUtil.isCode(code))
			throw new CocException("必须以字母开头！且只能由数字、字母、下划线组成！");

		return true;
	}

	@Override
	public String format(String fieldValue) {
		if (fieldValue == null)
			return "";

		return fieldValue;
	}

	@Override
	public String parse(String strFieldValue, Class type) {
		return strFieldValue;
	}

}
