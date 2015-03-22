package com.jsoft.cocit.util;

import com.jsoft.cocit.log.Log;
import com.jsoft.cocit.log.Logs;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public abstract class LogUtil {
	private static Log log = Logs.getLog(LogUtil.class);

	private static boolean traceDebug = true;

	private static boolean traceInfo = true;

	private static boolean traceWarn = true;

	private static boolean traceError = true;

	public static void trace(String message, Object... args) {
		if (args.length == 0) {
			log.trace(message);
		} else if (args[args.length - 1] instanceof Throwable) {
			if (traceDebug) {
				if (args.length == 1) {
					log.trace(message, (Throwable) args[0]);
				} else {
					log.trace(String.format(message, args), (Throwable) args[args.length - 1]);
				}
			} else {
				if (args.length > 1)
					message = String.format(message, args);

				Throwable ex = (Throwable) args[args.length - 1];
				if (ex instanceof NullPointerException)
					log.trace(message, ex);
				else {
					Throwable cause = ex.getCause();
					log.trace(message + "\n\t" + ex.toString() + "\n\t" + cause);
				}
			}
		} else {
			log.tracef(message, args);
		}
	}

	/**
	 * 
	 * @param content
	 * @param opArgs
	 *            可变参数，最后一个参数可以是Throwable
	 */
	public static void debug(String message, Object... args) {
		if (args.length == 0) {
			log.debug(message);
		} else if (args[args.length - 1] instanceof Throwable) {
			if (traceDebug) {
				if (args.length == 1) {
					log.debug(message, (Throwable) args[0]);
				} else {
					log.debug(String.format(message, args), (Throwable) args[args.length - 1]);
				}
			} else {
				if (args.length > 1)
					message = String.format(message, args);

				Throwable ex = (Throwable) args[args.length - 1];
				if (ex instanceof NullPointerException)
					log.debug(message, ex);
				else {
					Throwable cause = ex.getCause();
					log.debug(message + "\n\t" + ex.toString() + "\n\t" + cause);
				}
			}
		} else {
			log.debugf(message, args);
		}
	}

	/**
	 * 
	 * @param content
	 * @param opArgs
	 *            可变参数，最后一个参数可以是Throwable
	 */
	public static void info(String message, Object... args) {
		if (args.length == 0) {
			log.info(message);
		} else if (args[args.length - 1] instanceof Throwable) {
			if (traceInfo) {
				if (args.length == 1) {
					log.info(message, (Throwable) args[0]);
				} else {
					log.info(String.format(message, args), (Throwable) args[args.length - 1]);
				}
			} else {
				if (args.length > 1)
					message = String.format(message, args);

				Throwable ex = (Throwable) args[args.length - 1];
				if (ex instanceof NullPointerException)
					log.info(message, ex);
				else {
					Throwable cause = ex.getCause();
					log.info(message + "\n\t" + ex.toString() + "\n\t" + cause);
				}
			}
		} else {
			log.infof(message, args);
		}
	}

	/**
	 * 
	 * @param content
	 * @param opArgs
	 *            可变参数，最后一个参数可以是Throwable
	 */
	public static void warn(String message, Object... args) {
		if (args.length == 0) {
			log.warn(message);
		} else if (args[args.length - 1] instanceof Throwable) {
			if (traceWarn) {
				if (args.length == 1) {
					log.warn(message, (Throwable) args[0]);
				} else {
					log.warn(String.format(message, args), (Throwable) args[args.length - 1]);
				}
			} else {
				if (args.length > 1)
					message = String.format(message, args);

				Throwable ex = (Throwable) args[args.length - 1];
				if (ex instanceof NullPointerException)
					log.warn(message, ex);
				else {
					Throwable cause = ex.getCause();
					log.warn(message + "\n\t" + ex.toString() + "\n\t" + cause);
				}
			}
		} else {
			log.warnf(message, args);
		}
		// if (traceWarn)
		// new Exception().printStackTrace();
	}

	/**
	 * 
	 * @param fmt
	 * @param opArgs
	 *            可变参数，最后一个参数可以是Throwable
	 */
	public static void error(String message, Object... args) {
		if (args.length == 0) {
			log.error(message);
		} else if (args[args.length - 1] instanceof Throwable) {
			if (traceError) {
				if (args.length == 1) {
					log.error(message, (Throwable) args[0]);
				} else {
					log.error(String.format(message, args), (Throwable) args[args.length - 1]);
				}
			} else {
				if (args.length > 1)
					message = String.format(message, args);

				Throwable ex = (Throwable) args[args.length - 1];
				if (ex instanceof NullPointerException)
					log.error(message, ex);
				else {
					Throwable cause = ex.getCause();
					log.error(message + "\n\t" + ex.toString() + "\n\t" + cause);
				}
			}
		} else {
			log.errorf(message, args);
		}
		// if (traceError)
		// new Exception().printStackTrace();
	}

	public static boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	public static boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	public static boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	public static void debug(StringBuffer sb) {
		log.debug(sb);
	}

	public static void info(StringBuffer sb) {
		log.info(sb.toString());
	}

}
