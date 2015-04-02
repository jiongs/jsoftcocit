package com.jsoft.cocit.entity.impl.coc;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.coc.IExtCocEntity;
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
@CocEntity(name = "定义实体对象", key = Const.TBL_COC_ENTITY, sn = 2, uniqueFields = Const.F_KEY, indexFields = Const.F_KEY, dbEncoding = true//
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
                   @CocColumn(name = "实体名称", field = "name"),//
                   @CocColumn(name = "实体编号", field = "key"),//
                   @CocColumn(name = "实体分类", field = "catalogKey", fkTargetAsGroup = true, fkTargetEntity = Const.TBL_COC_CATALOG, asFilterNode = true), //
                   @CocColumn(name = "实体JAVA类", field = "className"), //
                   @CocColumn(name = "实体扩展类", field = "extendsClassName"),//
                   @CocColumn(name = "实体数据表", field = "tableName"),//
                   @CocColumn(name = "排序表达式", field = "sortExpr"), //
                   @CocColumn(name = "模块UI", field = "uiView"), //
                   @CocColumn(name = "流程业务", field = "workflow"), //
                   @CocColumn(name = "序号", field = "sn"), //
           }) // end group
           }// end groups
)
public class CocEntityImpl extends NamedEntity implements IExtCocEntity {

	@Column(length = 64)
	protected String catalogKey;
	@Column(length = 64)
	protected String catalogName;
	@Column(length = 512)
	protected String sortExpr;
	@Column(length = 64)
	protected String tableName;
	@Column(length = 128)
	protected String className;
	@Column(length = 64)
	protected String uiView;
	@Column(length = 64)
	protected String pathPrefix;
	@Column(length = 64)
	protected String compileVersion;
	@Column(length = 128)
	protected String extendsClassName;
	@Column(length = 256)
	protected String uniqueFields;
	@Column(length = 256)
	protected String indexFields;
	@Column(length = 256)
	protected String dataAuthFields;
	private boolean workflow;

	protected void toJson(StringBuffer sb) {
		super.toJson(sb);

		this.toJson(sb, "catalogKey", catalogKey);
		this.toJson(sb, "catalogName", catalogName);
		this.toJson(sb, "sortExpr", sortExpr);
		this.toJson(sb, "tableName", tableName);
		this.toJson(sb, "className", className);
		this.toJson(sb, "extendsClassName", extendsClassName);
		// this.toJson(sb, "uiType", uiType);
		this.toJson(sb, "uiView", uiView);
		this.toJson(sb, "pathPrefix", pathPrefix);
		this.toJson(sb, "compileVersion", compileVersion);
		this.toJson(sb, "uniqueFields", uniqueFields);
		this.toJson(sb, "indexFields", indexFields);
	}

	public void release() {
		super.release();

		this.catalogKey = null;
		this.catalogName = null;
		this.sortExpr = null;
		this.tableName = null;
		this.className = null;
		this.extendsClassName = null;
		// this.uiType = 0;
		this.uiView = null;
		this.pathPrefix = null;
		this.compileVersion = null;
		this.uniqueFields = null;
		this.indexFields = null;
	}

	public String getCatalogKey() {
		return catalogKey;
	}

	public void setCatalogKey(String entityCatalogKey) {
		this.catalogKey = entityCatalogKey;
	}

	public String getSortExpr() {
		return sortExpr;
	}

	public void setSortExpr(String dataSortExpr) {
		this.sortExpr = dataSortExpr;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String dataTableName) {
		this.tableName = dataTableName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String entityClassName) {
		this.className = entityClassName;
	}

	// public byte getUiType() {
	// return uiType;
	// }
	//
	// public void setUiType(byte uiType) {
	// this.uiType = uiType;
	// }

	public String getUiView() {
		return uiView;
	}

	public void setUiView(String uiTemplate) {
		this.uiView = uiTemplate;
	}

	public String getPathPrefix() {
		return pathPrefix;
	}

	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getCompileVersion() {
		return compileVersion;
	}

	public void setCompileVersion(String compilerVersion) {
		this.compileVersion = compilerVersion;
	}

	public String getExtendsClassName() {
		return extendsClassName;
	}

	public void setExtendsClassName(String extendsClassName) {
		this.extendsClassName = extendsClassName;
	}

	public String getUniqueFields() {
		return uniqueFields;
	}

	public String getIndexFields() {
		return indexFields;
	}

	public void setUniqueFields(String uniqueFields) {
		this.uniqueFields = uniqueFields;
	}

	public void setIndexFields(String indexFields) {
		this.indexFields = indexFields;
	}

	public String getDataAuthFields() {
		return dataAuthFields;
	}

	public void setDataAuthFields(String dataAuthFields) {
		this.dataAuthFields = dataAuthFields;
	}

	public boolean isWorkflow() {
		return workflow;
	}

	public void setWorkflow(boolean workflow) {
		this.workflow = workflow;
	}

}
