package com.jsoft.cocit.dmengine.info;

import java.util.List;

import com.jsoft.cocit.baseentity.config.IDicEntity;

public interface IDicInfo extends INamedEntityInfo<IDicEntity>, IDicEntity {
	List<IDicItemInfo> getItems();
}
