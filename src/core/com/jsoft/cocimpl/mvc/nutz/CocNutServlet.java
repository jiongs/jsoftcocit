package com.jsoft.cocimpl.mvc.nutz;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsoft.cocimpl.ExtHttpContext;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.util.ExceptionUtil;

@SuppressWarnings("serial")
public class CocNutServlet extends HttpServlet {

	
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ExtHttpContext me = null;
		try {
			me = (ExtHttpContext) Cocit.me().makeHttpContext(req, resp);

			if (!Cocit.me().actionHandler().execute(req, resp)) {
				resp.setStatus(404);
			}

		} catch (SecurityException e) {
			throw new ServletException(ExceptionUtil.msg(e));
		} finally {
			if (me != null) {
				me.release();
			}
		}
	}
}
