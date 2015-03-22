package com.jsoft.cocimpl.mvc.nutz;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.nutz.lang.Strings;
import org.nutz.mvc.ActionChain;
import org.nutz.mvc.ActionChainMaker;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.UrlMapping;
import org.nutz.mvc.annotation.BlankAtException;
import org.nutz.mvc.impl.ActionInvoker;
import org.nutz.mvc.impl.MappingNode;

import com.jsoft.cocit.util.LogUtil;

/**
 * 该类在 {@link org.nutz.mvc.impl.UrlMappingImpl} 的基础上进行修改，详细参见 @Cocit 标识部分
 * 
 * @author yongshan.ji
 * @preserve
 * 
 */
public class CocUrlMappingImpl implements UrlMapping {

	private Map<String, ActionInvoker> map;

	private MappingNode<ActionInvoker> root;

	public CocUrlMappingImpl() {
		this.map = new HashMap<String, ActionInvoker>();
		this.root = new MappingNode<ActionInvoker>();
	}

	public void add(ActionChainMaker maker, ActionInfo ai, NutConfig config) {
		ActionChain chain = maker.eval(config, ai);
		for (String path : ai.getPaths()) {
			if (Strings.isBlank(path))
				throw new BlankAtException(ai.getModuleType(), ai.getMethod());

			// 尝试获取，看看有没有创建过这个 URL 调用者
			ActionInvoker invoker = map.get(path);

			// 如果没有增加过这个 URL 的调用者，为其创建备忘记录，并加入索引
			if (null == invoker) {
				invoker = new ActionInvoker();
				map.put(path, invoker);
				root.add(path, invoker);
			}

			// 将动作链，根据特殊的 HTTP 方法，保存到调用者内部
			if (ai.isForSpecialHttpMethod()) {
				for (String httpMethod : ai.getHttpMethods())
					invoker.addChain(httpMethod, chain);
			}
			// 否则，将其设置为默认动作链
			else {
				invoker.setDefaultChain(chain);
			}

			/*
			 * 打印基本调试信息
			 */
			if (LogUtil.isDebugEnabled()) {
				// 打印路径
				String[] paths = ai.getPaths();
				StringBuilder sb = new StringBuilder();
				if (null != paths && paths.length > 0) {
					sb.append("   '").append(paths[0]).append("'");
					for (int i = 1; i < paths.length; i++)
						sb.append(", '").append(paths[i]).append("'");
				} else {
					sb.append("!!!EMPTY!!!");
				}
				// 打印方法名
				Method method = ai.getMethod();
				String str;
				if (null != method)
					str = method.getName() + "(...) : " + method.getReturnType().getSimpleName();
				else
					str = "???";
				LogUtil.debug("%s >> %s | @Ok(%s) @Fail(%s) | by %d Filters | (I:%s/O:%s)", Strings.alignLeft(sb, 30, ' '), str, ai.getOkView(), ai.getFailView(), (null == ai.getFilterInfos() ? 0 : ai.getFilterInfos().length), ai.getInputEncoding(),
						ai.getOutputEncoding());
			}
		}
		// 记录一个 @At.key
		if (!Strings.isBlank(ai.getPathKey()))
			config.getAtMap().add(ai.getPathKey(), ai.getPaths()[0]);
	}

	public ActionInvoker get(ActionContext ac) {
		String path = Mvcs.getRequestPath(ac.getRequest());
		ActionInvoker invoker = root.get(ac, path);

		if (LogUtil.isDebugEnabled())
			LogUtil.debug("UrlMapping.get: invoker = %s [path: %s]", invoker, path);

		return invoker;
	}

	// @Cocit: 添加一个新方法
	public ActionInvoker get(ActionContext ac, String path) {
		ActionInvoker invoker = root.get(ac, path);

		if (path.endsWith("/")) {
			ac.getPathArgs().add("");
		}

		if (LogUtil.isDebugEnabled())
			LogUtil.debug("UrlMapping.get: invoker = %s [path: %s]", invoker, path);

		return invoker;
	}
}
