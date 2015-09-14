package com.jsoft.cocimpl.orm.nutz.intercepter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.nutz.dao.entity.Link;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.orm.IExtDao;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ObjectUtil;

public class OneFieldGetterIntercepter extends LinkFieldGetterIntercepter {

	public OneFieldGetterIntercepter(IExtDao dao, Link link) {
		this.dao = dao;
		this.link = link;
		// FetchType fetch = (FetchType) link.get("fetch");
		this.lazy = true;// FetchType.LAZY == fetch;
	}

	public Object afterInvoke(Object obj, Object returnValue, Method method, Object... args) {
		if (lazy && ClassUtil.isLazy(returnValue)) {
			Link mappedLink = (Link) link.get("mappedLink");

			String fkField = link.getOwnField().getName();
			Field targetField = link.getTargetField();
			String fkTargetField = targetField.getName();
			Class targetClass = returnValue.getClass();
			String mappedBy = null;
			if (mappedLink != null) {
				mappedBy = mappedLink.getOwnField().getName();
			}

			Serializable id = ObjectUtil.getValue(returnValue, fkTargetField);
			if (id != null) {
				returnValue = Cocit.me().orm().get(targetClass, Expr.eq(fkTargetField, id));
			}

			ObjectUtil.setValue(obj, fkField, returnValue);

			if (mappedBy != null && returnValue != null) {
				if (!Collection.class.isAssignableFrom(targetClass)) {
					ObjectUtil.setValue(returnValue, mappedBy, obj);
				}
			}
		}

		return returnValue;
	}
}
