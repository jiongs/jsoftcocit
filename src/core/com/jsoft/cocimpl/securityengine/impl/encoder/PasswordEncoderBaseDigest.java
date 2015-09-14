package com.jsoft.cocimpl.securityengine.impl.encoder;

public abstract class PasswordEncoderBaseDigest extends PasswordEncoderImpl {

	private boolean encodeHashAsBase64 = false;

	public boolean getEncodeHashAsBase64() {
		return encodeHashAsBase64;
	}

	public void setEncodeHashAsBase64(boolean encodeHashAsBase64) {
		this.encodeHashAsBase64 = encodeHashAsBase64;
	}
}