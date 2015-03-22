package com.jsoft.cocimpl.orm.nutz.intercepter;

import java.lang.reflect.Method;
import java.util.List;

import org.nutz.dao.entity.Link;
import org.nutz.lang.Mirror;

import com.jsoft.cocit.orm.ExtDao;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.ObjectUtil;

public class ManyFieldGetterIntercepter extends LinkFieldGetterIntercepter {

	public ManyFieldGetterIntercepter(ExtDao dao, Link link) {
		this.dao = dao;
		this.link = link;
		// FetchType fetch = (FetchType) link.get("fetch");
		this.lazy = true;// FetchType.LAZY == fetch;
	}

	public Object afterInvoke(Object obj, Object returnObj, Method method, Object... args) {
		if (lazy && returnObj == null) {
			String name = link.getOwnField().getName();
			Link mappedLink = (Link) link.get("mappedLink");
			String mappedBy = null;
			if (mappedLink != null) {
				mappedBy = mappedLink.getOwnField().getName();
			}
			dao.fetchLinks(obj, name);
			Mirror me = Mirror.me(obj.getClass());
			try {
				returnObj = me.getValue(obj, me.getField(name));
			} catch (Throwable e) {
				LogUtil.warn("ManyFieldGetterIntercepter.afterInvoke: Warn! %s", ExceptionUtil.msg(e));
			}
			if (mappedBy != null && returnObj != null) {
				if (returnObj instanceof List) {
					List list = (List) returnObj;
					for (Object elm : list) {
						ObjectUtil.setValue(elm, mappedBy, obj);
					}
				} else {
					ObjectUtil.setValue(returnObj, mappedBy, obj);
				}
			}
		}

		return returnObj;
	}
}
