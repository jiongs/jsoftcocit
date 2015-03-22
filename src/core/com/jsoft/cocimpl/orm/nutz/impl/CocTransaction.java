package com.jsoft.cocimpl.orm.nutz.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.nutz.lang.ComboException;
import org.nutz.trans.Transaction;

import com.jsoft.cocit.util.LogUtil;

public class CocTransaction extends Transaction {

	private static int ID = 0;

	private List<Pair> list;

	private static class Pair {
		Pair(DataSource ds, Connection conn, int level) throws SQLException {
			this.ds = ds;
			this.conn = conn;
			oldLevel = conn.getTransactionIsolation();
			if (oldLevel != level)
				conn.setTransactionIsolation(level);
		}

		DataSource ds;

		Connection conn;

		int oldLevel;
	}

	public CocTransaction() {
		list = new ArrayList<Pair>();
	}

	protected void commit() {
		LogUtil.debug("DAO.Transaction: commit...");
		ComboException ce = new ComboException();
		for (Pair p : list) {
			try {
				// 提交事务
				p.conn.commit();
				// 恢复旧的事务级别
				if (p.conn.getTransactionIsolation() != p.oldLevel)
					p.conn.setTransactionIsolation(p.oldLevel);
			} catch (SQLException e) {
				ce.add(e);
			}
		}
		// 如果有一个数据源提交时发生异常，抛出
		if (null != ce.getCause()) {
			throw ce;
		}
	}

	public Connection getConnection(DataSource dataSource) throws SQLException {
		LogUtil.debug("DAO.Transaction: getConnection...");
		for (Pair p : list)
			if (p.ds == dataSource)
				return p.conn;

		Connection conn = dataSource.getConnection();
		// System.out.printf("=> %s\n", conn.toString());
		if (conn.getAutoCommit())
			conn.setAutoCommit(false);
		// Store conn, it will set the trans level
		list.add(new Pair(dataSource, conn, getLevel()));

		return conn;
	}

	public int getId() {
		return ID++;
	}

	public void close() {
		LogUtil.debug("DAO.Transaction:close...");
		ComboException ce = new ComboException();
		for (Pair p : list) {
			try {
				// 试图恢复旧的事务级别
				if (!p.conn.isClosed())
					if (p.conn.getTransactionIsolation() != p.oldLevel)
						p.conn.setTransactionIsolation(p.oldLevel);
			} catch (Throwable e) {
			} finally {
				try {
					p.conn.close();
				} catch (Exception e) {
					ce.add(e);
				}
			}
		}
		// 清除数据源记录
		list.clear();
	}

	protected void rollback() {
		LogUtil.debug("DAO.Transaction: rollback...");
		for (Pair p : list) {
			try {
				p.conn.rollback();
			} catch (Throwable e) {
			}
		}
	}

}
