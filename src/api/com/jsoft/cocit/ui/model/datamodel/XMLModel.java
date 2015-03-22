package com.jsoft.cocit.ui.model.datamodel;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.ViewNames;
import com.jsoft.cocit.ui.model.UIDataModel;
import com.jsoft.cocit.util.StringUtil;

/**
 * XML数据模型
 * 
 * @author jiongsoft
 * 
 */
public class XMLModel extends UIDataModel {
	String content;

	public static XMLModel make(String content) {
		XMLModel ret = new XMLModel();

		ret.content = content;

		return ret;
	}

	protected XMLModel() {
	}

	@Override
	public void release() {
		super.release();
		this.content = null;
	}

	public String getViewName() {
		if (StringUtil.isBlank(viewName))
			return ViewNames.VIEW_XML;

		return viewName;
	}

	public String getContentType() {
		return Const.CONTENT_TYPE_XML;
	}

	public String getContent() {
		return content;
	}

}
