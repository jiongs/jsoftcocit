package com.jsoft.cocimpl.dmengine.impl.info;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.baseentity.IDataEntity;
import com.jsoft.cocit.baseentity.IDataEntityExt;
import com.jsoft.cocit.baseentity.coc.ICocFieldEntity;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.constant.FieldModes;
import com.jsoft.cocit.dmengine.IEntityInfoFactory;
import com.jsoft.cocit.dmengine.info.ICocActionInfo;
import com.jsoft.cocit.dmengine.info.ICocEntityInfo;
import com.jsoft.cocit.dmengine.info.ICocFieldInfo;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.log.Log;
import com.jsoft.cocit.log.Logs;
import com.jsoft.cocit.orm.IOrm;
import com.jsoft.cocit.orm.expr.Expr;
import com.jsoft.cocit.util.ExcelUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.Option;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 * @param <T>
 */
class SystemExcelUtil {
	private Log log = Logs.getLog(SystemExcelUtil.class);

	private IEntityInfoFactory entityServiceFactory;
	private IOrm orm;
	// private String tenantCode;
	// private String tenantName;
	private Class moduleClass;
	private Map<String, ICocFieldInfo> fieldsMap = new HashMap();// <字段名称, 字段>
	private String[] excelHeads;
	private List<String[]> excelRows;

	SystemExcelUtil(ICocEntityInfo module, ICocActionInfo action, File excel) throws FileNotFoundException, IOException, CocException {
		this.entityServiceFactory = Cocit.me().getEntityServiceFactory();
		// entityManager = Cocit.me().getEntityManagerFactory().getManager(null, module);
		orm = Cocit.me().orm();
		// tenantCode = Cocit.me().getHttpContext().getLoginTenantCode();
		// tenantName = Cocit.me().getHttpContext().getLoginTenant().getName();
		moduleClass = module.getClassOfEntity();

		// 处理自定义系统相关信息
		List<ICocFieldInfo> fields = (List<ICocFieldInfo>) module.getFieldsOfEnabled();
		for (ICocFieldInfo fld : fields) {
			String name = fld.getName();
			boolean valid = !fld.isDisabled();
			int mode = fld.getMode(action.getCode(), null);// fld.getMode(action.getCode(), false, "E");
			valid = valid && (mode == FieldModes.E || mode == FieldModes.M);

			if (valid)
				fieldsMap.put(name, fld);
		}
		if (log.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("系统(" + module.getName() + ")字段：\n");
			for (ICocFieldInfo fld : fields) {
				if (fieldsMap.get(fld.getName()) == null) {
					continue;
				}
				sb.append(fld.getName()).append("：").append(fld.getFieldName());
				sb.append("\n");
			}
			log.debug(sb.toString());
		}

		// 处理excel相关信息
		excelRows = ExcelUtil.parseExcel(excel);
		if (excelRows != null) {
			if (log.isDebugEnabled()) {
				StringBuffer sb = new StringBuffer();
				sb.append("Excel数据:\n");
				for (String[] row : excelRows) {
					for (String cell : row) {
						sb.append(cell).append(", ");
					}
					sb.append("\n");
				}
				log.debug(sb.toString());
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

	public SystemExcelUtil(ICocEntityInfo module, File excel) throws FileNotFoundException, IOException, CocException {
		this.entityServiceFactory = Cocit.me().getEntityServiceFactory();
		// entityManager = Cocit.me().getEntityManagerFactory().getManager(null, module);
		orm = Cocit.me().orm();
		// tenantCode = Cocit.me().getHttpContext().getLoginTenantCode();
		moduleClass = module.getClassOfEntity();

		// 处理自定义系统相关信息
		List<ICocFieldInfo> datas = (List<ICocFieldInfo>) module.getFieldsOfEnabled();
		for (ICocFieldInfo data : datas) {
			String name = data.getName();

			fieldsMap.put(name, data);
		}
		if (log.isDebugEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("系统(" + module.getName() + ")字段：\n");
			for (ICocFieldEntity fld : datas) {
				if (fieldsMap.get(fld.getName()) == null) {
					continue;
				}
				sb.append(fld.getName()).append("：").append(fld.getFieldName());
				sb.append("\n");
			}
			log.debug(sb.toString());
		}

		// 处理excel相关信息
		excelRows = ExcelUtil.parseExcel(excel);
		if (excelRows != null) {
			if (log.isDebugEnabled()) {
				StringBuffer sb = new StringBuffer();
				sb.append("Excel数据:\n");
				for (String[] row : excelRows) {
					for (String cell : row) {
						sb.append(cell).append(", ");
					}
					sb.append("\n");
				}
				log.debug(sb.toString());
			}
			if (excelRows.size() > 0) {
				excelHeads = excelRows.get(0);
				excelRows.remove(0);
			}
		}
	}

	public List getRows() throws InstantiationException, IllegalAccessException {

		final List<IDataEntityExt> retList = new ArrayList();
		int excelRowIndex = 1;
		int excelColIndex = 0;
		for (String[] row : excelRows) {
			excelRowIndex++;
			IDataEntityExt data = (IDataEntityExt) moduleClass.newInstance();
			for (int i = 0; i < row.length; i++) {
				excelColIndex = i + 1;
				if (i >= excelHeads.length) {
					break;
				}
				try {
					ICocFieldInfo fld = fieldsMap.get(excelHeads[i]);
					if (fld == null) {
						throw new CocException("字段“%s”不存在！", excelHeads[i]);
					}
					String propName = fld.getFieldName();
					String propValue = row[i];

					ICocEntityInfo fkModule = null;
					if (fld.isManyToOne()) {
						fkModule = entityServiceFactory.getEntity(fld.getFkTargetEntityCode());
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
						log.debug(propName);

						String nextProp = propName.substring(dot + 1);
						propName = propName.substring(0, dot);

						// if (fkModule == null && fkModule.getField(fld.getFkFieldCode()) != null) {
						// fkModule = fkModule.getField().getModuleCode();
						// }
						if (fkModule == null) {
							throw new CocException("Excel表中的第 " + excelColIndex + " 列【" + fld.getName() + "】是外键字段，但引用的系统不存在！");
						}
						Object fkValue = null;
						if (!StringUtil.isBlank(propValue)) {
							Class fkClass = fkModule.getClassOfEntity();
							fkValue = orm.get(fkClass, Expr.eq(nextProp, propValue).or(Expr.eq(Const.F_CODE, propValue)));
							if (fkValue == null) {
								// throw new CocException("Excel表中的第 " + colIndex + " 列【" + fld.getName() + "】是外键字段，但第 " + rowIndex + " 行【" + propValue + "】在【" + fkSystem.getName() + "】模块中不存在！");
								fkValue = fkClass.newInstance();
								ObjectUtil.setValue(fkValue, nextProp, propValue);
								ObjectUtil.setValue(fkValue, Const.F_CODE, propValue);
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
					// log.error("导入Excel中的第 " + excelRowIndex + " 行第 " + excelColIndex + " 列时出错！" + e.getMessage(), e);
					throw new CocException("导入Excel中的第 " + excelRowIndex + " 行第 " + excelColIndex + " 列时出错！\n错误信息：" + e.getMessage());
				}
			}

			//
			// if (data instanceof IOfTenantEntityExt) {
			// ((IOfTenantEntityExt) data).setTenantCode(tenantCode);
			// ((IOfTenantEntityExt) data).setTenantName(tenantName);
			// }

			retList.add(data);
		}

		return retList;
	}

	private List getDataRows() throws InstantiationException, IllegalAccessException {
		ICocFieldInfo pkfld = fieldsMap.get(excelHeads[0]);
		final List<IDataEntity> list = new ArrayList();
		int rowIndex = 1;
		int colIndex = 0;
		for (String[] row : excelRows) {
			rowIndex++;
			IDataEntityExt data = (IDataEntityExt) orm.get(moduleClass, Expr.eq(pkfld.getFieldName(), row[0]));
			if (data == null) {
				data = (IDataEntityExt) moduleClass.newInstance();
			}
			for (int i = 0; i < row.length; i++) {
				colIndex = i + 1;
				try {
					ICocFieldInfo fld = (ICocFieldInfo) fieldsMap.get(excelHeads[i]);
					String propname = fld.getFieldName();
					String propvalue = row[i];

					ICocEntityInfo fkModule = fld.getFkTargetEntity();
					if (fkModule != null) {
						int dot = propname.indexOf(".");
						if (dot < 0) {
							propname = propname + ".name";
						}
					}

					int dot = propname.indexOf(".");
					// 设置外键值
					if (dot > 0) {
						log.debug(propname);

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
			// if (data instanceof IOfTenantEntityExt) {
			// ((IOfTenantEntityExt) data).setTenantCode(tenantCode);
			// ((IOfTenantEntityExt) data).setTenantName(tenantName);
			// }

			list.add(data);
		}

		return list;
	}

	public int save() throws InstantiationException, IllegalAccessException, CocException {
		try {
			orm.save(this.getDataRows());
		} catch (Throwable e) {
			log.error(ExceptionUtil.msg(e));
			throw new CocException(e);
		}
		return excelRows.size();
	}
}
