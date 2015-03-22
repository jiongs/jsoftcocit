package com.jsoft.cocimpl.ioc.impl;

import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import com.jsoft.cocimpl.ioc.IocProxy;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.LogUtil;

public class IocProxyImpl implements IocProxy {

	private NutIoc ioc;

	public IocProxy init(String... paths) {
		ioc = new NutIoc(new JsonLoader(paths));
		return this;
	}

	public Object get(String name) {
		try {
			return ioc.get(null, name);
		} catch (NullPointerException npe) {
			return null;
		} catch (Throwable ex) {
			LogUtil.error("获取BEAN<%s>失败! 错误信息： %s", name, ExceptionUtil.msg(ex));
			return null;
		}
	}

	public Object getIoc() {
		return ioc;
	}
}
