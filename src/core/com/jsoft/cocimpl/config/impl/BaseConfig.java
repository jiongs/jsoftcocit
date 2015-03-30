package com.jsoft.cocimpl.config.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;

import org.nutz.lang.stream.StringInputStream;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.config.IConfig;
import com.jsoft.cocit.config.IDynaBean;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.FileUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @preserve all
 * @author Ji Yongshan
 * 
 */
public abstract class BaseConfig implements IConfig, IDynaBean {
	public static final String FILE_EXT = ".properties";

	protected Properties properties = new Properties();

	protected String configFile;

	protected BaseConfig() {
	}

	protected BaseConfig(String path) {
		initPath(path);
		init();
	}

	protected void copyTo(BaseConfig config) {
		config.configFile = configFile;
		config.properties = (Properties) properties.clone();
	}

	protected void initPath(String path) {
		configFile = path;
		if (configFile.endsWith(FILE_EXT)) {
			configFile = configFile.substring(0, configFile.length() - FILE_EXT.length());
		}
		configFile = configFile.replace("\\", "/").replace(".", "/") + FILE_EXT;
	}

	public String get(String key) {
		return properties.getProperty(key);
	}

	public String get(String key, String defaultReturnValue) {
		String v = get(key);
		if (v == null || v.trim().length() == 0) {
			return defaultReturnValue;
		}
		return v;
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int dft) {
		try {
			String value = get(key);
			if (value == null) {
				return dft;
			}
			return Integer.parseInt(value);
		} catch (Throwable e) {
			return dft;
		}
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean deflt) {
		try {
			return Boolean.parseBoolean(get(key));
		} catch (Throwable e) {
			return deflt;
		}
	}

	public void put(String key, String value) {
		this.properties.put(key, value);
	}

	public BaseConfig set(String key, Object value) {
		this.properties.put(key, value);
		return this;
	}

	public Properties getProperties() {
		return properties;
	}

	//
	// public boolean is(byte index) {
	// return false;
	// }

	protected void init() {
		/*
		 * 加载默认配置文件
		 */
		String filePath = ICocConfig.CONFIG_FILE_PACKAGE + this.configFile;
		InputStream is = null;
		try {
			String content = null;
			File file = new File(filePath);
			if (file.exists()) {
				content = FileUtil.read(file);
			} else {
				content = FileUtil.read(filePath);
			}
			if (!StringUtil.isBlank(content)) {
				is = new StringInputStream(StringUtil.toUnicode(content));
				if (is != null)
					properties.load(is);
			}
		} catch (Throwable e) {
			LogUtil.warn("从类路径加载配置文件出错! [%s] %s", filePath, ExceptionUtil.msg(e));
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException iglore) {
					LogUtil.warn(ExceptionUtil.msg(iglore));
				}
			}
		}
		/*
		 * 加载自定义配置文件
		 */
		is = null;
		File file = null;
		try {
			String content = null;
			file = getExtConfigFile();
			if (file != null && file.exists()) {
				content = FileUtil.read(file);
			}

			if (!StringUtil.isBlank(content)) {
				is = new StringInputStream(StringUtil.toUnicode(content));

				Properties customProps = new Properties();
				customProps.load(is);
				properties.putAll(customProps);
			}
		} catch (Throwable e) {
			LogUtil.error("从配置文件目录加载配置文件<%s>出错! 详细信息：%s", file == null ? "NULL" : file.getAbsolutePath(), ExceptionUtil.msg(e));
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException iglore) {
					LogUtil.warn(ExceptionUtil.msg(iglore));
				}
			}
		}
	}

	protected StringBuffer toText() {
		StringBuffer sb = new StringBuffer();
		Iterator keys = this.properties.keySet().iterator();
		while (keys.hasNext()) {
			Object key = keys.next();
			sb.append(key).append("=").append(properties.get(key)).append("\n");
		}

		return sb;
	}

	public String toString() {
		return toText().toString();
	}

	protected File getExtConfigFile() {
		int idx = configFile.lastIndexOf("/");
		String fileName;
		if (idx > -1) {
			fileName = configFile.substring(idx + 1);
		} else {
			fileName = configFile;
		}

		File file = new File(Cocit.me().getConfig().getConfigDir() + "/" + fileName);
		// if (!file.exists()) {
		// File tplFile = new File(Cocit.me().getConfig().getConfigDir() + "/" + fileName);
		// file.getParentFile().mkdirs();
		// try {
		// file.createNewFile();
		// FileUtil.copy(tplFile, file);
		// } catch (Throwable e) {
		// }
		// }

		return file;
	}

	public void save() throws IOException {
		File file = getExtConfigFile();
		OutputStream os = null;
		try {
			if (file != null && !file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			os = new FileOutputStream(file);
			properties.store(os, "");
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Throwable e) {
					LogUtil.warn(ExceptionUtil.msg(e));
				}
			}
		}
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
