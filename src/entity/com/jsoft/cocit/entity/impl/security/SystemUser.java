package com.jsoft.cocit.entity.impl.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.constant.StatusCodes;
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
                   @CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c", uiForm = "form_c"),//
                   @CocAction(name = "编辑", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e", uiForm = "form_c"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v", uiForm = "form_c"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d", warnMessage = "您确定要删除该用户吗？"), //
                   @CocAction(name = "重置密码", opCode = OpCodes.OP_UPDATE_ROW, key = "e1", warnMessage = "您确定要为该用户重置密码吗？", assignValues = "rawPassword: '12345678', rawPassword2: '12345678'"), //
                   @CocAction(name = "启用", opCode = OpCodes.OP_UPDATE_ROWS, key = "e3", assignValues = "statusCode: 1"), //
                   @CocAction(name = "停用", opCode = OpCodes.OP_UPDATE_ROWS, key = "e4", assignValues = "statusCode: " + StatusCodes.STATUS_CODE_DISABLED), //
           // @CocAction(name = "解锁", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e2", uiForm = "user_form"), //
           // @CocAction(name = "确认", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e5", uiForm = "user_form"), //
           },//
           groups = { @CocGroup(name = "基本信息", key = "basic", fields = { //
                                @CocColumn(name = "登录帐号", field = "key", pattern = "username", mode = "c:M *:S"), //
                                        @CocColumn(name = "用户名称", field = "name", mode = "c:M e:M"),//
                                        @CocColumn(name = "邮箱地址", field = "emailAddress", pattern = "email", length = 64), //
                                        @CocColumn(name = "手机号码", field = "telCode", pattern = "phone", length = 16), //
                                        @CocColumn(name = "登录密码", field = "rawPassword", uiView = "password", pattern = "password", mode = "*:N c:M e:E"), //
                                        @CocColumn(name = "验证密码", field = "rawPassword2", uiView = "password", mode = "*:N c:M e:E"), //
                                        @CocColumn(name = "用户状态", field = "statusCode", dicOptions = "0:新建,1:启用,-999:停用,99990:初始数据"),//
                                }), // end group
                   @CocGroup(name = "权限信息", key = "auth", fields = { //
                             @CocColumn(name = "所属群组", field = "groups", fkTargetEntity = Const.TBL_SEC_GROUP, multiSelect = true, mode = "*:S c:E e:E"), //
                                     @CocColumn(name = "群组名称", field = "groupNames", fkTargetEntity = Const.TBL_SEC_GROUP, fkDependField = "groupNames", fkTargetField = "name", mode = "*:S c:E e:E"), //
                                     @CocColumn(name = "拥有角色", field = "roles", fkTargetEntity = Const.TBL_SEC_ROLE, fkTargetField = "key", mode = " *:S c:E e:E"),//
                                     @CocColumn(name = "角色名称", field = "roleNames", fkTargetEntity = Const.TBL_SEC_ROLE, fkTargetField = "name", fkDependField = "roleNames", mode = "*:S c:E e:E") //
                             }) // end group
           }// end groups
)
@CuiEntity(//
           grid = @CuiGrid(fields = "key|name|emailAddress|telCode|statusCode", rowActions = "e|v|d|e1|e3|e4"),//
           forms = {//
           @CuiForm(key = "form_c", fields = "key,name,rawPassword,rawPassword2,emailAddress,telCode,groups,roles") //
           }//
)
public class SystemUser extends Principal implements ISystemUser {

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
	protected String groups;
	@Column(length = 512)
	protected String groupNames;
	@Column(length = 512)
	protected String roles;
	@Column(length = 512)
	protected String roleNames;

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
		return statusCode == -1;
	}

	public int getPrincipalType() {
		return Const.USER_SYSTEM;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getTelCode() {
		return telCode;
	}

	public void setTelCode(String telCode) {
		this.telCode = telCode;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

}
