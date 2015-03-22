package com.jsoft.cocit.service.impl;

import com.jsoft.cocit.service.IWorkflowTaskStatistics;

public class WorkflowTaskStatisticsImpl implements IWorkflowTaskStatistics {

	private int total;
	private int waiting;
	private int specialUrgent;
	private int urgent;
	private int expired;
	private int expire;
	private int hurry;
	private int attention;
	private int reading;
	private int authorize;
	private String linkUrl;

	WorkflowTaskStatisticsImpl() {

	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getWaiting() {
		return waiting;
	}

	public void setWaiting(int waiting) {
		this.waiting = waiting;
	}

	public int getSpecialUrgent() {
		return specialUrgent;
	}

	public void setSpecialUrgent(int specialUrgent) {
		this.specialUrgent = specialUrgent;
	}

	public int getUrgent() {
		return urgent;
	}

	public void setUrgent(int urgent) {
		this.urgent = urgent;
	}

	public int getExpired() {
		return expired;
	}

	public void setExpired(int expired) {
		this.expired = expired;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public int getHurry() {
		return hurry;
	}

	public void setHurry(int hurry) {
		this.hurry = hurry;
	}

	public int getAttention() {
		return attention;
	}

	public void setAttention(int attention) {
		this.attention = attention;
	}

	public int getReading() {
		return reading;
	}

	public void setReading(int reading) {
		this.reading = reading;
	}

	public int getAuthorize() {
		return authorize;
	}

	public void setAuthorize(int authorize) {
		this.authorize = authorize;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

}
