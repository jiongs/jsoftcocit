package com.jsoft.cocit.entityengine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jsoft.cocimpl.ui.UIView;
import com.jsoft.cocit.entity.security.ISystemMenu;

/**
 * <B>实体主界面UI注解：</B>用来定义实体模块主界面。
 * 
 * @author Ji Yongshan
 * 
 */
@Target(value = { ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface CuiEntity {

	String name() default "";

	/**
	 * 界面编码：可以被实体模块引用({@link CocEntity#uiView()})作为默认视图使用；也可以被功能菜单所引用（{@link ISystemMenu#getUiView()}）。
	 * <p>
	 * 如果未指定界面KEY，则自动将实体对象KEY作为界面KEY使用。
	 * <p>
	 * 如果指定了界面KEY，则必需使用({@link CocEntity#uiView()})注解来引用此界面。
	 * 
	 * @return
	 */
	String key() default "";

	/**
	 * 界面布局：用于布局主从结构数据表维护界面，主从表采用横向排列方式布局。
	 * <p>
	 * 如：cols="50%,*"，表示将界面划分为2列，第一列宽度占50%作为主表UI，剩下的部分作为从表UI。多个从表默认用Tabs分隔。
	 * <p>
	 * cols="300,*"
	 * 
	 * @return
	 */
	String cols() default "";

	/**
	 * 界面布局：用于布局主从结构数据表维护界面，主从表采用纵向排列方式布局。
	 * <p>
	 * 如：rows="50%,*"，表示将界面划分为2行，第一行高度占50%作为主表UI，剩下的部分作为从表UI。多个从表默认用Tabs分隔。
	 * <p>
	 * rows="300,*"
	 * 
	 * @return
	 */
	String rows() default "";

	/**
	 * <b>实体数据过滤器字段：</b>用来指定哪些字段可以作为过滤器节点使用？以便于在实体模块主界面中快速过滤Grid数据。
	 * <P>
	 * 多个属性之间用竖线(|)分隔，属性列表由({@link CocColumn#field()})值组成；
	 * <P>
	 * 列表中的属性顺序即为树形过滤器的节点顺序；
	 * 
	 * @return
	 */
	String filterFields() default "";

	/**
	 * “过滤器”的显示位置：可选值"0|1|2|3|4|5"。
	 * <UL>
	 * <LI>0(auto)：自动计算“查询框”的摆放位置；
	 * <LI>1(top)：表示将“过滤器”显示在顶部“操作按钮”右边；（暂不支持）
	 * <LI>2(bottom of grid)：表示将“过滤器”嵌入在底部“分页导航”里面；（暂不支持）
	 * <LI>3(left of grid)：表示将“过滤器”显示在“GRID”左边；（暂不支持）
	 * <LI>4(right of toolbar)：表示将“过滤器”显示在“操作按钮”右边；
	 * <LI>5(gridtop)：表示将“过滤器”嵌入“GRID”顶部；（暂不支持）
	 * </UL>
	 * 
	 * @return
	 */
	byte filterFieldsPos() default 0;

	/**
	 * 数据过滤器展现方式：即视图名称，该值对应 {@link UIView#getName()}，即表示选择相应的 {@link UIView} 来展现数据过滤器。
	 * 
	 * @return
	 */
	String filterFieldsView() default "";

	/**
	 * <b>实体数据查询字段：</b>用来描述哪些字段将参与到查询编辑器中？以方便用户自定义组合查询。
	 * <P>
	 * 多个属性之间用竖线(|)分隔，属性列表由({@link CocColumn#field()})值组成；
	 * <P>
	 * 列表中的属性顺序即为查询编辑器中的字段顺序；
	 * 
	 * @return
	 */
	String queryFields() default "";

	/**
	 * 查询框的显示位置：可选值"0|1|2|3|4|5"。
	 * <UL>
	 * <LI>0(auto)：自动计算“查询框”的摆放位置；
	 * <LI>1(up of toolbar)：表示将“查询框”显示在“操作按钮”上面——顶部；
	 * <LI>2(bottom)：表示将查询框显示在底部；（暂不支持）
	 * <LI>3(left)：表示将“查询框”显示在“操作按钮”的左边；（暂不支持）
	 * <LI>4(right of toolbar)：表示将“查询框”显示在“操作栏”右边；
	 * <LI>5(gridtop)：表示将查询框嵌入GRID顶部；（暂不支持）
	 * </UL>
	 * 
	 * @return
	 */
	byte queryFieldsPos() default 0;

	/**
	 * 查询编辑器展现方式：即视图名称，该值对应 {@link UIView#getName()}，即表示选择相应的 {@link UIView} 来展现查询编辑器。
	 * 
	 * @return
	 */
	String queryFieldsView() default "";

	/**
	 * 用来指定哪些操作将显示在({@link #actionsPos()})所指定的位置？
	 * <p>
	 * 多个操作用竖线(|)分隔。 如：actions="c|e|d"
	 * 
	 * @return
	 */
	String actions() default "";

	/**
	 * 操作按钮显示方式：即视图名称，可选值"buttons|menu"，该值对应 {@link UIView#getName()}，即表示选择相应的 {@link UIView} 来展现操作按钮。
	 * 
	 * @return
	 */
	String actionsView() default "";

	/**
	 * 操作按钮的显示位置：可选值"0|1|2|3|4|5"。
	 * <UL>
	 * <LI>0(auto)：自动，即根据URL API自动检测操作按钮的位置；
	 * <LI>1(top)：表示将操作按钮显示在顶部；
	 * <LI>2(bottom)：表示将操作按钮显示在底部；
	 * <LI>3(left)：表示将操作按钮显示在左边；
	 * <LI>4(right)：表示将操作按钮显示在右边；
	 * <LI>5(gridtop)：表示将操作按钮嵌入GRID顶部；
	 * </UL>
	 * 
	 * @return
	 */
	byte actionsPos() default 0;

	String uiView() default "";

	/**
	 * 定义主界面之GRID
	 * 
	 * @return
	 */
	CuiGrid grid() default @CuiGrid(fields = "");

	/**
	 * 定义实体表单：每个实体界面可以拥有多套表单，多套实体界面可以定义各自的表单，表单编号可以相同，以便于在操作按钮中引用。
	 * <p>
	 * 即访问不同的实体模块主界面时，其对应的操作按钮表单界面也可以不一样。
	 * 
	 * @return
	 */
	CuiForm[] forms() default {};
}
