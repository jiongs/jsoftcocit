package com.jsoft.cocimpl.orm.nutz;

import org.nutz.dao.entity.Link;
import org.nutz.dao.impl.CocInsertInvoker;
import org.nutz.lang.Mirror;

import com.jsoft.cocit.orm.Dao;
import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.util.ObjectUtil;

public class SaveOneInvoker extends CocInsertInvoker {

	public SaveOneInvoker(Dao dao, Object mainObj, Mirror<?> mirror) {
		super(dao, mainObj, mirror);
	}

	public void invoke(Link link, Object one) {
		if (ObjectUtil.isEmpty((EnMapping) dao.getEntity(one.getClass()), one)) {
			dao.insert(one);
		}
		// Mirror<?> ta = Mirror.me(one.getClass());
		// Object value = ta.getValue(one, link.getTargetField());
		// mirror.setValue(mainObj, link.getReferField(), value);
	}
}
