package com.jsoft.cocimpl.mvc.nutz;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionInfo;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.View;
import org.nutz.mvc.impl.processor.ViewProcessor;

import com.jsoft.cocit.util.ExceptionUtil;

/**
 * 
 * @preserve public
 * @author Ji Yongshan
 * 
 */
public class CocFailProcessor extends ViewProcessor {

	public void init(NutConfig config, ActionInfo ai) throws Throwable {
		view = evalView(config, ai, ai.getFailView());
	}

	public void process(ActionContext ac) throws Throwable {
		Object re = ac.getMethodReturn();
		// Store object to request
		if (null != re)
			ac.getRequest().setAttribute(ViewProcessor.DEFAULT_ATTRIBUTE, re);
		Throwable err = ExceptionUtil.root(ac.getError());
		if (re != null && re instanceof View) {
			((View) re).render(ac.getRequest(), ac.getResponse(), err);
		} else {
			view.render(ac.getRequest(), ac.getResponse(), null == re ? err : re);
		}
		doNext(ac);
	}
}