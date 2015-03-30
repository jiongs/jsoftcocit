package com.jsoft.cocit.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import org.nutz.json.Json;
import org.nutz.lang.Files;

import com.jsoft.cocimpl.util.json.BaseJson;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.exception.CocConfigException;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public abstract class JsonUtil {
	private static BaseJson json = BaseJson.DEFAULT;

	public static String toJson(Object obj) {
		return json.toJson(obj);
	}

	public static void toJson(Writer writer, Object obj) {
		json.toJson(writer, obj);
	}

	public static List loadFromJson(String str) {
		return json.fromJson(str);
	}

	public static <T> T loadFromJsonOrFile(Class<T> type, String str) {
		List<T> array = new LinkedList();

		ICocConfig commonConfig = Cocit.me().getConfig();
		T[] array1 = null;
		T[] array2 = null;
		InputStream is = null;

		InputStream is2 = null;
		String json1 = null;
		String json2 = null;
		try {
			if (str.trim().startsWith("[") && str.endsWith("]")) {
				json1 = str;
			} else {
				String path = str;

				// 读取JSON文件
				if (str.startsWith("/") || str.startsWith("\\")) {
					// 从上下文环境中读取文件
					path = Cocit.me().getContextDir() + str;
					is = Files.findFileAsStream(path);
				} else {
					// 从classes目录下读取文件
					path = commonConfig.getClassDir() + "/" + str;
					is = Files.findFileAsStream(path);
					if (is == null) {// 从classes下的配置路径下读取文件
						path = commonConfig.getClassDir() + "/" + commonConfig.getConfigPkg().replace(".", "/") + "/" + str;
						is = Files.findFileAsStream(path);
					}

					// 从jar包中加载JSON流
					if (is == null) {
						is = Files.findFileAsStream(str);
					}
					if (is == null) {
						path = type.getPackage().getName().replace(".", "/") + "/" + str;
						is = Files.findFileAsStream(path);
					}
					if (is == null) {
						path = commonConfig.getConfigPkg().replace(".", "/") + "/" + str;
						is = Files.findFileAsStream(path);
					}
					// 安装软件特有的数据
					// if (Demsy.me().getSoft() != null) {
					// String path2 = config.getConfigDir() + "/" + Demsy.me().getSoft().getCode() + "/"
					// + json;
					// is2 = Files.findFileAsStream(path2);
					// }
				}

				if (is == null && is2 == null)
					return null;

				if (is != null)
					json1 = FileUtil.readAll(is, "UTF-8");
				if (is2 != null)
					json2 = FileUtil.readAll(is2, "UTF-8");
			}
			if (json1 != null)
				array1 = (T[]) Json.fromJson(ClassUtil.forName(type.getName() + "[]"), json1);
			if (json2 != null)
				array2 = (T[]) Json.fromJson(ClassUtil.forName(type.getName() + "[]"), json2);

		} catch (Throwable e) {

			throw new CocConfigException("文件内容非法! %s [file: %s]", ExceptionUtil.msg(e), str);

		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
			if (is2 != null)
				try {
					is2.close();
				} catch (IOException e) {
				}
		}

		if (array1 != null)
			for (T e : array1)
				array.add(e);
		if (array2 != null)
			for (T e : array2)
				array.add(e);

		return (T) array;
	}

	public static <T> T loadFromJson(Class<T> type, String str) {
		return json.fromJson(type, str);
	}
}
