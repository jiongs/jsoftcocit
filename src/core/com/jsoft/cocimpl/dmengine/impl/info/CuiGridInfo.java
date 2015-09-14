package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.baseentity.cui.ICuiGridEntity;
import com.jsoft.cocit.baseentity.cui.ICuiGridFieldEntity;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.EntityTypes;
import com.jsoft.cocit.dmengine.info.ICuiEntityInfo;
import com.jsoft.cocit.dmengine.info.ICuiGridFieldInfo;
import com.jsoft.cocit.dmengine.info.ICuiGridInfo;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.ui.view.StyleRule;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.StringUtil;

public class CuiGridInfo extends DataEntityInfo<ICuiGridEntity> implements ICuiGridInfo {

	private ICuiEntityInfo cuiEntity;
	private Map<String, ICuiGridFieldInfo> cuiFieldMap;

	// private String sql;
	// private List sqlParams;

	CuiGridInfo(ICuiGridEntity obj, ICuiEntityInfo cuiEntity) {
		super(obj);
		this.cuiEntity = cuiEntity;
	}

	public void release() {
		super.release();
		this.cuiEntity = null;
		if (cuiFieldMap != null) {
			for (ICuiGridFieldInfo f : cuiFieldMap.values()) {
				f.release();
			}
			cuiFieldMap.clear();
			cuiFieldMap = null;
		}
	}

	@Override
	public ICuiGridFieldInfo getField(String fieldName) {
		if (cuiFieldMap == null) {
			cuiFieldMap = new HashMap();
			List<ICuiGridFieldEntity> fields = (List<ICuiGridFieldEntity>) orm().query(EntityTypes.CuiGridField, Expr.eq(Const.F_COC_ENTITY_CODE, this.getCocEntityCode())//
			        .and(Expr.eq(Const.F_CUI_ENTITY_CODE, this.getCuiEntityCode()))//
			        .and(Expr.eq(Const.F_CUI_GRID_CODE, this.getCode()))//
			        );
			if (fields != null) {
				for (ICuiGridFieldEntity field : fields) {
					cuiFieldMap.put(field.getCode(), new CuiGridFieldInfo(field, this));
				}
			}
		}
		return this.cuiFieldMap.get(fieldName);
	}

	@Override
	public List<String> getFrozenFieldsList() {
		return StringUtil.toList(this.getFrozenFields(), "|");
	}

	@Override
	public List<String> getFieldsList() {
		return StringUtil.toList(this.getFields(), "|");
	}

	@Override
	public ICuiEntityInfo getCuiEntity() {
		return this.cuiEntity;
	}

	@Override
	public String getCocEntityCode() {
		return this.entityData.getCocEntityCode();
	}

	@Override
	public String getCuiEntityCode() {
		return this.entityData.getCuiEntityCode();
	}

	@Override
	public String getFrozenFields() {
		return this.entityData.getFrozenFields();
	}

	@Override
	public String getFields() {
		return this.entityData.getFields();
	}

	@Override
	public boolean isRownumbers() {
		return this.entityData.isRownumbers();
	}

	@Override
	public boolean isMultiSelect() {
		return this.entityData.isMultiSelect();
	}

	@Override
	public boolean isSelectOnCheck() {
		return this.entityData.isSelectOnCheck();
	}

	@Override
	public boolean isCheckOnSelect() {
		return this.entityData.isCheckOnSelect();
	}

	@Override
	public byte getPaginationPos() {
		return this.entityData.getPaginationPos();
	}

	@Override
	public String getPaginationActions() {
		return this.entityData.getPaginationActions();
	}

	@Override
	public int getPageIndex() {
		return this.entityData.getPageIndex();
	}

	@Override
	public int getPageSize() {
		return this.entityData.getPageSize();
	}

	@Override
	public String getPageOptions() {
		return this.entityData.getPageOptions();
	}

	@Override
	public String getSortExpr() {
		return this.entityData.getSortExpr();
	}

	@Override
	public boolean isShowHeader() {
		return this.entityData.isShowHeader();
	}

	@Override
	public boolean isShowFooter() {
		return this.entityData.isShowFooter();
	}

	@Override
	public String getRowActions() {
		return this.entityData.getRowActions();
	}

	@Override
	public String getRowActionsView() {
		return this.entityData.getRowActionsView();
	}

	@Override
	public byte getRowActionsPos() {
		return this.entityData.getRowActionsPos();
	}

	@Override
	public String getTreeField() {
		return this.entityData.getTreeField();
	}

	@Override
	public String getRowStyleRule() {
		return this.entityData.getRowStyleRule();
	}

	@Override
	public StyleRule[] getRowStyles() {
		String strRule = this.getRowStyleRule();

		if (StringUtil.isBlank(strRule))
			return null;

		if (strRule.startsWith("[")) {
			return JsonUtil.loadFromJson(StyleRule[].class, strRule);
		} else if (strRule.startsWith("{")) {
			StyleRule rule = JsonUtil.loadFromJson(StyleRule.class, strRule);
			return new StyleRule[] { rule };
		} else {
			StyleRule rule = JsonUtil.loadFromJson(StyleRule.class, "{" + strRule + "}");
			return new StyleRule[] { rule };
		}
	}

	@Override
	public String getUiView() {
		return entityData.getUiView();
	}

	@Override
	public boolean isSingleRowEdit() {
		return entityData.isSingleRowEdit();
	}

	@Override
	public String getPrevInterceptors() {
		return entityData.getPrevInterceptors();
	}

	@Override
	public String getPostInterceptors() {
		return entityData.getPostInterceptors();
	}

	// @Override
	// public String getCommand() {
	// return entityData.getCommand();
	// }

	// @Override
	// public String getSQL() {
	// if (sql == null) {
	// parseCommand();
	// }
	//
	// return sql;
	// }
	//
	// @Override
	// public List<String> getSQLParams() {
	// if (sql == null) {
	// parseCommand();
	// }
	//
	// return sqlParams;
	// }

	// private void parseCommand() {
	// String command = getCommand();
	// StringBuffer sqlSB = new StringBuffer();
	// sqlParams = new ArrayList();
	// int from = command.indexOf("${");
	// int to = command.indexOf("}");
	//
	// while (from > -1 && to > -1) {
	// String str = command.substring(0, from);
	// String param = command.substring(from + 2, to);
	// sqlSB.append(str).append("?");
	// sqlParams.add(param);
	//
	// command = command.substring(to + 1);
	// from = command.indexOf("${");
	// to = command.indexOf("}");
	// }
	// sqlSB.append(command.substring(to + 1));
	//
	// sql = sqlSB.toString();
	// }

}
