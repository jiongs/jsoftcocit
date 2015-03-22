package com.jsoft.cocit.constant;

import com.jsoft.cocit.entity.coc.ICocAction;
import com.jsoft.cocit.entity.coc.ICocCatalog;
import com.jsoft.cocit.entity.coc.ICocEntity;
import com.jsoft.cocit.entity.coc.ICocField;
import com.jsoft.cocit.entity.coc.ICocGroup;
import com.jsoft.cocit.entity.config.IDataSource;
import com.jsoft.cocit.entity.config.IDic;
import com.jsoft.cocit.entity.config.IDicItem;
import com.jsoft.cocit.entity.config.ISystemPreference;
import com.jsoft.cocit.entity.config.ITenantPreference;
import com.jsoft.cocit.entity.cui.ICuiEntity;
import com.jsoft.cocit.entity.cui.ICuiForm;
import com.jsoft.cocit.entity.cui.ICuiFormAction;
import com.jsoft.cocit.entity.cui.ICuiFormField;
import com.jsoft.cocit.entity.cui.ICuiGrid;
import com.jsoft.cocit.entity.cui.ICuiGridField;
import com.jsoft.cocit.entity.log.ILoginLog;
import com.jsoft.cocit.entity.log.IOperationLog;
import com.jsoft.cocit.entity.log.IRunningLog;
import com.jsoft.cocit.entity.log.IUploadLog;
import com.jsoft.cocit.entity.security.IGroup;
import com.jsoft.cocit.entity.security.IGroupMember;
import com.jsoft.cocit.entity.security.IAuthority;
import com.jsoft.cocit.entity.security.IRole;
import com.jsoft.cocit.entity.security.IRoleMember;
import com.jsoft.cocit.entity.security.ISystem;
import com.jsoft.cocit.entity.security.ISystemMenu;
import com.jsoft.cocit.entity.security.ISystemUser;
import com.jsoft.cocit.entity.security.ITenant;

public abstract class EntityTypes {

	/*
	 * LOG
	 */
	public static Class<? extends IUploadLog> UploadLog = null;
	public static Class<? extends IRunningLog> RunningLog = null;
	public static Class<? extends ILoginLog> LoginLog = null;
	public static Class<? extends IOperationLog> OperationLog = null;

	/*
	 * COC 定义
	 */
	public static Class<? extends ICocCatalog> CocCatalog = null;
	public static Class<? extends ICocEntity> CocEntity = null;
	public static Class<? extends ICocAction> CocAction = null;
	public static Class<? extends ICocGroup> CocGroup = null;
	public static Class<? extends ICocField> CocField = null;

	/*
	 * CUI 定义
	 */
	public static Class<? extends ICuiEntity> CuiEntity = null;
	public static Class<? extends ICuiGrid> CuiGrid = null;
	public static Class<? extends ICuiGridField> CuiGridField = null;
	public static Class<? extends ICuiForm> CuiForm = null;
	public static Class<? extends ICuiFormField> CuiFormField = null;
	public static Class<? extends ICuiFormAction> CuiFormAction = null;

	/*
	 * 权限
	 */
	public static Class<? extends ISystem> System = null;
	public static Class<? extends ITenant> Tenant = null;
	public static Class<? extends ISystemMenu> SystemMenu = null;
	public static Class<? extends IRole> Role = null;
	public static Class<? extends IRoleMember> UserRole = null;
	public static Class<? extends IAuthority> Authority = null;
	public static Class<? extends IGroup> Group = null;
	public static Class<? extends IGroupMember> UserGroup = null;
	public static Class<? extends ISystemUser> SystemUser = null;

	/*
	 * Config
	 */
	public static Class<? extends IDataSource> DataSource = null;
	public static Class<? extends IDic> Dic = null;
	public static Class<? extends IDicItem> DicItem = null;
	public static Class<? extends ISystemPreference> SystemPreference = null;
	public static Class<? extends ITenantPreference> TenantPreference = null;

	/**
	 * 
	 * @param cls
	 * @return 0：设置不成功！即指定的类不是平台预置的类；
	 *         <p>
	 *         1：设置成功！即指定的类是平台预置的类；
	 *         <p>
	 *         2：类已经存在！即指定的类是平台预置的类，但已经设置过了，不允许重复设置；
	 */
	public static int setupEntityType(Class typeOfEntity) {
		int ret = 1;

		if (ICocAction.class.isAssignableFrom(typeOfEntity)) {
			if (CocAction != null) {
				ret = 2;
			} else {
				CocAction = typeOfEntity;
			}
		} else if (ICocCatalog.class.isAssignableFrom(typeOfEntity)) {
			if (CocCatalog != null) {
				ret = 2;
			} else {
				CocCatalog = typeOfEntity;
			}
		} else if (ICocField.class.isAssignableFrom(typeOfEntity)) {
			if (CocField != null) {
				ret = 2;
			} else {
				CocField = typeOfEntity;
			}
		} else if (ICocGroup.class.isAssignableFrom(typeOfEntity)) {
			if (CocGroup != null) {
				ret = 2;
			} else {
				CocGroup = typeOfEntity;
			}
		} else if (ICocEntity.class.isAssignableFrom(typeOfEntity)) {
			if (CocEntity != null) {
				ret = 2;
			} else {
				CocEntity = typeOfEntity;
			}
		} else if (ISystem.class.isAssignableFrom(typeOfEntity)) {
			if (System != null) {
				ret = 2;
			} else {
				System = typeOfEntity;
			}
		} else if (IGroup.class.isAssignableFrom(typeOfEntity)) {
			if (Group != null) {
				ret = 2;
			} else {
				Group = typeOfEntity;
			}
		} else if (IAuthority.class.isAssignableFrom(typeOfEntity)) {
			if (Authority != null) {
				ret = 2;
			} else {
				Authority = typeOfEntity;
			}
		} else if (IRole.class.isAssignableFrom(typeOfEntity)) {
			if (Role != null) {
				ret = 2;
			} else {
				Role = typeOfEntity;
			}
		} else if (ISystemMenu.class.isAssignableFrom(typeOfEntity)) {
			if (SystemMenu != null) {
				ret = 2;
			} else {
				SystemMenu = typeOfEntity;
			}
		} else if (ITenant.class.isAssignableFrom(typeOfEntity)) {
			if (Tenant != null) {
				ret = 2;
			} else {
				Tenant = typeOfEntity;
			}
		} else if (ISystemUser.class.isAssignableFrom(typeOfEntity)) {
			if (SystemUser != null) {
				ret = 2;
			} else {
				SystemUser = typeOfEntity;
			}
		} else if (IGroupMember.class.isAssignableFrom(typeOfEntity)) {
			if (UserGroup != null) {
				ret = 2;
			} else {
				UserGroup = typeOfEntity;
			}
		} else if (IRoleMember.class.isAssignableFrom(typeOfEntity)) {
			if (UserRole != null) {
				ret = 2;
			} else {
				UserRole = typeOfEntity;
			}
		} else if (IDataSource.class.isAssignableFrom(typeOfEntity)) {
			if (DataSource != null) {
				ret = 2;
			} else {
				DataSource = typeOfEntity;
			}
		} else if (IDic.class.isAssignableFrom(typeOfEntity)) {
			if (Dic != null) {
				ret = 2;
			} else {
				Dic = typeOfEntity;
			}
		} else if (IDicItem.class.isAssignableFrom(typeOfEntity)) {
			if (DicItem != null) {
				ret = 2;
			} else {
				DicItem = typeOfEntity;
			}
		} else if (ISystemPreference.class.isAssignableFrom(typeOfEntity)) {
			if (SystemPreference != null) {
				ret = 2;
			} else {
				SystemPreference = typeOfEntity;
			}
		} else if (ITenantPreference.class.isAssignableFrom(typeOfEntity)) {
			if (TenantPreference != null) {
				ret = 2;
			} else {
				TenantPreference = typeOfEntity;
			}
		} else if (IRunningLog.class.isAssignableFrom(typeOfEntity)) {
			if (RunningLog != null) {
				ret = 2;
			} else {
				RunningLog = typeOfEntity;
			}
		} else if (ILoginLog.class.isAssignableFrom(typeOfEntity)) {
			if (LoginLog != null) {
				ret = 2;
			} else {
				LoginLog = typeOfEntity;
			}
		} else if (IOperationLog.class.isAssignableFrom(typeOfEntity)) {
			if (OperationLog != null) {
				ret = 2;
			} else {
				OperationLog = typeOfEntity;
			}
		} else if (IUploadLog.class.isAssignableFrom(typeOfEntity)) {
			if (UploadLog != null) {
				ret = 2;
			} else {
				UploadLog = typeOfEntity;
			}
		} else if (ICuiEntity.class.isAssignableFrom(typeOfEntity)) {
			if (CuiEntity != null) {
				ret = 2;
			} else {
				CuiEntity = typeOfEntity;
			}
		} else if (ICuiForm.class.isAssignableFrom(typeOfEntity)) {
			if (CuiForm != null) {
				ret = 2;
			} else {
				CuiForm = typeOfEntity;
			}
		} else if (ICuiFormField.class.isAssignableFrom(typeOfEntity)) {
			if (CuiFormField != null) {
				ret = 2;
			} else {
				CuiFormField = typeOfEntity;
			}
		} else if (ICuiFormAction.class.isAssignableFrom(typeOfEntity)) {
			if (CuiFormAction != null) {
				ret = 2;
			} else {
				CuiFormAction = typeOfEntity;
			}
		} else if (ICuiGrid.class.isAssignableFrom(typeOfEntity)) {
			if (CuiGrid != null) {
				ret = 2;
			} else {
				CuiGrid = typeOfEntity;
			}
		} else if (ICuiGridField.class.isAssignableFrom(typeOfEntity)) {
			if (CuiGridField != null) {
				ret = 2;
			} else {
				CuiGridField = typeOfEntity;
			}
		} else {
			ret = 0;
		}

		return ret;
	}

	// public static void clear() {
	// EntityAction = null;
	// EntityCatalog = null;
	// EntityField = null;
	// EntityFieldGroup = null;
	// EntityModule = null;
	// System = null;
	// Group = null;
	// GroupRole = null;
	// Permission = null;
	// Role = null;
	// SystemMenu = null;
	// Tenant = null;
	// SystemUser = null;
	// UserGroup = null;
	// UserRole = null;
	// DataSource = null;
	// Dic = null;
	// DicItem = null;
	// SystemPreference = null;
	// TenantPreference = null;
	// RunningLog = null;
	// LoginLog = null;
	// OperationLog = null;
	// }
}
