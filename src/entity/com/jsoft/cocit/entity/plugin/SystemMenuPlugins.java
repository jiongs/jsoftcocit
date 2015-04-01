package com.jsoft.cocit.entity.plugin;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.entity.impl.security.SystemMenu;
import com.jsoft.cocit.entityengine.bizplugin.BizEvent;
import com.jsoft.cocit.entityengine.bizplugin.BizPlugin;

public abstract class SystemMenuPlugins {

	public static class c1 extends BizPlugin {

		@Override
		public void beforeSubmit(BizEvent event) {
			SystemMenu obj = (SystemMenu) event.getDataObject();
			obj.setSystemKey(Cocit.me().getHttpContext().getLoginSystemKey());
		}

	}
}
