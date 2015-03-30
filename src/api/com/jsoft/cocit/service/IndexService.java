package com.jsoft.cocit.service;

import java.util.Date;
import java.util.List;

import com.jsoft.cocit.util.Option;

/**
 * 首页管理器：用来管理后台首页需要的数据
 * 
 * @author Ji Yongshan
 * 
 */
public interface IndexService {

	/**
	 * 获取“公开”公告消息
	 * 
	 * @return
	 */
	List<? extends INoticeMessage> getPublicMessages(int pageSize);

	/**
	 * 获取“我的”消息
	 * 
	 * @param user
	 *            当前用户登录帐号
	 * @return
	 */
	List<? extends INoticeMessage> getMyMessages(int pageSize, String user);

	/**
	 * 获取“我的”计划日程
	 * 
	 * @param user
	 *            当前用户登录帐号
	 * @param dateFrom
	 *            计划起始时间
	 * @param dateTo
	 *            计划截止时间
	 * @return
	 */
	List<? extends IScheduleTask> getMyScheduleTasks(int pageSize, String user, Date dateFrom, Date dateTo);

	/**
	 * 获取“领导”计划日程
	 * 
	 * @param user
	 *            当前用户登录帐号
	 * @param dateFrom
	 *            计划起始时间
	 * @param dateTo
	 *            计划截止时间
	 * @return
	 */
	List<? extends IScheduleTask> getLeaderScheduleTasks(int pageSize, String user, Date dateFrom, Date dateTo);

	/**
	 * 获取“部门”计划日程
	 * 
	 * @param user
	 *            当前用户登录帐号
	 * @param dateFrom
	 *            计划起始时间
	 * @param dateTo
	 *            计划截止时间
	 * @return
	 */
	List<? extends IScheduleTask> getDepartmentScheduleTasks(int pageSize, String user, Date dateFrom, Date dateTo);

	/**
	 * 获取“我的”流程任务数量
	 * 
	 * @param user
	 *            当前用户登录帐号
	 * @return
	 */
	IWorkflowTaskStatistics getMyWorkflowTaskStatistics(String user);

	/**
	 * 获取“我的”流程任务
	 * 
	 * @param user
	 *            当前用户登录帐号
	 * @param type
	 *            任务级别：如“0-全部、1-待办、2-催办、3-特急办件、4-加急件、5-过期件、6-到期件、7-委托件、8-待阅件、9-关注”
	 * @return
	 */
	List<? extends IWorkflowTask> getMyWorkflowTasks(int pageSize, String user, int type);

	/**
	 * 获取“搜索分类”
	 * 
	 * @return
	 */
	List<Option> getSearchCategories(int pageSize);

	/**
	 * 获取“搜索关键字”
	 * 
	 * @return
	 */
	List<? extends ISearchKeyword> getSearchKeywords(int pageSize);

	// /**
	// * 获取“应用中心”功能菜单
	// *
	// * @param user
	// * 当前用户登录帐号
	// * @return
	// */
	// List<ISystemMenu> getAppCenterMenus(String user);
}
