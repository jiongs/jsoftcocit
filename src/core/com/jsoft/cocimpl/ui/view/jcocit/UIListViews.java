package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.Writer;
import java.util.List;

import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.control.UIList;
import com.jsoft.cocit.ui.model.datamodel.UIListData;
import com.jsoft.cocit.ui.view.BaseModelView;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.ObjectUtil;

public abstract class UIListViews {

	public static class UIListView extends BaseModelView<UIList> {
		public String getName() {
			return ViewNames.VIEW_LIST;
		}

		public void render(Writer out, UIList model) throws Exception {
			// TODO:
		}
	}

	public static class UIListDataView extends BaseModelView<UIListData> {
		public String getName() {
			return ViewNames.VIEW_LISTDATA;
		}

		public void render(Writer out, UIListData model) throws Exception {
			// ListWidget gridModel = getModel();
			// List<Column> columns = gridModel.getColumns();

			List data = (List) model.getData();
			if (data != null) {

				boolean noFirstRow = false;
				StringBuffer sb = new StringBuffer();
				sb.append('[');
				for (Object obj : data) {

					if (noFirstRow) {
						sb.append(",{");
					} else {
						sb.append('{');
						noFirstRow = true;
					}

					sb.append(String.format("\"value\":%s", JsonUtil.toJson(ObjectUtil.getValue(obj, "code"))));
					sb.append(String.format(",\"text\":%s", JsonUtil.toJson(obj == null ? "" : obj.toString())));

					// for (Column col : columns) {
					// String prop = col.getField();
					// Object value = ObjectUtil.getValue(obj, prop);
					// value = col.getEntityField().format(value);
					//
					// sb.append(String.format(",\"%s\":%s", prop, Json.toJson(value)));
					// }

					sb.append('}');
				}
				sb.append(']');

				out.write(sb.toString());
			}
		}
	}
}
