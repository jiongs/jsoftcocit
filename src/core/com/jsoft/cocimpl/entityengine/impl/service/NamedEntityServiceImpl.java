package com.jsoft.cocimpl.entityengine.impl.service;

import com.jsoft.cocit.entity.INamedEntity;
import com.jsoft.cocit.entityengine.service.NamedEntityService;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public abstract class NamedEntityServiceImpl<T extends INamedEntity> extends DataEntityServiceImpl<T> implements NamedEntityService<T>, INamedEntity {

	protected NamedEntityServiceImpl(T entity) {
		super(entity);
	}

	public String getName() {
		return entityData.getName();
	}

	public Integer getSn() {
		return entityData.getSn();
	}
}
