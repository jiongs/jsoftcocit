package com.jsoft.cocit.entityengine;

import java.util.List;

import com.jsoft.cocit.entityengine.bizplugin.IBizPlugin;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.expr.CndExpr;

/**
 * 实体管理器:
 * <UL>
 * <LI>自动加载操作码对应的业务插件，以便执行操作的时候执行相关的业务逻辑；
 * <LI>自动加载操作码对应的字段过滤表达式，以便只对特定的字段进行业务操作；
 * </UL>
 * 
 * @author yongshan.ji
 * @param <Object>
 */
public interface DataManager {
	public void release();

	public DataEngine getDataEngine();

	// public Orm orm();

	// public Class getType();

	// public SystemMenuService getFuncMenu();
	//
	// public SystemMenuService getEntityModule();

	/**
	 * 保存实体对象： 可以保存单个实体、实体集合、数组、Map等。
	 * <p>
	 * 不存在的关联(Many/ManyMany)对象将“会”被同步保存；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            实体对象
	 * @param opMode
	 *            操作码
	 * @return 保存了多少条记录
	 */
	public int save(SystemMenuService menuService, CocEntityService entityService, Object obj, String opMode) throws CocException;

	/**
	 * 批量修改： 修改满足条件的数据记录。
	 * <p>
	 * 不存在的关联(Many/ManyMany)对象将“会”被同步保存；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            实体对象: 存放最新字段值
	 * @param opMode
	 *            操作码
	 * @param expr
	 *            表达式: 用于描述满足条件的记录、要修改的字段等
	 * @return 修改了多少条数据
	 */
	public int updateMore(SystemMenuService menuService, CocEntityService entityService, Object obj, String opMode, CndExpr expr) throws CocException;

	/**
	 * 删除实体对象： 可以删除单个数据实体、实体集合、数组、Map等。
	 * <p>
	 * 关联(Many/ManyMany)对象将“不会”被同步删除；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            实体对象
	 * @param opMode
	 *            操作码
	 * @return 删除了多少条记录
	 */
	public int delete(SystemMenuService menuService, CocEntityService entityService, Object obj, String opMode) throws CocException;

	/**
	 * 删除实体对象： 删除实体ID与指定ID相同的对象
	 * <p>
	 * 关联(Many/ManyMany)对象将“不会”被同步删除；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param opMode
	 *            操作码
	 * @param expr
	 *            表达式
	 * @return 删除了多少条记录
	 * @throws CocException
	 */
	public int delete(SystemMenuService menuService, CocEntityService entityService, Long id, String opMode) throws CocException;

	/**
	 * 批量删除： 删除满足条件的实体数据记录
	 * <p>
	 * 关联(Many/ManyMany)对象将“不会”被同步删除；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param opMode
	 *            操作码
	 * @param expr
	 *            表达式
	 * @return 删除了多少条记录
	 */
	public int deleteMore(SystemMenuService menuService, CocEntityService entityService, String opMode, CndExpr expr) throws CocException;

	/**
	 * 加载实体对象
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param opMode
	 *            操作码
	 * @param id
	 *            实体ID
	 * @return 实体对象
	 */
	public Object load(SystemMenuService menuService, CocEntityService entityService, String opMode, Long id) throws CocException;

	public Object load(SystemMenuService systemMenu, CocEntityService cocEntity, String opMode, CndExpr expr);

	public int count(SystemMenuService menuService, CocEntityService entityService, String opMode, CndExpr expr) throws CocException;

	public List query(SystemMenuService menuService, CocEntityService entityService, String opMode, CndExpr expr) throws CocException;

	// /**
	// * 查询分页数据集
	// * <p>
	// * 查询条件或查询字段从分页器中获取；
	// * <p>
	// * 执行操作的同时执行操作码对应的业务逻辑；
	// *
	// * @param pager
	// * 查询分页器
	// * @param opMode
	// * 操作码
	// * @return 查询到的结果集
	// */
	// public List<Object> query(Pager<Object> pager, String opMode) throws CocException;

	/**
	 * 执行业务逻辑，最后一个业务插件设置到业务事件中的返回值就是执行的结果。
	 * 
	 * @param opMode
	 *            操作码
	 * @return 执行结果
	 */
	public Object run(SystemMenuService menuService, CocEntityService entityService, Object obj, String opMode) throws CocException;

	/**
	 * 异步保存实体对象： 可以保存单个实体、实体集合、数组、Map等。
	 * <p>
	 * 不存在的关联(Many/ManyMany)对象将被同步保存；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            实体对象
	 * @param opMode
	 *            操作码
	 */
	public void asynSave(SystemMenuService menuService, CocEntityService entityService, Object obj, String opMode) throws CocException;

	/**
	 * 异步批量修改： 修改满足条件的数据记录。
	 * <p>
	 * 同步保存不存在的关联(Many/ManyMany)对象；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            单个实体对象
	 * @param opMode
	 *            操作码
	 * @param igloreNull
	 *            是否忽略空值
	 * @param expr
	 *            条件表达式
	 */
	public void asynUpdateMore(SystemMenuService menuService, CocEntityService entityService, Object obj, String opMode, CndExpr expr) throws CocException;

	/**
	 * 异步删除实体对象： 可以删除单个数据实体、实体集合、数组、Map等。
	 * <p>
	 * 同步删除关联(Many/ManyMany)对象；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param obj
	 *            实体对象
	 * @param opMode
	 *            操作码
	 * @return 删除了多少条记录
	 */
	public void asynDelete(SystemMenuService menuService, CocEntityService entityService, Object obj, String opMode) throws CocException;

	/**
	 * 异步批量删除： 删除满足条件的实体数据记录
	 * <p>
	 * 关联对象“不会被删除”；
	 * <p>
	 * 执行操作的同时执行操作码对应的业务逻辑；
	 * 
	 * @param klass
	 *            实体类
	 * @param opMode
	 *            操作码
	 * @param expr
	 *            表达式
	 */
	public void asynDeleteMore(SystemMenuService menuService, CocEntityService entityService, String opMode, CndExpr expr) throws CocException;

	// /**
	// * 异步查询分页数据集
	// * <p>
	// * 查询条件或查询字段从分页器中获取；
	// * <p>
	// * 执行操作的同时执行操作码对应的业务逻辑；
	// *
	// * @param pager
	// * 查询分页器
	// * @param opMode
	// * 操作码
	// */
	// public void asynQuery(Pager<Object> pager, String opMode) throws CocException;

	/**
	 * 异步执行业务逻辑，最后一个业务插件设置到业务事件中的返回值就是执行的结果。
	 * 
	 * @param opMode
	 *            操作码
	 */
	public Object asynRun(SystemMenuService menuService, CocEntityService entityService, Object obj, String opMode) throws CocException;

	public IBizPlugin[] getPlugins(SystemMenuService menuService, CocEntityService entityService, String actionMode);
}
