package com.jsoft.cocit.baseentity.coc;

import com.jsoft.cocit.baseentity.INamedEntityExt;

/**
 * 字段分组
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface ICocGroupEntityExt extends INamedEntityExt, ICocGroupEntity {

	void setCocEntityCode(String entityCode);

	void setMode(String mode);
}
