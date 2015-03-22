package com.jsoft.cocit.entityengine.service;

import com.jsoft.cocit.entity.INamedEntity;

/**
 * 实体服务类：服务于某个特定的实体对象
 * 
 * @author jiongs753
 * 
 */
public interface NamedEntityService<T extends INamedEntity> extends DataEntityService<T>, INamedEntity {

	public T getEntityData();

}
