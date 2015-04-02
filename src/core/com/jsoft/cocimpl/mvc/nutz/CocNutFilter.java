package com.jsoft.cocimpl.mvc.nutz;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.ExtHttpContext;
import com.jsoft.cocit.util.ExceptionUtil;

/**
 * @deprecated
 * @author yongshan.ji
 * 
 */
public class CocNutFilter implements Filter {

	public void init(FilterConfig conf) throws ServletException {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		ExtHttpContext me = null;
		try {
			me = (ExtHttpContext) Cocit.me().makeHttpContext(req, resp);

			if (Cocit.me().actionHandler().execute(req, resp)) {
				return;
			}

		} catch (SecurityException e) {
			throw new ServletException(ExceptionUtil.msg(e));
		} finally {
			if (me != null) {
				me.release();
			}
		}
		chain.doFilter(req, resp);
	}
}
