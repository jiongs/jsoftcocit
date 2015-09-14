package com.jsoft.cocit.baseentity;

import java.util.Date;


/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IDataEntityExt extends IDataEntity {

	void setId(Long id);

	void setCode(String code);

	void setVersion(Integer version);

	void setCreatedDate(Date date);

	void setCreatedUser(String user);

	void setCreatedOpLog(String loginLog);

	void setUpdatedDate(Date date);

	void setUpdatedUser(String user);

	void setUpdatedOpLog(String loginLog);

	void setStatusCode(int statusCode);

}
