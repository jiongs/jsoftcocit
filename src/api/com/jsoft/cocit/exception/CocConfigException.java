package com.jsoft.cocit.exception;


public class CocConfigException extends CocException {

	private static final long serialVersionUID = -7480728064068398328L;

	private String redirect;

	public CocConfigException() {
		super();
	}

	public CocConfigException(Throwable e) {
		super(e);
	}

	public CocConfigException(String msg, Throwable e) {
		super(msg, e);
	}

	public CocConfigException(String fmt, Object... args) {
		super(String.format(fmt, args));
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}
}
