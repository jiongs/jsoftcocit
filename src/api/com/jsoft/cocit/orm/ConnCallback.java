package com.jsoft.cocit.orm;

import java.sql.Connection;

public interface ConnCallback {

	Object invoke(Connection conn) throws Exception;

}
