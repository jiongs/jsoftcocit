package com.jsoft.cocit.dmengine.info;

import com.jsoft.cocit.baseentity.config.IDicItemEntity;

public interface IDicItemInfo extends INamedEntityInfo<IDicItemEntity>, IDicItemEntity {
	IDicInfo getDic();
}
