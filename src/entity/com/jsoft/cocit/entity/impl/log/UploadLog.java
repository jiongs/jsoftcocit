package com.jsoft.cocit.entity.impl.log;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.entity.DataEntity;
import com.jsoft.cocit.entity.log.IExtUploadLog;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

@Entity
@CocEntity(name = "上传文件日志", key = Const.TBL_LOG_UPLOAD//
           , actions = { @CocAction(name = "删除", opCode = 299, key = "d") //
                   , @CocAction(name = "查看", opCode = 102, key = "v") //
           }//
           , groups = { @CocGroup(name = "基本信息", key = "basic"//
                                  , fields = { @CocColumn(name = "文件名称", field = "localName")//
                                          , @CocColumn(name = "名称拼音", field = "localNamePinYin") //
                                          , @CocColumn(name = "文件标识", field = "key") //
                                          , @CocColumn(name = "文件路径", field = "filePath") //
                                          , @CocColumn(name = "文件大小", field = "contentLength") //
                                          , @CocColumn(name = "上传模块", field = "uploadFromModule") //
                                          , @CocColumn(name = "上传字段", field = "uploadFromField") //
                                          , @CocColumn(name = "上传时间", field = "createdDate", pattern = "datetime") //
                                          , @CocColumn(name = "上传帐号", field = "createdUser") //
                                          , @CocColumn(name = "存储路径", field = "abstractPath") //
                                          , @CocColumn(name = "内容类型", field = "contentType") //
                                          , @CocColumn(name = "扩展名", field = "extName") //
                                  }) }// end groups
)
public class UploadLog extends DataEntity implements IExtUploadLog {

	@Column(length = 64, name = "tenant_key_")
	protected String tenantKey;

	@Column(length = 128, name = "tenant_name_")
	protected String tenantName;

	private String uploadFromModule;

	private String uploadFromField;

	private Long contentLength;

	@Column(length = 512)
	private String filePath;

	@Column(length = 128)
	private String localName;

	@Column(length = 128)
	private String localNamePinYin;

	@Column(length = 10)
	private String extName;

	private String contentType;

	public void release() {
		super.release();

		this.tenantKey = null;
		this.tenantName = null;
		this.extName = null;
		this.uploadFromModule = null;
		this.uploadFromField = null;
		this.contentLength = null;
		this.localName = null;
		this.contentType = null;
	}

	public String getAbstractPath() {
		return Cocit.me().getContextDir() + key;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getExtName() {
		return extName;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public String getLocalName() {
		return localName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setFilePath(String path) {
		this.filePath = path;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

	public String getUploadFromModule() {
		return uploadFromModule;
	}

	public void setUploadFromModule(String moduleKey) {
		this.uploadFromModule = moduleKey;
	}

	public String getUploadFromField() {
		return uploadFromField;
	}

	public void setUploadFromField(String fieldKey) {
		this.uploadFromField = fieldKey;
	}

	public String getTenantKey() {
		return tenantKey;
	}

	public void setTenantKey(String tenantKey) {
		this.tenantKey = tenantKey;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getLocalNamePinYin() {
		return localNamePinYin;
	}

	public void setLocalNamePinYin(String localNamePinYin) {
		this.localNamePinYin = localNamePinYin;
	}

}
