package com.jsoft.cocit.ui.model.datamodel;

import java.io.Writer;
import java.util.Iterator;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIDataModel;
import com.jsoft.cocit.util.JsonUtil;

public class JSONModel extends UIDataModel {

	protected String content;

	/**
	 * 创建JSON模型，输出JSON文本。
	 * 
	 * @param content
	 *            JSON文本
	 */
	public static JSONModel make(String content) {
		JSONModel model = new JSONModel();

		model.content = content;

		return model;
	}

	@Override
	public void release() {
		super.release();
		this.content = null;
	}

	protected JSONModel() {
	}

	public String getViewName() {
		return ViewNames.VIEW_JSON;
	}

	public String getContent() {
		if (!content.startsWith("{")) {

			StringBuffer sb = new StringBuffer();
			sb.append('{');
			sb.append("\"content\": ").append(JsonUtil.toJson(content));
			Iterator<String> keys = context.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				Object value = context.get(key);
				sb.append(",\"" + key + "\": ").append(JsonUtil.toJson(value));
			}

			sb.append('}');

			return sb.toString();
		}

		return content;
	}

	public void render(Writer out) throws Exception {

	}
}
