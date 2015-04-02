package com.jsoft.cocit.entity.coc;

import com.jsoft.cocit.entity.INamedEntity;

/**
 * 实体模块：用来描述“实体类、实体表、实体UI”等实体管理相关的属性。
 * 
 * @author yongshan.ji
 * @preserve all
 */
public interface ICocEntity extends INamedEntity {

	// ===========================================================================================================
	// 实体数据相关API
	// ===========================================================================================================

	/**
	 * 映射实体表， 如果未指定映射表，则自动计算映射表名称
	 * <p>
	 * 表示实体模块组件被映射到哪个实体表？
	 * <p>
	 * 如果指定的实体表不存在或空，则使用指定的值作为实体表名称并创建相关的实体表。
	 * <p>
	 * 如果指定的实体表已存在，则自动根据表属性创建实体模块组件。
	 * <p>
	 * 一般用于实体表已经存在，或无需对实体表进行编码的情况。
	 */
	String getTableName();

	/**
	 * 排序表达式：如“grid:id DESC; tree:name ASC”表示“在GRID中按name倒排序树形界面中按name排序”。
	 * 
	 * @return
	 */
	String getSortExpr();

	// ===========================================================================================================
	// 实体类相关API
	// ===========================================================================================================

	/**
	 * 实体分类KEY：逻辑外键字段，关联到实体分类的逻辑主键“{@link ICocCatalog#getKey()}”。
	 * 
	 * @return
	 */
	String getCatalogKey();

	/**
	 * 分类名称：外键冗余字段
	 * 
	 * @return
	 */
	String getCatalogName();

	/**
	 * 映射实体类，如果为指定映射实体类，择将自动生成映射实体类
	 * <p>
	 * 表示实体模块组件被映射到哪个实体类？
	 * <p>
	 * 如果指定的实体类不存在或空，则使用指定的值作为实体类名称并创建相关的实体类。
	 * <p>
	 * 如果指定的实体类已存在，则自动根据类属性创建实体模块组件。
	 * <p>
	 * 一般用于实体类已经存在，或无需对实体表进行编码的情况。
	 * 
	 * @return
	 */
	String getClassName();

	// /**
	// * 获取功能模块“数据实体”拥有者关联的字段，用来控制功能模块“数据实体”安全。
	// * <ul>
	// * <li>数据实体拥有者字段可以是组或用户类型的字段。也可以用"|"分隔的字段编号描述多字段</li>
	// * <li>如果数据实体拥有类型为用户，则该字段中的值与特定的用户相关联，表示模块数据实体的拥有者为特定的用户。</li>
	// * <li>如果数据实体拥有类型为组，则该字段中的值与特定的组相关联，表示模块数据实体的拥有者为特定的组。</li>
	// * </ul>
	// *
	// * @return 字段名称
	// */
	// String getEntityOwnerField();

	// ===========================================================================================================
	// UI相关API
	// ===========================================================================================================

	// /**
	// * UI类型：即描述默认以什么方式展现功能模块主界面？
	// *
	// * @return
	// */
	// byte getUiType();

	/**
	 * UI视图：即自定义功能模块主界面的JSP路径。实体模块管理界面可以不采用平台提供的默认展现方式，而是自定义模块界面。
	 * 
	 * @return
	 */
	String getUiView();

	String getPathPrefix();

	String getCompileVersion();

	String getExtendsClassName();

	String getUniqueFields();

	String getIndexFields();

	String getDataAuthFields();

	/**
	 * 检查该业务模块是否需要走工作流引擎？
	 * <p>
	 * true: 该业务模块需要走工作流引擎 <br/>
	 * false: 该业务模块无需走工作流引擎
	 * 
	 * @return
	 */
	boolean isWorkflow();
}
