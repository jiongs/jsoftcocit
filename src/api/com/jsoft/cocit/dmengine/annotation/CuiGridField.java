package com.jsoft.cocit.dmengine.annotation;

import com.jsoft.cocit.ui.view.UICellView;

/**
 * <b>实体Grid列注解：</b>用来描述字段在GRID列中的表现形式。
 */
public @interface CuiGridField {
	/**
	 * 列名称
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

	/**
	 * 列宽：即该字段在Grid中的显示宽度
	 * <p>
	 * 0——前端自动列宽<br>
	 * -1——后台自动计算列宽
	 * 
	 * @return
	 */
	int width() default -1;

	/**
	 * 是否在GRID中隐藏该字段？
	 */
	boolean hidden() default false;
	
	/**
	 * 对齐方式：即该字段在Grid中的的对齐方式，可选值包括(left|center|right)；
	 * <UL>
	 * <LI>默认：自动，即自动计算列对齐方式，数字字段右对齐，其他类型字段均左对齐；
	 * <LI>left：左对齐
	 * <LI>center：居中对齐
	 * <LI>right：右对齐
	 * </UL>
	 * 
	 * @return
	 */
	String align() default "";

	/**
	 * 表头对齐方式：即该字段在Grid中的的对齐方式，可选值包括(left|center|right)；
	 * <UL>
	 * <LI>默认：自动，即自动计算列对齐方式，数字字段右对齐，其他类型字段均左对齐；
	 * <LI>left：左对齐
	 * <LI>center：居中对齐
	 * <LI>right：右对齐
	 * </UL>
	 * 
	 * @return
	 */
	String halign() default "";

	/**
	 * 单元格数据展现方式：用来表示数据在GRID单元格中的展现方式，该属性值对应({@link UICellView#getName()})。
	 * <p>
	 * 如：cellView="link" 即表示在GRID中以链接方式显示字段值，当点击链接时，可以查看相关明细数据。 <br/>
	 * cellView="linkToForm"
	 * 
	 * @return
	 */
	String cellView() default "";

	/**
	 * 用于外键字段：当在{@link #cellViewLinkTarget()}指定的位置显示链接数据时，由该属性决定调用“外键实体”的哪个操作查看数据详情？
	 * <p>
	 * 如：
	 * <UL>
	 * <LI>@CuiGridField(field="category", cellView="linkToForm", cellViewLinkUrl="/cocentity/getFormToSave/web_category:web_category:e/${category.id}")
	 * <LI>@CuiGridField(field="name", cellView="linkToForm", cellViewLinkUrl="/cocentity/getFormToSave/web_content:web_content:v/${id}")
	 * <LI>@CuiGridField(field="name", cellView="link", cellViewLinkUrl="${runtimeLinkUrl}")
	 * </UL>
	 */
	String cellViewLinkUrl() default "";

	/**
	 * 单元格数据链接目标：当(cellView="link")时，通过该属性指定打开链接的目标位置，可以是“auto|tabsbar|dialog|_self|_blank|{other}”。
	 * <UL>
	 * <LI>auto：自动，即表示计算链接目标；
	 * <LI>tabsbar：如果当前工作环境中有tabsbar，则在tabsbar中新开一个tab用于显示链接数据；
	 * <LI>dialog：表示在新的dialog窗口中显示链接数据；
	 * <LI>_self：表示在自身窗口中显示链接数据
	 * <LI>_blank：表示在新浏览器窗口中显示链接数据；
	 * </UL>
	 */
	String cellViewLinkTarget() default "";

	/**
	 * 是否显示单元格tip信息，主要用于当单元格数据过长时，鼠标放在单元格上即显示完整信息。
	 * 
	 * @return
	 */
	boolean cellTips() default false;

	/**
	 * 语法规则同{@link CuiGrid#rowStyle()}
	 * <p>
	 * 举例1：cellStyle = "[{where: {statusCode: 0}, style: 'color: #334455;'}]"<br/>
	 * 举例2：cellStyle = "[{where: {statusCode: [-1, -2]}, style: 'color: red'}]"
	 */
	String cellStyle() default "";
}