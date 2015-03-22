package com.jsoft.cocit.entity.coc;

import com.jsoft.cocit.entity.IExtNamedEntity;

/**
 * 字段分组
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface IExtCocGroup extends IExtNamedEntity, ICocGroup {

	void setCocEntityKey(String entityKey);

	void setMode(String mode);
}
