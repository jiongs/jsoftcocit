package com.jsoft.cocit.dmengine.info;

import java.util.List;
import java.util.Map;

import com.jsoft.cocit.baseentity.coc.ICocActionEntity;
import com.jsoft.cocit.dmengine.bizplugin.IBizPlugin;
import com.jsoft.cocit.orm.expr.CndExpr;

/**
 * 操作服务类：为每个操作提供一对一的服务。
 * 
 * 
 * @author jiongs753
 * 
 */
public interface ICocActionInfo extends INamedEntityInfo<ICocActionEntity>, ICocActionEntity {
	ICocEntityInfo getEntity();

	IBizPlugin[] getBizPlugins();

	Map getDefaultValues();

	Map getAssignValues();

	CndExpr getWhere();

	List<String> getProxyActionsList();
}
