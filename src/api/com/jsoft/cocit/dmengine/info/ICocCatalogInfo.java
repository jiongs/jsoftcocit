package com.jsoft.cocit.dmengine.info;

import com.jsoft.cocit.baseentity.coc.ICocCatalogEntity;

public interface ICocCatalogInfo extends INamedEntityInfo<ICocCatalogEntity>, ICocCatalogEntity {
	public ICocCatalogInfo getParentCatalog();
}
