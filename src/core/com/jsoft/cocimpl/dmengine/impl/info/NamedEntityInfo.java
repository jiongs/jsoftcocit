package com.jsoft.cocimpl.dmengine.impl.info;

import java.io.Serializable;

import com.jsoft.cocit.baseentity.INamedEntity;
import com.jsoft.cocit.dmengine.info.INamedEntityInfo;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public abstract class NamedEntityInfo<T extends INamedEntity> extends DataEntityInfo<T> implements INamedEntityInfo<T>, INamedEntity, Serializable {

	protected NamedEntityInfo(T entity) {
		super(entity);
	}

	public String getName() {
		return entityData.getName();
	}
	

	public Integer getSn() {
		return entityData.getSn();
	}

	@Override
	public String getNamePinyin() {
		return entityData.getNamePinyin();
	}

	@Override
	public String getNameAbbr() {
		return entityData.getNameAbbr();
	}

	@Override
	public String getNameAbbrPinyin() {
		return entityData.getNameAbbrPinyin();
	}
}
