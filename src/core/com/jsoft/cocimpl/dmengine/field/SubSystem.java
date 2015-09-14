package com.jsoft.cocimpl.dmengine.field;

import java.util.ArrayList;
import java.util.List;

import com.jsoft.cocit.baseentity.INamedEntityExt;
import com.jsoft.cocit.dmengine.field.IExtField;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.JsonUtil;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.SortUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * 子系统数据：将被自动保存到子系统数据表中
 * 
 * @author yongshan.ji
 * 
 * @param <T>
 */
public class SubSystem<T> implements IExtField {
	private Class<T> type;

	private String jsonData;

	private List<T> list;

	public SubSystem() {
		this("");
	}

	public SubSystem(String str) {
		init(str);
	}

	public SubSystem(List<T> list) {
		if (list != null)
			SortUtil.sort(list, "sn", true);

		this.list = list;
	}

	public SubSystem(String str, Class<T> type) {
		this.type = type;
		init(str);
	}

	private void init(String str) {
		if (StringUtil.isBlank(jsonData)) {
			this.jsonData = "";
		}
		this.jsonData = str;

		if (list == null && type != null) {
			list = new ArrayList();
			if (!StringUtil.isBlank(jsonData)) {
				try {
					list = (List<T>) JsonUtil.loadFromJson(type, jsonData);
				} catch (Throwable e) {
					LogUtil.error("从JsonUtil加载子系统数据出错! %s", e);
					// throw new DemsyException(e);
				}
			}
		}
	}

	public List<T> getList() {
		return list;
	}

	public List<T> getList(Class type) {
		List list = new ArrayList();
		if (!StringUtil.isBlank(jsonData)) {
			try {
				list = (List<T>) JsonUtil.loadFromJson(type, jsonData);
			} catch (Throwable e) {
				LogUtil.error("从JsonUtil加载子系统数据出错! %s", e);
				throw new CocException(e);
			}
		}
		if (list != null) {
			int sn = 1;
			for (Object obj : list) {
				if (obj instanceof INamedEntityExt) {
					((INamedEntityExt) obj).setSn(sn++);
				}
			}
		}

		return list;
	}

	public String toString() {
		return jsonData == null ? "" : jsonData;
	}

	public String toJson() {
		return JsonUtil.toJson(jsonData);
	}
}
