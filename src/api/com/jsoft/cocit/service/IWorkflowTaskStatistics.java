package com.jsoft.cocit.service;

/**
 * 流程任务统计：
 * 
 * @author Ji Yongshan
 * 
 */
public interface IWorkflowTaskStatistics {

	/**
	 * 链接地址前缀：地址 + type 即为真正的链接地址
	 * <p>
	 * 任务类型：如“0-全部、1-待办、2-催办、3-特急办件、4-加急件、5-过期件、6-到期件、7-委托件、8-待阅件、9-关注”
	 * 
	 * @return
	 */
	String getLinkUrl();

	/**
	 * 总共：
	 * 
	 * @return
	 */
	int getTotal();

	/**
	 * 待办：
	 * 
	 * @return
	 */
	int getWaiting();

	/**
	 * 特急：
	 * 
	 * @return
	 */
	int getSpecialUrgent();

	/**
	 * 加急：
	 * 
	 * @return
	 */
	int getUrgent();

	/**
	 * 过期：
	 * 
	 * @return
	 */
	int getExpired();

	/**
	 * 到期：
	 * 
	 * @return
	 */
	int getExpire();

	/**
	 * 催办：
	 * 
	 * @return
	 */
	int getHurry();

	/**
	 * 关注：
	 * 
	 * @return
	 */
	int getAttention();

	/**
	 * 待阅：等待阅览
	 * 
	 * @return
	 */
	int getReading();

	/**
	 * 委托：别人委托我办理的任务
	 * 
	 * @return
	 */
	int getAuthorize();
}
