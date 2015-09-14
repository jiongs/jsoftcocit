package com.jsoft.cocimpl.dmengine.impl.info;

import com.jsoft.cocit.baseentity.security.ISystemEntity;

public class EmptySystemInfo extends SystemInfo {

	public EmptySystemInfo(ISystemEntity obj) {
		super(obj);
	}

	public String getName() {
		return "";
	}

}