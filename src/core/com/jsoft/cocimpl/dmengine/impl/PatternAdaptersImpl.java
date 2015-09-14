package com.jsoft.cocimpl.dmengine.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.dmengine.IPatternAdapter;
import com.jsoft.cocit.dmengine.IPatternAdapters;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.DateUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;

public class PatternAdaptersImpl implements IPatternAdapters {

	private Map<String, IPatternAdapter> adapters;

	private Map<String, IPatternAdapter> runtimeAdapters;

	public PatternAdaptersImpl() {
		adapters = new HashMap();
		runtimeAdapters = new HashMap();
	}

	@Override
	public IPatternAdapter getAdapter(String pattern) {
		if (StringUtil.isBlank(pattern))
			return null;

		/*
		 * 获取静态Pattern适配器
		 */
		IPatternAdapter ret = adapters.get(pattern.trim().toLowerCase());

		/*
		 * 获取动态Pattern适配器
		 */
		if (ret == null) {
			ret = runtimeAdapters.get(pattern);
			if (ret == null) {
				ret = make(pattern);
				runtimeAdapters.put(pattern, ret);
			}
		}

		return ret;
	}

	@Override
	public void addAdapter(IPatternAdapter adapter) {
		if (adapter == null || StringUtil.isBlank(adapter.getName())) {
			throw new CocException("PatternAdapters.addAdapter. Fail！ [adapterName: %s, adapter: %s]",//
					adapter == null ? "<NULL>" : adapter.getName(),//
					adapter == null ? "<NULL>" : adapter.getClass().getName()//
			);
		}

		adapters.put(adapter.getName().trim().toLowerCase(), adapter);
	}

	public static <T> IPatternAdapter make(final String pattern) {
		return new IPatternAdapter<T>() {

			@Override
			public String getName() {
				return null;
			}

			public String getPattern() {
				return pattern;
			}

			@Override
			public boolean validate(Object fieldValue) {
				return true;
			}

			@Override
			public String format(Object fieldValue) {
				if (fieldValue == null)
					return "";

				return ObjectUtil.format(fieldValue, pattern);
			}

			@Override
			public T parse(String strFieldValue, Class<T> valueType) {
				try {
					if (Date.class.isAssignableFrom(valueType)) {
						return (T) DateUtil.parse(strFieldValue, pattern);
					} else if (Number.class.isAssignableFrom(valueType)) {
						Number df = new DecimalFormat(pattern).parse(strFieldValue);

						return (T) StringUtil.castTo(df.toString(), valueType);

					}
				} catch (Throwable e) {
					LogUtil.error("PatternAdapters.parse: Error! [strFieldValue: %s, pattern: %s] %s",//
							strFieldValue,//
							pattern,//
							e//
					);
				}

				return null;
			}

		};
	}
}
