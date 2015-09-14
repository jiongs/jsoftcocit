package com.jsoft.cocimpl.dmengine.impl.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.constant.CommandNames;
import com.jsoft.cocit.dmengine.command.WebCommandContext;
import com.jsoft.cocit.dmengine.command.WebCommandInterceptor;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.dmengine.info.ICuiEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiGridInfo;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.PageResult;

public class SqlQueryExecutor extends WebCommandInterceptor {

	@Override
	public String getName() {
		return "";
	}

	@Override
	protected boolean execute(WebCommandContext command) {
		/*
		 * 获取服务对象
		 */
		IOrm orm = command.getOrm();
		ICocEntityInfo cocEntity = command.getCocEntity();
		ICuiEntityInfo cuiEntity = cocEntity.getCuiEntity(null);
		ICuiGridInfo cuiGrid = cuiEntity.getCuiGrid();

		/*
		 * 获取实体类
		 */
		Class typeOfEntity = cocEntity.getClassOfEntity();

		/*
		 * 解析SQL语句
		 */
		// String sql = cuiGrid.getSQL();

		/*
		 * 获取SQL查询参数
		 */
		// List<String> sqlParamNames = cuiGrid.getSQLParams();
		List sqlParams = new ArrayList();
		// if (sqlParamNames != null) {
		// Map params = new HashMap();
		// Map expr = command.getQueryJsonExpr();
		// Iterator<String> paramNames = expr.keySet().iterator();
		// while (paramNames.hasNext()) {
		// String paramName = paramNames.next();
		// String propName = paramName;
		// int idx = paramName.indexOf(" ");
		// if (idx > -1) {
		// propName = paramName.substring(0, idx);
		// }
		// List values = (List) expr.get(paramName);
		// if (values != null) {
		// if (values.size() == 1)
		// params.put(propName, values.get(0));
		// else
		// params.put(propName, values);
		// }
		// }
		// for (String param : sqlParamNames) {
		// sqlParams.add(params.get(param));
		// }
		// }

		/*
		 * 计算字段映射
		 */
		Map<String, String> columnToPropMap = new HashMap();
		List<ICocFieldInfo> fields = cocEntity.getFields();
		for (ICocFieldInfo column : fields) {
			columnToPropMap.put(column.getDbColumnName(), column.getFieldName());
		}

		/*
		 * 创建返回值
		 */
		PageResult pager = new PageResult();

		// List result = orm.query(typeOfEntity, sql, sqlParams, columnToPropMap);
		// pager.setResult(result);
		//
		// int totalRecord = orm.execSql(sql, sqlParams);
		// pager.setTotalRecord(totalRecord);

		return true;
	}
}
