package com.jsoft.cocit.exception;

public class CocUnloginException extends CocSecurityException {

	private static final long serialVersionUID = -7480728064068398328L;

	private String redirect;

	public CocUnloginException() {
		super();
	}

	public CocUnloginException(Throwable e) {
		super(e);
	}

	public CocUnloginException(String msg, Throwable e) {
		super(msg, e);
	}

	public CocUnloginException(String fmt, Object... args) {
		super(String.format(fmt, args));
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
}
