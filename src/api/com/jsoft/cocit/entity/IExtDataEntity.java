package com.jsoft.cocit.entity;

import java.util.Date;


/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IExtDataEntity extends IDataEntity {

	void setId(Long id);

	void setKey(String key);

	void setVersion(Integer version);

	void setCreatedDate(Date date);

	void setCreatedUser(String user);

	void setCreatedOpLog(String loginLog);

	void setUpdatedDate(Date date);

	void setUpdatedUser(String user);

	void setUpdatedOpLog(String loginLog);

	void setStatusCode(int statusCode);

}
