package com.jsoft.cocimpl.securityengine.impl.encoder;

public class ShaPasswordEncoder extends MessageDigestPasswordEncoder {

	public ShaPasswordEncoder() {
		this(1);
	}

	public ShaPasswordEncoder(int strength) {
		super("SHA-" + strength);
	}
}