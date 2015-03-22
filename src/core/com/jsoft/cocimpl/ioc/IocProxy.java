package com.jsoft.cocimpl.ioc;

public interface IocProxy {

	<T> T get(String name);

	IocProxy init(String... paths);

	<T> T getIoc();
}
