package com.jsoft.cocit.dmengine.command;

/**
 * 命令拦截器
 * 
 * @author Ji Yongshan
 * 
 */
public interface ICommandInterceptor {

	/**
	 * 命令拦截器名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 执行命令：执行当前任务。
	 * 
	 * @param context
	 * @return true：中断执行后续命令；false: 继续执行后续命令
	 */
	boolean execute(ICommandContext context);

}
