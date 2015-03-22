package com.jsoft.cocit.entityengine.annotation;

import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entityengine.bizplugin.IBizPlugin;

/**
 * 实体操作注释：使用该注释将自动为CoC实体类对应的业务表生成相关的业务操作。如：添加、删除、修改、查询等。
 * 
 * @author jiongsoft
 * @preserve all
 */
public @interface CocAction {

	// long id() default 0;

	/**
	 * 操作名称：和操作图标【{@link #logo()}】一起组成操作按钮。
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * 操作全称：通常用作操作表单的标题
	 * 
	 * @return
	 */
	String title() default "";

	// String mode() default "";

	/**
	 * 操作ID：在同一个实体模块内唯一，用来唯一表示一个操作。
	 * <p>
	 * 不同的操作ID可以具有相同的操作码【{@link #opCode()}】
	 * 
	 * @return
	 */
	String key() default "";

	/**
	 * 序号：操作按钮在操作菜单中的显示顺序
	 * 
	 * @return
	 */
	int sn() default 0;

	/**
	 * 操作码：操作类型是由平台提供的，参见{@link OpCodes#OP_CODE_XXX}。
	 * <p>
	 * 同一个操作码可以对应多个不同的具体操作，通过{@link #key()}来区别。
	 * <p>
	 * 相同的操作码通常具有相同默认操作界面。
	 * 
	 * @return
	 */
	int opCode() default 0;

	// /**
	// * @deprecated 用{@link #plugin()}代替
	// */
	// String pluginName() default "";

	/**
	 * 业务插件：【{@link IBizPlugin}】的子类
	 * 
	 * @return
	 */
	Class plugin() default void.class;

	//
	// /**
	// * 操作按钮的显示位置：可选值(top|begin|end|pagination)
	// * <UL>
	// * <LI>top：显示在顶部操作按钮栏，通常用于“新增、批处理”等操作；
	// * <LI>begin：显示在GRID行头，通常用于单条数据的操作，如“修改、删除”等操作；
	// * <LI>end：显示在GRID行尾，通常用于单条数据的操作，如“修改、删除”等操作；
	// * <LI>pagination：显示在分页栏，在分页栏显示时，通常只显示图标，通常用于“新增、批处理、设置”等操作；
	// * </UL>
	// * <font color="red">TODO: 暂不支持</font>
	// *
	// * @return
	// */
	// String position() default "";

	/**
	 * 操作按钮图片：用来替换文字。
	 * <P>
	 * 默认操作菜单上的操作按钮由操作图标{@link #logo()}加操作名称{@link #name()}组成。
	 * <P>
	 * TODO:
	 * 
	 * @return
	 */
	String btnImage() default "";

	/**
	 * 操作图标：和操作名称【{@link #name()}】一起组成操作按钮。
	 * 
	 * @return
	 */
	String logo() default "";

	// /**
	// * @deprecated 用{@link #uiPage()}代替
	// */
	// String targetUrl() default "";

	/**
	 * 操作表单： 用来指定执行操作时打开的表单UI。
	 * <p>
	 * <UL>
	 * <LI>用途：用来指定执行操作时的个性化界面，UI页面文件可以是“.jsp”或“.st”文件，也可以不是具体的页面，而是通过{@link CuiForm}所定义的表单KEY({@link CuiForm#key()})；
	 * <LI>规则：UI页面可以是类路径或环境路径；
	 * <OL>
	 * <LI>环境路径：以“/”开头则认为是环境路径，环境路径支持“.jsp”和“.st”文件；如："/WEB-INF/jsp/orderManager.jsp"、“/WEB-INF/templates/addProduct.st”
	 * <LI>类路径：不以“/”开头则认为是类路径，类路径只支持“.st”文件，不支持“.jsp”文件，类路径中的“.st”可以省略；如“com.jha.templates.addProduct”、“com/jha/templates/addProduct”
	 * </OL>
	 * <LI>空值：如果UI页面为空或空串，则自动使用平台提供的默认操作界面；
	 * <LI>备注：如果无需不需要对操作界面作个性化定制，则不用设置该值；
	 * </UL>
	 * 
	 * @return
	 */
	String uiForm() default "";

	/**
	 * 自定义表单URL地址：如果未设置该值，则由平台自动生成操作按钮访问地址。
	 * <p>
	 * 如：
	 * <UL>
	 * <LI>@CocAction(uiFormUrl="/cocentity/getFormToSave/web_category:web_category:e/${category.id}")
	 * <LI>@CocAction(uiFormUrl="/cocentity/getFormToSave/web_content:web_content:v/${id}")
	 * <LI>@CocAction(uiFormUrl="${runtimeLinkUrl}")
	 * </UL>
	 */
	String uiFormUrl() default "";

	/**
	 * 执行操作时在哪个窗口打开页面？
	 * <p>
	 * 相当于超链接中的target属性。
	 * <p>
	 * 默认在平台内嵌的Dialog中打开。
	 * 
	 * @return
	 */
	String uiFormTarget() default "";

	int uiWindowWidth() default 0;

	int uiWindowHeight() default 0;

	/**
	 * 停用：如果操作已被停用，则将不显示在功能菜单中，也不能执行已被停用的操作。
	 * 
	 * @return
	 */
	boolean disabled() default false;

	/**
	 * 执行操作成功后的提示信息。
	 * <p>
	 */
	String successMessage() default "";

	/**
	 * 执行操作失败后的错误信息。
	 * <p>
	 */
	String errorMessage() default "";

	/**
	 * 执行操作的警告信息，出现警告信息时，需要让用户确认是否继续执行该操作？
	 * <p>
	 */
	String warnMessage() default "";

	/**
	 * <b>默认值规则：</b>即执行该操作时，如果字段值不存在，则自动将规则中指定的字段值赋予相应的字段。
	 */
	String defaultValues() default "";

	/**
	 * <b>赋值规则：</b>即执行该操作时，不管字段值是否存在，都自动将规则中指定的字段值赋予相应的字段。
	 * <p>
	 * 注：如果设置了“条件规则({@link #where()})”，则只有满足条件的数据才会被赋值。
	 * <p>
	 * 主要用于“审核类操作”。
	 * <p>
	 * 相当于SQL语句“UPDATE table1 (field1,field2) VALUES (value1, value2)”中"VALUES"后面的部分。
	 */
	String assignValues() default "";

	/**
	 * <b>条件规则：</b>用作赋值条件：只有满足该条件的数据对象，其相应的字段值才会被“赋值规则”中的字段值所替换。 用作行操作：只有满足该条件的数据行，才会显示此操作。
	 * 
	 * <b>语法：</b>
	 * <UL>
	 * <LI>1. JSON语法： { field1 : value1, field2 : [value2-1, value2-2, value2-3] }
	 * <LI>2. 伪SQL语法：((field1 op value1) and (field2 op value2)) or field3 in (v1,v2,v3)
	 * <LI>3. 注：上两种语法不能混用！！！ 操作类型(op)参见枚举类API文档 “com.jsoft.cocit.orm.expr. CndType”
	 * </UL>
	 * 
	 * <b>举例：</b>
	 * 
	 * <pre>
	 * @ CocAction(name = "撤销置顶", 
	 * opCode = OpCodes.OP_UPDATE_ROW, 
	 * key = "no_top", 
	 * assignValues = "frontDate:null", 
	 * where = " statusCode in (2,3,4,5) or(frontDate nn)")
	 * 
	 * </pre>
	 */
	String where() default "";

	/**
	 * 代理操作：该操作可以不是具体的操作，而是其他操作的代理，按设定的被代理操作的顺序，如果数据满足被代理的操作执行条件，即跳转到相应的操作！
	 * <p>
	 * 语法：其值来自({@link #key()})，多个操作之间用“|”分隔。
	 * <p>
	 * 功能：
	 * 
	 * @return
	 */
	String proxyActions() default "";

	/**
	 * 操作配置文件：通过该属性引入操作配置文件，即可省去常用的如“删除、查询、排序”等操作。
	 * 
	 * @return
	 */
	String importFromFile() default "";

	/**
	 * 操作提示：操作提示可显示在操作菜单的alt|title属性上，当鼠标移动到操作按钮上面时，即显示该提示信息。
	 * 
	 * <p>
	 * TODO:
	 */
	String notes() default "";

}
