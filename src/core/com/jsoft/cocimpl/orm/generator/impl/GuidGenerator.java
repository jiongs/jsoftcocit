package com.jsoft.cocimpl.orm.generator.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.orm.ExtDao;
import com.jsoft.cocit.orm.NoTransConnCallback;
import com.jsoft.cocit.orm.generator.Generator;
import com.jsoft.cocit.orm.mapping.EnColumnMapping;
import com.jsoft.cocit.orm.mapping.EnMapping;

public class GuidGenerator implements Generator {

	private Map<ExtDao, String> sqls = new HashMap();

	public String getName() {
		return "guid";
	}

	public Serializable generate(final ExtDao dao, EnMapping entity, EnColumnMapping column, final Object dataObject, String... params) {
		return (Serializable) dao.run(new NoTransConnCallback() {
			public Object invoke(Connection conn) throws Exception {
				return generate(dao, conn, dataObject);
			}
		});
	}

	private String sql(ExtDao dao) {
		if (sqls.get(dao) == null) {
			sqls.put(dao, dao.getDialect().getSelectKEYString());
		}
		return sqls.get(dao);
	}

	private Serializable generate(ExtDao dao, Connection conn, Object object) throws SQLException {
		try {
			PreparedStatement st = conn.prepareStatement(sql(dao));
			try {
				ResultSet rs = st.executeQuery();
				final String result;
				try {
					rs.next();
					result = rs.getString(1);
				} finally {
					rs.close();
				}
				return result;
			} finally {
				st.close();
			}
		} catch (SQLException sqle) {
			throw sqle;
		}
	}

}
