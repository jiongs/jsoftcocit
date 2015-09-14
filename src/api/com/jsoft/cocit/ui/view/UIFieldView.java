package com.jsoft.cocit.ui.view;

import java.io.Writer;

import com.jsoft.cocit.dmengine.annotation.CocColumn;
import com.jsoft.cocit.ui.model.UIFieldModel;
import com.jsoft.cocit.util.StringUtil;

/**
 * <B>字段窗体Render：</B>用来输出【{@link CocColumn#uiView()}】中指定的字段窗体。
 * <UL>
 * <LI>实现类名命名规则：如“ComboboxWidgetRender, CombogridWidgetRender, CombotreeWidgetRender, NumberboxWidgetRender”等；
 * <LI>实现类包名：扩展的适配器包名为“com.jha.cocit.ui.view”；
 * <LI>生成的HTML片段中，属性值用双引号(")括起来，不能用单引号(')，如：&lt;input name="entity.date" value="2014-12-03" /&gt;合法，<br>
 * &lt;input name='entity.date' value='2014-12-03' /&gt;非法，
 * </UL>
 * 将窗体HTML代码片段输出到Writer中。
 * 
 * @author Ji Yongshan
 * 
 */
public interface UIFieldView {

	/**
	 * <B>视图名字：</B>如果【{@link CocColumn#uiView()}】中指定的窗体名字与这里的名字相同，则使用该Render来输出字段UI。
	 * 
	 */
	String getName();

	/**
	 * 输出窗体界面
	 * 
	 * @param out
	 *            HTML代码片段将输出到这里
	 * @param fieldModel
	 *            字段UI
	 * @param dataObject
	 *            表单数据，即实体数据对象
	 * @param fieldName
	 *            字段名称，用作INPUT标签的name。如：&lt;input name="entity.date" /&gt;，"entity.name"为字段名。
	 * @param fieldValue
	 *            字段值，是一个对象类型，如Integer/Date/Boolean/Object等，<br>
	 *            首先调用{@link UIFieldModel#format(Object)}对其进行格式化，<br>
	 *            其次调用{@link StringUtil#escapeHtml(String)}对字段值进行转义，<br>
	 *            然后才能作为INPUT标签的value。 如：&lt;input name="entity.date" value="2014-12-03" /&gt;<br>
	 */
	void render(Writer out, UIFieldModel fieldModel, Object dataObject, String fieldName, Object fieldValue) throws Exception;
}
