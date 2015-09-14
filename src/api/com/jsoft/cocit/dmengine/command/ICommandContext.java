package com.jsoft.cocit.dmengine.command;

import java.util.List;

/**
 * 命令执行环境：即调用命令拦截器中的execute方法时，将需要的参数等信息，直接放该命令环境对象中，拦截器可以从环境中获取所需要的数据并将执行结果存放在命令环境对象中。
 * 
 * @author Ji Yongshan
 * 
 */
public interface ICommandContext {

	/**
	 * 执行命令：调用该方法时，命令链的执行顺序：前拦截器、命令执行器、后拦截器
	 * 
	 * @param prevInterceptors
	 *            前拦截器列表
	 * @param commandExecutor
	 *            命令名称
	 * @param postInterceptors
	 *            后拦截器列表
	 */
	void execute(List<ICommandInterceptor> prevInterceptors, ICommandInterceptor commandExecutor, List<ICommandInterceptor> postInterceptors);

	/**
	 * 
	 * 执行命令：调用该方法时，命令链的执行顺序：前拦截器、命令执行器、后拦截器
	 * 
	 * @param prevInterceptorOrNames
	 *            前拦截器列表：可以是ICommandInterceptor的实例，也可以是已注册的命令名称。
	 * @param commandExecutorName
	 *            命令名称
	 * @param postInterceptorOrNames
	 *            后拦截器列表：可以是ICommandInterceptor的实例，也可以是已注册的命令名称。
	 */
	void execute(List prevInterceptorOrNames, String commandExecutorName, List postInterceptorOrNames);

	/**
	 * 获取执行结果
	 * 
	 * @return
	 */
	Object getResult();
}
