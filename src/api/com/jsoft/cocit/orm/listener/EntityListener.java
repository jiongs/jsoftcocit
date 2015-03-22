package com.jsoft.cocit.orm.listener;

import com.jsoft.cocit.orm.ExtDao;
import com.jsoft.cocit.orm.mapping.EnMapping;

public interface EntityListener {
	void insertBefore(ExtDao dao, EnMapping mapping, Object entity);

	void insertAfter(ExtDao dao, EnMapping mapping, Object entity);

	void updateBefore(ExtDao dao, EnMapping mapping, Object entity);

	void updateAfter(ExtDao dao, EnMapping mapping, Object entity);

	void deleteBefore(ExtDao dao, EnMapping mapping, Object entity);

	void deleteAfter(ExtDao dao, EnMapping mapping, Object entity);
}
