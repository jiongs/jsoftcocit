package com.jsoft.cocimpl.orm.generator.impl;

import static com.jsoft.cocit.constant.FieldNames.F_CREATED_DATE;
import static com.jsoft.cocit.constant.FieldNames.F_CREATED_OP_LOG;
import static com.jsoft.cocit.constant.FieldNames.F_CREATED_USER;
import static com.jsoft.cocit.constant.FieldNames.F_ID;
import static com.jsoft.cocit.constant.FieldNames.F_UPDATED_DATE;
import static com.jsoft.cocit.constant.FieldNames.F_UPDATED_OP_LOG;
import static com.jsoft.cocit.constant.FieldNames.F_UPDATED_USER;

import java.io.Serializable;
import java.util.Date;

import com.jsoft.cocimpl.ExtHttpContext;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.entity.IExtDataEntity;
import com.jsoft.cocit.orm.ExtDao;
import com.jsoft.cocit.orm.generator.Generator;
import com.jsoft.cocit.orm.mapping.EnColumnMapping;
import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ObjectUtil;

public class OperationLogGenerator implements Generator {

	public Serializable generate(ExtDao dao, EnMapping entity, EnColumnMapping column, Object dataObject, String... params) {

		ExtHttpContext ctx = (ExtHttpContext) Cocit.me().getHttpContext();

		if (dataObject instanceof IExtDataEntity) {
			IExtDataEntity data = (IExtDataEntity) dataObject;
			Long id = data.getId();
			if (id == null || id <= 0) {
				data.setCreatedDate(new Date());

				if (ctx != null) {
					data.setCreatedUser(ctx.getLoginUsername());
					data.setCreatedOpLog(ctx.getLoginLogKey());
				}
			} else {

				data.setUpdatedDate(new Date());
				if (ctx != null) {
					data.setUpdatedUser(ctx.getLoginUsername());
					data.setUpdatedOpLog(ctx.getLoginLogKey());
				}
			}

		} else {

			Serializable id = (Serializable) ObjectUtil.getValue(dataObject, F_ID);

			if (id == null || (id instanceof Number && ((Number) id).longValue() <= 0)) {
				if (ClassUtil.hasField(dataObject.getClass(), F_CREATED_DATE))
					ObjectUtil.setValue(dataObject, F_CREATED_DATE, new Date());

				if (ClassUtil.hasField(dataObject.getClass(), F_CREATED_USER) && ctx != null)
					ObjectUtil.setValue(dataObject, F_CREATED_USER, ctx.getLoginUsername());

				if (ClassUtil.hasField(dataObject.getClass(), F_CREATED_OP_LOG) && ctx != null)
					ObjectUtil.setValue(dataObject, F_CREATED_OP_LOG, ctx.getRequest().getRemoteAddr());
			}

			if (ClassUtil.hasField(dataObject.getClass(), F_UPDATED_DATE))
				ObjectUtil.setValue(dataObject, F_UPDATED_DATE, new Date());

			if (ClassUtil.hasField(dataObject.getClass(), F_UPDATED_USER) && ctx != null)
				ObjectUtil.setValue(dataObject, F_UPDATED_USER, ctx.getLoginUsername());

			if (ClassUtil.hasField(dataObject.getClass(), F_UPDATED_OP_LOG) && ctx != null)
				ObjectUtil.setValue(dataObject, F_UPDATED_OP_LOG, ctx.getRequest().getRemoteAddr());

		}

		return null;
	}

	@Override
	public String getName() {
		return "oplog";
	}
}
