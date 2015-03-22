package com.jsoft.cocit.entity.security;

/**
 * <b>系统用户: </b> 即可以使用COC平台系统的人。
 * <p>
 * <b>用户可以有一下几种方式使用Cocit提供的功能</b>
 * <p>
 * 1. 任何人均可使用授权给特殊功能角色（任何人）的功能模块
 * <p>
 * 2. 拥有特殊功能角色（超级用户）的人可以使用Cocit提供的所有功能模块
 * <p>
 * 3. 上述两种以外的人可以根据其拥有的功能角色来使用对应的功能模块
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface ISystemUser extends IUser {

	void setUsername(String username);

	void setPassword(String pass);

	void setRawPassword(String rawPwd);

	void setRawPassword2(String rawPassword2);

}
