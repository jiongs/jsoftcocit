package com.jsoft.cocimpl.ui.view;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

import com.jsoft.cocimpl.ui.view.jcocitfield.CKEditorFieldView;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.ui.model.control.UIField;
import com.jsoft.cocit.ui.view.UIFieldView;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.IteratorAdapter;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

public abstract class BaseFieldView implements UIFieldView {

	@Override
	public void render(Writer out, UIFieldModel fieldModel, Object entityObject, String fieldName, Object fieldValue) throws Exception {
		int mode = fieldModel.getMode();

		CocFieldService entityFieldService = ((UIField) fieldModel).getFieldService();

		String prefix = fieldName.replace(entityFieldService.getFieldName(), "");

		String idFieldName = "";
		String textFieldName = "";
		String idValue = "";
		String textValue = "";
		String targetIdField = "";
		if (entityFieldService.getFkTargetFieldKey() != null) {
			targetIdField = entityFieldService.getFkTargetFieldKey();
		}
		String targetTextField = "";

		boolean isFkOneField = entityFieldService.isManyToOne();
		CocFieldService nameField = entityFieldService.getEntity().getFkNameField(entityFieldService.getFieldName());
		if (nameField != null) {
			targetTextField = nameField.getFkTargetFieldKey();
			textFieldName = prefix + nameField.getFieldName();
		} else {
			if (isFkOneField) {
				targetTextField = Const.F_NAME;
			} else {
				targetTextField = targetIdField;
			}
		}
		if (isFkOneField) {
			idFieldName = fieldName + "." + targetIdField;
			textFieldName = fieldName + "." + targetTextField;
		} else {
			idFieldName = fieldName;
		}

		if (fieldValue != null) {
			if (isFkOneField) {
				/*
				 * 加载外键对象的相关对象值
				 */
				Class targetClassOfEntity = fieldValue.getClass();
				Object targetIdValue = ObjectUtil.getValue(fieldValue, targetIdField);
				Object fkObject = fieldValue;
				if (targetIdValue != null) {
					idValue = targetIdValue.toString();

					if (!ClassUtil.isAgent(targetClassOfEntity)) {
						fkObject = Cocit.me().orm().get(targetClassOfEntity, Expr.fieldRexpr(targetIdField + "|" + targetTextField).and(Expr.eq(targetIdField, targetIdValue)));
					}
				}
				if (fkObject != null) {
					Object targetTextValue = ObjectUtil.getValue(fkObject, targetTextField);
					if (targetTextValue != null) {
						textValue = targetTextValue.toString();
					} else {
						textValue = idValue;
					}
				} else {
					textValue = idValue;
				}
			} else {
				idValue = fieldValue.toString();

				if (targetIdField.equals(targetTextField)) {
					Option option = fieldModel.getDicOption(fieldValue);
					if (option != null) {
						textValue = option.getText();
					} else {
						textValue = entityFieldService.formatFieldValue(fieldValue);
					}
				} else if (StringUtil.hasContent(idValue)) {

					if (nameField != null) {
						textValue = ObjectUtil.getStringValue(entityObject, nameField.getFieldName());
					} else {
						CocEntityService targetModule = entityFieldService.getFkTargetEntity();
						Class targetClassOfEntity = targetModule.getClassOfEntity();

						List<String> idList = StringUtil.toList(idValue);

						List<Object> fkObjectList = Cocit.me().orm().query(targetClassOfEntity, Expr.fieldRexpr(targetIdField + "|" + targetTextField).and(Expr.in(targetIdField, idList)));
						if (fkObjectList != null) {
							for (Object fkObject : fkObjectList) {
								Object targetTextValue = ObjectUtil.getValue(fkObject, targetTextField);
								if (targetTextValue != null) {
									textValue += "," + targetTextValue.toString();
								}
							}
							if (textValue.length() > 0) {
								textValue = textValue.substring(1);
							}
						}
					}

				}
			}
		}

		// write(out, "<div style=\"position: relative;\">");
		int uiMode = FieldModes.parseUiMode(mode);

		String escapeHtml = textValue;
		if (!CKEditorFieldView.class.isAssignableFrom(this.getClass())) {
			escapeHtml = StringUtil.escapeHtml(textValue);//
		}

		switch (uiMode) {

			case FieldModes.H:// H：隐藏字段
				write(out, "<input type=\"hidden\" name=\"%s\" value=\"%s\" />", //
				        idFieldName,//
				        idValue//
				);
				break;

			case FieldModes.I:// I：检查字段值，字段值被隐藏且可以提交。
				write(out, "<input type=\"hidden\" name=\"%s\" value=\"%s\" />",//
				        idFieldName,//
				        idValue//
				);
				write(out, "<span>%s</span>", //
				        escapeHtml//
				);
				break;

			case FieldModes.R:// R：只读字段显示的是字段文本，隐藏的是字段值。
				write(out, "<input type=\"hidden\" name=\"%s\" value=\"%s\" />",//
				        idFieldName,//
				        idValue//
				);
				write(out, "<input class=\"input\" type=\"text\" name=\"%s\" value=\"%s\" readonly=\"true\" />", //
				        textFieldName,//
				        escapeHtml//
				);
				break;

			case FieldModes.D:// D：禁用字段，字段值不能被提交
				write(out, "<input class=\"input\" type=\"text\" name=\"%s\" value=\"%s\" disabled=\"true\" />", //
				        textFieldName,//
				        escapeHtml//
				);
				break;
			case FieldModes.S:// S、P：显示字段值文本
			case FieldModes.P:

				write(out, "<span>%s</span>", //
				        escapeHtml//
				);
				break;

			case FieldModes.E:// E：编辑模式
			case FieldModes.M:

				String str = getElementBegin();
				if (StringUtil.hasContent(str)) {
					write(out, str);

					write(out, " id=\"%s\" name=\"%s\" ",//
					        fieldModel.getId(),//
					        idFieldName//
					);
				}

				this.renderElementAttr(out, fieldModel, entityObject, fieldValue, idFieldName, textFieldName, idValue, textValue, targetIdField, targetTextField);

				str = getElementEnd();
				if (StringUtil.hasContent(str)) {
					write(out, str);
				}
		}

		// write(out, "</div>");F
	}

	protected String getElementBegin() {
		return "<input";
	}

	/**
	 * 输出输入框元素属性，不包括 id|name
	 * 
	 * @param out
	 * @param fieldModel
	 * @param entityObject
	 * @param fieldValue
	 * @param idFieldName
	 * @param textFieldName
	 * @param idValue
	 * @param textValue
	 * @param targetIdField
	 * @param targetTextField
	 * @throws Exception
	 */
	protected abstract void renderElementAttr(//
	        Writer out, //
	        UIFieldModel fieldModel,//
	        Object entityObject,//
	        Object fieldValue,//
	        String idFieldName,//
	        String textFieldName,//
	        String idValue,//
	        String textValue,//
	        String targetIdField,//
	        String targetTextField //
	) throws Exception;

	protected void renderAttrs(Writer out, Properties attrs) throws IOException {
		IteratorAdapter keys = new IteratorAdapter(attrs.keys());
		while (keys.hasNext()) {
			String key = (String) keys.next();
			String val = attrs.getProperty(key);

			write(out, " %s=\"%s\"", key, val);
		}
	}

	protected String getElementEnd() {
		return "/>";
	}

	protected void write(Writer out, String format, Object... args) throws IOException {
		out.write(String.format(format, args));
	}
}
