package com.jsoft.cocimpl.securityengine.impl.encoder;

public class PasswordEncoderSha extends PasswordEncoderMessageDigest {

	public PasswordEncoderSha() {
		this(1);
	}

	public PasswordEncoderSha(int strength) {
		super("SHA-" + strength);
	}
}