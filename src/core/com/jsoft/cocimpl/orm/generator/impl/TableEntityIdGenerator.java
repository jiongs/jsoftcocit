package com.jsoft.cocimpl.orm.generator.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.jsoft.cocimpl.orm.dialect.Dialect;
import com.jsoft.cocimpl.orm.generator.EntityIdGenerator;
import com.jsoft.cocimpl.orm.nutz.EnMappingImpl;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.ExtDao;
import com.jsoft.cocit.orm.NoTransConnCallback;
import com.jsoft.cocit.orm.mapping.EnColumnMapping;
import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.util.LogUtil;

public class TableEntityIdGenerator implements EntityIdGenerator<EnMapping> {
	private String tableName;

	private String pkColumnName;

	private int pkColumnLength = 255;

	private String valueColumnName;

	private int initialValue = 1;

	private String selectQuery;

	private String insertQuery;

	private String updateQuery;

	// private long accessCount = 0;

	public TableEntityIdGenerator(final String tableName, String pkColumnName, String valueColumnName) {
		this.tableName = tableName;
		this.pkColumnName = pkColumnName;
		this.valueColumnName = valueColumnName;
		this.selectQuery = buildSelectQuery();
		this.updateQuery = buildUpdateQuery();
		this.insertQuery = buildInsertQuery();
	}

	// 检查 ID 生成器
	private void checkTable(final ExtDao dao) {
		try {
			dao.run(new NoTransConnCallback() {
				public Object invoke(Connection conn) throws Exception {
					Statement stat = null;
					ResultSet rs = null;
					try {
						stat = conn.createStatement();
						String sql = "SELECT COUNT(*) FROM " + tableName + " where 1!=1";
						rs = stat.executeQuery(sql);
						if (rs.next())
							return rs.getObject(1);
					} catch (SQLException e) {
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
					Statement stmt = conn.createStatement();
					try {
						stmt.execute(sqlCreateStrings(dao.getDialect()).toString());
					} catch (Exception sqle) {
					} finally {
						if (null != stat)
							try {
								stat.close();
							} catch (Throwable e) {
							}
					}
					return null;
				}
			});

		} catch (Exception e) {
			LogUtil.error("检查ID表出错! %s", e);
		}
	}

	protected String buildSelectQuery() {
		return "select " + valueColumnName + " from " + tableName + " where " + pkColumnName + "=?";
	}

	protected String buildUpdateQuery() {
		return "update " + tableName + " set " + valueColumnName + "=? " + " where " + valueColumnName + "=? and " + pkColumnName + "=?";
	}

	protected String buildInsertQuery() {
		return "insert into " + tableName + " (" + pkColumnName + ", " + valueColumnName + ") " + " values (?,?)";
	}

	public Serializable generate(ExtDao dao, final EnMapping entity, EnColumnMapping column, Object dataObject, String... params) {
		final String columnName = ((EnMappingImpl) entity).getField(entity.getIdProperty()).getColumnName();
		try {
			return (Serializable) dao.run(new NoTransConnCallback() {
				public Object invoke(Connection conn) throws Exception {
					return generate(conn, entity.getTableName(), columnName);
				}
			});
		} catch (Throwable e) {
			checkTable(dao);
			return (Serializable) dao.run(new NoTransConnCallback() {
				public Object invoke(Connection conn) throws Exception {
					return generate(conn, entity.getTableName(), columnName);
				}
			});
		}
	}

	private Serializable generate(Connection conn, String segmentValue, String idField) throws SQLException {
		int maxValue = initialValue;
		int oldValue = initialValue;
		int result;
		int rows;
		int count = 0;
		do {
			count++;
			PreparedStatement maxPS = null;
			ResultSet maxRS = null;
			try {
				maxPS = conn.prepareStatement("select max(" + idField + ") from " + segmentValue);
				maxRS = maxPS.executeQuery();
				if (maxRS.next()) {
					maxValue = maxRS.getInt(1) + 1;
				}
			} catch (Throwable e) {
				LogUtil.error("查询最大值出错! [table=%s, field=%s] %s", segmentValue, idField, e);
			} finally {
				if (maxRS != null)
					maxRS.close();
				if (maxPS != null)
					maxPS.close();
			}

			PreparedStatement selectPS = conn.prepareStatement(selectQuery);
			try {
				selectPS.setString(1, segmentValue);
				ResultSet selectRS = selectPS.executeQuery();
				if (!selectRS.next()) {
					PreparedStatement insertPS = null;

					try {
						result = maxValue;
						insertPS = conn.prepareStatement(insertQuery);
						insertPS.setString(1, segmentValue);
						insertPS.setLong(2, result);
						insertPS.execute();
					} finally {
						if (insertPS != null) {
							insertPS.close();
						}
					}
				} else {
					result = oldValue = selectRS.getInt(1);
				}
				if (maxValue == 1) {
					result = 1;
				} else {
					result = Math.max(result, maxValue);
				}
				selectRS.close();
			} catch (SQLException sqle) {
				throw sqle;
			} finally {
				selectPS.close();
			}

			PreparedStatement updatePS = conn.prepareStatement(updateQuery);
			try {
				long newValue = result + 1;
				updatePS.setLong(1, newValue);
				updatePS.setLong(2, oldValue);
				updatePS.setString(3, segmentValue);
				rows = updatePS.executeUpdate();
			} catch (SQLException sqle) {
				throw sqle;
			} finally {
				updatePS.close();
			}

			if (count > 10) {
				throw new SQLException("自动生成字段ID值失败！");
			}
		} while (rows == 0);

		// accessCount++;

		return new Long(result);
	}

	public String sqlCreateStrings(Dialect dialect) throws CocException {
		return new StringBuffer().append(dialect.getCreateTableString()).append(' ').append(tableName).append(" ( ").append(pkColumnName).append(' ').append(dialect.getTypeName(Types.VARCHAR, pkColumnLength, 0, 0)).append(",  ")
		        .append(valueColumnName).append(' ').append(dialect.getTypeName(Types.BIGINT)).append(", primary key ( ").append(pkColumnName).append(" ) ) ").toString();
	}

	public String getTableName() {
		return tableName;
	}

	@Override
	public String getName() {
		return "id";
	}
}
