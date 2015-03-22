package com.jsoft.cocit.entity.security;

import com.jsoft.cocit.entity.INamedEntity;

/**
 * 系统实体接口：平台中可以包含多套系统，如“针对药监局的《药品监管系统》、《食品监管系统》，针对药品企业的《药品管理系统》，针对食品企业的《食品管理系统》”等。
 * <OL>
 * <LI>租户拥有者禁用【{@link #isTenantOwnerDisabled()}】：true——禁用租户拥有者，表示使用该系统的用户不受租户（单位、企业）的限制，可以查看所有租户（单位、企业）的数据。false——启用租户拥有者，表示使用该系统的人受租户的限制，只能查看自己单位（本租户）的数据。
 * </OL>
 * 
 * @author yongshan.ji
 * @preserve all
 * 
 */
public interface ISystem extends INamedEntity {
}
