package com.jsoft.cocimpl.action;

import java.util.Date;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.CocUrl;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.ui.model.datamodel.AlertModel;
import com.jsoft.cocit.util.DateUtil;
import com.jsoft.cocit.util.HttpUtil;
import com.jsoft.cocit.util.LogUtil;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class UtilAction {

	@At(CocUrl.CHK_HEARTBEAT)
	public AlertModel chkHeartbeat(String timestamp) {
		try {
			HttpContext ctx = Cocit.me().getHttpContext();
			Date date = new Date(Long.parseLong(timestamp));
			String strDate = DateUtil.format(date, DateUtil.DEFAULT_DATE_TIME_PATTERN);
			LogUtil.debug("应用程序心跳检测......{IP:%s, referer:%s, jsessionid:%s}", ctx.getRequest().getRemoteAddr(), ctx.getRequest().getHeader("referer"), ctx.getRequest().getRequestedSessionId());

			return AlertModel.makeSuccess(strDate);
		} catch (Throwable e) {
			return AlertModel.makeError(e.getMessage());
		}
	}

	@At(CocUrl.GET_IMG_VERIFY_CODE)
	public void getImgVerifyCode() {
		HttpContext ctx = Cocit.me().getHttpContext();
		HttpUtil.makeImgVerifyCode(ctx.getRequest(), ctx.getResponse());
	}

	@At(CocUrl.CHK_IMG_VERIFY_CODE)
	public AlertModel chkImgVerifyCode(String code) {
		String message = "";
		try {
			HttpContext ctx = Cocit.me().getHttpContext();
			HttpUtil.checkImgVerifyCode(ctx.getRequest(), code, null);

			message = "检查验证码成功！";

			return AlertModel.makeSuccess(message);
		} catch (Throwable e) {
			LogUtil.warn("", e);
			message = "验证码非法！";

			return AlertModel.makeError(message);
		}
	}

	// @At(UrlAPI.GET_SMS_VERIFY_CODE)
	// public AlertsModel getSmsVerifyCode(String tel) {
	// String message = "";
	// try {
	// ActionContext ctx = Cocit.me().getActionContext();
	//
	// String code = HttpUtil.makeSmsVerifyCode(ctx.getRequest(), tel);
	//
	// SoftService soft = ctx.getSoftService();
	// String tpl = soft.getConfig("sms.verify_code_content", "请在网页表单中输入您的验证码：%s");
	//
	// String content = String.format(tpl, code);
	//
	// /**
	// * 发送短信
	// */
	// MTSmsEntity sms = MTSmsEntity.make("手机验证码", tel, content);
	// ActionHelper actionHelper = ActionHelper.make("0:MTSmsEntity:c");
	// actionHelper.entityManager.save(sms, "c");
	//
	// message = "获取短信验证码成功，请注意查看您的手机短信！";
	//
	// return AlertsModel.makeSuccess(message);
	// } catch (Throwable e) {
	// Log.warn("", e);
	// message = "获取短信验证码失败！" + e.getMessage();
	//
	// return AlertsModel.makeError(message);
	// }
	// }

	@At(CocUrl.GET_SMS_VERIFY_CODE2)
	public AlertModel getSmsVerifyCode2(String tel) {
		String message = "";
		try {
			HttpContext ctx = Cocit.me().getHttpContext();

			String code = HttpUtil.makeSmsVerifyCode(ctx.getRequest(), tel);

			// SoftService soft = ctx.getSoftService();
			// String tpl = soft.getConfig("sms.verify_code_content", "请在网页表单中输入您的验证码：%s");
			//
			// String content = String.format(tpl, code);

			// /**
			// * 发送短信
			// */
			// MTSmsEntity sms = MTSmsEntity.make("手机验证码", tel, content);
			// ActionHelper actionHelper = ActionHelper.make("0:MTSmsEntity:c");
			// actionHelper.entityManager.save(sms, "c");

			message = "短信通道异常，请输入验证码：" + code;

			return AlertModel.makeSuccess(message);
		} catch (Throwable e) {
			LogUtil.warn("", e);
			message = "获取短信验证码失败！" + e.getMessage();

			return AlertModel.makeError(message);
		}
	}

	@At(CocUrl.CHK_SMS_VERIFY_CODE)
	public AlertModel chkSmsVerifyCode(String tel, String code) {
		String message = "";
		try {
			HttpContext ctx = Cocit.me().getHttpContext();
			HttpUtil.checkSmsVerifyCode(ctx.getRequest(), tel, code, null);

			message = "检查验证码成功！";

			return AlertModel.makeSuccess(message);
		} catch (Throwable e) {
			LogUtil.warn("", e);
			message = "验证码非法！";

			return AlertModel.makeError(message);
		}
	}
}
