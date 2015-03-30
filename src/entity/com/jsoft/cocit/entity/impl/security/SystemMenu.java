package com.jsoft.cocit.entity.impl.security;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.IExtSystemOwnerEntity;
import com.jsoft.cocit.entity.TreeEntity;
import com.jsoft.cocit.entity.security.IExtSystemMenu;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;
import com.jsoft.cocit.entityengine.annotation.Cui;
import com.jsoft.cocit.entityengine.annotation.CuiEntity;
import com.jsoft.cocit.entityengine.annotation.CuiGrid;

/**
 * 系统菜单组成了系统
 */
@Entity
@CocEntity(name = "系统菜单管理", key = Const.TBL_SEC_SYSMENU, sn = 3, uniqueFields = "systemKey,key", indexFields = "systemKey,key",//
           actions = {
                   //
                   @CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c"), //
                   @CocAction(name = "修改", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_REMOVE_ROWS, key = "r"), //
                   @CocAction(name = "永久删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d") //
           },//
           groups = {
                   //
                   @CocGroup(name = "基本信息", key = "basic", fields = {
                           //
                           @CocColumn(name = "上级菜单", field = "parentKey", fkTargetEntity = Const.TBL_SEC_SYSMENU, uiView = "combotree"), //
                           @CocColumn(name = "上级菜单", field = "parentName", fkTargetEntity = Const.TBL_SEC_SYSMENU, fkTargetField = "name", fkDependField = "parentKey"), //
                           @CocColumn(name = "菜单名称", field = "name", mode = "c:M e:M"),//
                           @CocColumn(name = "菜单编码", field = "key", mode = "c:M e:M"),//
                           @CocColumn(name = "菜单序号", field = "sn", mode = "*:N v:P"),//
                           @CocColumn(name = "菜单类型", field = "type", mode = "c:M e:M", dicOptions = "90:分类菜单, 1:静态菜单, 2:实体菜单"), //
                           @CocColumn(name = "访问路径", field = "path"), //
                           @CocColumn(name = "菜单状态", field = "statusCode"),//
                           @CocColumn(name = "实体编号", field = "entityKey", fkTargetEntity = Const.TBL_COC_ENTITY, uiView = "combotree"), //
                           @CocColumn(name = "操作列表", field = "actions"),//
                           @CocColumn(name = "字段列表", field = "fields"),//
                           @CocColumn(name = "查询条件", field = "whereRule"),//
                           @CocColumn(name = "默认值", field = "defaultValuesRule"),//
                           @CocColumn(name = "引用界面", field = "uiView"),//
                           @CocColumn(name = "是否隐藏", field = "hidden"),//
                           @CocColumn(name = "菜单状态", field = "statusCode", uiView = "radio", dicOptions = "1:隐藏,0:正常,99990:初始数据"),//
                   }), //
                   @CocGroup(name = "其他属性设置", key = "other", fields = {
                           //
                           @CocColumn(name = "菜单徽标", field = "logo", pattern = "image"),//
                           @CocColumn(name = "菜单图片", field = "image", pattern = "image"), //
                   }) // end group
           }// end groups
)
@Cui({//
@CuiEntity(filterFields = "parentKey", queryFields = "name|entityKey",//
           grid = @CuiGrid(fields = "name|key|entityKey|actions|type|statusCode", rowActions = "e|v|r|d"//
           )) //
})
public class SystemMenu extends TreeEntity implements IExtSystemMenu, IExtSystemOwnerEntity {

	@Column(length = 64, nullable = false)
	protected String systemKey;
	@Column(length = 64)
	protected String dataSourceKey;
	@Column(length = 512)
	protected String path;
	@Column(length = 128)
	protected String logo;
	@Column(length = 128)
	protected String image;
	protected int type;
	@Column(length = 64)
	protected String entityKey;
	@Column(length = 128)
	protected String actions;
	@Column(length = 512)
	protected String fields;
	@Column(length = 512)
	protected String whereRule;
	@Column(length = 512)
	protected String defaultValuesRule;
	@Column(length = 256)
	protected String uiView;
	protected boolean hidden;

	@Transient
	protected String actionPermission;
	@Transient
	protected String fieldPermission;
	@Transient
	protected String dataPermission;

	@Transient
	protected List<SystemMenu> children;

	public String getSystemKey() {
		return systemKey;
	}

	public void setSystemKey(String systemKey) {
		this.systemKey = systemKey;
	}

	public String getDataSourceKey() {
		return dataSourceKey;
	}

	public void setDataSourceKey(String dataSourceKey) {
		this.dataSourceKey = dataSourceKey;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRefEntity() {
		return entityKey;
	}

	public void setRefEntity(String refEntity) {
		this.entityKey = refEntity;
	}

	// public String getEntityName() {
	// return entityModuleName;
	// }
	//
	// public void setEntityModuleName(String referencedName) {
	// this.entityModuleName = referencedName;
	// }

	public String getActions() {
		return actions;
	}

	public void setActions(String referencedActionsRule) {
		this.actions = referencedActionsRule;
	}

	public String getUiView() {
		return uiView;
	}

	public void setUiView(String uiKey) {
		this.uiView = uiKey;
	}

	// public String getPathPrefix() {
	// return pathPrefix;
	// }
	//
	// public void setPathPrefix(String pathPrefix) {
	// this.pathPrefix = pathPrefix;
	// }

	public String getFields() {
		return fields;
	}

	public void setFields(String entityFields) {
		this.fields = entityFields;
	}

	public String getWhereRule() {
		return whereRule;
	}

	public void setWhereRule(String rule) {
		this.whereRule = rule;
	}

	public String getDefaultValuesRule() {
		return defaultValuesRule;
	}

	public void setDefaultValuesRule(String defaultValuesRule) {
		this.defaultValuesRule = defaultValuesRule;
	}

	public String getFieldPermission() {
		return fieldPermission;
	}

	public void setFieldPermission(String fieldPermission) {
		this.fieldPermission = fieldPermission;
	}

	public String getDataPermission() {
		return dataPermission;
	}

	public void setDataPermission(String dataPermission) {
		this.dataPermission = dataPermission;
	}

	public String getActionPermission() {
		return actionPermission;
	}

	public void setActionPermission(String actionPermission) {
		this.actionPermission = actionPermission;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
