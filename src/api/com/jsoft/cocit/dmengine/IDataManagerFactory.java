package com.jsoft.cocit.dmengine;

import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.exception.CocException;

/**
 * 用于获取授权业务模块管理器。
 * 
 * @author yongshan.ji
 * 
 */
public interface IDataManagerFactory {

	public void release();

	public IDataManager getManager(ISystemMenuInfo systemMenu) throws CocException;

}
