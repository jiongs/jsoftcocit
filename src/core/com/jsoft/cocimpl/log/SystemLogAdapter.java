package com.jsoft.cocimpl.log;

import com.jsoft.cocit.log.Log;


public class SystemLogAdapter implements LogAdapter {

	public Log getLogger(String className) {
		return SystemLog.me();
	}

	public boolean canWork() {
		return true;
	}

}
