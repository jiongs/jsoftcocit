package com.jsoft.cocimpl.dmengine.impl;

import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.baseentity.IDataEntity;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.constant.FieldNames;
import com.jsoft.cocit.dmengine.IDataEngine;
import com.jsoft.cocit.dmengine.IDataManager;
import com.jsoft.cocit.dmengine.bizplugin.IBizPlugin;
import com.jsoft.cocit.dmengine.info.ICocActionInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.securityengine.ILoginSession;
import com.jsoft.cocit.securityengine.SecurityService;
import com.jsoft.cocit.service.LogService;
import com.jsoft.cocit.util.StringUtil;

public class DataManagerImpl implements IDataManager {

	//
	private IDataEngine dataEngine;

	// EntityEngine entityEngine, SystemMenuService systemMenu, SystemMenuService module
	DataManagerImpl(IDataEngine entityEngine) throws CocException {
		this.dataEngine = entityEngine;
	}

	public void release() {
		dataEngine.release();
		dataEngine = null;
	}

	public IDataEngine getDataEngine() {
		return dataEngine;
	}

	public int save(ISystemMenuInfo menuService, ICocEntityInfo entityService, Object entity, String actionMode) throws CocException {
		this.checkPermission(menuService, entityService, entity, actionMode);

		entityService.validateDataObject(actionMode, entity);

		int result = 0;
		LogService logService = Cocit.me().getLogService();
		try {

			result = dataEngine.save(entity, this.buildCndExpr(menuService, entityService, null, actionMode), getPlugins(menuService, entityService, actionMode));

			return result;
		} finally {
			logService.makeOpLog(menuService, entityService, actionMode, entity, result);
		}
	}

	public int updateMore(ISystemMenuInfo menuService, ICocEntityInfo entityService, Object obj, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, obj, actionMode);

		entityService.validateDataObject(actionMode, obj);

		int result = 0;
		LogService logService = Cocit.me().getLogService();
		try {

			result = dataEngine.updateMore(obj, this.buildCndExpr(menuService, entityService, expr, actionMode), getPlugins(menuService, entityService, actionMode));

			return result;
		} finally {
			logService.makeOpLog(menuService, entityService, actionMode, obj, result);
		}
	}

	public int delete(final ISystemMenuInfo menuService, final ICocEntityInfo entityService, final Object entity, final String actionMode) throws CocException {
		int result = 0;
		// Trans.exec(new Atom() {
		// public void run() {
		checkPermission(menuService, entityService, entity, actionMode);

		if (entity instanceof List) {
			for (Object obj : (List) entity) {
				checkRefedBy(entityService, obj);
			}
		} else {
			checkRefedBy(entityService, entity);
		}

		// int result = 0;
		LogService logService = Cocit.me().getLogService();
		try {

			result = dataEngine.delete(entity, getPlugins(menuService, entityService, actionMode));
		} finally {
			logService.makeOpLog(menuService, entityService, actionMode, entity, result);
		}
		// }
		// });

		return result;
	}

	public int delete(final ISystemMenuInfo menuService, final ICocEntityInfo entityService, final Long id, final String actionMode) throws CocException {
		int result = 0;
		// Trans.exec(new Atom() {
		// public void run() {
		checkPermission(menuService, entityService, actionMode);

		checkRefedBy(entityService, dataEngine.orm().get(entityService.getClassOfEntity(), id, Expr.fieldRexpr("id|key")));

		LogService logService = Cocit.me().getLogService();
		try {

			result = dataEngine.delete(entityService.getClassOfEntity(), id, getPlugins(menuService, entityService, actionMode));
		} finally {
			logService.makeOpLog(menuService, entityService, actionMode, id, result);
		}
		// }
		// });

		return result;
	}

	/**
	 * 检查当前对象是否被其他对象外键所引用？
	 * 
	 * @param entityService
	 * @param obj
	 */
	public void checkRefedBy(ICocEntityInfo entityService, Object obj) {
		if (!(obj instanceof IDataEntity)) {
			return;
		}

		IMessageConfig msgs = Cocit.me().getMessages();
		IOrm orm = this.dataEngine.orm();

		IDataEntity mainObject = (IDataEntity) obj;// 主表数据
		List<ICocFieldInfo> fkFieldsOfChildren = entityService.getFkFieldsOfOtherEntities();
		if (fkFieldsOfChildren != null) {
			ICocEntityInfo childEntity;
			String mainPKField, fkFieldNameOfChild;
			Class typeOfChildEntity;
			for (ICocFieldInfo fkFieldOfChild : fkFieldsOfChildren) {

				childEntity = fkFieldOfChild.getEntity();// 子表实体
				mainPKField = fkFieldOfChild.getFkTargetFieldCode();// 主表主键字段
				fkFieldNameOfChild = fkFieldOfChild.getFieldName();// 子表外键字段
				typeOfChildEntity = childEntity.getClassOfEntity();// 子表实体类

				/*
				 * 获取主表主键值
				 */
				Object mainPK = null;
				if (FieldNames.F_CODE.equals(mainPKField)) {
					mainPK = mainObject.getCode();// 主表主键
				} else if (FieldNames.F_ID.equals(mainPKField)) {
					mainPK = mainObject.getId();
				}

				if (StringUtil.isBlank("" + mainPK)) {
					return;
				}

				/*
				 * 查询子表数据的条件
				 */
				CndExpr expr = null;
				if (fkFieldOfChild.isMultiSelect()) {
					expr = Expr.eq(fkFieldNameOfChild, mainPK).or(Expr.contains(fkFieldNameOfChild, mainPK + ",")).or(Expr.contains(fkFieldNameOfChild, "," + mainPK));
				} else {
					expr = Expr.eq(fkFieldNameOfChild, mainPK);
				}

				/*
				 * 级联删除子表数据
				 */
				if (fkFieldOfChild.isFkCascadeDelete()) {
					orm.delete(typeOfChildEntity, Expr.eq(fkFieldNameOfChild, mainPK));
				} else {
					/*
					 * 子表正在使用子表数据：则删除主表数据失败！
					 */
					if (orm.count(typeOfChildEntity, expr) > 0) {
						throw new CocException(msgs.getMsg("10013", mainObject, childEntity.getName()));
					}
				}

			}
		}

	}

	public int deleteMore(ISystemMenuInfo menuService, ICocEntityInfo entityService, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		return dataEngine.deleteMore(entityService.getClassOfEntity(), expr, getPlugins(menuService, entityService, actionMode));
	}

	public Object load(ISystemMenuInfo menuService, ICocEntityInfo entityService, String actionMode, Long id) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		return dataEngine.load(entityService.getClassOfEntity(), id, this.buildCndExpr(menuService, entityService, null, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public Object load(ISystemMenuInfo menuService, ICocEntityInfo entityService, String opMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, opMode);

		return dataEngine.load(entityService.getClassOfEntity(), expr, getPlugins(menuService, entityService, opMode));
	}

	public int count(ISystemMenuInfo menuService, ICocEntityInfo entityService, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		return dataEngine.count(entityService.getClassOfEntity(), this.buildCndExpr(menuService, entityService, expr, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public List query(ISystemMenuInfo menuService, ICocEntityInfo entityService, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		return dataEngine.query(entityService.getClassOfEntity(), this.buildCndExpr(menuService, entityService, expr, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public Object run(ISystemMenuInfo menuService, ICocEntityInfo entityService, Object obj, String actionMode) throws CocException {
		return dataEngine.run(obj, this.buildCndExpr(menuService, entityService, null, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public void asynSave(ISystemMenuInfo menuService, ICocEntityInfo entityService, Object entity, String actionMode) throws CocException {
		this.checkPermission(menuService, entityService, entity, actionMode);

		entityService.validateDataObject(actionMode, entity);

		dataEngine.asynSave(entity, buildCndExpr(menuService, entityService, null, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public void asynUpdateMore(ISystemMenuInfo menuService, ICocEntityInfo entityService, Object obj, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, obj, actionMode);

		entityService.validateDataObject(actionMode, obj);

		dataEngine.asynUpdateMore(obj, expr, getPlugins(menuService, entityService, actionMode));
	}

	public void asynDelete(ISystemMenuInfo menuService, ICocEntityInfo entityService, Object obj, String actionMode) throws CocException {
		this.checkPermission(menuService, entityService, obj, actionMode);

		dataEngine.asynDelete(obj, getPlugins(menuService, entityService, actionMode));
	}

	public void asynDeleteMore(ISystemMenuInfo menuService, ICocEntityInfo entityService, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		dataEngine.asynDeleteMore(entityService.getClassOfEntity(), expr, getPlugins(menuService, entityService, actionMode));
	}

	public Object asynRun(ISystemMenuInfo menuService, ICocEntityInfo entityService, Object obj, String actionMode) throws CocException {

		return dataEngine.asynRun(obj, buildCndExpr(menuService, entityService, null, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	// ====================================================================
	// 权限控制
	// ====================================================================

	protected void checkPermission(ISystemMenuInfo menuService, ICocEntityInfo entityService, String actionMode) throws CocException {

		SecurityService securityEngine = Cocit.me().getSecurityEngine();

		ILoginSession login = Cocit.me().getHttpContext().getLoginSession();

		securityEngine.allowAccessAction(login, menuService, actionMode);
	}

	protected void checkPermission(ISystemMenuInfo menuService, ICocEntityInfo entityService, Object obj, String actionMode) throws CocException {
		if (obj instanceof List) {
			for (Object ele : (List) obj) {
				this.checkPermission(menuService, entityService, ele, actionMode);
			}
		} else {
			this.checkPermission(menuService, entityService, actionMode);
		}
	}

	/**
	 * 创建查询条件、字段过滤、数据权限等条件表达式
	 * 
	 * @param expr
	 *            指定的条件表达式
	 * @param actionMode
	 *            操作ID
	 * @return 新的条件表达式
	 */
	protected CndExpr buildCndExpr(ISystemMenuInfo systemMenu, ICocEntityInfo entityService, CndExpr expr, String actionMode) {

		CndExpr fkExpr = Cocit.me().getSecurityEngine().getDataFilter(systemMenu);
		if (fkExpr != null) {
			if (expr == null)
				expr = fkExpr;
			else
				expr = expr.and(fkExpr);
		}

		return expr;
	}

	/**
	 * 加载模块业务插件
	 * 
	 * @return
	 */
	public IBizPlugin[] getPlugins(ISystemMenuInfo menuService, ICocEntityInfo entityService, String actionMode) {
		ICocActionInfo entityAction = entityService.getAction(actionMode);

		if (entityAction == null)
			return null;

		return entityAction.getBizPlugins();
	}

}
