package com.jsoft.cocimpl.orm.nutz.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jsoft.cocimpl.orm.DMLSession;
import com.jsoft.cocimpl.orm.dialect.Dialect;
import com.jsoft.cocimpl.orm.generator.INamingStrategy;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.baseentity.IOfSystemEntity;
import com.jsoft.cocit.baseentity.IOfTenantEntity;
import com.jsoft.cocit.baseentity.IOfTenantEntityExt;
import com.jsoft.cocit.baseentity.security.ISystemEntity;
import com.jsoft.cocit.baseentity.security.ITenantEntity;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.orm.ConnCallback;
import com.jsoft.cocit.orm.IDao;
import com.jsoft.cocit.orm.IExtOrm;
import com.jsoft.cocit.orm.PageResult;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.orm.expr.NullCndExpr;
import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.securityengine.ILoginSession;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.ObjectUtil;

public class ProxyOrm implements IExtOrm {
	private OrmImpl orm;

	public ProxyOrm(OrmImpl orm) {
		this.orm = orm;
	}

	private String getLoginTenantCode() {
		if (!Cocit.me().getConfig().isMultiTenant()) {
			return null;
		}

		HttpContext ctx = Cocit.me().getHttpContext();
		if (ctx == null)
			return null;

		ILoginSession login = ctx.getLoginSession();
		if (login == null) {
			return "";
		}

		ISystemEntity system = login.getSystem();
		if (system == null) {
			return "";
		}

		ITenantEntity tenant = login.getTenant();
		if (tenant == null) {
			return "";
		}

		return tenant.getCode();
	}

	private String getLoginSystemCode() {

		HttpContext ctx = Cocit.me().getHttpContext();
		if (ctx == null)
			return null;

		ILoginSession login = ctx.getLoginSession();
		if (login == null) {
			return "";
		}

		ISystemEntity system = login.getSystem();
		if (system == null) {
			return "";
		}

		return system.getCode();
	}

	private void setSystemTenantToObj(Object obj) {
		if (obj instanceof IOfTenantEntityExt) {
			String tenantCode = getLoginTenantCode();
			if (tenantCode != null) {
				((IOfTenantEntityExt) obj).setTenantCode(tenantCode);
			}
		}

	}

	private CndExpr filterBySystemTenant(Object obj, CndExpr expr) {

		HttpContext ctx = Cocit.me().getHttpContext();
		if (ctx == null) {
			return expr;
		}

		Class typeOfEntity;
		if (obj instanceof Class) {
			typeOfEntity = (Class) obj;
		} else if (obj instanceof List) {
			List list = (List) obj;
			if (list.size() == 0)
				return expr;

			typeOfEntity = list.get(0).getClass();
		} else {
			typeOfEntity = obj.getClass();
		}

		CndExpr systemExpr = null;
		if (IOfSystemEntity.class.isAssignableFrom(typeOfEntity)) {
			String systemCode = getLoginSystemCode();
			systemExpr = ExprUtil.systemIs(systemCode);
		}
		CndExpr tenantExpr = null;
		if (IOfTenantEntity.class.isAssignableFrom(typeOfEntity)) {
			String tenantCode = getLoginTenantCode();
			tenantExpr = ExprUtil.tenantIs(tenantCode);
		}

		if (tenantExpr != null) {
			if (expr == null) {
				expr = tenantExpr;
			} else {
				expr = expr.and(tenantExpr);
			}
		}

		if (systemExpr != null) {
			if (expr == null) {
				expr = systemExpr;
			} else {
				expr = expr.and(systemExpr);
			}
		}

		return expr;
	}

	public int save(Object obj, NullCndExpr fieldRexpr) {
		setSystemTenantToObj(obj);

		return orm.save(obj, fieldRexpr);
	}

	public int insert(Object obj, NullCndExpr fieldRexpr) {
		setSystemTenantToObj(obj);

		return orm.insert(obj, fieldRexpr);
	}

	public int update(Object obj, NullCndExpr fieldRexpr) {
		setSystemTenantToObj(obj);

		return orm.update(obj, fieldRexpr);
	}

	public int updateMore(Object obj, CndExpr expr) {
		expr = filterBySystemTenant(obj, expr);

		return orm.updateMore(obj, expr);
	}

	public int delete(Object obj) {
		Class cls;
		List<Serializable> idList = new ArrayList();
		if (obj instanceof List) {
			List list = (List) obj;
			if (list.size() == 0)
				return 0;

			cls = list.get(0).getClass();
			for (Object o : ((List) obj)) {
				idList.add(ObjectUtil.getId(o));
			}
		} else {
			cls = obj.getClass();
			idList.add(ObjectUtil.getId(obj));
		}

		CndExpr expr = Expr.in(Const.F_ID, idList);
		expr = filterBySystemTenant(cls, expr);

		return orm.delete(cls, expr);
	}

	public int delete(Class klass, Serializable id) {
		CndExpr expr = Expr.eq(Const.F_ID, id);
		expr = filterBySystemTenant(klass, expr);

		return orm.delete(klass, expr);
	}

	public int delete(Class klass, Long id) {
		CndExpr expr = Expr.eq(Const.F_ID, id);
		expr = filterBySystemTenant(klass, expr);

		return orm.delete(klass, expr);
	}

	public int delete(Class klass, String key) {
		CndExpr expr = Expr.eq(Const.F_CODE, key);
		expr = filterBySystemTenant(klass, expr);

		return orm.delete(klass, expr);
	}

	public int delete(Class klass, CndExpr expr) {
		expr = filterBySystemTenant(klass, expr);

		return orm.delete(klass, expr);
	}

	public int clear(Class classOfEntity, CndExpr expr) {
		expr = filterBySystemTenant(classOfEntity, expr);

		return orm.clear(classOfEntity, expr);
	}

	public Object get(Class klass, Serializable id) {
		if (id instanceof Long) {
			return get(klass, (Long) id);
		} else {
			return get(klass, id.toString());
		}
	}

	public Object get(Class klass, Long id) {
		CndExpr expr = Expr.eq(Const.F_ID, id);
		expr = filterBySystemTenant(klass, expr);

		return orm.get(klass, expr);
	}

	public Object get(Class klass, String key) {
		CndExpr expr = Expr.eq(Const.F_CODE, key);
		expr = filterBySystemTenant(klass, expr);

		return orm.get(klass, expr);
	}

	public Object load(Class klass) {
		CndExpr expr = filterBySystemTenant(klass, null);

		return orm.load(klass, expr);
	}

	public Object load(Class klass, CndExpr expr) {
		expr = filterBySystemTenant(klass, expr);

		return orm.load(klass, expr);
	}

	public List query(Class classOfEntity) {
		return query(classOfEntity, null);
	}

	public List query(Class classOfEntity, CndExpr expr) {
		expr = filterBySystemTenant(classOfEntity, expr);

		return orm.query(classOfEntity, expr);
	}

	public List query(PageResult pager) {
		CndExpr expr = filterBySystemTenant(pager.getType(), pager.getQueryExpr());
		pager.setQueryExpr(expr);

		return orm.query(pager);
	}

	public int count(Class classOfEntity) {
		return count(classOfEntity, null);
	}

	public int count(Class classOfEntity, CndExpr expr) {
		expr = filterBySystemTenant(classOfEntity, expr);

		return orm.count(classOfEntity, expr);
	}

	public Object get(Class classOfEntity, CndExpr expr) {
		expr = filterBySystemTenant(classOfEntity, expr);

		return orm.get(classOfEntity, expr);
	}

	@Override
	public int save(Object obj) {
		return orm.save(obj);
	}

	@Override
	public <T> T get(Class<T> classOfEntity, Serializable id, CndExpr fieldRexpr) {
		return (T) orm.get(classOfEntity, id, fieldRexpr);
	}

	@Override
	public Object run(ConnCallback conn) {
		return orm.run(conn);
	}

	@Override
	public <T> List<T> query(Class<T> typeOfEntity, String sql, List sqlParams, Map<String, String> columnToPropMap) {
		return orm.query(typeOfEntity, sql, sqlParams, columnToPropMap);
	}

	@Override
	public int execSql(String sql, List sqlParams) {
		return orm.execSql(sql, sqlParams);
	}

	@Override
	public DMLSession getDMLSession() {
		return orm.getDMLSession();
	}

	@Override
	public int insert(Object obj) {
		return orm.insert(obj);
	}

	@Override
	public int update(Object obj) {
		return orm.update(obj);
	}

	@Override
	public EnMapping getEnMapping(Class classOfEntity) {
		return orm.getEnMapping(classOfEntity);
	}

	@Override
	public EnMapping getEnMapping(Class classOfEntity, boolean asynTable) {
		return orm.getEnMapping(classOfEntity, asynTable);
	}

	@Override
	public EnMapping getEnMapping(Class classOfT, boolean syncTable, boolean syncRefTable) {
		return orm.getEnMapping(classOfT, syncTable, syncRefTable);
	}

	@Override
	public String getIdProperty(Class classOfEntity) {
		return orm.getIdProperty(classOfEntity);
	}

	@Override
	public Dialect getDialect() {
		return orm.getDialect();
	}

	@Override
	public INamingStrategy getNamingStrategy() {
		return orm.getNamingStrategy();
	}

	@Override
	public void removeMapping(Class cls) {
		orm.removeMapping(cls);
	}

	@Override
	public void clearMapping() {
		orm.clearMapping();
	}

	public OrmImpl getProxiedOrm() {
		return orm;
	}

	@Override
	public IDao getDao() {
		return orm.getDao();
	}
}