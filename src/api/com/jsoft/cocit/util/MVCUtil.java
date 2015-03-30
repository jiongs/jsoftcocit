package com.jsoft.cocit.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.json.Json;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.entity.security.ITenant;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;

public abstract class MVCUtil {

	public static String themePath, stylePath, imagePath, scriptPath;

	// MVC全局变量
	public static final Map GLOBAL_PARAMS = new HashMap();
	static {
		initContextVariables();
	}

	public static void initContextVariables() {
		themePath = Cocit.me().getConfig().getImgDomainPath() + makeUrl("/themes/defaults");
		stylePath = Cocit.me().getConfig().getCssDomainPath() + makeUrl("/jCocit/css");
		imagePath = Cocit.me().getConfig().getImgDomainPath() + makeUrl("/themes/defaults/images");
		scriptPath = Cocit.me().getConfig().getScriptDomainPath() + makeUrl("/jCocit/js");

		/*
		 * 设置模版环境变量——全局变量
		 */
		// 静态资源URL前缀 = 静态资源域名路径 + 环境路径
		GLOBAL_PARAMS.put("contextPath", makeUrl(""));

		GLOBAL_PARAMS.put("themePath", themePath);
		GLOBAL_PARAMS.put("stylePath", stylePath);
		GLOBAL_PARAMS.put("imagePath", imagePath);
		GLOBAL_PARAMS.put("scriptPath", scriptPath);

		// UI 全局样式设置
		// CONTEXT_VARIABLES.put("fontSize", "12px");
	}

	public static String makeUrl(String path, SystemMenuService menu) {

		StringBuffer sb = new StringBuffer();

		String str = "";
		if (menu != null) {
			str = "" + menu.getKey();
		}
		sb.append(str);

		return makeUrl(path, sb.toString());
	}

	public static String makeUrl(String path, SystemMenuService menu, CocEntityService module) {
		StringBuffer sb = new StringBuffer();

		if (menu == null && module != null) {
			menu = module.getSystemMenu();
		}

		String str = "";
		if (menu != null) {
			str = "" + menu.getKey();
		}
		sb.append(str + ":");

		str = "";
		if (module != null) {
			str = "" + module.getKey();
		}
		sb.append(str);

		return makeUrl(path, sb.toString());
	}

	public static String makeUrl(String path, SystemMenuService menuService, CocEntityService entityService, CocActionService action) {
		return makeUrl(path, menuService, entityService, action.getKey());
	}

	public static String makeUrl(String path, SystemMenuService menuService, CocEntityService entityService, String action) {
		StringBuffer sb = new StringBuffer();

		if (menuService == null && entityService != null) {
			menuService = entityService.getSystemMenu();
		}

		String str = "";
		if (menuService != null) {
			str = "" + menuService.getKey();
		}
		sb.append(str + ":");

		str = "";
		if (entityService != null) {
			str = "" + entityService.getKey();
		}
		sb.append(str + ":");

		sb.append(action);

		return makeUrl(path, sb.toString());
	}

	/**
	 * 解码路径参数
	 * 
	 * @param opArgs
	 * @return
	 */
	public static String[] decodeArgs(String args) {
		String str = args;

		return StringUtil.toArray(str, ":");
	}

	/**
	 * 将JSP路径参数转换成以“/”开头的相对路径
	 * 
	 * @param jspPathArgs
	 *            JSP页面路径，有子目录的用冒号分隔，如: /visit/index 路径为： visit:index
	 * @return 返回 /visit/index
	 */
	public static String makeJspPath(String jspPathArgs) {
		return "/" + jspPathArgs.replace(':', '/');
	}

	/**
	 * 计算环境路径并将路径中的/*替换成真实的路径参数，如 /main/* 用[119,12]替换后变成 /main/119/12
	 * 
	 * @param path
	 *            指定的路径
	 * @param params
	 *            路径参数：用于替换path中的/*部分
	 * @return 环境路径：即自动加上环境路径前缀
	 */
	public static String makeUrl(String path, Object... params) {
		if (StringUtil.isBlank(path)) {
			return Cocit.me().getContextPath();
		}

		path = makePath(path, params);

		if (path.startsWith("/")) {
			return Cocit.me().getContextPath() + path;
		}

		return path;
	}

	/**
	 * 将占位符替换成参数
	 * 
	 * @param path
	 *            路径，可以带占位符“/*”
	 * @param params
	 *            占位符填充参数，用来填充path中“/*”
	 * @return
	 */
	public static String makePath(String path, Object... params) {
		if (StringUtil.isBlank(path)) {
			return "";
		}

		StringBuffer urlParam = new StringBuffer();
		for (Object param : params) {
			urlParam.append("/");
			if (param == null)
				urlParam.append("");
			else
				urlParam.append(param);
		}
		if (params != null && params.length > 0) {
			if (path.indexOf("/*") > -1)
				path = path.replace("/*", urlParam);
			else
				path += urlParam;
		}

		return path;
	}

	public static String getPath(HttpServletRequest req) {
		String path = req.getPathInfo();
		if (path == null)
			return req.getServletPath();

		return path;
	}

	public static String getQueryString(HttpServletRequest req) {
		return req.getQueryString();
	}

	public static String getQueryJsonString(HttpServletRequest req) {
		Map<String, Object> paramMap = new HashMap();
		Enumeration<String> names = req.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String[] value = req.getParameterValues(name);
			if (value != null && value.length > 0) {
				if (value.length == 1) {
					paramMap.put(name, value[0]);
				} else {
					paramMap.put(name, value);
				}
			}
		}
		return Json.toJson(paramMap).replace("\r\n", "").replace("\n\r", "").replace("\n", "").replace("\r", "");
	}

	public static String getURI(HttpServletRequest req) {
		String url = req.getRequestURI();
		if (!StringUtil.isBlank(req.getQueryString()))
			return url + "?" + req.getQueryString();

		return url;
	}

	public static String getURL(HttpServletRequest req) {
		String url = req.getRequestURL().toString();
		if (!StringUtil.isBlank(req.getQueryString()))
			return url + "?" + req.getQueryString();

		return url;
	}

	public static String getUploadPath() {
		String uploadFolder = Cocit.me().getConfig().get(ICocConfig.PATH_UPLOAD);
		if (StringUtil.isBlank(uploadFolder)) {
			uploadFolder = "/upload";
		}
		if (uploadFolder.charAt(0) != '/') {
			uploadFolder = "/" + uploadFolder;
		}

		ITenant tenant = Cocit.me().getHttpContext().getLoginTenant();
		if (tenant == null || tenant.getId() == null)
			return uploadFolder + "/0";

		String tenantKey = tenant.getKey();
		String tenantPath = tenantKey.replace('.', '_');

		return uploadFolder + "/" + tenantPath;
	}

	/**
	 * 
	 * 将表达式串中的变量替换成对象中的字段值。
	 * <p>
	 * 如：expr = "/website/detail.jsp?category=${key}&id=${content.id}" <br>
	 * ${key}：将被“thisObject.getKey()”字段值替换；<br>
	 * ${content.id}：将被“vars.get("content").getId()”字段值替换
	 * 
	 * @param expr
	 * @param thisObject
	 * @param vars
	 * @return
	 */
	public static String parseUrlExpr(String expr, Object thisObject, Map<String, Object> vars) {

		if (expr == null)
			return "";

		while (true) {
			int from = expr.indexOf(Const.VAR_PREFIX);
			if (from > -1) {
				int to = expr.indexOf(Const.VAR_POSTFIX);
				if (to <= from) {
					break;
				}

				String var = expr.substring(from, to + Const.VAR_POSTFIX.length());

				String propName = var.replace(Const.VAR_PREFIX, "").replace(Const.VAR_POSTFIX, "");
				Object obj = thisObject;
				int dot = propName.indexOf(".");
				if (dot > 0 && vars != null) {
					String objProp = propName.substring(0, dot);
					propName = propName.substring(dot + 1);
					obj = vars.get(objProp);
				}
				String propValue = ObjectUtil.getStringValue(obj, propName);

				expr = expr.replace(var, propValue);
			} else {
				break;
			}
		}

		// if (expr.startsWith("/")) {
		// return Cocit.me().getContextPath() + expr;
		// }

		return expr;
	}
}