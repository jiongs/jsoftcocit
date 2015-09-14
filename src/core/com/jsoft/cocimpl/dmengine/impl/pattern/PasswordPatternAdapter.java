package com.jsoft.cocimpl.dmengine.impl.pattern;

import com.jsoft.cocit.constant.PatternNames;
import com.jsoft.cocit.dmengine.IPatternAdapter;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.StringUtil;

public class PasswordPatternAdapter implements IPatternAdapter<String> {
	private int minLength = 8;

	private int maxLength = 16;

	private boolean needUpperCase = false;

	private boolean needLowerCase = false;

	private boolean needNumber = true;

	private boolean needSymbols = false;

	@Override
	public String getName() {
		return PatternNames.PATTERN_PASSWORD;
	}

	@Override
	public String getPattern() {
		return "";
	}

	@Override
	public boolean validate(String pwd) {
		if (StringUtil.isBlank(pwd))
			return true;

		/*
		 * 密码长度验证
		 */
		if (pwd.length() < minLength || pwd.length() > maxLength) {
			throw new CocException("长度必须为%s-%s位！", minLength, maxLength);
		}

		/*
		 * 密码必需包含大写字母
		 */
		if (needUpperCase && pwd.toLowerCase().equals(pwd)) {
			throw new CocException("必须包含至少一个大写字母！");
		}

		/*
		 * 密码必需包含小写字母
		 */
		if (needLowerCase && pwd.toUpperCase().equals(pwd)) {
			throw new CocException("必须包含至少一个小写字母！");
		}

		/*
		 * 密码必需包含数字
		 */
		if (needNumber) {
			boolean containsNumber = false;
			for (int i = pwd.length() - 1; i >= 0; i--) {
				char c = pwd.charAt(i);
				if (c >= '0' && c <= '9') {
					containsNumber = true;
					break;
				}
			}
			if (!containsNumber) {
				throw new CocException("必须包含至少一个数字！");
			}
		}

		/*
		 * 密码必需包含特殊字符
		 */
		if (needSymbols) {
			boolean containsSymbols = false;
			for (int i = pwd.length() - 1; i >= 0; i--) {
				char c = pwd.charAt(i);
				if (!(c >= '0' && c <= '9')// is not number
				        && !(c >= 'a' && c <= 'z')// is not lower case
				        && !(c >= 'A' && c <= 'Z')// is not lower case
				        && !(c == ' ')// is not space
				) {
					containsSymbols = true;
					break;
				}
			}
			if (!containsSymbols) {
				throw new CocException("必须包含至少一个特殊字符（除数字、字母、空格以外的字符）！");
			}
		}

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
