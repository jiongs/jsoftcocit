package com.jsoft.cocimpl.orm.nutz.impl;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.nutz.dao.Condition;
import org.nutz.dao.entity.Entity;
import org.nutz.lang.Mirror;

import com.jsoft.cocimpl.orm.DMLSession;
import com.jsoft.cocimpl.orm.Pager;
import com.jsoft.cocimpl.orm.dialect.Dialect;
import com.jsoft.cocimpl.orm.generator.INamingStrategy;
import com.jsoft.cocimpl.orm.listener.EntityListeners;
import com.jsoft.cocimpl.orm.nutz.EnMappingHolder;
import com.jsoft.cocimpl.orm.nutz.EnMappingMaker;
import com.jsoft.cocimpl.util.Assert;
import com.jsoft.cocit.config.IDSConfig;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.exception.CocDBException;
import com.jsoft.cocit.orm.ConnCallback;
import com.jsoft.cocit.orm.ExtDao;
import com.jsoft.cocit.orm.ExtOrm;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.orm.expr.NullCndExpr;
import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class OrmImpl implements ExtOrm {

	// 依赖注入
	private ExtDao dao;

	public OrmImpl(IDSConfig config, EnMappingHolder holder, EnMappingMaker maker, EntityListeners listeners) {
		try {
			dao = new ExtDaoImpl(getComboPooledDataSource(config), holder, maker, listeners);
		} catch (Throwable e) {
			throw new CocDBException(ExceptionUtil.msg(e));
		}
	}

	protected DataSource getComboPooledDataSource(IDSConfig config) {
		ComboPooledDataSource ds = new ComboPooledDataSource();
		ds.setJdbcUrl(config.getUrl());
		try {
			ds.setDriverClass(config.getDriver());
		} catch (PropertyVetoException e) {
			LogUtil.error("ORM.getComboPooledDataSource: Error! %s", ExceptionUtil.msg(e));
		}

		Properties props = new Properties();

		Properties configProps = config.getProperties();
		if (configProps != null) {
			props.putAll(configProps);
		}

		if (props.getProperty("c3p0.testConnectionOnCheckout") == null)
			props.put("c3p0.testConnectionOnCheckout", "true");
		if (props.getProperty("c3p0.max_statement") == null)
			props.put("c3p0.max_statement", "500");
		if (props.getProperty("c3p0.timeout") == null)
			props.put("c3p0.timeout", "1000");
		if (props.getProperty("c3p0.initialPoolSize") == null)
			props.put("c3p0.initialPoolSize", "3");
		if (props.getProperty("c3p0.minPoolSize") == null)
			props.put("c3p0.minPoolSize", "5");
		if (props.getProperty("c3p0.maxPoolSize") == null)
			props.put("c3p0.maxPoolSize", "50");
		props.put("user", config.getUser());
		props.put("password", config.getPassword());

		ds.setProperties(props);
		/*
		 * 初始化时获取三个连接，取值应在minPoolSize与maxPoolSize之间。Default: 5
		 */
		ds.setInitialPoolSize(Integer.parseInt(props.getProperty("c3p0.initialPoolSize")));
		/*
		 * 连接池中保留的最大连接数。Default: 15
		 */
		ds.setMinPoolSize(Integer.parseInt(props.getProperty("c3p0.minPoolSize")));
		/*
		 * 连接池中保留的最大连接数。Default: 1000
		 */
		ds.setMaxPoolSize(Integer.parseInt(props.getProperty("c3p0.maxPoolSize")));
		// 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。
		ds.setAcquireIncrement(3);
		// 定义在从数据库获取新连接失败后重复尝试的次数
		ds.setAcquireRetryAttempts(100);
		// 次连接中间隔时间，单位毫秒
		ds.setAcquireRetryDelay(100);
		// 连接关闭时默认将所有未提交的操作提交
		ds.setAutoCommitOnClose(false);
		/*
		 * c3p0将建一张名为Test的空表，并使用其自带的查询语句进行测试。如果定义了这个参数那么 属性preferredTestQuery将被忽略。你不能在这张Test表上进行任何操作，它将只供c3p0测试 使用。Default: null
		 */
		ds.setAutomaticTestTable(Const.TEST_TABLE_NAME);
		/*
		 * 获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常。但是数据源仍有效 保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试 获取连接失败后该数据源将申明已断开并永久关闭。Default: false
		 */
		ds.setBreakAfterAcquireFailure(true);
		/*
		 * 当连接池用完时客户端调用getConnection()后等待获取新连接的时间，超时后将抛出 SQLException,如设为0则无限期等待。单位毫秒。Default: 0 等待2分钟后抛异常
		 */
		ds.setCheckoutTimeout(120000);
		/*
		 * 通过实现ConnectionTester或QueryConnectionTester的类来测试连接。类名需制定全路径。 Default: com.mchange.v2.c3p0.impl.DefaultConnectionTester
		 */
		// ds.setConnectionTesterClassName();
		/*
		 * 最大空闲时间,XXX秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0 5分钟未使用则丢弃
		 */
		ds.setMaxIdleTime(300);

		ds.setIdleConnectionTestPeriod(30);
		/*
		 * JDBC的标准参数，用以控制数据源内加载的PreparedStatements数量。但由于预缓存的statements 属于单个connection而不是整个连接池。所以设置这个参数需要考虑到多方面的因素。 如果maxStatements与maxStatementsPerConnection均为0，则缓存被关闭。Default: 0
		 */
		ds.setMaxStatements(100);
		/*
		 * maxStatementsPerConnection定义了连接池内单个连接所拥有的最大缓存statements数。Default: 0
		 */
		ds.setMaxStatementsPerConnection(10);
		/*
		 * c3p0是异步操作的，缓慢的JDBC操作通过帮助进程完成。扩展这些操作可以有效的提升性能 通过多线程实现多个操作同时被执行。Default: 5
		 */
		ds.setNumHelperThreads(5);

		return ds;
	}

	public int save(Object obj, NullCndExpr fieldRexpr) {
		Assert.notNull(obj, "ORM.save: obj cannot be null!");

		if (LogUtil.isTraceEnabled()) {

			Class classOfEntity = obj.getClass();
			LogUtil.trace("ORM.save: ...... [obj: %s, fieldRexpr: %s, classOfEntity: %s]",//
			        StringUtil.toString(obj),//
			        fieldRexpr,//
			        classOfEntity.getSimpleName()//
			);

			int result = dao.save(obj, Cnds.fieldRexpr(fieldRexpr), Cnds.isIgloreNull(fieldRexpr));
			// result += dao.deleteRelations(obj, null);
			result += dao.saveLinks(obj, null);

			LogUtil.trace("ORM.save: result = %s [obj: %s, fieldRexpr: %s, classOfEntity: %s]", //
			        result,//
			        StringUtil.toString(obj),//
			        fieldRexpr,//
			        classOfEntity.getSimpleName()//
			);

			return result;
		} else {

			int result = dao.save(obj, Cnds.fieldRexpr(fieldRexpr), Cnds.isIgloreNull(fieldRexpr));
			// result += dao.deleteRelations(obj, null);
			result += dao.saveLinks(obj, null);

			return result;
		}
	}

	public int save(Object obj) {
		Assert.notNull(obj, "ORM.save: obj cannot be null!");

		return this.save(obj, null);
	}

	public int insert(Object obj, NullCndExpr fieldRexpr) {
		Assert.notNull(obj, "ORM.insert: obj cannot be null!");

		if (LogUtil.isTraceEnabled())
			LogUtil.trace("新增%s......[obj: %s, fieldRexpr: %s]", ClassUtil.getDisplayName(obj.getClass()), JsonUtil.toJson(obj), fieldRexpr);

		int result = dao.insert(obj, Cnds.fieldRexpr(fieldRexpr), Cnds.isIgloreNull(fieldRexpr));
		// result += dao.deleteRelations(obj, null);
		result += dao.saveLinks(obj, null);

		if (LogUtil.isDebugEnabled())
			LogUtil.debug("新增%s成功! [result: %s, fieldRexpr: %s]", ClassUtil.getDisplayName(obj.getClass()), result, fieldRexpr);
		if (LogUtil.isTraceEnabled())
			LogUtil.trace(JsonUtil.toJson(obj));

		return result;
	}

	public int insert(Object obj) {
		Assert.notNull(obj, "ORM.insert: obj cannot be null!");

		return this.insert(obj, null);
	}

	public int update(Object obj, NullCndExpr fieldRexpr) {
		Assert.notNull(obj, "ORM.update: obj cannot be null!");

		if (LogUtil.isTraceEnabled())
			LogUtil.trace("修改%s......[obj: %s, fieldRexpr: %s]", ClassUtil.getDisplayName(obj.getClass()), JsonUtil.toJson(obj), fieldRexpr);

		int result = dao.update(obj, Cnds.fieldRexpr(fieldRexpr), Cnds.isIgloreNull(fieldRexpr));
		// result += dao.deleteRelations(obj, null);
		result += dao.saveLinks(obj, null);

		if (LogUtil.isDebugEnabled())
			LogUtil.debug("修改%s成功! [result: %s, fieldRexpr: %s]", ClassUtil.getDisplayName(obj.getClass()), result, fieldRexpr);
		if (LogUtil.isTraceEnabled())
			LogUtil.trace(JsonUtil.toJson(obj));

		return result;
	}

	public int update(Object obj) {
		Assert.notNull(obj, "ORM.update: obj cannot be null!");

		return this.update(obj, null);
	}

	public int updateMore(Object obj, CndExpr expr) {
		Assert.notNull(obj, "ORM.updateMore: obj cannot be null!");

		Condition cnd = Cnds.toCnd(expr);
		if (LogUtil.isTraceEnabled())
			LogUtil.trace("批量修改%s......[obj: %s, expr: %s]", ClassUtil.getDisplayName(obj.getClass()), JsonUtil.toJson(obj), cnd == null ? "" : cnd.toSql(null));

		int result = dao.update(obj, Cnds.fieldRexpr(expr), Cnds.isIgloreNull(expr), cnd);
		result += dao.deleteRelations(obj, null);
		result += dao.saveLinks(obj, null);

		if (LogUtil.isDebugEnabled())
			LogUtil.debug("批量修改%s成功! %s [result: %s]", ClassUtil.getDisplayName(obj.getClass()), cnd == null ? "" : cnd.toSql(null), result);
		if (LogUtil.isTraceEnabled())
			LogUtil.trace(JsonUtil.toJson(obj));

		return result;
	}

	public int delete(Object obj) {
		Assert.notNull(obj, "ORM.delete: obj cannot be null!");

		if (LogUtil.isTraceEnabled()) {

			Class classOfEntity = obj.getClass();
			LogUtil.trace("ORM.delete: ...... [obj: %s, classOfEntity: %s]", //
			        StringUtil.toString(obj),//
			        classOfEntity.getSimpleName()//
			);

			int result = dao.deleteMany(obj, null);
			result += dao.delete(obj);

			LogUtil.trace("ORM.delete: result = %s [obj: %s, classOfEntity: %s]", //
			        result,//
			        StringUtil.toString(obj),//
			        classOfEntity.getSimpleName()//
			);

			return result;
		} else {

			int result = dao.deleteMany(obj, null);
			result += dao.delete(obj);

			return result;
		}

	}

	public int delete(Class classOfEntity, Serializable id) {
		Assert.notNull(classOfEntity, "ORM.delete: classOfEntity cannot be null!");

		return this.delete(this.get(classOfEntity, id));
	}

	public int delete(Class classOfEntity, Long id) {
		Assert.notNull(classOfEntity, "ORM.delete: classOfEntity cannot be null!");

		return this.delete(this.get(classOfEntity, id));
	}

	public int delete(Class classOfEntity, String key) {
		Assert.notNull(classOfEntity, "ORM.delete: classOfEntity cannot be null!");

		return this.delete(this.get(classOfEntity, key));
	}

	public int clear(Class classOfEntity, CndExpr expr) {
		Assert.notNull(classOfEntity, "ORM.clear: classOfEntity cannot be null!");

		Condition cnd = Cnds.toCnd(expr);
		if (LogUtil.isDebugEnabled()) {
			LogUtil.trace("ORM.clear: ...... [classOfEntity: %s]", //
			        classOfEntity.getSimpleName()//
			);

			int result = dao.clear(classOfEntity, cnd);

			LogUtil.trace("ORM.clear: result = %s [classOfEntity: %s]", //
			        result,//
			        classOfEntity.getSimpleName()//
			);

			return result;
		} else
			return dao.clear(classOfEntity);
	}

	public int delete(Class classOfEntity, CndExpr expr) {
		Assert.notNull(classOfEntity, "ORM.delete: classOfEntity cannot be null!");

		Condition cnd = Cnds.toCnd(expr);
		if (LogUtil.isDebugEnabled()) {

			Entity entity = (Entity) this.getEnMapping(classOfEntity);
			LogUtil.trace("ORM.delete: ...... [classOfEntity: %s, expr: %s, sql: %s]",//
			        classOfEntity.getSimpleName(),//
			        expr,//
			        cnd == null ? "" : cnd.toSql(entity)//
			);

			int result = dao.clear(classOfEntity, cnd);

			LogUtil.trace("ORM.delete: result = %s [classOfEntity: %s, expr: %s, sql: %s]", //
			        result,//
			        classOfEntity.getSimpleName(),//
			        expr,//
			        cnd == null ? "" : cnd.toSql(entity)//
			);

			return result;
		} else
			return dao.clear(classOfEntity, cnd);
	}

	public Object get(Class classOfEntity, Serializable id) {
		Assert.notNull(classOfEntity, "ORM.get: classOfEntity cannot be null!");

		return get(classOfEntity, id, null);
	}

	public Object get(Class classOfEntity, Long id) {
		Assert.notNull(classOfEntity, "ORM.get: classOfEntity cannot be null!");

		return dao.fetch(classOfEntity, null, id);
	}

	public Object get(Class classOfEntity, String key) {
		Assert.notNull(classOfEntity, "ORM.get: classOfEntity cannot be null!");

		return load(classOfEntity, Expr.eq(Const.F_KEY, key));
	}

	public Object get(Class classOfEntity, Serializable id, CndExpr fieldRexpr) {
		Assert.notNull(classOfEntity, "ORM.get: classOfEntity cannot be null!");

		if (LogUtil.isDebugEnabled()) {

			LogUtil.trace("ORM.get: ...... [classOfEntity: %s, id: %s, fieldRexpr: %s]",//
			        classOfEntity.getSimpleName(),//
			        id,//
			        fieldRexpr//
			);

			Object result = dao.fetch(classOfEntity, Cnds.fieldRexpr(fieldRexpr), Long.parseLong(id.toString()));

			LogUtil.trace("ORM.get: result = %s [classOfEntity: %s, id: %s, fieldRexpr: %s]",//
			        StringUtil.toString(result),//
			        classOfEntity.getSimpleName(),//
			        id,//
			        fieldRexpr//
			);

			return result;
		} else {
			return dao.fetch(classOfEntity, Cnds.fieldRexpr(fieldRexpr), Long.parseLong(id.toString()));
		}
	}

	public Object load(Class classOfEntity) {
		Assert.notNull(classOfEntity, "ORM.load: classOfEntity cannot be null!");

		return this.load(classOfEntity, (CndExpr) null);
	}

	public Object load(Class classOfEntity, CndExpr expr) {
		Assert.notNull(classOfEntity, "ORM.load: classOfEntity cannot be null!");

		Condition cnd = Cnds.toCnd(expr);
		if (LogUtil.isDebugEnabled()) {

			Entity entity = (Entity) this.getEnMapping(classOfEntity);
			LogUtil.trace("ORM.get: ...... [classOfEntity: %s, expr: %s, sql: %s]",//
			        classOfEntity.getSimpleName(),//
			        expr,//
			        cnd == null ? "" : cnd.toSql(entity)//
			);

			Object result = dao.fetch(classOfEntity, cnd);

			LogUtil.trace("ORM.get: result = %s [classOfEntity: %s, expr: %s, sql: %s]",//
			        StringUtil.toString(result),//
			        classOfEntity.getSimpleName(),//
			        expr,//
			        cnd == null ? "" : cnd.toSql(entity)//
			);

			return result;
		} else {
			return dao.fetch(classOfEntity, cnd);
		}
	}

	public List query(Class classOfEntity) {
		Assert.notNull(classOfEntity, "ORM.query: classOfEntity cannot be null!");

		return query(classOfEntity, null);
	}

	public List query(Class classOfEntity, CndExpr expr) {
		// if (IWebCategory.class.isAssignableFrom(classOfEntity)) {
		// return new ArrayList();
		// }

		Assert.notNull(classOfEntity, "ORM.query: classOfEntity cannot be null!");

		Condition cnd = Cnds.toCnd(expr);
		if (LogUtil.isDebugEnabled()) {

			Entity entity = (Entity) this.getEnMapping(classOfEntity);
			LogUtil.trace("ORM.query: ...... [classOfEntity: %s, expr: %s, sql: %s]",//
			        classOfEntity.getSimpleName(),//
			        expr, //
			        cnd == null ? "" : cnd.toSql(entity)//
			);

			List result = dao.query(classOfEntity, Cnds.fieldRexpr(expr), cnd, Cnds.toPager(dao, expr));

			LogUtil.trace("ORM.query: result = %s [classOfEntity: %s, expr: %s, sql: %s]",//
			        StringUtil.toString(result),//
			        classOfEntity.getSimpleName(),//
			        expr, //
			        cnd == null ? "" : cnd.toSql(entity)//
			);

			return result;
		} else {
			return dao.query(classOfEntity, Cnds.fieldRexpr(expr), cnd, Cnds.toPager(dao, expr));
		}

	}

	public int count(Class classOfEntity) {
		Assert.notNull(classOfEntity, "ORM.count: classOfEntity cannot be null!");

		return this.count(classOfEntity, null);
	}

	public int count(Class classOfEntity, CndExpr expr) {
		Assert.notNull(classOfEntity, "ORM.count: classOfEntity cannot be null!");

		Condition cnd = Cnds.toCnd(expr);
		if (LogUtil.isDebugEnabled()) {
			Entity entity = (Entity) this.getEnMapping(classOfEntity);
			LogUtil.trace("ORM.count: ...... [classOfEntity: %s, expr: %s, sql: %s]",//
			        classOfEntity.getSimpleName(),//
			        expr,//
			        cnd == null ? "" : cnd.toSql(entity)//
			);

			int result = dao.count(classOfEntity, cnd);

			LogUtil.trace("ORM.count: result = %s [classOfEntity: %s, expr: %s, sql: %s]",//
			        result,//
			        classOfEntity.getSimpleName(),//
			        expr,//
			        cnd == null ? "" : cnd.toSql(entity)//
			);

			return result;
		} else {
			return dao.count(classOfEntity, cnd);
		}
	}

	public Object run(final ConnCallback call) {
		return dao.run(call);
	}

	public String getIdProperty(Class klass) {
		return dao.getEnMapping(klass).getIdProperty();
	}

	public EnMapping getEnMapping(Class classOfT) {
		return this.getEnMapping(classOfT, true, true);
	}

	public EnMapping getEnMapping(Class classOfT, boolean syncTable) {
		return this.getEnMapping(classOfT, syncTable, false);
	}

	public EnMapping getEnMapping(Class classOfEntity, boolean syncTable, boolean syncRefTable) {
		if (LogUtil.isTraceEnabled()) {
			LogUtil.trace("ORM.getEnMapping: ...... [classOfEntity: %s, syncTable: %s, syncRefTable: %s]",//
			        classOfEntity.getSimpleName(),//
			        syncTable,//
			        syncRefTable//
			);

			EnMapping result = dao.getEnMapping(classOfEntity, syncTable, syncRefTable);

			LogUtil.trace("ORM.getEnMapping: result = %s [classOfEntity: %s, syncTable: %s, syncRefTable: %s]",//
			        result,//
			        classOfEntity.getSimpleName(),//
			        syncTable,//
			        syncRefTable//
			);

			return result;
		} else {
			return dao.getEnMapping(classOfEntity, syncTable, syncRefTable);
		}
	}

	public Dialect getDialect() {
		return dao.getDialect();
	}

	public INamingStrategy getNamingStrategy() {
		return dao.getNamingStrategy();
	}

	public ExtDao getDao() {
		return dao;
	}

	public DMLSession getDMLSession() {
		return dao.getDMLSession();
	}

	public void removeMapping(Class cls) {
		dao.getEntityHolder().remove(cls);
	}

	public void clearMapping() {
		dao.getEntityHolder().clear();
	}

	public Object get(Class classOfEntity, CndExpr expr) {
		if (classOfEntity == null)
			return 0;

		if (expr == null) {
			expr = Expr.page(1, 1);
		} else if (expr.getPagerExpr() == null) {
			expr = expr.setPager(1, 1);
		}

		List list = this.query(classOfEntity, expr);

		return (list == null || list.size() == 0) ? null : list.get(0);
	}

	public List query(Pager pager) {
		Assert.notNull(pager, "ORM.query: pager cannot be null!");

		CndExpr expr = pager.getQueryExpr();
		String fieldRexpr = Cnds.fieldRexpr(expr);
		Class klass = pager.getType();

		Condition cnd = Cnds.toCnd(expr);

		if (LogUtil.isTraceEnabled())
			LogUtil.trace("ORM.query: %s......[expr: %s, fieldRexpr: %s]", ClassUtil.getDisplayName(klass), cnd == null ? "" : cnd.toSql(null), fieldRexpr);

		int totalRecord = dao.count(klass, Cnds.cnd(expr));
		pager.setTotalRecord(totalRecord);
		pager.setResult(dao.query(klass, fieldRexpr, cnd, Cnds.toPager(dao, expr)));

		if (LogUtil.isDebugEnabled())
			LogUtil.debug("分页查询%s成功! %s [fieldRexpr: %s, totalRecord: %s, pageRecord: %s]", ClassUtil.getDisplayName(klass), cnd == null ? "" : cnd.toSql(null), fieldRexpr, totalRecord, pager.getResult().size());

		return pager.getResult();
	}

	@Override
	public <T> List<T> query(final Class<T> typeOfEntity, final String sql, final List sqlParams, final Map<String, String> columnToPropMap) {

		return (List<T>) this.dao.run(new ConnCallback() {
			@Override
			public Object invoke(Connection conn) throws Exception {

				PreparedStatement stmt = null;
				ResultSet rst = null;

				try {
					stmt = conn.prepareStatement(sql);

					/*
					 * 设置查询参数
					 */
					int parameterIndex = 0;
					for (Object param : sqlParams) {
						parameterIndex++;
						if (param instanceof Integer) {
							stmt.setInt(parameterIndex, (Integer) param);
						} else if (param instanceof Byte) {
							stmt.setByte(parameterIndex, (Byte) param);
						} else if (param instanceof Short) {
							stmt.setShort(parameterIndex, (Short) param);
						} else if (param instanceof Long) {
							stmt.setLong(parameterIndex, (Long) param);
						} else if (param instanceof Float) {
							stmt.setFloat(parameterIndex, (Float) param);
						} else if (param instanceof Double) {
							stmt.setDouble(parameterIndex, (Double) param);
						} else if (param instanceof Date) {
							stmt.setDate(parameterIndex, (Date) param);
						} else if (param instanceof Boolean) {
							stmt.setBoolean(parameterIndex, (Boolean) param);
						} else {
							stmt.setString(parameterIndex, param.toString());
						}
					}

					/*
					 * 执行查询结果
					 */
					rst = stmt.executeQuery();
					ResultSetMetaData metadata = rst.getMetaData();
					int size = metadata.getColumnCount();
					List<String> columnNames = new ArrayList();
					for (int i = 1; i <= size; i++) {
						columnNames.add(metadata.getColumnLabel(i));
					}

					/*
					 * 创建List对象
					 */
					List ret = new ArrayList();
					if (Map.class.isAssignableFrom(typeOfEntity)) {
						Map obj;
						while (rst.next()) {
							obj = new HashMap();
							for (String column : columnNames) {
								String prop = columnToPropMap.get(column);
								if (prop == null || prop.trim().length() == 0) {
									prop = column;
								}

								try {
									obj.put(prop, rst.getObject(column));
								} catch (Throwable e) {
									LogUtil.warn("转换SQL查询结果到Map对象失败！", ExceptionUtil.msg(e));
								}
							}

							ret.add(obj);
						}
					} else {
						Mirror me = Mirror.me(typeOfEntity);
						Object obj;
						while (rst.next()) {
							obj = me.born();

							for (String column : columnNames) {
								String prop = columnToPropMap.get(column);
								if (prop == null || prop.trim().length() == 0) {
									prop = column;
								}

								try {
									Object value = rst.getObject(column);
									me.setValue(obj, prop, value);
								} catch (Throwable e) {
									LogUtil.warn("转换SQL查询结果到对象失败：字段不存在(%s.%s)！", typeOfEntity.getSimpleName(), prop);
								}
							}

							ret.add(obj);
						}
					}

					return ret;
				} finally {
					try {
						if (rst != null)
							rst.close();
					} catch (Throwable e) {

					}
					try {
						if (stmt != null)
							stmt.close();
					} catch (Throwable e) {

					}
				}
			}

		});
	}

	@Override
	public int execSql(final String sql, final List sqlParams, final Map<String, String> columnToPropMap) {

		return (Integer) this.dao.run(new ConnCallback() {
			@Override
			public Object invoke(Connection conn) throws Exception {

				PreparedStatement stmt = null;
				ResultSet rst = null;

				try {
					stmt = conn.prepareStatement(sql);

					/*
					 * 设置查询参数
					 */
					int parameterIndex = 0;
					for (Object param : sqlParams) {
						parameterIndex++;
						if (param instanceof Integer) {
							stmt.setInt(parameterIndex, (Integer) param);
						} else if (param instanceof Byte) {
							stmt.setByte(parameterIndex, (Byte) param);
						} else if (param instanceof Short) {
							stmt.setShort(parameterIndex, (Short) param);
						} else if (param instanceof Long) {
							stmt.setLong(parameterIndex, (Long) param);
						} else if (param instanceof Float) {
							stmt.setFloat(parameterIndex, (Float) param);
						} else if (param instanceof Double) {
							stmt.setDouble(parameterIndex, (Double) param);
						} else if (param instanceof Date) {
							stmt.setDate(parameterIndex, (Date) param);
						} else if (param instanceof Boolean) {
							stmt.setBoolean(parameterIndex, (Boolean) param);
						} else {
							stmt.setString(parameterIndex, param.toString());
						}
					}

					/*
					 * 执行查询结果
					 */
					rst = stmt.executeQuery();

					if (rst.next())
						return rst.getInt(1);

					return 0;
				} finally {
					try {
						if (rst != null)
							rst.close();
					} catch (Throwable e) {

					}
					try {
						if (stmt != null)
							stmt.close();
					} catch (Throwable e) {

					}
				}
			}

		});
	}
}