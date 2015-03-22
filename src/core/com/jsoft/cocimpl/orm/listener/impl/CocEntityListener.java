package com.jsoft.cocimpl.orm.listener.impl;

import java.io.Serializable;
import java.util.Iterator;

import com.jsoft.cocimpl.orm.generator.impl.GuidGenerator;
import com.jsoft.cocimpl.orm.generator.impl.OperationLogGenerator;
import com.jsoft.cocimpl.orm.nutz.EnColumnMappingImpl;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.orm.ExtDao;
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

	private Generator key;
	private OperationLogGenerator oplog;

	private void init() {
		if (key == null) {
			key = new GuidGenerator();
			oplog = new OperationLogGenerator();
		}
	}

	public void deleteBefore(ExtDao dao, EnMapping mapping, Object obj) {
	}

	public void insertBefore(ExtDao dao, EnMapping mapping, Object obj) {
		init();
		synchronized (obj) {
			oplog.generate(dao, mapping, null, obj);

			String idfld = mapping.getIdProperty();
			if (!StringUtil.isBlank(idfld)) {
				Serializable id = mapping.getIdGenerator().generate(dao, mapping, null, obj);
				ObjectUtil.setValue(obj, idfld, id);
			}

			Iterator<EnColumnMapping> columns = mapping.getGeneratorColumns();
			while (columns.hasNext()) {
				EnColumnMappingImpl column = (EnColumnMappingImpl) columns.next();
				String fieldName = column.getName();
				String generator = column.getGenerator();
				String gname = generator;
				String params = "";
				int from = generator.indexOf("(");
				if (from > 0) {
					int to = generator.indexOf(")");
					if (to > from) {
						gname = generator.substring(0, from);
						params = generator.substring(from + 1, to);
					}
				}
				try {

					Generator g = Cocit.me().getEntityGenerators().getGenerator(gname);
					if (g == null) {
						throw new CocException("字段生成器不存在! @CocColumn(field=\"%s\", generator=\"%s\")", fieldName, generator);
					}

					Object fieldValue = g.generate(dao, mapping, column, obj, StringUtil.toArray(params));
					ObjectUtil.setValue(obj, fieldName, fieldValue);
				} catch (Throwable e) {
					throw new CocException("生成字段值失败！%s", ExceptionUtil.msg(e));
				}
			}
		}
	}

	public void updateBefore(ExtDao dao, EnMapping mapping, Object obj) {
		init();
		synchronized (obj) {
			oplog.generate(dao, mapping, null, obj);
		}

	}

	public void deleteAfter(ExtDao dao, EnMapping mapping, Object entity) {

	}

	public void insertAfter(ExtDao dao, EnMapping mapping, Object entity) {

	}

	public void updateAfter(ExtDao dao, EnMapping mapping, Object entity) {

	}

}
