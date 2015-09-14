// $codepro.audit.disable unnecessaryCast
package com.jsoft.cocimpl.dmengine.impl.info;

import com.jsoft.cocit.baseentity.coc.ICocCatalogEntity;
import com.jsoft.cocit.dmengine.info.ICocCatalogInfo;

public class CocCatalogInfo extends NamedEntityInfo<ICocCatalogEntity> implements ICocCatalogInfo {

	CocCatalogInfo(ICocCatalogEntity obj) {
		super(obj);
	}

	@Override
	public void release() {
		super.release();
	}

	@Override
	public String getPrevInterceptors() {
		return entityData.getPrevInterceptors();
	}

	@Override
	public String getPostInterceptors() {
		return entityData.getPostInterceptors();
	}

	@Override
	public String getParentCode() {
		return entityData.getParentCode();
	}

	@Override
	public ICocCatalogInfo getParentCatalog() {
		return this.getEntityInfoFactory().getCocCatalog(this.getParentCode());
	}
}
