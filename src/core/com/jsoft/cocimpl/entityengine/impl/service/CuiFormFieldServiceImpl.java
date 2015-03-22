package com.jsoft.cocimpl.entityengine.impl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.json.Json;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.entity.cui.ICuiFormField;
import com.jsoft.cocit.entityengine.service.CuiFormFieldService;
import com.jsoft.cocit.entityengine.service.CuiFormService;
import com.jsoft.cocit.entityengine.service.DicItemService;
import com.jsoft.cocit.entityengine.service.DicService;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

public class CuiFormFieldServiceImpl extends NamedEntityServiceImpl<ICuiFormField> implements CuiFormFieldService {

	private Option[] dicOptions;
	private CuiFormServiceImpl cuiForm;

	CuiFormFieldServiceImpl(ICuiFormField field, CuiFormServiceImpl cuiForm) {
		super(field);
		this.cuiForm = cuiForm;
	}

	@Override
	public Option getDicOption(Object fieldValue) {
		if (fieldValue == null)
			return null;

		String value = fieldValue.toString();
		if (fieldValue instanceof Boolean) {
			value = ((Boolean) fieldValue) ? "1" : "0";
		}

		return makeDicOptionsMap().get(value);
	}

	private Map<Object, Option> makeDicOptionsMap() {
		Map<Object, Option> ret = new HashMap();
		Option[] options = this.getDicOptionsArray();
		if (options != null) {
			for (Option kv : options) {
				ret.put(kv.getValue(), kv);
			}
		}

		return ret;
	}

	@Override
	public Option[] getDicOptionsArray() {
		if (dicOptions != null)
			return dicOptions;

		String strOptions = entityData.getDicOptions();
		if (!StringUtil.isBlank(strOptions)) {

			// 转换字符串为 KeyValue[]。
			dicOptions = makeDicOptions(strOptions);

		} else
			dicOptions = new Option[0];

		return dicOptions;
	}

	private Option[] makeDicOptions(String strOptions) {
		strOptions = strOptions.trim();
		if (strOptions.charAt(0) == '[') {
			try {
				return Json.fromJson(Option[].class, strOptions);
			} catch (Throwable e) {
				LogUtil.error("解析字段字典选项列表时出错：非法字典选项表达式！(%s)", strOptions, e);
			}
		} else if (strOptions.startsWith(Const.VAR_PREFIX)) {

			List<Option> oplist = new ArrayList();
			DicService catalog = (DicService) this.esf().getDic(strOptions.substring(Const.VAR_PREFIX.length(), strOptions.length() - 1));
			List<DicItemService> list = catalog.getItems();
			for (DicItemService item : list) {
				if (!item.isDisabled() && !item.isDisabled())
					oplist.add(Option.make(item.getText(), item.getValue()));
			}

			Option[] dicOptions = new Option[oplist.size()];
			dicOptions = oplist.toArray(dicOptions);

			return dicOptions;
		} else {
			String[] strs = StringUtil.toArray(strOptions, ",;，；\r\t\n");
			Option[] options = new Option[strs.length];
			int i = 0;
			for (String item : strs) {
				item = item.trim();
				int idx = item.indexOf(":");
				if (idx < 0) {
					idx = item.indexOf("：");
				}
				if (idx > -1) {
					options[i++] = Option.make(item.substring(idx + 1).trim(), item.substring(0, idx).trim());
				} else {
					options[i++] = Option.make(item, item);
				}
			}
			return options;
		}

		return new Option[0];
	}

	@Override
	public CuiFormService getCuiForm() {
		return cuiForm;
	}

	@Override
	public int getModeValue() {
		return FieldModes.parseMode(getMode());
	}

	@Override
	public String getCocEntityKey() {
		return entityData.getCocEntityKey();
	}

	@Override
	public String getCuiEntityKey() {
		return entityData.getCuiEntityKey();
	}

	@Override
	public String getCuiFormKey() {
		return entityData.getCuiFormKey();
	}

	@Override
	public String getMode() {
		return entityData.getMode();
	}

	@Override
	public String getUiView() {
		return entityData.getUiView();
	}

	@Override
	public byte getLabelPos() {
		return entityData.getLabelPos();
	}

	@Override
	public String getStyle() {
		return entityData.getStyle();
	}

	@Override
	public String getStyleClass() {
		return entityData.getStyleClass();
	}

	@Override
	public String getOneToManyTargetAction() {
		return entityData.getOneToManyTargetAction();
	}

	@Override
	public String getFieldName() {
		return entityData.getFieldName();
	}

	@Override
	public String getUiViewLinkUrl() {
		return entityData.getUiViewLinkUrl();
	}

	@Override
	public String getUiViewLinkTarget() {
		return entityData.getUiViewLinkTarget();
	}

	@Override
	public String getAlign() {
		return entityData.getAlign();
	}

	@Override
	public byte getColspan() {
		return entityData.getColspan();
	}

	@Override
	public byte getRowspan() {
		return entityData.getRowspan();
	}

	@Override
	public String getDicOptions() {
		return entityData.getDicOptions();
	}

	@Override
	public String getHalign() {
		return entityData.getHalign();
	}

	@Override
	public String getFkComboUrl() {
		return entityData.getFkComboUrl();
	}

	@Override
	public String getFkComboWhere() {
		return entityData.getFkComboWhere();
	}

}
