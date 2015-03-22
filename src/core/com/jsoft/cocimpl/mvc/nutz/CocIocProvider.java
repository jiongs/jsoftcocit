package com.jsoft.cocimpl.mvc.nutz;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.IocProvider;
import org.nutz.mvc.NutConfig;

public class CocIocProvider implements IocProvider {

	
	public Ioc create(NutConfig config, String[] args) {
		return null;// (Ioc) Cocit.me().ioc.getIoc();
	}

}
