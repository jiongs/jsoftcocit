package com.jsoft.cocit.mvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsoft.cocimpl.ExtHttpContext;
import com.jsoft.cocimpl.mvc.nutz.CocActionHandler;
import com.jsoft.cocimpl.mvc.servlet.StaticResourceFilter;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICommonConfig;
import com.jsoft.cocit.constant.UrlAPI;
import com.jsoft.cocit.exception.CocConfigException;
import com.jsoft.cocit.exception.CocUnloginException;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.MVCUtil;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public class CocFilter implements Filter {

	private String encoding = "UTF-8";

	private static String REXP_EXEC_RESOURCE = "^.+\\.(php|asp|aspx)$";

	private static String REXP_IGNORE_RESOURCE = "^.+\\.(ico|java|jsp|jspx|js|css|jsf|php|asp|aspx|" + Cocit.me().getConfig().get(ICommonConfig.UPLOAD_FILTER) + ")$";

	private static String REXP_STATIC_RESOURCE = "^/(scripts2|themes2)/*";

	private Pattern patternExecResource;

	private Pattern patternIgnoreResource;

	private Pattern patternStaticResource;

	private Pattern patternUploadResource;

	private StaticResourceFilter filterStaticResource;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.initCocit(filterConfig);
	}

	public void destroy() {
		this.destroyCoc();
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String uri = MVCUtil.getPath(req);
		String url = MVCUtil.getURL(req) + ";jsessionid=" + req.getRequestedSessionId() + ";IP=" + req.getRemoteAddr();// + ", referer:" + req.getHeader("referer") + "}";
		if (LogUtil.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			Enumeration names = req.getParameterNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				String[] values = req.getParameterValues(name);
				if (values != null) {
					if (values.length > 1) {
						sb.append("\n" + name + " = [");
					} else {
						sb.append("\n" + name + " = ");
					}
					int count = 0;
					for (String v : values) {
						if (count > 0) {
							sb.append(", ");
						}
						sb.append(v);

						count++;
					}
					if (values.length > 1) {
						sb.append("]");
					}
				}
			}

			LogUtil.info("MVC.doFilter: BEGIN ...... [%s]%s", url, sb);
		}

		doPostEncoding(req, resp, chain);

		Cocit.me().makeStopWatch();

		if (!this.doStaticFilters(req, resp, chain, uri, url)) {

			try {
				if (!this.doCocFilter(req, resp, chain, url)) {
					chain.doFilter(req, resp);
				}
			} catch (CocConfigException e) {
				resp.sendRedirect(MVCUtil.makeUrl(UrlAPI.URL_ADMIN_CONFIG));
				resp.flushBuffer();
			} catch (CocUnloginException e) {
				resp.sendRedirect(MVCUtil.makeUrl(UrlAPI.URL_ADMIN_LOGIN));
				resp.flushBuffer();
			}
		}

		LogUtil.info("MVC.doFilter: END! [%s][%s] ", url, Cocit.me().getStopWatch());
	}

	protected void doPostEncoding(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws UnsupportedEncodingException {
		req.setCharacterEncoding(encoding);
	}

	protected boolean doCocFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, String url) throws IOException, ServletException {
		ExtHttpContext ctx = null;
		try {
			ctx = (ExtHttpContext) Cocit.me().makeHttpContext(req, resp);

			CocActionHandler handler = Cocit.me().actionHandler();

			if (handler.execute(req, resp)) {

				LogUtil.debug("MVC.doCocFilter: SUCCESS! [%s]", url);

				return true;
			}

			LogUtil.error("MVC.doCocFilter: FAILED! [%s]", url);

		} catch (Throwable e) {
			LogUtil.error("MVC.doCocFilter: ERROR! [%s] %s", url, e);
		} finally {
			if (ctx != null)
				ctx.release();

			Cocit.me().releaseHttpConext();
		}

		return false;
	}

	protected boolean doStaticFilters(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, String uri, String url) throws IOException, ServletException {
		if (patternExecResource.matcher(uri).find()) {
			// resp.sendError(500);
			Writer out = resp.getWriter();
			/*
			 * 试图锁死攻击者浏览器......
			 */
			try {
				resp.setContentType("text/html; charset=utf-8");
				out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
				out.write("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
				out.write("<head>");
				out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
				out.write("</head>");
				out.write("<body>");
				out.write("<script type=\"text/javascript\">while(true){document.write('小样儿！！！');}</script>");
				out.write("</body>");
				out.write("</html>");
			} catch (Throwable e) {

			} finally {
				out.close();
			}

			return true;

			// throw new ServletException("小样儿!!!");
		}
		if (patternUploadResource.matcher(uri).find()) {
			chain.doFilter(req, resp);
			return true;
		}
		if (patternStaticResource.matcher(uri).find()) {
			if (Cocit.me().getConfig().isProductMode()) {
				chain.doFilter(req, resp);
				return true;
			}

			List<Filter> filters = new LinkedList();
			filters.add(filterStaticResource);

			try {

				FilterInvoker fi = new FilterInvoker(req, resp, chain);
				VirtualFilterChain virtualFilterChain = new VirtualFilterChain(fi, filters);
				virtualFilterChain.doFilter(fi.getRequest(), fi.getResponse());

				return true;
			} catch (Throwable e) {
				// LogUtil.warnf("处理静态资源出错! [%s] %s", url, e);
				// return true;
			}
		}
		if (patternIgnoreResource.matcher(uri).find()) {
			try {
				chain.doFilter(req, resp);
			} catch (Throwable e) {
				LogUtil.trace("%s", e);
			}
			return true;
		}

		return false;
	}

	protected void initCocit(FilterConfig filterConfig) throws ServletException {
		patternExecResource = Pattern.compile(REXP_EXEC_RESOURCE, Pattern.CASE_INSENSITIVE);
		patternIgnoreResource = Pattern.compile(REXP_IGNORE_RESOURCE, Pattern.CASE_INSENSITIVE);
		patternStaticResource = Pattern.compile(REXP_STATIC_RESOURCE, Pattern.CASE_INSENSITIVE);
		patternUploadResource = Pattern.compile("^" + Cocit.me().getConfig().getUploadPath() + "/*", Pattern.CASE_INSENSITIVE);
		filterStaticResource = new StaticResourceFilter();
		filterStaticResource.init(filterConfig);
	}

	protected void destroyCoc() {
		filterStaticResource.destroy();
	}

	// ============执行Cocit业务逻辑===============

	/*
	 * 以下是兼容 SFT 的辅助函数
	 */

	// private Map<String, Filter> filterMap;
	//
	// private Map<String, String[]> filterPattern;
	//
	// private String[] filterNames;
	//
	// private PathMatcher matcher;

	private static class VirtualFilterChain implements FilterChain {
		private FilterInvoker fi;

		private List<Filter> additionalFilters;

		private int currentPosition = 0;

		private int len = 0;

		private VirtualFilterChain(FilterInvoker filterInvocation, List<Filter> additionalFilters) {
			this.fi = filterInvocation;
			this.additionalFilters = additionalFilters;
			this.len = additionalFilters.size();
		}

		public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			if (currentPosition == len) {
				fi.getChain().doFilter(request, response);
			} else {
				Filter nextFilter = additionalFilters.get(currentPosition++);

				if (LogUtil.isTraceEnabled()) {

					String url = MVCUtil.getURL((HttpServletRequest) request);

					LogUtil.trace("Filter<%s>处理请求...[%s]<%s/%s>...", nextFilter.getClass().getSimpleName(), url, currentPosition, len);

					nextFilter.doFilter(request, response, this);

					LogUtil.trace("Filter<%s>处理请求: 结束. [%s]<%s/%s>...", nextFilter.getClass().getSimpleName(), url, currentPosition, len);

				} else {
					nextFilter.doFilter(request, response, this);
				}
			}
		}
	}

	private static class FilterInvoker {

		private FilterChain chain;

		private HttpServletRequest request;

		private HttpServletResponse response;

		private FilterInvoker(ServletRequest request, ServletResponse response, FilterChain chain) {
			if ((request == null) || (response == null) || (chain == null)) {
				throw new IllegalArgumentException("参数值不能为 null");
			}

			if (!(request instanceof HttpServletRequest)) {
				throw new IllegalArgumentException("参数值只能为HttpServletRequest对象");
			}

			if (!(response instanceof HttpServletResponse)) {
				throw new IllegalArgumentException("参数值只能为HttpServletResponse对象");
			}

			this.request = (HttpServletRequest) request;
			this.response = (HttpServletResponse) response;
			this.chain = chain;
		}

		public FilterChain getChain() {
			return chain;
		}

		public ServletRequest getRequest() {
			return request;
		}

		public ServletResponse getResponse() {
			return response;
		}
	}
}
