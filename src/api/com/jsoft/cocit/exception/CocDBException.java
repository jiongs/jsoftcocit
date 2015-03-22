package com.jsoft.cocit.exception;

public class CocDBException extends CocConfigException {

	private static final long serialVersionUID = -4522244358506763136L;

	public CocDBException() {
		super();
	}

	public CocDBException(Throwable e) {
		super(e);
	}

	public CocDBException(String msg, Throwable e) {
		super(msg, e);
	}

	public CocDBException(String fmt, Object... args) {
		super(String.format(fmt, args));
	}

}
