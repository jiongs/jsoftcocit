package com.jsoft.cocit.service.impl;

import java.util.Date;

import com.jsoft.cocit.service.INoticeMessage;

public class NoticeMessageImpl implements INoticeMessage {

	private String title;
	private String linkUrl;
	private Date publishDate;

	private NoticeMessageImpl(String title, String linkUrl, Date date) {
		this.title = title;
		this.linkUrl = linkUrl;
		this.publishDate = date;
	}

	static NoticeMessageImpl make(String title, String linkUrl, Date date) {
		return new NoticeMessageImpl(title, linkUrl, date);
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

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

}
