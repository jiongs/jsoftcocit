package com.jsoft.cocit.config;

public interface IMessageConfig extends IConfig {

	String getMsg(String key, Object... args);
}
