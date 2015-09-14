package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.Writer;
import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.control.UISearchBox;
import com.jsoft.cocit.ui.view.BaseModelView;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

public class UISearchBoxView extends BaseModelView<UISearchBox> {
	public String getName() {
		return ViewNames.VIEW_SEARCHBOX;
	}

	public void render(Writer out, UISearchBox model) throws Exception {

		// 下拉菜单：分类查询
		List<Option> list = model.getData();

		String token = Cocit.me().getHttpContext().getClientUIToken();
		int width = model.get("width", 250);

		write(out, "<input id=\"%s\" class=\"jCocit-ui jCocit-searchbox\" data-options=\"token:'%s', resultUI: %s, prompt:'', height: 26, width: %s, onSearch: jCocit.entity.doSearch",//
		        model.getId(),//
		        token,//
		        StringUtil.toJSArray(model.getResultUI()),//
		        width//
		);

		// 下拉菜单：分类查询
		if (!ObjectUtil.isNil(list)) {
			write(out, ", menu:'#searchbox_menu_%s'", token);
		}

		write(out, "\"/>");

		// 下拉菜单：分类查询
		if (!ObjectUtil.isNil(list)) {
			write(out, "<div id=\"searchbox_menu_%s\" data-options=\"minWidth:150\">", token);

			write(out, "<div data-options=\"name: ''\">关键字</div>");
			int count = 0;
			String value;
			String key;
			byte type = 0;
			for (Option obj : list) {
				value = obj.getValue();
				key = obj.getText();
				type = obj.get("type", (byte) 0);

				switch (type) {
					case Const.FIELD_TYPE_NUMBER:
					case Const.FIELD_TYPE_DATE:
						write(out, "<div data-options=\"name: '%s'\">%s", value, key);
						break;
					default:
						write(out, "<div data-options=\"name: '%s'\">%s", value, key);
						break;
				}

				// 操作符子菜单
				// switch (type) {
				// case Const.FIELD_TYPE_NUMBER:
				// case Const.FIELD_TYPE_DATE:
				// write(out, "<div>");
				// write(out, "<div data-options=\"name: '%s eq'\">%s 等于...</div>", value, key);
				// write(out, "<div data-options=\"name: '%s ne'\">%s 不等于...</div>", value, key);
				// write(out, "<div data-options=\"name: '%s lt'\">%s 小于...</div>", value, key);
				// write(out, "<div data-options=\"name: '%s le'\">%s 小于等于...</div>", value, key);
				// write(out, "<div data-options=\"name: '%s gt'\">%s 大于...</div>", value, key);
				// write(out, "<div data-options=\"name: '%s ge'\">%s 大于等于...</div>", value, key);
				// write(out, "<div data-options=\"name: '%s in'\">%s 在...中</div>", value, key);
				// write(out, "<div data-options=\"name: '%s ni'\">%s 不在...中</div>", value, key);
				// write(out, "<div data-options=\"name: '%s gl'\">%s 在...之间</div>", value, key);
				// write(out, "</div>");
				// break;
				// case Const.FIELD_TYPE_RICHTEXT:
				// case Const.FIELD_TYPE_STRING:
				// case Const.FIELD_TYPE_TEXT:
				// case Const.FIELD_TYPE_UPLOAD:
				// write(out, "<div style=\"width:60px;\">");
				// write(out, "<div data-options=\"name: '%s eq'\">%s 等于...</div>", value, key);
				// write(out, "<div data-options=\"name: '%s ne'\">%s 不等于...</div>", value, key);
				// write(out, "<div data-options=\"name: '%s nu'\">%s 为空</div>", value, key);
				// write(out, "<div data-options=\"name: '%s nn'\">%s 不为空</div>", value, key);
				// write(out, "<div data-options=\"name: '%s cn'\">%s 包含...</div>", value, key);
				// write(out, "<div data-options=\"name: '%s nc'\">%s 不包含...</div>", value, key);
				// write(out, "<div data-options=\"name: '%s sw'\">%s 以...开头</div>", value, key);
				// write(out, "<div data-options=\"name: '%s bn'\">%s 不以...开头</div>", value, key);
				// write(out, "<div data-options=\"name: '%s ew'\">%s 以...结尾</div>", value, key);
				// write(out, "<div data-options=\"name: '%s en'\">%s 以...结尾</div>", value, key);
				// write(out, "<div data-options=\"name: '%s in'\">%s 在...中</div>", value, key);
				// write(out, "<div data-options=\"name: '%s ni'\">%s 不在...中</div>", value, key);
				// write(out, "</div>");
				// break;
				// default:
				//
				// }

				write(out, "</div>");
				count++;
				if (count == 12)
					break;
			}
			write(out, "</div>");
		}

	}

}
