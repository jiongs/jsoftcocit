package com.jsoft.cocit.entity.impl.coc;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.OpCodes;
import com.jsoft.cocit.entity.NamedEntity;
import com.jsoft.cocit.entity.coc.IExtCocField;
import com.jsoft.cocit.entityengine.annotation.CocAction;
import com.jsoft.cocit.entityengine.annotation.CocColumn;
import com.jsoft.cocit.entityengine.annotation.CocEntity;
import com.jsoft.cocit.entityengine.annotation.CocGroup;

/**
 * 表单字段显示模式
 * <p>
 * 字段显示模式：用空格分隔，与子系统数据操作中指定的动作模式组合使用。
 * <p>
 * M: Must 必需的
 * <p>
 * E: Edit 可编辑的 (即可读写)
 * <p>
 * I: Inspect 检查（带有一个隐藏字段存放其值）
 * <p>
 * S: Show 显示（但不带隐藏字段）
 * <p>
 * N: None 不显示
 * <p>
 * P: Present 如果该字段有值就显示，否则如果没有值就不显示该字段
 * <p>
 * H: Hidden 隐藏 (不显示，但有一个隐藏框存在)
 * <p>
 * R: Read only 只读
 * <p>
 * D: Disable 禁用
 * <p>
 * 举例说明：
 * <p>
 * v:I——查看数据时，该字段处于检查模式
 * <p>
 * e:E——编辑数据时，字段可编辑
 * <p>
 * bu:N——批量修改数据时，字段不可见
 * 
 * @author Ji Yongshan
 * @preserve all
 * 
 */
@Entity
@CocEntity(name = "定义实体字段", key = Const.TBL_COC_FIELD, sn = 5, uniqueFields = Const.FLIST_COC_KEYS, indexFields = Const.FLIST_COC_KEYS, dbEncoding = true//
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
                   @CocColumn(name = "实体对象", field = "cocEntityKey", fkTargetEntity = Const.TBL_COC_ENTITY, fkTargetAsParent = true, uiView = "combotree", asFilterNode = true),//
                   @CocColumn(name = "字段名称", field = "name"),//
                   @CocColumn(name = "JAVA属性", field = "key"),//
                   @CocColumn(name = "数据表列名", field = "dbColumnName"),//
                   @CocColumn(name = "数据表列定义", field = "dbColumnDefinition"),//
                   @CocColumn(name = "显示模式", field = "mode"),//
                   @CocColumn(name = "文本长度", field = "length"),//
                   @CocColumn(name = "校验规则", field = "pattern"),//
                   @CocColumn(name = "数字精度", field = "precision"),//
                   @CocColumn(name = "数字小数位", field = "scale"),//
                   @CocColumn(name = "Grid列", field = "asGridColumn", dicOptions = "1:是,0:不是"),//
                   @CocColumn(name = "Grid列顺序", field = "gridColumnSn"),//
                   @CocColumn(name = "Grid列宽度", field = "gridColumnWidth"),//
                   @CocColumn(name = "字段UI", field = "uiView", asGridColumn = false),//
                   @CocColumn(name = "过滤器节点", field = "asFilterNode", dicOptions = "1:是,0:不是"), //
                   @CocColumn(name = "字典选项", field = "dicOptions"), //
                   @CocColumn(name = "Many字段目标实体", field = "manyTargetEntityKey"), //
                   @CocColumn(name = "外键目标实体", field = "fkTargetEntityKey", fkTargetEntity = Const.TBL_COC_ENTITY),//
                   @CocColumn(name = "外键目标字段", field = "fkTargetFieldKey"),//
                   @CocColumn(name = "外键目标分组", field = "fkTargetAsGroup", dicOptions = "1:是,0:不是"),//
                   @CocColumn(name = "序号", field = "sn"),//
                   @CocColumn(name = "UI级联", field = "uiCascading"),//
                   @CocColumn(name = "临时字段", field = "isTransient"),//
                   @CocColumn(name = "字段分组", field = "cocGroupKey", fkTargetEntity = Const.TBL_COC_GROUP),//
           }) // end group
           }// end groups
)
// 为避免多实体一个表的情况，现将SystemData改为AbstractSystemData，从而避免DTYPE字段。
public class CocFieldImpl extends NamedEntity implements IExtCocField {

	@Column(length = 50)
	protected String cocEntityKey;// 逻辑外键：用来描述字段所属的实体模块
	@Column(length = 50)
	protected String cocGroupKey;// 逻辑外键：用来描述字段所属的组
	protected int fieldType;// 字段类型
	@Column(length = 50)
	protected String manyTargetEntityKey;// 逻辑外键：用来描述“OneToMany字段”关联的目标实体
	@Column(length = 50)
	protected String fkTargetEntityKey;// 逻辑外键：用来描述“外键字段”关联的目标实体
	@Column(length = 50)
	protected String fkTargetFieldKey;// 逻辑外键：用来描述“外键字段”关联的目标字段
	@Column(length = 50)
	protected String fkDependFieldKey;// 逻辑外键：用来描述“冗余外键”的依赖字段，依赖于哪个真正的“外键字段”
	protected boolean fkTargetAsParent;
	protected boolean fkTargetAsGroup;
	@Column(name = "_length")
	protected int length;
	@Column(name = "_precision")
	protected Integer precision;
	@Column(name = "_scale")
	protected Integer scale;
	@Column(length = 255)
	protected String dicOptions;
	@Column(length = 255)
	protected String mode;
	protected String uomOptions;
	protected String uomOption;
	protected String pattern;
	@Column(length = 255)
	protected String uiCascading;
	@Column(length = 255)
	protected String dataCascading;
	protected String uiView;
	protected boolean asGridColumn;
	protected Integer gridColumnSn;
	protected Integer gridColumnWidth;
	protected boolean asFilterNode;
	protected String dbColumnName;
	@Column(length = 255)
	protected String dbColumnDefinition;
	protected boolean dbColumnNotNull;
	protected boolean multiSelect;
	@Column(length = 255)
	protected String defaultValue;
	@Column(length = 128)
	protected String generator;
	protected boolean isTransient;
	@Column(length = 255)
	private String fkComboWhere;
	@Column(length = 255)
	private String fkComboUrl;

	protected void toJson(StringBuffer sb) {
		super.toJson(sb);

		this.toJson(sb, "cocEntityKey", cocEntityKey);
		this.toJson(sb, "cocGroupKey", cocGroupKey);
		this.toJson(sb, "fieldType", fieldType);
		this.toJson(sb, "fkTargetEntityKey", fkTargetEntityKey);
		this.toJson(sb, "fkTargetFieldKey", fkTargetFieldKey);
		this.toJson(sb, "fkTargetAsParent", fkTargetAsParent);
		this.toJson(sb, "fkTargetAsGroup", fkTargetAsGroup);
		this.toJson(sb, "length", length);
		this.toJson(sb, "precision", precision);
		this.toJson(sb, "scale", scale);
		this.toJson(sb, "dicOptions", dicOptions);
		this.toJson(sb, "mode", mode);
		this.toJson(sb, "pattern", pattern);
		this.toJson(sb, "dbColumnName", dbColumnName);
		this.toJson(sb, "uiCascading", uiCascading);
		this.toJson(sb, "dataCascading", dataCascading);
		this.toJson(sb, "uiView", uiView);
		this.toJson(sb, "asGridColumn", asGridColumn);
		this.toJson(sb, "gridColumnSn", gridColumnSn);
		this.toJson(sb, "gridColumnWidth", gridColumnWidth);
		this.toJson(sb, "asFilterNode", asFilterNode);
		this.toJson(sb, "dbColumnDefinition", dbColumnDefinition);
		this.toJson(sb, "multipleValue", multiSelect);
		this.toJson(sb, "defaultValue", defaultValue);
		this.toJson(sb, "isTransient", isTransient);
	}

	public void release() {
		super.release();

		this.cocEntityKey = null;
		this.cocGroupKey = null;
		this.fieldType = 0;
		this.fkTargetEntityKey = null;
		this.fkTargetFieldKey = null;
		this.fkDependFieldKey = null;
		this.fkTargetAsParent = false;
		this.fkTargetAsGroup = false;
		this.length = 0;
		this.precision = null;
		this.scale = null;
		this.dicOptions = null;
		this.mode = null;
		this.uomOptions = null;
		this.uomOption = null;
		this.pattern = null;
		this.uiCascading = null;
		this.dataCascading = null;
		this.uiView = null;
		this.asGridColumn = false;
		this.gridColumnSn = null;
		this.gridColumnWidth = null;
		this.asFilterNode = false;
		this.dbColumnName = null;
		this.dbColumnDefinition = null;
		this.multiSelect = false;
		this.defaultValue = null;
		this.isTransient = false;
	}

	@Override
	public String getCocEntityKey() {
		return cocEntityKey;
	}

	@Override
	public void setCocEntityKey(String moduleKey) {
		this.cocEntityKey = moduleKey;
	}

	@Override
	public String getCocGroupKey() {
		return cocGroupKey;
	}

	@Override
	public void setCocGroupKey(String groupKey) {
		this.cocGroupKey = groupKey;
	}

	@Override
	public int getFieldType() {
		return fieldType;
	}

	@Override
	public void setFieldType(int dataType) {
		this.fieldType = dataType;
	}

	@Override
	public String getFkTargetEntityKey() {
		return fkTargetEntityKey;
	}

	@Override
	public void setFkTargetEntityKey(String fkModuleKey) {
		this.fkTargetEntityKey = fkModuleKey;
	}

	@Override
	public String getFkTargetFieldKey() {
		return fkTargetFieldKey;
	}

	@Override
	public void setFkTargetFieldKey(String fkEntityFieldKey) {
		this.fkTargetFieldKey = fkEntityFieldKey;
	}

	@Override
	public boolean isMultiSelect() {
		return multiSelect;
	}

	@Override
	public Integer getScale() {
		return scale;
	}

	@Override
	public void setScale(Integer scale) {
		this.scale = scale;
	}

	@Override
	public Integer getPrecision() {
		return precision;
	}

	@Override
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	@Override
	public String getMode() {
		return mode;
	}

	@Override
	public void setMode(String mode) {
		this.mode = mode;
	}

	@Override
	public String getUomOptions() {
		return uomOptions;
	}

	@Override
	public void setUomOptions(String uomOptions) {
		this.uomOptions = uomOptions;
	}

	@Override
	public String getUomOption() {
		return uomOption;
	}

	@Override
	public void setUomOption(String uomOption) {
		this.uomOption = uomOption;
	}

	@Override
	public String getDicOptions() {
		return dicOptions;
	}

	@Override
	public void setDicOptions(String options) {
		this.dicOptions = options;
	}

	// public String getRegexpMask() {
	// return regexpMask;
	// }
	//
	// public void setRegexpMask(String regexpMask) {
	// this.regexpMask = regexpMask;
	// }

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String getPattern() {
		return pattern;
	}

	@Override
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public String getFieldName() {
		return getKey();
	}

	@Override
	public void setFieldName(String field) {
		setKey(field);
	}

	// public String getUploadType() {
	// return uploadType;
	// }
	//
	// public void setUploadType(String uploadType) {
	// this.uploadType = uploadType;
	// }

	@Override
	public boolean isTransient() {
		return isTransient;
	}

	@Override
	public void setTransient(boolean transientField) {
		this.isTransient = transientField;
	}

	@Override
	public boolean isAsGridColumn() {
		return asGridColumn;
	}

	@Override
	public void setAsGridColumn(boolean gridField) {
		this.asGridColumn = gridField;
	}

	@Override
	public Integer getGridColumnSn() {
		return gridColumnSn;
	}

	@Override
	public void setGridColumnSn(Integer gridSN) {
		this.gridColumnSn = gridSN;
	}

	@Override
	public Integer getGridColumnWidth() {
		return gridColumnWidth;
	}

	@Override
	public void setGridColumnWidth(Integer gridWidth) {
		this.gridColumnWidth = gridWidth;
	}

	@Override
	public boolean isFkTargetAsParent() {
		return fkTargetAsParent;
	}

	@Override
	public void setFkTargetAsParent(boolean mappingToMaster) {
		this.fkTargetAsParent = mappingToMaster;
	}

	// public byte getUiType() {
	// return uiType;
	// }
	//
	// public void setUiType(byte uiType) {
	// this.uiType = uiType;
	// }

	@Override
	public String getUiView() {
		return uiView;
	}

	@Override
	public void setUiView(String uiTemplate) {
		this.uiView = uiTemplate;
	}

	// public boolean isPassword() {
	// return password;
	// }
	//
	// public void setPassword(boolean password) {
	// this.password = password;
	// }

	@Override
	public boolean isAsFilterNode() {
		return asFilterNode;
	}

	@Override
	public void setAsFilterNode(boolean disabledDimension) {
		this.asFilterNode = disabledDimension;
	}

	@Override
	public String getDataCascading() {
		return dataCascading;
	}

	@Override
	public void setDataCascading(String cascadeMode) {
		this.dataCascading = cascadeMode;
	}

	@Override
	public boolean isFkTargetAsGroup() {
		return fkTargetAsGroup;
	}

	@Override
	public void setFkTargetAsGroup(boolean groupBy) {
		this.fkTargetAsGroup = groupBy;
	}

	// public String getDicCatalogKey() {
	// return dicCatalogKey;
	// }
	//
	// public void setDicCatalogKey(String dictionaryKey) {
	// this.dicCatalogKey = dictionaryKey;
	// }

	@Override
	public void setMultiSelect(boolean multipleValue) {
		this.multiSelect = multipleValue;
	}

	@Override
	public String getUiCascading() {
		return uiCascading;
	}

	@Override
	public void setUiCascading(String uiCascading) {
		this.uiCascading = uiCascading;
	}

	@Override
	public String getDbColumnName() {
		return dbColumnName;
	}

	@Override
	public void setDbColumnName(String columnName) {
		this.dbColumnName = columnName;
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String getDbColumnDefinition() {
		return dbColumnDefinition;
	}

	@Override
	public void setDbColumnDefinition(String columnDefinition) {
		this.dbColumnDefinition = columnDefinition;
	}

	@Override
	public String getFkDependFieldKey() {
		return fkDependFieldKey;
	}

	@Override
	public void setFkDependFieldKey(String fkDependField) {
		this.fkDependFieldKey = fkDependField;
	}

	public String getManyTargetEntityKey() {
		return manyTargetEntityKey;
	}

	public void setManyTargetEntityKey(String manyTargetEntityKey) {
		this.manyTargetEntityKey = manyTargetEntityKey;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public boolean isDbColumnNotNull() {
		return dbColumnNotNull;
	}

	public void setDbColumnNotNull(boolean dbColumnNotNull) {
		this.dbColumnNotNull = dbColumnNotNull;
	}

	public String getFkComboWhere() {
		return fkComboWhere;
	}

	public void setFkComboWhere(String fkComboWhere) {
		this.fkComboWhere = fkComboWhere;
	}

	public String getFkComboUrl() {
		return fkComboUrl;
	}

	public void setFkComboUrl(String fkComboUrl) {
		this.fkComboUrl = fkComboUrl;
	}
}
