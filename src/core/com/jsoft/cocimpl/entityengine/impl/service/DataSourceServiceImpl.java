package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.Properties;

import com.jsoft.cocit.entity.config.IDataSource;
import com.jsoft.cocit.entityengine.service.DataSourceService;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class DataSourceServiceImpl extends NamedEntityServiceImpl<IDataSource> implements DataSourceService {

	DataSourceServiceImpl(IDataSource obj) {
		super(obj);
	}

	public String getUrl() {
		return entityData.getUrl();
	}

	public String getDriver() {
		return entityData.getDriver();
	}

	public String getUser() {
		return entityData.getUser();
	}

	public String getPassword() {
		return entityData.getPassword();
	}

	public Properties getProperties() {
		return entityData.getProperties();
	}

}
