package com.jsoft.cocit.ui.model.datamodel;

import com.jsoft.cocimpl.util.ResponseUtil;
import com.jsoft.cocit.constant.Const;

/**
 * Alerts模型：用于产生客户端AJAX所需要的JSON对象，通常用于提示框。
 * <p>
 * <code>
 * {
 * 	"statusCode": 200,
 * 	"message": "操作成功"
 * }
 * <p>
 * {
 * 	"statusCode": 300,
 * 	"message": "操作失败"
 * }
 * <p>
 * {
 * 	"statusCode": 301,
 * 	"message": "无权执行该操作"
 * }
 * </code>
 * 
 * @author jiongsoft
 * 
 */
public class AlertModel extends JSONModel {
	protected int statusCode;

	private AlertModel() {
		super();
	}

	public static AlertModel makeSuccess(String message) {
		return make(Const.RESPONSE_CODE_SUCCESS, message);
	}

	public static AlertModel makeError(String message) {
		return make(Const.RESPONSE_CODE_ERROR, message);
	}

	public static AlertModel makeNoPermission(String message) {
		return make(Const.RESPONSE_CODE_ERROR_NO_PERMISSION, message);
	}

	/**
	 * 
	 * <UL>
	 * <LI>SUCCESS : 200, 操作成功
	 * <LI>SUCCESS_EXTRA_CHANNEL_USER : 201,
	 * <LI>ERROR : 300, 操作失败
	 * <LI>ERROR_NO_ACCESS : 301, 无权执行该操作
	 * <LI>ERROR_WHETHER_KICK_OUT : 302,
	 * <LI>ERROR_DONT_KICK_OUT : 303
	 * </UL>
	 * 
	 * @param statusCode
	 * @param message
	 */
	private static AlertModel make(int statusCode, String message) {
		AlertModel model = new AlertModel();

		model.statusCode = statusCode;
		model.content = message;

		return model;
	}

	@Override
	public void release() {
		super.release();
		this.statusCode = 0;
	}

	public String getContent() {
		return ResponseUtil.makeJson(statusCode, content, context);
	}
}
