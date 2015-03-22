package com.jsoft.cocit.entityengine.annotation;

import com.jsoft.cocit.ui.view.UIFieldView;

/**
 * <b>实体表单字段注解：</b>用来描述字段在表单中的展现方式。
 */
public @interface CuiFormField {

	/**
	 * 字段名称
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * 属性名：对应({@link CocColumn#field()})；
	 * 
	 * @return
	 */
	String field();

	// /**
	// * 字段宽：即该字段在Grid中的显示宽度；如果未指定，则会根据字段长度({@link CocColumn#length()})自动计算列宽度；
	// *
	// * @return
	// */
	// int width() default 0;

	// /**
	// * 字段默认值：如果为指定该值，则默认从{@link CocColumn#defalutValue()}解析默认值；
	// * <p>
	// * 如果此处指定了默认值，则在该表单中使用此处指定的默认值。
	// *
	// * @return
	// */
	// String defaultValue() default "";

	/**
	 * 字段选项：如果未指定该值，则使用{@link CocColumn#dicOptions()}作为字典选项；
	 * <p>
	 * 如果此处指定了自定义的字典选项，则必需由开发人员保证，此处的字典选项只能是{@link CocColumn#dicOptions()}的子集。
	 */
	String dicOptions() default "";

	/**
	 * 对齐方式：即该字段在Grid中的的对齐方式，可选值包括(left|center|right)；
	 * <UL>
	 * <LI>auto：自动，即自动计算列对齐方式，数字字段右对齐，其他类型字段均左对齐；
	 * <LI>left：左对齐
	 * <LI>center：居中对齐
	 * <LI>right：右对齐
	 * </UL>
	 * 
	 * @return
	 */
	String align() default "";

	/**
	 * 字段模式：用于描述字段显示模式，如果未指定该值，则通过{@link CocColumn#mode()}解析表单字段显示模式。
	 * <p>
	 * 此处的语法不同于{@link CocColumn#mode()}，此处只需要指定模式值即可。
	 * <p>
	 * 如：mode="M"；mode="MI"；mode="E" 等
	 * 
	 * @return
	 */
	String mode() default "";

	/**
	 * 字段UI View：如果未指定该值，则使用{@link CocColumn#uiView()}。
	 * <p>
	 * 该属性值必需与{@link UIFieldView#getName()}返回值一致，即表示使用相应的{@link UIFieldView}来展现字段。
	 * 
	 * @return
	 */
	String uiView() default "";

	/**
	 * 用于外键字段：当在{@link #uiViewLinkTarget()}指定的位置显示链接数据时，由该属性决定调用“外键实体”的哪个操作查看数据详情？
	 * 
	 * @return
	 */
	String uiViewLinkUrl() default "";

	/**
	 * 数据链接目标：当字段类型为外键、图片等，通过该属性指定打开链接的目标位置，可以是“auto|tabsbar|dialog|_self|_blank|{other}”。
	 * <UL>
	 * <LI>auto：自动，即表示计算链接目标；
	 * <LI>tabsbar：如果当前工作环境中有tabsbar，则在tabsbar中新开一个tab用于显示链接数据；
	 * <LI>dialog：表示在新的dialog窗口中显示链接数据；
	 * <LI>_self：表示在自身窗口中显示链接数据
	 * <LI>_blank：表示在新浏览器窗口中显示链接数据；
	 * </UL>
	 */
	String uiViewLinkTarget() default "";

	/**
	 * 字段标签位置：可选值"0|1|2|3|4|5|9"，，默认由平台根据字段类型自动计算，比如"richtext"字段，标签显示在输入框的上边(3-up)。
	 * <UL>
	 * <LI>0(auto)：自动，即使用开发平台自动提供的默认位置；
	 * <LI>1(left)：左边，即将字段标签显示在输入框的左边；
	 * <LI>2(right)：右边，即将字段标签显示在输入框的右边；
	 * <LI>3(up)：上边，即将字段标签显示在输入框的上边；
	 * <LI>4(down)：下边，即将字段标签显示在输入框的下边；
	 * <LI>5(top)：顶部，即将字段标签显示在输入框的顶部；
	 * <LI>127(none)：不显示，即表示不显示字段标签；
	 * </UL>
	 * 
	 * @return
	 */
	byte labelPos() default 0;

	// /**
	// * 对齐方式：即该字段在Grid中的的对齐方式，可选值包括(left,center,right)；如果为指定该值，则自动数字字段右对齐外，其他类型字段均左对齐；
	// *
	// * @return
	// */
	// String align() default "";

	/**
	 * OneToMany字段操作KEY：用于主从表结构，将子表操作按钮对应的表单作为主表的“OneToMany(mappedBy="")|OneToOne(mappedBy="")”字段UI；以便于维护主表单条数据的同时，一起维护子表数据。
	 * <p>
	 * 该属性只用于“@OneToMany(mappedBy="xxx")字段”或“@OneToOne(mappedBy="xxx")”；
	 * <p>
	 * 其值来自从表实体注解中的{@link CocAction#key()}，即表示引用相应的从表操作按钮表单界面作为OneToMeny字段UI。
	 */
	String oneToManyTargetAction() default "";

	/**
	 * 字段跨越的列数：用于字段输入框TD的colspan属性。
	 * 
	 * @return
	 */
	byte colspan() default 1;

	/**
	 * 字段跨域的行数：用于字段标签和字段输入框TD的rowspan属性。
	 * 
	 * @return
	 */
	byte rowspan() default 1;

	/**
	 * 字段样式：对应 HTML 元素的 style="..." 属性
	 * 
	 * @return
	 */
	String style() default "";

	/**
	 * 字段引用样式：对应 HTML 元素的 class="..." 属性
	 * 
	 * @return
	 */
	String styleClass() default "";

	/**
	 * 外键字段查询条件
	 * 
	 * <b>条件规则：</b>维护当前数据时，需要通过Combo组件选择字段值，该属性即用来指定可选项的过滤条件。
	 * 
	 * <b>语法：</b>
	 * <UL>
	 * <LI>1. JSON语法： { field1 : value1, field2 : [value2-1, value2-2, value2-3] }
	 * <LI>2. 伪SQL语法：((field1 op value1) and (field2 op value2)) or field3 in (v1,v2,v3)
	 * <LI>3. 注：上两种语法不能混用！！！ 操作类型(op)参见枚举类API文档 “com.jsoft.cocit.orm.expr. CndType”
	 * </UL>
	 * 
	 * @return
	 */
	String fkComboWhere() default "";

	/**
	 * 外键字段数据加载地址：即可以指定从自定义的Action地址加载UI组件所需要的JSON数据。
	 * 
	 * @return
	 */
	String fkComboUrl() default "";
}