package com.jsoft.cocit.config;

import java.util.Properties;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public interface IDSConfig {
	public static String URL = "url";

	public static String DRIVER = "driver";

	public static String USER = "user";

	public static String PWSSWORD = "password";

	public static String TYPE = "type";

	public static String DATABASE = "database";

	public String getUrl();

	public String getDriver();

	public String getUser();

	public String getPassword();

	public Properties getProperties();
}
