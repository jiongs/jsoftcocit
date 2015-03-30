package com.jsoft.cocit.entity.log;

/**
 * 上传文件信息接口
 * 
 * @author yongshan.ji
 * 
 */
public interface IUploadLog {
	String getKey();

	/**
	 * 获取上传时的本地文件名
	 * 
	 * @return 返回原始文件名称
	 */
	String getLocalName();

	String getLocalNamePinYin();

	/**
	 * 获取文件相对路径
	 * 
	 * @return 返回SERVLET环境下的文件相对路径
	 */
	String getFilePath();

	/**
	 * 获取上传文件内容字节大小
	 * 
	 * @return
	 */
	Long getContentLength();

	/**
	 * 获取上传文件内容类型
	 * 
	 * @return
	 */
	String getContentType();

	/**
	 * 获取文件绝对路径
	 * 
	 * @return
	 */
	String getAbstractPath();

}
