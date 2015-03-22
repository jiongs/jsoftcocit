package com.jsoft.cocimpl.orm.nutz;

import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.util.ClassUtil;

/**
 * 实体管理器：
 * <p>
 * 1. 通过实体类代理来管理实体
 * <p>
 * 
 * @TODO 2. 长时间不用的实体将被自动清除
 * @author yongshan.ji
 */
public class EnMappingHolder {

	private Map<Class<?>, EnMapping<?>> mappings;// 实体映射<AOP代理类,实体>

	private Map<Class, Class> agents;// 实体代理<实体类，AOP代理类>

	private static EnMappingHolder me;

	public static EnMappingHolder make() {
		if (me == null) {
			me = new EnMappingHolder();
		}

		return me;
	}

	private EnMappingHolder() {
		mappings = new HashMap<Class<?>, EnMapping<?>>();
		agents = new HashMap<Class, Class>();
	}

	public void clear() {
		// for (EnMapping obj : mappings.values()) {
		// synchronized (obj) {
		// obj.release();
		// }
		// }
		mappings.clear();
		agents.clear();
	}

	// public void remove(EnMappingImpl entity) {
	// Class classOfT = ClassUtil.getType(entity.getType());
	//
	// Class agentType = agents.get(classOfT);
	// agents.remove(classOfT);
	//
	// if (agentType != null) {
	// mappings.remove(agentType);
	// }
	// }

	public void remove(Class classOfT) {
		classOfT = ClassUtil.getType(classOfT);

		Class agentType = agents.get(classOfT);
		agents.remove(classOfT);

		if (agentType != null) {
			EnMappingImpl entity = (EnMappingImpl) mappings.get(agentType);
			mappings.remove(agentType);

			entity.release();
		}
	}

	public void cacheEntity(Class agentClass, EnMapping entity) {
		mappings.put(agentClass, entity);
	}

	public void cacheAgent(Class classOfT, Class agentClass) {
		agents.put(classOfT, agentClass);
	}

	public <T> EnMappingImpl<T> getEnMapping(Class<T> classOfT) {
		if (ClassUtil.isLazy(classOfT)) {
			return (EnMappingImpl<T>) getEnMapping(classOfT.getSuperclass());
		}
		if (!ClassUtil.isAgent(classOfT)) {
			classOfT = agents.get(classOfT);
		}

		if (classOfT == null)
			return null;
		else
			return (EnMappingImpl<T>) mappings.get(classOfT);
	}

	public int countEntity() {
		return mappings.size();
	}
}
