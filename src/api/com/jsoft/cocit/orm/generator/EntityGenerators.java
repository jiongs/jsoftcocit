package com.jsoft.cocit.orm.generator;


public interface EntityGenerators {
	Generator getGenerator(String name);

	void addGenerator(Generator adapter);
}
