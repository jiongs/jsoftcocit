package com.jsoft.cocimpl.mvc.nutz;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;
import org.nutz.mvc.view.DefaultViewMaker;

import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.util.StringUtil;

public class CocViewMaker extends DefaultViewMaker implements ViewMaker {

	
	public View make(Ioc ioc, String t, String v) {
		String value = v;
		String type = t;
		if (StringUtil.isBlank(value)) {
			int idx = type.indexOf('.');
			if (idx > -1) {
				value = type.substring(0, idx);
				type = value;
			}
		}

		// if (type.equals(VW_BIZ))
		// return new BizView();
		// if (type.equals("st"))
		// return new SmartyView(value);
		if (type.equals(UIModelView.VIEW_TYPE)) {
			return UIModelView.make();
		}

		return super.make(ioc, t, v);
	}
}
