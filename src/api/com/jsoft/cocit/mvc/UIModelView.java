package com.jsoft.cocit.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.View;
import org.nutz.mvc.view.JspView;

import com.jsoft.cocimpl.ui.UIView;
import com.jsoft.cocimpl.ui.view.SmartyView;
import com.jsoft.cocimpl.util.ResponseUtil;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.HttpContext;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.ViewKeys;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.exception.CocSecurityException;
import com.jsoft.cocit.ui.model.BaseUIModel;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.ui.model.control.UIForm;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.HttpUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 该类是 Nutz MVC 模型中 View 的实现，用于将 {@link UIModel}的输出工作分派到对应的{@link UIView}。
 * 
 * @author jiongs753
 * 
 */
public class UIModelView implements View {
	public static final String VIEW_TYPE = "coc";

	private static UIModelView me;

	public static UIModelView make() {
		synchronized (UIModelView.class) {
			if (me == null) {
				me = new UIModelView();
			}
			return me;
		}
	}

	@Override
	public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Throwable {
		if (LogUtil.isTraceEnabled()) {
			LogUtil.trace("UIModelView.render: ...... [typeOfUIModel: %s, model: %s]", //
			        obj == null ? "<NULL>" : obj.getClass().getSimpleName(), //
			        StringUtil.toString(obj)//
			);
		}

		if (obj == null)
			return;

		PrintWriter out = null;

		try {
			out = resp.getWriter();

			HttpContext httpContext = Cocit.me().getHttpContext();
			// LoginSession loginSession = httpContext.getLoginSession();
			httpContext.getClientUIToken();
			httpContext.getClientResultUI();

			/*
			 * 通过 View 输出 UIModel
			 */
			if (obj instanceof UIModel) {
				UIModel uiModel = (UIModel) obj;

				if (uiModel.getException() != null) {
					writeError(resp, out, uiModel.getException(), uiModel.getContentType());

					return;
				}

				if (!uiModel.isCachable()) {
					resp.setHeader("Pragma", "no-cache");
					resp.setHeader("Cache-Control", "no-cache");
					resp.setDateHeader("Expires", -1);
				}
				if (!StringUtil.isBlank(uiModel.getContentType())) {
					resp.setContentType(uiModel.getContentType());
				}

				String viewName = uiModel.getViewName();
				if (StringUtil.isBlank(viewName)) {
					throw new CocException("MVC.render: view name not be null! [model: %s]", uiModel.getClass().getSimpleName());
				}

				UIView view = Cocit.me().getViews().getView(viewName);

				/*
				 * 通过已注册的 View(UIView) 输出 HTML
				 */
				if (view != null) {

					StringWriter stringWriter = new StringWriter();
					boolean renderHTMLMetadata = false;
					String title = "";
					if (uiModel instanceof BaseUIModel) {
						BaseUIModel baseModel = (BaseUIModel) uiModel;
						renderHTMLMetadata = !baseModel.isAjax();
						title = baseModel.getTitle();
					}

					// 输出 HTML 起始标签
					if (renderHTMLMetadata) {
						HttpUtil.renderHTMLHeader(stringWriter, Cocit.me().getContextPath(), title);
					}

					view.render(stringWriter, uiModel);

					// 输出 HTML 结束标签
					if (renderHTMLMetadata) {
						HttpUtil.renderHTMLFooter(stringWriter);
					}

					out.write(stringWriter.toString());

				}

				/*
				 * 通过 JSP 输出 HTML
				 */
				else if (viewName.endsWith(".jsp")) {

					// 准备 JSP 变量
					Map context = MVCUtil.GLOBAL_PARAMS;
					Iterator<String> keys = context.keySet().iterator();
					while (keys.hasNext()) {
						String key = keys.next();
						Object value = context.get(key);
						req.setAttribute(key, value);
					}
					if (uiModel instanceof BaseUIModel) {
						BaseUIModel baseModel = (BaseUIModel) uiModel;
						keys = baseModel.getContext().keySet().iterator();
						while (keys.hasNext()) {
							String key = keys.next();
							Object value = baseModel.get(key);
							req.setAttribute(key, value);
						}
					}
					req.setAttribute(ViewKeys.UI_MODEL_KEY, uiModel);
					if (uiModel instanceof UIForm) {
						req.setAttribute(ViewKeys.BEAN_KEY, ((UIForm) uiModel).getDataObject());
					}
					Integer contentWidth = uiModel.get("width", httpContext.getClientUIWidth());
					Integer contentHeight = uiModel.get("height", httpContext.getClientUIHeight());
					req.setAttribute("contentHeight", contentHeight);
					req.setAttribute("contentWidth", contentWidth);

					// 输出 JSP 内容
					new JspView(viewName).render(req, resp, null);

				}

				/*
				 * 通过 JSP 输出 HTML
				 */
				else if (viewName.endsWith(".st")) {

					StringWriter stringWriter = new StringWriter();
					boolean renderHTMLMetadata = false;
					String title = "";

					if (uiModel instanceof BaseUIModel) {
						BaseUIModel baseModel = (BaseUIModel) uiModel;
						renderHTMLMetadata = !baseModel.isAjax();
						title = baseModel.getTitle();
					}

					// 输出 HTML 起始标签
					if (renderHTMLMetadata) {
						HttpUtil.renderHTMLHeader(stringWriter, Cocit.me().getContextPath(), title);
					}

					// 准备 ST 变量
					Map context = new HashMap();
					context.putAll(MVCUtil.GLOBAL_PARAMS);
					context.put(ViewKeys.UI_MODEL_KEY, uiModel);

					// 输出 ST 内容
					SmartyView smartyView = (SmartyView) Cocit.me().getViews().getView(ViewNames.VIEW_SMARTY);
					if (smartyView == null) {
						throw new CocException("MVC.render: view not be found! [viewName: %s]", ViewNames.VIEW_SMARTY);
					}
					smartyView.render(stringWriter, context, viewName);

					// 输出 HTML 结束标签
					if (renderHTMLMetadata) {
						HttpUtil.renderHTMLFooter(stringWriter);
					}

					out.write(stringWriter.toString());

				} else {

					throw new CocException("MVC.render: view not be found! [viewName: %s]", uiModel.getViewName());

				}

				uiModel.release();

			}

			/*
			 * 通过异常信息
			 */
			else if (obj instanceof Throwable) {
				LogUtil.error("错误！", obj);

				writeError(resp, out, (Throwable) obj, Const.CONTENT_TYPE_HTML);

			} else {

				out.write("不支持输出类型！");

				LogUtil.error("UIModelRenderView.render: 不支持输出类型！[type: %s, obj: %s]",//
				        obj == null ? "<NULL>" : obj.getClass().getName(),//
				        StringUtil.toString(obj)//
				);

			}
		} catch (Throwable e) {

			LogUtil.error("UIModelRenderView.render: Error! %s", ExceptionUtil.msg(e), e);

			out.write(ExceptionUtil.msg(e));

		} finally {
			resp.flushBuffer();
			try {
				if (out != null)
					out.close();
			} catch (Throwable e) {
				LogUtil.warn("", e);
			}
		}

	}

	protected void writeError(HttpServletResponse resp, PrintWriter out, Throwable ex, String contentType) throws IOException {

		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", -1);
		resp.setContentType(contentType);

		if (Const.CONTENT_TYPE_HTML.equals(contentType)) {
			ResponseUtil.writeError(out, ex);
		} else if (Const.CONTENT_TYPE_JSON.equals(contentType)) {
			int statusCode = Const.RESPONSE_CODE_ERROR;
			if (ex instanceof CocSecurityException) {
				statusCode = Const.RESPONSE_CODE_ERROR_NO_PERMISSION;
			}
			out.write(ResponseUtil.makeJson(statusCode, ExceptionUtil.msg(ex), null));
		}
	}
}
