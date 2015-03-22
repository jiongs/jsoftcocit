package com.jsoft.cocit.entity;

import java.util.Date;

import javax.persistence.Entity;

import com.jsoft.cocit.entity.log.ILoginLog;

/**
 * 【数据实体】接口：该接口的所有实现类如带有【{@link Entity}】注释即为实体类，实体类将被ORM框架映射到数据库表，其实体对象都将被映射到数据库表记录。
 * <p>
 * 此接口中定义了一些平台通用的公共字段Getter方法。包括：
 * <OL>
 * <LI>数据ID【{@link #getId()}】：即数据的唯一标识符，是数据的物理主键，没有业务含义。发生数据移植、上报、合并等业务处理的时候，该字段不参与业务处理。
 * <LI>数据版本【{@link #getVersion()}】：用来作为数据版本控制字段，不允许两个人同时修改一条数据。
 * <LI>创建者登录信息【{@link #getCreatedOpLog()}】：系统登录日志KEY【{@link ILoginLog#getKey()}】，表示添加此记录到数据表中的登录信息是什么(如哪个IP、什么浏览器、哪个登录帐号等)？
 * <LI>创建者帐号【{@link #getCreatedUser()}】：系统当前登录帐号(是创建者登录信息的冗余字段)，表示添加此记录到数据表中的是“谁”？
 * <LI>创建时间【{@link #getCreatedDate()}】：系统当前时间，表示添加此记录到数据表中是“什么时间”？
 * <LI>最近修改者登录信息【{@link #getCreatedOpLog()}】：系统登录日志KEY【{@link ILoginLog#getKey()}】，表示最近修改此记录的登录信息是什么(如哪个IP、什么浏览器、哪个登录帐号等)？
 * <LI>最近修改者帐号【{@link #getCreatedUser()}】：系统当前登录帐号(是修改者登录信息的冗余字段)，表示最近修改此记录的是“谁”？
 * <LI>最近修改时间【{@link #getCreatedDate()}】：系统当前时间，表示最近修改此记录是“什么时间”？
 * <LI><I>以上字段由开发平台自动设值。</I>
 * <LI>数据KEY【{@link #getKey()}】：数据的KEY，数据的“逻辑主键”，或与其他字段（如租户KEY、其他主表KEY等）组合后作为数据的“逻辑主键”。
 * <LI>数据状态码【{@link #getStatusCode()}】：用来描述数据的当前状态，可以表示在此记录上执行了什么操作？
 * <UL>
 * <LI>“新建【{@link StatusCodes#STATUS_CODE_NEW}】”状态：即该数据自insert到数据表之后，未对该数据执行过“update”操作。
 * <LI>“预置【{@link StatusCodes#STATUS_CODE_BUILDIN}】”状态：即系统预置数据。用户只能使用；不能对该数据执行“update”或“delete”操作。
 * <LI>“删除【{@link StatusCodes#STATUS_CODE_REMOVED}】”状态：表示该数据已被逻辑删除。不允许该数据参与任何业务逻辑的处理；不允许对该数据执行“update”操作；但可以对该数据执行“delete”操作即物理删除。
 * <LI>“停用【{@link StatusCodes#STATUS_CODE_DISABLED}】”状态：即已被停用的数据。不允许该数据参与任何业务逻辑的处理；但可以对该数据执行“update”操作。
 * <LI>“归档【{@link StatusCodes#STATUS_CODE_ARCHIVED}】”状态：即已被归档的数据。不允许对该数据执行“update”操作；不允许对该数据执行“delete”操作即永久删除；但可以该数据可以被移植到另外一张单独的数据表中即物理归档。
 * <LI>对基础数据的维护类操作，可以根据操作名称的不同，有区别地选择 1 到 99 之间的值作为数据的状态码。
 * <LI>处理业务逻辑的过程中，对数据所作“正向的、正面的、积极的”update操作（如业务流程的前进状态），其状态码介于 1 到 99 之间。
 * <LI>处理业务逻辑的过程中，对数据所作“反向的、反面的、消极的”update操作（如业务流程的返回状态），其状态码介于 -1 到 -99 之间。
 * <LI>只能对“删除（{@link StatusCodes#STATUS_CODE_REMOVED}）”状态的数据执行“delete”操作即永久删除，物理归档除外。
 * </UL>
 * <LI>数据是否是预置的【{@link #isBuildin()}】：检查“实体数据状态{@link #getStatusCode()}”是否等于“预置（{@link StatusCodes#STATUS_CODE_BUILDIN}）”状态？
 * <LI>数据是否已被停用【{@link #isDisabled()}】：检查“实体数据状态{@link #getStatusCode()}”是否等于“停用（{@link StatusCodes#STATUS_CODE_DISABLED}）”状态？
 * <LI>数据是否已被删除【{@link #isRemoved()}】： 检查“实体数据状态{@link #getStatusCode()}”是否等于“删除（{@link StatusCodes#STATUS_CODE_REMOVED}）”状态？
 * <LI>数据是否已被归档【{@link #isArchived()}】：检查“实体数据状态{@link #getStatusCode()}”是否等于“归档（{@link StatusCodes#STATUS_CODE_ARCHIVED}）”状态？
 * <LI>将数据转换为JSON【{@link #toJson()}】：将实体数据转换成JSON格式。
 * </OL>
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
public interface IDataEntity {

	Long getId();

	String getKey();

	Integer getVersion();

	Date getCreatedDate();

	String getCreatedUser();

	String getCreatedOpLog();

	Date getUpdatedDate();

	String getUpdatedUser();

	String getUpdatedOpLog();

	int getStatusCode();

	boolean isBuildin();

	boolean isDisabled();

	boolean isRemoved();

	boolean isArchived();

	String toJson();

	void release();
}
