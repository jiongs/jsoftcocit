package com.jsoft.cocit.ui.model;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jsoft.cocimpl.ui.UIView;
import com.jsoft.cocimpl.ui.UIViews;
import com.jsoft.cocit.Cocit;
import com.jsoft.cocit.constant.Const;
import com.jsoft.cocit.exception.CocException;
import com.jsoft.cocit.util.LogUtil;
import com.jsoft.cocit.util.StringUtil;

/**
 * UI控件模型：支持用户交互的UI模型
 * 
 * @author yongshan.ji
 * 
 */
public abstract class BaseUIModel implements UIModel {

	protected String id;
	protected String title;
	protected boolean ajax;
	protected String themeName;
	protected String viewName;
	protected String contextPath;
	protected Map<String, Object> context;
	protected Throwable exception;
	protected Properties attributes;

	/**
	 * 结果UI：此UI组件发生交互时时，将重新加载哪些UI组件的数据？即此UI组件的数据发生变化时，结果UI组件的数据将被刷新。
	 * <p>
	 * 其值为UI组件的HTML元素ID
	 */
	private List<String> resultUI;
	/**
	 * 参数UI：即加载此UI组件数据时将从哪些UI组件获取参数？
	 */
	private List<String> paramUI;
	private List<String> fkTargetFields;
	private List<String> fkFields;

	public void release() {
		this.id = null;
		this.title = null;
		this.themeName = null;
		this.viewName = null;
		this.contextPath = null;
		this.context.clear();
		this.context = null;
		this.exception = null;

		attributes.clear();
		attributes = null;

		resultUI.clear();
		resultUI = null;

		paramUI.clear();
		paramUI = null;

		fkTargetFields.clear();
		fkTargetFields = null;

		fkFields.clear();
		fkFields = null;
	}

	protected BaseUIModel() {
		context = new HashMap();
		attributes = new Properties();
		resultUI = new ArrayList();
		paramUI = new ArrayList();
		fkTargetFields = new ArrayList();
		fkFields = new ArrayList();
	}

	public void addResultUI(String htmlID) {
		this.resultUI.add(htmlID);
	}

	public void addParamUI(String htmlID) {
		this.paramUI.add(htmlID);
	}

	public void addRefTargetField(String field) {
		this.fkTargetFields.add(field);
	}

	public void addRefField(String field) {
		this.fkFields.add(field);
	}

	public String getContentType() {
		return Const.CONTENT_TYPE_HTML;
	}

	public boolean isCachable() {
		return false;
	}

	public void render(Writer out) throws Exception {
		UIViews views = Cocit.me().getViews();
		String viewName = getViewName();
		UIView view = views.getView(viewName);

		if (view == null) {
			throw new CocException("MVC.render: view not be found! [viewName: %s, model: %s]", getViewName(), this.getClass().getSimpleName());
		}

		view.render(out, this);
	}

	/**
	 * 
	 * @param propName
	 * @param propValue
	 * @return
	 */
	public <T extends BaseUIModel> T set(String propName, Object propValue) {
		return put(propName, propValue);
	}

	public <T extends BaseUIModel> T put(String key, Object value) {
		if (key != null && value != null)
			context.put(key, value);

		return (T) this;
	}

	public <T extends BaseUIModel> T putAll(Map ctx) {
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

	public String getThemeName() {
		return themeName;
	}

	public <T extends BaseUIModel> T setThemeName(String themeName) {
		this.themeName = themeName;

		return (T) this;
	}

	public String getId() {
		return id;
	}

	public <T extends BaseUIModel> T setId(Long id) {
		if (id != null)
			this.id = id.toString();

		return (T) this;
	}

	public <T extends BaseUIModel> T setId(String id) {
		this.id = id;

		return (T) this;
	}

	@Override
	public Throwable getException() {
		return exception;
	}

	public <T extends BaseUIModel> T setException(Throwable exception) {
		this.exception = exception;

		return (T) this;
	}

	public boolean isAjax() {
		return ajax;
	}

	public <T extends BaseUIModel> T setAjax(boolean nested) {
		this.ajax = nested;

		return (T) this;
	}

	public String getTitle() {
		return title;
	}

	public <T extends BaseUIModel> T setTitle(String name) {
		this.title = name;

		return (T) this;
	}

	public String getViewName() {
		return viewName;
	}

	public <T extends BaseUIModel> T setViewName(String viewName) {
		this.viewName = viewName;

		return (T) this;
	}

	public String getContextPath() {
		return contextPath;
	}

	public <T extends BaseUIModel> T setContextPath(String contextPath) {
		this.contextPath = contextPath;

		return (T) this;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public Properties getAttributes() {
		return attributes;
	}

	public void setAttributes(Properties attrs) {
		if (attrs != null)
			this.attributes = attrs;
	}

	public void putAttribute(String key, String value) {
		this.attributes.setProperty(key, value);
	}

	public void putAttributes(Properties props) {
		this.attributes.putAll(props);
	}

	public String toString() {
		return this.title;
	}

	public List<String> getResultUI() {
		return resultUI;
	}

	public List<String> getParamUI() {
		return paramUI;
	}

	public List<String> getFkTargetFields() {
		return fkTargetFields;
	}

	public List<String> getFkFields() {
		return fkFields;
	}

	public void setResultUI(List<String> resultUI) {
    	this.resultUI = resultUI;
    }

}
