package com.jsoft.cocit.dmengine.info;

import com.jsoft.cocit.baseentity.IDataEntity;

/**
 * 实体服务类：服务于某个特定的实体对象
 * 
 * @author jiongs753
 * 
 */
public interface IDataEntityInfo<T extends IDataEntity> extends IDataEntity {

	public T getEntityData();

}
