package com.jsoft.cocit.entityengine.annotation;

/**
 * <b>实体表单注解：</b>用来定义实体操作表单，以方便操作按钮引用。
 */
public @interface CuiForm {

	/**
	 * 用来描述表单的标题，该标题将显示在表单上。用来取代{@link CocAction#name()}
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * 用来描述表单的编号，该名字可以被{@link CocAction#uiForm()}所引用，即表示相应的操作按钮将使用此表单作为操作表单。
	 * 
	 * @return
	 */
	String key() default "";

	/**
	 * 字段列表：用来描述字段在表单中的布局方式i。显示多少行？每行显示多少个字段？
	 * <p>
	 * 语法：propNames="[groupKey1:]prop1|prop2|prop3,prop4,prop5|prop6,...;[groupKey2:]prop11|prop12|prop13,prop14,prop15|prop16,..."
	 * <P>
	 * 多个属性之间用竖线或逗号(|,)分隔，竖线(|)分隔即表示被分隔的字段在一行显示，逗号(,)分隔即表示字段换行显示，属性列表由({@link CocColumn#field()})值组成；
	 * <P>
	 * 列表中的属性顺序即为表单中的字段显示顺序；
	 * <p>
	 * 如：propNames="name|key,parent|type,content,image|keywords"；
	 * 
	 * @return
	 */
	String fields() default "";

	/**
	 * 批处理字段：用来表示哪些字段支持批量添加、修改等。
	 * 
	 * @return
	 */
	String batchFields() default "";

	/**
	 * 字段标签位置：可选值"0|1|2|3|4|5|127"，默认由平台根据字段类型自动计算，比如批处理表单(batching=true)，标签位置为(5-top)
	 * <UL>
	 * <LI>0(auto)：自动，即使用开发平台自动提供的默认位置；
	 * <LI>1(left)：左边，即将字段标签显示在输入框的左边；
	 * <LI>2(right)：右边，即将字段标签显示在输入框的右边；TODO:不支持
	 * <LI>3(up)：上边，即将字段标签显示在输入框的上边；TODO:不支持
	 * <LI>4(down)：下边，即将字段标签显示在输入框的下边；TODO:不支持
	 * <LI>5(top)：顶部，即将字段标签显示在输入框的顶部；通常用于批处理类操作。
	 * <LI>127(none)：不显示，即表示不显示字段标签；TODO:不支持
	 * </UL>
	 * 
	 * @return
	 */
	byte fieldLabelPos() default 0;

	/**
	 * 字段列表：是属性列表中字段的详细描述，是对属性列表{@link #fields()}的补充。详情请参见{@link CuiFormField}
	 * 
	 * @return
	 */
	CuiFormField[] fieldsDetail() default {};

	/**
	 * 表单样式：对应 FORM 元素的 style="..." 属性
	 * 
	 * @return
	 */
	String style() default "";

	/**
	 * 表单引用样式：对应 FORM 元素的 class="..." 属性
	 * 
	 * @return
	 */
	String styleClass() default "";

	// /**
	// * 子模块表单：用于主从结构的子模块表单。子模块的表单将在子模块中定义，此处只是引用。
	// * <p>
	// * 语法：subForms={"items",""};
	// *
	// * @return
	// */
	// String[] subForms() default {};

	/**
	 * 表单操作：引用实体操作定义，多个操作之间用“|”分隔
	 * 
	 * @return
	 */
	String actions() default "";

	/**
	 * top left | top center | top right | bottom left | bottom center | bottom right
	 * <p>
	 * 
	 * @return
	 */
	String actionsPos() default "";

	CuiFormAction[] actionsDetail() default {};

}