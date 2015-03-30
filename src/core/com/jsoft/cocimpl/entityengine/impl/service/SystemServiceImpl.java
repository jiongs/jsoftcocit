package com.jsoft.cocimpl.entityengine.impl.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.entity.security.ISystem;
import com.jsoft.cocit.entity.security.ISystemMenu;
import com.jsoft.cocit.entityengine.service.SystemMenuService;
import com.jsoft.cocit.entityengine.service.SystemPreferenceService;
import com.jsoft.cocit.entityengine.service.SystemService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.securityengine.AuthorizedObject;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.SortUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class SystemServiceImpl extends NamedEntityServiceImpl<ISystem> implements SystemService {

	private List<SystemMenuService> systemMenus;

	private Map<Serializable, SystemMenuService> systemMenuMap;

	private Map<String, SystemPreferenceService> configs;

	protected SystemServiceImpl(ISystem obj) {
		super(obj);
	}

	@Override
	public void release() {
		super.release();

		if (systemMenus != null) {
			for (SystemMenuService o : systemMenus) {
				o.release();
			}
			systemMenus.clear();
			systemMenus = null;
		}
		if (systemMenuMap != null) {
			systemMenuMap.clear();
			systemMenuMap = null;
		}
		if (configs != null) {
			for (SystemPreferenceService v : configs.values()) {
				v.release();
			}
			configs.clear();
			configs = null;
		}
	}

	private void initSystemMenus() {
		if (this.entityData == null) {
			throw new CocException("访问系统失败！请重新登录试试！");
		}
		synchronized (this.entityData) {
			if (systemMenus == null || systemMenus.size() == 0) {

				/*
				 * 加载数据
				 */
				CndExpr expr = ExprUtil.systemIs(entityData.getKey());
				List<ISystemMenu> menus = (List<ISystemMenu>) Cocit.me().getProxiedORM().query(EntityTypes.SystemMenu, expr);
				if (menus != null) {
					systemMenuMap = new HashMap();
					systemMenus = new ArrayList();

					for (ISystemMenu menu : menus) {
						SystemMenuService menuService = new SystemMenuServiceImpl(menu, this);

						systemMenuMap.put(menuService.getId(), menuService);
						systemMenuMap.put(menuService.getKey(), menuService);

						systemMenus.add(menuService);
					}

					SortUtil.sort(systemMenus, Const.F_SN, true);
				}
			}
		}
	}

	private void initConfigs() {
		configs = new HashMap();
		// TODO
	}

	public SystemMenuService getSystemMenu(Serializable systemMenuID) {
		if (systemMenus == null || systemMenus.size() == 0) {
			initSystemMenus();
		}

		return systemMenuMap.get(systemMenuID);
	}

	public <T> T getConfigItem(String key, T defaultValue) {
		if (configs == null) {
			this.initConfigs();
		}
		SystemPreferenceService sps = configs.get(key);
		if (sps != null) {
			return sps.get(defaultValue);
		} else {
			String systemKey = this.getKey();
			String config = Cocit.me().getConfig().get(key + "." + systemKey);
			if (config != null) {
				return StringUtil.castTo(config, defaultValue);
			}
		}

		return defaultValue;
	}

	public SystemMenuService getSystemMenuByModule(String moduleKey) {
		if (systemMenus == null) {
			initSystemMenus();
		}

		for (SystemMenuService menu : systemMenus) {
			if (moduleKey.equals(menu.getRefEntity())) {
				return menu;
			}
		}

		return null;
	}

	@Override
	public AuthorizedObject[] getAuthorizedObjects() {
		this.initSystemMenus();

		return (AuthorizedObject[]) systemMenus.toArray();
	}

	public List<SystemMenuService> getSystemMenus() {
		this.initSystemMenus();
		return systemMenus;
	}
}
