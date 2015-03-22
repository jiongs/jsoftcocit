package com.jsoft.cocit.ui.model;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.util.StringUtil;

/**
 * 输出HTML/TEXT文本。
 * 
 * @author jiongsoft
 * 
 */
public class HTMLModel extends BaseUIModel {

	String content;

	public static HTMLModel make(String content) {
		HTMLModel ret = new HTMLModel();

		ret.content = content;

		return ret;
	}

	protected HTMLModel() {
		super();
	}

	public void release() {
		super.release();
		this.content = null;
	}

	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_HTML;

		return viewName;
	}

	public String getContentType() {
		return Const.CONTENT_TYPE_HTML;
	}

	public boolean isCachable() {
		return false;
	}

	public String getContent() {
		return content;
	}
}
