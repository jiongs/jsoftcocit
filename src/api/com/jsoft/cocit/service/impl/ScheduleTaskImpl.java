package com.jsoft.cocit.service.impl;

import java.util.Date;

import com.jsoft.cocit.service.IScheduleTask;

public class ScheduleTaskImpl implements IScheduleTask {
	private String title;
	private String linkUrl;
	private Date taskDate;

	static ScheduleTaskImpl make(String title, String linkUrl, Date taskdate) {
		return new ScheduleTaskImpl(title, linkUrl, taskdate);
	}

	private ScheduleTaskImpl(String title, String linkUrl, Date taskdate) {
		this.title = title;
		this.taskDate = taskdate;
		this.linkUrl = linkUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

}
