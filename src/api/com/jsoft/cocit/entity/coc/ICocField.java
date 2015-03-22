package com.jsoft.cocit.entity.coc;

import com.jsoft.cocit.entity.INamedEntity;

/**
 * 实体字段：用于描述“实体属性、数据表字段、字段UI”等信息。
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface ICocField extends INamedEntity {
	/**
	 * 实体定义：逻辑外键，关联到“{@link ICocEntity#getKey()}”字段。
	 * <p>
	 * 用于描述该字段属于哪个实体（实体类、实体表）？
	 * 
	 * @return
	 */
	String getCocEntityKey();

	/**
	 * 字段分组：逻辑外键，关联到“{@link ICocGroup#getKey()}”字段。
	 * <p>
	 * 用于描述该字段属于哪个分组？注：字段和分组必须属于同一个实体。
	 * 
	 * @return
	 */
	String getCocGroupKey();

	/**
	 * 字段数据类型：可选值参见“{@link ICocField#DATA_TYPE_XXXXX}”值。
	 * 
	 * @return
	 */
	int getFieldType();

	/**
	 * 获取“OneToMany”字段关联的实体KEY
	 * 
	 * @return
	 */
	String getManyTargetEntityKey();

	/**
	 * 外键关联的目标实体：逻辑外键，关联到“{@link ICocEntity#getKey()}”字段。
	 * <p>
	 * 用来描述该字段引用到哪个模块（哪张表）？
	 * 
	 * @return
	 */
	String getFkTargetEntityKey();

	/**
	 * 此“外键字段”关联到上述实体的哪个字段？不允许直接关联到物理主键字段，只允许关联到逻辑主键字段或其他数据字段。
	 * <p>
	 * 用来描述该字段引用到上述表中的哪个字段？
	 * 
	 * @return
	 */
	String getFkTargetFieldKey();

	/**
	 * 获取冗余外键的以来字段，即该外键字段依赖于哪个真正的逻辑外键字段？
	 * 
	 * @return
	 */
	String getFkDependFieldKey();

	/**
	 * 判断“系统引用”类型的字段是否允许将该系统映射成引用系统的子系统？
	 * 
	 * @return
	 */
	boolean isFkTargetAsParent();

	/**
	 * 实体数据是否按该外键字段进行分组？
	 * 
	 * @return
	 */
	boolean isFkTargetAsGroup();

	String getFkComboWhere();

	String getFkComboUrl();

	/**
	 * 检查“外键字段”或“上传字段”或“字典字段”是否支持多值
	 * 
	 * @return
	 */
	boolean isMultiSelect();

	/**
	 * 字段标度或长度：用于文本类型表示字段长度，用于数字类型表示数字的整数部分长度。
	 * 
	 * @return
	 */
	Integer getScale();

	/**
	 * 字段精度：用于数字类型表示数字的小数部分长度。
	 * 
	 * @return
	 */
	Integer getPrecision();

	/**
	 * <b>字段显示模式(mode)：</b>用空格分隔，与子系统数据操作中指定的动作模式组合使用。
	 * <p>
	 * 格式：[操作模式:显示模式]
	 * <ul>
	 * <li>M: Must 必需的</li>
	 * <li>E: Edit 可编辑的 (即可读写)</li>
	 * <li>I: Inspect 检查（带有一个隐藏字段存放其值）</li>
	 * <li>S: Show 显示（但不带隐藏字段）</li>
	 * <li>N: None 不显示</li>
	 * <li>P: Present 如果该字段有值就显示，否则如果没有值就不显示该字段</li>
	 * <li>H: Hidden 隐藏 (不显示，但有一个隐藏框存在)</li>
	 * <li>R: Read only 只读</li>
	 * <li>D: Disable 禁用</li>
	 * </ul>
	 * <p>
	 * <b> 字段显示模式举例说明(mode)： </b>
	 * <ul>
	 * <li>v:I——查看数据时，该字段处于检查模式</li>
	 * <li>e:E——编辑数据时，字段可编辑</li>
	 * <li>bu:N——批量修改数据时，字段不可见</li>
	 * </ul>
	 */
	String getMode();

	/**
	 * 获取“数值类型”字段值计量单位选项，以便录入、编辑数据过程中生成计量单位下拉选项。
	 * <p>
	 * 计量单位选项可以是计量单位类别编号，如距离“distance”；或如下个格式的文本：{"m":"米", "km":"千米"}
	 * 
	 * @return
	 */
	String getUomOptions();

	/**
	 * 获取“数值类型”字段计量单位，以便录入编辑数据过程中固定计量单位
	 * <p>
	 * 如果计量单位选项是计量单位类别，则该值可以是指定类别下的计量单位编号。
	 * <p>
	 * 如计量单位使用的是类别编号“distance”(距离)，则计量单位可以限制为“m”(米)。
	 * <p>
	 * 
	 * 
	 * @return
	 */
	String getUomOption();

	/**
	 * 获取“字典类型”字段值选项，以便录入、编辑数据过程中生成单选或多选选项。
	 * <p>
	 * 字段值选项可以是字典类别编号，或如下个格式的文本：{"m":"男", "w":"女"}
	 * 
	 * @return
	 */
	String getDicOptions();

	/**
	 * 默认值表达式
	 * 
	 * @return
	 */
	String getDefaultValue();

	/**
	 * 获取“数值类型”、“日期”、“日期时间”类型字段的显示格式。
	 * <p>
	 * 日期类型如：yyyy-MM-dd(2010-12-31)；(yyyyMMdd)20101231；(yyyy-MM-dd hh:mm:ss)2010-12-31 12:30:25
	 * <p>
	 * 数字类型如：###,###.00
	 * 
	 * @return
	 */
	String getPattern();

	/**
	 * 获取动态生成的业务实体属性名。
	 * 
	 * @return
	 */
	String getFieldName();

	/**
	 * 判断字段是否为临时字段？临时字段对应的数据不会被持久化到数据库中。
	 * 
	 * @return
	 */
	boolean isTransient();

	/**
	 * 判断该字段是否是Grid列？即是否允许在Grid列表中显示？
	 * 
	 * @return
	 */
	boolean isAsGridColumn();

	/**
	 * 获取该字段在Grid列中的显示顺序
	 * 
	 * @return
	 */
	Integer getGridColumnSn();

	/**
	 * 获取该字段在Grid列中的显示宽度
	 * 
	 * @return
	 */
	Integer getGridColumnWidth();

	/**
	 * 获取该字段在Form表单上的展现方式，即字段UI窗体
	 * 
	 * @return
	 */
	String getUiView();

	/**
	 * 该字段是否作为过滤器节点字段？
	 * 
	 * @return
	 */
	boolean isAsFilterNode();

	/**
	 * 级联模式：即哪些字段发生变化后，该字段将以何种模式显示。
	 * <p>
	 * 格式: {上级字段值}:{mode}
	 * 
	 * @return
	 */
	String getUiCascading();

	/**
	 * 获取字段的数据级联
	 * 
	 * @return
	 */
	String getDataCascading();

	/**
	 * 获取字段在数据库端的列名
	 * 
	 * @return
	 */
	String getDbColumnName();

	boolean isDbColumnNotNull();

	/**
	 * 获取字段在数据库端的类型
	 * 
	 * @return
	 */
	String getDbColumnDefinition();

	/**
	 * 获取String类型的字段长度
	 * 
	 * @return
	 */
	int getLength();

	String getGenerator();
}
