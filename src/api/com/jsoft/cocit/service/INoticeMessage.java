package com.jsoft.cocit.service;

import java.util.Date;

/**
 * 通知公告消息：
 * 
 * @author Ji Yongshan
 * 
 */
public interface INoticeMessage {

	/**
	 * 获取“通知公告消息”标题
	 * 
	 * @return
	 */
	public String getTitle();

	/**
	 * 获取“通知公告消息”链接地址
	 * 
	 * @return
	 */
	public String getLinkUrl();

	public Date getPublishDate();
}
