package com.jsoft.cocimpl.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICommonConfig;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.LogUtil;

public abstract class ContextPlugins {
	private static Map<Class, Object> pluginMap = new HashMap();

	// <环境插件配置KEY, 插件对象>
	private static Map<String, IContextPlugin> contextPluginMap = new HashMap();

	public static void startContextPlugins() {
		LogUtil.info("启动 Plugin(s)......");

		// TODO: 从配置文件中加载环境插件：通过环境插件前缀查找
		List contextPlugins = new LinkedList();
		contextPlugins.add("com.jsoft.cocit.plugin.impl.QuartzContextPlugin");

		for (Object className : contextPlugins) {
			IContextPlugin plugin = null;
			try {
				if (className instanceof String) {
					Class cls = ClassUtil.forName((String) className);
					plugin = (IContextPlugin) Mirror.me(cls).born();
				} else if (className instanceof IContextPlugin) {
					plugin = (IContextPlugin) className;
				}

				if (plugin.support()) {
					plugin.start();

					LogUtil.info("启动 Plugin: 结束. [%s, class=%s]", plugin.getName(), className);
				}
			} catch (Throwable e) {
				LogUtil.error("启动 Plugin 出错! [%s, class=%s] %s", plugin == null ? "NULL" : plugin.getName(), className, ExceptionUtil.msg(e));
			}
		}
		LogUtil.info("启动 Plugin(s): 结束. [size=%s]", contextPlugins.size());
	}

	public static void closeContextPlugins() throws CocException {
		LogUtil.info("停止 Plugin(s)...... [size=%s]", contextPluginMap.size());

		Collection<IContextPlugin> contextPlugins = contextPluginMap.values();
		for (IContextPlugin plugin : contextPlugins) {
			try {
				if (plugin.support()) {
					plugin.close();

					LogUtil.info("停止  Plugin: 结束. [%s, class=%s]", plugin.getName(), plugin.getClass().getName());
				}
			} catch (Throwable e) {
				LogUtil.error("停止 Plugin 出错! [%s, class=%s] %s", plugin.getName(), plugin.getClass().getName(), ExceptionUtil.msg(e));
			}
		}

		LogUtil.info("停止 Plugin(s): 结束. [size=%s]", contextPlugins.size());
	}

	public static <T> T getPlugin(Class<T> interfaceOfPlugin) {
		synchronized (pluginMap) {
			Object plugin = pluginMap.get(interfaceOfPlugin);
			if (plugin == null) {

				String key = ICommonConfig.PLUGIN_PREFIX + interfaceOfPlugin.getSimpleName();
				try {
					String pluginImpl = Cocit.me().getConfig().get(key);
					plugin = Class.forName(pluginImpl).newInstance();
					pluginMap.put(interfaceOfPlugin, plugin);

					LogUtil.info("加载 Plugin: 结束. [key=%s, plugin=%s]", key, plugin.getClass().getName());
				} catch (Throwable e) {
					LogUtil.error("加载 Plugin 出错! [key=%s] %s", key, e);
				}

			}
			return (T) plugin;
		}
	}
}
