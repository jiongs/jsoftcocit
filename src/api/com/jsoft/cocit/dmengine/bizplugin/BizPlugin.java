package com.jsoft.cocit.dmengine.bizplugin;

/**
 * 
 * @author Ji Yongshan
 * @preserve all
 */
public abstract class BizPlugin<T> implements IBizPlugin<T> {

	public void beforeSubmit(BizEvent<T> event) {
	}

	public void afterSubmit(BizEvent<T> event) {
	}

	public void beforeLoad(BizEvent<T> event) {
	}

	public void afterLoad(BizEvent<T> event) {
	}

}
