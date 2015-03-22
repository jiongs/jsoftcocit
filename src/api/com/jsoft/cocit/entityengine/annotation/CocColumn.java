package com.jsoft.cocit.entityengine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.Column;

import com.jsoft.cocit.entityengine.PatternAdapter;
import com.jsoft.cocit.orm.generator.Generator;

/**
 * <b>字段注释：</b>用于描述实体POJO属性与“数据表字段、业务逻辑、表单输入框”的关系、属性等。
 * <p>
 * <UL>
 * <LI>字段映射：默认自动将POJO属性转换成数据库字段名，如需数据库字段名，则可通过{@link Column#name()}来指定；更多字段映射可参见{@link Column}。
 * <LI>业务逻辑：
 * <LI>表单输入框：
 * <LI>目标：可用于“嵌入式、POJO属性、Getter方法”，嵌入式即用在{@link CocEntity#groups()}元素的{@link CocGroup#fields()}元素中；
 * <LI>优先级：该注释可同用于“嵌入式、POJO属性、Getter方法”上，按优先级“嵌入式 > POJO属性 > Getter方法”合并注释项；
 * </UL>
 * 
 * @author jiongsoft
 * @preserve all
 * 
 */
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CocColumn {

	/**
	 * <b>字段名称：</b>实体字段的业务名称。
	 * <P>
	 * <UL>
	 * <LI>用途：字段名称可用作GRID表头、输入框Label等；
	 * <LI>空值：不允许为空；
	 * <LI>长度：名称长度为255个字符，一个汉字为2个字符；
	 * <LI>规则：字段名称只能由“数字、字母、下划线、中文”等组成；
	 * <LI>示例：如“单位名称”、“员工姓名”、“岗位职责”等；
	 * </UL>
	 */
	public String name() default "";

	/**
	 * @deprecated 用 {@link #field()} 代替。
	 */
	public String propName() default "";

	/**
	 * <B>JAVA字段名：即JAVA POJO属性</B>
	 * <p>
	 * <UL>
	 * <OL>
	 * <LI>用作类注释：当该注释被嵌入到类注释中时，必须指定property值，用来关联到指定的属性；
	 * <LI>用在POJO属性上：当注释用在POJO属性上时，该属性无效；
	 * <LI>用在Getter方法上：当注释用在Getter方法上时，该属性无效；
	 * <LI>不能用在类变量(static变量)上；
	 * </OL>
	 * <LI>规则：其值只能是是对应的POJO属性名、或Getter方法对应的属性名；
	 * </UL>
	 */
	public String field() default "";

	/**
	 * 序号：Serial Number
	 * <p>
	 * 默认将该值作为表单中的字段显示顺序。
	 * 
	 * @deprecated 字段在表单中的显示顺序由 {@link CuiForm#fields()}中的字段顺序决定。
	 */
	public int sn() default 0;

	/**
	 * POJO属性是否是运行时属性？
	 * <p>
	 * true——是，POJO属性将不会映射到任何数据表字段；
	 * <p>
	 * false——否（默认值），POJO属性将自动映射到数据表字段；
	 * 
	 */
	public boolean isTransient() default false;

	/**
	 * 字段是否作为Grid列？即是否在GRID列中显示？
	 * <p>
	 * true——显示，false——不显示
	 * 
	 * @deprecated 字段是否作为Grid列？取决于 {@link CuiGrid#fields()}中是否存在该字段。
	 */
	public boolean asGridColumn() default false;

	/**
	 * 字段在GRID列中的显示顺序。
	 * 
	 * @deprecated 字段在GRID中的显示顺序取决于 {@link CuiGrid#fields()}中的字段顺序。
	 */
	public int gridColumnSn() default 0;

	/**
	 * 字段在GRID列中的显示宽度(px)。
	 * 
	 * @deprecated 字段在GRID中的列宽(px)，取决于 {@link CuiGridField#width()}。
	 */
	public int gridColumnWidth() default 0;

	/**
	 * 外键字段所关联的目标实体，其值来自于【{@link CocEntity#key()}】。
	 * <p>
	 * 即：用来描述数据表中的外键字段所引用的是哪张数据表？
	 * 
	 */
	public String fkTargetEntity() default "";

	/**
	 * 外键字段所关联的目标字段
	 * <p>
	 * String类型的外键默认关联到目标实体的“逻辑主键(key)”字段。
	 * <P>
	 * 实体类型的(@ManyToOne和@OneToOne)外键字段默认关联到目标实体的“物理主键(id)”字段
	 * <p>
	 * 如果是数据冗余字段，则可指定使其关联到被引用实体的属性【{@link CocColumn#field()}】。
	 */
	public String fkTargetField() default "";

	/**
	 * 外键依赖字段：即该外键字段是哪个外键字段的冗余？通常和{@link #fkTargetField()}一起成对出现。
	 * <p>
	 * <B>应用场景：</B>orgKey是逻辑外键字段，是真正的外键；orgName是该逻辑外键对应的Display字段，是冗余字段。 <br>
	 * orgKey关联到目标实体的逻辑主键；而orgName则关联到目标实体的name字段。
	 * <p>
	 * <B>用法举例：</B>@CocColumn(propName="orgKey", fkTargetEntity="hr_organization") <br>
	 * 则@CocColumn(propName="orgName", fkTargetEntity="hr_organization", fkTargetField="name", fkDependField="orgKey")
	 * 
	 * @return
	 */
	public String fkDependField() default "";

	/**
	 * 外键字段关联的目标实体是否是该实体的父实体？即该模块是目标模块的子模块。
	 * <p>
	 * 用于描述子主从表关系。如果指定了(targetAsParent=true)，则该模块将被作为主模块的子模块形式展现。
	 * <p>
	 * 如：在“订单条目”的“所属订单”字段中设置了(targetAsParent=true)，则“订单条目”将以子模块的形式出现在“订单管理模块”中。
	 */
	public boolean fkTargetAsParent() default false;

	/**
	 * 是否需要按此外键字段对数据进行分组？
	 * <p>
	 * 用在树形结构或GRID结构展现数据集时生效。
	 * <p>
	 * 如：在“员工信息管理”的“所属部门”字段中设置了(targetAsGroup=true)。此时若某模块的外键字段引用到了员工信息表，则在表单中通过"ComboTree|ComboGrid|ComboBox"选择员工时，即可按部门对员工信息进行分组以方便查找并选择。
	 * <p>
	 * 注：通常只能对1个或0个外键字段设置(targetAsGroup=true)。
	 */
	public boolean fkTargetAsGroup() default false;

	/**
	 * 自定义字典选项，格式如“1:可用, 0:停用”
	 * <p>
	 * 语法：
	 * <UL>
	 * <LI>简单语法：“value-1:text-1, value-2:text-2, ... , value-n:text-n”，如“0:未婚, 1:已婚, 2:离异”
	 * <LI>JSON语法：“[{'value-1':'text-1'}, {'value-2':'text-2'}, ... , {'value-n':'text-n'}]”，如果值中包含逗号(,)、冒号(:)等，则采JSON语法；
	 * <LI>字典表：“${字典KEY}”，如果字典选项来自字典表，则使用该语法，如性别字段(dicOptions=${sex})；
	 * </UL>
	 */
	String dicOptions() default "";

	/**
	 * 字段pattern: 用来校验字段值是否合法？用来格式化字段值在前端的展现格式。
	 * <p>
	 * 语法：其值关联到（{@link PatternAdapter#getName()}）
	 * <UL>
	 * <LI>密码字段：即以密码方式输入字段值，如用户密码字段，设置(pattern="password")
	 * <LI>日期字段：如pattern="yyyy-MM-dd HH:mm:ss", pattern="date"
	 * <LI>数字字段：如pattern="#,###,##0.00", pattern="number"
	 * <LI>字符串字段：用正则表达式校验字段值
	 * <LI>文件上传字段：如pattern="*.jpg,*.png,*.gif", pattern="image"
	 * </UL>
	 */
	String pattern() default "";

	/**
	 * 字段模式：用来描述执行不同操作时，各个字段的展现方式。
	 * <p>
	 * 语法：
	 * <UL>
	 * <LI>简单语法：“op-1:mode-1, op-2:mode-2 ... op-n:mode-n”，风格符可以是逗号或空格。
	 * <LI>JSON语法：“{'op-1':'mode-1', 'op-2':'mode-2' ... 'op-n':'mode-n'}”，如果值中包含逗号(,)、冒号(:)等，则采用JSON语法；
	 * </UL>
	 * <UL>
	 * <LI>多个操作模式之间可以用“空格、逗号”分隔，操作编码来自{@link CocAction#key()}；
	 * <LI>字段模式：字段模式可用来对字段值做校验、控制表单中的字段展现方式等。
	 * <OL>
	 * <LI>M: Must 必需的，通常用作校验，可以和其他模式组合使用，如：MS,MH等，单独的M相当于ME。
	 * <LI>E: Edit 可编辑的 (即可读写)
	 * <LI>I: Inspect 检查（带有一个隐藏字段存放其值）
	 * <LI>S: Show 显示（但不带隐藏字段）
	 * <LI>N: None 不显示
	 * <LI>P: Present 如果该字段有值就显示，否则如果没有值就不显示该字段
	 * <LI>H: Hidden 隐藏 (不显示，但有一个隐藏框存在)
	 * <LI>R: Read only 只读
	 * <LI>D: Disable 禁用
	 * <LI>优先级：（N > S > P > D > I > R > H > E）主要用于运行时动态计算字段显示模式。
	 * </OL>
	 * <LI>举例：“*:N c:M e:E v:P”——默认(*)不显示；执行添加(c)操作时该字段必填；执行修改(e)操作是字段可编辑；执行查看(v)操作时，字段有值则显示；
	 * </UL>
	 */
	String mode() default "";

	/**
	 * UI视图组件：可以是预置的窗体小组件，也可以是以.st结尾的自定义窗体组件类型。
	 * <p>
	 * 预置类型：
	 * <UL>
	 * <LI>字典字段：select，TODO：checkbox|combobox等，默认select；
	 * <LI>外键字段：combobox|combogrid|combotree|combolist|combogriddialog|combotreedialog|combocascadingbox等，默认combogrid；
	 * <LI>数字字段：numberbox|spinner，默认numberbox
	 * <LI>日期(日期时间)字段：combomonth|combodate|combodatetime，默认由平台自动根据日期时间pattern选择combodate或combodatetime。
	 * <LI>时间字段：TODO：spinnertime
	 * </UL>
	 * <p>
	 * TODO: 以“.st”结尾的自定义窗体组件
	 * <p>
	 * TODO： combotree|combolist|combogriddialog|combotreedialog|combomonth|spinner
	 */
	String uiView() default "";

	/**
	 * 字段值生成器：用来自动生成字段值，关联到{@link Generator#getName()}
	 * <p>
	 * 生成器必需实现接口({@link Generator})
	 * <p>
	 * 生成器通过"WEB-INF/config/iocconfig.json"文件中的“entityGenerators”数组进行配置。
	 * <p>
	 * 
	 * <pre>
	 * {
	 * 	 beansConfig : {
	 * 		type : "com.jsoft.cocit.config.impl.BeansConfig",
	 * 		fields : {
	 * 			views : [],
	 * 			fieldViews : [],
	 * 			cellViews : [],
	 * 			patternAdapters: [],
	 * 			entityGenerators: ["com.jsoft.cocit.orm.generator.DemoGenerator"],
	 * 			entityPackages : ["com.jsoft.cocit.entity.demo"]
	 * 		}
	 * 	}
	 * }
	 * </pre>
	 */
	String generator() default "";

	/**
	 * 该字段是否作为过滤器节点字段？即是否显示在过滤器中用于可以快速过滤数据？
	 * <p>
	 * 该属性只对“字典字段、BOOLEAN字段、外键字段”等生效。
	 * <p>
	 * 默认对上述字段自动产生数据过滤器，如果不想支持则设置(asFilterNode=true)。
	 * 
	 * 
	 * @deprecated 字段是否作为过滤器节点字段?取决于{@link CuiEntity#filterFields()}中是否存在该字段。
	 */
	boolean asFilterNode() default false;

	/**
	 * UI显示模式联动：用于描述该字段的UI显示模式依赖于哪些字段？如何依赖？
	 * <UL>
	 * <LI>语法：uiCascading="field1={*:mode1, value1|value2:mode2}; field2={*:mode3, value3|value4:mode4}; ..."，运行时字段显示模式可能会有多个值，则依据显示模式之间的优先级（N > S > P > D > I > R > H > E），如果显示模式为M则可以和上述模式进行组合。
	 * <LI>含义：字段(field1)的值等于其他值(*表示其他值)时，该字段显示模式为mode1，当字段(field1)的值在列表[value1,value2]中时，该字段显示模式为mode2；
	 * <LI>场景：网站栏目中指定该栏目内容必需提供图片，即把网站栏目作为网站内容的配置。
	 * <LI>示例：@CocColumn(name="内容图片", propName="image", uiCascading="category.neededContentImage:{*:N, 1:M}", pattern="image")，表示该“内容图片”字段依赖于网站栏目中的是否需要内容图片这个字段(neededContentImage)的配置。 <BR>
	 * 默认是不需要提供图片的，如果栏目是一个图片栏目，则该栏目的内容必需提供图片。
	 * <LI>Boolean类型的值用1或0表示。
	 * </UL>
	 */
	String uiCascading() default "";

	//
	// /**
	// * 下拉列表数据联动：用于描述字段与字段之间的数据联动关系。
	// * <UL>
	// * <LI>规则：当前字段值发生改变时，“哪些”字段的下拉列表会随之更新？根据“关联表的哪个字段”来更新下拉列表？(即关联表的哪个字段值“等于”当前字段值？)
	// * <LI>语法：dataCascading=“field1.reffield1|field2.reffield2|...|fieldn.reffieldn”。
	// * <LI>场景：如“发货地址表(ShipmentsAddress)”的字段“省份(province)、城市(city)”分别引用到了“省份信息表(Province)”、“城市信息表(City)”，此时填写发货地址过程中，选中省份后，城市下拉列表应该同步更新为所选省份下面的相关城市。
	// * <p>
	// * ShipmentsAddress.province——>Province.key；ShipmentsAddress.city——>City.key；City.province——>Province.key
	// * <LI>示例：dataCascading="city.province"，在“发货地址表”的“province”字段上设置上述属性，表示“省份”字段值发生改变时，“城市(city)”字段的下拉列表会随之更新，根据“城市表的外键字段(province)”来更新下拉列表。
	// * <LI>TODO:
	// * </UL>
	// */
	String dataCascading() default "";

	/**
	 * 默认值：针对不同的操作可以指定不同的默认值。如“状态字段”，执行“后台录入、前台注册”等操作时，虽然同属于insert操作，但可以对其指定不同默认值。
	 * <p>
	 * 语法：
	 * <UL>
	 * <LI>简化语法：“op-1:value-1, op-2:value-2, ... , op-n:value-n”；
	 * <LI>JSON语法：“{'op-1':'value-1', 'op-2':'value-2', ... , 'op-n':'value-n'}”，如果值中包含逗号(,)、冒号(:)等，则采用JSON语法；
	 * <LI>TODO:
	 * </UL>
	 */
	String defalutValue() default "";

	/**
	 * String类型的字段长度
	 */
	int length() default 0;

	/**
	 * 数字类型的整数位长度
	 */
	int precision() default 0;

	/**
	 * 数字类型的小数位长度
	 */
	int scale() default 0;

	/**
	 * 数据表的列名。
	 */
	String dbColumnName() default "";

	/**
	 * 定义字段类型
	 */
	String dbColumnDefinition() default "";

	/**
	 * 数据表字段可空
	 */
	boolean nullable() default true;

	boolean readonly() default false;

	/**
	 * “String类型外键字段、字典字段、Upload字段”等是否支持多选？
	 */
	boolean multiSelect() default false;

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

	/**
	 * 字段备注：字段备注可显示在UI表单中字段输入框后面，起到提示的作用。
	 * <p>
	 * TODO:
	 */
	public String notes() default "";
}
