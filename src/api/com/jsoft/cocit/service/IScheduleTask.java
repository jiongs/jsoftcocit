package com.jsoft.cocit.service;

import java.util.Date;

/**
 * 计划日程：
 * 
 * @author Ji Yongshan
 * 
 */
public interface IScheduleTask {

	/**
	 * 获取“计划任务”标题
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * 获取“计划任务”链接地址
	 * 
	 * @return
	 */
	public String getLinkUrl();

	/**
	 * 获取“计划任务”时间
	 * 
	 * @return
	 */
	public Date getTaskDate();

}
