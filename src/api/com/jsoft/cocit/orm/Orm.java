package com.jsoft.cocit.orm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.NullCndExpr;

/**
 * 对象关系数据库映射接口：用于管理和查询数据库数据表，包括：增、删、查、改数据库表记录。
 * 
 * @author yongshan.ji
 * @preserve all
 * 
 */
public interface Orm {
	/**
	 * <b>保存实体对象</b>
	 * <p>
	 * 1. 保存的实体对象可以是：单个实体、实体集合、数组、Map等；
	 * <p>
	 * 2. 如果实体数据已经存在（物理主键大于0即认为实体对象已经存在）则对数据作修改操作，否则插入一条新记录到数据表中；
	 * <p>
	 * 3. 不存在的Many/ManyMany关联对象将会被自动保存；
	 * <p>
	 * 4. 只有匹配正则表达式的字段才会被保存，如果未指定字段正则表达式，则所有字段都将被保存；
	 * <p>
	 * 5. 多个正则表达式之间按“|”关系处理；
	 * 
	 * @param obj
	 *            实体对象
	 * @param fieldRexpr
	 *            字段正则表达式(如:“id|name”)
	 * @return 新增和修改的总记录数
	 */
	public int save(Object obj, NullCndExpr fieldRexpr);

	public int save(Object obj);

	/**
	 * <b>批量修改</b>
	 * <p>
	 * 1. 不存在的Many/ManyMany关联对象将会被自动保存；
	 * <p>
	 * 2. 只修改匹配正则表达式的字段，未指定正则表达式，则修改所有字段；
	 * <p>
	 * 3. 多个表达式中的条件表达式按“AND”关系处理；字段表达式按“|”关系处理；
	 * <p>
	 * 4. 满足条件的数据字段将被实体对象中的字段值替换；
	 * 
	 * @param obj
	 *            实体对象
	 * @param expr
	 *            表达式
	 * @return 受影响的记录数
	 */
	public int updateMore(Object obj, CndExpr expr);

	/**
	 * <b>删除实体对象</b>
	 * <p>
	 * 1. 删除的实体对象可以是：单个实体、实体集合、数组、Map等；
	 * <p>
	 * 2. 关联的Many/ManyMany数据“不会”被自动删除；
	 * 
	 * @param obj
	 *            实体对象
	 * @return 成功删除的记录数
	 */
	public int delete(Object obj);

	/**
	 * <b>删除实体对象</b>
	 * <p>
	 * 1. 只删除实体ID与指定ID相同的实体对象；
	 * <p>
	 * 2. 关联的Many/ManyMany数据“不会”被自动删除；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param id
	 *            实体ID，可以是Long型的物理主键，也可以是String型的逻辑主键。
	 * @return 成功删除的记录数
	 */
	public int delete(Class classOfEntity, Serializable id);

	public int delete(Class classOfEntity, Long id);

	public int delete(Class classOfEntity, String key);

	/**
	 * <b>批量删除</b>
	 * <p>
	 * 1. 删除满足条件的实体数据，如果没有指定删除条件，则将清空整张表；
	 * <p>
	 * 2. 关联的Many/ManyMany数据“不会”被自动删除；
	 * <p>
	 * 3. 多个表达式中的条件表达式按“AND”关系处理；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param expr
	 *            表达式
	 * @return 成功删除的记录数
	 */
	public int delete(Class classOfEntity, CndExpr expr);

	/**
	 * 清除实体表中的所有数据
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @return
	 */
	public int clear(Class classOfEntity, CndExpr expr);

	/**
	 * <b>获取实体对象</b>
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param id
	 *            实体ID，可以是Long型的物理主键，也可以是String型的逻辑主键
	 * @return 实体对象
	 */
	public <T> T get(Class<T> classOfEntity, Serializable id);

	public <T> T get(Class<T> classOfEntity, Long id);

	public <T> T get(Class<T> classOfEntity, String key);

	/**
	 * <b>加载实体对象</b>
	 * <p>
	 * 1. 加载满足条件的实体对象；
	 * <p>
	 * 2. 如果未指定条件则将对整张表进行查询；
	 * <p>
	 * 3. 如果满足条件的数据有多条，则加载第一条；
	 * <p>
	 * 4. 只有满足正则表达式的字段才会被绑定，如果未指定字段表达式，则所有字段都将被绑定；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param expr
	 *            条件表达式
	 * @return 实体对象
	 */
	public <T> T get(Class<T> classOfEntity, CndExpr expr);

	/**
	 * <b>加载实体对象</b>
	 * <p>
	 * 1. 加载实体ID与指定ID相等的实体对象；
	 * <p>
	 * 2. 只有匹配正则表达式的字段才会被绑定到实体对象中，如果未指定字段表达式，则所有字段都将被绑定到实体对象中；
	 * <p>
	 * 3. 多个正则表达式之间按“|”关系处理；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param id
	 *            实体数据ID
	 * @param fieldRexpr
	 *            字段正则表达式(如“id|name”)
	 * @return 实体对象
	 */
	public <T> T get(Class<T> classOfEntity, Serializable id, CndExpr fieldRexpr);

	//
	// /**
	// * <b>加载实体对象</b>
	// * <p>
	// * 1. 加载满足条件的实体对象；
	// * <p>
	// * 2. 如果未指定条件则将对整张表进行查询；
	// * <p>
	// * 3. 如果满足条件的数据有多条，则加载第一条；
	// * <p>
	// * 4. 只有满足正则表达式的字段才会被绑定，如果未指定字段表达式，则所有字段都将被绑定；
	// *
	// * @param classOfEntity
	// * 实体类
	// * @param expr
	// * 条件表达式
	// * @return 实体对象
	// */
	// public <T> T load(Class<T> classOfEntity, CndExpr expr);

	/**
	 * <b>查询分页实体集</b>
	 * <p>
	 * 1. 如果未指定查询条件，则将对整表进行查询;
	 * <p>
	 * 2. 只有满足正则表达式的字段才会被绑定，如果未指定字段表达式，则所有字段都将被绑定。
	 * <p>
	 * 3. 多个表达式中：条件表达式按“AND”关系处理；字段表达式按“|”关系处理；排序、分组、分页等表达式来自第一个表达式；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param expr
	 *            表达式
	 * @return 分页结果集
	 */
	public <T> List<T> query(Class<T> classOfEntity, CndExpr expr);

	public <T> List<T> query(Class<T> classOfEntity);

	/**
	 * <b>统计</b>
	 * <p>
	 * 1. 若未指定统计条件，则将对整表进行统计；
	 * <p>
	 * 2. 多个表达式中：条件表达式按“AND”关系处理；
	 * 
	 * @param classOfEntity
	 *            实体类
	 * @param expr
	 *            表达式
	 * @return 实体数据记录数
	 */
	public int count(Class classOfEntity, CndExpr expr);

	public int count(Class classOfEntity);

	/**
	 * 运行Connection回调，便于直接使用数据库Connection对象
	 * 
	 * @param conn
	 *            回调对象
	 * @return 运行结果
	 * @throws Exception
	 */
	public Object run(ConnCallback conn);

	/**
	 * 直接执行SQL查询并将查询结果转换为列表对象。
	 * 
	 * @param typeOfEntity
	 *            数据对象类型：即结果集中的每条记录都会被转换成该类的实例。
	 * @param sql
	 *            SQL语句，支持占位符。
	 * @param sqlParams
	 *            SQL参数，用来填充SQL语句中的占位符，请确保参数数量与占位符数量及类型的一致性。
	 * @param columnToPropMap
	 *            映射，用来描述SQL结果集中的字段名如何映射到JAVA属性名。该映射对象中key为SQL列名，value为JAVA属性名。
	 * @return 对象列表
	 */
	public <T> List<T> query(Class<T> typeOfEntity, String sql, List sqlParams, Map<String, String> columnToPropMap);

	public int execSql(String sql, List sqlParams);

}
