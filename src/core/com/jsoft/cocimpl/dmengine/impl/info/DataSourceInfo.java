package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.Properties;

import com.jsoft.cocit.baseentity.config.IDataSourceEntity;
import com.jsoft.cocit.dmengine.info.IDataSourceInfo;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class DataSourceInfo extends NamedEntityInfo<IDataSourceEntity> implements IDataSourceInfo {

	DataSourceInfo(IDataSourceEntity obj) {
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
