package com.jsoft.cocit.log;

import com.jsoft.cocimpl.log.JdkLogAdapter;
import com.jsoft.cocimpl.log.Log4jLogAdapter;
import com.jsoft.cocimpl.log.LogAdapter;
import com.jsoft.cocimpl.log.LogbackLogAdapter;
import com.jsoft.cocimpl.log.SystemLogAdapter;

public abstract class Logs {

	private static LogAdapter adapter;

	static {
		LogbackLogAdapter logback = new LogbackLogAdapter();
		if (logback.canWork()) {
			adapter = logback;
		} else {
			Log4jLogAdapter log4j = new Log4jLogAdapter();
			if (log4j.canWork()) {
				adapter = log4j;
			} else {
				JdkLogAdapter jdklog = new JdkLogAdapter();
				if (jdklog.canWork()) {
					adapter = jdklog;
				} else {
					SystemLogAdapter systemlog = new SystemLogAdapter();
					adapter = systemlog;
				}
			}
		}
	}

	public static Log get() {
		return adapter.getLogger(new Throwable().getStackTrace()[1].getClassName());
	}

	public static Log getLog(Class<?> clazz) {
		return getLog(clazz.getName());
	}

	public static Log getLog(String className) {
		return adapter.getLogger(className);
	}

	// public static void error(String msg) {
	//
	// }

}
