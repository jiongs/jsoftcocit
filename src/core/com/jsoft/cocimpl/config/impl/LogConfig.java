package com.jsoft.cocimpl.config.impl;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ILogConfig;
import com.jsoft.cocit.util.LogUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 */
public class LogConfig implements ILogConfig {
	private static final String LOGBACK_CLASS_NAME = "ch.qos.logback.classic.Logger";

	private static final String LOG4J_CLASS_NAME = "org.apache.log4j.Logger";

	public LogConfig() {
		this.init();
	}

	protected void init() {
		reload();
	}

	private boolean log4jCanWork() {
		try {
			Class.forName(LOG4J_CLASS_NAME, true, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			return false;
		}

		return true;
	}

	private boolean logbackCanWork() {
		try {
			Class.forName(LOGBACK_CLASS_NAME, true, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	public void reload() {
		LogUtil.info("加载日志配置......");
		if (log4jCanWork()) {
			Log4jLoader.reload();
		} else {
			LogUtil.info("加载日志配置: <LOG4J不工作>");
		}
		if (logbackCanWork()) {
			LogbackLoader.reload();
		} else {
			LogUtil.info("加载日志配置: <LOGBACK不工作>");
		}
		LogUtil.info("加载日志配置: 结束.");
	}

	public void shutdown() {
		if (log4jCanWork()) {
			Log4jLoader.shutdown();
		}
		if (logbackCanWork()) {
			LogbackLoader.shutdown();
		}
	}

	private static class Log4jLoader {
		static void reload() {
			LogUtil.info("加载日志配置: 加载LOG4J配置...");
			String xmlfile = null;

			// String softCode = Cocit.me().getConfig().getDefaultSoftCode();
			// String softContext = softCode.replace(".", "_");
			// xmlfile = Cocit.me().getConfig().getContextDir() + "/" + softContext + "/config/log4j-test.properties";

			if (xmlfile == null || !new File(xmlfile).exists())
				xmlfile = Cocit.me().getConfig().getConfigDir() + "/log4j-test.properties";

			try {
				if (!Cocit.me().getConfig().isProductMode() && new File(xmlfile).exists()) {
					LogUtil.info("加载日志配置: LOG4J配置文件 [%s]", xmlfile);
					PropertyConfigurator.configure(xmlfile);
				} else {

					// xmlfile = Cocit.me().getConfig().getContextDir() + "/" + softContext + "/config/log4j.properties";
					// if (!new File(xmlfile).exists())
					// xmlfile = Cocit.me().getConfig().getConfigDir() + "/log4j.properties";

					if (xmlfile == null || new File(xmlfile).exists()) {
						LogUtil.info("加载日志配置: LOG4J配置文件 [%s]", xmlfile);
						PropertyConfigurator.configure(xmlfile);
					} else
						LogUtil.info("加载日志配置: <LOG4J配置文件不存在>");
				}
				LogUtil.info("加载日志配置: 加载LOG4J配置: 结束.");
			} catch (Throwable e) {
				LogUtil.info("加载日志配置: 加载LOG4J配置出错! [%s]\n错误信息：%s", xmlfile, e);
			}
		}

		static void shutdown() {
			LogManager.shutdown();
		}
	}

	private static class LogbackLoader {
		static void reload() {
			LogUtil.info("加载日志配置: 加载LOGCONFIG配置...");

			String xmlfile = null;

			// String softCode = Cocit.me().getConfig().getDefaultSoftCode();
			// String softContext = softCode.replace(".", "_");
			// xmlfile = Cocit.me().getConfig().getContextDir() + "/" + softContext + "/config/logconfig-test.xml";

			if (xmlfile == null || !new File(xmlfile).exists())
				xmlfile = Cocit.me().getConfig().getConfigDir() + "/logconfig-test.xml";

			try {
				if (!Cocit.me().getConfig().isProductMode() && new File(xmlfile).exists()) {
					LogUtil.info("加载日志配置: LOGCONFIG配置文件 [%s]", xmlfile);
					ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
					LoggerContext loggerContext = (LoggerContext) loggerFactory;
					loggerContext.stop();
					JoranConfigurator configurator = new JoranConfigurator();
					configurator.setContext(loggerContext);
					configurator.doConfigure(new File(xmlfile));
				} else {
					// xmlfile = Cocit.me().getConfig().getContextDir() + "/" + softContext + "/config/logconfig.xml";

					if (xmlfile == null || !new File(xmlfile).exists())
						xmlfile = Cocit.me().getConfig().getConfigDir() + "/logconfig.xml";
					if (new File(xmlfile).exists()) {
						LogUtil.info("加载日志配置: LOGCONFIG配置文件 [%s]", xmlfile);
						ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
						LoggerContext loggerContext = (LoggerContext) loggerFactory;
						loggerContext.stop();
						JoranConfigurator configurator = new JoranConfigurator();
						configurator.setContext(loggerContext);
						configurator.doConfigure(new File(xmlfile));
					} else
						LogUtil.info("加载日志配置: <LOGCONFIG配置文件不存在>");

				}
				LogUtil.info("加载日志配置: 加载LOGCONFIG配置: 结束.");
			} catch (Throwable e) {
				LogUtil.error("加载日志配置: 加载LOGCONFIG配置出错! [%s]\n错误信息：%s", xmlfile, e);
			}
		}

		static void shutdown() {
		}
	}
}
