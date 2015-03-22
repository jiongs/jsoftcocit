package com.jsoft.cocimpl.entityengine.impl.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocimpl.entityengine.EntityServiceFactory;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.entity.IDataEntity;
import com.jsoft.cocit.entity.IExtDataEntity;
import com.jsoft.cocit.entity.IExtTenantOwnerEntity;
import com.jsoft.cocit.entity.coc.ICocField;
import com.jsoft.cocit.entityengine.service.CocActionService;
import com.jsoft.cocit.entityengine.service.CocEntityService;
import com.jsoft.cocit.entityengine.service.CocFieldService;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.ExcelUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
class SystemExcel {
	private EntityServiceFactory entityServiceFactory;
	private Orm orm;
	private String tenantKey;
	private String tenantName;
	private Class moduleClass;
	private Map<String, CocFieldService> fieldsMap = new HashMap();// <字段名称, 字段>
	private String[] excelHeads;
	private List<String[]> excelRows;

	SystemExcel(CocEntityService module, CocActionService action, File excel) throws FileNotFoundException, IOException, CocException {
		this.entityServiceFactory = Cocit.me().getEntityServiceFactory();
		// entityManager = Cocit.me().getEntityManagerFactory().getManager(null, module);
		orm = Cocit.me().orm();
		tenantKey = Cocit.me().getHttpContext().getLoginTenantKey();
		tenantName = Cocit.me().getHttpContext().getLoginTenant().getName();
		moduleClass = module.getClassOfEntity();

		// 处理自定义系统相关信息
		List<CocFieldService> fields = (List<CocFieldService>) module.getFieldsOfEnabled();
		for (CocFieldService fld : fields) {
			String name = fld.getName();
			boolean valid = !fld.isDisabled();
			int mode = fld.getMode(action.getKey(), null);// fld.getMode(action.getKey(), false, "E");
			valid = valid && (mode == FieldModes.E || mode == FieldModes.M);

			if (valid)
				fieldsMap.put(name, fld);
		}
		if (LogUtil.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("系统(" + module.getName() + ")字段：\n");
			for (CocFieldService fld : fields) {
				if (fieldsMap.get(fld.getName()) == null) {
					continue;
				}
				sb.append(fld.getName()).append("：").append(fld.getFieldName());
				sb.append("\n");
			}
			LogUtil.info(sb.toString());
		}

		// 处理excel相关信息
		excelRows = ExcelUtil.parseExcel(excel);
		if (excelRows != null) {
			if (LogUtil.isInfoEnabled()) {
				StringBuffer sb = new StringBuffer();
				sb.append("Excel数据:\n");
				for (String[] row : excelRows) {
					for (String cell : row) {
						sb.append(cell).append(", ");
					}
					sb.append("\n");
				}
				LogUtil.info(sb.toString());
			}
			if (excelRows.size() > 0) {
				excelHeads = excelRows.get(0);
				excelRows.remove(0);
			}
		}
		// 校验： 验证表头是否重复？是否为系统字段？
		if (excelHeads != null) {
			List<String> listHeads = new ArrayList();
			for (String str : excelHeads) {
				if (listHeads.contains(str)) {
					throw new CocException(" Excel数据校验出错：数据表表头不能重复！【" + str + "】列非法");
				}
				if (fieldsMap.get(str) == null) {
					throw new CocException(" Excel数据校验出错：数据表表头在系统字段中必须存在并在新增时允许编辑！【" + str + "】列非法");
				}
				listHeads.add(str);
			}
			List<String> excelPKs = new ArrayList();
			int len = excelRows.size();
			for (int i = 0; i < len; i++) {
				String[] row = excelRows.get(i);
				if (row == null || row.length <= 1) {
					throw new CocException(" Excel数据校验出错：数据表至少需要 2 列！但第 " + (i + 2) + " 行只有 " + (row == null ? 0 : row.length) + " 列");
				}
				if (row[0] == null || row[0].trim().length() == 0) {
					throw new CocException(" Excel数据校验出错：数据表的第一列不允许为空！第 " + (i + 2) + " 行第一列非法");
				}
				if (excelPKs.contains(row[0])) {
					throw new CocException(" Excel数据校验出错：数据表的第一列不允许重复！第 " + (i + 2) + " 行第一列非法");
				}
				excelPKs.add(row[0]);
			}
		}
	}

	public SystemExcel(CocEntityService module, File excel) throws FileNotFoundException, IOException, CocException {
		this.entityServiceFactory = Cocit.me().getEntityServiceFactory();
		// entityManager = Cocit.me().getEntityManagerFactory().getManager(null, module);
		orm = Cocit.me().orm();
		tenantKey = Cocit.me().getHttpContext().getLoginTenantKey();
		moduleClass = module.getClassOfEntity();

		// 处理自定义系统相关信息
		List<CocFieldService> datas = (List<CocFieldService>) module.getFieldsOfEnabled();
		for (CocFieldService data : datas) {
			String name = data.getName();

			fieldsMap.put(name, data);
		}
		if (LogUtil.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("系统(" + module.getName() + ")字段：\n");
			for (ICocField fld : datas) {
				if (fieldsMap.get(fld.getName()) == null) {
					continue;
				}
				sb.append(fld.getName()).append("：").append(fld.getFieldName());
				sb.append("\n");
			}
			LogUtil.info(sb.toString());
		}

		// 处理excel相关信息
		excelRows = ExcelUtil.parseExcel(excel);
		if (excelRows != null) {
			if (LogUtil.isInfoEnabled()) {
				StringBuffer sb = new StringBuffer();
				sb.append("Excel数据:\n");
				for (String[] row : excelRows) {
					for (String cell : row) {
						sb.append(cell).append(", ");
					}
					sb.append("\n");
				}
				LogUtil.info(sb.toString());
			}
			if (excelRows.size() > 0) {
				excelHeads = excelRows.get(0);
				excelRows.remove(0);
			}
		}
	}

	public List getRows() throws InstantiationException, IllegalAccessException {

		final List<IExtDataEntity> retList = new ArrayList();
		int excelRowIndex = 1;
		int excelColIndex = 0;
		for (String[] row : excelRows) {
			excelRowIndex++;
			IExtDataEntity data = (IExtDataEntity) moduleClass.newInstance();
			for (int i = 0; i < row.length; i++) {
				excelColIndex = i + 1;
				if (i >= excelHeads.length) {
					break;
				}
				try {
					CocFieldService fld = fieldsMap.get(excelHeads[i]);
					if (fld == null) {
						throw new CocException("字段“%s”不存在！", excelHeads[i]);
					}
					String propName = fld.getFieldName();
					String propValue = row[i];

					CocEntityService fkModule = null;
					if (fld.isManyToOne()) {
						fkModule = entityServiceFactory.getEntity(fld.getFkTargetEntityKey());
						if (fkModule != null) {
							int dot = propName.indexOf(".");
							if (dot < 0) {
								propName = propName + ".name";
							}
						}
					}
					int dot = propName.indexOf(".");

					/*
					 * 解析外键字段
					 */
					if (dot > 0) {
						LogUtil.debug(propName);

						String nextProp = propName.substring(dot + 1);
						propName = propName.substring(0, dot);

						// if (fkModule == null && fkModule.getField(fld.getFkFieldKey()) != null) {
						// fkModule = fkModule.getField().getModuleKey();
						// }
						if (fkModule == null) {
							throw new CocException("Excel表中的第 " + excelColIndex + " 列【" + fld.getName() + "】是外键字段，但引用的系统不存在！");
						}
						Object fkValue = null;
						if (!StringUtil.isBlank(propValue)) {
							Class fkClass = fkModule.getClassOfEntity();
							fkValue = orm.get(fkClass, Expr.eq(nextProp, propValue).or(Expr.eq(Const.F_KEY, propValue)));
							if (fkValue == null) {
								// throw new CocException("Excel表中的第 " + colIndex + " 列【" + fld.getName() + "】是外键字段，但第 " + rowIndex + " 行【" + propValue + "】在【" + fkSystem.getName() + "】模块中不存在！");
								fkValue = fkClass.newInstance();
								ObjectUtil.setValue(fkValue, nextProp, propValue);
								ObjectUtil.setValue(fkValue, Const.F_KEY, propValue);
							}
						}

						ObjectUtil.setValue(data, propName, fkValue);
					} else {
						/*
						 * 解析字典字段
						 */
						Option[] options = fld.getDicOptionsArray();
						if (options != null && options.length > 0) {
							for (Option option : options) {
								if (option.getText().equals(propValue)) {
									propValue = option.getValue();
									break;
								}
							}
						}

						//
						if (propValue != null && propValue.trim().length() > 0)
							ObjectUtil.setValue(data, propName, propValue);
					}
				} catch (Throwable e) {
					LogUtil.error("导入Excel中的第 " + excelRowIndex + " 行第 " + excelColIndex + " 列时出错！" + e.getMessage(), e);
					throw new CocException("导入Excel中的第 " + excelRowIndex + " 行第 " + excelColIndex + " 列时出错！\n错误信息：" + e.getMessage());
				}
			}

			//
			if (data instanceof IExtTenantOwnerEntity) {
				((IExtTenantOwnerEntity) data).setTenantKey(tenantKey);
				((IExtTenantOwnerEntity) data).setTenantName(tenantName);
			}

			retList.add(data);
		}

		return retList;
	}

	private List getDataRows() throws InstantiationException, IllegalAccessException {
		CocFieldService pkfld = fieldsMap.get(excelHeads[0]);
		final List<IDataEntity> list = new ArrayList();
		int rowIndex = 1;
		int colIndex = 0;
		for (String[] row : excelRows) {
			rowIndex++;
			IExtDataEntity data = (IExtDataEntity) orm.get(moduleClass, Expr.eq(pkfld.getFieldName(), row[0]));
			if (data == null) {
				data = (IExtDataEntity) moduleClass.newInstance();
			}
			for (int i = 0; i < row.length; i++) {
				colIndex = i + 1;
				try {
					CocFieldService fld = (CocFieldService) fieldsMap.get(excelHeads[i]);
					String propname = fld.getFieldName();
					String propvalue = row[i];

					CocEntityService fkModule = fld.getFkTargetEntity();
					if (fkModule != null) {
						int dot = propname.indexOf(".");
						if (dot < 0) {
							propname = propname + ".name";
						}
					}

					int dot = propname.indexOf(".");
					// 设置外键值
					if (dot > 0) {
						LogUtil.debug(propname);

						String nextprop = propname.substring(dot + 1);
						propname = propname.substring(0, dot);

						if (fkModule == null) {
							throw new CocException("Excel表中的第 " + colIndex + " 列【" + fld.getName() + "】是外键字段，但引用的系统不存在！");
						}
						Object fkvalue = null;
						if (!StringUtil.isBlank(propvalue)) {
							fkvalue = orm.get(fkModule.getClassOfEntity(), Expr.eq(nextprop, propvalue));
							if (fkvalue == null) {
								throw new CocException("Excel表中的第 " + colIndex + " 列【" + fld.getName() + "】是外键字段，但第 " + rowIndex + " 行【" + propvalue + "】在【" + fkModule.getName() + "】模块中不存在！");
							}
						}
						ObjectUtil.setValue(data, propname, fkvalue);
					} else {
						ObjectUtil.setValue(data, propname, propvalue);
					}
				} catch (Throwable e) {
					throw new CocException("导入Excel中的第 " + rowIndex + " 行第 " + colIndex + " 列时出错！" + e.getMessage());
				}
			}

			//
			if (data instanceof IExtTenantOwnerEntity) {
				((IExtTenantOwnerEntity) data).setTenantKey(tenantKey);
				((IExtTenantOwnerEntity) data).setTenantName(tenantName);
			}

			list.add(data);
		}

		return list;
	}

	public int save() throws InstantiationException, IllegalAccessException, CocException {
		try {
			orm.save(this.getDataRows());
		} catch (Throwable e) {
			LogUtil.error(ExceptionUtil.msg(e));
			throw new CocException(e);
		}
		return excelRows.size();
	}
}
