package com.jsoft.cocit.service;

import java.util.Date;

/**
 * 流程任务：
 * 
 * @author Ji Yongshan
 * 
 */
public interface IWorkflowTask {

	/**
	 * 获取“流程任务”标题
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * 获取“流程任务”链接地址
	 * 
	 * @return
	 */
	public String getLinkUrl();

	public Date getTaskDate();
}
