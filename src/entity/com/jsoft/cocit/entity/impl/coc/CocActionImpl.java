package com.jsoft.cocit.entity.impl.coc;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.TreeEntity;
import com.jsoft.cocit.entity.coc.IExtCocAction;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
@Entity
@CocEntity(name = "定义实体操作", key = Const.TBL_COC_ACTION, sn = 3, dbEncoding = true, uniqueFields = Const.FLIST_COC_KEYS, indexFields = Const.FLIST_COC_KEYS//
           , actions = {
                   //
                   @CocAction(name = "添加", opCode = OpCodes.OP_INSERT_FORM_DATA, key = "c"),//
                   @CocAction(name = "编辑", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "e"), //
                   @CocAction(name = "删除", opCode = OpCodes.OP_DELETE_ROWS, key = "d"), //
                   @CocAction(name = "查看", opCode = OpCodes.OP_UPDATE_FORM_DATA, key = "v") //
           }// end actions
           , groups = {
           //
           @CocGroup(name = "基本信息", key = "basic", fields = {
                   //
                   @CocColumn(name = "实体对象", field = "cocEntityKey", fkTargetEntity = Const.TBL_COC_ENTITY, fkTargetAsParent = true, asFilterNode = true),//
                   @CocColumn(name = "操作名称", field = "name"),//
                   @CocColumn(name = "操作编码", field = "key"),//
                   @CocColumn(name = "操作码", field = "opCode"), //
                   @CocColumn(name = "窗口高度", field = "uiWindowHeight"),//
                   @CocColumn(name = "窗口宽度", field = "uiWindowWidth"),//
                   @CocColumn(name = "业务插件", field = "plugin"), //
                   @CocColumn(name = "表单UI", field = "uiForm"), //
                   @CocColumn(name = "表单UI目标", field = "uiFormTarget"), //
                   @CocColumn(name = "父操作", field = "parentKey"),//
                   @CocColumn(name = "序号", field = "sn"), //
           }) // end group
           }// end groups
)
public class CocActionImpl extends TreeEntity implements IExtCocAction {
	@Column(length = 64)
	protected String cocEntityKey;
	protected String uiForm;
	protected String uiFormUrl;
	@Column(length = 16)
	protected String uiFormTarget;
	protected Integer opCode;
	protected String logo;
	protected String btnImage;
	@Column(length = 255)
	protected String plugin;
	@Column(length = 255, name = "title_")
	protected String title;
	@Column(length = 512)
	protected String defaultValuesRule;
	@Column(length = 512)
	protected String assignValuesRule;
	@Column(length = 512)
	protected String whereRule;
	protected String proxyActions;
	@Column(length = 255)
	protected String successMessage;
	@Column(length = 255)
	protected String errorMessage;
	@Column(length = 255)
	protected String warnMessage;
	protected int uiWindowHeight;
	protected int uiWindowWidth;

	protected void toJson(StringBuffer sb) {
		super.toJson(sb);

		this.toJson(sb, "cocEntityKey", cocEntityKey);
		this.toJson(sb, "uiView", uiForm);
		this.toJson(sb, "uiViewTarget", uiFormTarget);
		this.toJson(sb, "opCode", opCode);
		this.toJson(sb, "fullName", title);
		this.toJson(sb, "plugin", plugin);
		this.toJson(sb, "logo", logo);
		this.toJson(sb, "btnImage", btnImage);
		this.toJson(sb, "defaultValuesRule", defaultValuesRule);
		this.toJson(sb, "valuesRule", assignValuesRule);
		this.toJson(sb, "whereRule", whereRule);
		this.toJson(sb, "successMessage", successMessage);
		this.toJson(sb, "errorMessage", errorMessage);
		this.toJson(sb, "warnMessage", warnMessage);
	}

	public void release() {
		super.release();

		this.cocEntityKey = null;
		this.uiFormTarget = null;
		this.opCode = null;
		this.plugin = null;
		this.logo = null;
		this.btnImage = null;
		this.defaultValuesRule = null;
		this.uiForm = null;
		this.successMessage = null;
		this.errorMessage = null;
		this.warnMessage = null;
		this.title = null;
	}

	public Integer getOpCode() {
		return opCode;
	}

	// public String getMode() {
	// return mode;
	// }

	public String getPlugin() {
		return plugin;
	}

	public String getLogo() {
		return logo;
	}

	public String getBtnImage() {
		return btnImage;
	}

	public void setOpCode(Integer opCcode) {
		this.opCode = opCcode;
	}

	// public void setMode(String mode) {
	// this.mode = mode;
	// }

	public void setPlugin(String plugin) {
		this.plugin = plugin;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public void setBtnImage(String image) {
		this.btnImage = image;
	}

	public String getUiForm() {
		return uiForm;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getWarnMessage() {
		return warnMessage;
	}

	public void setUiForm(String template) {
		this.uiForm = template;
	}

	public void setSuccessMessage(String successInfo) {
		this.successMessage = successInfo;
	}

	public void setErrorMessage(String errorInfo) {
		this.errorMessage = errorInfo;
	}

	public void setWarnMessage(String warnInfo) {
		this.warnMessage = warnInfo;
	}

	public String getUiFormTarget() {
		return uiFormTarget;
	}

	public void setUiFormTarget(String targetWindow) {
		this.uiFormTarget = targetWindow;
	}

	public void setDefaultValuesRule(String params) {
		this.defaultValuesRule = params;
	}

	public String getCocEntityKey() {
		return cocEntityKey;
	}

	public void setCocEntityKey(String moduleKey) {
		this.cocEntityKey = moduleKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String fullName) {
		this.title = fullName;
	}

	public String getAssignValuesRule() {
		return assignValuesRule;
	}

	public void setAssignValuesRule(String valuesRule) {
		this.assignValuesRule = valuesRule;
	}

	public String getWhereRule() {
		return whereRule;
	}

	public void setWhereRule(String whereRule) {
		this.whereRule = whereRule;
	}

	public String getDefaultValuesRule() {
		return defaultValuesRule;
	}

	public String getUiFormUrl() {
		return uiFormUrl;
	}

	public void setUiFormUrl(String uiFormUrl) {
		this.uiFormUrl = uiFormUrl;
	}

	public String getProxyActions() {
		return proxyActions;
	}

	public void setProxyActions(String proxyActions) {
		this.proxyActions = proxyActions;
	}

	public int getUiWindowHeight() {
		return uiWindowHeight;
	}

	public void setUiWindowHeight(int uiWindowHeight) {
		this.uiWindowHeight = uiWindowHeight;
	}

	public int getUiWindowWidth() {
		return uiWindowWidth;
	}

	public void setUiWindowWidth(int uiWindowWidth) {
		this.uiWindowWidth = uiWindowWidth;
	}

}
