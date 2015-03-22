package com.jsoft.cocit.entity.impl.config;

import java.io.IOException;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.nutz.lang.Strings;

import com.jsoft.cocit.config.IDynaBean;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.config.IDataSource;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.PropertiesUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 */
@Entity
@CocEntity(name = "数据源配置", key = Const.TBL_CFG_DATASOURCE, sn = 1, actions = {
//
		@CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c")//
		, @CocAction(importFromFile = "EntityAction.data.js") //
}// end actions
, groups = {
//
@CocGroup(name = "基本信息", key = "basic",//
fields = {
//
		@CocColumn(name = "名称", propName = "name", mode = "*:P c:M e:M")//
		, @CocColumn(name = "编码", propName = "key", mode = "c:M e:M") //
		, @CocColumn(name = "URL", propName = "url", mode = "c:M e:M") //
		, @CocColumn(name = "驱动", propName = "driver", mode = "c:M e:M") //
		, @CocColumn(name = "用户", propName = "user", mode = "c:M e:M") //
		, @CocColumn(name = "密码", propName = "pwd", mode = "c:E e:E") //
		, @CocColumn(name = "序号", propName = "sn", mode = "*:N v:P") //
		, @CocColumn(name = "创建时间", propName = "createdDate", mode = "*:N v:P", pattern = "datetime") //
		, @CocColumn(name = "创建帐号", propName = "createdUser", mode = "*:N v:P") //
		, @CocColumn(name = "修改时间", propName = "updatedDate", mode = "*:N v:P", pattern = "datetime") //
		, @CocColumn(name = "修改帐号", propName = "updatedUser", mode = "*:N v:P") //
}) // end group
}// end groups
)
public class DataSourceImpl extends NamedEntity implements IDataSource, IDynaBean {
	private String url;

	private String driver;

	@Column(length = 64)
	private String user;

	@Column(length = 64)
	private String pwd;

	@Column(name = "ext_status_code_")
	protected Long extStatusCode;

	@Column(name = "ext_field_values_", columnDefinition = "text")
	protected String extFieldValues;

	@Transient
	protected Properties extProps;

	public void release() {
		super.release();

		this.url = null;
		this.driver = null;
		this.user = null;
		this.pwd = null;
		this.extStatusCode = null;
		this.extFieldValues = null;
		if (extProps != null) {
			this.extProps.clear();
			this.extProps = null;
		}
	}

	public String getUrl() {
		return url;
	}

	public String getDriver() {
		return driver;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return pwd;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setExtFieldValues(String props) {
		extFieldValues = props;
		try {
			extProps = PropertiesUtil.toProps(props);
		} catch (IOException e) {
			throw ExceptionUtil.throwEx(e);
		}
	}

	public Properties getProperties() {
		if (extProps == null) {
			try {
				extProps = PropertiesUtil.toProps(this.extFieldValues);
			} catch (IOException e) {
				throw ExceptionUtil.throwEx(e);
			}
		}
		return extProps;
	}

	public String get(String key) {
		return (String) getProperties().get(key);
	}

	public DataSourceImpl set(String key, Object value) {
		if (value == null)
			getProperties().remove(key);
		else
			getProperties().put(key, value);

		try {
			this.extFieldValues = PropertiesUtil.toString(extProps);
		} catch (IOException e) {
			throw ExceptionUtil.throwEx(e);
		}

		return this;
	}

	@Override
	public int hashCode() {
		return 37 * 17 + getUrl().hashCode();
	}

	@Override
	public boolean equals(Object that) {
		if (!getClass().equals(that.getClass())) {
			return false;
		}
		DataSourceImpl thatEntity = (DataSourceImpl) that;
		if ((this == that)) {
			return true;
		}
		String url = getUrl();
		if (Strings.isEmpty(url)) {
			return false;
		}
		String url2 = thatEntity.getUrl();
		if (thatEntity == null || Strings.isEmpty(url2)) {
			return false;
		}
		return url.equals(url2);
	}

	@Override
	public String toString() {
		return new StringBuffer().append("URL=").append(this.getUrl()).append(", DRIVER=").append(this.getDriver()).append(", USER=").append(getUser()).append(", PWD=").append(getPassword()).toString();
	}

}
