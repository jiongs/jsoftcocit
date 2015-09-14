package com.jsoft.cocit.baseentity.log;

import java.util.Date;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface ILogLoginEntity {
	String getCode();

	Date getLoginDate();

	String getLoginSystem();

	String getLoginUser();
}
