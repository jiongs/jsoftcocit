package com.jsoft.cocimpl.log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jsoft.cocimpl.orm.dialect.Dialect;
import com.jsoft.cocimpl.orm.generator.INamingStrategy;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.entity.log.IOperationLog;
import com.jsoft.cocit.orm.ExtOrm;
import com.jsoft.cocit.orm.NoTransConnCallback;

public class OperationLogUtil {

	private static OperationLogUtil me;

	public static OperationLogUtil get() {
		synchronized (OperationLogUtil.class) {
			if (me == null) {
				try {
					me = new OperationLogUtil();
				} catch (Throwable e) {
				}
			}
		}
		return me;
	}

	private OperationLogUtil() {
	}

	private ExtOrm orm() {
		return (ExtOrm) Cocit.me().orm();
	}

	public synchronized void save(final IOperationLog log) throws SQLException {

		final ExtOrm orm = orm();

		if (orm != null)
			orm.run(new NoTransConnCallback() {
				public Object invoke(Connection conn) throws Exception {

					Dialect dialect = orm.getDialect();
					INamingStrategy naming = orm.getNamingStrategy();
					String pk = naming.columnName("id_");
					String tableName = naming.tableNameFromClassName(EntityTypes.OperationLog.getSimpleName());
					String insertSql = sqlInsertStrings(dialect, naming, tableName, pk);
					String maxIdSql = "SELECT MAX(" + pk + ") FROM " + tableName;

					Statement stat = null;
					ResultSet rs = null;
					long maxId = 0;
					try {
						stat = conn.createStatement();
						rs = stat.executeQuery(maxIdSql);
						if (rs.next())
							maxId = rs.getLong(1);
					} catch (SQLException e) {
						System.err.println("执行SQL<" + maxIdSql + ">出错! 错误信息： " + e);
					} finally {
						if (null != stat)
							try {
								stat.close();
							} catch (Throwable e) {
							}
						if (null != rs)
							try {
								rs.close();
							} catch (Throwable e) {
							}
					}
					PreparedStatement stmt = conn.prepareStatement(insertSql);
					try {
						stmt.setLong(1, maxId + 1);

						return stmt.execute();
					} catch (SQLException sqle) {
						System.err.println("执行SQL<" + insertSql + ">出错! 错误信息： " + sqle);
						return null;
					} finally {
						if (null != stmt)
							try {
								stmt.close();
							} catch (Throwable e) {
							}
					}
				}
			});
	}

	private String sqlInsertStrings(Dialect dialect, INamingStrategy naming, String tableName, String pk) {
		StringBuffer sb = new StringBuffer().append("insert into ").append(tableName).append("(");
		sb.append(pk);
		sb.append(",").append(naming.propertyToColumnName("loginuser"));
		sb.append(",").append(naming.propertyToColumnName("fqnofctgrcls"));
		sb.append(",").append(naming.propertyToColumnName("loggername"));
		sb.append(",").append(naming.propertyToColumnName("datetime"));
		sb.append(",").append(naming.propertyToColumnName("level"));
		sb.append(",").append(naming.propertyToColumnName("message"));
		sb.append(",").append(naming.propertyToColumnName("threadname"));
		sb.append(",").append(naming.propertyToColumnName("stacktrace"));
		sb.append(",").append(naming.propertyToColumnName("ndc"));
		sb.append(",").append(naming.propertyToColumnName("locationinfo"));
		sb.append(",").append(naming.propertyToColumnName("remoteUrl"));
		sb.append(",").append(naming.propertyToColumnName("remoteUri"));
		sb.append(",").append(naming.propertyToColumnName("remoteIp"));
		sb.append(",").append(naming.propertyToColumnName("memEslipse"));
		sb.append(",").append(naming.propertyToColumnName("eslipse"));
		sb.append(",").append(naming.propertyToColumnName("monitor"));
		sb.append(",").append(naming.propertyToColumnName("remoteAddress"));
		sb.append(") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		return sb.toString();
	}
	//
	// private String sqlCreateStrings(Dialect dialect, INamingStrategy naming,
	// String tableName, String pk) throws DemsyException {
	// StringBuffer sb = new
	// StringBuffer().append(dialect.getCreateTableString()).append(' ').append(tableName).append("(");
	// sb.append(pk).append(' ');
	// sb.append(dialect.getTypeName(Types.BIGINT, 0, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("loginuser")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 50, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("fqnofctgrcls")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("loggername")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("datetime")).append(' ');
	// sb.append(dialect.getTypeName(Types.DATE, 255, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("level")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 20, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("content")).append(' ');
	// sb.append(dialect.getTypeName(Types.CLOB, 0, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("threadname")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("stacktrace")).append(' ');
	// sb.append(dialect.getTypeName(Types.CLOB, 0, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("ndc")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	//
	// sb.append(",").append(naming.propertyToColumnName("locationinfo")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	// sb.append(",").append(naming.propertyToColumnName("remoteUrl")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	// sb.append(",").append(naming.propertyToColumnName("remoteUri")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 255, 0, 0));
	// sb.append(",").append(naming.propertyToColumnName("remoteIp")).append(' ');
	// sb.append(dialect.getTypeName(Types.VARCHAR, 64, 0, 0));
	//
	// sb.append(", primary key ( ").append(naming.columnName("_id")).append(" ) ) ");
	//
	// return sb.toString();
	// }
}
