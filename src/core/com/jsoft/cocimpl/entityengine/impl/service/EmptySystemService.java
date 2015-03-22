package com.jsoft.cocimpl.entityengine.impl.service;

import com.jsoft.cocit.entity.security.ISystem;

public class EmptySystemService extends SystemServiceImpl {

	public EmptySystemService(ISystem obj) {
		super(obj);
	}

	public String getName() {
		return "";
	}

}