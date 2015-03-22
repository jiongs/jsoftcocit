package com.jsoft.cocit.entity.security;

import com.jsoft.cocit.constant.MenuTypes;
import com.jsoft.cocit.entity.ITreeEntity;
import com.jsoft.cocit.entity.config.IDataSource;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CuiEntity;

/**
 * <b>功能菜单：</b> 即提供给用户使用的功能菜单。
 */
public interface ISystemMenu extends ITreeEntity {
	/**
	 * 系统KEY：用来描述该菜单属于哪个系统？
	 * <UL>
	 * <LI>开发平台中可以有多个“逻辑系统”，该方法用来获取该菜单所属的系统。
	 * <LI>逻辑外键，关联到“系统（{@link ISystem}）”的“逻辑主键（{@link ISystem#getKey()}）”。
	 * </UL>
	 */
	String getSystemKey();

	/**
	 * 数据源KEY：用来描述该菜单使用哪个数据源（即连接到哪个数据库）？
	 * <UL>
	 * <LI>逻辑外键，关联到“数据源实体（{@link IDataSource}）”的“逻辑主键（{@link IDataSource#getKey()}）”。
	 * </UL>
	 */
	String getDataSourceKey();

	/**
	 * 获取菜单徽标
	 * 
	 * @return
	 */
	String getLogo();

	/**
	 * 获取菜单图片
	 * 
	 * @return
	 */
	String getImage();

	/**
	 * 菜单类型：
	 * <UL>
	 * <LI>分类菜单{@link MenuTypes#MENU_TYPE_FOLDER}：没有具体的功能界面，只是用来对其他功能菜单进行分类以方便管理。
	 * <LI>静态菜单{@link MenuTypes#MENU_TYPE_STATIC}：提供菜单访问路径，即可进入相应的功能模块操作界面。
	 * <LI>实体菜单{@link MenuTypes#MENU_TYPE_ENTITY}：用来管理实体数据的功能菜单称为“实体菜单”。
	 * </UL>
	 */
	public int getType();

	/**
	 * 获取功能菜单访问路径。
	 * <UL>
	 * <LI>实体菜单{@link MenuTypes#MENU_TYPE_ENTITY}：如果菜单类型为实体菜单，未指定访问路径，则自动计算实体菜单主界面访问路径。
	 * <LI>如：/cocentity/getFormToSave/web_category_0104:web_content:e/-1
	 * </UL>
	 */
	String getPath();

	/**
	 * 实体KEY：用来描述该“实体菜单”关联到哪个实体？
	 * <p>
	 * 注：同一个“实体”可以同时挂载到不同的菜单上。
	 * <p>
	 * 如：web_category
	 * 
	 */
	String getRefEntity();

	/**
	 * 操作列表：用来描述该菜单只能访问关联模块的哪些操作?
	 * <p>
	 * 注：同一个“实体”可以同时挂载到不同的菜单上。
	 * <p>
	 * 其值关联到{@link CocAction#key()}，多个操作之间用竖线‘|’分隔。
	 * <p>
	 * 如：c|e|v|d
	 * <p>
	 * 相当于“操作权限”的一种实现方式
	 * 
	 * @return
	 */
	String getActions();

	/**
	 * 字段列表：用来描述该菜单只能访问哪些字段？
	 * <p>
	 * 注：同一个“实体”可以同时挂载到不同的菜单上。
	 * <p>
	 * 其值关联到{@link CocColumn#field()}，多个字段之间用竖线‘|’分隔。
	 * <p>
	 * 如：name|key|code|statusCode
	 * <p>
	 * 相当于“字段权限”
	 */
	String getFields();

	/**
	 * 条件规则：即通过该菜单访问功能模块（如：实体模块、报表模块）时，只能访问满足查询条件的数据。
	 * <p>
	 * 注：同一个功能模块（如：实体模块、报表模块）可以同时挂载到不同的菜单上。
	 * <P>
	 * 语法：JSON对象 <code>
	 * {
	 * 		'field1' : value1,
	 * 		'field2' : [value2-1, value2-2, value2-3]
	 * }
	 * </code>
	 * <UL>
	 * <LI>实体菜单{@link MenuTypes#MENU_TYPE_ENTITY}：如果菜单类型为“实体菜单”，即用来描述该“实体菜单”只能访问满足条件的数据。
	 * </UL>
	 * <p>
	 * 相当于SQL语句“SELECT (field1,field2) FROM table1 WHERE field3=value3 and field4 in (v4-1, v4-2)”中“WHERE”后面的部分。
	 * <p>
	 * 相当于“数据行权限”的一种方式
	 */
	public String getWhereRule();

	/**
	 * <b>默认值规则：</b>即访问该菜单中的操作按钮时，如果字段值不存在，则自动将规则中指定的字段值赋予相应的字段。
	 */
	String getDefaultValuesRule();

	/**
	 * 界面KEY：
	 * <UL>
	 * <LI>实体菜单：如果菜单类型为“实体菜单”，即用来描述该“实体菜单”引用“实体定义”中的哪个界面？其值关联到{@link CuiEntity#key()}。
	 * </UL>
	 * 
	 */
	public String getUiView();

	boolean isHidden();
}
