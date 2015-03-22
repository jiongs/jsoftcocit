package com.jsoft.cocimpl.entityengine.impl.service;

import java.io.Serializable;
import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.IDSConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.entity.coc.ICocEntity;
import com.jsoft.cocit.entity.security.ISystem;
import com.jsoft.cocit.entity.security.ISystemMenu;
import com.jsoft.cocit.entity.security.ITenant;
import com.jsoft.cocit.entity.security.IUser;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.ExprUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
class EntityDAOHelper {
	private static EntityDAOHelper me;

	private Orm orm;

	private EntityDAOHelper() {
	}

	private Orm orm() {
		if (orm == null) {
			orm = Cocit.me().orm();
		}
		return orm;
	}

	static EntityDAOHelper get() {
		if (me == null)
			me = new EntityDAOHelper();

		return me;
	}

	IDSConfig getDataSource(Serializable dataSourceID) {
		if (dataSourceID instanceof String)
			return (IDSConfig) orm().get(EntityTypes.DataSource, (String) dataSourceID);
		else
			return (IDSConfig) orm().get(EntityTypes.DataSource, (Long) dataSourceID);
	}

	ICocEntity getEntity(Serializable entityID) {
		if (entityID instanceof String)
			return (ICocEntity) orm().get(EntityTypes.CocEntity, (String) entityID);
		else
			return (ICocEntity) orm().get(EntityTypes.CocEntity, (Long) entityID);
	}

	ITenant getTenant(Serializable tenantIDOrDomain) {
		if (tenantIDOrDomain instanceof String)
			return (ITenant) orm().get(EntityTypes.Tenant, (String) tenantIDOrDomain);
		else
			return (ITenant) orm().get(EntityTypes.Tenant, (Long) tenantIDOrDomain);
	}

	public List<ISystem> getSystems() {
		return (List<ISystem>) orm().query(EntityTypes.System);
	}

	ISystem getSystem(Serializable systemID) {
		if (systemID instanceof String)
			return (ISystem) orm().get(EntityTypes.System, (String) systemID);
		else
			return (ISystem) orm().get(EntityTypes.System, (Long) systemID);
	}

	public ISystemMenu getSystemMenu(Serializable systemMenuID) {
		if (systemMenuID instanceof String)
			return (ISystemMenu) orm().get(EntityTypes.SystemMenu, (String) systemMenuID);
		else
			return (ISystemMenu) orm().get(EntityTypes.SystemMenu, (Long) systemMenuID);
	}

	IUser getSystemUser(String tenantKey, String username) {
		CndExpr expr = ExprUtil.tenantIs(tenantKey);
		if (expr == null)
			expr = Expr.eq(Const.F_KEY, username);
		else
			expr = expr.and(Expr.eq(Const.F_KEY, username));

		return (IUser) orm().get(EntityTypes.SystemUser, expr);
	}

	<T> T get(Class<T> typeOfEntity, Serializable id) {
		if (id instanceof String)
			return (T) orm().get(typeOfEntity, (String) id);
		else
			return (T) orm().get(typeOfEntity, (Long) id);
	}

}
