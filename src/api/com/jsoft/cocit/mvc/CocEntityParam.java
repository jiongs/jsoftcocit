package com.jsoft.cocit.mvc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.lang.inject.Injecting;
import org.nutz.mvc.adaptor.ParamConvertor;
import org.nutz.mvc.adaptor.Params;

import com.jsoft.cocit.config.IDynaBean;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.util.ClassUtil;
import com.jsoft.cocit.util.ExceptionUtil;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.ObjectUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 
 * @author Ji Yongshan
 * 
 */
public class CocEntityParam {
	private String name;
	private String[] value;
	private List<CocEntityParam> array = new ArrayList();// 如果该值存在，则表示参数将被转换成一个List
	private Map<String, CocEntityParam> fields = new HashMap<String, CocEntityParam>();

	/**
	 * 初始化当前结点
	 * 
	 */
	public boolean put(String path, String... value) {
		if (path.trim().length() == 0)
			return false;

		String field = fetchName(path);

		if (field.trim().length() == 0)
			return false;

		this.name = field.trim();
		if (field.equals(path)) {
			this.value = value;
		} else {

			String subPath = path.substring(field.length());
			if (subPath.charAt(0) == '[') {
				int end = subPath.indexOf(']');
				if (end > 0) {
					String pre = subPath.substring(1, end);
					if (pre.indexOf('[') > -1 || pre.indexOf(']') > -1 || pre.trim().startsWith(".") || pre.trim().endsWith(".")) {
						return false;
					}
					subPath = pre + subPath.substring(end + 1);
				} else {
					return false;
				}
			} else if (subPath.charAt(0) == '.') {
				subPath = subPath.substring(1);
			} else {
				return false;
			}

			if (!addChild(subPath, value)) {
				return false;
			}
		}

		return true;
	}

	private boolean addChild(String path, String... value) {
		if (path.trim().length() == 0)
			return false;

		String field = fetchName(path).trim();

		if (field.length() == 0)
			return false;

		try {
			int idx = Integer.parseInt(field);
			for (int i = array.size(); i <= idx; i++) {
				array.add(new CocEntityParam());
			}
			CocEntityParam onn = array.get(idx);

			onn.put(path, value);

		} catch (Throwable e) {
			CocEntityParam onn = fields.get(field);
			if (onn == null) {
				onn = new CocEntityParam();
			}

			fields.put(field, onn);

			onn.put(path, value);
		}

		return true;
	}

	private String fetchName(String path) {
		int idx = path.indexOf('.');
		int from = path.indexOf('[');

		if (from > -1) {
			if (idx > -1) {
				idx = Math.min(idx, from);
			} else {
				idx = from;
			}
		}

		if (idx > -1) {
			path = path.substring(0, idx);
		}

		if (!StringUtil.isCode(path.trim())) {
			try {
				if (Integer.parseInt(path) >= 0) {
					return path;
				} else {
					return "";
				}
			} catch (Throwable e) {
				return "";
			}
		}

		return path;
	}

	public Map injectMap(Mirror mirror, Map<String, String> fieldMode) {
		Map ret = new HashMap();
		Iterator<String> keys = fields.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next().trim();
			if (key.charAt(0) == '[') {
				key = key.substring(1, key.length() - 1);
				ret.put(key, fields.get(key).inject(mirror, null, fieldMode));
			}
		}

		return ret;
	}

	public List injectList(Mirror mirror, Map<String, String> fieldMode) {
		List list = new LinkedList();
		Iterator<String> keys = fields.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next().trim();
			if (key.charAt(0) == '[') {
				list.add(fields.get(key).inject(mirror, null, fieldMode));
			}
		}

		return list;
	}

	public Object inject(Mirror mirror, Object oldObj, Map<String, String> fieldMode) {
		/*
		 * 依赖注入的旧对象非空
		 */
		if (oldObj != null) {

			/*
			 * 依赖注入的旧对象为“List”
			 */
			if (oldObj instanceof List) {
				List retList = new ArrayList();

				List oldList = (List) oldObj;
				Map<Serializable, Object> oldMap = new HashMap();
				for (Object ele : oldList) {
					Serializable id = ObjectUtil.getId(ele);
					if (id != null) {
						oldMap.put(id, ele);
					}
				}

				/*
				 * 依赖注入的旧对象为“List”：将“entity[number]”开头的参数注入到“List”中与之ID相同的每个对象元素
				 */
				if (array.size() > 0) {
					for (CocEntityParam paramEle : array) {

						// 查找ID参数
						CocEntityParam paramID = paramEle.fields.get(Const.F_ID);
						Long newId = null;
						if (paramID != null && paramID.value != null && paramID.value.length > 0) {
							try {
								newId = Long.parseLong(paramID.value[0]);
							} catch (Throwable e) {
							}
						}

						// 通过ID找出旧对象
						Object retObj = null;
						if (newId != null) {
							retObj = oldMap.remove(newId);
						}

						// 将公共字段注入到旧对象中
						if (this.fields.size() > 0) {
							retObj = inject(mirror, retObj, null, fieldMode);
						}

						// 将数组字段注入到旧对象中
						retObj = paramEle.inject(mirror, retObj, null, fieldMode);

						// 加入到返回列表
						retList.add(retObj);
					}
				}

				/*
				 * 将公共字段注入到List对象的其他元素
				 */
				Iterator it = oldMap.values().iterator();
				while (it.hasNext()) {
					Object old = it.next();
					inject(mirror, old, null, fieldMode);
				}

				// 返回最新列表
				return retList;
			}

			/*
			 * 依赖注入的旧对象为“实体对象”
			 */
			else {
				// 依赖注入的旧对象为“实体对象”：优先将entity.和entity[field]开头的参数注入到对象
				if (this.fields.size() > 0) {
					return inject(mirror, oldObj, null, fieldMode);
				}
				// 依赖注入的旧对象为“实体对象”：但entity.和entity[field]不存在，则自动将entity[0]开头的参数注入到对象
				else if (array.size() > 0) {
					return array.get(0).inject(mirror, oldObj, null, fieldMode);
				}
				// 没有“entity”开头的有效参数
				else {
					return oldObj;
				}
			}

		}

		/*
		 * 依赖注入的旧对象为NULL
		 */
		else {
			/*
			 * 依赖注入的旧对象为NULL：参数中“有”数组“entity[number]”
			 */
			if (array.size() > 0) {
				// 将entity.field和entity[field]转换成列表元素（对象）的“公共字段”
				List retList = new ArrayList();
				Object commonFields, retObj;
				for (CocEntityParam ele : array) {
					commonFields = inject(mirror, oldObj, null, fieldMode);
					retObj = ele.inject(mirror, commonFields, null, fieldMode);
					if (retObj != null) {
						retList.add(retObj);
					}
				}

				return retList;
			}

			/*
			 * 依赖注入的旧对象为NULL：参数中“没有”数组“entity[number]”
			 */
			else {
				return inject(mirror, oldObj, null, fieldMode);
			}

		}

	}

	/**
	 * 将结点树中的值注入到 mirror 中
	 * 
	 */
	public Object inject(Mirror mirror, Object obj, Class genericType, Map<String, String> fieldMode) {
		if (obj == null) {
			obj = mirror.born();
		}
		for (Entry<String, CocEntityParam> entry : fields.entrySet()) {
			CocEntityParam param = entry.getValue();
			String[] values = param.getValue();
			String key = entry.getKey();
			if (fieldMode != null) {
				String mode = fieldMode.get(key);
				if (!Strings.isEmpty(mode) && !"M".equals(mode) && !"E".equals(mode) && !"H".equals(mode)) {
					continue;
				}
			}
			if (param.isLeaf() && values != null && values.length > 0) {
				try {
					// if (obj instanceof List) {
					// List list = (List) obj;
					// for (String value : values) {
					// Mirror itemMirror = Mirror.me(genericType);
					// Object item = itemMirror.born();
					// Injecting in = itemMirror.getInjecting(key);
					// ParamConvertor pc = Params.makeParamConvertor(itemMirror.getField(entry.getKey()).getType());
					// in.inject(item, pc.convert(new String[] { value }));
					// list.add(item);
					// }
					// } else {
					Injecting in = mirror.getInjecting(key);
					Class type = mirror.getField(entry.getKey()).getType();
					ParamConvertor pc = Params.makeParamConvertor(type);
					if (Number.class.isAssignableFrom(type)) {
						values[0] = values[0].replace(",", "");
					}
					in.inject(obj, pc.convert(new String[] { values[0] }));
					// }
				} catch (RuntimeException e) {
					LogUtil.debug(ExceptionUtil.msg(e));

					Method m = null;
					try {
						m = mirror.getSetter(key, String.class);
					} catch (Throwable e1) {
					}
					// 设置扩展属性值
					if (m == null) {
						if (obj instanceof IDynaBean && values != null && values.length > 0) {
							((IDynaBean) obj).set(key, values[0]);
						}
					} else {
						throw e;
					}
				} catch (Throwable e) {
					LogUtil.debug(ExceptionUtil.msg(e));

					// 设置扩展属性值
					if (obj instanceof IDynaBean && values != null && values.length > 0) {
						((IDynaBean) obj).set(key, values[0]);
					}
				}
				continue;
			}

			try {
				// 不是叶子结点,不能直接注入
				Injecting in = mirror.getInjecting(key);
				Field field = mirror.getField(entry.getKey());
				Class type = field.getType();
				if (type.equals(List.class)) {
					String listGenericStr = field.getGenericType().toString();
					listGenericStr = listGenericStr.substring(listGenericStr.indexOf("<") + 1, listGenericStr.length() - 1);
					Class listGenericType = ClassUtil.forName(listGenericStr);
					Mirror listGerericMirror = Mirror.me(listGenericType);

					Mirror<?> listFieldMirror = Mirror.me(LinkedList.class);
					List listFieldValue = ObjectUtil.getValue(obj, field.getName());
					if (listFieldValue == null) {
						listFieldValue = (List) listFieldMirror.born();
					}

					in.inject(obj, param.inject(listGerericMirror, listFieldValue, null));
				} else {
					Mirror<?> fieldMirror = Mirror.me(type);
					Object next = null;
					if (!ObjectUtil.isEntity(type)) {
						next = mirror.getValue(obj, key);
					}
					in.inject(obj, param.inject(fieldMirror, next, fieldMode));
				}
			} catch (Throwable e) {
				LogUtil.warn("", e);
				// if (obj instanceof IDynamic) {
				// onn.injectDynamic((IDynamic) obj, key);
				// }
				continue;
			}
		}

		return obj;
	}

	// private Object injectDynamic(IDynamic obj, String path) {
	// for (Entry<String, EntityParamNode> entry : children.entrySet()) {
	// EntityParamNode onn = entry.getValue();
	// String[] value = onn.getValue();
	// String key = entry.getKey();
	// if (onn.isLeaf()) {
	// if (value != null && value.length > 0) {
	// obj.set(path + "." + key, value[0]);
	// } else {
	// obj.set(path + "." + key, "");
	// }
	// } else {
	// onn.injectDynamic(obj, path + "." + key);
	// }
	// }
	// return obj;
	// }

	public int size() {
		return fields.size();
	}

	public String[] getValue() {
		return value;
	}

	public boolean isLeaf() {
		return fields.size() == 0 && fields.size() == 0;
	}

	public String getName() {
		return name;
	}

	private String makeSpace(int level) {
		String ret = "";
		for (int i = 0; i < level; i++) {
			ret += "  ";
		}
		return ret;
	}

	public String toJson(int level) {
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append(name + " : ").append((value == null ? "" : JsonUtil.toJson(value)));

		if (fields.size() > 0) {
			sb.append(", fields : {");

			Iterator<String> it = fields.keySet().iterator();
			while (it.hasNext()) {
				String childKey = it.next();
				CocEntityParam childVal = fields.get(childKey);

				sb.append("\n" + makeSpace(level + 2));
				sb.append(childVal.toJson(level + 2));
			}

			sb.append("\n" + makeSpace(level) + "}");
		}
		if (array.size() > 0) {
			sb.append(", array : [");
			for (CocEntityParam one : array) {
				sb.append("\n" + makeSpace(level + 2));
				sb.append(one.toJson(level + 2));
			}
			sb.append("\n" + makeSpace(level + 1) + "]");
		}

		sb.append("}");

		return sb.toString();
	}

	public String toJson() {
		return toJson(0);
	}

	public String toString() {
		return toJson();
	}
}