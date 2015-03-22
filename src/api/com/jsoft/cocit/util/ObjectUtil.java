package com.jsoft.cocit.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nutz.lang.Mirror;

import com.jsoft.cocit.config.IDynaBean;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.entity.DataEntity;
import com.jsoft.cocit.entity.IDataEntity;
import com.jsoft.cocit.orm.mapping.EnMapping;

/**
 * 
 * @author Ji Yongshan
 * @preserve public
 * 
 */
public abstract class ObjectUtil {

	/**
	 * 判断指定的集合是否为空。参数为 null 或 size() 为0都将范围 true。
	 * 
	 * @param coll
	 * @return
	 */
	public static boolean isNil(Collection coll) {
		return coll == null || coll.size() == 0;
	}

	/**
	 * 获取对象的id字段值，或者直接获取对象的toString值。
	 * 
	 * @param obj
	 * @return
	 */
	public static String idOrtoString(Object obj) {
		if (obj == null)
			return "";

		String ret = "";
		Mirror me = Mirror.me(obj.getClass());
		try {
			if (me.getField("id") != null) {
				Object id = getValue(obj, "id");
				if (id != null)
					ret = id.toString();
			} else {
				ret = obj.toString();
			}
		} catch (NoSuchFieldException e) {
			// Log.warn("", e);
			ret = obj.toString();
		}

		return ret;
	}

	public static String toString(Object obj) {
		if (obj == null)
			return "";

		return obj.toString();
	}

	public static String format(Object value, String pattern) {
		if (!StringUtil.isBlank(pattern) && pattern.charAt(0) == '*') {
			pattern = pattern.substring(1);
		}
		if (value instanceof String) {
			return (String) value;
		} else if (value instanceof Date) {
			if (!StringUtil.isBlank(pattern)) {
				try {
					return DateUtil.format((Date) value, pattern);
				} catch (Throwable e) {
					LogUtil.warn("", e);
					return DateUtil.formatDateTime((Date) value);
				}
			} else {
				return DateUtil.format((Date) value);
			}
		} else if (value instanceof Number && !StringUtil.isBlank(pattern)) {
			// if (Strings.isEmpty(pattern)) {
			// if (value instanceof Long || value instanceof Integer ||
			// value instanceof Short)
			// pattern = "#,##0";
			// else
			// pattern = "#,##0.0#";
			// }
			return new DecimalFormat(pattern).format(new BigDecimal(value.toString()));
		}

		if (value instanceof List) {
			List list = (List) value;
			if (list.size() == 0) {
				return "";
			} else {
				StringBuffer sb = new StringBuffer();
				for (Object obj : list) {
					sb.append(',').append(obj);
				}
				return sb.substring(1);
			}
		}

		return value == null ? "" : value.toString();
	}

	public static <T> T getValue(Object obj, final String path) {
		if (obj == null) {
			return null;
		}

		if (obj instanceof List) {
			List list = (List) obj;
			if (list.size() > 0) {
				obj = list.get(0);
			}
		}

		if (StringUtil.isBlank(path))
			return null;

		int dot = path.indexOf(".");
		if (dot > -1) {
			Object subObj = null;
			try {
				if (obj instanceof Map) {
					subObj = ((Map) obj).get(path.substring(0, dot));
				} else {
					subObj = Mirror.me(obj.getClass()).getValue(obj, path.substring(0, dot));
				}
			} catch (Throwable e) {
				try {
					if (obj instanceof IDynaBean) {
						return (T) ((IDynaBean) obj).get(path);
					}
				} catch (Throwable iglore) {
				}
			}
			if (subObj == null) {
				return null;
			}

			return (T) getValue(subObj, path.substring(dot + 1));
		} else {
			try {
				return (T) Mirror.me(obj.getClass()).getValue(obj, path);
			} catch (Throwable e) {
				try {
					if (obj instanceof IDynaBean) {
						return (T) ((IDynaBean) obj).get(path);
					}
				} catch (Throwable iglore) {
				}
				return null;
			}
		}
	}

	public static void setValue(Object obj, String path, Object value) {
		if (obj == null) {
			return;
		}
		Mirror me = Mirror.me(obj);
		try {
			int dot = path.indexOf(".");
			if (dot < 0) {
				me.setValue(obj, path, value);
			} else {
				String prop = path.substring(0, dot);
				Class fldtype = me.getField(prop).getType();
				Mirror fldme = Mirror.me(fldtype);
				Object fldval = fldme.born();
				setValue(fldval, path.substring(dot + 1), value);
				setValue(obj, prop, fldval);
			}
		} catch (NoSuchFieldException e) {
			LogUtil.warn("ObjectUtil.setValue(obj=%s, path=%s, value=%s): %s", obj, path, value, e);
		}
	}

	public static void setId(EnMapping mapping, Object obj, Serializable id) {
		if (obj instanceof DataEntity) {
			((DataEntity) obj).setId((Long) id);
			return;
		}
		String field = Const.F_ID;
		if (mapping != null) {
			field = mapping.getIdProperty();
		}

		if (field != null)
			setValue(obj, field, id);
	}

	public static boolean isEntity(Object obj) {
		if (obj == null) {
			return false;
		}
		return ClassUtil.isEntityType(obj.getClass());
	}

	public static Serializable getId(Object obj, String idField) {
		if (StringUtil.isBlank(idField)) {
			return getId(obj);
		}
		return getValue(obj, idField);
	}

	public static Serializable getId(Object obj) {
		if (obj instanceof IDataEntity) {
			return ((IDataEntity) obj).getId();
		}
		return getValue(obj, Const.F_ID);
	}

	public static Serializable getId(EnMapping mapping, Object obj) {
		if (obj instanceof IDataEntity) {
			return ((IDataEntity) obj).getId();
		}
		String field = Const.F_ID;
		if (mapping != null) {
			field = mapping.getIdProperty();
		}

		if (field == null)
			return null;

		return getValue(obj, field);
	}

	public static boolean isEmpty(Object[] array) {
		return (array == null || array.length == 0);
	}

	public static boolean isEmpty(Object obj) {
		return isEmpty(null, obj);
	}

	public static boolean isEmpty(Collection obj) {
		return obj == null || obj.size() == 0;
	}

	public static boolean isEmpty(List obj) {
		return obj == null || obj.size() == 0;
	}

	public static boolean isEmpty(EnMapping mapping, Object obj) {
		Serializable id = getId(mapping, obj);
		if (id instanceof Number) {
			return ((Number) id).intValue() <= 0;
		}
		if (id instanceof String) {
			return id == null || id.toString().trim().length() < 0;
		}
		return id == null;
	}

	/**
	 * 将指定字段转换成下拉选项键值
	 * 
	 * @param obj
	 * @param prop
	 * @return
	 */
	public static String toKey(Object obj, String prop) {
		if (obj == null) {
			return "";
		}
		if (ClassUtil.isBoolean(obj.getClass())) {
			if ((Boolean) obj) {
				return "1";
			} else {
				return "0";
			}
		}
		if (ClassUtil.isPrimitive(obj.getClass())) {
			return obj.toString();
		}
		try {
			Object v = Mirror.me(obj.getClass()).getValue(obj, prop == null || prop.trim().length() == 0 ? "id" : prop);
			if (v == null) {
				return "";
			}
			return v.toString();
		} catch (Throwable e) {
			return obj.toString();
		}
	}

	/**
	 * 生成下拉选项的键值
	 * 
	 * @param obj
	 * @return
	 */
	public static String toKey(Object obj) {
		return toKey(obj, null);
	}

	public static String getStringValue(Object obj, String path) {
		return format(getValue(obj, path), null);
	}

	public static String getStringValue(Object bean, String path, String pattern) {
		if (bean == null || StringUtil.isBlank(path)) {
			return "";
		}

		return format(getValue(bean, path), pattern);
	}

	public static void setDefaultValues(Object obj, Map defaultValues) {
		if (obj == null || defaultValues == null) {
			return;
		}

		Mirror mirror = Mirror.me(obj.getClass());

		Iterator<String> fields = defaultValues.keySet().iterator();
		while (fields.hasNext()) {
			String field = fields.next();

			if (field.indexOf(" ") > -1) {
				continue;
			}
			
			Object oldValue = ObjectUtil.getValue(obj, field);
			if (oldValue != null) {
				if (oldValue instanceof String) {
					if (((String) oldValue).trim().length() != 0)
						continue;
				} else {
					continue;
				}
			}

			Object value = defaultValues.get(field);
			if (value instanceof List) {
				try {
					Class type = mirror.getField(field).getType();
					if (List.class.isAssignableFrom(type)) {
						ObjectUtil.setValue(obj, field, value);
					} else {
						List listValue = (List) value;
						if (listValue.size() > 0) {
							ObjectUtil.setValue(obj, field, listValue.get(0));
						}
					}
				} catch (NoSuchFieldException e) {
					List listValue = (List) value;
					if (listValue.size() > 0) {
						ObjectUtil.setValue(obj, field, listValue.get(0));
					}
				}
			} else {
				ObjectUtil.setValue(obj, field, value);
			}
		}
	}

	public static void setValues(Object obj, Map values) {
		if (obj == null || values == null) {
			return;
		}

		Mirror mirror = Mirror.me(obj.getClass());

		Iterator<String> fields = values.keySet().iterator();
		while (fields.hasNext()) {
			String field = fields.next();
			Object value = values.get(field);
			if (value instanceof List) {
				try {
					Class type = mirror.getField(field).getType();
					if (List.class.isAssignableFrom(type)) {
						ObjectUtil.setValue(obj, field, value);
					} else {
						List listValue = (List) value;
						if (listValue.size() > 0) {
							ObjectUtil.setValue(obj, field, listValue.get(0));
						}
					}
				} catch (NoSuchFieldException e) {
					List listValue = (List) value;
					if (listValue.size() > 0) {
						ObjectUtil.setValue(obj, field, listValue.get(0));
					}
				}
			} else {
				ObjectUtil.setValue(obj, field, value);
			}
		}
	}
}
