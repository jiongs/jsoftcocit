package com.jsoft.cocit.entity.plugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.config.ICocConfig;
import com.jsoft.cocit.constant.PrincipalTypes;
import com.jsoft.cocit.entity.impl.security.AuthorityImpl;
import com.jsoft.cocit.entity.impl.security.SystemUser;
import com.jsoft.cocit.entityengine.bizplugin.BizEvent;
import com.jsoft.cocit.entityengine.bizplugin.BizPlugin;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.StringUtil;

public abstract class AuthorityPlugins {
	/**
	 * 添加用户权限
	 */
	public static class c extends BizPlugin {
		public void beforeSubmit(BizEvent event) {
			Orm orm = event.getOrm();

			ICocConfig config = Cocit.me().getConfig();

			Map<String, AuthorityImpl> oldMap = null;
			List<AuthorityImpl> list = (List<AuthorityImpl>) event.getDataObject();
			String userKey, groupKey, roleKey, menuKey, systemKey, dataRows;
			String cocSystem = config.getCocitSystemKey();
			for (AuthorityImpl auth : list) {
				userKey = auth.getUserKey();
				groupKey = auth.getGroupKey();
				roleKey = auth.getRoleKey();

				if (StringUtil.isBlank(userKey) && StringUtil.isBlank(groupKey) && StringUtil.isBlank(roleKey)) {
					throw new CocException("请先选择授权对象（用户、群组、角色）！");
				}

				menuKey = auth.getMenuKey();
				systemKey = auth.getSystemKey();
				// dataRows = auth.getDataRows();

				if (cocSystem.equals(systemKey)) {
					throw new CocException("您不能为“%s”授权！", config.getCocitSystemName());
				}

				if (oldMap == null) {
					List<AuthorityImpl> oldList = null;
					if (StringUtil.hasContent(userKey)) {
						oldList = orm.query(AuthorityImpl.class, Expr.eq("userKey", userKey).and(Expr.eq("systemKey", systemKey)));
					} else if (StringUtil.hasContent(groupKey)) {
						oldList = orm.query(AuthorityImpl.class, Expr.eq("groupKey", groupKey).and(Expr.eq("systemKey", systemKey)));
					} else if (StringUtil.hasContent(roleKey)) {
						oldList = orm.query(AuthorityImpl.class, Expr.eq("roleKey", groupKey).and(Expr.eq("systemKey", systemKey)));
					}
					if (oldList != null) {
						oldMap = new HashMap();
						for (AuthorityImpl old : oldList) {
							oldMap.put(old.getMenuKey(), old);
						}
					}
				}
				AuthorityImpl oldAuth = oldMap.get(menuKey);

				/*
				 * 权限已经存在：则修改它
				 */
				if (oldAuth != null) {
					oldMap.remove(menuKey);
					auth.setId(oldAuth.getId());
				}

				if (StringUtil.isBlank(userKey) && StringUtil.isBlank(groupKey)) {
					throw new CocException("权限主体(用户、群组、角色)不能为空！");
				}

				if (StringUtil.hasContent(userKey)) {
					SystemUser user = orm.get(SystemUser.class, userKey);
					List<String> roles = StringUtil.toList(user.getRoles());
					if (roles != null && roles.contains(PrincipalTypes.ROLE_ROOT)) {
						throw new CocException("用户(%s)已经拥有超级角色，不用再给他授予菜单权限！", user);
					}
				}
			}

			if (oldMap != null) {
				Iterator<AuthorityImpl> it = oldMap.values().iterator();
				while (it.hasNext()) {
					AuthorityImpl old = it.next();
					orm.delete(old);
				}
			}
		}
	}

	public static class d extends BizPlugin {
		public void beforeSubmit(BizEvent event) {

		}
	}
}
