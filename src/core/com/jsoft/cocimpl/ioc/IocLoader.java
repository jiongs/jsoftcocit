package com.jsoft.cocimpl.ioc;

import java.io.File;

import com.jsoft.cocimpl.ioc.impl.IocProxyImpl;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.util.LogUtil;

public abstract class IocLoader {
	private static final String FILE_EXT = ".js";

	public static IocProxy load() {
		LogUtil.info("加载IOC......");
		String configFile = Cocit.me().getConfig().getIOCConfig();
		if (configFile.endsWith(FILE_EXT)) {
			configFile = configFile.substring(0, configFile.length() - FILE_EXT.length());
		}
		configFile = configFile.replace("\\", "/").replace(".", "/") + FILE_EXT;
		int idx = configFile.lastIndexOf("/");
		String fileName;
		if (idx > -1) {
			fileName = configFile.substring(idx + 1);
		} else {
			fileName = configFile;
		}
		File file = new File(Cocit.me().getConfig().getConfigDir() + "/" + fileName);

		String[] paths = null;
		try {

			if (!file.exists()) {
				LogUtil.trace("加载IOC: 扩展的IOC配置文件不存在 [path=%s]", file.getAbsolutePath());
				paths = new String[1];
				paths[0] = configFile;
			} else {
				paths = new String[2];
				paths[0] = configFile;
				paths[1] = file.getAbsolutePath();
			}
			if (LogUtil.isInfoEnabled()) {
				StringBuffer sb = new StringBuffer();
				int i = 0;
				for (String s : paths) {
					if (i != 0) {
						sb.append(", ");
					}
					sb.append(s);
					i++;
				}
				LogUtil.info("加载IOC: IOC配置文件 [%s]", sb.toString());
				IocProxy ret = new IocProxyImpl().init(paths);
				LogUtil.info("加载IOC: 结束.");

				return ret;
			} else
				return new IocProxyImpl().init(paths);
		} catch (Throwable e) {
			LogUtil.error("加载IOC出错! %s", e);
			return null;
		}
	}
}
