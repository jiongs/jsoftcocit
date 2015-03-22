package com.jsoft.cocit.exception;

import com.jsoft.cocit.Cocit;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public class CocException extends RuntimeException {
	private static final long serialVersionUID = 5608067870963671246L;

	public CocException() {
		super();
	}

	public CocException(Throwable e) {
		super(e);
	}

	public CocException(String msg, Throwable e) {
		super(msg, e);
	}

	public CocException(String fmt, Object... args) {
		super(Cocit.me().getMessages().getMsg(fmt, args));
	}

}
