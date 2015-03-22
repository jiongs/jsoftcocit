package com.jsoft.cocimpl.config.impl;

import java.io.File;

import org.nutz.lang.Strings;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.IConfig;
import com.jsoft.cocit.config.IDSConfig;
import com.jsoft.cocit.util.LogUtil;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public class DSConfig extends BaseConfig implements IDSConfig, IConfig {

	public DSConfig() {
		super(Cocit.me().getConfig().getDBConfig());
	}

	public DSConfig(String cfg) {
		super(cfg);
	}

	public DSConfig(String url, String driver, String user, String pwd) {
		properties.put(URL, url);
		properties.put(DRIVER, driver);
		properties.put(USER, user);
		properties.put(PWSSWORD, pwd);
	}

	public String getUrl() {
		return get(URL, "");
	}

	public String getDriver() {
		return get(DRIVER, "");
	}

	public String getPassword() {
		return get(PWSSWORD, "");
	}

	public String getUser() {
		return get(USER, "");
	}

	protected void init() {
		File file = null;
		if (LogUtil.isInfoEnabled()) {
			file = this.getExtConfigFile();
			if (file != null) {
				if (file.exists())
					LogUtil.info("加载数据库配置......[%s]", file.getAbsolutePath());
				else {
					LogUtil.error("数据库配置文件不存在！[$s]", file.getAbsolutePath());
				}
			} else {
				LogUtil.error("数据库配置文件不存在！[$s]", this.configFile);
			}
		}

		super.init();
		//
		// InputStream is = null;
		// try {
		// String content = null;
		// if (file != null && file.exists()) {
		// content = FileUtil.read(file);
		// }
		//
		// if (!StringUtil.isEmpty(content)) {
		// is = new StringInputStream(StringUtil.toUnicode(content));
		//
		// Properties customProps = new Properties();
		// customProps.load(is);
		// properties.putAll(customProps);
		// }
		// } catch (IOException e) {
		// LogUtil.error("从配置文件目录加载配置文件<%s>出错! 详细信息：%s", file == null ? "NULL" : file.getAbsolutePath(), ExceptionUtil.msg(e));
		// } finally {
		// if (is != null) {
		// try {
		// is.close();
		// } catch (IOException iglore) {
		// LogUtil.warn(ExceptionUtil.msg(iglore));
		// }
		// }
		// }

		if (LogUtil.isInfoEnabled()) {
			LogUtil.info("加载数据库配置结束！[\n%s]", toText());
		}
	}

	protected void copyTo(DSConfig config) {
		super.copyTo(config);
	}

	public IConfig copy() {
		DSConfig ret = new DSConfig();
		this.copyTo(ret);

		return ret;
	}

	public int hashCode() {
		return 37 * 17 + (getUrl()).hashCode();
	}

	public boolean equals(Object that) {
		if (!getClass().equals(that.getClass())) {
			return false;
		}
		DSConfig thatEntity = (DSConfig) that;
		if ((this == that)) {
			return true;
		}
		String url = getUrl();
		if (Strings.isEmpty(url)) {
			return false;
		}
		String url2 = thatEntity.getUrl();
		if (thatEntity == null || Strings.isEmpty(url2)) {
			return false;
		}
		return url.equals(url2);
	}

	public String toString() {
		return new StringBuffer().append("URL=").append(this.getUrl()).append(", DRIVER=").append(this.getDriver()).append(", USER=").append(getUser()).append(", PWD=").append(getPassword()).toString();
	}

}
