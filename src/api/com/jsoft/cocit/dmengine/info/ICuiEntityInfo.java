package com.jsoft.cocit.dmengine.info;

import java.util.List;

import com.jsoft.cocit.baseentity.cui.ICuiEntity;

/**
 * 实体功能模块主界面服务对象
 * 
 * @author Ji Yongshan
 * 
 */
public interface ICuiEntityInfo extends IDataEntityInfo<ICuiEntity>, ICuiEntity {
	/**
	 * 获取此主界面所属的实体对象
	 * 
	 * @return
	 */
	public ICocEntityInfo getCocEntity();

	/**
	 * 每个实体UI包含多个表单，通过 FORM KEY 获取表单对象
	 * 
	 * @param formKey
	 *            表单 KEy
	 * @return
	 */
	public ICuiFormInfo getCuiForm(String formKey);

	/**
	 * 每个实体UI只包含一个 GRID，通过该方法获取 GRID 对象
	 * 
	 * @return
	 */
	public ICuiGridInfo getCuiGrid();

	public List<String> getActionsList();

	public List<String> getFilterFieldsList();

	public List<String> getQueryFieldsList();

	public List<String> getColsList();

	public List<String> getRowsList();
}
