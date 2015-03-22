package com.jsoft.cocit.entityengine.service;

import java.util.List;

import com.jsoft.cocit.entity.cui.ICuiEntity;

/**
 * 实体功能模块主界面服务对象
 * 
 * @author Ji Yongshan
 * 
 */
public interface CuiEntityService extends DataEntityService<ICuiEntity>, ICuiEntity {
	/**
	 * 获取此主界面所属的实体对象
	 * 
	 * @return
	 */
	public CocEntityService getCocEntity();

	/**
	 * 每个实体UI包含多个表单，通过 FORM KEY 获取表单对象
	 * 
	 * @param formKey
	 *            表单 KEy
	 * @return
	 */
	public CuiFormService getCuiForm(String formKey);

	/**
	 * 每个实体UI只包含一个 GRID，通过该方法获取 GRID 对象
	 * 
	 * @return
	 */
	public CuiGridService getCuiGrid();

	public List<String> getActionsList();

	public List<String> getFilterFieldsList();

	public List<String> getQueryFieldsList();

	public List<String> getColsList();

	public List<String> getRowsList();
}
