package com.jsoft.cocit.baseentity.common;

import com.jsoft.cocit.baseentity.IOfTenantEntityExt;

public interface IFileInfoEntityExt extends IOfTenantEntityExt, IFileInfoEntity {

	void setContentType(String contentType);

	void setExtName(String fileExtension);

	void setContentLength(long length);

	void setPath(String path);

	void setName(String fileLocalName);

	void setNamePinyin(String pinyinFirstChar);

	void setCatalogCode(String moduleId);

	void setCatalog2Code(String fieldId);

}
