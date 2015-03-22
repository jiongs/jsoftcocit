package com.jsoft.cocimpl.ui.view;

import java.io.InputStreamReader;
import java.io.StringReader;

import org.lilystudio.smarty4j.Engine;
import org.lilystudio.smarty4j.Template;
import org.lilystudio.smarty4j.TemplateException;

public class SmartyTemplate extends Template {

	private String classpath;

	SmartyTemplate(Engine engine, InputStreamReader isr, String path) throws TemplateException {
		super(engine, null, isr, true);
		this.classpath = path;
	}

	SmartyTemplate(Engine engine, StringReader isr, String classpath) throws TemplateException {
		super(engine, null, isr, true);
		this.classpath = classpath;
	}

	public String getClassPath() {
		return classpath;
	}
}