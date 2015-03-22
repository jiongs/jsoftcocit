package com.jsoft.cocimpl.orm.nutz.intercepter;

import java.lang.reflect.Method;

import org.nutz.aop.InterceptorChain;
import org.nutz.aop.interceptor.AbstractMethodInterceptor;
import org.nutz.dao.entity.Link;

import com.jsoft.cocit.orm.ExtDao;
import com.jsoft.cocit.util.LogUtil;

public abstract class LinkFieldGetterIntercepter extends AbstractMethodInterceptor {

	protected ExtDao dao;

	protected Link link;

	protected boolean lazy;

	public boolean whenError(Throwable e, Object obj, Method method, Object... args) {
		LogUtil.error("调用<" + obj.getClass().getSimpleName() + "." + method.getName() + ">时出现错误! ", e);
		return super.whenError(e, obj, method, args);
	}

	public boolean whenException(Exception e, Object obj, Method method, Object... args) {
		LogUtil.error("调用<" + obj.getClass().getSimpleName() + "." + method.getName() + ">时出现错误! ", e);
		return super.whenException(e, obj, method, args);
	}

	public void filter(InterceptorChain chain) throws Throwable {
		super.filter(chain);
	}
}
