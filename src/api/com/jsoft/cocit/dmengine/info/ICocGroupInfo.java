package com.jsoft.cocit.dmengine.info;

import java.util.List;

import com.jsoft.cocit.baseentity.coc.ICocGroupEntity;

/**
 * 分组服务类：为字段分组提供一对一的服务。
 * 
 * <UL>
 * <LI>代表一个运行时的自定义数据分组，通常由定义在数据库中的数据实体解析而来；
 * <LI>与数据表的关系：每个数据分组只能隶属于一个数据表；
 * <LI>与数据字段的关系：每个数据分组可以包含多个数据字段；
 * </UL>
 * 
 * @author jiongs753
 * 
 */
public interface ICocGroupInfo extends INamedEntityInfo<ICocGroupEntity>, ICocGroupEntity {

	ICocEntityInfo getEntity();

	List<ICocFieldInfo> getFields();

	String getModeByAction(String actionID);

}
