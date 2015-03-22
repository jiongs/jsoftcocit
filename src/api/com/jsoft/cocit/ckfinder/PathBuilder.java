package com.jsoft.cocit.ckfinder;

import javax.servlet.http.HttpServletRequest;

import com.ckfinder.connector.configuration.DefaultPathBuilder;
import com.ckfinder.connector.utils.PathUtils;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.util.MVCUtil;

public class PathBuilder extends DefaultPathBuilder {
	@Override
	public String getBaseUrl(final HttpServletRequest request) {
		String baseURL = Cocit.me().getContextPath() + MVCUtil.getUploadPath();

		return PathUtils.addSlashToBeginning(PathUtils.addSlashToEnd(baseURL));
	}

	@Override
	public String getBaseDir(final HttpServletRequest request) {
		return super.getBaseDir(request);
	}

}
