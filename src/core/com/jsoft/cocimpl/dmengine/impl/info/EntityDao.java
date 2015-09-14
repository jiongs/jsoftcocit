package com.jsoft.cocimpl.dmengine.impl.info;

import java.io.Serializable;
import java.util.List;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.baseentity.coc.ICocCatalogEntity;
import com.jsoft.cocit.baseentity.coc.ICocEntity;
import com.jsoft.cocit.baseentity.security.ISystemEntity;
import com.jsoft.cocit.baseentity.security.ISystemMenuEntity;
import com.jsoft.cocit.baseentity.security.ITenantEntity;
import com.jsoft.cocit.baseentity.security.IUserEntity;
import com.jsoft.cocit.config.IDSConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
class EntityDao {
	private static EntityDao me;

	private IOrm orm;

	private EntityDao() {
	}

	private IOrm orm() {
		if (orm == null) {
			orm = Cocit.me().orm();
		}
		return orm;
	}

	static EntityDao get() {
		if (me == null)
			me = new EntityDao();

		return me;
	}

	IDSConfig getDataSource(Serializable dataSourceID) {
		if (dataSourceID instanceof String)
			return (IDSConfig) orm().get(EntityTypes.DataSource, (String) dataSourceID);
		else
			return (IDSConfig) orm().get(EntityTypes.DataSource, (Long) dataSourceID);
	}

	ICocCatalogEntity getCocCatalog(Serializable entityID) {
		if (entityID instanceof String)
			return (ICocCatalogEntity) orm().get(EntityTypes.CocCatalog, (String) entityID);
		else
			return (ICocCatalogEntity) orm().get(EntityTypes.CocCatalog, (Long) entityID);
	}

	ICocEntity getEntity(Serializable entityID) {
		if (entityID instanceof String)
			return (ICocEntity) orm().get(EntityTypes.CocEntity, (String) entityID);
		else
			return (ICocEntity) orm().get(EntityTypes.CocEntity, (Long) entityID);
	}

	ITenantEntity getTenant(Serializable tenantIDOrDomain) {
		if (tenantIDOrDomain instanceof String)
			return (ITenantEntity) orm().get(EntityTypes.Tenant, (String) tenantIDOrDomain);
		else
			return (ITenantEntity) orm().get(EntityTypes.Tenant, (Long) tenantIDOrDomain);
	}

	public List<ISystemEntity> getSystems() {
		return (List<ISystemEntity>) orm().query(EntityTypes.System);
	}

	ISystemEntity getSystem(Serializable systemID) {
		if (systemID instanceof String)
			return (ISystemEntity) orm().get(EntityTypes.System, (String) systemID);
		else
			return (ISystemEntity) orm().get(EntityTypes.System, (Long) systemID);
	}

	public ISystemMenuEntity getSystemMenu(Serializable systemMenuID) {
		if (systemMenuID instanceof String)
			return (ISystemMenuEntity) orm().get(EntityTypes.SystemMenu, (String) systemMenuID);
		else
			return (ISystemMenuEntity) orm().get(EntityTypes.SystemMenu, (Long) systemMenuID);
	}

	IUserEntity getSystemUserByCodeOrTel(String tenantCode, String username) {
		CndExpr expr = Expr.eq(Const.F_CODE, username).or(Expr.eq(Const.F_TEL, username));

		return (IUserEntity) orm().get(EntityTypes.SystemUser, expr);
	}

	<T> T get(Class<T> typeOfEntity, Serializable id) {
		if (id instanceof String)
			return (T) orm().get(typeOfEntity, (String) id);
		else
			return (T) orm().get(typeOfEntity, (Long) id);
	}

}
