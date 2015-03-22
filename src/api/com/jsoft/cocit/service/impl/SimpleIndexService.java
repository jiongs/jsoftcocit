package com.jsoft.cocit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.jsoft.cocit.service.INoticeMessage;
import com.jsoft.cocit.service.IScheduleTask;
import com.jsoft.cocit.service.ISearchKeyword;
import com.jsoft.cocit.service.IWorkflowTask;
import com.jsoft.cocit.service.IWorkflowTaskStatistics;
import com.jsoft.cocit.service.IndexService;
import com.jsoft.cocit.util.Option;

public class SimpleIndexService implements IndexService {

	@SuppressWarnings("deprecation")
	private Date getDate() {
		int yy = new Random().nextInt(2015);
		yy = 2015 - 1900;
		int MM = new Random().nextInt(12);
		MM = MM % 2;
		int dd = new Random().nextInt(31);
		dd = dd % 31;
		int HH = new Random().nextInt(24);
		HH = HH % 24;
		int mm = new Random().nextInt(60);
		mm = mm % 60;
		int ss = new Random().nextInt(60);
		ss = ss % 60;

		return new Date(yy, MM, dd, HH, mm, ss);
	}

	@Override
	public List<INoticeMessage> getPublicMessages(int pageSize) {
		List<INoticeMessage> list = new ArrayList();

		list.add(NoticeMessageImpl.make("公司人员变动公告", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年春节放假通知", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年稷恒安年终总结会通知", "", getDate()));
		list.add(NoticeMessageImpl.make("本周三自由健身运动地点改变", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站您有3个BUG未解决", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));

		List<INoticeMessage> ret = new ArrayList();
		for (int i = 0; i < pageSize; i++) {
			ret.add(list.get(i));
		}

		return ret;
	}

	@Override
	public List<INoticeMessage> getMyMessages(int pageSize, String user) {
		List<INoticeMessage> list = new ArrayList();

		list.add(NoticeMessageImpl.make("公司人员变动公告", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年春节放假通知", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年稷恒安年终总结会通知", "", getDate()));
		list.add(NoticeMessageImpl.make("本周三自由健身运动地点改变", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站您有3个BUG未解决", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站您有3个BUG未解决", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("检验所网站你有新的bug", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(NoticeMessageImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));

		List<INoticeMessage> ret = new ArrayList();
		for (int i = 0; i < pageSize; i++) {
			ret.add(list.get(i));
		}

		return ret;
	}

	@Override
	public List<IScheduleTask> getMyScheduleTasks(int pageSize, String user, Date dateFrom, Date dateTo) {
		List<IScheduleTask> list = new ArrayList();

		list.add(ScheduleTaskImpl.make("基础功能--主题风格的自定义模块开发", "", getDate()));
		list.add(ScheduleTaskImpl.make("基础功能--用户、登录、密码管理设计开发", "", getDate()));
		list.add(ScheduleTaskImpl.make("COC--MVC之Ul组件的设计", "", getDate()));
		list.add(ScheduleTaskImpl.make("COC--实体定义之操作定义", "", getDate()));
		list.add(ScheduleTaskImpl.make("COC--MVC模型设计文档整理", "", getDate()));
		list.add(ScheduleTaskImpl.make("COC--平台管理系统之应用系统管理功能开发", "", getDate()));
		list.add(ScheduleTaskImpl.make("COC--平台配置及初始化开发", "", getDate()));
		// list.add(ScheduleTaskImpl.make("COC--MVC之标记库设计", getDate(), ""));
		// list.add(ScheduleTaskImpl.make("如何保证产品质量？自动化测试DEMO开发", getDate(), ""));

		return list;
	}

	@Override
	public List<IScheduleTask> getLeaderScheduleTasks(int pageSize, String user, Date dateFrom, Date dateTo) {
		List<IScheduleTask> list = new ArrayList();

		list.add(ScheduleTaskImpl.make("检验所工作流项目合同整理", "", getDate()));
		list.add(ScheduleTaskImpl.make("商务团队组建", "", getDate()));
		list.add(ScheduleTaskImpl.make("参加昆明市项目调研会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("药监局监管平台需求调研", "", getDate()));
		list.add(ScheduleTaskImpl.make("开发平台功能评审", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("药监局监管平台需求调研", "", getDate()));
		list.add(ScheduleTaskImpl.make("开发平台功能评审", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("药监局监管平台需求调研", "", getDate()));
		list.add(ScheduleTaskImpl.make("开发平台功能评审", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("药监局监管平台需求调研", "", getDate()));
		list.add(ScheduleTaskImpl.make("开发平台功能评审", "", getDate()));

		List<IScheduleTask> ret = new ArrayList();
		for (int i = 0; i < pageSize; i++) {
			ret.add(list.get(i));
		}

		return ret;
	}

	@Override
	public List<IScheduleTask> getDepartmentScheduleTasks(int pageSize, String user, Date dateFrom, Date dateTo) {
		List<IScheduleTask> list = new ArrayList();

		list.add(ScheduleTaskImpl.make("检验所工作流项目合同整理", "", getDate()));
		list.add(ScheduleTaskImpl.make("商务团队组建", "", getDate()));
		list.add(ScheduleTaskImpl.make("参加昆明市项目调研会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("药监局监管平台需求调研", "", getDate()));
		list.add(ScheduleTaskImpl.make("开发平台功能评审", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("药监局监管平台需求调研", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("药监局监管平台需求调研", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("药监局监管平台需求调研", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("药监局监管平台需求调研", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年1月29日权限管理方案评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("2015年2月3日下午项目评审会", "", getDate()));
		list.add(ScheduleTaskImpl.make("药监局监管平台需求调研", "", getDate()));

		List<IScheduleTask> ret = new ArrayList();
		for (int i = 0; i < pageSize; i++) {
			ret.add(list.get(i));
		}

		return ret;
	}

	@Override
	public IWorkflowTaskStatistics getMyWorkflowTaskStatistics(String user) {
		WorkflowTaskStatisticsImpl ret = new WorkflowTaskStatisticsImpl();
		ret.setTotal(30);
		ret.setAuthorize(10);
		ret.setAttention(10);
		ret.setExpire(1);
		ret.setExpired(2);
		ret.setHurry(3);
		ret.setReading(5);
		ret.setSpecialUrgent(1);
		ret.setUrgent(2);
		ret.setWaiting(13);

		return ret;
	}

	@Override
	public List<IWorkflowTask> getMyWorkflowTasks(int pageSize, String user, int type) {
		List<IWorkflowTask> list = new ArrayList();

		list.add(WorkflowTaskImpl.make("基础功能--主题风格的自定义模块开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("基础功能--用户、登录、密码管理设计开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--MVC之Ul组件的设计", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--实体定义之操作定义", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--MVC模型设计文档整理", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--平台管理系统之应用系统管理功能开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--平台配置及初始化开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--MVC之标记库设计", "", getDate()));
		list.add(WorkflowTaskImpl.make("如何保证产品质量？自动化测试DEMO开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--平台管理系统之应用系统管理功能开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--平台配置及初始化开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--MVC之标记库设计", "", getDate()));
		list.add(WorkflowTaskImpl.make("如何保证产品质量？自动化测试DEMO开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--平台管理系统之应用系统管理功能开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--平台配置及初始化开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--MVC之标记库设计", "", getDate()));
		list.add(WorkflowTaskImpl.make("如何保证产品质量？自动化测试DEMO开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--平台管理系统之应用系统管理功能开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--平台配置及初始化开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--MVC之标记库设计", "", getDate()));
		list.add(WorkflowTaskImpl.make("如何保证产品质量？自动化测试DEMO开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--平台管理系统之应用系统管理功能开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--平台配置及初始化开发", "", getDate()));
		list.add(WorkflowTaskImpl.make("COC--MVC之标记库设计", "", getDate()));
		list.add(WorkflowTaskImpl.make("如何保证产品质量？自动化测试DEMO开发", "", getDate()));

		List<IWorkflowTask> ret = new ArrayList();
		for (int i = 0; i < pageSize; i++) {
			ret.add(list.get(i));
		}

		return ret;
	}

	@Override
	public List<Option> getSearchCategories(int pageSize) {
		List<Option> list = new ArrayList();

		list.add(Option.make("功能模块", "01"));
		list.add(Option.make("通知公告", "02"));
		list.add(Option.make("我的任务", "03"));
		list.add(Option.make("我的日程", "04"));

		return list;
	}

	@Override
	public List<ISearchKeyword> getSearchKeywords(int pageSize) {
		List<ISearchKeyword> list = new ArrayList();

		list.add(SearchKeywordImpl.make("01", "权限管理"));
		list.add(SearchKeywordImpl.make("02", "评审会"));
		list.add(SearchKeywordImpl.make("03", "放假"));
		list.add(SearchKeywordImpl.make("04", "请假"));
		list.add(SearchKeywordImpl.make("05", "公文"));
		list.add(SearchKeywordImpl.make("06", "审批"));
		list.add(SearchKeywordImpl.make("06", "网站内容管理"));

		return list;
	}

	// @Override
	// public List<ISystemMenu> getAppCenterMenus(String user) {
	// return null;
	// }

}
