package com.jsoft.cocimpl.dmengine.impl.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jsoft.cocit.dmengine.IPatternAdapter;
/**
 *电话号码验证：手机以1开头的11位数字；座机有区号以0开头的10-12位数字，无区号为7-8为数字
 * @author YZKJ
 *
 */
public class CheckPhoneAdapter implements IPatternAdapter<String> {

	@Override
	public String format(String str) {
		return str;
	}

	@Override
	public String getName() {
		return "phone";
	}

	@Override
	public String getPattern() {
		return "";
	}

	@Override
	public String parse(String str, Class<String> arg1) {
		return str;
	}
	
	@Override
	public boolean validate(String str) {
		return isMatch("(^0[\\d]{2,3}?-?)?[\\d]{7,8}$", str) || isMatch("[1][\\d]{10}", str);
	}
	public boolean isMatch(String regex, String str) {
		if (str == null || str.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
}
