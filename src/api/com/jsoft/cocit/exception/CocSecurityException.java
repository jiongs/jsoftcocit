package com.jsoft.cocit.exception;

import com.jsoft.cocit.Cocit;

public class CocSecurityException extends CocException {

	private static final long serialVersionUID = -6634480503807002472L;

	public CocSecurityException() {
		super();
	}

	public CocSecurityException(Throwable e) {
		super(e);
	}

	public CocSecurityException(String msg, Throwable e) {
		super(msg, e);
	}

	public CocSecurityException(String fmt, Object... args) {
		super(Cocit.me().getMessages().getMsg(fmt, args));
	}

}
