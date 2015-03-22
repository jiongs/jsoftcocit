package com.jsoft.cocit.entity.impl.security;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.security.ISystemUser;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;
import com.jsoft.cocit.entityengine.annotation.CuiEntity;
import com.jsoft.cocit.entityengine.annotation.CuiForm;
import com.jsoft.cocit.entityengine.annotation.CuiGrid;
import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.util.StringUtil;

/**
 * 系统用户：即使用平台提供的“系统”的帐号
 * 
 */
@Entity
@CocEntity(name = "系统用户管理", key = Const.TBL_SEC_SYSUSER, sn = 4, uniqueFields = Const.FLIST_TENANT_KEYS, indexFields = Const.FLIST_TENANT_KEYS,//
           actions = {
                   //
                   @CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c", uiForm = "user_form"),//
                   @CocAction(name = "编辑", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e", uiForm = "user_form"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v", uiForm = "user_form"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d", warnMessage = "您确定要删除该用户吗？"), //
                   @CocAction(name = "重置密码", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e1", uiForm = "user_form"), //
                   @CocAction(name = "解锁", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e2", uiForm = "user_form"), //
                   @CocAction(name = "启用", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e3", uiForm = "user_form"), //
                   @CocAction(name = "停用", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e4", uiForm = "user_form"), //
                   @CocAction(name = "确认", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e5", uiForm = "user_form"), //
           },//
           groups = { @CocGroup(name = "基本信息", key = "basic", fields = { //
                                @CocColumn(name = "登录帐号", field = "key", pattern = "username", mode = "c:M *:S"), //
                                        @CocColumn(name = "用户名称", field = "name", mode = "c:M e:M"),//
                                        @CocColumn(name = "邮箱地址", field = "emailAddress", pattern = "email", length = 64), //
                                        @CocColumn(name = "手机号码", field = "telCode", pattern = "phone", length = 16), //
                                        @CocColumn(name = "登录密码", field = "rawPassword", uiView = "password", pattern = "password", mode = "*:N c:M e:E"), //
                                        @CocColumn(name = "验证密码", field = "rawPassword2", uiView = "password", mode = "*:N c:M e:E"), //
                                        @CocColumn(name = "用户状态", field = "statusCode", dicOptions = "0:正常,1:已锁定,99990:初始化,-99999:已删除"),//
                                }) // end group
           }// end groups
)
@CuiEntity(//
           grid = @CuiGrid(fields = "key|name|emailAddress|telCode|statusCode"),//
           forms = {//
           @CuiForm(key = "user_form", fields = "key,name,rawPassword,rawPassword2,emailAddress,telCode") //
           }//
)
public class SystemUser extends Principal implements ISystemUser {

	@OneToMany(mappedBy = "userMember")
	protected List<GroupMember> groups;
	protected String emailAddress;
	protected String telCode;
	@Column(length = 128)
	protected String password;
	@Column(length = 255)
	protected String pwdQuestion;
	@Column(length = 255)
	protected String pwdAnswer;
	protected String image;
	protected String logo;
	protected Date expiredFrom;
	protected Date expiredTo;
	@Column(length = 512)
	protected String roles;

	@Transient
	protected String rawPassword;
	@Transient
	protected String rawPassword2;

	public Date getExpiredFrom() {
		return expiredFrom;
	}

	public void setExpiredFrom(Date expiredFrom) {
		this.expiredFrom = expiredFrom;
	}

	public Date getExpiredTo() {
		return expiredTo;
	}

	public void setExpiredTo(Date expiredTo) {
		this.expiredTo = expiredTo;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return getKey();
	}

	public void setKey(String key) {
		super.setKey(key);

		this.encodePwd();
	}

	public void setUsername(String code) {
		setKey(code);
	}

	public void setRawPassword(String pwd) {
		this.rawPassword = pwd;
		this.encodePwd();
	}

	public void setRawPassword2(String rawPassword2) {
		this.rawPassword2 = rawPassword2;
		this.encodePwd();
	}

	protected void encodePwd() {
		if (!StringUtil.isBlank(key) && !StringUtil.isBlank(rawPassword) && !StringUtil.isBlank(rawPassword2)) {
			if (!rawPassword.equals(rawPassword2)) {
				throw new CocSecurityException("两次密码不一致!");
			}
			this.password = Cocit.me().getSecurityEngine().getPasswordEncoder().encodePassword(rawPassword, key);
		}
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRawPassword() {
		return rawPassword;
	}

	public String getRawPassword2() {
		return rawPassword2;
	}

	public String getPwdQuestion() {
		return pwdQuestion;
	}

	public String getPwdAnswer() {
		return pwdAnswer;
	}

	public String getImage() {
		return image;
	}

	public String getLogo() {
		return logo;
	}

	public void setPwdQuestion(String pwdQuestion) {
		this.pwdQuestion = pwdQuestion;
	}

	public void setPwdAnswer(String pwdAnswer) {
		this.pwdAnswer = pwdAnswer;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public boolean isLocked() {
		return statusCode == 1;
	}

	public int getPrincipalType() {
		return Const.USER_SYSTEM;
	}

	public List<GroupMember> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupMember> groups) {
		this.groups = groups;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

}
