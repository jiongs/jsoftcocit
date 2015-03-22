package com.jsoft.cocimpl.context;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.jsoft.cocit.Cocit;

public class QuartzContextPlugin implements IContextPlugin {
	public static final String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";

	private static Scheduler scheduler = null;

	public String getName() {
		return "定时任务调度程序";
	}

	public void start() throws SchedulerException {
		if (Cocit.me().getServletContext().getAttribute(QUARTZ_FACTORY_KEY) != null) {
			throw new SchedulerException("已启动!");
		}

		StdSchedulerFactory factory = new StdSchedulerFactory();

		scheduler = factory.getScheduler();

		scheduler.start();

		Cocit.me().getServletContext().setAttribute(QUARTZ_FACTORY_KEY, factory);
	}

	public void close() throws SchedulerException {
		if (scheduler != null)
			scheduler.shutdown();
	}

	
	public boolean support() {
		return Cocit.me().getServletContext() != null;
	}

}
