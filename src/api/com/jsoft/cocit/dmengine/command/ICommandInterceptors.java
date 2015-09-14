package com.jsoft.cocit.dmengine.command;

public interface ICommandInterceptors {
	ICommandInterceptor getCommandInterceptor(String interceptorName);

	void addCommandInterceptor(ICommandInterceptor interceptor);
}
