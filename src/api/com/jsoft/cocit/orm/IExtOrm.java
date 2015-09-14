package com.jsoft.cocit.orm;

import java.util.List;

import com.jsoft.cocimpl.orm.DMLSession;
import com.jsoft.cocimpl.orm.dialect.Dialect;
import com.jsoft.cocimpl.orm.generator.INamingStrategy;
import com.jsoft.cocit.orm.expr.NullCndExpr;
import com.jsoft.cocit.orm.mapping.EnMapping;

/**
 * 数据库ORM： 封装底层DAO对数据库的操作。
 * <UL>
 * <LI>支持条件表达式；
 * <LI>不受事务控制；
 * <LI>不能执行业务插件；
 * <LI>不支持直接对表的操作；
 * <LI>支持关联实体的同步操作：底层DAO不支持关联实体的同步操作；
 * </UL>
 * 
 * @author yongshan.ji
 * @preserve all
 * @param <T>
 */
public interface IExtOrm extends IOrm {
	IDao getDao();
	
	public DMLSession getDMLSession();

	/**
	 * <b>插入实体对象</b>
	 * <p>
	 * 1. 插入的实体对象可以是：单个实体、实体集合、数组、Map等；
	 * <p>
	 * 2. 不存在的Many/ManyMany关联对象将会被自动保存；
	 * <p>
	 * 3. 只有匹配正则表达式的字段才会被插入，如果未指定字段正则表达式，则所有字段都将被插入；
	 * <p>
	 * 4. 多个正则表达式之间按“|”关系处理；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式(如“id|name”)
	 * @return 新增的记录数
	 */
	public int insert(Object obj, NullCndExpr fieldRexpr);

	public int insert(Object obj);

	/**
	 * <b>修改实体对象</b>
	 * <p>
	 * 1. 修改的实体对象可以是：单个实体、实体集合、数组、Map等；
	 * <p>
	 * 2. 不存在的Many/ManyMany关联对象将会被自动保存；
	 * <p>
	 * 3. 只有匹配正则表达式的字段才会被修改，如果未指定字段正则表达式，则所有字段都将被修改；
	 * <p>
	 * 4. 多个正则表达式之间按“|”关系处理；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式(如“id|name”)
	 * @return 修改的记录数
	 */
	public int update(Object obj, NullCndExpr fieldRexpr);

	public int update(Object obj);

	/**
	 * <b>查询分页实体集</b>
	 * <p>
	 * 1. 如果未指定查询条件，则将对整表进行查询;
	 * <p>
	 * 2. 只有满足正则表达式的字段才会被绑定，如果未指定字段表达式，则所有字段都将被绑定。
	 * <p>
	 * 3. 多个表达式中：条件表达式按“AND”关系处理；字段表达式按“|”关系处理；排序、分组、分页等表达式来自第一个表达式；
	 * <p>
	 * 4. 表达式来自查询分页器；
	 * <p>
	 * 5. 满足条件的总记录数将被回填到查询分页器中；
	 * 
	 * @param pager
	 *            查询分页器
	 * @return 分页结果集
	 */
	public List query(PageResult pager);

	/**
	 * 获取指定实体类的映射，默认将自动同步数据库表、字段、外键等。
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @return 实体映射
	 */
	public EnMapping getEnMapping(Class classOfEntity);

	/**
	 * 获取指定实体类的映射，并根据autoDDL决定是否同步数据库表、字段、外键等。
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param asynTable
	 *            是否自动同步数据库表、字段、外键等。
	 * @return 实体映射
	 */
	public EnMapping getEnMapping(Class classOfEntity, boolean asynTable);

	public EnMapping getEnMapping(Class classOfT, boolean syncTable, boolean syncRefTable);

	/**
	 * 获取实体类的ID字段属性名
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @return 属性名
	 */
	public String getIdProperty(Class classOfEntity);

	/**
	 * 获取数据库本地方言
	 * 
	 * @return
	 */
	public Dialect getDialect();

	/**
	 * 获取命名策略
	 * <p>
	 * 命名策略用来将Java实体类名、属性名转换成数据库表名、字段名等。
	 * 
	 * @param classOfEntity
	 *            实体类，不同的实体类可以有不同的命名策略
	 * @return 命名策略
	 */
	public INamingStrategy getNamingStrategy();

	public void removeMapping(Class cls);

	public void clearMapping();
}
