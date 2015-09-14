package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.Date;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.baseentity.IDataEntity;
import com.jsoft.cocit.dmengine.IDataModelEngine;
import com.jsoft.cocit.dmengine.IDataManagerFactory;
import com.jsoft.cocit.dmengine.IEntityInfoFactory;
import com.jsoft.cocit.dmengine.info.IDataEntityInfo;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.securityengine.SecurityService;

public abstract class DataEntityInfo<T extends IDataEntity> implements IDataEntityInfo<T> {
	protected T entityData;

	protected DataEntityInfo(T entity) {
		this.entityData = entity;
	}

	protected IOrm orm() {
		return Cocit.me().orm();
	}

	protected IDataModelEngine ee() {
		return Cocit.me().getEntityEngine();
	}

	protected SecurityService se() {
		return Cocit.me().getSecurityEngine();
	}

	protected IDataManagerFactory getDataManagerFactory() {
		return Cocit.me().getDataManagerFactory();
	}

	protected IEntityInfoFactory getEntityInfoFactory() {
		return Cocit.me().getEntityServiceFactory();
	}

	@Override
	public void release() {
		// if (entityData != null) {
		// entityData.release();
		//
		// this.entityData = null;
		// }
	}

	public T getEntityData() {
		return entityData;
	}

	public Long getId() {
		return entityData.getId();
	}

	public String getCode() {
		return entityData.getCode();
	}

	public Integer getVersion() {
		return entityData.getVersion();
	}

	public Date getCreatedDate() {
		return entityData.getCreatedDate();
	}

	public String getCreatedUser() {
		return entityData.getCreatedUser();
	}

	public String getCreatedOpLog() {
		return entityData.getCreatedOpLog();
	}

	public Date getUpdatedDate() {
		return entityData.getUpdatedDate();
	}

	public String getUpdatedUser() {
		return entityData.getUpdatedUser();
	}

	public String getUpdatedOpLog() {
		return entityData.getUpdatedOpLog();
	}

	public int getStatusCode() {
		return entityData.getStatusCode();
	}

	public boolean isBuildin() {
		return entityData.isBuildin();
	}

	public boolean isDisabled() {
		return entityData.isDisabled();
	}

	public boolean isRemoved() {
		return entityData.isRemoved();
	}

	public boolean isArchived() {
		return entityData.isArchived();
	}

	public String toString() {
		return entityData.toString();
	}

	public String toJsonString() {
		return entityData.toString();
	}
}
