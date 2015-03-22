package com.jsoft.cocit.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.entity.log.ILoginLog;
import com.jsoft.cocit.entity.security.IUser;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * “数据实体”基类：该类实现了【{@link IDataEntity}】接口，其所有子类所映射的数据表都将拥有如下字段：
 * <p>
 * <UL>
 * <LI>id：数据ID，物理主键，数据库端默认字段名【id_】，字段类型【Long】,物理主键的值将通过COC平台提供的ID生成器【COC_ID_GENERATOR】自动产生
 * <LI>key：数据KEY，逻辑主键，数据库端默认字段名【key_】，字段类型【String】，字段长度【64】
 * <LI>version：数据版本，数据库端默认字段名【version_】，字段类型【Integer】，字段长度【64】
 * <LI>createdDate：创建时间，数据库端默认字段名【created_date_】，字段类型【Date】
 * <LI>createdUser：创建帐号，数据库端默认字段名【created_user_】，字段类型【逻辑外键，关联到“用户信息表的逻辑主键{@link IUser#getKey()}”】，字段长度【64】
 * <LI>createdLogin：创建者登录信息，数据库端默认字段名【created_login_】，字段类型【逻辑外键，关联到“登录日志表的逻辑主键{@link ILoginLog#getKey()}”】，字段长度【64】
 * <LI>updatedDate：最近修改时间，数据库端默认字段名【updated_date_】，字段类型【Date】
 * <LI>updatedUser：最近修改帐号，数据库端默认字段名【updated_user_】，字段类型【逻辑外键，关联到“用户信息表的逻辑主键{@link IUser#getKey()}”】，字段长度【64】
 * <LI>updatedLogin：最近修改者登录信息，数据库端默认字段名【updated_login_】，字段类型【逻辑外键，关联到“登录日志表的逻辑主键{@link ILoginLog#getKey()}”】，字段长度【64】
 * <LI>statusCode：数据状态，数据库端默认字段名【status_code_】，字段类型【int】
 * <LI>更多信息参见【{@link IDataEntity}】
 * </UL>
 * 
 * @author yongshan.ji
 * @preserve all
 */
public abstract class DataEntity implements IExtDataEntity {

	@Id
	@Column(name = "id_")
	@GeneratedValue(generator = "CocitIdGenerator", strategy = GenerationType.TABLE)
	@TableGenerator(name = "CocitIdGenerator", table = "COC_ID_GENERATOR", pkColumnName = "id_key", valueColumnName = "next_hi", allocationSize = 1, initialValue = 20)
	protected Long id;

	@Column(name = "key_", length = 64)
	protected String key;

	@Column(name = "version_")
	protected Integer version;

	@Column(name = "created_date_")
	protected Date createdDate;

	@Column(length = 64, name = "created_user_")
	protected String createdUser;

	@Column(length = 64, name = "created_oplog_")
	protected String createdOpLog;

	@Column(name = "updated_date_")
	protected Date updatedDate;

	@Column(length = 64, name = "updated_user_")
	protected String updatedUser;

	@Column(length = 64, name = "updated_oplog_")
	protected String updatedOpLog;

	@Column(name = "status_code_")
	protected int statusCode;

	public DataEntity() {

	}

	public DataEntity(String key) {
		this.key = key;
	}

	public boolean isBuildin() {
		return Const.STATUS_CODE_BUILDIN == this.statusCode;
	}

	public boolean isDisabled() {
		return Const.STATUS_CODE_DISABLED == this.statusCode;
	}

	public boolean isRemoved() {
		return Const.STATUS_CODE_REMOVED == this.statusCode;
	}

	public boolean isArchived() {
		return Const.STATUS_CODE_ARCHIVED == this.statusCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getVersion() {
		return version;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setVersion(Integer id) {
		this.version = id;
	}

	public void setUpdatedDate(Date date) {
		this.updatedDate = date;
	}

	public void setStatusCode(int code) {
		statusCode = code;
	}

	public void setUpdatedUser(String operatedUser) {
		this.updatedUser = operatedUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getCreatedOpLog() {
		return createdOpLog;
	}

	public void setCreatedOpLog(String loginLog) {
		this.createdOpLog = loginLog;
	}

	public String getUpdatedOpLog() {
		return updatedOpLog;
	}

	public void setUpdatedOpLog(String loginLog) {
		this.updatedOpLog = loginLog;
	}

	// ***Other

	// public int hashCode() {
	// if (id == null) {
	// return super.hashCode();
	// }
	// return 37 * 17 + id.hashCode();
	// }

	public boolean equals(Object that) {
		if (that == null)
			return false;

		if (!ClassUtil.getType(getClass()).equals(ClassUtil.getType(that.getClass()))) {
			return false;
		}

		DataEntity thatEntity = (DataEntity) that;
		if (id == null || id == 0 || thatEntity.id == null || thatEntity.id == 0) {
			return this == that;
		}

		return thatEntity.id.equals(id);
	}

	public String toString() {
		if (key != null && key.trim().length() > 0) {
			return key;
		}

		if (id != null) {
			return id.toString();
		}

		return super.toString();
	}

	public String toJson() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");

		sb.append("\"key\": ").append(JsonUtil.toJson(key));
		toJson(sb);

		sb.append("}");

		return sb.toString();
	}

	protected void toJson(StringBuffer sb, String field, Object value) {
		toJson(sb, field, value, null);
	}

	protected void toJson(StringBuffer sb, String field, Object value, Object ignoreValue) {
		if (value == null)
			return;
		if (ignoreValue == null) {
			if (value instanceof String && StringUtil.isBlank((String) value))
				return;
			if (value.equals(0) || value.equals(0.0) || value.equals(false))
				return;
		} else {
			if (value.equals(ignoreValue))
				return;
		}

		sb.append(", \"" + field + "\": ").append(JsonUtil.toJson(value));
	}

	protected void toJson(StringBuffer sb) {
		this.toJson(sb, "statusCode", statusCode);
		this.toJson(sb, "version", version);
		this.toJson(sb, "createdUser", createdUser);
		this.toJson(sb, "createdDate", createdDate);
		this.toJson(sb, "createdOpLog", createdOpLog);
		this.toJson(sb, "updatedUser", updatedUser);
		this.toJson(sb, "updatedDate", updatedDate);
		this.toJson(sb, "updatedOpLog", updatedOpLog);
		this.toJson(sb, "id", id);
	}

	public void release() {
		this.id = null;
		this.key = null;
		this.version = null;
		this.createdDate = null;
		this.createdOpLog = null;
		this.createdUser = null;
		this.updatedDate = null;
		this.updatedOpLog = null;
		this.updatedUser = null;
		this.statusCode = 0;
	}
}
