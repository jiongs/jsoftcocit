package com.jsoft.cocimpl.orm.listener.impl;

import java.io.Serializable;
import java.util.Iterator;

import com.jsoft.cocimpl.orm.generator.impl.GuidGenerator;
import com.jsoft.cocimpl.orm.generator.impl.OperationLogGenerator;
import com.jsoft.cocimpl.orm.nutz.EnColumnMappingImpl;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.IExtDao;
import com.jsoft.cocit.orm.generator.EntityGenerators;
import com.jsoft.cocit.orm.generator.Generator;
import com.jsoft.cocit.orm.listener.EntityListener;
import com.jsoft.cocit.orm.mapping.EnColumnMapping;
import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @preserve
 * @author Ji Yongshan
 * 
 */
public class CocEntityListener implements EntityListener {

	private Generator				key;
	private OperationLogGenerator	oplog;

	private void init() {
		if (key == null) {
			key = new GuidGenerator();
			oplog = new OperationLogGenerator();
		}
	}

	public void deleteBefore(IExtDao dao, EnMapping mapping, Object obj) {
	}

	public void insertBefore(IExtDao dao, EnMapping mapping, Object obj) {
		init();
		synchronized (obj) {
			oplog.generate(dao, mapping, null, obj);

			String idfld = mapping.getIdProperty();
			if (!StringUtil.isBlank(idfld)) {
				Serializable id = mapping.getIdGenerator().generate(dao, mapping, null, obj);
				ObjectUtil.setValue(obj, idfld, id);
			}

			EntityGenerators generators = Cocit.me().getEntityGenerators();

			Iterator<EnColumnMapping> columns = mapping.getGeneratorColumns();
			while (columns.hasNext()) {
				EnColumnMappingImpl column = (EnColumnMappingImpl) columns.next();
				String fieldName = column.getName();

				Object fieldValue = ObjectUtil.getValue(obj, fieldName);
				if (fieldValue != null && fieldValue.toString().trim().length() > 0) {
					continue;
				}

				String expr = column.getGenerator();
				String gname, params;
				fieldValue = null;

				/*
				 * 表达式解析：$V{genName.param}
				 */
				while (StringUtil.hasContent(expr)) {
					gname = "";
					params = "";

					if (expr.startsWith("$V{")) {
						int end = expr.indexOf("}");
						if (end > 0) {
							String fullprop = expr.substring(3, end);
							int dot = fullprop.indexOf(".");
							if (dot > 0) {
								gname = fullprop.substring(0, dot);
								params = fullprop.substring(dot + 1);
							} else {
								gname = fullprop;
							}

							expr = expr.substring(end + 1);
						} else {
							gname = expr;
							expr = "";
						}
					} else {// 兼容：genName(params)
						int from = expr.indexOf("(");
						if (from > 0) {
							int to = expr.indexOf(")");
							if (to > from) {
								gname = expr.substring(0, from);
								params = expr.substring(from + 1, to);
							}
						} else {
							gname = expr;
						}
						expr = "";
					}
					try {
						Generator g = generators.getGenerator(gname);
						if (g == null) {
							params = gname;
							g = generators.getGenerator("code");// 默认编码生成器：解析内容支持：$D?,$N?,$C?
						}
						if (g == null) {
							throw new CocException("字段生成器不存在! fieldName = %s", fieldName);
						}

						if (fieldValue == null) {
							fieldValue = g.generate(dao, mapping, column, obj, StringUtil.toArray(params));
						} else {
							fieldValue = fieldValue.toString() + g.generate(dao, mapping, column, obj, StringUtil.toArray(params));
						}

					} catch (Throwable e) {
						throw new CocException("生成字段值失败! %s", ExceptionUtil.msg(e));
					}
				}

				ObjectUtil.setValue(obj, fieldName, fieldValue);
			}
		}
	}

	public void updateBefore(IExtDao dao, EnMapping mapping, Object obj) {
		init();
		synchronized (obj) {
			oplog.generate(dao, mapping, null, obj);
		}

	}

	public void deleteAfter(IExtDao dao, EnMapping mapping, Object entity) {

	}

	public void insertAfter(IExtDao dao, EnMapping mapping, Object entity) {

	}

	public void updateAfter(IExtDao dao, EnMapping mapping, Object entity) {

	}

}
