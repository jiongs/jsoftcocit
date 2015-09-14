package com.jsoft.cocit.dmengine.info;

import com.jsoft.cocit.baseentity.INamedEntity;

/**
 * 实体服务类：服务于某个特定的实体对象
 * 
 * @author jiongs753
 * 
 */
public interface INamedEntityInfo<T extends INamedEntity> extends IDataEntityInfo<T>, INamedEntity {

	public T getEntityData();

}
