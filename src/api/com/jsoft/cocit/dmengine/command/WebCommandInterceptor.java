package com.jsoft.cocit.dmengine.command;

public abstract class WebCommandInterceptor implements ICommandInterceptor {

	@Override
	public boolean execute(ICommandContext commandContext) {
		return execute((WebCommandContext) commandContext);
	}

	/**
	 * 执行命令：执行当前任务。
	 * 
	 * @param context
	 * @return true：中断执行后续命令；false: 继续执行后续命令
	 */
	protected abstract boolean execute(WebCommandContext commandContext);

}