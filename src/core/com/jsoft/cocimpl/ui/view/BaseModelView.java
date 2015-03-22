package com.jsoft.cocimpl.ui.view;

import java.io.IOException;
import java.io.Writer;

import com.jsoft.cocimpl.ui.UIView;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.StringUtil;

public abstract class BaseModelView<T extends UIModel> implements UIView<T> {

	protected void toJson(StringBuffer sb, String field, Object value) {
		if (value == null)
			return;
		if (value instanceof String && StringUtil.isBlank((String) value))
			return;

		sb.append("\n\t, \"" + field + "\": ").append(JsonUtil.toJson(value == null ? null : value.toString()));
	}

	protected void append(StringBuffer out, String format, Object... args) {
		out.append('\n');
		if (args.length == 0)
			out.append(format);
		else
			out.append(String.format(format, args));
	}

	protected void write(Writer out, String format, Object... args) throws IOException {
		out.write("\n");
		if (args.length == 0)
			out.write(format);
		else
			out.write(String.format(format, args));
	}

}
