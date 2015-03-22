package com.jsoft.cocimpl.mvc.nutz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.Loading;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.UrlMapping;
import org.nutz.mvc.impl.ActionInvoker;

import com.jsoft.cocimpl.ExtHttpContext;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.util.LogUtil;

/**
 * 该类在 {@link org.nutz.mvc.ActionHandler} 的基础上修改，详细参见 @Cocit 标识部分
 * 
 * @author yongshan.ji
 * 
 */
public class CocActionHandler {

	private Loading loading;

	private UrlMapping mapping;

	private NutConfig config;

	public CocActionHandler(NutConfig config) {
		this.config = config;
		this.loading = config.createLoading();
		this.mapping = loading.load(config);
	}

	public void depose() {
		loading.depose(config);
	}

	// @Cocit: 新增的方法，用于执行ACTION
	/**
	 * 执行“Cocit平台”中的带应用编号的路径。
	 */
	public boolean execute(HttpServletRequest req, HttpServletResponse resp) {
		LogUtil.trace("CocActionHander.execute: ......");

		ExtHttpContext ctx = (ExtHttpContext) Cocit.me().getHttpContext();

		ActionContext ac = ctx.actionContext();

		ActionInvoker invoker = ctx.actionInvoker();

		if (null == invoker)
			return false;
		else
			LogUtil.trace("CocActionHander.execute: invoker = %s", invoker.getClass().getName());

		invoker.invoke(ac);

		return true;
	}

	// @Cocit: 新增的方法
	/**
	 * 获取UrlMapping对象，创建“Cocit请求”上下文环境时调用该方法，用来获取请求路径对应的 {@link org.nutz.mvc.ActionChain}.
	 */
	public UrlMapping getMapping() {
		return mapping;
	}

}
