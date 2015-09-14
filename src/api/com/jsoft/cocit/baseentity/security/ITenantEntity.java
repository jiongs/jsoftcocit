package com.jsoft.cocit.baseentity.security;

import com.jsoft.cocit.baseentity.INamedEntity;
import com.jsoft.cocit.baseentity.config.IDataSourceEntity;

/**
 * 【租户】接口：该接口是【{@link INamedEntity}】接口的子接口，实现该接口的实体类映射表用来存储企业级用户（系统租户）信息，如“食品药品管理局、各食品企业、各药品企业等”。
 * <OL>
 * <LI>系统KEY【{@link #getSystemCode()}】：逻辑外键，关联到【{@link ISystemEntity#getCode()}】字段。 COC平台包含了多套系统，该字段用来描述企业用户（系统租户）使用的是哪套系统？如果同一企业用户（系统租户）同时使用了多套系统，则用“|”分隔（如Sytem1|System2）。
 * <LI>数据源KEY【{@link #getDataSourceCode()}】:逻辑外键，关联到【{@link IDataSourceEntity#getCode()}】字段。每套系统可以使用不同的数据源，同理每个租户也可以使用不同的数据源。该字段即用来描述企业用户（系统租户）使用的是哪个数据源？
 * <LI>有效起始日期【{@link #getExpiredFrom()}】：企业用户（系统租户）只能在有效期起始日期之后有权使用平台提供的系统功能。
 * <LI>有效截止日期【{@link #getExpiredTo()}】：企业用户（系统租户）只能在有效截止日期之前有权使用平台提供的系统功能。
 * <LI>企业用户（系统租户）域名【{@link #getDomain()}】：可以通过访问的域名来判断当前登录用户是哪个企业的？
 * </OL>
 * 
 * @author yongshan.ji
 * @preserve all
 * 
 */
public interface ITenantEntity extends INamedEntity {

	String getDataSourceCode();

	String getDomain();

	String getSystemCode();
}
