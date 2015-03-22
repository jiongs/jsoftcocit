package com.jsoft.cocit.entityengine.annotation;

/**
 * <b>实体Grid注解：</b>用于描述实体模块主界面中的GRID风格。
 * 
 * @return
 */
public @interface CuiGrid {
	String key() default "";

	/**
	 * 锁定列字段列表：用来表示哪些字段将在Grid中显示并锁定，即滚动条滚动时，锁定列将不会滚动。
	 * <P>
	 * 多个属性之间用竖线(|)分隔，属性列表由({@link CocColumn#field()})值组成；
	 * <P>
	 * 列表中的属性顺序即为Grid表头的显示顺序；
	 * 
	 * @return
	 */
	String frozenFields() default "";

	/**
	 * 字段列表：用来表示哪些字段将在Grid中显示。
	 * <P>
	 * 多个属性之间用竖线(|)分隔，属性列表由({@link CocColumn#field()})值组成；
	 * <P>
	 * 列表中的属性顺序即为Grid表头的显示顺序；
	 * 
	 * @return
	 */
	String fields();

	/**
	 * true：表示将在GRID中显示行序号。
	 * 
	 * @return
	 */
	boolean rownumbers() default true;

	/**
	 * true：表示支持多选。
	 * 
	 * @return
	 */
	boolean multiSelect() default true;

	/**
	 * true：表示当选中行的 checkbox 框时，此行也将被选中。
	 * 
	 * @return
	 */
	boolean selectOnCheck() default true;

	/**
	 * true：表示当选中一行时，此行的 checkbox 框也将被选中。
	 * 
	 * @return
	 */
	boolean checkOnSelect() default true;

	/**
	 * 分页栏显示在什么位置？"bottom|top"
	 * <UL>
	 * <LI>0(auto)：即由开发平台自动选择默认方式显示分页导航；
	 * <LI>1(bottom)：表示将分页导航显示在GRID底部；
	 * <LI>2(top)：表示将分页导航显示在GRID顶部；
	 * <LI>3(bottom&top)：表示将分页导航同时显示在GRID顶部和底部；
	 * <LI>9(none)：表示不显示分页导航；
	 * </UL>
	 */
	byte paginationPos() default 0;

	/**
	 * 用来指定哪些操作嵌入到GRID分页导航中？
	 * <p>
	 * 多个操作用竖线(|)分隔。 如：paginationActions="c|e|d"
	 * 
	 * @return
	 */
	String paginationActions() default "";

	/**
	 * 页索引：用来表示首次加载数据时显示第几页，该值必需大于1。
	 * 
	 * @return
	 */
	int pageIndex() default 1;

	/**
	 * 页大小：用来表示每次加载多少条数据？
	 * 
	 * @return
	 */
	int pageSize() default 20;

	/**
	 * 页大小选项：用来在交互界面中由用户自己选择页大小{@link #pageSize()}。
	 * 
	 * @return
	 */
	String pageOptions() default "[10,20,50,100]";

	/**
	 * <b>数据排序表达式：</b>默认数据排序规则。
	 * <p>
	 * <UL>
	 * <LI>用途：用来描述通过平台API获取业务数据时的默认排序规则；
	 * <LI>语法：“field1 desc, field2 asc”，类似于SQL语法中的“ORDER BY”之后的部分，区别是此处的字段名使用的是JAVA属性；
	 * <LI>空值：如果排序表达式为空或空串，则将自动按“物理主键”即id倒排序，即“id desc”；
	 * <LI>示例：“updatedDate desc”表示默认按修改时间倒排序；
	 * </UL>
	 */
	String sortExpr() default "";

	/**
	 * 是否需要显示GRID表头？true：显示，false：不显示；
	 * 
	 * @return
	 */
	boolean showHeader() default true;

	/**
	 * 是否需要显示GRID表脚？true：显示，false：不显示；
	 * <p>
	 * 表脚通常用来显示统计汇总数据
	 * 
	 * @return
	 */
	boolean showFooter() default false;

	/**
	 * 用来指定哪些操作将显示在({@link #rowActionsPos()})所指定的位置？
	 * <p>
	 * 多个操作用竖线(|)分隔。 如：actions="c|e|d"，操作编码的顺序即为操作按钮在单元格中的显示顺序。
	 * 
	 * @return
	 */
	String rowActions() default "";

	String rowActionsView() default "";

	/**
	 * 
	 * 操作按钮的显示位置：可选值(0|1|2)
	 * <UL>
	 * <LI>0(auto)：自动，即表示根据平台默认实现；
	 * <LI>1(head)：表示将操作按钮按顺序显示在GRID行前面，通常用于单条数据的操作，如“修改、删除”等操作；
	 * <LI>2(end)：表示将操作按钮按顺序显示在GRID行后面，通常用于单条数据的操作，如“修改、删除”等操作；
	 * </UL>
	 * 
	 * @return
	 */
	byte rowActionsPos() default 0;

	/**
	 * 行条件样式：
	 * <p>
	 * 语法规则：JSON数组， [{rule1}, {rule2}, ...]，数组元素语法如下：<br/>
	 * {where: {field1: value1, field2: [value21, value12]}, style: 'color: #ABCDEF;'}
	 * <p>
	 * 举例1：rowStyle = "[{where: {statusCode: 0}, style: 'color: #334455;'}]"<br/>
	 */
	String rowStyle() default "";

	/**
	 * Tree字段：表示该Grid是一个TreeGrid
	 */
	String treeField() default "";

	String uiView() default "";

	/**
	 * 字段列表：用来对Grid字段进行详细说明，如宽度、对齐方式等，是对{@link #fields()}的补充。详情请参见{@link CuiGridField}
	 * 
	 * @return
	 */
	CuiGridField[] fieldsDetail() default {};
}
