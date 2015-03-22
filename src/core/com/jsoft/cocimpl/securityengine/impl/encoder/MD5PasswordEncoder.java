package com.jsoft.cocimpl.securityengine.impl.encoder;

import com.jsoft.cocit.securityengine.PasswordEncoder;

public class MD5PasswordEncoder implements PasswordEncoder {
	private MD5 md5 = new MD5();

	public String encodePassword(String rawPass, Object salt) {
		return md5.getMD5ofStr(rawPass);
	}

	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		String pass1 = "" + encPass;
		String pass2 = encodePassword(rawPass, salt);

		return pass1.toLowerCase().equals(pass2.toLowerCase());
	}

	public boolean isValidPassword(String encPass, String rawPass, Object salt) {
		return isPasswordValid(encPass, rawPass, salt);
	}

}
