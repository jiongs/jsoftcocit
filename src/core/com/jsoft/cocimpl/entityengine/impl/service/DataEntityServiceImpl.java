package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.Date;

import com.jsoft.cocimpl.entityengine.EntityServiceFactory;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.entity.IDataEntity;
import com.jsoft.cocit.entityengine.DataManagerFactory;
import com.jsoft.cocit.entityengine.EntityEngine;
import com.jsoft.cocit.entityengine.service.DataEntityService;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.securityengine.SecurityEngine;

public abstract class DataEntityServiceImpl<T extends IDataEntity> implements DataEntityService<T> {
	protected T entityData;

	protected DataEntityServiceImpl(T entity) {
		this.entityData = entity;
	}

	protected Orm orm() {
		return Cocit.me().getProxiedORM();
	}

	protected EntityEngine ee() {
		return Cocit.me().getEntityEngine();
	}

	protected SecurityEngine se() {
		return Cocit.me().getSecurityEngine();
	}

	protected DataManagerFactory dmf() {
		return Cocit.me().getDataManagerFactory();
	}

	protected EntityServiceFactory esf() {
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

	public String getKey() {
		return entityData.getKey();
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

	public String toJson() {
		return entityData.toString();
	}
}
