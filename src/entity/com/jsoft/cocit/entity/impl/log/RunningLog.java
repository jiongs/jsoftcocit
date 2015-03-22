package com.jsoft.cocit.entity.impl.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.log.IRunningLog;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
@Entity
@CocEntity(name = "系统运行日志", key = Const.TBL_LOG_RUNNING, sn = 3, actions = {
//
		@CocAction(name = "清空", opCode = OpCodes.OP_CLEAR, key = "clr") //
		, @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v") //
}//
, groups = { @CocGroup(name = "基本信息", key = "basic"//
, fields = {
//
		@CocColumn(propName = "message") //
		, @CocColumn(propName = "remoteUri")//
		, @CocColumn(propName = "remoteIp")//
		, @CocColumn(propName = "remoteAddress")//
		, @CocColumn(propName = "datetime")//
		, @CocColumn(propName = "eslipse")//
		, @CocColumn(propName = "memEslipse")//
		, @CocColumn(propName = "level") //
		, @CocColumn(propName = "monitor")//
		, @CocColumn(propName = "threadname") //
		, @CocColumn(propName = "loginuser")//
		, @CocColumn(propName = "remoteUrl")//
		, @CocColumn(propName = "loggername") //
		, @CocColumn(propName = "locationinfo") //
		, @CocColumn(propName = "stacktrace") //
}) }// end groups
)
public class RunningLog implements IRunningLog {
	@Id
	@Column(name = "id_")
	protected Long id;

	@Column(length = 50)
	@CocColumn(name = "登录用户")
	protected String loginuser;

	protected String fqnofctgrcls;

	@CocColumn(name = "日志名称")
	protected String loggername;

	@CocColumn(name = "日志时间", pattern = "yyyy-MM-dd HH:mm:ss,SSS")
	protected Date datetime;

	@Column(length = 20)
	@CocColumn(name = "日志级别", dicOptions = "TRACE:跟踪,DEBUG:调试,INFO:信息,WARN:警告,ERROR:错误,FATAL:致命")
	protected String level;

	@Column(length = 2000)
	@CocColumn(name = "日志内容")
	protected String message;

	@CocColumn(name = "线程名称")
	protected String threadname;

	@Column(columnDefinition = "text")
	@CocColumn(name = "异常信息")
	protected String stacktrace;

	protected String ndc;

	@CocColumn(name = "信息来源")
	protected String locationinfo;

	@CocColumn(name = "远程URL")
	protected String remoteUrl;

	@CocColumn(name = "远程URI")
	protected String remoteUri;

	@Column(length = 64)
	@CocColumn(name = "远程IP")
	protected String remoteIp;

	@Column(length = 64)
	@CocColumn(name = "远程地址")
	protected String remoteAddress;

	@CocColumn(name = "内存消耗")
	protected long memEslipse;

	@CocColumn(name = "时间消耗")
	protected long eslipse;

	@CocColumn(name = "资源检测")
	protected String monitor;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level.toUpperCase().trim();
	}

	public void setStackTrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	public String getNdc() {
		return ndc;
	}

	public void setNdc(String ndc) {
		this.ndc = ndc;
	}

	public String getFqnofctgrcls() {
		return fqnofctgrcls;
	}

	public void setFqnofctgrcls(String fqnofctgrcls) {
		this.fqnofctgrcls = fqnofctgrcls;
	}

	public String getLoggername() {
		return loggername;
	}

	public void setLoggername(String loggername) {
		this.loggername = loggername;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setLogtime(Date logtime) {
		this.datetime = logtime;
	}

	public String getThreadname() {
		return threadname;
	}

	public void setThreadname(String threadname) {
		this.threadname = threadname;
	}

	public String getStacktrace() {
		return stacktrace;
	}

	public void setStacktrace(String stacktrace) {
		this.stacktrace = stacktrace;
	}

	public String getLocationinfo() {
		return locationinfo;
	}

	public void setLocationinfo(String locationinfo) {
		this.locationinfo = locationinfo;
	}

	public String getLoginuser() {
		return loginuser;
	}

	public void setLoginuser(String loginuser) {
		this.loginuser = loginuser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public String getRemoteUri() {
		return remoteUri;
	}

	public void setRemoteUri(String remoteUri) {
		this.remoteUri = remoteUri;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public long getMemEslipse() {
		return memEslipse;
	}

	public void setMemEslipse(long memEslipse) {
		this.memEslipse = memEslipse;
	}

	public long getEslipse() {
		return eslipse;
	}

	public void setEslipse(long eslipse) {
		this.eslipse = eslipse;
	}

	public String getMonitor() {
		return monitor;
	}

	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
}
