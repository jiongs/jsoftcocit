package com.jsoft.cocit.entityengine.service;

import java.util.List;
import java.util.Map;

import com.jsoft.cocit.entity.coc.ICocAction;
import com.jsoft.cocit.entityengine.bizplugin.IBizPlugin;
import com.jsoft.cocit.orm.expr.CndExpr;

/**
 * 操作服务类：为每个操作提供一对一的服务。
 * 
 * 
 * @author jiongs753
 * 
 */
public interface CocActionService extends NamedEntityService<ICocAction>, ICocAction {
	CocEntityService getEntity();

	IBizPlugin[] getBizPlugins();

	Map getDefaultValues();

	Map getAssignValues();

	CndExpr getWhere();

	List<String> getProxyActionsList();
}
