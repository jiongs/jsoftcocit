package com.jsoft.cocimpl.ioc.aop;

import org.nutz.aop.ClassAgent;

public interface LazyClassAgent extends ClassAgent {

	String LAZY_CLASSNAME_SUFFIX = "$$LAZY" + CLASSNAME_SUFFIX;

}
