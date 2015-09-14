package com.jsoft.cocit.orm.listener;

import com.jsoft.cocit.orm.IExtDao;
import com.jsoft.cocit.orm.mapping.EnMapping;

public interface EntityListener {
	void insertBefore(IExtDao dao, EnMapping mapping, Object entity);

	void insertAfter(IExtDao dao, EnMapping mapping, Object entity);

	void updateBefore(IExtDao dao, EnMapping mapping, Object entity);

	void updateAfter(IExtDao dao, EnMapping mapping, Object entity);

	void deleteBefore(IExtDao dao, EnMapping mapping, Object entity);

	void deleteAfter(IExtDao dao, EnMapping mapping, Object entity);
}
