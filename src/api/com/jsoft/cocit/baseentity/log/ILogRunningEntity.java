package com.jsoft.cocit.baseentity.log;

import java.util.Date;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 *
 */
public interface ILogRunningEntity {

	String getLoginuser();

	String getFqnofctgrcls();

	String getLoggername();

	Date getDatetime();

	String getLevel();

	String getMessage();

	String getStacktrace();

	String getThreadname();

	String getNdc();

	String getLocationinfo();

	String getRemoteUrl();

	String getRemoteUri();

	String getRemoteIp();

	long getMemEslipse();

	long getEslipse();

	String getMonitor();

	String getRemoteAddress();

}
