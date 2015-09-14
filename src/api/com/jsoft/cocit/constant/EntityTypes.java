package com.jsoft.cocit.constant;

import com.jsoft.cocit.baseentity.coc.ICocActionEntity;
import com.jsoft.cocit.baseentity.coc.ICocCatalogEntity;
import com.jsoft.cocit.baseentity.coc.ICocEntity;
import com.jsoft.cocit.baseentity.coc.ICocFieldEntity;
import com.jsoft.cocit.baseentity.coc.ICocGroupEntity;
import com.jsoft.cocit.baseentity.common.IFileInfoEntity;
import com.jsoft.cocit.baseentity.config.IDataSourceEntity;
import com.jsoft.cocit.baseentity.config.IDicEntity;
import com.jsoft.cocit.baseentity.config.IDicItemEntity;
import com.jsoft.cocit.baseentity.config.ISystemPreferenceEntity;
import com.jsoft.cocit.baseentity.config.ITenantPreferenceEntity;
import com.jsoft.cocit.baseentity.cui.ICuiEntity;
import com.jsoft.cocit.baseentity.cui.ICuiFormAction;
import com.jsoft.cocit.baseentity.cui.ICuiFormEntity;
import com.jsoft.cocit.baseentity.cui.ICuiFormFieldEntity;
import com.jsoft.cocit.baseentity.cui.ICuiGridEntity;
import com.jsoft.cocit.baseentity.cui.ICuiGridFieldEntity;
import com.jsoft.cocit.baseentity.log.ILogLoginEntity;
import com.jsoft.cocit.baseentity.log.ILogOperationEntity;
import com.jsoft.cocit.baseentity.log.ILogRunningEntity;
import com.jsoft.cocit.baseentity.security.IGroupEntity;
import com.jsoft.cocit.baseentity.security.IGroupMemberEntity;
import com.jsoft.cocit.baseentity.security.IPermissionEntity;
import com.jsoft.cocit.baseentity.security.IRoleEntity;
import com.jsoft.cocit.baseentity.security.IRoleMemberEntity;
import com.jsoft.cocit.baseentity.security.ISystemEntity;
import com.jsoft.cocit.baseentity.security.ISystemMenuEntity;
import com.jsoft.cocit.baseentity.security.ISystemUserEntity;
import com.jsoft.cocit.baseentity.security.ITenantEntity;

public abstract class EntityTypes {

	/*
	 * LOG
	 */
	public static Class<? extends IFileInfoEntity> FileInfo = null;
	public static Class<? extends ILogRunningEntity> RunningLog = null;
	public static Class<? extends ILogLoginEntity> LoginLog = null;
	public static Class<? extends ILogOperationEntity> OperationLog = null;

	/*
	 * COC 定义
	 */
	public static Class<? extends ICocCatalogEntity> CocCatalog = null;
	public static Class<? extends ICocEntity> CocEntity = null;
	public static Class<? extends ICocActionEntity> CocAction = null;
	public static Class<? extends ICocGroupEntity> CocGroup = null;
	public static Class<? extends ICocFieldEntity> CocField = null;

	/*
	 * CUI 定义
	 */
	public static Class<? extends ICuiEntity> CuiEntity = null;
	public static Class<? extends ICuiGridEntity> CuiGrid = null;
	public static Class<? extends ICuiGridFieldEntity> CuiGridField = null;
	public static Class<? extends ICuiFormEntity> CuiForm = null;
	public static Class<? extends ICuiFormFieldEntity> CuiFormField = null;
	public static Class<? extends ICuiFormAction> CuiFormAction = null;

	/*
	 * 权限
	 */
	public static Class<? extends ISystemEntity> System = null;
	public static Class<? extends ITenantEntity> Tenant = null;
	public static Class<? extends ISystemMenuEntity> SystemMenu = null;
	public static Class<? extends IRoleEntity> Role = null;
	public static Class<? extends IRoleMemberEntity> UserRole = null;
	public static Class<? extends IPermissionEntity> Authority = null;
	public static Class<? extends IGroupEntity> Group = null;
	public static Class<? extends IGroupMemberEntity> UserGroup = null;
	public static Class<? extends ISystemUserEntity> SystemUser = null;

	/*
	 * Config
	 */
	public static Class<? extends IDataSourceEntity> DataSource = null;
	public static Class<? extends IDicEntity> Dic = null;
	public static Class<? extends IDicItemEntity> DicItem = null;
	public static Class<? extends ISystemPreferenceEntity> SystemPreference = null;
	public static Class<? extends ITenantPreferenceEntity> TenantPreference = null;

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

		if (ICocActionEntity.class.isAssignableFrom(typeOfEntity)) {
			if (CocAction != null) {
				ret = 2;
			} else {
				CocAction = typeOfEntity;
			}
		} else if (ICocCatalogEntity.class.isAssignableFrom(typeOfEntity)) {
			if (CocCatalog != null) {
				ret = 2;
			} else {
				CocCatalog = typeOfEntity;
			}
		} else if (ICocFieldEntity.class.isAssignableFrom(typeOfEntity)) {
			if (CocField != null) {
				ret = 2;
			} else {
				CocField = typeOfEntity;
			}
		} else if (ICocGroupEntity.class.isAssignableFrom(typeOfEntity)) {
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
		} else if (ISystemEntity.class.isAssignableFrom(typeOfEntity)) {
			if (System != null) {
				ret = 2;
			} else {
				System = typeOfEntity;
			}
		} else if (IGroupEntity.class.isAssignableFrom(typeOfEntity)) {
			if (Group != null) {
				ret = 2;
			} else {
				Group = typeOfEntity;
			}
		} else if (IPermissionEntity.class.isAssignableFrom(typeOfEntity)) {
			if (Authority != null) {
				ret = 2;
			} else {
				Authority = typeOfEntity;
			}
		} else if (IRoleEntity.class.isAssignableFrom(typeOfEntity)) {
			if (Role != null) {
				ret = 2;
			} else {
				Role = typeOfEntity;
			}
		} else if (ISystemMenuEntity.class.isAssignableFrom(typeOfEntity)) {
			if (SystemMenu != null) {
				ret = 2;
			} else {
				SystemMenu = typeOfEntity;
			}
		} else if (ITenantEntity.class.isAssignableFrom(typeOfEntity)) {
			if (Tenant != null) {
				ret = 2;
			} else {
				Tenant = typeOfEntity;
			}
		} else if (ISystemUserEntity.class.isAssignableFrom(typeOfEntity)) {
			if (SystemUser != null) {
				ret = 2;
			} else {
				SystemUser = typeOfEntity;
			}
		} else if (IGroupMemberEntity.class.isAssignableFrom(typeOfEntity)) {
			if (UserGroup != null) {
				ret = 2;
			} else {
				UserGroup = typeOfEntity;
			}
		} else if (IRoleMemberEntity.class.isAssignableFrom(typeOfEntity)) {
			if (UserRole != null) {
				ret = 2;
			} else {
				UserRole = typeOfEntity;
			}
		} else if (IDataSourceEntity.class.isAssignableFrom(typeOfEntity)) {
			if (DataSource != null) {
				ret = 2;
			} else {
				DataSource = typeOfEntity;
			}
		} else if (IDicEntity.class.isAssignableFrom(typeOfEntity)) {
			if (Dic != null) {
				ret = 2;
			} else {
				Dic = typeOfEntity;
			}
		} else if (IDicItemEntity.class.isAssignableFrom(typeOfEntity)) {
			if (DicItem != null) {
				ret = 2;
			} else {
				DicItem = typeOfEntity;
			}
		} else if (ISystemPreferenceEntity.class.isAssignableFrom(typeOfEntity)) {
			if (SystemPreference != null) {
				ret = 2;
			} else {
				SystemPreference = typeOfEntity;
			}
		} else if (ITenantPreferenceEntity.class.isAssignableFrom(typeOfEntity)) {
			if (TenantPreference != null) {
				ret = 2;
			} else {
				TenantPreference = typeOfEntity;
			}
		} else if (ILogRunningEntity.class.isAssignableFrom(typeOfEntity)) {
			if (RunningLog != null) {
				ret = 2;
			} else {
				RunningLog = typeOfEntity;
			}
		} else if (ILogLoginEntity.class.isAssignableFrom(typeOfEntity)) {
			if (LoginLog != null) {
				ret = 2;
			} else {
				LoginLog = typeOfEntity;
			}
		} else if (ILogOperationEntity.class.isAssignableFrom(typeOfEntity)) {
			if (OperationLog != null) {
				ret = 2;
			} else {
				OperationLog = typeOfEntity;
			}
		} else if (IFileInfoEntity.class.isAssignableFrom(typeOfEntity)) {
			if (FileInfo != null) {
				ret = 2;
			} else {
				FileInfo = typeOfEntity;
			}
		} else if (ICuiEntity.class.isAssignableFrom(typeOfEntity)) {
			if (CuiEntity != null) {
				ret = 2;
			} else {
				CuiEntity = typeOfEntity;
			}
		} else if (ICuiFormEntity.class.isAssignableFrom(typeOfEntity)) {
			if (CuiForm != null) {
				ret = 2;
			} else {
				CuiForm = typeOfEntity;
			}
		} else if (ICuiFormFieldEntity.class.isAssignableFrom(typeOfEntity)) {
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
		} else if (ICuiGridEntity.class.isAssignableFrom(typeOfEntity)) {
			if (CuiGrid != null) {
				ret = 2;
			} else {
				CuiGrid = typeOfEntity;
			}
		} else if (ICuiGridFieldEntity.class.isAssignableFrom(typeOfEntity)) {
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
