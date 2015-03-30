package com.jsoft.cocit.entity.coc;

import com.jsoft.cocit.entity.ITreeEntity;

/**
 * 动作：即在功能模块上执行的操作。
 * 
 * <UL>
 * <LI>代表一个运行时的自定义数据操作，通常由定义在数据库中的数据实体解析而来；
 * <LI>与数据表的关系：每个数据操作只能隶属于一个数据表，而一个数据表可以包含多个数据操作；
 * <LI>同一个数据表内数据操作之间的关系：数据操作可以是树形结构，用于构成树形结构的操作菜单；
 * </UL>
 * <p>
 * <b>操作模式：</b>
 * <UL>
 * <LI>c: 用于新增操作，所有新增操作都以c开头
 * <LI>e: 用于编辑操作，所有编辑操作都以e开头
 * <LI>d: 用于删除操作，所有删除操作都以d开头
 * <LI>v: 用于浏览操作，所有浏览操作都以v开头
 * <LI>bu: 批量修改
 * </UL>
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface ICocAction extends ITreeEntity {
	String getCocEntityKey();

	Integer getOpCode();

	String getPlugin();

	String getTitle();

	String getBtnImage();

	String getLogo();

	String getUiForm();

	String getUiFormUrl();

	String getUiFormTarget();
	
	/**
	 * <b>默认值规则：</b>即执行该操作时，如果字段值不存在，则自动将规则中指定的字段值赋予相应的字段。
	 */
	String getDefaultValuesRule();

	/**
	 * <b>赋值规则：</b>即执行该操作时，不管字段值是否存在，都自动将规则中指定的字段值赋予相应的字段。
	 * <p>
	 * 注：如果设置了“条件规则({@link #getWhereRule()})”，则只有满足条件的数据才会被赋值。
	 * <p>
	 * 主要用于“审核类操作”。
	 * <p>
	 * 相当于SQL语句“UPDATE table1 (field1,field2) VALUES (value1, value2)”中"VALUES"后面的部分。
	 */
	String getAssignValuesRule();

	/**
	 * <b>条件规则：</b>条件规则有两个用途
	 * <UL>
	 * <LI>用作赋值条件：只有满足该条件的数据对象，其相应的字段值才会被“赋值规则({@link #getAssignValuesRule()})”中的字段值所替换。
	 * <LI>用作行操作：只有满足该条件的数据行，才会显示此操作。
	 * </UL>
	 */
	String getWhereRule();

	String getProxyActions();

	String getSuccessMessage();

	String getErrorMessage();

	String getWarnMessage();

	int getUiWindowHeight();

	int getUiWindowWidth();

}
