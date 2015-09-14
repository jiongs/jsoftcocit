package com.jsoft.cocit.securityengine;

import com.jsoft.cocit.baseentity.log.ILogLoginEntity;
import com.jsoft.cocit.dmengine.info.IUserInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.dmengine.info.ITenantInfo;
import com.jsoft.cocit.util.Tree;

/**
 * 登录信息： 描述客户端用户登录信息
 * 
 * @author yongshan.ji
 */
public interface ILoginSession extends Authentication {
	void release();

	ITenantInfo getTenant();

	ISystemInfo getSystem();

	/**
	 * 获取登录信息中验证成功的用户实体
	 * 
	 * @return 用户实体对象，不能返回空值。
	 */
	IUserInfo getUser();

	int getUserType();

	/**
	 * 获取登录信息中的用户帐号
	 * 
	 * @return
	 */
	String getUsername();

	/**
	 * 获取登录信息中的用户缓存对象
	 * 
	 * @param <T>
	 * @param key
	 * @return
	 */
	<T> T get(String key);

	/**
	 * 设置用户缓存对象到登录信息中
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	ILoginSession set(String key, Object value);

	/**
	 * 获取客户端浏览器宽度
	 * 
	 * @return
	 */
	double getBrowserWidth();

	/**
	 * 获取客户端浏览器高度
	 * 
	 * @return
	 */
	double getBrowserHeight();

	String getLoginLogCode();

	// /**
	// * 获取浏览器右边——内容区域的宽度
	// *
	// * @return
	// */
	// double getBodyWidth();

	public Tree makeFuncMenu(String urlPrefix);

	public ILogLoginEntity getLoginLog();

	void setLoginLog(ILogLoginEntity loginLog);

	String getAppURL();

	/**
	 * 为用户添加临时权限
	 */
	void putAccessVoter(String voterCode, SecurityVoter voter);

	SecurityVoter removeAccessVoter(String voterCode);

}
