package com.jsoft.cocimpl.log;

import com.jsoft.cocit.log.Log;

public interface LogAdapter {

	Log getLogger(String className);

}