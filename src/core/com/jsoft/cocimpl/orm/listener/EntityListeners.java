package com.jsoft.cocimpl.orm.listener;

import java.util.List;

import com.jsoft.cocit.orm.ExtDao;
import com.jsoft.cocit.orm.listener.EntityListener;
import com.jsoft.cocit.orm.mapping.EnMapping;

public class EntityListeners {

	private List<EntityListener> listeners;

	public EntityListeners() {
	}

	public EntityListeners(List<EntityListener> l) {
		this.listeners = l;
	}

	public List<EntityListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<EntityListener> listeners) {
		this.listeners = listeners;
	}

	public void insertBefore(ExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.insertBefore(dao, entity, obj);
			}
		}
	}

	public void insertAfter(ExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.insertAfter(dao, entity, obj);
			}
		}
	}

	public void updateBefore(ExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.updateBefore(dao, entity, obj);
			}
		}
	}

	public void updateAfter(ExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.updateAfter(dao, entity, obj);
			}
		}
	}

	public void deleteBefore(ExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.deleteBefore(dao, entity, obj);
			}
		}
	}

	public void deleteAfter(ExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.deleteAfter(dao, entity, obj);
			}
		}
	}
}
