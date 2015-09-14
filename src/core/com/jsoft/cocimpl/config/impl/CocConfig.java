package com.jsoft.cocimpl.config.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.nutz.lang.stream.StringInputStream;

import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.config.ICommonConfig;
import com.jsoft.cocit.config.ViewConfig;
import com.jsoft.cocit.util.FileUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @preserve all
 * @author Ji Yongshan
 * 
 */
@SuppressWarnings("deprecation")
public class CocConfig extends BaseConfig implements ICocConfig, ICommonConfig {

	private String configPath;

	private String contextPath;

	private String contextDir;

	private ViewConfig viewConfig;

	private void initDefault() {
		properties.put(CONFIG_DB, "dbconfig.properties");
		properties.put(CONFIG_I18N, "i18nconfig.properties");
		properties.put(CONFIG_USER, "rootuser.properties");
		properties.put(CONFIG_IOC, "iocconfig.js");
		properties.put(MODE_PRODUCT, "false");

		properties.put(PATH_CONFIG, "/WEB-INF/config");
		properties.put(PATH_LOGS, "/WEB-INF/logs");
		properties.put(PATH_TEMP, "/WEB-INF/tmp");
		properties.put(PATH_THEME, "/themes2/defaults");
		properties.put(PATH_UPLOAD, "/upload");
		properties.put("configPkg", "com.jsoft.cocit.data");

		properties.put(DOMAIN_IMAGE, "");
		properties.put(DOMAIN_SCRIPT, "");
		properties.put(DOMAIN_CSS, "");

		properties.put("cocit.cocitSystemName", "COC平台管理系统");
		properties.put("cocit.cocitSystemKey", "___coc_platform___");
		properties.put("cocit.cocitTenantName", "COC平台租户");
		properties.put("cocit.cocitTenantKey", "0000");

		properties.put("cocit.defaultSystemName", "");
		properties.put("cocit.defaultSystemKey", "");

		properties.put("cocit.isMultiTenant", "false");
		properties.put("cocit.isMultiSystem", "false");

		properties.put("entity.packages", "com.jsoft.cocit.entity.impl");

		// 内置路径配置
	}

	private CocConfig() {

	}

	public CocConfig(String contextPath, String contextDir, String configPath) {
		this.initDefault();

		this.contextPath = contextPath;
		this.contextDir = contextDir;
		if (StringUtil.isBlank(configPath)) {
			configPath = this.get(PATH_CONFIG);
			configPath = configPath.replace("\\", "/").replace(".", "/");
			if (!configPath.startsWith("/")) {
				configPath = "/" + configPath;
			}
		}
		this.configPath = configPath;

		initPath(COC_CONFIG_FILE);
		init();
	}

	protected void init() {
		if (LogUtil.isDebugEnabled()) {
			File file = this.getExtConfigFile();
			LogUtil.debug("加载 APP 参数配置......[%s, %s]", this.configFile, file.exists() ? file.getAbsolutePath() : "");
		}
		super.init();
		if (LogUtil.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append(toText());
			LogUtil.debug("参数配置：%s", sb);
		}
	}

	public File getExtConfigFile() {
		int idx = configFile.lastIndexOf("/");
		String fileName;
		if (idx > -1) {
			fileName = configFile.substring(idx + 1);
		} else {
			fileName = configFile;
		}
		File file = new File(getConfigDir() + "/" + fileName);

		/*
		 * 加载 /WEB-INF/config/app.properties
		 */
		InputStream is = null;
		Properties customProps = null;
		try {
			if (file.exists()) {
				String str = FileUtil.read(file);
				is = new StringInputStream(StringUtil.toUnicode(str));

				customProps = new Properties();
				customProps.load(is);
			}
		} catch (IOException e) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException iglore) {
				}
			}
		}

		/*
		 * 检查 /{project-context-path}/config/app.properties
		 */
		// if (customProps != null) {
		// String softCode = AppConfig.getDefaultSoftCode(customProps);
		// String softContext = softCode.replace(".", "_");
		// File file2 = new File(getContextDir() + "/" + softContext + "/config/" + fileName);
		// if (!file2.exists()) {
		// file2.getParentFile().mkdirs();
		// try {
		// file2.createNewFile();
		// FileUtil.copy(file, file2);
		// } catch (IOException e) {
		// }
		// }
		//
		// return file2;
		// }

		return file;
	}

	public String getConfigPath() {
		return configPath;
	}

	public String getConfigDir() {
		return contextDir + getConfigPath();
	}

	public String getCocitSystemName() {
		return this.get("cocit.cocitSystemName");
	}

	public String getCocitSystemCode() {
		return this.get("cocit.cocitSystemKey");
	}

	public String getCocitTenantName() {
		return this.get("cocit.cocitTenantName");
	}

	public String getCocitTenantCode() {
		return this.get("cocit.cocitTenantKey");
	}

	public String getDefaultSystemName() {
		return this.get("cocit.defaultSystemName");
	}

	public String getDefaultSystemCode() {
		return this.get("cocit.defaultSystemKey");
	}

	public String getDBConfig() {
		return this.get(CONFIG_DB);
	}

	public String getI18NConfig() {
		return this.get(CONFIG_I18N);
	}

	public String getIOCConfig() {
		return this.get(CONFIG_IOC);
	}

	public String getLogsPath() {
		return this.get(PATH_LOGS);
	}

	public String getLogsDir() {
		return contextDir + getLogsPath();
	}

	public String getTempPath() {
		return this.get(PATH_TEMP);
	}

	public String getTempDir() {
		return contextDir + getTempPath();
	}

	public String getWebInfoDir() {
		return contextDir + "/WEB-INF";
	}

	public String getClassDir() {
		return contextDir + "/WEB-INF/classes";
	}

	public String getUserConfig() {
		return this.get(CONFIG_USER);
	}

	public String getImgDomainPath() {
		return this.get(DOMAIN_IMAGE);
	}

	public String getScriptDomainPath() {
		return this.get(DOMAIN_SCRIPT);
	}

	public String getCssDomainPath() {
		return this.get(DOMAIN_CSS);
	}

	public String getThemePath() {
		return this.get(PATH_THEME);
	}

	public String getContextDir() {
		return contextDir;
	}

	public String getContextPath() {
		return contextPath;
	}

	//
	// public String getTplPackage() {
	// return this.get(PKG_TPL);
	// }

	protected void copyTo(CocConfig config) {
		config.configPath = this.configPath;
		config.contextDir = this.contextDir;
		config.contextPath = this.contextPath;
		super.copyTo(config);
	}

	public ICocConfig copy() {
		CocConfig ret = new CocConfig();
		this.copyTo(ret);

		return ret;
	}

	public boolean isProductMode() {
		return this.getBoolean(MODE_PRODUCT);
	}

	public boolean isAutoUpgradeEntityDefinition() {
		return this.getBoolean("mode.autoupgrade.entitydefinition");
	}

	@Override
	public boolean isAutoUpgradeEntityTables() {
		return this.getBoolean("mode.autoupgrade.entitytables");
	}

	public String getUploadPath() {
		return this.get(PATH_UPLOAD);
	}

	//
	// public String getCustomFieldPkg() {
	// return get("customFieldPkg");
	// }
	//

	public String getConfigPkg() {
		return get("configPkg");
	}

	//
	//
	// public boolean isPrivacyMode() {
	// return getBoolean("security.privacyMode");
	// }

	@Override
	public boolean isMultiTenant() {
		return this.getBoolean("cocit.isMultiTenant");
	}

	@Override
	public boolean isMultiSystem() {
		return this.getBoolean("cocit.isMultiSystem");
	}

	@Override
	public ViewConfig getViewConfig() {
		if (viewConfig == null) {
			viewConfig = new ViewConfig(this);
		}
		return viewConfig;
	}
}
