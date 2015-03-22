package com.jsoft.cocit.entityengine.service;

import java.util.List;

import com.jsoft.cocit.entity.config.IDic;

public interface DicService extends NamedEntityService<IDic>, IDic {
	List<DicItemService> getItems();
}
