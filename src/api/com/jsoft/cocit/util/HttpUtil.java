package com.jsoft.cocit.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jsoft.cocimpl.ExtHttpContext;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.exception.CocException;

public abstract class HttpUtil {

	public static void writeJson(HttpServletResponse response, String json) throws IOException {
		write(response, json, "text/json; charset=UTF-8");
	}

	public static void writeHtml(HttpServletResponse response, String html) throws IOException {
		write(response, html, "text/html; charset=UTF-8");
	}

	public static void writeXml(HttpServletResponse response, String xml) throws IOException {
		write(response, xml, "text/xml; charset=UTF-8");
	}

	public static void write(HttpServletResponse response, String content, String contentType) throws IOException {
		PrintWriter writer = null;
		try {
			response.setContentType(contentType);
			writer = response.getWriter();
			writer.write(content);
			writer.flush();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Throwable ex) {
					LogUtil.error("HttpUtil.write: error!", ex);
				}
			}
		}
	}

	public static void write(HttpServletResponse response, File file) throws IOException {
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			String extName = file.getName();
			extName = extName.substring(extName.lastIndexOf(".") + 1);
			String attName = file.getName();
			response.setHeader("Content-Disposition", "attachement; filename=" + attName);
			response.setContentType("application/octet-stream");
			inStream = new FileInputStream(file);
			outStream = response.getOutputStream();
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			outStream.flush();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (Throwable ex) {
					LogUtil.error("HttpUtil.write: error!", ex);
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (Throwable ex) {
					LogUtil.error("HttpUtil.write: error!", ex);
				}
			}
		}
	}

	/**
	 * 
	 */
	// private static Color getRandColor(int fc, int bc) {
	// Random random = new Random();
	// if (fc > 255)
	// fc = 255;
	// if (bc > 255)
	// bc = 255;
	// int r = fc + random.nextInt(bc - fc);
	// int g = fc + random.nextInt(bc - fc);
	// int b = fc + random.nextInt(bc - fc);
	// return new Color(r, g, b);
	// }

	/**
	 * 生成随机验证码，验证码被保存在session(key=cocit_verify_code)中，可以通过{@link #checkVerificationCode(String)进行验证 。
	 */
	public static String makeSmsVerifyCode(HttpServletRequest request, String mobile) {

		if (!StringUtil.isMobile(mobile)) {
			throw new CocException("非法手机号码！");
		}

		StringBuffer sb = new StringBuffer();

		char[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

		Random r = new Random();
		int index, len = ch.length;
		for (int i = 0; i < 4; i++) {
			index = r.nextInt(len);
			sb.append(ch[index]);
		}

		HttpSession session = request.getSession(true);

		session.setAttribute("cocit_verify_mobile", mobile);
		session.setAttribute("cocit_verify_code", sb.toString());

		return sb.toString();
	}

	/**
	 * 检查客户端提交的验证码是否与session(key=cocit_verify_code)中保存的验证码一致？如果不一致，将抛出异常。
	 * <p>
	 * 检查时将忽略大小写。
	 * 
	 * @param code
	 * @param exceptionMessage
	 */
	public static void checkSmsVerifyCode(HttpServletRequest request, String mobile, String code, String exceptionMessage) {

		if (StringUtil.isBlank(exceptionMessage))
			exceptionMessage = "验证码或手机号不正确！";

		HttpSession session = request.getSession(true);

		String validMobile = (String) session.getAttribute("cocit_verify_mobile");
		String validCode = (String) session.getAttribute("cocit_verify_code");

		if (StringUtil.isBlank(code) || StringUtil.isBlank(validCode) || StringUtil.isBlank(mobile) || StringUtil.isBlank(validMobile))
			throw new CocException(exceptionMessage);

		if (!code.toLowerCase().equals(validCode.toLowerCase()) || !mobile.toLowerCase().equals(validMobile.toLowerCase()))
			throw new CocException(exceptionMessage);

		session.removeAttribute("cocit_verify_mobile");
		session.removeAttribute("cocit_verify_code");
	}

	/**
	 * 生成随机图片验证码，并将图片对象输出到response中
	 * <p>
	 * 验证码文本被保存在session(key=cocit_verify_code)中，可以通过{@link #checkVerificationCode(String)}进行验证 。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String makeImgVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics g = image.getGraphics();

		Random random = new Random();

		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.white);

		// 生成彩色背景
		// g.setColor(getRandColor(200, 250));
		// g.fillRect(0, 0, width, height);
		// g.setColor(getRandColor(160, 200));
		// for (int i = 0; i < 100; i++) {
		// int x = random.nextInt(width);
		// int y = random.nextInt(height);
		// int xl = random.nextInt(12);
		// int yl = random.nextInt(12);
		// g.drawLine(x, y, x + xl, y + yl);
		// }

		char[] ch = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();

		g.setFont(new Font("Times New Roman", Font.ITALIC | Font.BOLD | Font.PLAIN, 18));

		StringBuffer sb = new StringBuffer();
		int index, len = ch.length;
		for (int i = 0; i < 4; i++) {
			g.setColor(new Color(random.nextInt(88), random.nextInt(188), random.nextInt(255)));

			// 写什么数字，在图片 的什么位置画
			index = random.nextInt(len);
			g.drawString("" + ch[index], (i * 14) + 2, 16);

			sb.append(ch[index]);
		}

		HttpSession session = request.getSession(true);
		session.setAttribute("cocit_verify_code", sb.toString());

		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		OutputStream out = null;
		try {
			out = response.getOutputStream();

			ImageIO.write(image, "JPEG", out);
		} catch (IOException ex) {
			LogUtil.error("HttpUtil.makeImgVerifyCode: error! ", ex);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ex) {
					LogUtil.error("HttpUtil.makeImgVerifyCode: error! ", ex);
				}
			}
		}

		return sb.toString();
	}

	/**
	 * 检查客户端提交的验证码是否与session(key=cocit_verify_code)中保存的验证码一致？如果不一致，将抛出异常。
	 * <p>
	 * 检查时将忽略大小写。
	 * 
	 * @param code
	 * @param exceptionMessage
	 */
	public static void checkImgVerifyCode(HttpServletRequest request, String code, String exceptionMessage) {

		if (StringUtil.isBlank(exceptionMessage))
			exceptionMessage = "验证码不正确！";

		HttpSession session = request.getSession(true);

		String validCode = (String) session.getAttribute("cocit_verify_code");

		if (StringUtil.isBlank(code) || StringUtil.isBlank(validCode))
			throw new CocException(exceptionMessage);

		if (!code.toLowerCase().equals(validCode.toLowerCase()))
			throw new CocException(exceptionMessage);
	}

	public static void removeImgVarifyCode(HttpServletRequest request) {

		HttpSession session = request.getSession(true);

		session.removeAttribute("cocit_verify_code");

	}

	public static void renderHTMLHeader(Writer out, String contextPath, String title) throws IOException {
		HttpContext context = Cocit.me().getHttpContext();
		SystemService system = context.getLoginSystem();
		String[] themesCSS = StringUtil.toArray(system.getConfigItem(Const.CONFIG_KEY_THEMES_CSS, "blue"), ",");
		String[] themesJS = StringUtil.toArray(system.getConfigItem(Const.CONFIG_KEY_THEMES_JS, ""), ",");

		write(out, "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		write(out, "<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		write(out, "<head>");
		write(out, "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		write(out, "<title>%s</title>", (title == null ? "" : title));

		/*
		 * CSS
		 */
		write(out, "<link href=\"%s/jCocit.min.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", MVCUtil.stylePath);
		if (themesCSS != null) {
			for (String theme : themesCSS) {
				if (theme.startsWith("/")) {
					write(out, "<link href=\"%s%s\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath, theme);
				} else {
					write(out, "<link href=\"%s/ext/jCocit.themes.%s.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", MVCUtil.stylePath, theme);
				}
			}
		}

		/*
		 * 开发模式
		 */
		ExtHttpContext ctx = (ExtHttpContext) Cocit.me().getHttpContext();
		if (ctx.isDevHost()) {
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.panel.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.button.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			// print(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.combodate.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.icon.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.searchbox.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.tabs.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.tree.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.dialog.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.window.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.datagrid.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.upload.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.alerts.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.ui.messager.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);

			//
			write(out, "<link href=\"%s/jCocit-src/css/jCocit.plugin.entity.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath);

			if (themesCSS != null) {
				for (String theme : themesCSS) {
					if (theme.startsWith("/")) {
						write(out, "<link href=\"%s%s\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath, theme);
					} else {
						write(out, "<link href=\"%s/jCocit-src/css/ext/jCocit.themes.%s.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\" />", contextPath, theme);
					}
				}
			}
		}

		/*
		 * JS
		 */
		write(out, "<script src=\"%s/jQuery.min.js\" type=\"text/javascript\"></script>", MVCUtil.scriptPath);
		write(out, "<script src=\"%s/jCocit.min.js\" type=\"text/javascript\"></script>", MVCUtil.scriptPath);
		if (themesJS != null) {
			for (String theme : themesJS) {
				if (theme.equals("ckeditor")) {

					write(out, "<script src=\"%s/ckeditor/ckeditor.js\" type=\"text/javascript\"></script>", MVCUtil.scriptPath);
					write(out, "<script src=\"%s/ckfinder/ckfinder.js\" type=\"text/javascript\"></script>", MVCUtil.scriptPath);

					continue;
				}

				if (theme.startsWith("/")) {
					write(out, "<script src=\"%s%s\" type=\"text/javascript\"></script>", contextPath, theme);
				} else {
					write(out, "<script src=\"%s/ext/jCocit.themes.%s.js\" type=\"text/javascript\"></script>", MVCUtil.scriptPath, theme);
				}
			}
		}
		write(out, "<script src=\"%s/ext/jCocit.nls.zh.js\" type=\"text/javascript\"></script>", MVCUtil.scriptPath);
		if (((ExtHttpContext) Cocit.me().getHttpContext()).isDevHost()) {
			// write(out, "<script src=\"%s/jCocit-src/js/jCocit.core.js\" type=\"text/javascript\"></script>", contextPath);
			// write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.combo.js\" type=\"text/javascript\"></script>", contextPath);
			// write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.combodialog.js\" type=\"text/javascript\"></script>", contextPath);
			// write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.searchbox.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.utils.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.parse.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.alerts.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.ckeditor.js\" type=\"text/javascript\"></script>", contextPath);
			// write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.dialog.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.tree.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.combo.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.combotree.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.datagrid.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.combodialog.js\" type=\"text/javascript\"></script>", contextPath);
			// write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.pagination.js\" type=\"text/javascript\"></script>", contextPath);
			 write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.treegrid.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.upload.js\" type=\"text/javascript\"></script>", contextPath);
			// write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.ckeditor.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.button.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.dialog.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.ui.messager.js\" type=\"text/javascript\"></script>", contextPath);
			write(out, "<script src=\"%s/jCocit-src/js/jCocit.plugin.entity.js\" type=\"text/javascript\"></script>", contextPath);

			if (themesJS != null) {
				for (String theme : themesJS) {
					if (theme.equals("ckeditor"))
						continue;

					if (theme.startsWith("/")) {
						write(out, "<script src=\"%s%s\" type=\"text/javascript\"></script>", contextPath, theme);
					} else {
						write(out, "<script src=\"%s/jCocit-src/js/jCocit.themes.%s.js\" type=\"text/javascript\"></script>", contextPath, theme);
					}
				}
			}

			write(out, "<script src=\"%s/jCocit-src/js/jCocit.nls.zh.js\" type=\"text/javascript\"></script>", contextPath);
		}

		/*
		 * 语言设置
		 */
		write(out, "<script type=\"text/javascript\">$(document).ready(function(){jCocit.setOptions({contextPath:'%s',contentWidth:%s,contentHeight:%s});});</script>", //
		        contextPath,//
		        ctx.getClientUIWidth(), //
		        ctx.getClientUIHeight()//
		);

		write(out, "</head>");
		write(out, "<body>");

	}

	public static void renderHTMLFooter(Writer out) throws IOException {
		write(out, "</body></html>");
	}

	private static void write(Writer out, String format, Object... args) throws IOException {
		out.write("\n");
		if (args.length == 0)
			out.write(format);
		else
			out.write(String.format(format, args));
	}
}
