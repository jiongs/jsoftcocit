// $codepro.audit.disable unnecessaryCast
package com.jsoft.cocimpl.dmengine.impl.info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.baseentity.coc.ICocActionEntity;
import com.jsoft.cocit.dmengine.bizplugin.IBizPlugin;
import com.jsoft.cocit.dmengine.info.ICocActionInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.expr.CndExpr;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ExprUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
public class CocActionInfo extends NamedEntityInfo<ICocActionEntity> implements ICocActionInfo {

	private ICocEntityInfo module;
	private List<IBizPlugin> plugins;

	CocActionInfo(ICocActionEntity obj, ICocEntityInfo module) {
		super(obj);
		this.module = module;
	}

	@Override
	public void release() {
		super.release();

		// this.module = null;
		if (plugins != null) {
			plugins.clear();
			plugins = null;
		}
	}

	public String getCocEntityCode() {
		return entityData.getCocEntityCode();
	}

	public String getTitle() {
		return entityData.getTitle();
	}

	public Integer getOpCode() {
		return entityData.getOpCode();
	}

	public String getPlugin() {
		return entityData.getPlugin();
	}

	public String getBtnImage() {
		return entityData.getBtnImage();
	}

	public String getLogo() {
		return entityData.getLogo();
	}

	public String getUiForm() {
		return entityData.getUiForm();
	}

	public String getSuccessMessage() {
		return entityData.getSuccessMessage();
	}

	public String getErrorMessage() {
		return entityData.getErrorMessage();
	}

	public String getWarnMessage() {
		return entityData.getWarnMessage();
	}

	//
	// public String getTargetUrl() {
	// return entity.getTargetUrl();
	// }

	public String getUiFormTarget() {
		return entityData.getUiFormTarget();
	}

	public String getDefaultValuesRule() {
		return entityData.getDefaultValuesRule();
	}

	public String getParentCode() {
		return entityData.getParentCode();
	}

//	public String getParentName() {
//		return entityData.getParentName();
//	}

	public ICocEntityInfo getEntity() {
		return module;
	}

	public IBizPlugin[] getBizPlugins() {
		if (plugins == null) {
			plugins = new ArrayList();
			String[] pluginArray = StringUtil.toArray(getPlugin(), ",");
			for (String pstr : pluginArray) {
				try {
					Object plugin = ClassUtil.forName(pstr).newInstance();
					if (plugin instanceof IBizPlugin) {
						plugins.add((IBizPlugin) plugin);
					} else {
						throw new CocException("业务插件必需实现IBizPlugin接口");
					}
				} catch (Throwable e) {
					plugins = null;
					throw new CocException("业务插件非法！%s", pstr);
				}
			}

		}
		IBizPlugin[] ret = new IBizPlugin[plugins.size()];
		int i = 0;
		for (IBizPlugin plugin : plugins) {
			ret[i++] = plugin;
		}

		return ret;
	}

	@Override
	public String getAssignValuesRule() {
		return entityData.getAssignValuesRule();
	}

	@Override
	public String getWhereRule() {
		return entityData.getWhereRule();
	}

	@Override
	public Map getDefaultValues() {
		return StringUtil.parseJsonToMap(getDefaultValuesRule());
	}

	@Override
	public Map getAssignValues() {
		return StringUtil.parseJsonToMap(getAssignValuesRule());
	}

	@Override
	public CndExpr getWhere() {
		return ExprUtil.parseToExpr(getWhereRule());
	}

	@Override
	public List<String> getProxyActionsList() {
		return StringUtil.toList(getProxyActions());
	}

	@Override
	public String getUiFormUrl() {
		return entityData.getUiFormUrl();
	}

	@Override
	public String getProxyActions() {
		return entityData.getProxyActions();
	}

	@Override
	public int getUiWindowHeight() {
		return entityData.getUiWindowHeight();
	}

	@Override
	public int getUiWindowWidth() {
		return entityData.getUiWindowWidth();
	}

	@Override
	public boolean isUiWindowHeader() {
		return entityData.isUiWindowHeader();
	}

	@Override
	public String getPrevInterceptors() {
		return entityData.getPrevInterceptors();
	}

	@Override
	public String getPostInterceptors() {
		return entityData.getPostInterceptors();
	}

}
