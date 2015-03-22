package com.jsoft.cocimpl.securityengine.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.jsoft.cocimpl.securityengine.impl.encoder.MD5PasswordEncoder;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.entity.TenantOwnerNamedEntity;
import com.jsoft.cocit.entity.security.ISystemUser;
import com.jsoft.cocit.util.StringUtil;

public class RootUser extends TenantOwnerNamedEntity implements ISystemUser {

	@Column(length = 128)
	private String emailAddress;
	@Column(length = 128)
	private String telCode;
	@Column(length = 128)
	private String password;
	@Transient
	private String rawPassword;
	@Transient
	private String rawPassword2;
	@Column(length = 255)
	private String pwdQuestion;// 忘记密码
	@Column(length = 255)
	private String pwdAnswer;
	private String image;//
	private String logo;//
	private Date expiredFrom;
	private Date expiredTo;
	private boolean locked;
	private String referencedKey;
	@Column(length = 512)
	protected String roles;

	RootUser() {
	}

	public String getReferencedKey() {
		return referencedKey;
	}

	public void setReferencedKey(String referencedKey) {
		this.referencedKey = referencedKey;
	}

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

	public void setUsername(String code) {
		super.setKey(code);
		this.encodePwd();
	}

	public void setRawPassword(String pwd) {
		this.rawPassword = pwd;
		this.encodePwd();
	}

	public void setRawPassword2(String rawPassword2) {
		this.rawPassword2 = rawPassword2;
		this.encodePwd();
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
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	protected void encodePwd() {
		if (!StringUtil.isBlank(key) && !StringUtil.isBlank(rawPassword) && !StringUtil.isBlank(rawPassword2)) {
			if (!rawPassword.equals(rawPassword2)) {
				throw new SecurityException("两次密码不一致!");
			}
			this.password = new MD5PasswordEncoder().encodePassword(rawPassword, key);
		}
	}

	public int getPrincipalType() {
		return Const.USER_ROOT;
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

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
}