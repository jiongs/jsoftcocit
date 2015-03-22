package com.jsoft.cocit.service.impl;

import java.util.Date;

import com.jsoft.cocit.service.IWorkflowTask;

public class WorkflowTaskImpl implements IWorkflowTask {

	private String title;
	private String linkUrl;
	private Date taskDate;

	private WorkflowTaskImpl(String title, String linkUrl, Date date) {
		this.title = title;
		this.linkUrl = linkUrl;
		this.taskDate = date;
	}

	static WorkflowTaskImpl make(String title, String linkUrl, Date date) {
		return new WorkflowTaskImpl(title, linkUrl, date);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Date getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

}
