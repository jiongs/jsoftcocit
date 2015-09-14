package com.jsoft.cocimpl.ui.view.jcocit;

import java.io.Writer;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.ui.UIViews;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.model.control.UISearchBox;
import com.jsoft.cocit.ui.view.BaseModelView;
import com.jsoft.cocit.ui.view.UIFieldView;
import com.jsoft.cocit.util.StringUtil;

public class UISearchFormView extends BaseModelView<UISearchBox> {
	public String getName() {
		return ViewNames.VIEW_SEARCHFORM;
	}

	public void render(Writer out, UISearchBox model) throws Exception {
		String token = Cocit.me().getHttpContext().getClientUIToken();
		UIViews views = Cocit.me().getViews();
		HttpContext ctx = Cocit.me().getHttpContext();

		/*
		 * 创建 FORM 标签：用DIV代替FORM，因为当DataGrid支持行编辑，且DataGrid外套有FORM标签时，这里FORM标签将失效。
		 */
		write(out, "<div id=\"%s\" class=\"searchForm jCocit-searchform\" data-options=\"resultUI: %s, token: '%s'\" >",//
		        model.getId(), //
		        StringUtil.toJSArray(model.getResultUI()),//
		        token//
		);
		write(out, "<table class=\"searchTable\"><tr>");
		int count = 0;
		int width = model.get("width", ctx.getClientUIWidth());
		int size = width / 200;
		boolean buttonExisted = false;
		for (UIFieldModel uiField : model.getFields()) {
			if (count > 0 && count % size == 0) {
				write(out, "<td colspan=2>");
				write(out, "<div class=\"MoreBtn\"></div><a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-button jCocit-dosearch\" data-options=\"onClick: jCocit.entity.doSearch\">%s</a>", "查询");
				write(out, "</td>");
				write(out, "</tr><tr class=\"MoreFlds Hidden\">");
				buttonExisted = true;
			}
			write(out, "<th class=\"entityFieldHeader\">%s:</th>", uiField.getTitle());

			write(out, "<td class=\"entityFieldBox\">");
			UIFieldView view = views.getFieldView(uiField.getViewName());
			String fieldname = uiField.getPropName();
			UIField f = (UIField) uiField;
			ICocFieldInfo fs = f.getFieldService();
			if (fs != null) {
				switch (fs.getFieldType()) {
					case Const.FIELD_TYPE_STRING:
					case Const.FIELD_TYPE_RICHTEXT:
					case Const.FIELD_TYPE_TEXT:
					case Const.FIELD_TYPE_UPLOAD:
						fieldname += " cn";
						break;
					case Const.FIELD_TYPE_FK:
					case Const.FIELD_TYPE_FK_REDUNDANT:
						f.setMultiple(true);
						f.set("checkOnSelect", true);
						f.set("selectOnCheck", true);
						f.set("onCheck", "jCocit.entity.doSearch");
						break;
					default:
						fieldname += " eq";
						break;

				}
			} else {
				fieldname += " eq";
			}

			view.render(out, uiField, null, fieldname, null);
			write(out, "</td>");

			count++;
		}
		if (!buttonExisted) {
			write(out, "<td colspan=2>");
			String buttonStyle = Cocit.me().getConfig().getViewConfig().getButtonStyle();
			if ("button".equals(buttonStyle)) {
				write(out, "<button class=\"coc-btn\" onClick=\"jCocit.entity.doSearch(this)\">%s</button>", "查询");
				write(out, "<button class=\"coc-btn\" onClick=\"jCocit.util.resetForm(this)\">%s</button>", "重置");
			} else {
				write(out, "<a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-button\" data-options=\"onClick: jCocit.entity.doSearch\"\">%s</a>", "查询");
				write(out, "<a href=\"javascript:void(0)\" class=\"jCocit-ui jCocit-button\" data-options=\"onClick: jCocit.util.resetForm\"\">%s</a>", "重置");
			}
			write(out, "</td>");
		}
		write(out, "</tr>");

		write(out, "</table>");
		write(out, "</div>");// 用DIV代替FORM
	}

}
