package com.jsoft.cocimpl.orm.generator.impl;

import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.generator.EntityGenerators;
import com.jsoft.cocit.orm.generator.Generator;
import com.jsoft.cocit.util.StringUtil;

public class EntityGeneratorsImpl implements EntityGenerators {

	private Map<String, Generator> generators;

	public EntityGeneratorsImpl() {
		generators = new HashMap();
	}

	@Override
	public Generator getGenerator(String name) {
		if (StringUtil.isBlank(name))
			return null;

		return generators.get(name.trim().toLowerCase());
	}

	@Override
	public void addGenerator(Generator gen) {
		if (gen == null || StringUtil.isBlank(gen.getName())) {
			throw new CocException("EntityGenerators.addGenerator. Fail！ [generatorName: %s, generator: %s]",//
			        gen == null ? "<NULL>" : gen.getName(),//
			        gen == null ? "<NULL>" : gen.getClass().getName()//
			);
		}

		generators.put(gen.getName().trim().toLowerCase(), gen);
	}
}
