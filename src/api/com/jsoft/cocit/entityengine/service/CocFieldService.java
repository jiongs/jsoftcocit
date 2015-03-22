package com.jsoft.cocit.entityengine.service;

import java.util.List;

import com.jsoft.cocit.entity.coc.ICocField;
import com.jsoft.cocit.util.Option;

/**
 * CoC自定义数据实体字段，也称“组件化自定义数据实体字段”、“自定义数据实体字段”、“数据实体字段”、“实体字段”、“数据字段”等。
 * 
 * 
 * <UL>
 * <LI>代表一个运行时的自定义数据字段，通常由定义在数据库中的数据实体解析而来；
 * <LI>与数据分组的关系：每个数据字段只能隶属于一个数据分组；
 * <LI>与数据表的关系：每个数据字段只能隶属于一个数据表；
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public interface CocFieldService extends NamedEntityService<ICocField>, ICocField {

	/**
	 * 获取实体模块Service
	 * 
	 * @return
	 */
	CocEntityService getEntity();

	/**
	 * 获取字段组Service
	 * 
	 * @return
	 */
	CocGroupService getGroup();

	/**
	 * 获取“OneToMany”引用到的目标模块Service
	 * 
	 * @return
	 */
	CocEntityService getOneToManyTargetEntity();

	/**
	 * 获取外键引用到的目标模块Service
	 * 
	 * @return
	 */
	CocEntityService getFkTargetEntity();

	/**
	 * 格式化字段值
	 * 
	 * @param fieldValue
	 *            字段值
	 * @return
	 */
	String formatFieldValue(Object fieldValue);

	/**
	 * 获取字典字段的选项数组
	 * 
	 * @return
	 */
	Option[] getDicOptionsArray();

	/**
	 * 根据字段值获取字典选项
	 * 
	 * @param fieldValue
	 *            字段值
	 * @return
	 */
	Option getDicOption(Object fieldValue);

	/**
	 * 根据操作编号获取字段显示模式
	 * 
	 * @param actionID
	 *            操作编号
	 * @return
	 */
	int getMode(String actionID, Object entityObject);

	// int getMode(String actionID);

	/**
	 * 获取上传字段的文件类型
	 * 
	 * @return
	 */
	List<String> getUploadTypes();

	// String getMode(String actionID, boolean mustPriority, String defalutMode);

	/**
	 * 检查该字段是否是外键字段？
	 * 
	 * @return
	 */
	boolean isFkField();

	/**
	 * 检该字段是否是实体类型的外键字段？即标注了@ManyToOne的字段。
	 * 
	 * @return
	 */
	boolean isManyToOne();

	boolean isNumber();

	/**
	 * 检查该字段是否是Boolean字段
	 * 
	 * @return
	 */
	boolean isBoolean();

	/**
	 * 检查该字段是否是String字段
	 * 
	 * @return
	 */
	boolean isString();

	/**
	 * 检查该字段是否是文本字段
	 * 
	 * @return
	 */
	boolean isText();

	/**
	 * 检查该字段是否是富文本字段
	 * 
	 * @return
	 */
	boolean isRichText();

	/**
	 * 检查改制段是否是OneToMany(mappedBy="xxx")或OneToOne(mappedBy="xxx")字段
	 * 
	 * @return
	 */
	boolean isOneToMany();

	/**
	 * 获取字段的返回值类型
	 * 
	 * @return
	 */
	Class getClassOfField();

	/**
	 * 通常fullPropName与 propName相同，@ManyToOne字段fullPropName = propName + '.' + targetFieldKey，
	 * 
	 * @return
	 */
	String getFullPropName();

	boolean isDic();

}
