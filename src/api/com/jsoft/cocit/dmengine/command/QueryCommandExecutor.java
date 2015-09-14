package com.jsoft.cocit.dmengine.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.PageResult;

public abstract class QueryCommandExecutor extends WebCommandInterceptor {

	@Override
	protected boolean execute(WebCommandContext command) {
		/*
		 * 获取服务对象
		 */
		IOrm orm = command.getOrm();
		ICocEntityInfo cocEntity = command.getCocEntity();

		/*
		 * 获取实体类
		 */
		Class typeOfEntity = cocEntity.getClassOfEntity();

		/*
		 * 获取SQL查询语句
		 */
		String sql = getSql(command);

		/*
		 * 获取SQL查询参数
		 */
		List sqlParams = getSqlParams(command);

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

		List result = orm.query(typeOfEntity, sql, sqlParams, columnToPropMap);
		pager.setResult(result);

		int totalRecord = orm.execSql(sql, sqlParams);
		pager.setTotalRecord(totalRecord);

		return true;
	}

	protected abstract String getSql(WebCommandContext command);

	protected abstract List getSqlParams(WebCommandContext command);

}
