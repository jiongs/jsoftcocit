package com.jsoft.cocimpl.action;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.mvc.UIModelView;
import com.jsoft.cocit.ui.model.UIModel;
import com.jsoft.cocit.ui.model.datamodel.JSONModel;

@Ok(UIModelView.VIEW_TYPE)
@Fail(UIModelView.VIEW_TYPE)
public class AdminAction extends BaseAdminAction {

	protected static final String ADMIN_URL = "/admin";

	protected void init() {
		adminUrl = ADMIN_URL;

		Cocit coc = Cocit.me();
		ICocConfig config = coc.getConfig();

		resourcePath = config.get("admin.resourcePath", "/admin");
		topHeight = config.getInt("admin.topHeight", 98);
		bottomHeight = config.getInt("admin.bottomHeight", 0);
		frameGap = config.getInt("admin.frameGap", 5);
		leftWidth = config.getInt("admin.leftWidth", 240);
		bodyHGap = config.getInt("admin.bodyHGap", 10);
		bodyWGap = config.getInt("admin.bodyWGap", 5);
	}

	@At(ADMIN_URL + "/login/*")
	public UIModel login(String funcExpr) {
		return super.login(funcExpr);
	}

	@At(ADMIN_URL + "/logout/*")
	public JSONModel logout(String tenantKey) {
		return super.logout(tenantKey);
	}

	@At(ADMIN_URL)
	@Fail("redirect:" + ADMIN_URL + "/login")
	public UIModel admin() {
		return super.admin();
	}

	@At(ADMIN_URL + "/top")
	public UIModel top() throws CocException {
		return super.top();
	}

	@At(ADMIN_URL + "/left/*")
	@Fail("redirect:" + ADMIN_URL + "/login")
	public UIModel left(String parentMenuID) throws CocException {
		return super.left(parentMenuID);
	}

	@At(ADMIN_URL + "/body/*")
	public UIModel body() throws CocException {
		return super.body();
	}

	@At(ADMIN_URL + "/bottom/*")
	public UIModel bottom() throws CocException {
		return super.bottom();
	}

	@At(ADMIN_URL + "/changePassword/*")
	@Fail("redirect:" + ADMIN_URL + "/login")
	public UIModel changePassword(boolean isAjax) throws SecurityException {
		return super.changePassword(isAjax);
	}

}
