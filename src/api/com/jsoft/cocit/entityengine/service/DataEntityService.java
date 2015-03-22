package com.jsoft.cocit.entityengine.service;

import com.jsoft.cocit.entity.IDataEntity;

/**
 * 实体服务类：服务于某个特定的实体对象
 * 
 * @author jiongs753
 * 
 */
public interface DataEntityService<T extends IDataEntity> extends IDataEntity {

	public T getEntityData();

}
