package com.jsoft.cocimpl.orm.listener;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocimpl.orm.listener.impl.CocEntityListener;
import com.jsoft.cocit.orm.IExtDao;
import com.jsoft.cocit.orm.listener.EntityListener;
import com.jsoft.cocit.orm.mapping.EnMapping;

public class EntityListeners {

	private List<EntityListener> listeners;

	private static EntityListeners me;

	private EntityListeners() {
		listeners = new ArrayList();
		listeners.add(new CocEntityListener());
	}

	public static EntityListeners make() {
		if (me == null) {
			me = new EntityListeners();
		}
		return me;
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

	public void insertBefore(IExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.insertBefore(dao, entity, obj);
			}
		}
	}

	public void insertAfter(IExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.insertAfter(dao, entity, obj);
			}
		}
	}

	public void updateBefore(IExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.updateBefore(dao, entity, obj);
			}
		}
	}

	public void updateAfter(IExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.updateAfter(dao, entity, obj);
			}
		}
	}

	public void deleteBefore(IExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.deleteBefore(dao, entity, obj);
			}
		}
	}

	public void deleteAfter(IExtDao dao, EnMapping entity, Object obj) {
		if (listeners != null) {
			for (EntityListener l : listeners) {
				l.deleteAfter(dao, entity, obj);
			}
		}
	}
}
