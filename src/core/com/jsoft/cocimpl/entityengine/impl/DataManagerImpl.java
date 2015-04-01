package com.jsoft.cocimpl.entityengine.impl;

import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.IMessageConfig;
import com.jsoft.cocit.constant.FieldNames;
import com.jsoft.cocit.entity.IDataEntity;
import com.jsoft.cocit.entityengine.DataEngine;
import com.jsoft.cocit.entityengine.DataManager;
import com.jsoft.cocit.entityengine.bizplugin.IBizPlugin;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.securityengine.LoginSession;
import com.jsoft.cocit.securityengine.SecurityEngine;

public class DataManagerImpl implements DataManager {

	//
	private DataEngine dataEngine;

	// EntityEngine entityEngine, SystemMenuService systemMenu, SystemMenuService module
	DataManagerImpl(DataEngine entityEngine) throws CocException {
		this.dataEngine = entityEngine;
	}

	public void release() {
		dataEngine.release();
		dataEngine = null;
	}

	public DataEngine getDataEngine() {
		return dataEngine;
	}

	public int save(SystemMenuService menuService, CocEntityService entityService, Object entity, String actionMode) throws CocException {
		this.checkPermission(menuService, entityService, entity, actionMode);

		entityService.validateDataObject(actionMode, entity);
		
		return dataEngine.save(entity, this.buildCndExpr(menuService, entityService, null, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public int updateMore(SystemMenuService menuService, CocEntityService entityService, Object obj, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, obj, actionMode);

		entityService.validateDataObject(actionMode, obj);

		return dataEngine.updateMore(obj, this.buildCndExpr(menuService, entityService, expr, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public int delete(SystemMenuService menuService, CocEntityService entityService, Object entity, String actionMode) throws CocException {
		this.checkPermission(menuService, entityService, entity, actionMode);

		if (entity instanceof List) {
			for (Object obj : (List) entity) {
				checkRefedBy(entityService, obj);
			}
		} else {
			checkRefedBy(entityService, entity);
		}

		return dataEngine.delete(entity, getPlugins(menuService, entityService, actionMode));
	}

	public int delete(SystemMenuService menuService, CocEntityService entityService, Long id, String actionMode) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		checkRefedBy(entityService, dataEngine.orm().get(entityService.getClassOfEntity(), id, Expr.fieldRexpr("id|key")));

		return dataEngine.delete(entityService.getClassOfEntity(), id, getPlugins(menuService, entityService, actionMode));
	}

	/**
	 * 检查当前对象是否被其他对象外键所引用？
	 * 
	 * @param entityService
	 * @param obj
	 */
	public void checkRefedBy(CocEntityService entityService, Object o) {
		if (!(o instanceof IDataEntity)) {
			return;
		}

		IMessageConfig msgs = Cocit.me().getMessages();

		IDataEntity obj = (IDataEntity) o;
		Orm orm = this.dataEngine.orm();
		List<CocFieldService> fkFields = entityService.getFkFieldsOfOtherEntities();
		if (fkFields != null) {
			for (CocFieldService fld : fkFields) {
				CocEntityService refEntity = fld.getEntity();
				String fkTargetField = fld.getFkTargetFieldKey();
				if (FieldNames.F_KEY.equals(fkTargetField)) {
					String key = obj.getKey();
					if (orm.count(refEntity.getClassOfEntity(), Expr.eq(fld.getFieldName(), key)) > 0) {
						throw new CocException(msgs.getMsg("10013", obj, refEntity.getName()));
					}
				} else if (FieldNames.F_ID.equals(fkTargetField)) {
					Long id = obj.getId();
					if (orm.count(refEntity.getClassOfEntity(), Expr.eq(fld.getFieldName(), id)) > 0) {
						throw new CocException(msgs.getMsg("10013", obj, refEntity.getName()));
					}
				}
			}
		}

	}

	public int deleteMore(SystemMenuService menuService, CocEntityService entityService, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		return dataEngine.deleteMore(entityService.getClassOfEntity(), expr, getPlugins(menuService, entityService, actionMode));
	}

	public Object load(SystemMenuService menuService, CocEntityService entityService, String actionMode, Long id) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		return dataEngine.load(entityService.getClassOfEntity(), id, this.buildCndExpr(menuService, entityService, null, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public Object load(SystemMenuService menuService, CocEntityService entityService, String opMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, opMode);

		return dataEngine.load(entityService.getClassOfEntity(), expr, getPlugins(menuService, entityService, opMode));
	}

	public int count(SystemMenuService menuService, CocEntityService entityService, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		return dataEngine.count(entityService.getClassOfEntity(), this.buildCndExpr(menuService, entityService, expr, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public List query(SystemMenuService menuService, CocEntityService entityService, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		return dataEngine.query(entityService.getClassOfEntity(), this.buildCndExpr(menuService, entityService, expr, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public Object run(SystemMenuService menuService, CocEntityService entityService, Object obj, String actionMode) throws CocException {
		return dataEngine.run(obj, this.buildCndExpr(menuService, entityService, null, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public void asynSave(SystemMenuService menuService, CocEntityService entityService, Object entity, String actionMode) throws CocException {
		this.checkPermission(menuService, entityService, entity, actionMode);

		entityService.validateDataObject(actionMode, entity);

		dataEngine.asynSave(entity, buildCndExpr(menuService, entityService, null, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	public void asynUpdateMore(SystemMenuService menuService, CocEntityService entityService, Object obj, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, obj, actionMode);

		entityService.validateDataObject(actionMode, obj);

		dataEngine.asynUpdateMore(obj, expr, getPlugins(menuService, entityService, actionMode));
	}

	public void asynDelete(SystemMenuService menuService, CocEntityService entityService, Object obj, String actionMode) throws CocException {
		this.checkPermission(menuService, entityService, obj, actionMode);

		dataEngine.asynDelete(obj, getPlugins(menuService, entityService, actionMode));
	}

	public void asynDeleteMore(SystemMenuService menuService, CocEntityService entityService, String actionMode, CndExpr expr) throws CocException {
		this.checkPermission(menuService, entityService, actionMode);

		dataEngine.asynDeleteMore(entityService.getClassOfEntity(), expr, getPlugins(menuService, entityService, actionMode));
	}

	public Object asynRun(SystemMenuService menuService, CocEntityService entityService, Object obj, String actionMode) throws CocException {

		return dataEngine.asynRun(obj, buildCndExpr(menuService, entityService, null, actionMode), getPlugins(menuService, entityService, actionMode));
	}

	// ====================================================================
	// 权限控制
	// ====================================================================

	protected void checkPermission(SystemMenuService menuService, CocEntityService entityService, String actionMode) throws CocException {

		SecurityEngine securityEngine = Cocit.me().getSecurityEngine();

		LoginSession login = Cocit.me().getHttpContext().getLoginSession();

		securityEngine.allowAccessAction(login, menuService, actionMode);
	}

	protected void checkPermission(SystemMenuService menuService, CocEntityService entityService, Object obj, String actionMode) throws CocException {
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
	protected CndExpr buildCndExpr(SystemMenuService systemMenu, CocEntityService entityService, CndExpr expr, String actionMode) {

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
	public IBizPlugin[] getPlugins(SystemMenuService menuService, CocEntityService entityService, String actionMode) {
		CocActionService entityAction = entityService.getAction(actionMode);

		if (entityAction == null)
			return null;

		return entityAction.getBizPlugins();
	}

}
