package com.jsoft.cocit.entityengine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <b>实体注释：</b>用于描述实体的“数据表、业务逻辑、实体模块”的映射关系、属性等
 * <p>
 * <UL>
 * <LI>用途：该注释只用于实体类（实体类：即拥有{@link Entity}注释的POJO类），如果将该注释用于非实体类则无效；
 * <LI>数据表映射：如果要自定义数据库映射表的名字，可以通过{@link Table}注释来标注；
 * <LI>功能模块映射：平台将功能模块分为“静态模块、实体模块、报表模块、流程模块、分类模块（文件夹）”等，此注释只于来描述“实体模块”；
 * <LI>业务逻辑：
 * </UL>
 * <p>
 * 
 * @author yongshan.ji
 * @preserve all
 * 
 */
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CocEntity {

	// public long id() default 0;

	/**
	 * <b>实体名称：</b>实体模块的业务名称。
	 * <P>
	 * <UL>
	 * <LI>用途：实体名称可用作实体模块名、功能菜单名；
	 * <LI>长度：名称长度为255个字符，一个汉字为2个字符；
	 * <LI>规则：实体名称只能由“数字、字母、下划线、中文”等组成；
	 * <LI>示例：如“机构管理”、“员工管理”、“岗位管理”等；
	 * <LI>空值：不允许空值；
	 * </UL>
	 */
	public String name() default "";

	/**
	 * 是否对数据表名和字段名进行编码？
	 * 
	 * @return
	 */
	boolean dbEncoding() default false;

	/**
	 * <b>实体KEY：</b>实体模块的唯一编码。
	 * <p>
	 * <UL>
	 * <LI>用途：
	 * <OL>
	 * <LI>用作数据表名：实体KEY将被转换成数据库表名，转换规则遵循数据库命名规范。如：实体KEY为“HrJobPosition”，则对应的数据库表名为“hr_job_position”；
	 * <LI>用作模块编码：通过平台提供的API“加载功能界面、执行业务操作、获取业务数据”时，如需在API中指定模块编码，则模块编码即为此处的实体KEY；
	 * <LI>用作菜单编码：通过平台提供的API“加载功能界面、执行业务操作、获取业务数据”时，如需在API中指定系统菜单编码，则菜单编码默认即为此处的实体KEY；
	 * </OL>
	 * <LI>空值：如果该值为空或空串，则会把实体类名作为实体的KEY；如：类全名为"com.jha.hr.Organization",则对应的实体KEY为“Organization”；
	 * <LI>长途：KEY长度为64个字符；
	 * <LI>规则：KEY只能由“数字、字母、下划线”组成，且必须以“下划线”或“字母”开头；
	 * <LI>示例：如JAVA类为“com.jha.hr.Organization”“com.jha.hr.Employee”“com.jha.hr.JobPosition”，实体KEY为“HrOrganization”“HrEmployee”“HrJobPosition”，数据表则为“hr_organization”“hr_employee”“hr_job_position”；
	 * </UL>
	 */
	public String key() default "";

	public String tableName() default "";

	/**
	 * <b>数据排序表达式：</b>默认数据排序规则。
	 * <p>
	 * <UL>
	 * <LI>用途：用来描述通过平台API获取业务数据时的默认排序规则；
	 * <LI>语法：“分类1:表达式; 分类2:表达式”，“表达式”部分类似于SQL语法中的“ORDER BY”之后的部分，区别是此处的字段名支持JAVA属性；
	 * <LI>空值：如果排序表达式为空或空串，则将自动按“物理主键”即id倒排序，即“id desc”；
	 * <LI>示例：“updatedDate desc”表示默认按修改时间倒排序、“tree:name asc; grid:updatedDate desc”表示以树形结构获取数据时按名称正排序，以GRID形式获取数据时按修改时间倒排序；
	 * </UL>
	 */
	public String sortExpr() default "";

	// public byte layout() default 0;

	/**
	 * <B>实体分类：</B>用来指定实体模块所属类别的KEY。
	 */
	public String catalog() default "";

	/**
	 * <B>UI页面：</B>用来指定实体模块的个性化界面，可以直接引用已定义好的视图，也可以使用自动一的JSP/ST等。
	 * <p>
	 * <UL>
	 * <LI>用途：用来指定实体模块的个性化界面，UI页面文件可以是“.jsp”或“.st”文件；
	 * <LI>规则：UI页面可以是类路径或环境路径；
	 * <OL>
	 * <LI>环境路径：以“/”开头则认为是环境路径，环境路径支持“.jsp”和“.st”文件；如："/WEB-INF/jsp/orderManager.jsp"、“/WEB-INF/templates/orderManager.st”
	 * <LI>类路径：不以“/”开头则认为是类路径，类路径只支持“.st”文件，不支持“.jsp”文件，类路径中的“.st”可以省略；如“com.jha.templates.orderManager”、“com/jha/templates/orderManager”
	 * </OL>
	 * <LI>空值：如果UI页面为空或空串，则自动使用平台提供的默认功能界面；
	 * <LI>备注：如果无需对功能模块的主界面作个性化定制，则不用设置UI页面；
	 * </UL>
	 * 
	 * @return
	 */
	String uiView() default "";

	/**
	 * <B>初始化数据路径：</B>用来指定模块初始化数据。
	 * <p>
	 * <UL>
	 * <LI>用途：平台升级过程中，会自动将该JSON文件中的数据初始化到实体模块所对应的数据库表中，数据文件可以是“.js”或“.json”；
	 * <LI>规则：数据路径可以是类路径或环境路径；
	 * <OL>
	 * <LI>环境路径：以“/”开头则认为是环境路径，如："/WEB-INF/initdata/Organization.js"、“/WEB-INF/initdata/Organization.json”
	 * <LI>类路径：不以“/”开头则认为是类路径，如“com/jha/data/orderManager.json”
	 * </OL>
	 * <LI>数据：JSON文件内容是一个JS数组，数组中的每个元素是一个JS对象。平台升级过程中，JS数组会被转换成JAVA实体对象数组，JS对象也将被转换成JAVA实体对象，JS对象的属性值将被转换成JAVA实体对象中同名的属性值；
	 * <LI>示例：
	 * </UL>
	 */
	public String dataFile() default "";

	/**
	 */
	public boolean isBuildin() default false;

	/**
	 * 序号：用来指定“实体模块、功能菜单”显示顺序。
	 */
	public int sn() default 0;

	/**
	 * 动态业务实体类字节码的编译版本。
	 * <p>
	 * 用途：如果.class文件中的版本号和数据库中业务表定义的版本好不一致，将不能获得动态编译后的实体类class。
	 * <p>
	 * 备注：该属性只用于“动态实体模块”，对于开发人员硬编码的“实体模块”而言，该属性无效。
	 */
	public String version() default "";

	/**
	 * URL前缀：动态产生实体模块功能菜单时将该值作为功能菜单URL的前缀，如"urlPrefix=/coc"，则功能菜单URL为"/coc/getEntityModuleUI/HrOrganization"。
	 * <p>
	 * <UL>
	 * <LI>默认值：默认无需指定该值
	 * </UL>
	 * 
	 * @return
	 */
	public String urlPrefix() default "";

	/**
	 * 多列时各列之间用逗号分隔；多唯一性限制时用分号分隔。
	 * <p>
	 * CREATE UNIQUE INDEX index_name ON table_name (column_list)
	 * <p>
	 * ALTER TABLE table_name ADD UNIQUE (column_list)
	 * 
	 * @return
	 */
	public String uniqueFields() default "";

	/**
	 * 多列时各列之间用逗号分隔；多索引之间用分号分隔。
	 * <p>
	 * CREATE INDEX index_name ON table_name (column_list)
	 * <p>
	 * ALTER TABLE table_name ADD INDEX index_name (column_list)
	 * <p>
	 * ALTER TABLE table_name DROP INDEX index_name
	 * <p>
	 * DROP INDEX index_name ON talbe_name
	 * 
	 * @return
	 */
	public String indexFields() default "";

	/**
	 * 指定哪些字段可以作为数据权限字段？多个字段之间用"|"分隔。
	 * 
	 * @return
	 */
	public String dataAuthFields() default "";

	// public String desc() default "";

	/**
	 * 字段分组：
	 */
	CocGroup[] groups() default {};

	/**
	 * 操作列表：
	 */
	CocAction[] actions() default {};
}
