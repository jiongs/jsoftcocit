package com.jsoft.cocimpl.orm.generator.impl;

import java.io.Serializable;
import java.util.Date;

import com.jsoft.cocit.orm.IExtDao;
import com.jsoft.cocit.orm.generator.Generator;
import com.jsoft.cocit.orm.mapping.EnColumnMapping;
import com.jsoft.cocit.orm.mapping.EnMapping;
import com.jsoft.cocit.util.DateUtil;

public class TimeSNGenerator implements Generator {

	private static final Integer lock = 0;
	private static int count = 0;

	public String getName() {
		return "timesn";
	}

	public Serializable generate(IExtDao dao, EnMapping entity, EnColumnMapping column, Object dataObject, String... params) {
		synchronized (lock) {
			count++;

			return DateUtil.format(new Date(), "yyyyMMdd_HHmm") + "_" + count;
		}
	}
}
