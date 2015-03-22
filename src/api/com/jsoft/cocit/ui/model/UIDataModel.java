package com.jsoft.cocit.ui.model;

import java.io.Writer;
import java.util.Hashtable;
import java.util.Map;

import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * UI数据模型：用于表示通过AJAX访问的数据模型。由两部分组成：1.模型，2.数据
 * <p>
 * 继承该类的所有模型都将输出JSON或XML格式的数据。
 * 
 * @author yongshan.ji
 * 
 * @param <T>界面模型泛型
 * @param <D>数据泛型
 */
public abstract class UIDataModel<M extends UIControlModel> implements UIModel {

	protected String viewName;

	/**
	 * HTML模型：数据的输出依赖于该HTML模型
	 */
	protected M model;

	/**
	 * 业务数据：待输出的业务数据对象
	 */
	protected Object data;

	protected String contextPath;

	protected Map<String, Object> context;

	protected Throwable exception;

	protected UIDataModel() {
		this.context = new Hashtable();
	}

	@Override
	public void release() {
		this.viewName = null;
		if (this.model != null) {
			this.model.release();
			this.model = null;
		}
		this.data = null;
		this.contextPath = null;
		this.context.clear();
		this.context = null;
		this.exception = null;
	}

	public String getContentType() {
		return Const.CONTENT_TYPE_JSON;
	}

	public boolean isCachable() {
		return false;
	}

	public M getModel() {
		return model;
	}

	public void setModel(M model) {
		this.model = model;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getContextPath() {
		return contextPath;
	}

	public UIDataModel setContextPath(String contextPath) {
		this.contextPath = contextPath;
		return this;
	}

	public <T extends UIDataModel> T set(String propName, Object propValue) {
		if (propName != null && propValue != null)
			context.put(propName, propValue);

		return (T) this;
	}

	public <T extends UIDataModel> T put(String key, Object value) {
		if (key != null && value != null)
			context.put(key, value);

		return (T) this;
	}

	public <T extends UIDataModel> T putAll(Map ctx) {
		context.putAll(ctx);

		return (T) this;
	}

	public <T> T get(String propName) {
		return (T) context.get(propName);
	}

	public <T> T get(String propName, T defaultReturn) {
		Object value = context.get(propName);

		if (value == null)
			return defaultReturn;
		if (defaultReturn == null)
			return (T) value;

		Class valueType = defaultReturn.getClass();

		try {
			return (T) StringUtil.castTo(value.toString(), valueType);
		} catch (Throwable e) {
			LogUtil.error("WidgetModel.get: 出错！ {propName:%s, defaultReturn:%s, valueType:%s}", propName, defaultReturn, valueType.getName(), e);
		}

		return defaultReturn;
	}

	public boolean isAjax() {
		return true;
	}

	@Override
	public String getTitle() {
		return null;
	}

	public void render(Writer out) throws Exception {

	}
}
