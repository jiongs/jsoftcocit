package com.jsoft.cocit.entity.log;

import com.jsoft.cocit.entity.IExtTenantOwnerEntity;

public interface IExtUploadLog extends IExtTenantOwnerEntity, IUploadLog {

	void setContentType(String contentType);

	void setExtName(String fileExtension);

	void setLocalName(String fileLocalName);

	void setContentLength(long length);

	void setFilePath(String path);

	public void setUploadFromModule(String moduleKey);

	public void setUploadFromField(String moduleKey);
	
	public void setLocalNamePinYin(String localNamePinYin);

}
