package com.jsoft.cocit.entityengine;

import com.jsoft.cocit.entityengine.annotation.CocColumn;

/**
 * <B>Pattern适配器：</B>用来按特定规则“校验、解析、格式化”字段值。主要用于【{@link CocColumn#pattern()}】。
 * <UL>
 * <LI>与Pattern对应的规则可以从配置文件中读取，其key为前缀"pattern."，如果配置文件中没有指定的pattern，则实现类应支持默认值；
 * <LI>如果没有特殊需求，该接口的实现类，不能引入任何第三方工具类，即只能使用JDK自带的工具类。
 * <LI>实现类名命名规则：如“DatePatternAdapter, NumberPatternAdapter, EmailPatternAddapter, URLPatternAddapter”等；
 * <LI>实现类包名：扩展的适配器包名为“com.jha.cocit.entityengine.pattern”；
 * <LI>例如：适配器【{@link #getName()}】返回值为"date"，则可以先从配置文件获取"pattern.date"配置项，如果没有找到，则自动默认为"yyyy-MM-dd"； <br>
 * Cocit coc = Cocit.me(); <br>
 * ICocConfig config = coc.getConfig(); <br>
 * String format = config.get("pattern.date", "yyyy-MM-dd"); <br>
 * <br>
 * return (new SimpleDateFormat(format)).format(dateObj); <br>
 * </LI>
 * </UL>
 * 
 * @author Ji Yongshan
 * 
 */
public interface PatternAdapter<T> {
	/**
	 * Pattern名字：如果【{@link CocColumn#pattern()}】中指定的字段Pattern与这里相同，则使用该适配器来解析、校验字段值。
	 * 
	 * @return Pattern名称，如“date/datetime/time/email/url”等
	 */
	String getName();

	String getPattern();

	/**
	 * 校验字段值是否合法？
	 * 
	 * @param fieldValue
	 *            字段值
	 * @return 如果字段值满足【{@link #getName()}】所指定的规则，则返回true，否则返回false。
	 */
	boolean validate(T fieldValue);

	/**
	 * 格式化字段值为一个文本，通常用于数字、日期等字段。默认可以直接返回【fieldValue.toString()】。
	 * 
	 * @param fieldValue
	 *            字段值
	 * @return
	 */
	String format(T fieldValue);

	/**
	 * 解析字段值
	 * 
	 * @param strFieldValue
	 * @return
	 */
	T parse(String strFieldValue, Class<T> valueType);
}
