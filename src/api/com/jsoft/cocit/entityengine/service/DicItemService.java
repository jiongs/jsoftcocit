package com.jsoft.cocit.entityengine.service;

import com.jsoft.cocit.entity.config.IDicItem;

public interface DicItemService extends NamedEntityService<IDicItem>, IDicItem {
	DicService getDic();
}
