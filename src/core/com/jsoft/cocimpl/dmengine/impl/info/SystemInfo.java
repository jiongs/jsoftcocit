package com.jsoft.cocimpl.dmengine.impl.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.baseentity.config.ISystemPreferenceEntity;
import com.jsoft.cocit.baseentity.security.ISystemEntity;
import com.jsoft.cocit.baseentity.security.ISystemMenuEntity;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.constant.FieldNames;
import com.jsoft.cocit.dmengine.info.ISystemPreferenceInfo;
import com.jsoft.cocit.dmengine.info.ISystemMenuInfo;
import com.jsoft.cocit.dmengine.info.ISystemInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.orm.expr.Expr;
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
public class SystemInfo extends NamedEntityInfo<ISystemEntity> implements ISystemInfo {

	private List<ISystemMenuInfo> systemMenus;

	private Map<Serializable, ISystemMenuInfo> systemMenuMap;

	private Map<String, ISystemPreferenceInfo> configs;

	protected SystemInfo(ISystemEntity obj) {
		super(obj);
	}

	@Override
	public void release() {
		super.release();

		if (systemMenus != null) {
			for (ISystemMenuInfo o : systemMenus) {
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
			for (ISystemPreferenceInfo v : configs.values()) {
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
				CndExpr expr = ExprUtil.systemIs(entityData.getCode());
				// List<ISystemMenuEntity> menus = (List<ISystemMenuEntity>) Cocit.me().getProxiedORM().query(EntityTypes.SystemMenu, expr);
				List<ISystemMenuEntity> menus = (List<ISystemMenuEntity>) Cocit.me().orm().query(EntityTypes.SystemMenu, expr);
				if (menus != null) {
					systemMenuMap = new HashMap();
					systemMenus = new ArrayList();

					for (ISystemMenuEntity menu : menus) {
						ISystemMenuInfo menuService = new SecurityMenuInfo(menu, this);

						systemMenuMap.put(menuService.getId(), menuService);
						systemMenuMap.put(menuService.getCode(), menuService);

						systemMenus.add(menuService);
					}

					SortUtil.sort(systemMenus, Const.F_SN, true);
				}
			}
		}
	}

	private void initConfigs() {
		configs = new HashMap();

		CndExpr expr = Expr.eq(FieldNames.F_SYSTEM_CODE, entityData.getCode());
		// List<IPreferenceSystemEntity> list = (List<IPreferenceSystemEntity>) Cocit.me().getProxiedORM().query(EntityTypes.SystemPreference, expr);
		List<ISystemPreferenceEntity> list = (List<ISystemPreferenceEntity>) Cocit.me().orm().query(EntityTypes.SystemPreference, expr);
		for (ISystemPreferenceEntity config : list) {
			configs.put(config.getCode(), new Preference4SystemInfo(config, this));
		}
	}

	public ISystemMenuInfo getSystemMenu(Serializable systemMenuID) {
		if (systemMenus == null || systemMenus.size() == 0) {
			initSystemMenus();
		}

		return systemMenuMap.get(systemMenuID);
	}

	public <T> T getConfigItem(String key, T defaultValue) {
		if (configs == null) {
			this.initConfigs();
		}
		ISystemPreferenceInfo sps = configs.get(key);
		if (sps != null) {
			return sps.get(defaultValue);
		} else {
			String systemCode = this.getCode();
			String config = Cocit.me().getConfig().get(key + "." + systemCode);
			if (config != null) {
				return StringUtil.castTo(config, defaultValue);
			}
		}

		return defaultValue;
	}

	public ISystemMenuInfo getSystemMenuByModule(String moduleCode) {
		if (systemMenus == null) {
			initSystemMenus();
		}

		for (ISystemMenuInfo menu : systemMenus) {
			if (moduleCode.equals(menu.getRefEntity())) {
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

	public List<ISystemMenuInfo> getSystemMenus() {
		this.initSystemMenus();
		return systemMenus;
	}

	public Map<String, ISystemPreferenceInfo> getConfigs() {
		if (configs == null) {
			initConfigs();
		}
		return configs;
	}
}
