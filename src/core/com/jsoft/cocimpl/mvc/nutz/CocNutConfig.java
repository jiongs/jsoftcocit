package com.jsoft.cocimpl.mvc.nutz;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.mvc.Loading;
import org.nutz.mvc.annotation.LoadingBy;
import org.nutz.mvc.config.AbstractNutConfig;
import org.nutz.mvc.config.AtMap;
import org.nutz.mvc.impl.NutLoading;
import org.nutz.resource.Scans;

import com.jsoft.cocimpl.action.MainModule;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.util.LogUtil;

public class CocNutConfig extends AbstractNutConfig {

	public CocNutConfig() {
		Cocit.me().setContextAttribute(AtMap.class.getName(), new AtMap());
		Scans.me().init(Cocit.me().getServletContext());
	}

	public Loading createLoading() {
		/*
		 * 确保用户声明了 MainModule
		 */
		Class[] mainModules = getMainModules();
		Class<?> mainModule = mainModules[0];

		/*
		 * 获取 Loading
		 */
		LoadingBy by = mainModule.getAnnotation(LoadingBy.class);
		if (null == by) {
			if (LogUtil.isDebugEnabled())
				LogUtil.debug("Loading by " + NutLoading.class);
			return new NutLoading();
		}
		try {
			if (LogUtil.isDebugEnabled())
				LogUtil.debug("Loading by " + by.value());
			return Mirror.me(by.value()).born();
		} catch (Exception e) {
			throw Lang.wrapThrow(e);
		}
	}

	public Class<?> getMainModule() {
		return null;
	}

	public Class[] getMainModules() {
		return new Class[] { MainModule.class };
	}

	public String getAppRoot() {
		return Cocit.me().getContextDir();
	}

	public String getAppName() {
		return "Cocit";// Cocit.me().getConfig().getDefaultSoftName();
	}

	public ServletContext getServletContext() {
		return Cocit.me().getServletContext();
	}

	public String getInitParameter(String name) {
		return Cocit.me().getContextParameter(name);
	}

	public List<String> getInitParameterNames() {
		Iterator enums = Cocit.me().getContextParameters().keySet().iterator();
		LinkedList<String> re = new LinkedList<String>();
		while (enums.hasNext())
			re.add(enums.next().toString());
		return re;
	}

}
