package com.jsoft.cocimpl.ui.view.jcocitfield;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.view.UIFieldView;
import com.jsoft.cocit.util.PropertiesUtil;

public class PropertiesFieldView implements UIFieldView {

	@Override
	public String getName() {
		return ViewNames.FIELD_VIEW_PROPERTIES;
	}

	@Override
	public void render(Writer out, UIFieldModel fieldModel, Object entityObject, String fieldName, Object fieldValue) throws Exception {
		String str = fieldValue == null ? "" : fieldValue.toString();

		StringBuffer html = new StringBuffer();
		try {
			Properties props = PropertiesUtil.toProps(str);
			Enumeration keys = props.keys();
			html.append("<table>");
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = props.getProperty(key);
				html.append("<tr><th>").append(key).append("</th><td>").append(value).append("</td></tr>");
			}
			html.append("</table>");
		} catch (IOException e) {
			html.append(str);
		}

		out.write(html.toString());
	}
}
