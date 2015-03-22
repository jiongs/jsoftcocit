package com.jsoft.cocit.entityengine;

import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.exception.CocException;

/**
 * 用于获取授权业务模块管理器。
 * 
 * @author yongshan.ji
 * 
 */
public interface DataManagerFactory {

	public void release();

	public DataManager getManager(SystemMenuService systemMenu) throws CocException;

}
