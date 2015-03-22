package com.jsoft.cocimpl.entityengine.impl.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jsoft.cocit.entityengine.PatternAdapter;

public class CheckEmailAdapter implements PatternAdapter<String> {

	@Override
	public String format(String str) {
		return str;
	}

	@Override
	public String getName() {
		return "email";
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
		if (str == null || str.trim().equals("")) {
			return false;
		}
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
