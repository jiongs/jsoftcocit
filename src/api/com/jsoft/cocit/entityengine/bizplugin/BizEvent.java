// $codepro.audit.disable
package com.jsoft.cocit.entityengine.bizplugin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jsoft.cocit.orm.Orm;
import com.jsoft.cocit.orm.expr.Expr;

/**
 * 业务事件
 * 
 * @author yongshan.ji
 * @preserve all
 */
public class BizEvent<T> {
	private Map param = new HashMap();

	// 方法参数
	private T dataObject;

	private Class typeOfEntity;

	private Serializable dataID;

	private Expr expr;

	// 操作返回值
	private Object returnValue;

	private Orm orm;

	/**
	 * 获取事件实体对象，事件实体可以是单个数据实体、实体集合、查询分页等。
	 * 
	 * @return 实体对象：返回的实体对象可以时List也可以是具体的实体类对象。
	 */
	public T getDataObject() {
		return dataObject;
	}

	public void setDataObject(T entity) {
		this.dataObject = entity;
	}

	public Map getParam() {
		return param;
	}

	public void addParam(String key, Object value) {
		this.param.put(key, value);
	}

	public Class getTypeOfEntity() {
		return typeOfEntity;
	}

	public void setTypeOfEntity(Class klass) {
		this.typeOfEntity = klass;
	}

	public Expr getExpr() {
		return expr;
	}

	public void setExpr(Expr expr) {
		this.expr = expr;
	}

	public Serializable getDataID() {
		return dataID;
	}

	public void setDataID(Serializable id) {
		this.dataID = id;
	}

	public <X> X getReturnValue() {
		return (X) returnValue;
	}

	public void setReturnValue(Object resultEntity) {
		this.returnValue = resultEntity;
	}

	/**
	 * 获取实体管理器用到的ORM接口
	 * 
	 * @return ORM对象
	 */
	public Orm getOrm() {
		return orm;
	}

	public void setOrm(Orm orm) {
		this.orm = orm;
	}

}
