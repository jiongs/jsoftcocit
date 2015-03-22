package com.jsoft.cocit.entityengine.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.entity.coc.ICocEntity;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.util.Tree;

/**
 * 实体表服务类：对象将为实体表提供一对一的服务。
 * <UL>
 * <LI>代表一个运行时的自定义数据表，通常由定义在数据库中的数据实体解析而来；
 * <LI>与数据模块的关系：每个数据表可以被绑定到多个数据模块，但每个数据模块只能绑定一个数据表；
 * <LI>与数据分组的关系：每个数据表可以包含多个数据分组；
 * <LI>与数据字段的关系：每个数据表可以包含多个数据字段；
 * <LI>与数据子表的关系：每个数据表可以包含多个数据子表，用于描述一主多从结构的数据关系；
 * <LI>与数据父表的关系：每个数据表可以隶属于多个数据父表（如：自定义数据字段表既是自定义数据组的子表，也是自定义数据表的子表）；
 * <LI>与数据操作的关系：每个数据表可以包含多个数据操作，但每个数据操作只能隶属于一个数据表；
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public interface CocEntityService extends NamedEntityService<ICocEntity>, ICocEntity {

	List<CocEntityService> getSubEntities();

	List<CocGroupService> getGroups();

	CocGroupService getGroup(String groupKey);

	List<CocFieldService> getFields();

	List<CocFieldService> getFieldsOfEnabled();

	CocFieldService getField(String propName);

	List<CocFieldService> getFieldsOfFilter(boolean usedToSubEntity);

	List<CocFieldService> getFieldsOfDataAuth();

	CocFieldService getFieldOfTree();

	CocFieldService getFieldOfGroup();

	List<CocFieldService> getFieldsOfGrid(List<String> fields);

	/**
	 * <propName, EntityFieldService>
	 * 
	 * @return
	 */
	Map<String, CocFieldService> getFieldsMap();

	List<CocFieldService> getFkFields();

	/**
	 * 获取其他模块的外键字段：即其他模块的某些字段引用到此模块。
	 * 
	 * @return
	 */
	List<CocFieldService> getFkFieldsOfOtherEntities();

	/**
	 * 获取“儿子模块”的“外键字段”：即所有儿子模块都将有一个字段引用到此模块，这些字段的集合即为此模块的返回值。
	 * <p>
	 * 子模块： 即某些模块会以“此模块”为“父亲模块”，这样的模块称为此模块的子模块。子模块中一定有一个“逻辑外键”引用到“此模块”，且将该“逻辑外键”已被设置为“儿子外键”。
	 * 
	 * @return
	 */
	List<CocFieldService> getFkFieldsOfSubEntities();

	/**
	 * 获取子模块的外键字段：即子模块通过哪个外键关联到此模块？
	 * 
	 * @param subEntityKey
	 *            子模块KEY
	 * @return 子模块的外键字段
	 */
	CocFieldService getFkFieldOfSubEntity(String subEntityKey);

	List<CocFieldService> getFkFieldsOfRedundant(String fkPropName);

	CocFieldService getFkNameField(String fkPropName);

	List<CocActionService> getActions(List<String> actionKeys);

	CocActionService getAction(Serializable actionKey);

	SystemMenuService getSystemMenu();

	Tree getFilterData(List<String> filterFields, boolean usedToSubEntity);

	Tree getTreeData(CndExpr expr);

	Tree getRowsAuthData();

	void validateDataObject(String actionID, Object dataObject) throws CocException;

	<T> List<T> parseDataFromExcel(File excel);

	Class getClassOfEntity();

	String getPackageName();

	String getSimpleClassName();

	void initDataObjectWithDefaultValues(String actionID, Object dataObject);

	CuiEntityService getCuiEntity(String cuiKey);
}
