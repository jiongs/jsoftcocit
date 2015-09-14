package com.jsoft.cocimpl.dmengine.impl;

import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.dmengine.command.ICommandInterceptor;
import com.jsoft.cocit.dmengine.command.ICommandInterceptors;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.StringUtil;

public class CommandInterceptorsImpl implements ICommandInterceptors {

	private Map<String, ICommandInterceptor> receivers;

	public CommandInterceptorsImpl() {
		receivers = new HashMap();
	}

	@Override
	public ICommandInterceptor getCommandInterceptor(String pattern) {
		if (StringUtil.isBlank(pattern))
			return null;

		return receivers.get(pattern.trim().toLowerCase());
	}

	@Override
	public void addCommandInterceptor(ICommandInterceptor receiver) {
		if (receiver == null || StringUtil.isBlank(receiver.getName())) {
			throw new CocException("CommandReceiversImpl.addCommandReceiver. Fail！ [receiverName: %s, adapter: %s]",//
			        receiver == null ? "<NULL>" : receiver.getName(),//
			        receiver == null ? "<NULL>" : receiver.getClass().getName()//
			);
		}

		receivers.put(receiver.getName().trim().toLowerCase(), receiver);
	}

}
